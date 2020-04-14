import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import components.translation_file.TranslationFile;
import components.translation_file.TranslationFileListener;
import org.jetbrains.annotations.NotNull;
import state.ProjectSelectedFilesState;
import utils.SelectedVFUtil;
import java.util.Collection;
import java.util.Set;

public class TranslationsManager implements ProjectComponent {
  private final Project project;

  public TranslationsManager(Project project) {
    this.project = project;
  }

  @Override
  public void projectOpened() {
    String currentUser = System.getProperty("user.name");

    final ProjectSelectedFilesState selectedFilesState = ProjectSelectedFilesState.getInstance(project);
    System.out.println("IN INIT " + selectedFilesState);
    Set<String> selectedFilesPaths = selectedFilesState.getUserSelectedFiles(currentUser);

    Collection<VirtualFile> selectedVirtualFiles = SelectedVFUtil.getVirtualFilesByPaths(selectedFilesPaths);

    TranslationFile translationFile = new TranslationFile(project);
    translationFile.registerAndNotify(selectedVirtualFiles);

    VirtualFileManager
      .getInstance()
      .addVirtualFileListener(
        new TranslationFileListener(translationFile)
      );
  }

  @Override
  public void projectClosed() { }

  @Override
  public void initComponent() { }

  @Override
  public void disposeComponent() { }

  @NotNull
  @Override
  public String getComponentName() {
    return "TranslationManager";
  }
}