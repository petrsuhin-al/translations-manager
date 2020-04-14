package selectedFiles;

import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface SelectedFiles {
  default void consumeSuggestions(@NotNull String file, @NotNull Consumer<String> consumer) {}

  @NotNull
  String getName();

  @Nullable
  Boolean contains(@NotNull String file);

  @NotNull
  Set<String> getFiles();
}
