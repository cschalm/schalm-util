package org.schalm.util.helper.swing;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Simple superclass for <code>TableModels</code> uses in swing JTables.
 *
 * @author <a href="mailto:cschalm@users.sourceforge.net">Carsten Schalm</a>
 * @version $Id: SimpleTableModel.java 158 2014-03-01 22:22:07Z cschalm $
 * @param <T> type of contents
 */
public abstract class SimpleTableModel<T> extends AbstractTableModel {
    private static final long serialVersionUID = 4350792443522685912L;
    private List<String> columnNames = new ArrayList<>();
    private final List<T> dataVector = new ArrayList<>();

    public SimpleTableModel() {
        super();
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public void addRow(T entry) {
        dataVector.add(entry);
        fireTableDataChanged();
    }

    public void addAllRows(List<T> entries) {
        dataVector.addAll(entries);
        fireTableDataChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        if (columnNames == null) {
            return 0;
        }
        return columnNames.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        if (dataVector == null) {
            return 0;
        }
        return dataVector.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(int col) {
        if (columnNames == null) {
            return "";
        }
        return columnNames.get(col);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(int c) {
        if (dataVector == null || dataVector.isEmpty()) {
            return String.class;
        }
        return getValueAt(0, c).getClass();
    }

    public void removeAllRows() {
        if (dataVector == null) {
            return;
        }
        int size = dataVector.size();
        if (size > 0) {
            dataVector.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }

    public void removeRow(int row) {
        if (dataVector == null) {
            return;
        }
        dataVector.remove(row);
        fireTableDataChanged();
    }

    public int getRowIndex(T match) {
        if (dataVector == null) {
            return -1;
        }
        int result = 0;
        for (T entry : dataVector) {
            if (entry.equals(match)) {
                return result;
            }
            result++;
        }
        return -1;
    }

    public T getRow(int index) {
        return dataVector.get(index);
    }

    public boolean contains(T checkFor) {
        return dataVector.contains(checkFor);
    }

}
