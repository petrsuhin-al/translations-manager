package selectedFiles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public interface EditableSelectedFiles extends SelectedFiles {
  void addToFilesList(@Nullable String file);

  void removeFromFilesList(@Nullable String file);

  void addToFilesList(@Nullable Collection<String> files);

  void replaceAll(@Nullable Collection<String> files);

  void clear();

  @NotNull
  Set<String> getEditableFiles();
}
