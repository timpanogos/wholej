/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.util.prefs.Preferences;

import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.ServerControl;

public class RmiJdbData
{
    public static final String DefaultClusterUser = "asjdb_user";
    public static final String DefaultClusterPassword = DefaultClusterUser;
    public static final String DefaultClusterHome = "/var/opt/enc/ttc/storage";
    public static final String DefaultClusterSchema = "asjdb_admin";
    public static final String DefaultClusterUrl = "jdbc:derby:/cluster";
    public static final String DefaultClusterDomain = "[6:cluster.pewla.com]";

    public static final String DefaultPlatformUser = DefaultClusterUser;
    public static final String DefaultPlatformPassword = DefaultPlatformUser;
    public static final String DefaultPlatformHome = DefaultClusterHome;
    public static final String DefaultPlatformSchema = DefaultClusterSchema;
    public static final String DefaultPlatformUrl = "jdbc:derby:/platform";
    public static final String DefaultPlatformDomain = "[6:platform.pewla.com]";
    
    public static final String DefaultFactoryUser = DefaultClusterUser;
    public static final String DefaultFactoryPassword = DefaultFactoryUser;
    public static final String DefaultFactoryHome = DefaultClusterHome;
    public static final String DefaultFactorySchema = DefaultClusterSchema;
    public static final String DefaultFactoryUrl = "jdbc:derby:/factory";
    public static final String DefaultFactoryDomain = "[6:factory.pewla.com]";
    
    public final DomainType type;
    public final RmiData rmiData;
    
    public String defaultUser;
    public String prefUser;
    public String guiUser;
    public String defaultPassword;
    public String prefPassword;
    public String guiPassword;
    public String defaultHome;
    public String prefHome;
    public String guiHome;
    public String defaultSchema;
    public String prefSchema;
    public String guiSchema;
    public String defaultUrl;
    public String prefUrl;
    public String guiUrl;
    public String defaultDomain;
    public String prefDomain;
    public String guiDomain;
    
    public boolean running;
    public boolean modified;
    
    public final Preferences pref;
    
    public RmiJdbData(DomainType type, RmiData rmiData, Preferences pref)
    {
        this.type = type;
        this.pref = pref;
        this.rmiData = rmiData;
        switch(type)
        {
            case Cluster:
                defaultUser = DefaultClusterUser;
                defaultPassword = DefaultClusterPassword;
                defaultHome = DefaultClusterHome;
                defaultSchema = DefaultClusterSchema;
                defaultUrl = DefaultClusterUrl;
                prefUser = pref.get(ServerControl.RmiJdbClusterUserKey, DefaultClusterUser);
                prefPassword = pref.get(ServerControl.RmiJdbClusterPasswordKey, DefaultClusterPassword);
                prefHome = pref.get(ServerControl.RmiJdbClusterHomeKey, DefaultClusterHome);
                prefSchema = pref.get(ServerControl.RmiJdbClusterSchemaKey, DefaultClusterSchema);
                prefUrl = pref.get(ServerControl.RmiJdbClusterUrlKey, DefaultClusterUrl);
                prefDomain = pref.get(ServerControl.RmiJdbClusterDomainKey, DefaultClusterDomain);
                break;
            case Factory:
                defaultUser = DefaultFactoryUser;
                defaultPassword = DefaultFactoryPassword;
                defaultHome = DefaultFactoryHome;
                defaultSchema = DefaultFactorySchema;
                defaultUrl = DefaultFactoryUrl;
                prefUser = pref.get(ServerControl.RmiJdbFactoryUserKey, DefaultFactoryUser);
                prefPassword = pref.get(ServerControl.RmiJdbFactoryPasswordKey, DefaultFactoryPassword);
                prefHome = pref.get(ServerControl.RmiJdbFactoryHomeKey, DefaultFactoryHome);
                prefSchema = pref.get(ServerControl.RmiJdbFactorySchemaKey, DefaultFactorySchema);
                prefUrl = pref.get(ServerControl.RmiJdbFactoryUrlKey, DefaultFactoryUrl);
                prefDomain = pref.get(ServerControl.RmiJdbFactoryDomainKey, DefaultFactoryDomain);
                break;
            case Res:
                defaultUser = DefaultPlatformUser;
                defaultPassword = DefaultPlatformPassword;
                defaultHome = DefaultPlatformHome;
                defaultSchema = DefaultPlatformSchema;
                defaultUrl = DefaultPlatformUrl;
                prefUser = pref.get(ServerControl.RmiJdbPlatformUserKey, DefaultPlatformUser);
                prefPassword = pref.get(ServerControl.RmiJdbPlatformPasswordKey, DefaultPlatformPassword);
                prefHome = pref.get(ServerControl.RmiJdbPlatformHomeKey, DefaultPlatformHome);
                prefSchema = pref.get(ServerControl.RmiJdbPlatformSchemaKey, DefaultPlatformSchema);
                prefUrl = pref.get(ServerControl.RmiJdbPlatformUrlKey, DefaultPlatformUrl);
                prefDomain = pref.get(ServerControl.RmiJdbPlatformDomainKey, DefaultPlatformDomain);
                break;
            default:
                throw new IllegalArgumentException("only cluster, factory or platform currently supported");
        }
    }
}
