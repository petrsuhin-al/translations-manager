import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class TranslationsManagerComponent implements ProjectComponent {
    private Project project;

    public TranslationsManagerComponent(Project project) {
        this.project = project;
    }

    @Override
    public void projectOpened() {
        TranslationFile translationFile = new TranslationFile(project);

        translationFile.registerAndNotify(
                FilenameIndex.getAllFilesByExt(
                        project,
                        "json",
                        GlobalSearchScope.allScope(project)
                )
        );

        VirtualFileManager.getInstance().addVirtualFileListener(
                new TranslationFileListener(translationFile)
        );
    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "TranslationFile";
    }
}