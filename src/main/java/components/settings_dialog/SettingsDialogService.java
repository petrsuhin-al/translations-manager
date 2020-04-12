package components.settings_dialog;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import kotlin.jvm.JvmStatic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
}
