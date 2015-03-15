package com.asan.frontPages.niiForms;

import com.asan.NamesPkg.NII;
import com.asan.MainClass;

import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

public class ServerOperations extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private boolean status;

    private DefaultListModel model = new DefaultListModel();

    private JList<String> source;
    private JList<String> target;
    private JTextArea textArea;


    private String currServer;
    private ObjectName objectName;

    public ServerOperations(Object[] serverNames, ObjectName objName, String currentServer) {
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        if (currentServer != null && currentServer.length() != 0) {
            currServer = currentServer.substring(0, currentServer.length() - 1);
            model.addElement(currServer);
        }
        objectName = objName;

        String[] sourceData = new String[serverNames.length];
        for (int i = 0; i < serverNames.length; i++) {
            sourceData[i] = serverNames[i].toString();
        }

        source.setListData(sourceData);

        target.setModel(model);

//        sourceData = new String[1];
//        sourceData[0] = currServer;



//        target.setListData(sourceData);
        source.setDragEnabled(true);
        target.setDragEnabled(true);
        target.setDropMode(DropMode.ON_OR_INSERT);

        source.setTransferHandler(new ExportTransferHandler());
        target.setTransferHandler(new ImportTransferHandler());

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        ListModel<String> model1 = target.getModel();
        if (model1.getSize() == 0) {
            if (currServer != null && currServer != "") {
                Object[] params = {
                        currServer
                };

                String[] signature = {
                        String.class.getName()
                };
                MainClass.callJmxFunction(objectName, NII.func_removeConnector, params, signature);
                status = true;
            }
        } else {
            String s = model1.getElementAt(0).toString();
            if (currServer != null && currServer != "") {
                if (!s.equals(currServer)) {
                    Object[] params = {
                            currServer
                    };
                    String[] signature = {
                            String.class.getName()
                    };
                    MainClass.callJmxFunction(objectName, NII.func_removeConnector, params, signature);
                    params[0] = s;
                    MainClass.callJmxFunction(objectName, NII.func_addConnector, params, signature);
                    status = true;
                }
            } else {
                Object[] params = {
                        s
                };
                String[] signature = {
                        String.class.getName()
                };
                MainClass.callJmxFunction(objectName, NII.func_addConnector, params, signature);
                status = true;
            }
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public boolean showDialog() {
        setVisible(true);
        return status;
    }

    private class ExportTransferHandler extends TransferHandler {
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        public Transferable createTransferable(JComponent c) {
            return new StringSelection(source.getSelectedValue());
        }
    }

    public class ImportTransferHandler extends TransferHandler {
        private int[] indices = null;

        int count = 0;

        public boolean canImport(TransferHandler.TransferSupport info) {
            if (!info.isDataFlavorSupported(DataFlavor.stringFlavor) || target.getModel().getSize() == 1) {
                return false;
            }
            return true;
        }

        protected Transferable createTransferable(JComponent c) {
            JList list = (JList) c;
            indices = list.getSelectedIndices();
            Object[] values = list.getSelectedValues();

            StringBuffer buff = new StringBuffer();

            for (int i = 0; i < values.length; i++) {
                Object val = values[i];
                buff.append(val == null ? "" : val.toString());
                if (i != values.length - 1) {
                    buff.append("\n");
                }
            }

            return new StringSelection(buff.toString());
        }

        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE;
        }

        public boolean importData(TransferHandler.TransferSupport info) {
            if (!info.isDrop()) {
                return false;
            }
            JList list = (JList) info.getComponent();
            DefaultListModel listModel = (DefaultListModel) list.getModel();
            JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
            int index = dl.getIndex();
            boolean insert = dl.isInsert();

            Transferable t = info.getTransferable();
            String data;
            try {
                data = (String) t.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            String[] values = data.split("\n");

            for (int i = 0; i < values.length; i++) {
                if (insert) {
                    listModel.add(index++, values[i]);
                } else {
                    if (index < listModel.getSize()) {
                        listModel.set(index++, values[i]);
                    } else {
                        listModel.add(index++, values[i]);
                    }
                }
            }
            return true;
        }

        protected void exportDone(JComponent c, Transferable data, int action) {
            JList source = (JList) c;
            DefaultListModel listModel = (DefaultListModel) source.getModel();

            if (action == TransferHandler.MOVE) {
                for (int i = indices.length - 1; i >= 0; i--) {
                    listModel.remove(indices[i]);
                }
            }

            indices = null;
        }
    }
}

