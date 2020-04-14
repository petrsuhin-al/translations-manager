package state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.EventDispatcher;
import com.intellij.util.xmlb.annotations.Property;
import com.intellij.util.xmlb.annotations.Transient;
import com.intellij.util.xmlb.annotations.XCollection;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import selectedFiles.EditableSelectedFiles;
import selectedFiles.ProjectSelectedFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@State(
  name = "ProjectSelectedFilesState",
  storages = @Storage(value = "translationsManager", stateSplitter = ProjectSelectedFilesStateSplitter.class)
)
public class ProjectSelectedFilesState implements PersistentStateComponent<ProjectSelectedFilesState> {
  @Property(surroundWithTag = false)
  @XCollection(elementTypes = SelectedFilesState.class)
  public List<SelectedFilesState> selectedFilesListsStates = new ArrayList<>();

  private ProjectSelectedFiles projectSelectedFiles;

  private final EventDispatcher<SelectedFilesStateListener> listenerEventDispatcher = EventDispatcher.create(SelectedFilesStateListener.class);

  public ProjectSelectedFilesState() { }

  public static ProjectSelectedFilesState getInstance(Project project) {
    return ServiceManager.getService(project, ProjectSelectedFilesState.class);
  }

  @Transient
  public void setProjectSelectedFiles(ProjectSelectedFiles projectSelectedFiles) {
    selectedFilesListsStates.clear();

    Set<EditableSelectedFiles> projectSelectedFilesList = projectSelectedFiles.getSelectedFiles();

    System.out.println(projectSelectedFilesList);
    if (projectSelectedFilesList != null) {
      for (EditableSelectedFiles filesList : projectSelectedFilesList) {
        selectedFilesListsStates.add(new SelectedFilesState(filesList));
      }
    }

    retrieveProjectSelectedFilesLists();
  }

  @Transient
  public Set<String> getUserSelectedFiles(String name) {
    Set<String> selectedFiles = new THashSet<>();

    for (SelectedFilesState userSelectedFiles : selectedFilesListsStates) {
      if (userSelectedFiles.name.equals(name)) {
        selectedFiles.addAll(userSelectedFiles.selectedFiles);
      }
    }

    return selectedFiles;
  }

  @Transient
  public ProjectSelectedFiles getProjectSelectedFiles() {
    if (projectSelectedFiles == null) {
      projectSelectedFiles = new ProjectSelectedFiles();
    }

    return projectSelectedFiles;
  }

  @Override
  public ProjectSelectedFilesState getState() {
    if (projectSelectedFiles != null) {
      System.out.println("HERE " + projectSelectedFiles);
      //ensure all dictionaries within project dictionary will be stored
      setProjectSelectedFiles(projectSelectedFiles);
    }

    return this;
  }

  @Override
  public void loadState(@NotNull ProjectSelectedFilesState state) {
    this.selectedFilesListsStates = state.selectedFilesListsStates;
    retrieveProjectSelectedFilesLists();
  }

  public void retrieveProjectSelectedFilesLists() {
    Set<EditableSelectedFiles> selectedFiles = new THashSet<>();

    if (selectedFilesListsStates != null) {
      for (SelectedFilesState selectedFilesState : selectedFilesListsStates) {
        selectedFilesState.loadState(selectedFilesState);
        selectedFiles.add(selectedFilesState.getSelectedFilesList());
      }
    }

    projectSelectedFiles = new ProjectSelectedFiles(selectedFiles);
//    listenerEventDispatcher.getMulticaster().dictChanged(projectSelectedFiles);
  }

  @Override
  public String toString() {
    return "ProjectSelectedFilesState{ " + "projectSelectedFiles = " + projectSelectedFiles + " }";
  }

  public void addProjectDictListener(SelectedFilesStateListener listener) {
    listenerEventDispatcher.addListener(listener);
  }
}
