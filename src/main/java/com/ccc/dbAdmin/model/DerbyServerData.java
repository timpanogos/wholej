/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.util.prefs.Preferences;

import org.apache.derby.drda.NetworkServerControl;
import org.emitdo.research.app.dbAdmin.ServerControl;

public class DerbyServerData
{
    public static final String DefaultHost = "0.0.0.0";
    public static final String DefaultPort = "1527";
    public static final String DefaultHome = "/var/opt/enc/derby/storage";
    public static final String DefaultMaxThreads = "10";
    public static final String DefaultTimeslice = "0";
    public static final boolean DefaultLogConnections = true;

    public String host;
    public String port;
    public String home;
    public String maxThreads;
    public String timeslice;
    public boolean logConnections;
    public NetworkServerControl server;
    public boolean running;
    private final Preferences pref;
    
    public DerbyServerData(Preferences pref)
    {
        this.pref = pref;
        host = pref.get(ServerControl.DerbyServerHostKey, DefaultHost);
        port = pref.get(ServerControl.DerbyServerPortKey, DefaultPort);
        home = pref.get(ServerControl.DerbyServerHomeKey, DefaultHome);
        maxThreads = pref.get(ServerControl.DerbyServerMaxThreadsKey, DefaultMaxThreads);
        timeslice = pref.get(ServerControl.DerbyServerTimesliceKey, DefaultTimeslice);
        logConnections = pref.getBoolean(ServerControl.DerbyServerLogConnectionsKey, true);
    }
    
    public void savePreferences()
    {
        pref.put(ServerControl.DerbyServerHostKey, host);
        pref.put(ServerControl.DerbyServerPortKey, port);
        pref.put(ServerControl.DerbyServerHomeKey, home);
        pref.put(ServerControl.DerbyServerMaxThreadsKey, maxThreads);
        pref.put(ServerControl.DerbyServerTimesliceKey, timeslice);
        pref.putBoolean(ServerControl.DerbyServerLogConnectionsKey, logConnections);
    }
    
    @Override
    public String toString()
    {
        return host + ":" + port + " home: " + home + " threads: " + maxThreads + " timeslice: " + timeslice + " logConns: " + logConnections;
    }
}
