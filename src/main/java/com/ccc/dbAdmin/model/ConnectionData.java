/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.sql.Connection;
import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

import org.emitdo.research.app.dbAdmin.DbAdmin;
import org.emitdo.research.app.dbAdmin.DbAdmin.ConnType;
import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.DbAdmin.VendorType;

public class ConnectionData
{
    public static final String DefaultOracleAsManageDa = "org.emitdo.as.oracle.OracleStorageManagement";
    public static final String DefaultJavaDbAsManageDa = "org.emitdo.as.jdb.JdbDomainStorageDataAccessor";
    public static final String DefaultPgAsManageDa = "org.emitdo.as.postgres.PgDomainStorageDataAccessor";
    
    public static final String DefaultOraResManageDa = "org.emitdo.app.res.db.oracle.management.OraResPlatformManagerImpl";
    public static final String DefaultOraBatteryManageDa = "com.panasonic.pesdca.platform.battery.db.oracle.OraBatteryPlatformDataAccessor";
    public static final String DefaultOraAncillaryManageDa = "com.panasonic.pesdca.platform.ancillary.db.oracle.OraAncillaryPlatformDataAccessor";
    
    public static final String DefaultPgResManageDa = "org.emitdo.app.res.db.postgres.management.PgResPlatformManagerImpl";
    public static final String DefaultPgBatteryManageDa = "com.panasonic.pesdca.platform.battery.db.postgres.PgBatteryPlatformDataAccessor";
    public static final String DefaultPgAncillaryManageDa = "com.panasonic.pesdca.platform.ancillary.db.postgres.PgAncillaryPlatformDataAccessor";
    
    public static final String DefaultHome = "/var/opt/enc/derby/storage";
    
// TTC 
    public static final String DefaultAsClusterUserBase = "cluster";
    public static final String DefaultAsClusterUser = DefaultAsClusterUserBase+"_manager";
    public static final String DefaultAsClusterPassword = DefaultAsClusterUser;

    public static final String DefaultAsOracleClusterJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/orcl";
    public static final String DefaultAsOracleClusterWebUrl = "https://changeme.com:5505/dbAdmin/as/cluster/logon";
    public static final String DefaultAsJavaDbClusterJdbcUrl = "jdbc:derby://0.0.0.0:1527/cluster;create=true";
    public static final String DefaultAsJavaDbClusterWebUrl = DefaultAsOracleClusterWebUrl;
    public static final String DefaultAsPgClusterJdbcUrl = "engorcl,5432,asdb,postgres,postgres";
    public static final String DefaultAsPgClusterWebUrl = DefaultAsOracleClusterWebUrl;

// Res
    //factory
    public static final String DefaultAsFactoryUserBase = "factory";
    public static final String DefaultAsFactoryUser = DefaultAsFactoryUserBase+"_manager";
    public static final String DefaultAsFactoryPassword = DefaultAsFactoryUser;
    
    public static final String DefaultAsOracleFactoryJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/asdb";
    public static final String DefaultAsOracleFactoryWebUrl = "https://changeme.com:5505/dbAdmin/as/factory/logon";
    public static final String DefaultAsJavaDbFactoryJdbcUrl = "jdbc:derby://0.0.0.0:1527/factory;create=true";
    public static final String DefaultAsJavaDbFactoryWebUrl = DefaultAsOracleFactoryWebUrl;
    public static final String DefaultAsPgFactoryJdbcUrl = "engorcl,5432,asdb,postgres,postgres";
    public static final String DefaultAsPgFactoryWebUrl = DefaultAsOracleFactoryWebUrl;

    //res
    public static final String DefaultAsResUserBase = "res";
    public static final String DefaultAsResUser = DefaultAsResUserBase+"_manager";
    public static final String DefaultAsResPassword = DefaultAsResUser;
    public static final String DefaultResUserBase = "res_platform";
    public static final String DefaultResUser = DefaultResUserBase+"_manager";
    public static final String DefaultResPassword = DefaultResUser;

    public static final String DefaultAsOracleResJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/asdb";
    public static final String DefaultAsOracleResWebUrl = "https://changeme.com:5505/dbAdmin/as/res/logon";
    public static final String DefaultAsJavaDbResJdbcUrl = "jdbc:derby://0.0.0.0:1527/res;create=true";
    public static final String DefaultAsJavaDbResWebUrl = DefaultAsOracleResWebUrl;
    public static final String DefaultAsPgResJdbcUrl = "engorcl,5432,asdb,postgres,postgres";
    public static final String DefaultAsPgResWebUrl = DefaultAsOracleResWebUrl;
    
    public static final String DefaultResOracleJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/res";
    public static final String DefaultResOracleWebUrl = "https://changeme.com:5505/dbAdmin/res/logon";
    public static final String DefaultResJavaDbJdbcUrl = "jdbc:derby://0.0.0.0:1527/res;create=true";
    public static final String DefaultResJavaDbWebUrl = DefaultResOracleWebUrl;
    public static final String DefaultResPgJdbcUrl = "engorcl,5432,res,postgres,postgres";
    public static final String DefaultResPgWebUrl = DefaultResOracleWebUrl;
    
// Battery
    public static final String DefaultAsBatteryUserBase = "battery";
    public static final String DefaultAsBatteryUser = DefaultAsBatteryUserBase+"_manager";
    public static final String DefaultAsBatteryPassword = DefaultAsBatteryUser;
    public static final String DefaultBatteryUserBase = "battery_platform";
    public static final String DefaultBatteryUser = DefaultBatteryUserBase+"_manager";
    public static final String DefaultBatteryPassword = DefaultBatteryUser;

    public static final String DefaultAsOracleBatteryJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/battery";
    public static final String DefaultAsOracleBatteryWebUrl = "https://changeme.com:5505/dbAdmin/as/battery/logon";
    public static final String DefaultAsJavaDbBatteryJdbcUrl = "jdbc:derby://0.0.0.0:1527/battery;create=true";
    public static final String DefaultAsJavaDbBatteryWebUrl = DefaultAsOracleBatteryWebUrl;
    public static final String DefaultAsPgBatteryJdbcUrl = "engorcl,5432,asdb,postgres,postgres";
    public static final String DefaultAsPgBatteryWebUrl = DefaultAsOracleBatteryWebUrl;

    public static final String DefaultBatteryOracleJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/battery";
    public static final String DefaultBatteryOracleWebUrl = "https://changeme.com:5505/dbAdmin/battery/logon";
    public static final String DefaultBatteryJavaDbJdbcUrl = "jdbc:derby://0.0.0.0:1527/ancpwr;create=true";
    public static final String DefaultBatteryJavaDbWebUrl = DefaultBatteryOracleWebUrl;
    public static final String DefaultBatteryPgJdbcUrl = "engorcl,5432,battery,postgres,postgres";
    public static final String DefaultBatteryPgWebUrl = DefaultBatteryOracleWebUrl;

// Ancillary
    public static final String DefaultAsAncillaryUserBase = "ancillary";
    public static final String DefaultAsAncillaryUser = DefaultAsAncillaryUserBase+"_manager";
    public static final String DefaultAsAncillaryPassword = DefaultAsAncillaryUser;
    public static final String DefaultAncillaryUserBase = "ancillary_platform";
    public static final String DefaultAncillaryUser = DefaultAncillaryUserBase+"_manager";
    public static final String DefaultAncillaryPassword = DefaultAncillaryUser;

    public static final String DefaultAsOracleAncillaryJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/asdb";
    public static final String DefaultAsOracleAncillaryWebUrl = "https://changeme.com:5505/dbAdmin/as/ancillary/logon";
    public static final String DefaultAsJavaDbAncillaryJdbcUrl = "jdbc:derby://0.0.0.0:1527/ancillary;create=true";
    public static final String DefaultAsJavaDbAncillaryWebUrl = DefaultAsOracleAncillaryWebUrl;
    public static final String DefaultAsPgAncillaryJdbcUrl = "engorcl,5432,asdb,postgres,postgres";
    public static final String DefaultAsPgAncillaryWebUrl = DefaultAsOracleAncillaryWebUrl;
    
    public static final String DefaultAncillaryOracleJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/ancillary";
    public static final String DefaultAncillaryOracleWebUrl = "https://changeme.com:5505/dbAdmin/ancillary/logon";
    public static final String DefaultAncillaryJavaDbJdbcUrl = "jdbc:derby://0.0.0.0:1527/ancpwr;create=true";
    public static final String DefaultAncillaryJavaDbWebUrl = DefaultAncillaryOracleWebUrl;
    public static final String DefaultAncillaryPgJdbcUrl = "engorcl,5432,ancillary,postgres,postgres";
    public static final String DefaultAncillaryPgWebUrl = DefaultAncillaryOracleWebUrl;

// backup
    public static final String DefaultBackupUserBase = "backup";
    public static final String DefaultBackupUser = DefaultBackupUserBase+"_manager";
    public static final String DefaultBackupPassword = DefaultBackupUser;

    public static final String DefaultBackupOracleJdbcUrl = "jdbc:oracle:thin:@engorcl:1521/backup.com";
    public static final String DefaultBackupOracleWebUrl = "https://changeme.com:5505/dbAdmin/backup/logon";
    public static final String DefaultBackupJavaDbJdbcUrl = "jdbc:derby://0.0.0.0:1527/backup;create=true";
    public static final String DefaultBackupJavaDbWebUrl = DefaultBackupOracleWebUrl;
    public static final String DefaultBackupPgJdbcUrl = "engorcl,5432,backup,postgres,postgres";
    public static final String DefaultBackupPgWebUrl = DefaultBackupOracleWebUrl;

// naming    
    public static final String DefaultNamingUserBase = "naming";
    public static final String DefaultNamingUser = DefaultNamingUserBase+"_manager";
    public static final String DefaultNamingPassword = DefaultNamingUser;

    public static final String DefaultNamingOracleJdbcUrl = "jdbc:oracle:thin:@changeme:1521/naming.changeme.com";
    public static final String DefaultNamingOracleWebUrl = "https://changeme.com:5505/dbAdmin/naming/logon";
    public static final String DefaultNamingJavaDbJdbcUrl = "jdbc:derby://0.0.0.0:1527/naming;create=true";
    public static final String DefaultNamingJavaDbWebUrl = DefaultNamingOracleWebUrl;

// rg    
    public static final String DefaultRgUserBase = "asjdb";
    public static final String DefaultRgUser = DefaultRgUserBase+"_manager";
    public static final String DefaultRgPassword = DefaultRgUser;
    
    public static final String DefaultAsOracleRgJdbcUrl = "jdbc:oracle:thin:@changeme:1521/as.rg.com";
    public static final String DefaultAsOracleRgWebUrl = "https://changeme.com:5505/dbAdmin/as/rg/logon";
    public static final String DefaultAsJavaDbRgJdbcUrl = "jdbc:derby://0.0.0.0:1527/rg;create=true";
    public static final String DefaultAsJavaDbRgWebUrl = DefaultAsOracleRgWebUrl;
    
    public final String keyUser;
    public final String keyPassword;
    public final String keyUserBase;
    public final String keyUrl;
    public final String keyDataAccessor;
    public final String keyHome;
    public final String defaultUser;
    public String prefUser;
    public String guiUser;
    public final String defaultPassword;
    public String prefPassword;
    public String guiPassword;
    public final String defaultUserBase;
    public String prefUserBase;
    public String guiUserBase;
    public final String defaultUrl;
    public String prefUrl;
    public String guiUrl;
    public final String defaultDataAccessor;
    public String prefDataAccessor;
    public String guiDataAccessor;
    public final String defaultHome;
    public String prefHome;
    public String guiHome;
    
    public boolean supported;
    public boolean modified;
    public boolean connected;
    public JRadioButton radioButton;
    public JCheckBox checkBox;
    public final StorageType storageType; 
    public final VendorType vendorType; 
    public final DomainType domainType; 
    public final ConnType connType; 
    public Connection jdbcConnection;
    public Object dataAccessor;
    
    private final Preferences pref;
    
    public ConnectionData(
            Preferences pref,
            StorageType stype, 
            VendorType vtype, 
            DomainType dtype, 
            ConnType ctype, 
            String user, String pass, String userBase, String url)
    {
        this.pref = pref;
        storageType = stype;
        vendorType = vtype;
        domainType = dtype;
        connType = ctype;
        defaultUser = user;
        defaultPassword = pass;
        defaultUserBase = userBase;
        defaultUrl = url;
        if(StorageType.As == stype && VendorType.JavaDb == vtype)
            defaultDataAccessor = DefaultJavaDbAsManageDa;
        else if(StorageType.As == stype && VendorType.Postgres == vtype)
            defaultDataAccessor = DefaultPgAsManageDa;
        else if(StorageType.As == stype && VendorType.Oracle == vtype)
            defaultDataAccessor = DefaultOracleAsManageDa;
        
        else if(StorageType.Res == stype && VendorType.Postgres == vtype)
            defaultDataAccessor = DefaultPgResManageDa;
        else if(StorageType.Res == stype && VendorType.Oracle == vtype)
            defaultDataAccessor = DefaultOraResManageDa;
        
        else if(StorageType.Battery == stype && VendorType.Postgres == vtype)
            defaultDataAccessor = DefaultPgBatteryManageDa;
        else if(StorageType.Battery == stype && VendorType.Oracle == vtype)
            defaultDataAccessor = DefaultOraBatteryManageDa;

        else if(StorageType.Ancillary == stype && VendorType.Postgres == vtype)
            defaultDataAccessor = DefaultPgAncillaryManageDa;
        else if(StorageType.Ancillary == stype && VendorType.Oracle == vtype)
            defaultDataAccessor = DefaultOraAncillaryManageDa;
        else
            defaultDataAccessor = "unknown";
        
        defaultHome = DefaultHome;
        
        supported = true;
        if(ConnType.Web == ctype)
            supported = false;
        if(StorageType.Res == stype && VendorType.JavaDb == vtype)
            supported = false;
        if(StorageType.Backup == stype && VendorType.JavaDb == vtype)
            supported = false;
        if(StorageType.Naming == stype && VendorType.Oracle == vtype)
            supported = false;
        if(StorageType.As == stype && DomainType.Rg == dtype && VendorType.Oracle == vtype)
            supported = false;
        if(StorageType.Ancillary == stype && VendorType.JavaDb == vtype)
            supported = false;
        if(StorageType.Ancillary == stype && VendorType.Oracle == vtype)
            supported = false;
        if(StorageType.Battery == stype && VendorType.JavaDb == vtype)
            supported = false;
        if(StorageType.Battery == stype && VendorType.Oracle == vtype)
            supported = false;
        
        String prefix = DbAdmin.DbAdminPrefixKey + ".connection.";
        if(dtype == null)
        {
            keyUser = prefix + "user." + stype.name() + "." + vtype.name() + "." + ctype.name();
            keyUserBase = prefix + "userBase." + stype.name() + "." + vtype.name() + "." + ctype.name();
            prefUser = pref.get(keyUser, null);
            keyPassword = prefix + "password." + stype.name() + "." + vtype.name() + "." + ctype.name();
            prefPassword = pref.get(keyPassword, null);
            prefUserBase = pref.get(keyUserBase, null);
            keyUrl = prefix + "url." + stype.name() + "." + vtype.name() + "." + ctype.name();
            prefUrl = pref.get(keyUrl, null);
            keyDataAccessor = prefix + "da." + stype.name() + "." + vtype.name() + "." + ctype.name();
            prefDataAccessor = pref.get(keyDataAccessor, null);
            keyHome = prefix + "home." + stype.name() + "." + vtype.name() + "." + ctype.name();
            prefHome = pref.get(keyHome, null);
        }else
        {
            keyUser = prefix + "user." + stype.name() + "." + vtype.name() + "." + dtype.name() + "." + ctype.name();
            keyUserBase = prefix + "userBase." + stype.name() + "." + vtype.name() + "." + dtype.name() + "." + ctype.name();
            prefUser = pref.get(keyUser, null);
            keyPassword = prefix + "password." + stype.name() + "." + vtype.name() + "." + dtype.name() + "." + ctype.name();
            prefPassword = pref.get(keyPassword, null);
            prefUserBase = pref.get(keyUserBase, null);
            keyUrl = prefix + "url." + stype.name() + "." + vtype.name() + "." + dtype.name() + "." + ctype.name();
            prefUrl = pref.get(keyUrl, null);
            keyDataAccessor = prefix + "da." + stype.name() + "." + vtype.name() + "." + dtype.name() + "." + ctype.name();
            prefDataAccessor = pref.get(keyDataAccessor, null);
            keyHome = prefix + "home." + stype.name() + "." + vtype.name() + "." + dtype.name() + "." + ctype.name();
            prefHome = pref.get(keyHome, null);
        }
    }
    
    public String getUser()
    {
        return getUser(false);
    }
    
    public String getUser(boolean preferences)
    {
        if(modified && !preferences)
            return guiUser;
        if(prefUser == null)
            return defaultUser;
        return prefUser;
    }
    
    public String getUserBase()
    {
        return getUserBase(false);
    }
    
    public String getUserBase(boolean preferences)
    {
        if(modified && !preferences)
            return guiUserBase;
        if(prefUserBase == null)
            return defaultUserBase;
        return prefUserBase;
    }
    
    public String getPassword()
    {
        return getPassword(false);
    }
    
    public String getPassword(boolean preferences)
    {
        if(modified && !preferences)
            return guiPassword;
        if(prefPassword == null)
            return defaultPassword;
        return prefPassword;
    }
    
    public String getUrl()
    {
        return getUrl(false);
    }        
    
    public String getUrl(boolean preferences)
    {
        if(modified && !preferences)
            return guiUrl;
        if(prefUrl == null)
            return defaultUrl;
        return prefUrl;
    }
    
    public String getDataAccessor()
    {
        return getDataAccessor(false);
    }
    
    public String getDataAccessor(boolean preferences)
    {
        if(modified && !preferences)
            return guiDataAccessor;
        if(prefUrl == null)
            return defaultDataAccessor;
        return prefDataAccessor;
    }
    
    public String getHome()
    {
        return getHome(false);
    }
    
    public String getHome(boolean preferences)
    {
        if(modified && !preferences)
            return guiHome;
        if(prefHome == null)
            return defaultHome;
        return prefHome;
    }
    
    public void savePreferences()
    {
        pref.put(keyUser, guiUser);
        pref.put(keyPassword, guiPassword);
        pref.put(keyUserBase, guiUserBase);
        pref.put(keyUrl, guiUrl);
        pref.put(keyDataAccessor, guiDataAccessor);
        pref.put(keyHome, guiHome);
        prefUser = guiUser;
        prefPassword = guiPassword;
        prefUserBase = guiUserBase;
        prefUrl = guiUrl;
        prefDataAccessor = guiDataAccessor;
        prefHome = guiHome;
        modified = false;
    }
    
    @Override
    public String toString()
    {
        return getUser() + "/" + getPassword() + " " + getUrl();
    }
}
