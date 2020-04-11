package ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckboxTree;
import com.intellij.util.IconUtil;
import javax.swing.*;

public class FileTreeRenderer extends CheckboxTree.CheckboxTreeCellRenderer {
  @Override
  public void customizeRenderer(
    final JTree tree, final Object value, final boolean selected,
    final boolean expanded, final boolean leaf, final int row,
    final boolean hasFocus
  ) {
    if (!(value instanceof FileTreeNode)) {
      return;
    }

    final FileTreeNode node = (FileTreeNode) value;
    final VirtualFile file = node.getFile();
    final Project project = node.getProject();

    getTextRenderer().append(file.getName());
    getTextRenderer().setIcon(IconUtil.getIcon(file, Iconable.ICON_FLAG_READ_STATUS, project));
  }
}