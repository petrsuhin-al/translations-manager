package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import ui.SettingsDialogWrapper;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {
        System.out.println("ACTION");
        if (new SettingsDialogWrapper(actionEvent.getProject()).showAndGet()) {
            System.out.println();
        }
    }
}
