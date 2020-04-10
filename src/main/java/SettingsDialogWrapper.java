import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileContentsChangedAdapter;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckedTreeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

public class SettingsDialogWrapper extends DialogWrapper {
    public Collection<VirtualFile> files;
    public CheckedTreeNode checkboxTreeNode;

    public SettingsDialogWrapper(
      @Nullable Project project
    ) {
        super(project, true);

        init();
        setTitle("Translations Manager Settings");

        assert project != null;
        this.files = this.setAllAvailableFiles(
          FilenameIndex.getAllFilesByExt(
            project,
            "json",
            GlobalSearchScope.allScope(project)
          )
        );

        System.out.println(this.files);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());

        dialogPanel.setPreferredSize(new Dimension(400, 200));

        dialogPanel.add(jCheckBox("ru.json"), BorderLayout.BEFORE_FIRST_LINE);
        dialogPanel.add(jCheckBox("en.json"), BorderLayout.AFTER_LAST_LINE);

        return dialogPanel;
    }

    private JCheckBox jCheckBox(@NotNull String text) {
        String htmlString = "<html>" + text + "</html>";

        return new JCheckBox(htmlString);
    }

    private void addCheckboxNode(String name, VirtualFile file) {
        CheckedTreeNode f = new CheckedTreeNode();
    }

    private Collection<VirtualFile> setAllAvailableFiles(Collection<VirtualFile> files) {
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

    private Pattern createExcludedPattern() {
        StringBuilder regexp = new StringBuilder()
          .append(this.getRegExp(Constants.excludedFiles))
          .append('|')
          .append(this.getRegExp(Constants.excludedFolders));

        System.out.println(regexp);
        return Pattern.compile(regexp.toString());
    }

    private StringBuilder getRegExp(String[] values) {
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
