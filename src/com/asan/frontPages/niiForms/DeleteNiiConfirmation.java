package com.asan.frontPages.niiForms;

import com.asan.NamesPkg.NIIManager;
import com.asan.MainClass;

import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteNiiConfirmation extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel label1;
    private boolean ret;
    private int niiName;
    ObjectName objectName;

    public DeleteNiiConfirmation(int nii, ObjectName objName) {
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        pack();

        setResizable(false);

        niiName = nii;
        objectName = objName;

        String text = "آیا از حذف" + niiName + "مطمئن هستید؟";

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
                niiName
        };
        String[] signature = {
                int.class.getName()
        };

        MainClass.callJmxFunction(objectName, NIIManager.func_removeNii, params, signature);
        ret = true;
        dispose();
    }

    public boolean showDialog() {
        setVisible(true);
        return ret;
    }

    private void onCancel() {
        dispose();
    }
}
