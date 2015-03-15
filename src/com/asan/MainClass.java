package com.asan;

import com.asan.frontPages.generalForms.LoginForm;
import com.asan.frontPages.generalForms.StartForm;
import com.asan.JMXClasses.*;
import com.asan.NamesPkg.*;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainClass {

    public static Map<String, ServerConnectorMXBean> serverConnectorMXBeanHashMap = new HashMap<String, ServerConnectorMXBean>();
    public static Map<String, ClientsListenerMXBean> clientsListenerMXBeanHashMap = new HashMap<String, ClientsListenerMXBean>();
    public static Map<String, NiiMXBean> niiMXBeanHashMap = new HashMap<String, NiiMXBean>();
    public static ClientConnectionsManagerMXBean clientManager = new ClientConnectionsManagerMXBean();
    public static ServersConnectionManagerMXBean serversConnectionManager = new ServersConnectionManagerMXBean();
    public static LogMXBean logMXBean = new LogMXBean();
    public static NiiManagerMXBean niiManager = new NiiManagerMXBean();

    public static Map<String, ObjectName> objectNameMap = new HashMap<String, ObjectName>();

    static MBeanServerConnection mbeanConn = null;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

            LoginForm loginForm = new LoginForm();
            loginForm.setLocationRelativeTo(null);
            String[] address = loginForm.showDialog();

            //UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3].getClassName());
            String host = address[0];
            int port = Integer.parseInt(address[1]);
            String url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";
            JMXServiceURL serviceUrl = new JMXServiceURL(url);
            JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceUrl, null);
            mbeanConn = jmxConnector.getMBeanServerConnection();
            new MainClass().updateJmx(null, null);

            JFrame frame = new JFrame("سامانه نظارت");

            frame.setContentPane(new StartForm().panel1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMaximumSize(new Dimension(1020, 550));
            frame.setMinimumSize(new Dimension(1020, 550));
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
//            frame.pack();
            frame.setVisible(true);
            frame.setVisible(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void updateJmx(ObjectName name, QueryExp queryExp) {
        try {
            Set<ObjectInstance> beanSet = mbeanConn.queryMBeans(name, queryExp);
            for (ObjectInstance instance : beanSet) {
                ObjectName objectName = instance.getObjectName();

                String className = instance.getClassName();
                if (className.contains("com.asan")) {
                    String type = objectName.getKeyPropertyList().get("type").toString();
                    if (!objectNameMap.containsKey(type)) {
                        objectNameMap.put(type, objectName);
                    }
                    if (className.equals("com.asan.apcc.servers.NiiManager")) {

                        NiiManagerMXBean niiManagerMXBean = new NiiManagerMXBean();
                        niiManagerMXBean.NiiCount = (Integer) mbeanConn.getAttribute(objectName, NIIManager.attr_NiiCount);
                        niiManagerMXBean.NiiList_str = mbeanConn.getAttribute(objectName, NIIManager.attr_NiiList_str).toString();
                        niiManager = niiManagerMXBean;
                    }

                    if (className.equals("com.asan.apcc.servers.ServersConnectionManager")) {
                        ServersConnectionManagerMXBean servers = new ServersConnectionManagerMXBean();
                        servers.serverCount = (Integer) mbeanConn.getAttribute(objectName, ServerManager.attr_ServerCount);

                        serversConnectionManager = servers;
                    }

                    if (className.equals("com.asan.apcc.clients.ClientsConnectionManager")) {
                        ClientConnectionsManagerMXBean clients = new ClientConnectionsManagerMXBean();
                        clients.CurrentConnectionsCount = (Integer) mbeanConn.getAttribute(objectName, ClientManager.attr_CurrentConnectionsCount);
                        clients.SentPacketCount = (Long) mbeanConn.getAttribute(objectName, ClientManager.attr_ReceivedPacketCount);
                        clients.ReceivedPacketCount = (Long) mbeanConn.getAttribute(objectName, ClientManager.attr_SentPacketCount);

                        clientManager = clients;
                    }
                    if (className.equals("com.asan.apcc.management.LogMXBean")) {
                        LogMXBean log = new LogMXBean();
                        log.Enable_Client_Connect_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Client_Connect_Log);
                        log.Enable_Client_Disconnect_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Client_Disconnect_Log);
                        log.Enable_Client_Receive_Data_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Client_Receive_Data_Log);
                        log.Enable_Client_Receive_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Client_Receive_Log);
                        log.Enable_Client_Send_Error_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Client_Send_Error_Log);
                        log.Enable_Server_Receive_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Server_Receive_Log);
                        log.Enable_Server_Send_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Server_Send_Log);
                        log.Enable_Client_Send_Data_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Client_Send_Data_Log);
                        log.Enable_Client_Send_Log = (Boolean) mbeanConn.getAttribute(objectName, Logs.attr_Enable_Client_Send_Log);

                        logMXBean = log;
                    }
                    if (className.equals("com.asan.apcc.servers.Nii")) {
                        NiiMXBean niiMXBean = new NiiMXBean();
                        niiMXBean.NiiValue_hex = mbeanConn.getAttribute(objectName, NII.attr_NiiValue_hex).toString();
                        niiMXBean.ServerCount = (Integer) mbeanConn.getAttribute(objectName, NII.attr_ServerCount);
                        niiMXBean.ServerNames = mbeanConn.getAttribute(objectName, NII.attr_ServerNames).toString();

                        niiMXBeanHashMap.put(niiMXBean.NiiValue_hex, niiMXBean);
                    }
                    if (className.equals("com.asan.apcc.clients.SSLClientListener") || className.equals("com.asan.apcc.clients.ClientsListener")) {
                        ClientsListenerMXBean client = new ClientsListenerMXBean();
                        client.AcceptChannels = (Boolean) mbeanConn.getAttribute(objectName, Client.attr_AcceptChannels);
                        client.Active = (Boolean) mbeanConn.getAttribute(objectName, Client.attr_Active);
                        client.ActiveConnectionCount = (Integer) mbeanConn.getAttribute(objectName, Client.attr_ActiveConnectionCount);
                        client.Description = mbeanConn.getAttribute(objectName, Client.attr_Description).toString();
                        client.ListenPort = (Integer) mbeanConn.getAttribute(objectName, Client.attr_ListenPort);
                        client.Name = mbeanConn.getAttribute(objectName, Client.attr_Name).toString();
                        client.ReceivedPacketCount = (Long) mbeanConn.getAttribute(objectName, Client.attr_ReceivedPacketCount);
                        client.SentPacketCount = (Long) mbeanConn.getAttribute(objectName, Client.attr_SentPacketCount);
                        client.SocketReadTimeout_Second = (Integer) mbeanConn.getAttribute(objectName, Client.attr_SocketReadTimeout_Second);
                        client.ActiveConnectionCount = (Integer) mbeanConn.getAttribute(objectName, Client.attr_ActiveConnectionCount);
                        client.SSL = (Boolean) mbeanConn.getAttribute(objectName, Client.attr_SSL);

                        clientsListenerMXBeanHashMap.put(client.Name, client);
                    }
                    if (className.equals("com.asan.apcc.servers.ServerConnector")) {
                        ServerConnectorMXBean server = new ServerConnectorMXBean();
                        server.WriteBufferLowWaterMark = (Integer) mbeanConn.getAttribute(objectName, Server.attr_WriteBufferLowWaterMark);
                        server.WriteBufferHighWaterMark = (Integer) mbeanConn.getAttribute(objectName, Server.attr_WriteBufferHighWaterMark);
                        server.TotalSendCount = (Long) mbeanConn.getAttribute(objectName, Server.attr_TotalSendCount);
                        server.Writable = (Boolean) mbeanConn.getAttribute(objectName, Server.attr_Writable);
                        server.TotalReceivedCount = (Long) mbeanConn.getAttribute(objectName, Server.attr_TotalReceivedCount);
                        server.Port = (Integer) mbeanConn.getAttribute(objectName, Server.attr_Port);
                        server.Name = mbeanConn.getAttribute(objectName, Server.attr_Name).toString();
                        server.Ip = mbeanConn.getAttribute(objectName, Server.attr_Ip).toString();
                        server.AutoReconnectEnable = (Boolean) mbeanConn.getAttribute(objectName, Server.attr_AutoReconnectEnable);
                        server.Connected = (Boolean) mbeanConn.getAttribute(objectName, Server.attr_Connected);
                        server.ConnectionTryWaitTimeMillis = (Integer) mbeanConn.getAttribute(objectName, Server.attr_ConnectionTryWaitTimeMillis);
                        server.Enabled = (Boolean) mbeanConn.getAttribute(objectName, Server.attr_Enabled);

                        serverConnectorMXBeanHashMap.put(server.Name, server);
                    }
//                            Attribute nameMe = new Attribute("Message", "Hi again");
//                            mbeanConn.setAttribute(objectName, nameMe);
//                            mbeanConn.invoke(objectName, "sayHello", null, null);
                }
            }
            Object[] objects = serverConnectorMXBeanHashMap.values().toArray();
            System.out.println(objects.toString());

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void setJMXAttribute(ObjectName name, Attribute attribute) {
        try {
            mbeanConn.setAttribute(name, attribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callJmxFunction(ObjectName name, String OperationName, Object[] params, String[] signature) {
        try {
            mbeanConn.invoke(name, OperationName, params, signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
