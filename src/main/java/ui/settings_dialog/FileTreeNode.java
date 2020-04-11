package ui.settings_dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckedTreeNode;
import org.jetbrains.annotations.NotNull;

public class FileTreeNode extends CheckedTreeNode {
  @NotNull
  private final VirtualFile file;

  @NotNull
  private final Project project;

  public FileTreeNode(
    @NotNull Project project, @NotNull VirtualFile file
  ) {
    super(file);
    this.project = project;
    this.file = file;
  }

  @NotNull
  public Project getProject() {
    return project;
  }

  @NotNull
  public VirtualFile getFile() {
    return file;
  }
}
