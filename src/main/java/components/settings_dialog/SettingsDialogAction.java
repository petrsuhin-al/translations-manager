package components.settings_dialog;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import components.translation_file.TranslationFile;
import components.translation_file.TranslationFileListener;
import org.jetbrains.annotations.NotNull;

public class SettingsDialogAction extends AnAction {
  Project project;
    @Override
    public void actionPerformed(
      @NotNull AnActionEvent actionEvent
    ) {
        assert actionEvent.getProject() != null;
        project = actionEvent.getProject();

        SettingsDialogWrapper settingsDialog = new SettingsDialogWrapper(project);

        if (settingsDialog.showAndGet()) {
           settingsDialog.close(23);

           SettingsDialogState state = SettingsDialogService.getInstance().dialogSettingsState;

           TranslationFile translationFile = new TranslationFile(project);

           System.out.println("FILES" + state.selectedFiles);

           translationFile.registerAndNotify(state.selectedFiles);

           VirtualFileManager
             .getInstance()
             .addVirtualFileListener(
               new TranslationFileListener(translationFile)
             );
        }
    }
}
