/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.util.prefs.Preferences;

import org.emitdo.app.spawn.Spawn;
import org.emitdo.research.app.dbAdmin.ServerControl;

public class RmiData
{
    public static final String JaveRmiCodebaseKey = "java.rmi.server.codebase";
    public static final String JaveRmiHostKey = "java.rmi.server.hostname";
    public static final String JavePolicyKey = "java.security.policy";
    
    public static final String DefaultRegistryExe = "\\Program Files\\Java\\jdk1.6.0_21\\bin\\rmiregistry.exe";
    public static final String DefaultRegistryPort = "1099";

    public static final String DefaultCodebase = "file:/d:/projects/03-pew-test/bin/ file:/d:/projects/00-emit-oal/bin/ file:/d:/projects/13-emit-as-storage-management/bin/ file:/d:/projects/10-enc-app-common-api/bin/";
    public static final String DefaultHost = "localhost";
    public static final String DefaultPolicy = "/etc/opt/enc/dbAdmin/server.policy";

    public boolean modified;
    public String defaultRegistryExe;
    public String prefRegistryExe;
    public String guiRegistryExe;
    public String defaultRegistryPort;
    public String prefRegistryPort;
    public String guiRegistryPort;
    public String defaultCodebase;
    public String prefCodebase;
    public String guiCodebase;
    public String defaultHost;
    public String prefHost;
    public String guiHost;
    public String defaultPolicy;
    public String prefPolicy;
    public String guiPolicy;
    
    public final Preferences pref;
    public Spawn spawn;

    public RmiData(Preferences pref)
    {
        this.pref = pref;
        defaultRegistryExe = DefaultRegistryExe;
        prefRegistryExe = pref.get(ServerControl.RmiRegistryExeKey, DefaultRegistryExe);
        defaultRegistryPort = DefaultRegistryPort;
        prefRegistryPort = pref.get(ServerControl.RmiRegistryPortKey, DefaultRegistryPort);
        
        defaultCodebase = DefaultRegistryPort;
        prefCodebase = pref.get(ServerControl.RmiCodebaseKey, DefaultCodebase);
        defaultHost = DefaultRegistryPort;
        prefHost = pref.get(ServerControl.RmiHostKey, DefaultHost);
        defaultPolicy = DefaultRegistryPort;
        prefPolicy = pref.get(ServerControl.RmiServerPolicyKey, DefaultPolicy);
    }
}
