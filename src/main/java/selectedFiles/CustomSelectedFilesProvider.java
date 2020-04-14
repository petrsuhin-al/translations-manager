package selectedFiles;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.spellchecker.dictionary.Dictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomSelectedFilesProvider {
  ExtensionPointName<CustomSelectedFilesProvider> EP_NAME = ExtensionPointName.create("selectedFiles.customSelectedFilesProvider");
  
  @Nullable
  Dictionary get(@NotNull String path);

  boolean isApplicable(@NotNull String path);
  
  @NotNull
  default String getSelectedFilesType() {
    return "";
  }
}
