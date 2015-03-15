package com.asan.frontPages;

import javax.swing.table.DefaultTableModel;

public class NiisTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public NiisTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
}
