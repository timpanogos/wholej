/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;

import javax.swing.JMenuBar;
import javax.swing.JToolBar;

public class ServerControl
{
    public static final String DerbyServerHostKey = "org.emitdo.as.jdb.server.host";
    public static final String DerbyServerPortKey = "org.emitdo.as.jdb.server.port";
    public static final String DerbyServerHomeKey = "org.emitdo.as.jdb.server.home";
    public static final String DerbyServerMaxThreadsKey = "org.emitdo.as.jdb.server.maxConnections";
    public static final String DerbyServerTimesliceKey = "org.emitdo.as.jdb.server.timeslice";
    public static final String DerbyServerLogConnectionsKey = "org.emitdo.as.jdb.server.logConnections";
    
    public static final String RmiJdbClusterUserKey = "org.emitdo.as.jdb.rmi.cluster.user";
    public static final String RmiJdbClusterPasswordKey = "org.emitdo.as.jdb.rmi.cluster.password";
    public static final String RmiJdbClusterHomeKey = "org.emitdo.as.jdb.rmi.cluster.home";
    public static final String RmiJdbClusterSchemaKey = "org.emitdo.as.jdb.rmi.cluster.schema";
    public static final String RmiJdbClusterUrlKey = "org.emitdo.as.jdb.rmi.cluster.url";
    public static final String RmiJdbClusterDomainKey = "org.emitdo.as.jdb.rmi.cluster.domain";
    
    public static final String RmiJdbPlatformUserKey = "org.emitdo.as.jdb.rmi.platform.user";
    public static final String RmiJdbPlatformPasswordKey = "org.emitdo.as.jdb.rmi.platform.password";
    public static final String RmiJdbPlatformHomeKey = "org.emitdo.as.jdb.rmi.platform.home";
    public static final String RmiJdbPlatformSchemaKey = "org.emitdo.as.jdb.rmi.platform.schema";
    public static final String RmiJdbPlatformUrlKey = "org.emitdo.as.jdb.rmi.platform.url";
    public static final String RmiJdbPlatformDomainKey = "org.emitdo.as.jdb.rmi.platform.domain";
    
    public static final String RmiJdbFactoryUserKey = "org.emitdo.as.jdb.rmi.factory.user";
    public static final String RmiJdbFactoryPasswordKey = "org.emitdo.as.jdb.rmi.factory.password";
    public static final String RmiJdbFactoryHomeKey = "org.emitdo.as.jdb.rmi.factory.home";
    public static final String RmiJdbFactorySchemaKey = "org.emitdo.as.jdb.rmi.factory.schema";
    public static final String RmiJdbFactoryUrlKey = "org.emitdo.as.jdb.rmi.factory.url";
    public static final String RmiJdbFactoryDomainKey = "org.emitdo.as.jdb.rmi.factory.domain";

    public static final String RmiRegistryExeKey = "org.emitdo.as.rmi.registry.exe";
    public static final String RmiRegistryPortKey = "org.emitdo.as.rmi.registry.port";
    public static final String RmiCodebaseKey = "org.emitdo.as.rmi.codebase";
    public static final String RmiHostKey = "org.emitdo.as.rmi.host";
    public static final String RmiServerPolicyKey = "org.emitdo.as.rmi.server.policy";
    

    @SuppressWarnings("unused")
    private final DbAdmin asAdmin;
    
    public ServerControl(DbAdmin dbAdmin)
    {
        this.asAdmin = dbAdmin;
    }

    public void addMenu(JMenuBar menuBar, JToolBar toolBar)
    {
    }

    public void close()
    {
    }
    
    public void setBusy(boolean busy)
    {
    }
}
