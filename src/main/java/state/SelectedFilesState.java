package state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.util.xmlb.annotations.Transient;
import com.intellij.util.xmlb.annotations.XCollection;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import selectedFiles.EditableSelectedFiles;
import selectedFiles.UserSelectedFiles;
import java.util.Set;

@Tag("filesList")
public class SelectedFilesState implements PersistentStateComponent<SelectedFilesState> {
  public static final String NAME_ATTRIBUTE = "name";

  @XCollection(style = XCollection.Style.v2, elementName = "file", valueAttributeName = "")
  public Set<String> selectedFiles = new THashSet<>();

  @Attribute(NAME_ATTRIBUTE)
  public String name;

  @Transient
  private EditableSelectedFiles selectedFilesList;

  public SelectedFilesState() { }

  public SelectedFilesState(@NotNull EditableSelectedFiles filesList) {
    setSelectedFilesList(filesList);
  }

  @Transient
  public void setSelectedFilesList(@NotNull EditableSelectedFiles filesList) {
    this.selectedFilesList = filesList;
    this.name = filesList.getName();

    synchronizeSelectedFiles();
  }

  @Transient
  public EditableSelectedFiles getSelectedFilesList() {
    return selectedFilesList;
  }

  @Override
  public SelectedFilesState getState() {
    synchronizeSelectedFiles();
    return this;
  }

  private void synchronizeSelectedFiles() {
    if (selectedFilesList != null) {
      selectedFiles = new THashSet<>(selectedFilesList.getFiles());
    }
  }

  @Override
  public void loadState(@NotNull SelectedFilesState state) {
    if (state.name != null) {
      name = state.name;
    }

    selectedFiles = state.selectedFiles;

    retrieveSelectedFiles();
  }

  private void retrieveSelectedFiles() {
    assert name != null;
    selectedFilesList = new UserSelectedFiles(name);
    selectedFilesList.addToFilesList(selectedFiles);
  }

  @Override
  public String toString() {
    return "SelectedFilesState{ " + "selectedFiles = " + selectedFilesList + " }";
  }
}