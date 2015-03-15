package com.asan.frontPages;

import com.asan.NamesPkg.Client;
import com.asan.NamesPkg.ClientManager;
import com.asan.NamesPkg.Logs;
import com.asan.frontPages.niiForms.AddNiiDialog;
import com.asan.frontPages.niiForms.DeleteNiiConfirmation;
import com.asan.frontPages.niiForms.ServerOperations;
import com.asan.frontPages.serverForms.AddServerDialog;
import com.asan.frontPages.serverForms.DeleteServerConfirmation;
import com.asan.frontPages.serverForms.ServerInfoDialog;
import com.asan.JMXClasses.*;
import com.asan.MainClass;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SampleForm {

    public JPanel panel1;
    Map<String, ObjectName> objectNames = new HashMap<String, ObjectName>();

    //region client parameters
    private JLabel recievedPacketCount;
    private JButton resetCounterbutton;
    private JLabel sendPacketCount;
    private JLabel currentConnectionCount;
    private JButton stopListenbutton;
    private JTable clientTable;

    String[] clientColumnNameEng = {
            "Name",
            "Description",
            "ListenPort",
            "SocketReadTimeout_Second",
            "ReceivedPacketCount",
            "SentPacketCount",
            "ActiveConnectionCount",
            "AcceptChannels",
            "SSL",
            "Active"
    };
    String[] clientColumnNameFa = {
            "نام",
            "توضیحات",
            "پرت گوش دادن",
            "تایم اوت خواندن",
            "تعداد بسته های دریافتی",
            "تعداد بسته های دریافتی",
            "تعداد کانکشن فعال",
            "AcceptChannels",
            "SSL",
            "فعال؟"
    };
    ClientsTableModel clientsTableModel = new ClientsTableModel(null, clientColumnNameFa);
    private Map<String, ClientsListenerMXBean> listenerMXBeanMap = new HashMap<String, ClientsListenerMXBean>();
    //endregion

    //region nii parameters
    private JTable niiTable;
    private JButton addNII;
    private JButton removeNii;
    private JButton serverConfigurations;
    private Map<String, NiiMXBean> niiMXBeanHashMap = new HashMap<String, NiiMXBean>();


    String[] niiColumnNameEng = {
            "NiiValue_hex",
            "ServerNames",
            "ServerCount"
    };
    String[] niiColumnNameFa = {
            "شماره ی Nii",
            "نام سرورها",
            "تعداد سرورها"
    };
    NiisTableModel niisTableModel = new NiisTableModel(null, niiColumnNameFa);
    //endregion

    //region server Parameters
    private JButton deleteServerButton;
    private JTable serversTable;
    private JButton viewServerButton;
    private JButton addServer;
    private JLabel connectedServerCount;
    private JButton resetClientCounterbutton;
    private JButton startListenbutton;
    private JButton restartServerSocketbutton;
    private Map<String, ServerConnectorMXBean> serverConnectorMap = new HashMap<String, ServerConnectorMXBean>();
    String[] serverColumnNameEng = {"Name",
            "Ip",
            "Port",
            "Enabled",
            "Connected",
            "TotalReceiveCount",
            "TotalSendCount",
            "Writable"};

    static String[] serverColumnNameFa = {"نام",
            "Ip",
            "Port",
            "فعال",
            "متصل",
            "تعداد رسیده ها",
            "تعداد ارسالی",
            "قابل نوشتن"};
    ServersTableModel serversTableModel = new ServersTableModel(null, serverColumnNameFa);
    //endregion

    //region log parameters
    private JCheckBox clientSendLogCheck;
    private JCheckBox clientConnectLogCheck;
    private JCheckBox sendErrorLogCheck;
    private JCheckBox clientDisconnectLogCheck;
    private JCheckBox sendDataLogCheck;
    private JCheckBox serverRecieveLogCheck;
    private JCheckBox clientRecieveDataCheck;
    private JCheckBox clientRecieveLogCheck;
    private JCheckBox serverSendLogCheck;
    private JButton enableAllLogs;
    private JButton DisableAllLogs;
    //endregion

    public SampleForm() {
        panel1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        objectNames = MainClass.objectNameMap;
        serverInit();
        niiInit();
        clientInit();
        logInit();
    }

    //region log functions
    private void logInit() {

        final ObjectName objectName = objectNames.get("log");

        setLogCheckBoxesStatus();

        clientSendLogCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Client_Send_Log, Boolean.valueOf(clientSendLogCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });
        clientConnectLogCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Client_Connect_Log, Boolean.valueOf(clientConnectLogCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });
        sendErrorLogCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Client_Send_Error_Log, Boolean.valueOf(sendErrorLogCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });
        sendDataLogCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Client_Send_Data_Log, Boolean.valueOf(sendDataLogCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });
        clientDisconnectLogCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Client_Disconnect_Log, Boolean.valueOf(clientDisconnectLogCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });
        serverRecieveLogCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Server_Receive_Log, Boolean.valueOf(serverRecieveLogCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });
        clientRecieveDataCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Client_Receive_Data_Log, Boolean.valueOf(clientRecieveDataCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });
        clientRecieveLogCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Client_Receive_Log, Boolean.valueOf(clientRecieveLogCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });
        serverSendLogCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Attribute attribute = new Attribute(Logs.attr_Enable_Server_Send_Log, Boolean.valueOf(serverSendLogCheck.isSelected()));
                MainClass.setJMXAttribute(objectName, attribute);
            }
        });

        enableAllLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainClass.callJmxFunction(objectName, Logs.func_enableAllLogs, null, null);
                MainClass.updateJmx(objectName,null);
                setLogCheckBoxesStatus();
            }
        });
        DisableAllLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainClass.callJmxFunction(objectName, Logs.func_disableAllLogs, null, null);
                MainClass.updateJmx(objectName,null);
                setLogCheckBoxesStatus();
            }
        });

    }

    private void setLogCheckBoxesStatus() {
        clientSendLogCheck.setSelected(MainClass.logMXBean.Enable_Client_Send_Log);
        clientConnectLogCheck.setSelected(MainClass.logMXBean.Enable_Client_Connect_Log);
        sendErrorLogCheck.setSelected(MainClass.logMXBean.Enable_Client_Send_Error_Log);
        clientDisconnectLogCheck.setSelected(MainClass.logMXBean.Enable_Client_Disconnect_Log);
        sendDataLogCheck.setSelected(MainClass.logMXBean.Enable_Client_Send_Data_Log);
        serverRecieveLogCheck.setSelected(MainClass.logMXBean.Enable_Server_Receive_Log);
        clientRecieveDataCheck.setSelected(MainClass.logMXBean.Enable_Client_Receive_Data_Log);
        clientRecieveLogCheck.setSelected(MainClass.logMXBean.Enable_Client_Receive_Log);
        serverSendLogCheck.setSelected(MainClass.logMXBean.Enable_Server_Send_Log);
    }
    //endregion

    //region client functions
    private void clientInit() {
        updateClientTable();

        clientTable.setModel(clientsTableModel);

        clientTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        clientTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        clientTable.getColumnModel().getColumn(1).setPreferredWidth(210);
        clientTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        clientTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        clientTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        clientTable.getColumnModel().getColumn(5).setPreferredWidth(130);
        clientTable.getColumnModel().getColumn(6).setPreferredWidth(110);
        clientTable.getColumnModel().getColumn(7).setPreferredWidth(90);
        clientTable.getColumnModel().getColumn(8).setPreferredWidth(35);
        clientTable.getColumnModel().getColumn(9).setPreferredWidth(50);

        clientsTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateClientTable(e);
            }
        });

        resetClientCounterbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientFunctionCall(Client.func_resetCounters);
            }
        });
        resetCounterbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectName objectName = objectNames.get("client");
                MainClass.callJmxFunction(objectName, ClientManager.func_resetCounters, null, null);
            }
        });

        stopListenbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientFunctionCall(Client.func_stopListen);
            }
        });

        startListenbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientFunctionCall(Client.func_startListen);
            }
        });
        restartServerSocketbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clientFunctionCall(Client.func_restartServerSocket);
            }
        });

        updateScheduler();
    }

    private void clientFunctionCall(String function) {
        int selectedRow = clientTable.getSelectedRow();
        String key = clientsTableModel.getValueAt(selectedRow, 0).toString();
        ObjectName objectName = objectNames.get(key);
        MainClass.callJmxFunction(objectName, function, null, null);
    }

    private void updateClientTable(TableModelEvent e) {
        int changedColumn = e.getColumn();
        int changedRow = e.getFirstRow();
        if (changedColumn != -1 && changedRow != -1)
            changeAttr(changedRow, changedColumn, 1);
    }

    private void updateClientTable() {
        MainClass.updateJmx(null, null);
        Object[] objects = updateClientBean().values().toArray();
        for (Object obj : objects) {
            ClientsListenerMXBean client = (ClientsListenerMXBean) obj;
            Object[] row = createDataForClientTable(client);
            clientsTableModel.addRow(row);
        }
    }

    private Map<String, ClientsListenerMXBean> updateClientBean() {
        Map<String, ClientsListenerMXBean> newClients = new HashMap<String, ClientsListenerMXBean>();
        for (String key : MainClass.clientsListenerMXBeanHashMap.keySet()) {
            if (!listenerMXBeanMap.containsKey(key)) {
                listenerMXBeanMap.put(key, MainClass.clientsListenerMXBeanHashMap.get(key));
                newClients.put(key, MainClass.clientsListenerMXBeanHashMap.get(key));
            }
        }
        return newClients;
    }

    private Object[] createDataForClientTable(ClientsListenerMXBean client) {
        Object[] row = new Object[10];
        row[0] = client.Name;
        row[1] = client.Description;
        row[2] = client.ListenPort;
        row[3] = client.SocketReadTimeout_Second;
        row[4] = client.ReceivedPacketCount;
        row[5] = client.SentPacketCount;
        row[6] = client.ActiveConnectionCount;
        row[7] = client.AcceptChannels;
        row[8] = client.SSL;
        row[9] = client.Active;
        return row;
    }

    private void updateAllClientTable() {

        while (clientsTableModel.getRowCount() > 0) {
            clientsTableModel.removeRow(0);
        }
        listenerMXBeanMap = new HashMap<String, ClientsListenerMXBean>();

        updateClientTable();
    }
//endregion

    //region nii functions
    private void niiInit() {
        updateNiiTable();
        niiTable.setModel(niisTableModel);
        addNII.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToNiiTable();
            }
        });
        removeNii.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFromNiiTable();
            }
        });
        serverConfigurations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverConfigForNii();
            }
        });
    }

    private void updateAllNiiTable() {

        MainClass.updateJmx(null, null);
        while (niisTableModel.getRowCount() > 0) {
            niisTableModel.removeRow(0);
        }
        niiMXBeanHashMap = new HashMap<String, NiiMXBean>();

        updateNiiTable();

    }

    private void serverConfigForNii() {
        int selectedRow = niiTable.getSelectedRow();
        String key = niisTableModel.getValueAt(selectedRow, 0).toString();
        ObjectName objectName = objectNames.get(key);

        NiiMXBean niiMXBean = niiMXBeanHashMap.get(key);

        ServerOperations serverOp = new ServerOperations(serverConnectorMap.keySet().toArray(), objectName, niiMXBean.ServerNames);
        serverOp.pack();
        if (serverOp.showDialog()) {
            MainClass.updateJmx(objectName, null);
            niiMXBeanHashMap.replace(key, MainClass.niiMXBeanHashMap.get(key));
            niisTableModel.removeRow(selectedRow);
            niisTableModel.insertRow(selectedRow, createDataForNiiTable(niiMXBeanHashMap.get(key)));
        }
    }

    private void deleteFromNiiTable() {
        int selectedRow = niiTable.getSelectedRow();
        String key = niisTableModel.getValueAt(selectedRow, 0).toString();

        ObjectName objectName = objectNames.get("Nii_Manager");

        DeleteNiiConfirmation deleteServerConfirmation = new DeleteNiiConfirmation(Integer.parseInt(key), objectName);

        if (deleteServerConfirmation.showDialog()) {
            niisTableModel.removeRow(selectedRow);
            MainClass.niiMXBeanHashMap.remove(key);
            niiMXBeanHashMap.remove(key);
        }
    }

    private void addToNiiTable() {
        Object[] serverList = serverConnectorMap.keySet().toArray();

        ObjectName objectName = objectNames.get("Nii_Manager");
        AddNiiDialog addServerDialog = new AddNiiDialog(objectName, serverList);
        addServerDialog.pack();
        addServerDialog.setVisible(true);

        MainClass.updateJmx(null, null);
        updateNiiTable();
    }

    public void updateNiiTable() {
        MainClass.updateJmx(null, null);
        Object[] objects = updateNiisBean().values().toArray();
        for (Object obj : objects) {
            NiiMXBean connector = (NiiMXBean) obj;
            Object[] row = createDataForNiiTable(connector);
            niisTableModel.addRow(row);
        }
    }

    private Object[] createDataForNiiTable(NiiMXBean niiMXBean) {
        Object[] row = new Object[3];
        row[0] = niiMXBean.NiiValue_hex;
        row[1] = niiMXBean.ServerNames;
        row[2] = niiMXBean.ServerCount;
        return row;
    }

    private Map<String, NiiMXBean> updateNiisBean() {
        Map<String, NiiMXBean> newNiis = new HashMap<String, NiiMXBean>();
        for (String key : MainClass.niiMXBeanHashMap.keySet()) {
            if (!niiMXBeanHashMap.containsKey(key)) {
                niiMXBeanHashMap.put(key, MainClass.niiMXBeanHashMap.get(key));
                newNiis.put(key, MainClass.niiMXBeanHashMap.get(key));
            }
        }
        return newNiis;
    }
    //endregion

    //region server functions
    private void serverInit() {
        updateServerTable();

        serversTable.setModel(serversTableModel);

        serversTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateServerTable(e);
            }
        });
        deleteServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFromServerTable();
            }
        });
        viewServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewServerTable();
            }
        });
        addServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToServerTable();
            }
        });
    }

    private void addToServerTable() {
        ObjectName objectName = objectNames.get("Servers");

        AddServerDialog addServerDialog = new AddServerDialog(objectName);
        addServerDialog.setVisible(true);

        MainClass.updateJmx(null, null);
        updateServerTable();
    }

    private void viewServerTable() {
        int selectedRow = serversTable.getSelectedRow();
        String key = serversTableModel.getValueAt(selectedRow, 0).toString();
        ServerConnectorMXBean server = serverConnectorMap.get(key);
        ObjectName objectName = objectNames.get(key);

        ServerInfoDialog serverInfo = new ServerInfoDialog(server, objectName);
        serverInfo.pack();
        if (serverInfo.showDialog()) {
            MainClass.updateJmx(objectName, null);
            serverConnectorMap.replace(key, MainClass.serverConnectorMXBeanHashMap.get(key));
            serversTableModel.removeRow(selectedRow);
            serversTableModel.insertRow(selectedRow, createDataForServerTable(serverConnectorMap.get(key)));
        }
    }

    private void deleteFromServerTable() {
        int selectedRow = serversTable.getSelectedRow();
        String key = serversTableModel.getValueAt(selectedRow, 0).toString();

        ObjectName objectName = objectNames.get("Servers");

        DeleteServerConfirmation deleteServerConfirmation = new DeleteServerConfirmation(key, objectName);

        if (deleteServerConfirmation.showDialog()) {
            serversTableModel.removeRow(selectedRow);
            MainClass.serverConnectorMXBeanHashMap.remove(key);
            serverConnectorMap.remove(key);
        }
        updateAllNiiTable();
        String text = "تعداد سرورها :" + serverConnectorMap.size();
        connectedServerCount.setText(text);
    }

    private void updateServerTable(TableModelEvent e) {
        int changedColumn = e.getColumn();
        int changedRow = e.getFirstRow();
        if (changedColumn != -1 && changedRow != -1)
            changeAttr(changedRow, changedColumn, 0);
    }

    public void updateServerTable() {
        MainClass.updateJmx(null, null);
        Object[] objects = updateServerConnectorMap().values().toArray();
        for (Object obj : objects) {
            ServerConnectorMXBean connector = (ServerConnectorMXBean) obj;
            Object[] row = createDataForServerTable(connector);
            serversTableModel.addRow(row);
        }
        String text = "تعداد سرورها :" + serverConnectorMap.size();
        connectedServerCount.setText(text);
    }

    private Object[] createDataForServerTable(ServerConnectorMXBean connector) {
        Object[] row = new Object[8];
        row[0] = connector.Name;
        row[1] = connector.Ip;
        row[2] = connector.Port;
        row[3] = connector.Enabled;
        row[4] = connector.Connected;
        row[5] = connector.TotalReceivedCount;
        row[6] = connector.TotalSendCount;
        row[7] = connector.Writable;
        return row;
    }

    private Map<String, ServerConnectorMXBean> updateServerConnectorMap() {
        Map<String, ServerConnectorMXBean> newServers = new HashMap<String, ServerConnectorMXBean>();
        for (String key : MainClass.serverConnectorMXBeanHashMap.keySet()) {
            if (!serverConnectorMap.containsKey(key)) {
                serverConnectorMap.put(key, MainClass.serverConnectorMXBeanHashMap.get(key));
                newServers.put(key, MainClass.serverConnectorMXBeanHashMap.get(key));
            }
        }
        return newServers;
    }
    //endregion

    public void changeAttr(int row, int column, int type) {
        Attribute attribute = null;
        ObjectName name = null;
        switch (type) {
            case 0: //it is for server
                name = objectNames.get(serversTableModel.getValueAt(row, 0).toString());
                String valueAt = serversTableModel.getValueAt(row, column).toString();
                if (column == 1) {
                    attribute = new Attribute(serverColumnNameEng[column], valueAt);
                }
                if (column == 2) {
                    attribute = new Attribute(serverColumnNameEng[column], Integer.valueOf(valueAt));
                }
                if (column == 3) {
                    attribute = new Attribute(serverColumnNameEng[column], Boolean.valueOf(valueAt));
                }
                break;
            case 1://it is for client
                name = objectNames.get(clientsTableModel.getValueAt(row, 0).toString());
                valueAt = clientsTableModel.getValueAt(row, column).toString();
                if (column == 3) {
                    attribute = new Attribute(clientColumnNameEng[column], Integer.valueOf(valueAt));
                } else if (column == 7) {
                    attribute = new Attribute(clientColumnNameEng[column], Boolean.valueOf(valueAt));
                }
                break;
        }
        MainClass.setJMXAttribute(name, attribute);
    }

    //region scheduler
    class UpdateJob implements Runnable {

        UpdateJob() {

        }

        public void run() {
            ObjectName objectName = objectNames.get("client");
            MainClass.updateJmx(objectName, null);
            for (Object name : listenerMXBeanMap.keySet().toArray()) {
                objectName = objectNames.get(name.toString());
                MainClass.updateJmx(objectName, null);
            }
            String text = "تعداد بسته های دریافتی: " + MainClass.clientManager.ReceivedPacketCount;
            recievedPacketCount.setText(text);
            text = "تعداد بسته های ارسالی: " + MainClass.clientManager.SentPacketCount;
            sendPacketCount.setText(text);
            text = "تعداد connectionها:" + MainClass.clientManager.CurrentConnectionsCount;
            currentConnectionCount.setText(text);
            updateAllClientTable();
        }
    }

    private ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void updateScheduler() {
        Runnable runnable = new UpdateJob();
        final ScheduledFuture<?> future =
                scheduler.scheduleAtFixedRate(runnable, 10, 60, TimeUnit.SECONDS);
//            scheduler.schedule(new Runnable() {
//                public void run() { future.cancel(true); }
//            }, 60 * 60, TimeUnit.SECONDS);
    }
    //endregion

}


