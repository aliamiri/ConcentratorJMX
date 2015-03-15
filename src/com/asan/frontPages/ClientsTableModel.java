package com.asan.frontPages;

import javax.swing.table.DefaultTableModel;

public class ClientsTableModel extends DefaultTableModel {

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 7 || columnIndex == 8|| columnIndex == 9)
            return Boolean.class;
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 3 || column == 7)
            return true;
        return false;
    }

    public ClientsTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
}
