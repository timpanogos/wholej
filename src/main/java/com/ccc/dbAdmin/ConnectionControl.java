/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;

import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.emitdo.app.as.common.DomainStorageManagement;
import org.emitdo.app.res.db.oracle.management.OraResPlatformManagerImpl;
import org.emitdo.app.res.db.postgres.management.PgResPlatformManagerImpl;
import org.emitdo.app.util.Commands;
import org.emitdo.as.jdb.JavaDbStorage;
import org.emitdo.as.oracle.OracleStorageManagement;
import org.emitdo.db.common.javadb.JdbDatastoreBase;
import org.emitdo.db.common.oracle.OracleConnectionPoolCommands;
import org.emitdo.db.common.postgres.PgConnectionPoolCommands;
import org.emitdo.internal.app.res.db.oracle.OraResSqlGenerator;
import org.emitdo.internal.app.res.db.postgres.res.PgResSqlGenerator;
import org.emitdo.internal.app.service.backup.db.postgres.PgBackupSqlGenerator;
import org.emitdo.internal.app.service.naming.db.jdb.JdbNamingSqlGenerator;
import org.emitdo.internal.as.storage.jdb.JdbAsdbSqlGenerator;
import org.emitdo.internal.as.storage.postgres.domain.PgDomainSqlGenerator;
import org.emitdo.internal.as.storage.postgres.global.PgGlobalSqlGenerator;
import org.emitdo.internal.db.common.javadb.JdbBaseGenerator;
import org.emitdo.internal.db.common.javadb.JdbSqlUtil;
import org.emitdo.internal.db.common.oracle.OraBaseGenerator;
import org.emitdo.internal.db.common.oracle.OraSqlUtil;
import org.emitdo.internal.db.common.postgres.PgBaseGenerator;
import org.emitdo.internal.db.common.postgres.PgSqlGeneratorBase;
import org.emitdo.internal.db.common.postgres.PgSqlUtil;
import org.emitdo.oal.DOFObjectID.Domain;
import org.emitdo.research.app.dbAdmin.DbAdmin.ConnType;
import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.DbAdmin.VendorType;
import org.emitdo.research.app.dbAdmin.model.AsDomainInitializer;
import org.emitdo.research.app.dbAdmin.model.CannedResData;
import org.emitdo.research.app.dbAdmin.model.CannedResInitializer;
import org.emitdo.research.app.dbAdmin.model.ConnectionData;
import org.emitdo.research.app.dbAdmin.model.DomainData;
import org.emitdo.research.app.dbAdmin.model.OraBackupInitializer;
import org.emitdo.research.app.dbAdmin.model.OracleDataAccessor;
import org.emitdo.research.app.dbAdmin.model.OracleDatabaseClear;
import org.emitdo.research.app.dbAdmin.model.OracleResManager;
import org.emitdo.research.app.dbAdmin.model.PgBackupInitializer;
import org.emitdo.research.app.dbAdmin.model.PgResManager;
import org.emitdo.research.app.dbAdmin.model.PostgresDataAccessor;
import org.emitdo.research.app.dbAdmin.view.connection.AncillaryConnectionPanel;
import org.emitdo.research.app.dbAdmin.view.connection.AsConnectionPanel;
import org.emitdo.research.app.dbAdmin.view.connection.BackupConnectionPanel;
import org.emitdo.research.app.dbAdmin.view.connection.BatteryConnectionPanel;
import org.emitdo.research.app.dbAdmin.view.connection.NamingConnectionPanel;
import org.emitdo.research.app.dbAdmin.view.connection.ResConnectionPanel;
import org.emitdo.research.app.swing.SwappedFocusListener;

import com.panasonic.pesdca.internal.platform.battery.db.postgres.PgBatteryPlatformComponentHelper;
import com.panasonic.pesdca.internal.platform.battery.db.postgres.PgBatteryPlatformSqlGenerator;
import com.pew.test.ancpwr.db.batteryDb.postgres.PgBatteryDbTestCommands;

public class ConnectionControl
{
    public static final String DerbyServerHostKey = "org.emitdo.as.jdb.server.host";
    public static final String DerbyServerPortKey = "org.emitdo.as.jdb.server.port";
    public static final String DerbyServerHomeKey = "org.emitdo.as.jdb.server.home";
    public static final String DerbyServerMaxThreadsKey = "org.emitdo.as.jdb.server.maxConnections";
    public static final String DerbyServerTimesliceKey = "org.emitdo.as.jdb.server.timeslice";
    public static final String DerbyServerLogConnectionsKey = "org.emitdo.as.jdb.server.logConnections";
    
    public static final String ConnectButtonLabel = "Connect";
    public static final String DisconnectButtonLabel = "Disconnect";
    public static final String ConnectedActionCommand = "Connected";
    
    private final DbAdmin dbAdmin;
    private final Hashtable<String, ConnectionData> connections;
    
    private AsConnectionAction asConnectionAction;
    private ResConnectionAction resConnectionAction;
    private BackupConnectionAction backupConnectionAction;
    private NamingConnectionAction namingConnectionAction;
    private AncillaryConnectionAction ancillaryConnectionAction;
    private BatteryConnectionAction batteryConnectionAction;
    
    public ConnectionControl(DbAdmin dbAdmin)
    {
        this.dbAdmin = dbAdmin;
        connections = new Hashtable<String, ConnectionData>();
        ConnectionData ci = null;
        Preferences pref = dbAdmin.getUserRoot();
        
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Cluster,   ConnType.Jdbc, ConnectionData.DefaultAsClusterUser, ConnectionData.DefaultAsClusterPassword, ConnectionData.DefaultAsClusterUserBase, ConnectionData.DefaultAsOracleClusterJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Cluster,   ConnType.Web,  ConnectionData.DefaultAsClusterUser, ConnectionData.DefaultAsClusterPassword, ConnectionData.DefaultAsClusterUserBase, ConnectionData.DefaultAsOracleClusterWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Res,       ConnType.Jdbc, ConnectionData.DefaultAsResUser, ConnectionData.DefaultAsResPassword, ConnectionData.DefaultAsResUserBase, ConnectionData.DefaultAsOracleResJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Res,       ConnType.Web,  ConnectionData.DefaultAsResUser, ConnectionData.DefaultAsResPassword, ConnectionData.DefaultAsResUserBase, ConnectionData.DefaultAsOracleResWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Factory,   ConnType.Jdbc, ConnectionData.DefaultAsFactoryUser, ConnectionData.DefaultAsFactoryPassword, ConnectionData.DefaultAsFactoryUserBase, ConnectionData.DefaultAsOracleFactoryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Factory,   ConnType.Web,  ConnectionData.DefaultAsFactoryUser, ConnectionData.DefaultAsFactoryPassword, ConnectionData.DefaultAsFactoryUserBase, ConnectionData.DefaultAsOracleFactoryWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Rg,        ConnType.Jdbc, ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsOracleRgJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Rg,        ConnType.Web,  ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsOracleRgWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Battery,   ConnType.Jdbc, ConnectionData.DefaultAsBatteryUser, ConnectionData.DefaultAsBatteryPassword, ConnectionData.DefaultAsBatteryUserBase, ConnectionData.DefaultAsOracleBatteryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Battery,   ConnType.Web,  ConnectionData.DefaultAsBatteryUser, ConnectionData.DefaultAsBatteryPassword, ConnectionData.DefaultAsBatteryUserBase, ConnectionData.DefaultAsOracleBatteryWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Ancillary, ConnType.Jdbc, ConnectionData.DefaultAsAncillaryUser, ConnectionData.DefaultAsAncillaryPassword, ConnectionData.DefaultAsAncillaryUserBase, ConnectionData.DefaultAsOracleAncillaryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Oracle, DomainType.Ancillary, ConnType.Web,  ConnectionData.DefaultAsAncillaryUser, ConnectionData.DefaultAsAncillaryPassword, ConnectionData.DefaultAsAncillaryUserBase, ConnectionData.DefaultAsOracleAncillaryWebUrl);
            connections.put(ci.keyUser, ci);
        
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Cluster,   ConnType.Jdbc, ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbClusterJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Cluster,   ConnType.Web,  ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbClusterWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Res,       ConnType.Jdbc, ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbResJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Res,       ConnType.Web,  ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbResWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Factory,   ConnType.Jdbc, ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbFactoryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Factory,   ConnType.Web,  ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbFactoryWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Rg,        ConnType.Jdbc, ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbRgJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Rg,        ConnType.Web,  ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbRgWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Battery,   ConnType.Jdbc, ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbBatteryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Battery,   ConnType.Web,  ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbBatteryWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Ancillary, ConnType.Jdbc, ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbAncillaryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.JavaDb, DomainType.Ancillary, ConnType.Web,  ConnectionData.DefaultRgUser, ConnectionData.DefaultRgPassword, ConnectionData.DefaultRgUserBase, ConnectionData.DefaultAsJavaDbAncillaryWebUrl);
            connections.put(ci.keyUser, ci);
            
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Cluster,  ConnType.Jdbc, ConnectionData.DefaultAsClusterUser, ConnectionData.DefaultAsClusterPassword, ConnectionData.DefaultAsClusterUserBase, ConnectionData.DefaultAsPgClusterJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Cluster,  ConnType.Web,  ConnectionData.DefaultAsClusterUser, ConnectionData.DefaultAsClusterPassword, ConnectionData.DefaultAsClusterUserBase, ConnectionData.DefaultAsPgClusterWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Res, ConnType.Jdbc, ConnectionData.DefaultAsResUser, ConnectionData.DefaultAsResPassword, ConnectionData.DefaultAsResUserBase, ConnectionData.DefaultAsPgResJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Res, ConnType.Web,  ConnectionData.DefaultAsResUser, ConnectionData.DefaultAsResPassword, ConnectionData.DefaultAsResUserBase, ConnectionData.DefaultAsPgResWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Factory,  ConnType.Jdbc, ConnectionData.DefaultAsFactoryUser, ConnectionData.DefaultAsFactoryPassword, ConnectionData.DefaultAsFactoryUserBase, ConnectionData.DefaultAsPgFactoryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Factory,  ConnType.Web,  ConnectionData.DefaultAsFactoryUser, ConnectionData.DefaultAsFactoryPassword, ConnectionData.DefaultAsFactoryUserBase, ConnectionData.DefaultAsPgFactoryWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Battery,   ConnType.Jdbc, ConnectionData.DefaultAsBatteryUser, ConnectionData.DefaultAsBatteryPassword, ConnectionData.DefaultAsBatteryUserBase, ConnectionData.DefaultAsPgBatteryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Battery,   ConnType.Web,  ConnectionData.DefaultAsBatteryUser, ConnectionData.DefaultAsBatteryPassword, ConnectionData.DefaultAsBatteryUserBase, ConnectionData.DefaultAsPgBatteryWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Ancillary, ConnType.Jdbc, ConnectionData.DefaultAsAncillaryUser, ConnectionData.DefaultAsAncillaryPassword, ConnectionData.DefaultAsAncillaryUserBase, ConnectionData.DefaultAsPgAncillaryJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.As, VendorType.Postgres, DomainType.Ancillary, ConnType.Web,  ConnectionData.DefaultAsAncillaryUser, ConnectionData.DefaultAsAncillaryPassword, ConnectionData.DefaultAsAncillaryUserBase, ConnectionData.DefaultAsPgAncillaryWebUrl);
            connections.put(ci.keyUser, ci);
        
        ci = new ConnectionData(pref, StorageType.Res, VendorType.Oracle, null, ConnType.Jdbc, ConnectionData.DefaultResUser, ConnectionData.DefaultResPassword, ConnectionData.DefaultResUserBase, ConnectionData.DefaultResOracleJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Res, VendorType.Oracle, null, ConnType.Web,  ConnectionData.DefaultResUser, ConnectionData.DefaultResPassword, ConnectionData.DefaultResUserBase, ConnectionData.DefaultResOracleWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Res, VendorType.JavaDb, null, ConnType.Jdbc, ConnectionData.DefaultResUser, ConnectionData.DefaultResPassword, ConnectionData.DefaultResUserBase, ConnectionData.DefaultResJavaDbJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Res, VendorType.JavaDb, null, ConnType.Web,  ConnectionData.DefaultResUser, ConnectionData.DefaultResPassword, ConnectionData.DefaultResUserBase, ConnectionData.DefaultResJavaDbWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Res, VendorType.Postgres, null, ConnType.Jdbc, ConnectionData.DefaultResUser, ConnectionData.DefaultResPassword, ConnectionData.DefaultResUserBase, ConnectionData.DefaultResPgJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Res, VendorType.Postgres, null, ConnType.Web,  ConnectionData.DefaultResUser, ConnectionData.DefaultResPassword, ConnectionData.DefaultResUserBase, ConnectionData.DefaultResPgWebUrl);
            connections.put(ci.keyUser, ci);
            
        
        ci = new ConnectionData(pref, StorageType.Backup, VendorType.Oracle, null, ConnType.Jdbc, ConnectionData.DefaultBackupUser, ConnectionData.DefaultBackupPassword, ConnectionData.DefaultBackupUserBase, ConnectionData.DefaultBackupOracleJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Backup, VendorType.Oracle, null, ConnType.Web,  ConnectionData.DefaultBackupUser, ConnectionData.DefaultBackupPassword, ConnectionData.DefaultBackupUserBase, ConnectionData.DefaultBackupOracleWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Backup, VendorType.JavaDb, null, ConnType.Jdbc, ConnectionData.DefaultBackupUser, ConnectionData.DefaultBackupPassword, ConnectionData.DefaultBackupUserBase, ConnectionData.DefaultBackupJavaDbJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Backup, VendorType.JavaDb, null, ConnType.Web,  ConnectionData.DefaultBackupUser, ConnectionData.DefaultBackupPassword, ConnectionData.DefaultBackupUserBase, ConnectionData.DefaultBackupJavaDbWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Backup, VendorType.Postgres, null, ConnType.Jdbc, ConnectionData.DefaultBackupUser, ConnectionData.DefaultBackupPassword, ConnectionData.DefaultBackupUserBase, ConnectionData.DefaultBackupPgJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Backup, VendorType.Postgres, null, ConnType.Web,  ConnectionData.DefaultBackupUser, ConnectionData.DefaultBackupPassword, ConnectionData.DefaultBackupUserBase, ConnectionData.DefaultBackupPgWebUrl);
            connections.put(ci.keyUser, ci);
        
        ci = new ConnectionData(pref, StorageType.Naming, VendorType.Oracle, null, ConnType.Jdbc, ConnectionData.DefaultNamingUser, ConnectionData.DefaultNamingPassword, ConnectionData.DefaultNamingUserBase, ConnectionData.DefaultNamingOracleJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Naming, VendorType.Oracle, null, ConnType.Web,  ConnectionData.DefaultNamingUser, ConnectionData.DefaultNamingPassword, ConnectionData.DefaultNamingUserBase, ConnectionData.DefaultNamingOracleWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Naming, VendorType.JavaDb, null, ConnType.Jdbc, ConnectionData.DefaultNamingUser, ConnectionData.DefaultNamingPassword, ConnectionData.DefaultNamingUserBase, ConnectionData.DefaultNamingJavaDbJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Naming, VendorType.JavaDb, null, ConnType.Web,  ConnectionData.DefaultNamingUser, ConnectionData.DefaultNamingPassword, ConnectionData.DefaultNamingUserBase, ConnectionData.DefaultNamingJavaDbWebUrl);
            connections.put(ci.keyUser, ci);
            
        ci = new ConnectionData(pref, StorageType.Ancillary, VendorType.Oracle, null, ConnType.Jdbc, ConnectionData.DefaultAncillaryUser, ConnectionData.DefaultAncillaryPassword, ConnectionData.DefaultAncillaryUserBase, ConnectionData.DefaultAncillaryOracleJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Ancillary, VendorType.Oracle, null, ConnType.Web,  ConnectionData.DefaultAncillaryUser, ConnectionData.DefaultAncillaryPassword, ConnectionData.DefaultAncillaryUserBase, ConnectionData.DefaultAncillaryOracleWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Ancillary, VendorType.JavaDb, null, ConnType.Jdbc, ConnectionData.DefaultAncillaryUser, ConnectionData.DefaultAncillaryPassword, ConnectionData.DefaultAncillaryUserBase, ConnectionData.DefaultAncillaryJavaDbJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Ancillary, VendorType.JavaDb, null, ConnType.Web,  ConnectionData.DefaultAncillaryUser, ConnectionData.DefaultAncillaryPassword, ConnectionData.DefaultAncillaryUserBase, ConnectionData.DefaultAncillaryJavaDbWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Ancillary, VendorType.Postgres, null, ConnType.Jdbc, ConnectionData.DefaultAncillaryUser, ConnectionData.DefaultAncillaryPassword, ConnectionData.DefaultAncillaryUserBase, ConnectionData.DefaultAncillaryPgJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Ancillary, VendorType.Postgres, null, ConnType.Web,  ConnectionData.DefaultAncillaryUser, ConnectionData.DefaultAncillaryPassword, ConnectionData.DefaultAncillaryUserBase, ConnectionData.DefaultAncillaryPgWebUrl);
            connections.put(ci.keyUser, ci);
            
        ci = new ConnectionData(pref, StorageType.Battery, VendorType.Oracle, null, ConnType.Jdbc, ConnectionData.DefaultBatteryUser, ConnectionData.DefaultBatteryPassword, ConnectionData.DefaultBatteryUserBase, ConnectionData.DefaultBatteryOracleJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Battery, VendorType.Oracle, null, ConnType.Web,  ConnectionData.DefaultBatteryUser, ConnectionData.DefaultBatteryPassword, ConnectionData.DefaultBatteryUserBase, ConnectionData.DefaultBatteryOracleWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Battery, VendorType.JavaDb, null, ConnType.Jdbc, ConnectionData.DefaultBatteryUser, ConnectionData.DefaultBatteryPassword, ConnectionData.DefaultBatteryUserBase, ConnectionData.DefaultBatteryJavaDbJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Battery, VendorType.JavaDb, null, ConnType.Web,  ConnectionData.DefaultBatteryUser, ConnectionData.DefaultBatteryPassword, ConnectionData.DefaultBatteryUserBase, ConnectionData.DefaultBatteryJavaDbWebUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Battery, VendorType.Postgres, null, ConnType.Jdbc, ConnectionData.DefaultBatteryUser, ConnectionData.DefaultBatteryPassword, ConnectionData.DefaultBatteryUserBase, ConnectionData.DefaultBatteryPgJdbcUrl);
            connections.put(ci.keyUser, ci);
        ci = new ConnectionData(pref, StorageType.Battery, VendorType.Postgres, null, ConnType.Web,  ConnectionData.DefaultBatteryUser, ConnectionData.DefaultBatteryPassword, ConnectionData.DefaultBatteryUserBase, ConnectionData.DefaultBatteryPgWebUrl);
            connections.put(ci.keyUser, ci);
    }

    //TODO: context endedup not being used, might remove
    public void connect(final ConnectionData conn, final String domain, final boolean requireAuth, final SwappedFocusListener callback, final Object context)
    {
        dbAdmin.setLeftMessage("Attempting to connect to: " + toString());
        dbAdmin.setBusy(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                Throwable exception = null;
                HashMap<String, String> params = new HashMap<String, String>();
                try
                {
                    switch(conn.vendorType)
                    {
                        case JavaDb:
                            params.put(JdbDatastoreBase.UserKey, conn.getUser());
                            params.put(JdbDatastoreBase.PasswordKey, conn.getPassword());
                            params.put(JdbDatastoreBase.UrlKey, conn.getUrl());
                            params.put(JavaDbStorage.ConfigureKey, "true");
                            params.put(JdbDatastoreBase.RequireAuthenticationKey, ""+requireAuth);
                            params.put(JdbDatastoreBase.HomeKey, conn.getHome());
                            
                            JdbDatastoreBase.setRemoteHome(conn.getHome());
                            conn.dataAccessor = getSimpleConfiguredDataAccessor(conn, domain, params);
                            if(conn.dataAccessor instanceof JdbDatastoreBase)
                                conn.jdbcConnection = ((JdbDatastoreBase)conn.dataAccessor).getReadConnection();
                            break;
                        case Oracle:
                            switch(conn.storageType)
                            {
                                case As:
                                    params = new HashMap<String, String>();
                                    params.put(OracleStorageManagement.USER_KEY, conn.getUser());
                                    params.put(OracleStorageManagement.PASSWORD_KEY, conn.getPassword());
                                    params.put(OracleStorageManagement.URL_KEY, conn.getUrl());
                                    params.put(JdbDatastoreBase.RequireAuthenticationKey, ""+requireAuth);
                                    conn.dataAccessor = getSimpleConfiguredDataAccessor(conn, domain, params);
                                    break;
                                case Backup:
                                    conn.dataAccessor = new OracleDataAccessor(conn);
                                    break;
                                case Naming:
                                    break;
                                case Res:
                                    conn.dataAccessor = new OracleDataAccessor(conn);
                                    break;
                                case Ancillary:
                                    conn.dataAccessor = new OracleDataAccessor(conn);
                                    break;
                                case Battery:
                                    conn.dataAccessor = new OracleDataAccessor(conn);
                                    break;
                            }
                            break;
                        case Postgres:
                            switch(conn.storageType)
                            {
                                case As:
                                    conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
                                    break;
                                case Backup:
                                    conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
                                    break;
                                case Naming:
                                    break;
                                case Res:
                                    conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
                                    break;
                                case Ancillary:
                                    conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
                                    break;
                                case Battery:
                                    conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
                                    break;
                            }
                            break;
                    }
                }catch(Throwable e)
                {
                    exception = e;
                }
                final Object theContext = exception != null ? exception : context;
                Runnable runner = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(theContext instanceof Throwable)
                            conn.connected = false;
                        else
                            conn.connected = true;
                        dbAdmin.setBusy(false);
                        callback.focusRequested(theContext);
                    }
                };
                SwingUtilities.invokeLater(runner);
                return null;
            }
        };
        worker.execute();
    }
    
    public void disconnect(final ConnectionData conn, final SwappedFocusListener callback)
    {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                Throwable exception = null;
                try
                {
                    Method[] methods = conn.dataAccessor.getClass().getMethods();
                    int index = 0;
                    for(int i=0; i < methods.length; i++)
                    {
                        if(methods[i].getName().equals("Shutdown"))
                        {
                            index = i;
                            break;
                        }
                        if(methods[i].getName().equals("shutdown"))
                        {
                            index = i;
                            break;
                        }
                        if(methods[i].getName().equals("destroy"))
                        {
                            index = i;
                            break;
                        }
                        if(methods[i].getName().equals("close"))
                        {
                            index = i;
                            break;
                        }
                    }
                    methods[index].invoke(conn.dataAccessor);
                }catch(Throwable e)
                {
                    exception = e;
                }
                final Throwable theException = exception;
                Runnable runner = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        conn.connected = false;
                        conn.dataAccessor = null;
                        if(callback != null)
                            callback.focusRequested(theException);
                    }
                };
                SwingUtilities.invokeLater(runner);
                return null;
            }
        };
        worker.execute();
    }

    public void initializeAs(
            final ConnectionData conn, 
            final boolean autoCreate, final boolean requireAuth, final boolean createGlobal,
            final DomainData data, final DomainData clusterData, final Object clusterDa, 
            final SwappedFocusListener callback)
    {
        dbAdmin.setLeftMessage("Attempting to initialize: " + toString());
        dbAdmin.setBusy(true);
        final DomainStorageManagement theClusterDa = (DomainStorageManagement) clusterDa;
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                Throwable exception = null;
                HashMap<String, String> params = new HashMap<String, String>();
                try
                {
                    switch(conn.vendorType)
                    {
                        case JavaDb:
                            if(autoCreate)
                            {
                              JdbAsdbSqlGenerator asdbGenerator = new JdbAsdbSqlGenerator("asjdb");
                              asdbGenerator.setAsjdb(true, true);
                              asdbGenerator.setUseSameConnection(true, true);
                              asdbGenerator.setUser(conn.getUser(), true);
                              asdbGenerator.setPassword(conn.getPassword(), true);
                              asdbGenerator.setUrl(conn.getUrl(), true);
                              asdbGenerator.setDomain(data.getDomain(), true);
                              JdbBaseGenerator jdbbaseGenerator = new JdbBaseGenerator(new String[]{}, new Commands[]{asdbGenerator});
                              jdbbaseGenerator.setDelete(true, true);
                              jdbbaseGenerator.setCreate(true, true);
                              jdbbaseGenerator.setAdminSchema(true, true);
                              jdbbaseGenerator.setUrl(conn.getUrl(), true);
                              jdbbaseGenerator.setDbHome(conn.getHome(), true);
                              jdbbaseGenerator.setMasters(false, false);
                              jdbbaseGenerator.setAuthorization(requireAuth, true);
                              JdbSqlUtil util = new JdbSqlUtil(jdbbaseGenerator);
                              util.run();
                              util.close();
                            }
                            params.put(JdbDatastoreBase.UserKey, conn.getUser());
                            params.put(JdbDatastoreBase.PasswordKey, conn.getPassword());
                            params.put(JdbDatastoreBase.UrlKey, conn.getUrl());
                            params.put(JavaDbStorage.ConfigureKey, "true");
                            params.put(JdbDatastoreBase.RequireAuthenticationKey, ""+requireAuth);
                            conn.dataAccessor = getSimpleConfiguredDataAccessor(conn, null, params);
                            if(conn.domainType == DomainType.Rg)
                                break;
                            new AsDomainInitializer((DomainStorageManagement) conn.dataAccessor, theClusterDa, data, clusterData).run();
                            break;
                        case Oracle:
                            if(autoCreate)
                            {
                                OracleDatabaseClear odc = new OracleDatabaseClear(data.getDomainOid(), conn.getUser(), conn.getPassword(), conn.getUrl());
                                odc.run();
                            }
                            params = new HashMap<String, String>();
                            params.put(OracleStorageManagement.USER_KEY, conn.getUser());
                            params.put(OracleStorageManagement.PASSWORD_KEY, conn.getPassword());
                            params.put(OracleStorageManagement.URL_KEY, conn.getUrl());
                            params.put(JdbDatastoreBase.RequireAuthenticationKey, ""+requireAuth);
                            conn.dataAccessor = getSimpleConfiguredDataAccessor(conn, data.getDomain(), params);
                            new AsDomainInitializer((DomainStorageManagement) conn.dataAccessor, theClusterDa, data, clusterData).run();
                            break;
                        case Postgres:
                            if(autoCreate)
                            {
                                String[] urlValues = conn.getUrl().split(",");
                                String[] args = new String[16];
                                String[] gargs = new String[args.length];
                                args[0] = "-host";
                                args[1] = urlValues[0];
                                args[2] = "-port";
                                args[3] = urlValues[1];
                                args[4] = "-dbaUser";
                                args[5] = urlValues[3];
                                args[6] = "-dbaPass";
                                args[7] = urlValues[4];
                                args[8] = "-tablespace";
                                args[9] = data.getTablespace();
                                args[10] = "-user";
                                args[11] = conn.getUserBase()+"_user";
                                args[12] = "-manager";
                                args[13] = conn.getUserBase()+"_manager";
                                args[14] = "-admin";
                                args[15] = conn.getUserBase()+"_admin";
                                System.arraycopy(args, 0, gargs, 0, args.length);
                                gargs[11] = "global_"+conn.getUserBase()+"_user";
                                gargs[13] = "global_"+conn.getUserBase()+"_manager";
                                gargs[15] = "global_"+conn.getUserBase()+"_admin";

                                String domainIndex = "0";
                                if(createGlobal)
                                    domainIndex = "1";
                                PgGlobalSqlGenerator globalGenerator = null;
                                globalGenerator = new PgGlobalSqlGenerator(gargs, "0", PgGlobalSqlGenerator.DefaultGlobalSchema, PgDomainSqlGenerator.DefaultClusterDomainStr, PgDomainSqlGenerator.DefaultResDomainStr, PgDomainSqlGenerator.DefaultBatteryDomainStr, PgDomainSqlGenerator.DefaultAncillaryDomainStr, PgDomainSqlGenerator.DefaultFactoryDomainStr);
                                PgDomainSqlGenerator domainGenerator = null;
                                if(data.getDomain().contains(PgDomainSqlGenerator.DefaultClusterSchema))
                                    domainGenerator = new PgDomainSqlGenerator(args, domainIndex, PgDomainSqlGenerator.DefaultClusterSchema, PgGlobalSqlGenerator.DefaultGlobalSchema, PgDomainSqlGenerator.DefaultClusterDomainStr);
                                else if(data.getDomain().contains(PgDomainSqlGenerator.DefaultResSchema))
                                    domainGenerator = new PgDomainSqlGenerator(args, domainIndex, PgDomainSqlGenerator.DefaultResSchema, PgGlobalSqlGenerator.DefaultGlobalSchema, PgDomainSqlGenerator.DefaultResDomainStr);
                                else if(data.getDomain().contains(PgDomainSqlGenerator.DefaultBatterySchema))
                                    domainGenerator = new PgDomainSqlGenerator(args, domainIndex, PgDomainSqlGenerator.DefaultBatterySchema, PgGlobalSqlGenerator.DefaultGlobalSchema, PgDomainSqlGenerator.DefaultBatteryDomainStr);
                                else if(data.getDomain().contains(PgDomainSqlGenerator.DefaultAncillarySchema))
                                    domainGenerator = new PgDomainSqlGenerator(args, domainIndex, PgDomainSqlGenerator.DefaultAncillarySchema, PgGlobalSqlGenerator.DefaultGlobalSchema, PgDomainSqlGenerator.DefaultAncillaryDomainStr);
                                else if(data.getDomain().contains(PgDomainSqlGenerator.DefaultFactorySchema))
                                    domainGenerator = new PgDomainSqlGenerator(args, domainIndex, PgDomainSqlGenerator.DefaultFactorySchema, PgGlobalSqlGenerator.DefaultGlobalSchema, PgDomainSqlGenerator.DefaultFactoryDomainStr);
                                else
                                    throw new Exception("unknown domain: " + data.getDomain());
                                PgSqlGeneratorBase[] aggregateCommands = new PgSqlGeneratorBase[createGlobal ? 2 : 1];
                                if(createGlobal)
                                {
                                    aggregateCommands[0] = globalGenerator;
                                    aggregateCommands[1] = domainGenerator;
                                }else
                                    aggregateCommands[0] = domainGenerator;
                                PgBaseGenerator commands = new PgBaseGenerator(createGlobal ? gargs : args, aggregateCommands);
                                String sqlPath = aggregateCommands[0].getSqlPath(PgSqlGeneratorBase.DefaultSqlOuputPath);
//                                boolean useCustomTs = aggregateCommands[0].isUseCustomTablespace(true);
                                aggregateCommands[0].setupSqlOutputPath(sqlPath);
//                                aggregateCommands[0].setUseCustomTablespace(useCustomTs, false);
                                if(createGlobal)
                                {
                                    aggregateCommands[1].setupSqlOutputPath(sqlPath);
                                    aggregateCommands[1].setUser(args[11], false);
                                    aggregateCommands[1].setManager(args[13], false);
                                    aggregateCommands[1].setAdmin(args[15], false);
                                    aggregateCommands[1].setTablespace(args[9], false);
                                }
                                commands.setDbName(urlValues[2], false);
                                commands.setCreate(true, false);
                                commands.setDelete(true, false);
                                commands.setMasters(false, false);
                                
                                int i = 0;
                                if(!createGlobal)
                                    i = 3;
                                
                                for(; i < 6; i++)
                                {
                                    PgConnectionPoolCommands pgcpc = new PgConnectionPoolCommands();
                                    pgcpc.setHost(commands.getHost(urlValues[0]));
                                    pgcpc.setPort(urlValues[1]);
                                    pgcpc.setDatabaseName(urlValues[2]);
                                    switch(i)
                                    {
                                        case 0:
                                            globalGenerator.setConnectionPoolCommands(gargs[15], pgcpc);
                                            break;
                                        case 1:
                                            globalGenerator.setConnectionPoolCommands(gargs[13], pgcpc);
                                            break;
                                        case 2:
                                            globalGenerator.setConnectionPoolCommands(gargs[11], pgcpc);
                                            break;
                                        case 3:
                                            domainGenerator.setConnectionPoolCommands(args[15], pgcpc);
                                            break;
                                        case 4:
                                            domainGenerator.setConnectionPoolCommands(args[13], pgcpc);
                                            break;
                                        case 5:
                                            domainGenerator.setConnectionPoolCommands(args[11], pgcpc);
                                            break;
                                    }
                                }
                                PgSqlUtil util = new PgSqlUtil(commands);
                                util.run();
                                util.close();
                            }
                            conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
                            new AsDomainInitializer((DomainStorageManagement) conn.dataAccessor, theClusterDa, data, clusterData).run();
                            break;
                        
                    }
                }catch(Throwable e)
                {
                    exception = e;
                }
                final Throwable theException = exception;
                Runnable runner = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        conn.connected = true;
                        if(theException != null)
                            conn.connected = false;
                        dbAdmin.setBusy(false);
                        callback.focusRequested(theException);
                    }
                };
                SwingUtilities.invokeLater(runner);
                return null;
            }
        };
        worker.execute();
    }
    
    public void initializeRes(final CannedResData data, final boolean autoCreate, final SwappedFocusListener callback)
    {
        dbAdmin.setLeftMessage("Attempting to initialize: " + toString());
        dbAdmin.setBusy(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                Throwable exception = null;
                try
                {
                    switch(data.connections.currentlySelected.vendorType)
                    {
                        case JavaDb:
                            throw new Exception("not supported");
                        case Oracle:
                            if(autoCreate)
                            {
                                OraResSqlGenerator resGenerator = new OraResSqlGenerator("res");
                                resGenerator.setRes(true, true);
                                resGenerator.setUseSameConnection(true, true);
//                                resGenerator.setUser(data.connData.getUser(), true);
                                resGenerator.setUser(data.connections.currentlySelected.getUser(), true);
//                                resGenerator.setPassword(data.connData.getPassword(), true);
                                resGenerator.setPassword(data.connections.currentlySelected.getPassword(), true);
//                                resGenerator.setUrl(data.connData.getUrl(), true);
                                resGenerator.setUrl(data.connections.currentlySelected.getUrl(), true);
//                                OracleConnectionPoolCommands ocpc = new OracleConnectionPoolCommands();
//                                ocpc.setUrl(resGenerator.getUrl());
//                                resGenerator.setConnectionPoolCommands(data.connData.getUser(), ocpc);
//                                resGenerator.setConnectionPoolCommands(data.connections.currentlySelected.getUser(), ocpc);
                                OraBaseGenerator oraclebaseGenerator = new OraBaseGenerator(new String[]{}, new Commands[]{resGenerator});
                                oraclebaseGenerator.setDelete(true, true);
                                oraclebaseGenerator.setCreate(true, true);
                                oraclebaseGenerator.setAdminSchema(true, true);
                                oraclebaseGenerator.setTablespace(false, true);
//                                oraclebaseGenerator.setUrl(data.connData.getUrl(), true);
                                oraclebaseGenerator.setUrl(data.connections.currentlySelected.getUrl(), true);
                                oraclebaseGenerator.setMasters(false, false);
                                
                                for(int i=0; i < 3; i++)
                                {
                                    OracleConnectionPoolCommands ocpc = new OracleConnectionPoolCommands();
                                    ocpc.setUrl(resGenerator.getUrl());
                                    switch(i)
                                    {
                                        case 0:
                                            resGenerator.setConnectionPoolCommands(resGenerator.component.admin, ocpc);
                                            break;
                                        case 1:
                                            resGenerator.setConnectionPoolCommands(resGenerator.component.manager, ocpc);
                                            break;
                                        case 2:
                                            resGenerator.setConnectionPoolCommands(resGenerator.component.user, ocpc);
                                            break;
                                    }
                                }
                                
                                
                                OraSqlUtil util = new OraSqlUtil(oraclebaseGenerator);
                                util.run();
                                util.close();
                                data.connections.currentlySelected.dataAccessor = new OracleDataAccessor(data.connections.currentlySelected).dataAccessor;
                                data.connData = data.connections.currentlySelected;
                            }
                            OracleResManager orm = new OracleResManager((OraResPlatformManagerImpl)data.connData.dataAccessor);
                            CannedResInitializer cannedInit = new CannedResInitializer(orm);
                            cannedInit.setCannedData(data); 
                            cannedInit.run(); 
                            break;
                        case Postgres:
                            if(autoCreate)
                            {
                                ConnectionData conn = data.connections.currentlySelected;
                                String[] urlValues = conn.getUrl().split(",");
                                String[] args = new String[16];
                                args[0] = "-host";
                                args[1] = urlValues[0];
                                args[2] = "-port";
                                args[3] = urlValues[1];
                                args[4] = "-dbaUser";
                                args[5] = urlValues[3];
                                args[6] = "-dbaPass";
                                args[7] = urlValues[4];
                                args[8] = "-tablespace";
                                args[9] = data.getTablespace();
                                args[10] = "-user";
                                args[11] = conn.getUserBase()+"_user";
                                args[12] = "-manager";
                                args[13] = conn.getUserBase()+"_manager";
                                args[14] = "-admin";
                                args[15] = conn.getUserBase()+"_admin";
                                
                                PgResSqlGenerator resGenerator = new PgResSqlGenerator(args, "0", PgResSqlGenerator.DefaultResSchema);
                                PgSqlGeneratorBase[] aggregateCommands = new PgSqlGeneratorBase[1];
                                aggregateCommands[0] = resGenerator;
                                PgBaseGenerator commands = new PgBaseGenerator(args, aggregateCommands);
                                String sqlPath = aggregateCommands[0].getSqlPath(PgSqlGeneratorBase.DefaultSqlOuputPath);
                                aggregateCommands[0].setupSqlOutputPath(sqlPath);
                                commands.setDbName(urlValues[2], false);
                                commands.setCreate(true, false);
                                commands.setDelete(true, false);
                                commands.setMasters(false, false);
                                for(int i=0; i < 3; i++)
                                {
                                    PgConnectionPoolCommands pgcpc = new PgConnectionPoolCommands();
                                    pgcpc.setHost(commands.getHost(urlValues[0]));
                                    pgcpc.setPort(urlValues[1]);
                                    pgcpc.setDatabaseName(urlValues[2]);
                                    switch(i)
                                    {
                                        case 0:
                                            resGenerator.setConnectionPoolCommands(args[15], pgcpc);
                                            break;
                                        case 1:
                                            resGenerator.setConnectionPoolCommands(args[13], pgcpc);
                                            break;
                                        case 2:
                                            resGenerator.setConnectionPoolCommands(args[11], pgcpc);
                                            break;
                                    }
                                }
                                PgSqlUtil util = new PgSqlUtil(commands);
                                util.run();
                                util.close();
                                data.connections.currentlySelected.dataAccessor = new PostgresDataAccessor(data.connections.currentlySelected).dataAccessor;
                                data.connData = data.connections.currentlySelected;
                            }
                            PgResManager pgrm = new PgResManager((PgResPlatformManagerImpl)data.connData.dataAccessor);
                            cannedInit = new CannedResInitializer(pgrm);
                            cannedInit.setCannedData(data); 
                            cannedInit.run(); 
                            break;
                    }
                }catch(Throwable e)
                {
                    exception = e;
                }
                final Throwable theException = exception;
                Runnable runner = new Runnable()
                {
                    @Override
                    public void run()
                    {
//                        data.connData.connected = true;
//                        if(data.connData.domainType == DomainType.Rg || theException != null)
//                            data.connData.connected = false;
                        dbAdmin.setBusy(false);
                        callback.focusRequested(theException);
                    }
                };
                SwingUtilities.invokeLater(runner);
                return null;
            }
        };
        worker.execute();
    }
    
    public void initializeBackup(final ConnectionData conn, final boolean autoCreate, final String tablespace, final SwappedFocusListener callback)
    {
        dbAdmin.setLeftMessage("Attempting to initialize: " + toString());
        dbAdmin.setBusy(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                Throwable exception = null;
                try
                {
                    switch(conn.vendorType)
                    {
                        case JavaDb:
                            throw new Exception("not supported");
                        case Oracle:
                            OracleDataAccessor oracleAccessor = (OracleDataAccessor)conn.dataAccessor; 
                            OraBackupInitializer oraBuInit = (OraBackupInitializer)oracleAccessor.dataAccessor;  
//                            oraBuInit.setCannedData(data); 
                            oraBuInit.setConnectionData(conn); 
                            oraBuInit.run(); 
                            break;
                        case Postgres:
                            if(autoCreate)
                            {
                                String[] urlValues = conn.getUrl().split(",");
                                String[] args = new String[16];
                                args[0] = "-host";
                                args[1] = urlValues[0];
                                args[2] = "-port";
                                args[3] = urlValues[1];
                                args[4] = "-dbaUser";
                                args[5] = urlValues[3];
                                args[6] = "-dbaPass";
                                args[7] = urlValues[4];
                                args[8] = "-tablespace";
                                args[9] = tablespace;
                                args[10] = "-user";
                                args[11] = conn.getUserBase()+"_user";
                                args[12] = "-manager";
                                args[13] = conn.getUserBase()+"_manager";
                                args[14] = "-admin";
                                args[15] = conn.getUserBase()+"_admin";
                                
                                PgBackupSqlGenerator backupGenerator = new PgBackupSqlGenerator(args, "0", PgBackupSqlGenerator.DefaultBackupSchema);
                                PgSqlGeneratorBase[] aggregateCommands = new PgSqlGeneratorBase[1];
                                aggregateCommands[0] = backupGenerator;
                                PgBaseGenerator commands = new PgBaseGenerator(args, aggregateCommands);
                                String sqlPath = aggregateCommands[0].getSqlPath(PgSqlGeneratorBase.DefaultSqlOuputPath);
                                aggregateCommands[0].setupSqlOutputPath(sqlPath);
                                commands.setDbName(urlValues[2], false);
                                commands.setCreate(true, false);
                                commands.setDelete(true, false);
                                commands.setMasters(false, false);
                                for(int i=0; i < 3; i++)
                                {
                                    PgConnectionPoolCommands pgcpc = new PgConnectionPoolCommands();
                                    pgcpc.setHost(commands.getHost(urlValues[0]));
                                    pgcpc.setPort(urlValues[1]);
                                    pgcpc.setDatabaseName(urlValues[2]);
                                    switch(i)
                                    {
                                        case 0:
                                            backupGenerator.setConnectionPoolCommands(args[15], pgcpc);
                                            break;
                                        case 1:
                                            backupGenerator.setConnectionPoolCommands(args[13], pgcpc);
                                            break;
                                        case 2:
                                            backupGenerator.setConnectionPoolCommands(args[11], pgcpc);
                                            break;
                                    }
                                }
                                PgSqlUtil util = new PgSqlUtil(commands);
                                util.run();
                                util.close();
                                conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
//                                data.connData = data.connections.currentlySelected;
                            }
                            PgBackupInitializer pgBuInit = (PgBackupInitializer)conn.dataAccessor; 
//                            pgBuInit.setCannedData(data); 
                            pgBuInit.run(); 
                            break;
                    }
                }catch(Throwable e)
                {
                    exception = e;
                }
                final Throwable theException = exception;
                Runnable runner = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dbAdmin.setBusy(false);
                        callback.focusRequested(theException);
                    }
                };
                SwingUtilities.invokeLater(runner);
                return null;
            }
        };
        worker.execute();
    }
    
    public void initializeBattery(final ConnectionData conn, final boolean autoCreate, final String tablespace, final SwappedFocusListener callback)
    {
        dbAdmin.setLeftMessage("Attempting to initialize: " + toString());
        dbAdmin.setBusy(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                Throwable exception = null;
                try
                {
                    switch(conn.vendorType)
                    {
                        case JavaDb:
                            throw new Exception("not supported");
                        case Oracle:
                            throw new Exception("not supported");
                        case Postgres:
                            if(autoCreate)
                            {
                                String[] urlValues = conn.getUrl().split(",");
                                String[] args = new String[16];
                                args[0] = "-host";
                                args[1] = urlValues[0];
                                args[2] = "-port";
                                args[3] = urlValues[1];
                                args[4] = "-dbaUser";
                                args[5] = urlValues[3];
                                args[6] = "-dbaPass";
                                args[7] = urlValues[4];
                                args[8] = "-tablespace";
                                args[9] = tablespace;
                                args[10] = "-user";
                                args[11] = conn.getUserBase()+"_user";
                                args[12] = "-manager";
                                args[13] = conn.getUserBase()+"_manager";
                                args[14] = "-admin";
                                args[15] = conn.getUserBase()+"_admin";
                                
                                
                                
                                PgBatteryPlatformSqlGenerator batterydbGenerator = new PgBatteryPlatformSqlGenerator(args, "0", PgBatteryPlatformSqlGenerator.DefaultBatteryPlatformSchema);
                                PgSqlGeneratorBase[] aggregateCommands = new PgSqlGeneratorBase[1];
                                aggregateCommands[0] = batterydbGenerator;
                                PgBatteryDbTestCommands commands = new PgBatteryDbTestCommands(args, aggregateCommands);
                                String sqlPath = aggregateCommands[0].getSqlPath(PgSqlGeneratorBase.DefaultSqlOuputPath);
                                aggregateCommands[0].setupSqlOutputPath(sqlPath);
                                commands.setDbName(PgBatteryPlatformComponentHelper.DefaultDatabaseName, false);
                                commands.setCreate(true, false);
                                commands.setDelete(true, false);
                                commands.setMasters(false, false);
                                
                                for(int i=0; i < 3; i++)
                                {
                                    PgConnectionPoolCommands pgcpc = new PgConnectionPoolCommands();
                                    pgcpc.setHost(commands.getHost(urlValues[0]));
                                    pgcpc.setPort(urlValues[1]);
                                    pgcpc.setDatabaseName(urlValues[2]);
                                    switch(i)
                                    {
                                        case 0:
                                            batterydbGenerator.setConnectionPoolCommands(args[15], pgcpc);
                                            break;
                                        case 1:
                                            batterydbGenerator.setConnectionPoolCommands(args[13], pgcpc);
                                            break;
                                        case 2:
                                            batterydbGenerator.setConnectionPoolCommands(args[11], pgcpc);
                                            break;
                                    }
                                }
                                PgSqlUtil util = new PgSqlUtil(commands);
                                util.run();
                                util.close();
                                conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
//                                data.connData = data.connections.currentlySelected;
                            }
//                            PgBatteryPlatformDataAccessor batterDa = (PgBatteryPlatformDataAccessor)conn.dataAccessor; 
//                            pgBuInit.setCannedData(data); 
//                            batterDa.run(); 
                            break;
                    }
                }catch(Throwable e)
                {
                    exception = e;
                }
                final Throwable theException = exception;
                Runnable runner = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dbAdmin.setBusy(false);
                        callback.focusRequested(theException);
                    }
                };
                SwingUtilities.invokeLater(runner);
                return null;
            }
        };
        worker.execute();
    }
    
    public void initializeNaming(final ConnectionData conn, final boolean requireAuth, final SwappedFocusListener callback)
    {
        dbAdmin.setLeftMessage("Attempting to initialize: " + toString());
        dbAdmin.setBusy(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                Throwable exception = null;
                HashMap<String, String> params = new HashMap<String, String>();
                try
                {
                    JdbNamingSqlGenerator namingGenerator = new JdbNamingSqlGenerator("naming");
                    namingGenerator.setNaming(true, true);
                    namingGenerator.setUseSameConnection(true, true);
                    namingGenerator.setUser(conn.getUser(), true);
                    namingGenerator.setPassword(conn.getPassword(), true);
                    namingGenerator.setUrl(conn.getUrl(), true);
                    JdbBaseGenerator jdbbaseGenerator = new JdbBaseGenerator(new String[]{}, new Commands[]{namingGenerator});
                    jdbbaseGenerator.setDelete(true, true);
                    jdbbaseGenerator.setCreate(true, true);
                    jdbbaseGenerator.setAdminSchema(true, true);
                    jdbbaseGenerator.setUrl(conn.getUrl(), true);
                    jdbbaseGenerator.setDbHome(conn.getHome(), true);
                    jdbbaseGenerator.setMasters(false, false);
                    JdbSqlUtil util = new JdbSqlUtil(jdbbaseGenerator);
                    util.run();
                    util.close();
                    params.put(JdbDatastoreBase.UserKey, conn.getUser());
                    params.put(JdbDatastoreBase.PasswordKey, conn.getPassword());
                    params.put(JdbDatastoreBase.UrlKey, conn.getUrl());
                    params.put(JavaDbStorage.ConfigureKey, "true");
                    params.put(JdbDatastoreBase.RequireAuthenticationKey, ""+requireAuth);
                    conn.dataAccessor = getSimpleConfiguredDataAccessor(conn, null, params);
                }catch(Throwable e)
                {
                    exception = e;
                }
                final Throwable theException = exception;
                Runnable runner = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dbAdmin.setBusy(false);
                        callback.focusRequested(theException);
                    }
                };
                SwingUtilities.invokeLater(runner);
                return null;
            }
        };
        worker.execute();
    }
    
    public void initializeAncPwr(final ConnectionData conn, final boolean autoCreate, final SwappedFocusListener callback)
    {
        dbAdmin.setLeftMessage("Attempting to initialize: " + toString());
        dbAdmin.setBusy(true);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                Throwable exception = null;
                try
                {
                    switch(conn.vendorType)
                    {
                        case JavaDb:
                            throw new Exception("not supported");
                        case Oracle:
                            throw new Exception("not supported");
                        case Postgres:
                            if(autoCreate)
                            {
                                String[] urlValues = conn.getUrl().split(",");
                                String[] args = new String[8];
                                args[0] = "-host";
                                args[1] = urlValues[0];
                                args[2] = "-port";
                                args[3] = urlValues[1];
                                args[4] = "-dbaUser";
                                args[5] = urlValues[3];
                                args[6] = "-dbaPass";
                                args[7] = urlValues[4];
                                PgBatteryPlatformSqlGenerator ancPwrGenerator = null; //new PgBmuStatusSqlGenerator(args, "0", PgBmuStatusSqlGenerator.DefaultEmsSchema);
                                PgSqlGeneratorBase[] aggregateCommands = new PgSqlGeneratorBase[1];
                                aggregateCommands[0] = ancPwrGenerator;
                                PgBaseGenerator commands = new PgBaseGenerator(args, aggregateCommands);
                                String sqlPath = aggregateCommands[0].getSqlPath(PgSqlGeneratorBase.DefaultSqlOuputPath);
                                aggregateCommands[0].setupSqlOutputPath(sqlPath);
                                commands.setDbName(urlValues[2], false);
                                commands.setCreate(true, false);
                                commands.setDelete(true, false);
                                commands.setMasters(false, false);
                                for(int i=0; i < 3; i++)
                                {
                                    PgConnectionPoolCommands pgcpc = new PgConnectionPoolCommands();
                                    pgcpc.setHost(commands.getHost(urlValues[0]));
                                    pgcpc.setPort(urlValues[1]);
                                    pgcpc.setDatabaseName(urlValues[2]);
                                    switch(i)
                                    {
                                        case 0:
                                            ancPwrGenerator.setConnectionPoolCommands(ancPwrGenerator.getScripts().component.admin, pgcpc);
                                            break;
                                        case 1:
                                            ancPwrGenerator.setConnectionPoolCommands(ancPwrGenerator.getScripts().component.manager, pgcpc);
                                            break;
                                        case 2:
                                            ancPwrGenerator.setConnectionPoolCommands(ancPwrGenerator.getScripts().component.user, pgcpc);
                                            break;
                                    }
                                }
                                PgSqlUtil util = new PgSqlUtil(commands);
                                util.run();
                                util.close();
                                conn.dataAccessor = new PostgresDataAccessor(conn).dataAccessor;
//                                data.connData = data.connections.currentlySelected;
                            }
//                            PgAncPwrInitializer pgAncPwrInit = (PgAncPwrInitializer)conn.dataAccessor; 
//                            pgBuInit.setCannedData(data); 
//                            pgAncPwrInit.run(); 
                            break;
                    }
                }catch(Throwable e)
                {
                    exception = e;
                }
                final Throwable theException = exception;
                Runnable runner = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        dbAdmin.setBusy(false);
                        callback.focusRequested(theException);
                    }
                };
                SwingUtilities.invokeLater(runner);
                return null;
            }
        };
        worker.execute();
    }
    
    private Object getSimpleConfiguredDataAccessor(ConnectionData conn, String domain, Map<String, String> params) throws Exception
    {
        Class<?> clazz = Class.forName(conn.getDataAccessor());
        Object dataAccessor = clazz.newInstance();
        Method method = null;
        if(domain != null)
        {
            Domain oid = null;
            if(domain != null && domain.length() > 0)
                oid = Domain.create(domain);
            Method[] methods = clazz.getMethods();
            for(int i=0; i < methods.length; i++)
            {
                if(methods[i].getName().equals("Init"))
                {
                    method = clazz.getMethod("Init", Domain.class, Map.class);
                    break;
                }
                if(methods[i].getName().equals("init"))
                {
                    method = clazz.getMethod("init", Domain.class, Map.class);
                    break;
                }
                if(methods[i].getName().equals("init"))
                {
                    if(oid == null)
                        method = clazz.getMethod("init", Map.class);
                    else
                        method = clazz.getMethod("init", Domain.class, Map.class);
                    break;
                }
            }
            if(method != null)
                method.invoke(dataAccessor, oid, params);
            else
                throw new Exception("Did not find an init method");
        }
        else
        {
            method = clazz.getMethod("init", Map.class);
            method.invoke(dataAccessor, params);
        }
        return dataAccessor;
    }
    
    public ConnectionData getConnection(StorageType stype, VendorType vtype, DomainType dtype,  ConnType ctype)
    {
        String prefix = DbAdmin.DbAdminPrefixKey + ".connection.";
        String key = prefix + "user." + stype.name() + "." + vtype.name() + "." + ctype.name();
        if(dtype != null)
            key = prefix + "user." + stype.name() + "." + vtype.name() + "." + dtype.name() + "." + ctype.name();
        return connections.get(key);
    }
    
    public void addMenu(JMenuBar menuBar, JToolBar toolBar)
    {
        // Connection Menu
        JMenu asMenu = new JMenu("Connections");
        asMenu.setMnemonic(DbAdmin.MenuConnectionsMnemonic);
        asConnectionAction = new AsConnectionAction(asMenu, toolBar);
        resConnectionAction = new ResConnectionAction(asMenu, toolBar);
        batteryConnectionAction = new BatteryConnectionAction(asMenu, toolBar);
        ancillaryConnectionAction = new AncillaryConnectionAction(asMenu, toolBar);
        backupConnectionAction = new BackupConnectionAction(asMenu, toolBar);
        namingConnectionAction = new NamingConnectionAction(asMenu, toolBar);
        new JMenuItem(asConnectionAction);
        new JMenuItem(resConnectionAction);
        new JMenuItem(batteryConnectionAction);
        new JMenuItem(ancillaryConnectionAction);
        new JMenuItem(backupConnectionAction);
        new JMenuItem(namingConnectionAction);
        menuBar.add(asMenu);
    }

    public void close()
    {
        for(Entry<String, ConnectionData> entry : connections.entrySet())
        {
            try
            {
                ConnectionData conn = entry.getValue(); 
                if(conn.connected)
                    disconnect(conn, null);
            }catch(Exception e){} // best try
        }
    }
    
    public void setBusy(boolean busy)
    {
        asConnectionAction.setEnabled(!busy);
        resConnectionAction.setEnabled(!busy);
        backupConnectionAction.setEnabled(!busy);
        namingConnectionAction.setEnabled(!busy);
        ancillaryConnectionAction.setEnabled(!busy);
    }
    
    final class AsConnectionAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "AS Connections...";
        public static final String MouseOver = "Setup AS connections";
        public final int Mnemonic = DbAdmin.AsConnectionActionMnemonic;
        public final int Accelerator = DbAdmin.AsConnectionActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public AsConnectionAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            dbAdmin.swapContentPane(new AsConnectionPanel(dbAdmin));
        }
    }

    final class ResConnectionAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Res Connections...";
        public static final String MouseOver = "Setup Residential Platform connections";
        public final int Mnemonic = DbAdmin.ResConnectionActionMnemonic;
        public final int Accelerator = DbAdmin.ResConnectionActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public ResConnectionAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            dbAdmin.swapContentPane(new ResConnectionPanel(dbAdmin));
        }
    }

    final class BackupConnectionAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Backup Connections...";
        public static final String MouseOver = "Setup Backup connections";
        public final int Mnemonic = DbAdmin.BackupConnectionActionMnemonic;
        public final int Accelerator = DbAdmin.BackupConnectionActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public BackupConnectionAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            dbAdmin.swapContentPane(new BackupConnectionPanel(dbAdmin));
        }
    }

    final class NamingConnectionAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Naming Connections...";
        public static final String MouseOver = "Setup Naming connections";
        public final int Mnemonic = DbAdmin.NamingConnectionActionMnemonic;
        public final int Accelerator = DbAdmin.NamingConnectionActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public NamingConnectionAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            dbAdmin.swapContentPane(new NamingConnectionPanel(dbAdmin));
        }
    }
    
    final class AncillaryConnectionAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Ancillary Power Connections...";
        public static final String MouseOver = "Setup Ancillary Power connections";
        public final int Mnemonic = DbAdmin.AncillaryConnectionActionMnemonic;
        public final int Accelerator = DbAdmin.AncillaryConnectionActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public AncillaryConnectionAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            dbAdmin.swapContentPane(new AncillaryConnectionPanel(dbAdmin));
        }
    }
    
    final class BatteryConnectionAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Battery Connections...";
        public static final String MouseOver = "Setup Battery connections";
        public final int Mnemonic = DbAdmin.BatteryConnectionActionMnemonic;
        public final int Accelerator = DbAdmin.BatteryConnectionActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public BatteryConnectionAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            dbAdmin.swapContentPane(new BatteryConnectionPanel(dbAdmin));
        }
    }
}
