package com.asan.frontPages.serverForms;

import com.asan.JMXClasses.ServerConnectorMXBean;
import com.asan.NamesPkg.Server;
import com.asan.MainClass;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerInfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton disConnect;
    private JButton reset;
    private boolean status;

    private JButton connect;
    private JButton save;
    private JCheckBox autoReconnectEnable;
    private JTextField writeBufferHighWaterMark;
    private JTextField connectionTryWaitTimeMillis;
    private JTextField writeBufferLowWaterMark;
    private JLabel nameLabel;

    private String name;
    private ObjectName objName;

    public ServerInfoDialog(ServerConnectorMXBean server, ObjectName objectName) {
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(disConnect);

        objName = objectName;

        name = server.Name;
        nameLabel.setText(name);
        autoReconnectEnable.setSelected(server.AutoReconnectEnable);
        connectionTryWaitTimeMillis.setText(server.ConnectionTryWaitTimeMillis+"");
        writeBufferHighWaterMark.setText(server.WriteBufferHighWaterMark+"");
        writeBufferLowWaterMark.setText(server.WriteBufferLowWaterMark+"");

        disConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                status = true;
                MainClass.callJmxFunction(objName, Server.func_disconnect, null, null);
            }
        });

        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                status = true;
                MainClass.callJmxFunction(objName, Server.func_resetConnection, null, null);
            }
        });
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = true;
                MainClass.callJmxFunction(objName, Server.func_connect, null, null);
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = true;
                Attribute attribute = new Attribute(Server.attr_AutoReconnectEnable,Boolean.valueOf(autoReconnectEnable.isSelected()));
                MainClass.setJMXAttribute(objName,attribute);
                attribute = new Attribute(Server.attr_ConnectionTryWaitTimeMillis,Integer.valueOf(connectionTryWaitTimeMillis.getText()));
                MainClass.setJMXAttribute(objName,attribute);
                attribute = new Attribute(Server.attr_WriteBufferHighWaterMark,Integer.valueOf(writeBufferHighWaterMark.getText()));
                MainClass.setJMXAttribute(objName,attribute);
                attribute = new Attribute(Server.attr_WriteBufferLowWaterMark,Integer.valueOf(writeBufferLowWaterMark.getText()));
                MainClass.setJMXAttribute(objName,attribute);
                dispose();
            }
        });


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    public boolean showDialog(){
        setVisible(true);
        return status;
    }
}
