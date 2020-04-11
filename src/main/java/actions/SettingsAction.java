package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import components.settings_dialog.SettingsDialogWrapper;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(
      @NotNull AnActionEvent actionEvent
    ) {
        assert actionEvent.getProject() != null;
        SettingsDialogWrapper settingsDialog = new SettingsDialogWrapper(actionEvent.getProject());

        if (settingsDialog.showAndGet()) {
           settingsDialog.close(23);
        }
    }
}
