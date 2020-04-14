package selectedFiles;

import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

public interface Loader {
  void load(@NotNull Consumer<String> consumer);

  String getName();
}
