package settings;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.tree.TreeUtil;
import constants.Constants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import selectedFiles.EditableSelectedFiles;
import selectedFiles.ProjectSelectedFiles;
import selectedFiles.UserSelectedFiles;
import settings.templates.FileTreeNode;
import settings.templates.FileTreeRenderer;
import state.ProjectSelectedFilesState;
import java.util.regex.Pattern;
import utils.SelectedVFUtil;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SettingsDialogWrapper extends DialogWrapper {
    @NotNull private final Project project;
    @NotNull private final Collection<VirtualFile> files;
    @NotNull private final Map<VirtualFile, FileTreeNode> nodes = new HashMap<>();

    private String currentUser;
    private FileTreeNode root;
    private Collection<VirtualFile> initStateFiles = new ArrayList<>();

    public SettingsDialogWrapper(
      @NotNull Project providedProject
    ) {
        super(providedProject, true);

        project = providedProject;
        currentUser = System.getProperty("user.name");

        setBaseDir();
        setInitStateFiles();

        files = getAllAvailableFiles(
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
        treePanel.setBorder(IdeBorderFactory.createTitledBorder("Select files for manage:", false));

        return dialogPanel;
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        Collection<VirtualFile> checkedFiles = getCheckedFiles();
        Collection<String> filesPaths = new ArrayList<>();

        checkedFiles.forEach(file -> filesPaths.add(file.toString()));

        final ProjectSelectedFilesState selectedFilesState = ProjectSelectedFilesState.getInstance(project);
        EditableSelectedFiles userSelectedFiles = new UserSelectedFiles(currentUser);
        userSelectedFiles.replaceAll(filesPaths);

        ProjectSelectedFiles projectDictionary = new ProjectSelectedFiles(Collections.singleton(userSelectedFiles));

        System.out.println("IN WRAP " + projectDictionary);
        projectDictionary.setActiveName(currentUser);

        selectedFilesState.setProjectSelectedFiles(projectDictionary);

        return super.getOKAction();
    }

    private void setInitStateFiles() {
        final ProjectSelectedFilesState selectedFilesState = ProjectSelectedFilesState.getInstance(project);
        Set<String> selectedFilesPaths = selectedFilesState.getUserSelectedFiles(currentUser);

        initStateFiles = SelectedVFUtil.getVirtualFilesByPaths(selectedFilesPaths);
    }

    private void setBaseDir() {
        VirtualFile baseDir = SelectedVFUtil.getBaseDirLikeVirtualFile(project.getPresentableUrl());

        if (baseDir != null) {
            root = createDirectoryNodes(baseDir);
        } else {
            close(CANCEL_EXIT_CODE);
        }
    }

    @NotNull
    private FileTreeNode createDirectoryNodes(@NotNull VirtualFile file) {
        final FileTreeNode node = nodes.get(file);

        if (node != null) {
            return node;
        }

        final FileTreeNode newNode = new FileTreeNode(project, file);

        boolean isChecked = initStateFiles != null && initStateFiles.contains(file);
        newNode.setChecked(isChecked);

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

        CheckboxTree tree = new CheckboxTree(renderer, root);

        tree.getEmptyText()
          .setText("Translation files not found")
          .setIsVerticalFlow(true);

        if (!root.isLeaf()) {
            tree.setRootVisible(true);
        }

        tree.setCellRenderer(renderer);
        tree.setShowsRootHandles(false);
        TreeUtil.installActions(tree);
        TreeUtil.expandAll(tree);

        final JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(tree);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    @NotNull
    private Collection<VirtualFile> getCheckedFiles() {
        final Collection<VirtualFile> result = new ArrayList<>();

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
        String regexp = String.valueOf(
          this.getRegExp(Constants.excludedFiles)) +
          '|' +
          this.getRegExp(Constants.excludedFolders
        );

        return Pattern.compile(regexp);
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
}
