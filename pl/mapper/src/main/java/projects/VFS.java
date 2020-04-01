/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package projects;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VFS extends VirtualFile {
    @NotNull
    @Override
    public String getName() {
        return null;
    }

    @NotNull
    @Override
    public VirtualFileSystem getFileSystem() {
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }

    @NotNull
    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public boolean isWritable() {
        return false;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Nullable
    @Override
    public VirtualFile getParent() {
        return null;
    }

    @Override
    public VirtualFile[] getChildren() {
        return new VirtualFile[0];
    }

    @Override
    public OutputStream getOutputStream(Object o, long l, long l1) throws IOException {
        return null;
    }

    @Override
    public byte[] contentsToByteArray() throws IOException {
        return new byte[0];
    }

    @Override
    public long getTimeStamp() {
        return 0;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public void refresh(boolean b, boolean b1, Runnable runnable) {

    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }
}
