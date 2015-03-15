package com.asan.frontPages;

import javax.swing.table.DefaultTableModel;

public class ServersTableModel extends DefaultTableModel {

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3 || columnIndex==4 || columnIndex == 7)
            return Boolean.class;
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0 || column == 4 || column == 5 || column == 6 || column == 7)
            return false;
        return true;
    }

    public ServersTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
}
