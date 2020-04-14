package state;

import selectedFiles.EditableSelectedFiles;
import java.util.EventListener;

public interface SelectedFilesStateListener extends EventListener {
  void dictChanged(EditableSelectedFiles selectedFiles);
}
