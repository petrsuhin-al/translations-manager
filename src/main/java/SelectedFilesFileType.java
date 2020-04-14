import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SelectedFilesFileType implements FileType {
  public static final SelectedFilesFileType INSTANCE = new SelectedFilesFileType();

  @NotNull
  @Override
  public String getName() {
    return "SelectedFiles";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Selected files";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "dic";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return null;
  }

  @Override
  public boolean isBinary() {
    return false;
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Nullable
  @Override
  public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
    return null;
  }
}
