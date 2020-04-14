package state;

import com.intellij.openapi.components.StateSplitterEx;
import com.intellij.openapi.util.Pair;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProjectSelectedFilesStateSplitter extends StateSplitterEx {
  @NotNull
  @Override
  public List<Pair<Element, String>> splitState(@NotNull Element state) {
    return splitState(state, SelectedFilesState.NAME_ATTRIBUTE);
  }
}
