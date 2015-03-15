package com.asan.JMXClasses;

//client Listener *
public class ClientsListenerMXBean {

    public String Name;
    public String Description;
    public int ListenPort;
    public boolean SSL;
    public boolean Active;
    public boolean AcceptChannels;
    public int ActiveConnectionCount;

    public int SocketReadTimeout_Second;

    public long ReceivedPacketCount;
    public long SentPacketCount;
}

