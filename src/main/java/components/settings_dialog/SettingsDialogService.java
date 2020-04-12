package components.settings_dialog;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import components.translation_file.TranslationFile;
import components.translation_file.TranslationFileListener;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;

@State(
  name = "SettingsDialog",
  storages = {
    @Storage(value = "translations-manager.xml")
  }
)
public class SettingsDialogService implements PersistentStateComponent<SettingsDialogState> {
  SettingsDialogState dialogSettingsState = new SettingsDialogState();

  @JvmStatic
  public static SettingsDialogService getInstance() {
    return ServiceManager.getService(SettingsDialogService.class);
  }

  @Nullable
  @Override
  public SettingsDialogState getState() {
    return dialogSettingsState;
  }

  @Override
  public void loadState(@NotNull SettingsDialogState state) {
    dialogSettingsState = state;
  }

  public void setFilesInState(Collection<VirtualFile> files, Project project) {
    System.out.println("12345");
    dialogSettingsState.selectedFiles = files;

    TranslationFile translationFile = new TranslationFile(project);

    System.out.println("FILES" + dialogSettingsState.selectedFiles);

    translationFile.registerAndNotify(files);

    VirtualFileManager
      .getInstance()
      .addVirtualFileListener(
        new TranslationFileListener(translationFile)
      );
  }
}
