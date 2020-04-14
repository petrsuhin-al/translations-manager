package selectedFiles;

import gnu.trove.THashSet;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ProjectSelectedFiles implements EditableSelectedFiles {
  @NonNls
  private static final String DEFAULT_CURRENT_USER_NAME = "default.user";
  private static final String DEFAULT_PROJECT_SELECTED_FILES_NAME = "project";
  private String activeName;
  private Set<EditableSelectedFiles> selectedFiles;

  public ProjectSelectedFiles() { }

  public ProjectSelectedFiles(@NotNull Set<EditableSelectedFiles> selectedFiles) {
    this.selectedFiles = selectedFiles;
  }

  @NotNull
  @Override
  public String getName() {
    return DEFAULT_PROJECT_SELECTED_FILES_NAME;
  }

  public void setActiveName(String name) {
    activeName = name;
  }

  @Override
  @Nullable
  public Boolean contains(@NotNull String fileName) {
    if (selectedFiles == null) {
      return null;
    }
    int errors = 0;
    for (SelectedFiles file : selectedFiles) {
      Boolean contains = file.contains(fileName);

      if (contains == null) {
        errors++;
      } else if (contains) {
        return true;
      }
    }

    if ( errors == selectedFiles.size()) return null;
    return false;
  }

  @Override
  public void addToFilesList(String file) {
    getActiveSelectedFiles().addToFilesList(file);
  }

  @Override
  public void removeFromFilesList(String word) {
    getActiveSelectedFiles().removeFromFilesList(word);
  }

  @NotNull
  private EditableSelectedFiles getActiveSelectedFiles() {
    return ensureCurrentUserSelectedFiles();
  }

  @NotNull
  private EditableSelectedFiles ensureCurrentUserSelectedFiles() {
    if (activeName == null) {
      activeName = DEFAULT_CURRENT_USER_NAME;
    }

    EditableSelectedFiles result = getSelectedFileByName(activeName);

    if (result == null) {
      result = new UserSelectedFiles(activeName);

      if (selectedFiles == null) {
        selectedFiles = new THashSet<>();
      }

      selectedFiles.add(result);
    }

    return result;
  }

  @Nullable
  private EditableSelectedFiles getSelectedFileByName(@NotNull String name) {
    if (selectedFiles == null) {
      return null;
    }

    EditableSelectedFiles result = null;

    for (EditableSelectedFiles file : selectedFiles) {
      if (file.getName().equals(name)) {
        result = file;
        break;
      }
    }

    return result;
  }

  @Override
  public void replaceAll(@Nullable Collection<String> files) {
    getActiveSelectedFiles().replaceAll(files);
  }

  @Override
  public void clear() {
    getActiveSelectedFiles().clear();
  }


  @Override
  @NotNull
  public Set<String> getFiles() {
    if (selectedFiles == null) {
      return Collections.emptySet();
    }

    Set<String> files = new THashSet<>();

    for (SelectedFiles file : selectedFiles) {
      Set<String> otherWords = file.getFiles();
      files.addAll(otherWords);
    }

    return files;
  }

  @Override
  @NotNull
  public Set<String> getEditableFiles() {
    return getActiveSelectedFiles().getFiles();
  }


  @Override
  public void addToFilesList(@Nullable Collection<String> words) {
    getActiveSelectedFiles().addToFilesList(words);
  }

  public Set<EditableSelectedFiles> getSelectedFiles() {
    return selectedFiles;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ProjectSelectedFiles that = (ProjectSelectedFiles) o;

    return Objects.equals(selectedFiles, that.selectedFiles);
  }

  @Override
  public int hashCode() {
    int result = activeName != null ? activeName.hashCode() : 0;
    result = 31 * result + (selectedFiles != null ? selectedFiles.hashCode() : 0);
    return result;
  }

  @NonNls
  @Override
  public String toString() {
    return "ProjectSelectedFiles { " + "activeName = '" + activeName + '\'' + ", selectedFiles = " + selectedFiles + " }";
  }
}