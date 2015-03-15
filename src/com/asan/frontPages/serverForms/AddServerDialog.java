package com.asan.frontPages.serverForms;

import com.asan.NamesPkg.ServerManager;
import com.asan.MainClass;

import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddServerDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField Name;
    private JTextField IP;
    private JTextField Port;
    private JCheckBox Enabled;
    ObjectName objectName;

    public AddServerDialog(ObjectName objName) {
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        objectName = objName;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.pack();
        this.setResizable(false);

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
        Object[] params = {
                Name.getText(),
                IP.getText(),
                Integer.parseInt(Port.getText()),
                Enabled.isSelected()
        };
        String[] signature = {
                String.class.getName(),
                String.class.getName(),
                int.class.getName(),
                boolean.class.getName()
        };

        MainClass.callJmxFunction(objectName, ServerManager.func_addServer, params, signature);
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
