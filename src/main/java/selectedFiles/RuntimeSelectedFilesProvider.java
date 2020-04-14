package selectedFiles;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.spellchecker.dictionary.Dictionary;

public interface RuntimeSelectedFilesProvider {
  ExtensionPointName<RuntimeSelectedFilesProvider> EP_NAME = ExtensionPointName.create("selectedFiles.runtimeSelectedFilesProvider");

  Dictionary[] getSelectedFiles();
}
