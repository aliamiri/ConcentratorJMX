package com.asan.frontPages.niiForms;

import com.asan.NamesPkg.NII;
import com.asan.NamesPkg.NIIManager;
import com.asan.MainClass;

import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddNiiDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    Object[] servers;
    private JTextField jNiiName;
    private JList list1;
    int niiName;
    ObjectName objectName;

    public AddNiiDialog(ObjectName objName,Object[] serverNames) {
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        objectName = objName;
        servers= serverNames;
        list1.setListData(serverNames);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        int selectedIndex = list1.getSelectedIndex();

        String text = jNiiName.getText();
        niiName = Integer.parseInt(text);

        Object[] params = {
                niiName
        };
        String[] signature = {
                int.class.getName()
        };
        MainClass.callJmxFunction(objectName, NIIManager.func_addNii, params, signature);

        if(selectedIndex!=-1) {
            Object element = list1.getModel().getElementAt(selectedIndex);
            params[0] = element;
            signature[0] = String.class.getName();

            MainClass.updateJmx(null, null);

            ObjectName objectName1 = MainClass.objectNameMap.get(niiName + "");

            MainClass.callJmxFunction(objectName1, NII.func_addConnector, params, signature);
        }
        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
