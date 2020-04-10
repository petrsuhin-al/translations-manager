package ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.event.SelectionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vcs.VcsBundle;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileContentsChangedAdapter;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.*;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.EditableModel;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.ui.table.JBTable;
import ui.Constants;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

public class SettingsDialogWrapper extends DialogWrapper {
    public Collection<VirtualFile> files;
    public CheckedTreeNode checkboxTreeNode;
    public JBTable table;

    private static final int FILE_NAME_COLUMN = 0;
    private static final int SELECTED_COLUMN = 1;

    public SettingsDialogWrapper(
      @Nullable Project project
    ) {
        super(project, true);

        assert project != null;
        this.files = this.getAllAvailableFiles(
          FilenameIndex.getAllFilesByExt(
            project,
            "json",
            GlobalSearchScope.allScope(project)
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

//        GridBag gridBag = new GridBag()
//          .setDefaultInsets(JBUI.insets(1, 1, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
//          .setDefaultWeightX(1.0)
//          .setDefaultFill(GridBagConstraints.HORIZONTAL);

//        for (VirtualFile file : this.files) {
//            dialogPanel.add(jCheckBox(file.getName()), gridBag.nextLine().next().weightx(0.5));
//        }

        JPanel myProcessorTablePanel = new JPanel(new BorderLayout());
        SettingsTableModel myProcessorsModel = new SettingsTableModel();
        myProcessorTablePanel.setBorder(IdeBorderFactory.createTitledBorder("Annotation Processors", false));
        JBTable myProcessorTable = new JBTable(myProcessorsModel);
        myProcessorTable.getEmptyText().setText("Compiler will run all automatically discovered processors");
        JPanel myProcessorPanel = createTablePanel(myProcessorTable);
        myProcessorTablePanel.add(myProcessorPanel, BorderLayout.CENTER);

        dialogPanel.add(myProcessorTablePanel);

        return dialogPanel;
    }

    private static JPanel createTablePanel(final JBTable table) {
        return ToolbarDecorator.createDecorator(table)
          .disableUpAction()
          .disableDownAction()
          .setAddAction(new AnActionButtonRunnable() {
              @Override
              public void run(AnActionButton anActionButton) {
                  final TableCellEditor cellEditor = table.getCellEditor();
                  if (cellEditor != null) {
                      cellEditor.stopCellEditing();
                  }
                  final TableModel model = table.getModel();
                  ((EditableModel)model).addRow();
                  TableUtil.editCellAt(table, model.getRowCount() - 1, 0);
              }
          })
          .createPanel();
    }

    private JCheckBox jCheckBox(@NotNull String text) {
        String htmlString = "<html>" + text + "</html>";
        return new JCheckBox(htmlString, AllIcons.FileTypes.Json);
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

        System.out.println(regexp);
        return Pattern.compile(regexp.toString());
    }

    @NotNull
    private StringBuilder getRegExp(@NotNull String[] values) {
        StringBuilder regexp = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                regexp.append("|");
            }

            regexp.append("(?=.*").append(values[i]).append(")");
        }

        return regexp;
    }
}