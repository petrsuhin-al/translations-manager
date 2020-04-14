package selectedFiles;

import gnu.trove.THashSet;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Set;

public class UserSelectedFiles implements EditableSelectedFiles {
  private final String name;

  @NotNull
  private final Set<String> selectedFiles = new THashSet<>();

  public UserSelectedFiles(@NotNull String name) {
    this.name = name;
  }

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  @Override
  @Nullable
  public Boolean contains(@NotNull String word) {
    boolean contains = selectedFiles.contains(word);
    if (contains) return true;
    return null;
  }

  @NotNull
  @Override
  public Set<String> getFiles() {
    return selectedFiles;
  }

  @Override
  @NotNull
  public Set<String> getEditableFiles() {
    return selectedFiles;
  }

  @Override
  public void clear() {
    selectedFiles.clear();
  }

  @Override
  public void addToFilesList(String file) {
    if (file == null) {
      return;
    }

    selectedFiles.add(file);
  }

  @Override
  public void removeFromFilesList(String file) {
    if (file == null) {
      return;
    }

    selectedFiles.remove(file);
  }

  @Override
  public void replaceAll(@Nullable Collection<String> files) {
    clear();
    addToFilesList(files);
  }

  @Override
  public void addToFilesList(@Nullable Collection<String> files) {
    if (files == null || files.isEmpty()) {
      return;
    }

    for (String file : files) {
      addToFilesList(file);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserSelectedFiles that = (UserSelectedFiles) o;

    return name.equals(that.name);

  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @NonNls
  @Override
  public String toString() {
    return "UserSelectedFiles { " + "name = '" + name + '\'' + ", files.count = " + selectedFiles.size() + " }";
  }
}
