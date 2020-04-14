package components.translation_file;

import com.intellij.openapi.extensions.ExtensionPointName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TranslationFileProvider {
  ExtensionPointName<TranslationFileProvider> EP_NAME = ExtensionPointName.create("components.translation_file.TranslationFileProvider");

  @Nullable
  TranslationFile get(@NotNull String path);

  boolean isApplicable(@NotNull String path);

  @NotNull
  default String getDictionaryType() {
    return "";
  }
}
