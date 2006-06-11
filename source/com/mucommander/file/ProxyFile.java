package com.mucommander.file;

import com.mucommander.file.filter.FileFilter;
import com.mucommander.file.filter.FilenameFilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ProxyFile is an {@link AbstractFile} that acts as a proxy between the class that extends it
 * and the proxied <code>AbstractFile</code> instance specified to the constructor.
 * All <code>AbstractFile</code> methods (abstract of not) are proxied and delegated to the proxied file.
 * The {@link #getProxiedFile()} method allows to retrieve the proxied file.
 *
 * <p>This class is useful for wrapper files, such as {@link AbstractArchiveFile archive files}, that need
 * an existing <code>AbstractFile</code> instance (the proxied file) to provide additional functionalities.
 * By overriding/implementing every <code>AbstractFile</code> methods, <code>ProxyFile</code> ensures that
 * all <code>AbstractFile</code> methods can be safely used, even if they are overridden by the proxied
 * instance's class.
 *
 * <p><b>Implementation note:</b> the <code>java.lang.reflect.Proxy</code> class can unfortunately not be
 * used as it only works with interfaces (not abstract class). There doesn't seem to be any dynamic way to
 * proxy method invokations, so any modifications made to {@link AbstractFile} must be also reflected
 * in <code>ProxyFile</code>.
 *
 * @see AbstractArchiveFile
 * @author Maxence Bernard
 */
public abstract class ProxyFile extends AbstractFile {

    /** Proxied file */
    protected AbstractFile file;

    /** Custom URL for this ProxyFile, may be null */
    protected FileURL customURL;


    /**
     * Creates a new ProxyFile using the given file to delegate AbstractFile method calls to.
     *
     * @param file the file to be proxied
     */
    public ProxyFile(AbstractFile file) {
        super(file.getURL());
        this.file = file;
    }


    /**
     * Returns the <code>AbstractFile</code> instance proxied by this </code>ProxyFile</code>.
     */
    public AbstractFile getProxiedFile() {
        return file;
    }


    /////////////////////////////////
    // AbstractFile implementation //
    /////////////////////////////////

    public long getDate() {
        return file.getDate();
    }

    public boolean changeDate(long lastModified) {
        return file.changeDate(lastModified);
    }

    public long getSize() {
        return file.getSize();
    }

    public AbstractFile getParent() {
        return file.getParent();
    }

    public void setParent(AbstractFile parent) {
        file.setParent(parent);
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean canRead() {
        return file.canRead();
    }

    public boolean canWrite() {
        return file.canWrite();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isSymlink() {
        return file.isSymlink();
    }

    public AbstractFile[] ls() throws IOException {
        return file.ls();
    }

    public void mkdir(String name) throws IOException {
        file.mkdir(name);
    }

    public InputStream getInputStream() throws IOException {
        return file.getInputStream();
    }

    public OutputStream getOutputStream(boolean append) throws IOException {
        return file.getOutputStream(append);
    }

    public void delete() throws IOException {
        file.delete();
    }

    public long getFreeSpace() {
        return file.getFreeSpace();
    }

    public long getTotalSpace() {
        return file.getTotalSpace();
    }

    ////////////////////////
    // Overridden methods //
    ////////////////////////

    public FileURL getURL() {
        return file.getURL();
    }

    public String getName() {
        return file.getName();
    }

    public String getNameWithoutExtension() {
        return file.getNameWithoutExtension();
    }

    public String getExtension() {
        return file.getExtension();
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public String getAbsolutePath(boolean appendSeparator) {
        return file.getAbsolutePath(appendSeparator);
    }

    public String getCanonicalPath() {
        return file.getCanonicalPath();
    }

    public String getCanonicalPath(boolean appendSeparator) {
        return file.getCanonicalPath(appendSeparator);
    }

    public String getSeparator() {
        return file.getSeparator();
    }

    public boolean isParentOf(AbstractFile file) {
        return file.isParentOf(file);
    }

    public boolean isBrowsable() {
        return file.isBrowsable();
    }

    public boolean isHidden() {
        return file.isHidden();
    }

    public AbstractFile getRoot() {
        return file.getRoot();
    }

    protected String addTrailingSeparator(String path) {
        return file.addTrailingSeparator(path);
    }

    protected String removeTrailingSlash(String path) {
        return file.removeTrailingSlash(path);
    }

    public InputStream getInputStream(long skipBytes) throws IOException {
        return file.getInputStream(skipBytes);
    }

    public void copyStream(InputStream in, boolean append) throws IOException {
        file.copyStream(in, append);
    }

    public void copyTo(AbstractFile destFile) throws IOException {
        file.copyTo(destFile);
    }

    public int getCopyToHint(AbstractFile destFile) {
        return file.getCopyToHint(destFile);
    }

    public void moveTo(AbstractFile destFile) throws IOException {
        file.moveTo(destFile);
    }

    public int getMoveToHint(AbstractFile destFile) {
        return file.getMoveToHint(destFile);
    }

    public AbstractFile[] ls(FileFilter filter) throws IOException {
        return file.ls(filter);
    }

    public AbstractFile[] ls(FilenameFilter filter) throws IOException {
        return file.ls(filter);
    }

    public boolean equals(Object f) {
        return file.equals(f);
    }

    public String toString() {
        return file.toString();
    }
}
