/*
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2010 Maxence Bernard
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mucommander.ui.viewer.text;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentListener;

import com.mucommander.file.AbstractFile;
import com.mucommander.file.FileOperation;
import com.mucommander.io.EncodingDetector;
import com.mucommander.io.RandomAccessInputStream;
import com.mucommander.io.bom.BOMInputStream;
import com.mucommander.text.Translator;
import com.mucommander.ui.dialog.DialogOwner;
import com.mucommander.ui.dialog.InformationDialog;
import com.mucommander.ui.encoding.EncodingListener;
import com.mucommander.ui.encoding.EncodingMenu;
import com.mucommander.ui.helper.MenuToolkit;
import com.mucommander.ui.helper.MnemonicHelper;
import com.mucommander.ui.viewer.FileViewer;


/**
 * A simple text viewer. Most of the implementation is located in {@link TextEditorImpl}.
 *
 * @author Maxence Bernard
 */
class TextViewer extends FileViewer implements EncodingListener {

    private TextEditorImpl textEditorImpl;
    
    /** Menu items */
    // Menus //
    private JMenu editMenu;
    // Items //
    private JMenuItem copyItem;
    private JMenuItem selectAllItem;
    private JMenuItem findItem;
    private JMenuItem findNextItem;
    private JMenuItem findPreviousItem;
    
    private String encoding;
    
    public TextViewer() {
    	textEditorImpl = new TextEditorImpl(false);

    	// Edit menu
    	editMenu = new JMenu(Translator.get("text_editor.edit"));
    	MnemonicHelper menuItemMnemonicHelper = new MnemonicHelper();

    	copyItem = MenuToolkit.addMenuItem(editMenu, Translator.get("text_editor.copy"), menuItemMnemonicHelper, null, this);

    	selectAllItem = MenuToolkit.addMenuItem(editMenu, Translator.get("text_editor.select_all"), menuItemMnemonicHelper, null, this);
    	editMenu.addSeparator();

    	findItem = MenuToolkit.addMenuItem(editMenu, Translator.get("text_viewer.find"), menuItemMnemonicHelper, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), this);
    	findNextItem = MenuToolkit.addMenuItem(editMenu, Translator.get("text_viewer.find_next"), menuItemMnemonicHelper, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), this);
    	findPreviousItem = MenuToolkit.addMenuItem(editMenu, Translator.get("text_viewer.find_previous"), menuItemMnemonicHelper, KeyStroke.getKeyStroke(KeyEvent.VK_F3, KeyEvent.SHIFT_DOWN_MASK), this);
    }
    
    void startEditing(AbstractFile file, DocumentListener documentListener) throws IOException {
//        this.file = file;

        // Auto-detect encoding

        // Get a RandomAccessInputStream on the file if possible, if not get a simple InputStream
        InputStream in = null;

        try {
            if(file.isFileOperationSupported(FileOperation.RANDOM_READ_FILE)) {
                try { in = file.getRandomAccessInputStream(); }
                catch(IOException e) {
                    // In that case we simply get an InputStream
                }
            }

            if(in==null)
                in = file.getInputStream();

            String encoding = EncodingDetector.detectEncoding(in);
            // If the encoding could not be detected or the detected encoding is not supported, default to UTF-8
            if(encoding==null || !Charset.isSupported(encoding))
                encoding = "UTF-8";

            if(in instanceof RandomAccessInputStream) {
                // Seek to the beginning of the file and reuse the stream
                ((RandomAccessInputStream)in).seek(0);
            }
            else {
                // TODO: it would be more efficient to use some sort of PushBackInputStream, though we can't use PushBackInputStream because we don't want to keep pushing back for the whole InputStream lifetime

                // Close the InputStream and open a new one
                // Note: we could use mark/reset if the InputStream supports it, but it is almost never implemented by
                // InputStream subclasses and a broken by design anyway.
                in.close();
                in = file.getInputStream();
            }

            // Load the file into the text area
            loadDocument(in, encoding, documentListener);
        }
        finally {
            if(in != null) {
                try {in.close();}
                catch(IOException e) {
                    // Nothing to do here.
                }
            }
        }
    }

    void loadDocument(InputStream in, String encoding, DocumentListener documentListener) throws IOException {
        this.encoding = encoding;

        // If the encoding is UTF-something, wrap the stream in a BOMInputStream to filter out the byte-order mark
        // (see ticket #245)
        if(encoding.toLowerCase().startsWith("utf")) {
            in = new BOMInputStream(in);
        }

        Reader isr = new BufferedReader(new InputStreamReader(in, encoding));

        textEditorImpl.read(isr);
        
        // Listen to document changes
        if(documentListener!=null)
            textEditorImpl.addDocumentListener(documentListener);
    }

    @Override
    public JMenuBar getMenuBar() {
    	JMenuBar menuBar = super.getMenuBar();
    	
    	// Encoding menu
    	EncodingMenu encodingMenu = new EncodingMenu(new DialogOwner(getFrame()), encoding);
        encodingMenu.addEncodingListener(this);

        menuBar.add(editMenu);
        menuBar.add(encodingMenu);
        
        return menuBar;
    }

    ///////////////////////////////
    // FileViewer implementation //
    ///////////////////////////////

    @Override
    public void view(AbstractFile file) throws IOException {
        startEditing(file, null);
    }
    
    @Override
	public JComponent getViewedComponent() {
		return textEditorImpl.getTextArea();
	}
    
    ///////////////////////////////////
    // ActionListener implementation //
    ///////////////////////////////////

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == copyItem)
        	textEditorImpl.copy();
        else if(source == selectAllItem)
        	textEditorImpl.selectAll();
        else if(source == findItem)
        	textEditorImpl.find();
        else if(source == findNextItem)
        	textEditorImpl.findNext();
        else if(source == findPreviousItem)
        	textEditorImpl.findPrevious();
        else
        	super.actionPerformed(e);
    }

    /////////////////////////////////////
    // EncodingListener implementation //
    /////////////////////////////////////

    public void encodingChanged(Object source, String oldEncoding, String newEncoding) {

    	try {
    		// Reload the file using the new encoding
    		// Note: loadDocument closes the InputStream
    		loadDocument(getCurrentFile().getInputStream(), newEncoding, null);
    	}
    	catch(IOException ex) {
    		InformationDialog.showErrorDialog(getFrame(), Translator.get("read_error"), Translator.get("file_editor.cannot_read_file", getCurrentFile().getName()));
    	}   
    }
}
