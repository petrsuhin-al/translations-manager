package components.translation_file;

import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.NotNull;

public class TranslationFileListener implements VirtualFileListener {
    private TranslationFile translationFile;

    public TranslationFileListener(TranslationFile translationFile) {
        this.translationFile = translationFile;
    }

    @Override
    public void propertyChanged(@NotNull VirtualFilePropertyEvent event) {
        if(event.getPropertyName().equals(VirtualFile.PROP_NAME)) {
            if(event.getFileName().equals("en.json")) {
                translationFile.registerAndNotify(event.getFile());
            }

            if(event.getOldValue().equals("en.json")) {
                String path = event.getFile().getPath().substring(0, event.getFile().getPath().length() - ((String)event.getNewValue()).length())+event.getOldValue();
                VirtualFile file = new FakeVirtualFile(path);
                translationFile.removeAndNotify(file);
            }
        }
    }

    @Override
    public void fileCreated(@NotNull VirtualFileEvent event) {
        if(event.getFileName().equals("en.json")) {
            translationFile.registerAndNotify(event.getFile());
        }
    }

    @Override
    public void fileDeleted(@NotNull VirtualFileEvent event) {
        if(event.getFileName().equals("en.json")) {
            translationFile.removeAndNotify(event.getFile());
        }
    }

    @Override
    public void fileMoved(@NotNull VirtualFileMoveEvent event) {
        translationFile.moveAndNotify(
                new FakeVirtualFile(event.getOldParent().getPath()+"/"+event.getFileName()),
                event.getFile()
        );
    }

    @Override
    public void fileCopied(@NotNull VirtualFileCopyEvent event) {
        if(event.getFileName().equals("project.json")) {
            translationFile.registerAndNotify(event.getFile());
        }
    }
}