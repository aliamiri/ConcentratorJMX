package com.asan.frontPages.serverForms;

import com.asan.NamesPkg.ServerManager;
import com.asan.MainClass;

import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteServerConfirmation extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel label1;
    ObjectName objectName;
    boolean ret;
    String serverName;

    public DeleteServerConfirmation(String server, ObjectName objName) {
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        this.pack();

        this.setResizable(false);

        serverName = server;
        objectName = objName;

        String text = "آیا از حذف" + serverName + "مطمئن هستید؟";

        label1.setText(text);

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
                serverName
        };
        String[] signature = {
                String.class.getName()
        };

        MainClass.callJmxFunction(objectName, ServerManager.func_removeServer, params, signature);

        ret = true;
        dispose();
    }

    public boolean showDialog() {
        setVisible(true);
        return ret;
    }

    private void onCancel() {
        ret = false;
        dispose();
    }
}
