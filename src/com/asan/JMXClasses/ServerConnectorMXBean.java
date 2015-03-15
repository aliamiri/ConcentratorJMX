package com.asan.JMXClasses;

// server  *
public class ServerConnectorMXBean {

    public String Name;
    public String Ip;
    public int Port;
    public boolean Enabled;
    public long TotalReceivedCount;
    public long TotalSendCount;
    public boolean Connected;
    public boolean Writable;

    public boolean AutoReconnectEnable;

    public int ConnectionTryWaitTimeMillis;
    public int WriteBufferHighWaterMark;
    public int WriteBufferLowWaterMark;

}
