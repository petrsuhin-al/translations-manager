package ui;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ui.EditableModel;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class SettingsTableModel extends AbstractTableModel implements EditableModel {
  private final List<String> myRows = new ArrayList<String>();

  public String getColumnName(int column) {
    if (column == 0) {
      return "File Name";
    }

    return super.getColumnName(column);
  }

  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public int getRowCount() {
    return myRows.size();
  }

  public int getColumnCount() {
    return 1;
  }

  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columnIndex == 0;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    if (columnIndex == 0) {
      return myRows.get(rowIndex);
    }

    return null;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (aValue != null && columnIndex == 0) {
      myRows.set(rowIndex, (String) aValue);
    }
  }

  public void removeRow(int idx) {
    myRows.remove(idx);
    fireTableRowsDeleted(idx, idx);
  }

  @Override
  public void exchangeRows(int oldIndex, int newIndex) { }

  @Override
  public boolean canExchangeRows(int oldIndex, int newIndex) {
    return false;
  }

  public void addRow() {
    myRows.add("");
    final int index = myRows.size() - 1;
    fireTableRowsInserted(index, index);
  }

  public void setFiles(Collection<VirtualFile> files) {
    clear();
    if (!files.isEmpty()) {
      for (VirtualFile file : files) {
        myRows.add(file.getName());
      }

      myRows.sort(String::compareToIgnoreCase);
      fireTableRowsInserted(0, files.size() - 1);
    }
  }

  public void clear() {
    final int count = myRows.size();

    if (count > 0) {
      myRows.clear();
      fireTableRowsDeleted(0, count - 1);
    }
  }

  public Collection<String> getFiles() {
    final Set<String> set = new HashSet<String>();

    for (String row : myRows) {
      if (row != null) {
        set.add(row.trim());
      }
    }

    set.remove("");
    return set;
  }
}
