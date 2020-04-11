package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import ui.settings_dialog.SettingsDialogWrapper;

import java.util.Objects;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {

        if (new SettingsDialogWrapper(Objects.requireNonNull(actionEvent.getProject())).showAndGet()) {
            System.out.println("OK");
        }
    }
}
