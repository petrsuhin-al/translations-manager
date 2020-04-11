package ui;

import com.intellij.ide.DefaultTreeExpander;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vcs.VcsRoot;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.tree.TreeModelAdapter;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import java.util.List;
import java.util.regex.Pattern;
import com.intellij.ui.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SettingsDialogWrapper extends DialogWrapper {
    @NotNull private final Project project;
    @NotNull private final Collection<VirtualFile> files;
    private FileTreeNode root;
    @NotNull private final Map<VirtualFile, FileTreeNode> nodes = ContainerUtil.newHashMap();

    private Editor commands;
    private Document commandsDocument;
    private CheckboxTree tree;
    private DefaultTreeExpander treeExpander;

    @NotNull
    private TreeModelListener treeModelListener = new TreeModelAdapter() {
        @Override
        public void treeNodesChanged(@NotNull TreeModelEvent event) {
            ApplicationManager
              .getApplication()
              .runWriteAction(() -> commandsDocument.setText(text));
        }
    };

    public SettingsDialogWrapper(
      @NotNull Project providedProject
    ) {
        super(providedProject, true);

        project = providedProject;
        root = createDirectoryNodes(providedProject.getBaseDir());

        files = this.getAllAvailableFiles(
          FilenameIndex.getAllFilesByExt(
            providedProject,
            "json",
            GlobalSearchScope.allScope(providedProject)
          )
        );

        init();
        setTitle("Translations Manager Settings");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        dialogPanel.setPreferredSize(JBUI.size(300, 500));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

        final JPanel treePanel = new JPanel(new BorderLayout());
        dialogPanel.add(treePanel, BorderLayout.CENTER);


        JScrollPane treeScrollPanel = createTreeScrollPanel();
        treePanel.add(treeScrollPanel, BorderLayout.CENTER);
        treePanel.setBorder(IdeBorderFactory.createTitledBorder("Select files for manage", false));

        return dialogPanel;
    }

    @NotNull
    private FileTreeNode createDirectoryNodes(@NotNull VirtualFile file) {
        final FileTreeNode node = nodes.get(file);

        if (node != null) {
            return node;
        }

        final FileTreeNode newNode = new FileTreeNode(project, file);
        nodes.put(file, newNode);

        if (nodes.size() != 1) {
            final VirtualFile parent = file.getParent();
            if (parent != null) {
                createDirectoryNodes(parent).add(newNode);
            }
        }

        return newNode;
    }

    private JScrollPane createTreeScrollPanel() {
        for (VirtualFile entry : files) {
            createDirectoryNodes(entry);
        }

        final FileTreeRenderer renderer = new FileTreeRenderer();

        tree = new CheckboxTree(renderer, root);

        tree.getEmptyText()
          .setText("Translation files not found")
          .setIsVerticalFlow(true);

        if (!root.isLeaf()) {
            tree.setRootVisible(true);
        }

        tree.setCellRenderer(renderer);
        tree.setShowsRootHandles(false);
        TreeUtil.installActions(tree);

        final JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(tree);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        TreeUtil.expandAll(tree);

        tree.getModel().addTreeModelListener(treeModelListener);
        treeExpander = new DefaultTreeExpander(tree);

        return scrollPane;
    }

    @NotNull
    private Collection<VirtualFile> getCheckedFiles() {
        final Collection<VirtualFile> result = new ArrayList<VirtualFile>();

        FileTreeNode leaf = (FileTreeNode) root.getFirstLeaf();

        if (leaf == null) return result;

        do {
            if (!leaf.isChecked()) continue;

            final VirtualFile file = leaf.getFile();

            result.add(file);
        } while ((leaf = (FileTreeNode) leaf.getNextLeaf()) != null);

        return result;
    }

    @NotNull
    @Contract("_ -> param1")
    private Collection<VirtualFile> getAllAvailableFiles(@NotNull Collection<VirtualFile> files) {
        Iterator<VirtualFile> filesIterator = files.iterator();

        Pattern pattern = createExcludedPattern();

        while (filesIterator.hasNext()) {
            String str = filesIterator.next().toString();

            if (pattern.matcher(str).find()) {
                filesIterator.remove();
            }
        }

        return files;
    }

    @NotNull
    private Pattern createExcludedPattern() {
        StringBuilder regexp = new StringBuilder()
          .append(this.getRegExp(Constants.excludedFiles))
          .append('|')
          .append(this.getRegExp(Constants.excludedFolders));

        return Pattern.compile(regexp.toString());
    }

    @NotNull
    private StringBuilder getRegExp(@NotNull String[] values) {
        StringBuilder regexp = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                regexp.append('|');
            }

            regexp.append("(?=.*").append(values[i]).append(")");
        }

        return regexp;
    }

    @NotNull
    private VirtualFile getRootVirtualFile() {
        String basePath = project.getBasePath();

        assert basePath != null;
        return VirtualFileManager.getInstance().findFileByUrl(project.getBasePath());
    }
}