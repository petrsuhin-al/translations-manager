import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class SettingsAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent actionEvent) {

        if (new SettingsDialogWrapper(actionEvent.getProject()).showAndGet()) {
            System.out.println();
        }
    }
}
