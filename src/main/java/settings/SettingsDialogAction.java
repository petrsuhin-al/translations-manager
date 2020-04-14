package settings;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SettingsDialogAction extends AnAction {
  @Override
  public void actionPerformed(
    @NotNull AnActionEvent actionEvent
  ) {
    assert actionEvent.getProject() != null;
    final Project project = actionEvent.getProject();

    SettingsDialogWrapper settingsDialog = new SettingsDialogWrapper(project);

    if (settingsDialog.showAndGet()) {
      settingsDialog.close(0);
    }
  }
}
