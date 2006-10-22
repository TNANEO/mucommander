package com.mucommander.ui.action;

import com.mucommander.file.FileSet;
import com.mucommander.file.filter.ArchiveFileKeeper;
import com.mucommander.ui.MainFrame;
import com.mucommander.ui.UnpackDialog;

/**
 * This action pops up the 'Unpack files' dialog that allows to unpack the currently marked files.
 *
 * @author Maxence Bernard
 */
public class UnpackAction extends SelectedFilesAction {

    public UnpackAction(MainFrame mainFrame) {
        super(mainFrame);

        setSelectedFileFilter(new ArchiveFileKeeper());
    }

    public void performAction() {
        FileSet files = mainFrame.getLastActiveTable().getSelectedFiles();
        if(files.size()>0)
            new UnpackDialog(mainFrame, files, false);
    }
}
