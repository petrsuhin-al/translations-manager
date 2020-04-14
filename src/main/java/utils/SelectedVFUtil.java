package utils;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class SelectedVFUtil {
  @NotNull
  public static Collection<VirtualFile> getVirtualFilesByPaths(Set<String> paths) {
    Collection<VirtualFile> virtualFiles = new ArrayList<>();

    for (String path : paths) {
      try {
        VirtualFile virtualFile = VfsUtil.findFileByURL(new URL(path));

        if (virtualFile != null && virtualFile.exists()) {
          virtualFiles.add(virtualFile);
        }
      } catch (MalformedURLException ex) {
        ex.printStackTrace();
      }
    }

    return virtualFiles;
  }

  @Nullable
  public static VirtualFile getBaseDirLikeVirtualFile(String path) {
    VirtualFile file = null;

    try {
      file = VfsUtil.findFileByURL(new URL("file://" + path));
    } catch (MalformedURLException ex) {
      ex.printStackTrace();
    }

    if (file != null && file.isDirectory()) {
      return file;
    } else {
      return null;
    }
  }
}
