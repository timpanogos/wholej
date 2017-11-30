/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */

package org.emitdo.research.app.dbAdmin.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.emitdo.app.res.db.oracle.OraDeviceJdbc;
import org.emitdo.app.res.db.oracle.OraInterfaceJdbc;
import org.emitdo.app.res.db.oracle.OraManagerDeviceJdbc;
import org.emitdo.app.res.db.oracle.OraManagerJdbc;
import org.emitdo.app.res.db.oracle.OraManagerVersionJdbc;
import org.emitdo.app.res.db.oracle.OraResJdbc;
import org.emitdo.app.res.db.oracle.OraVersionJdbc;
import org.emitdo.app.res.storage.ResPermission.All;
import org.emitdo.app.service.backup.DeviceBackupBaseInterface;
import org.emitdo.app.service.backup.DeviceRestoreBaseInterface;
import org.emitdo.app.service.logging.LoggingInterface;
import org.emitdo.app.service.naming.NamingInterface;
import org.emitdo.app.service.securityconfig.DeviceSecurityConfigInterface;
import org.emitdo.app.service.securityconfig.GatewaySecurityConfigInterface;
import org.emitdo.app.service.status.DeviceActivateInterface;
import org.emitdo.app.service.status.DeviceManageInterface;
import org.emitdo.app.service.update.UpdateManageInterface;
import org.emitdo.app.util.Commands;
import org.emitdo.db.common.oracle.OraCommon;
import org.emitdo.db.common.oracle.OraCommon.OraJdbcOid;
import org.emitdo.db.common.oracle.OraDatastoreBase;
import org.emitdo.db.common.oracle.OracleConnectionPoolCommands;
import org.emitdo.internal.app.res.db.oracle.OraResSqlGenerator;
import org.emitdo.internal.db.common.oracle.OraBaseGenerator;
import org.emitdo.internal.db.common.oracle.OraSqlUtil;
import org.emitdo.oal.DOFCredentials;
import org.emitdo.oal.DOFInterfaceID;
import org.emitdo.oal.DOFObjectID;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.oal.DOFObjectID.Domain;
import org.emitdo.oal.DOFUtil;
import org.emitdo.oal.security.DOFPermission;
import org.emitdo.oal.security.DOFPermission.Binding;
import org.emitdo.research.app.dbAdmin.model.ResDeviceTableModel.DeviceRow;
import org.emitdo.research.app.dbAdmin.model.ResManagerTableModel.ManagerRow;
import org.emitdo.research.app.dbAdmin.model.ResVersionTableModel.VersionRow;

public class OracleCannedResInitializer extends OraDatastoreBase
{
    public static final String ManagerBase = "[3:manager_";
    public static final String VersionManagerBase = "[3:vmanager_";
    public static final String VersionBase = "[3:version_";
    public static final String HomeDomainBase = "[6:home_";
    public static final String HomeDomainTail = ".pewla.com";
    
    public static final byte[] managersKey = DOFUtil.hexStringToBytes(       "0011000000000000000000000000000000000000000000000000000000000000");
    public static final byte[] devicesKey = DOFUtil.hexStringToBytes(        "0000110000000000000000000000000000000000000000000000000000000000");
    public static final byte[] gatewaysKey = DOFUtil.hexStringToBytes(       "0000001100000000000000000000000000000000000000000000000000000000");
    public static final byte[] factoryDevicesKey = DOFUtil.hexStringToBytes( "0000000011000000000000000000000000000000000000000000000000000000");
    public static final byte[] factoryGatewaysKey = DOFUtil.hexStringToBytes("0000000000110000000000000000000000000000000000000000000000000000");
    public static final byte[] gwDevicesKey = DOFUtil.hexStringToBytes(      "0000000000001100000000000000000000000000000000000000000000000000");
    public static final int managersKeyIndex = 2;
    public static final int devicesKeyIndex = 3;
    public static final int gatewaysKeyIndex = 4;
    public static final int factoryDevicesKeyIndex = 5;
    public static final int factoryGatewaysKeyIndex = 6;
    public static final int gwDevicesKeyIndex = 7;
    
    public static final String InsertManagerTableSql =
        "insert into " + OraManagerJdbc.TableName + " (" + OraManagerJdbc.ColumnOidName + ") values (?)";
    
    public static final String InsertVersionTableSql =
        "insert into " + OraVersionJdbc.TableName + " (" + OraVersionJdbc.ColumnOidName + ", " + OraVersionJdbc.ColumnOidExtName+") values (?, ?)";
    
    public static final String InsertManagerVersionTableSql =
        "insert into " + OraManagerVersionJdbc.TableName + " (" + OraManagerVersionJdbc.ColumnManagerName + ", " + OraManagerVersionJdbc.ColumnVersionName + ") values (?, ?)";
    
    public static final String InsertDeviceTableSql =
        "insert into " + OraDeviceJdbc.TableName + " (" + 
        OraDeviceJdbc.ColumnOidName + ", " + 
        OraDeviceJdbc.ColumnOidExtName + ", " + 
        OraDeviceJdbc.ColumnFactoryOidName + ", " + 
        OraDeviceJdbc.ColumnDomainOidName + ", " + 
        OraDeviceJdbc.ColumnProxyName + ", " + 
        OraDeviceJdbc.ColumnVersionName + ", " +
        OraDeviceJdbc.ColumnStatusTimeName +  
        ") values (?,?,?,?,?,?,?)";

    public static final String InsertManagerDeviceTableSql =
        "insert into " + OraManagerDeviceJdbc.TableName + " (" + OraManagerDeviceJdbc.ColumnManagerName + ", " + OraManagerDeviceJdbc.ColumnDeviceName + ") values (?, ?)";

    public static final String InsertInterfaceTableSql =
        "insert into " + OraInterfaceJdbc.TableName + " (" + OraInterfaceJdbc.ColumnIidName + ", " + OraInterfaceJdbc.ColumnManagerOnlyName + ") values (?, ?)";

    private PreparedStatement insertManagerTableStmt;
    private PreparedStatement insertVersionTableStmt;
    private PreparedStatement insertManagerVersionTableStmt;
    private PreparedStatement insertDeviceTableStmt;
    private PreparedStatement insertManagerDeviceTableStmt;
    private PreparedStatement insertInterfaceTableStmt;
    private CannedResData data;
    private Domain platformDomain;
    private Domain factoryDomain;
    private AtomicInteger nextDeviceScope;
    private Connection conn; 
    private final Hashtable<DOFObjectID, List<Authentication>> deviceManagersOnVersionMap;
    
    public OracleCannedResInitializer()
    {
        deviceManagersOnVersionMap = new Hashtable<DOFObjectID, List<Authentication>>();
    }
    
    public void setCannedData(CannedResData data) throws Exception
    {
        this.data = data;
        platformDomain = data.platformData.getDomainOid();
        factoryDomain = data.factoryData.getDomainOid();
        nextDeviceScope = new AtomicInteger(data.deviceScopeStartIndex - 1);
    }
    
    public void run() throws Exception
    {
        OraResSqlGenerator resGenerator = new OraResSqlGenerator("res");
        resGenerator.setRes(true, true);
        resGenerator.setUseSameConnection(true, true);
        resGenerator.setUser(data.connData.getUser(), true);
        resGenerator.setPassword(data.connData.getPassword(), true);
        resGenerator.setUrl(data.connData.getUrl(), true);
        OracleConnectionPoolCommands ocpc = new OracleConnectionPoolCommands();
        ocpc.setUrl(resGenerator.getUrl());
        resGenerator.setConnectionPoolCommands(data.connData.getUser(), ocpc);
        OraBaseGenerator oraclebaseGenerator = new OraBaseGenerator(new String[]{}, new Commands[]{resGenerator});
        oraclebaseGenerator.setDelete(true, true);
        oraclebaseGenerator.setCreate(true, true);
        oraclebaseGenerator.setAdminSchema(false, true);
        oraclebaseGenerator.setUrl(data.connData.getUrl(), true);
        oraclebaseGenerator.setMasters(false, false);
        OraSqlUtil util = new OraSqlUtil(oraclebaseGenerator);
        util.run();
        util.close();
        
        conn = connectionPool.getConnection();
//        conn.setAutoCommit(false);
        insertManagerTableStmt = conn.prepareStatement(InsertManagerTableSql);
        insertVersionTableStmt = conn.prepareStatement(InsertVersionTableSql);
        insertManagerVersionTableStmt = conn.prepareStatement(InsertManagerVersionTableSql);
        insertDeviceTableStmt = conn.prepareStatement(InsertDeviceTableSql);
        insertManagerDeviceTableStmt = conn.prepareStatement(InsertManagerDeviceTableSql);
        insertInterfaceTableStmt = conn.prepareStatement(InsertInterfaceTableSql);
        
        try
        {
            setupTables();
            conn.commit();
        }catch(Exception e)
        {
            conn.rollback();
            throw e;
        }finally
        {
            conn.close();
        }
    }
    
    public void setupTables() throws Exception
    {
        int deviceVersion = 0;
        int gatewayVersion = 1;
        int gatewayDeviceVersion = 2;
        int deviceManagerIndex = 1;
        int gatewayManagerIndex = deviceManagerIndex + data.getManagersPerDevice() - 1;
        int gatewayDeviceManagerIndex = gatewayManagerIndex + data.getManagersPerGateway() - 1;
        if(data.getDeviceCount() > 1)
        {
            gatewayVersion++;
            gatewayDeviceVersion++;
        }
        if(data.getGatewayCount() > 1)
        {
            gatewayDeviceVersion++;
        }

        for(int i=0; i < data.totalManagers; i++)
        {
            ManagerRow row = new ManagerRow();
            row.manager = OracleCannedResInitializer.getManagerForDevice(i, platformDomain);
            row.password = OracleCannedResInitializer.getPassword(row.manager);
            row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getManagerKeyForIndex(i));
            addManagerTableRow(row);
        }
        
        int size = data.getManagersPerVersion();
        for(int i=0; i < size; i++)
        {
            ManagerRow row = new ManagerRow();
            row.manager = OracleCannedResInitializer.getManagerForVersion(i, platformDomain);
            row.password = OracleCannedResInitializer.getPassword(row.manager);
            row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getManagerKeyForIndex(i));
            addManagerTableRow(row);
        }
        
        for(int i=0; i < data.totalVersions; i++)
        {
            VersionRow row = new VersionRow();
            row.versionId = OracleCannedResInitializer.getVersionForIndex(i, platformDomain).oid;
            Authentication[] managers = OracleCannedResInitializer.getManagersForVersionIndex(deviceManagerIndex, data.getManagersPerVersion(), platformDomain);
            row.manager = managers[0];
            addRowToVersionTable(row);
            conn.commit();
            associateManagerVersionTable(row);
            for(int j=1; j < data.getManagersPerVersion(); j++)
            {
                VersionRow mrow = new VersionRow();
                mrow.versionId = row.versionId;
                mrow.manager = managers[j];
                associateManagerVersionTable(mrow);
            }
        }
        
        for(int i=0; i < data.getDeviceCount(); i++)
        {
            DeviceRow row = new DeviceRow();
            row.device = OracleCannedResInitializer.getDeviceForIndex(i, platformDomain).oid;
            row.factory = OracleCannedResInitializer.getDeviceForIndex(i, factoryDomain).getAuthentication();
            int version = (i % 2) == 0 ? deviceVersion : deviceVersion + 1;
            row.versionId = OracleCannedResInitializer.getVersionForIndex(version, platformDomain).oid;
            Authentication[] managers = OracleCannedResInitializer.getManagersForDeviceIndex(deviceManagerIndex, data.getManagersPerDevice(), platformDomain);
            row.manager = managers[0];
            row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getDeviceKeyForIndex(i));
            addDeviceTableRow(row, true, nextDeviceScope.incrementAndGet());
            conn.commit();
            addManagerToVersionMap(row.versionId, managers[0]);                
            for(int j=1; j < data.getManagersPerDevice(); j++)
            {
                DeviceRow mrow = new DeviceRow();
                mrow.device = row.device;
                mrow.manager = managers[j];
                associateManagerToDevice(mrow);
                addManagerToVersionMap(row.versionId, managers[j]);                
            }
        }
        int nextGatewayDeviceIndex = 0;
        for(int i=0; i < data.getGatewayCount(); i++)
        {
            DOFObjectID home = null;
            DeviceRow row = new DeviceRow();
            row.device = OracleCannedResInitializer.getGatewayForIndex(i, platformDomain).oid;
            row.factory = OracleCannedResInitializer.getGatewayForIndex(i, factoryDomain).getAuthentication();
            row.domain = OracleCannedResInitializer.getHomeDomainForIndex(i);
            home = row.device;
            int version = (i % 2) == 0 ? gatewayVersion : gatewayVersion + 1;
            row.versionId = OracleCannedResInitializer.getVersionForIndex(version, platformDomain).oid;
            Authentication[] managers = OracleCannedResInitializer.getManagersForDeviceIndex(gatewayManagerIndex, data.getManagersPerGateway(), platformDomain);
            row.manager = managers[0];
            row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getGatewayKeyForIndex(i));
            addDeviceTableRow(row, true, nextDeviceScope.incrementAndGet());
            conn.commit();
            addManagerToVersionMap(row.versionId, managers[0]);                
            for(int j=1; j < data.getManagersPerGateway(); j++)
            {
                DeviceRow mrow = new DeviceRow();
                mrow.manager = managers[j];
                mrow.device = row.device;
                associateManagerToDevice(mrow);
                addManagerToVersionMap(row.versionId, managers[j]);                
            }
            for(int j=0; j < data.getGatewayDeviceCount(); j++)
            {
                row = new DeviceRow();
                row.device = OracleCannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex, platformDomain).oid;
                row.factory = OracleCannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex++, factoryDomain).getAuthentication();
                row.proxyId = home;
                version = gatewayDeviceVersion + j;
                row.versionId = OracleCannedResInitializer.getVersionForIndex(version, platformDomain).oid;
                managers = OracleCannedResInitializer.getManagersForDeviceIndex(gatewayDeviceManagerIndex, data.getManagersPerGatewayDevice(), platformDomain);
                row.manager = managers[0];
                row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getGatewayDeviceKeyForIndex(j));
                addDeviceTableRow(row, false, 0);
                addFactoryAuthNode(row.factory, row.key, data.factoryScope);
                conn.commit();
                addManagerToVersionMap(row.versionId, managers[0]);                
                for(int k=1; k < data.getManagersPerGatewayDevice(); k++)
                {
                    DeviceRow mrow = new DeviceRow();
                    mrow.manager = managers[k];
                    mrow.device = row.device;
                    associateManagerToDevice(mrow);
                    addManagerToVersionMap(row.versionId, managers[k]);                
                }
            }
        }
        for(Entry<DOFObjectID, List<Authentication>> entry : deviceManagersOnVersionMap.entrySet())
        {
            List<Authentication> managers = entry.getValue();
            int mcount = managers.size();
            for(int i=0; i < mcount; i++)
            {
                VersionRow row = new VersionRow();
                row.versionId = entry.getKey();
                row.manager = managers.get(i);
                associateManagerVersionTable(row);
            }
        }
        setupInterfaceTable();
    }
    
    private void addManagerToVersionMap(DOFObjectID version, Authentication manager)
    {
        List<Authentication> managers = deviceManagersOnVersionMap.get(version);
        if(managers == null)
        {
            managers = new ArrayList<Authentication>();
            deviceManagersOnVersionMap.put(version, managers);
        }
        if(managers.contains(manager))
            return;
        managers.add(manager);
    }
    
    private void addManagerTableRow(ManagerRow row) throws Exception
    {
        addPlatformAuthNode(row.manager, row.key, data.managersScope);
        addRowToManagerTable(row);
    }

    private void addFactoryAuthNode(Authentication device, String key, int scope) throws Exception
    {
        if(data.factoryDa.exists(device))
        {
            if(data.failOnAsExists)
                throw new Exception(device.toString() + " already exists in " + factoryDomain.toString());
            return;
        }
        data.factoryDa.addAuthenticationNode(device);
        data.factoryDa.setCredentials(device, DOFCredentials.Password.create(device, getPassword(device)));
        data.factoryDa.setCredentials(device, DOFCredentials.Key.create(device, DOFUtil.hexStringToBytes(key)));
        
        DOFPermission.Binding.Builder builder = new DOFPermission.Binding.Builder(Binding.ACTION_SESSION | Binding.ACTION_PROVIDE | Binding.ACTION_READ |
                                                                                  Binding.ACTION_WRITE | Binding.ACTION_EXECUTE);
        builder.setAllAttributesAllowed(true);
        Binding bindingPermission = builder.build();
        data.factoryDa.grantPermission(device, bindingPermission, data.factoryScope);
        String hub = data.factoryData.getHubOid();
        if(hub != null && hub.length() > 0)
            data.factoryDa.addToSecureGroup(device, Authentication.create(hub));
    }
    
    private void addPlatformAuthNode(DOFObjectID device, String key, int scope) throws Exception
    {
        Authentication oid = null;
        if(device instanceof Authentication)
            oid = (Authentication)device;
        else
            oid = Authentication.create(device);
        if(data.platformDa.exists(oid))
        {
            if(data.failOnAsExists)
                throw new Exception(device.toString() + " already exists in " + platformDomain.toString());
            return;
        }
        data.platformDa.addAuthenticationNode(oid);
        data.platformDa.setCredentials(oid, DOFCredentials.Password.create(oid, getPassword(oid)));
        data.platformDa.setCredentials(oid, DOFCredentials.Key.create(oid, DOFUtil.hexStringToBytes(key)));
        // note: all managers and gateways are given the ResidentialPlatform All permission, but in their own scope
        data.platformDa.grantPermission(oid, new All(), scope);
        String hub = data.platformData.getHubOid();
        if(hub != null && hub.length() > 0)
            data.platformDa.addToSecureGroup(oid, Authentication.create(hub));
    }
    
    private void addRemoteDomain(Authentication domain) throws Exception
    {
        if(domain == null)
            return;
        if(data.clusterDa.exists(domain))
        {
            if(data.failOnAsExists)
                throw new Exception(domain.toString() + " already exists in " + data.clusterData.getDomainOid().toString());
            return;
        }
        data.clusterDa.addRemoteDomainNode(domain);
    }
    
    private void addDeviceTableRow(DeviceRow row, boolean addAuth, int scope) throws Exception
    {
        if(addAuth)
        {
            addFactoryAuthNode(row.factory, row.key, data.factoryScope);
            addPlatformAuthNode(row.device, row.key, scope);
        }
        addRemoteDomain(row.domain);
        addRowToDeviceTable(row);
        conn.commit();
        associateManagerToDevice(row);
    }
    
    private void associateManagerToDevice(DeviceRow row) throws SQLException
    {
        long devicePid = OraDeviceJdbc.getPid(conn, row.device);
        long managerPid = OraManagerJdbc.getPid(conn, row.manager);
        insertManagerDeviceTableStmt.setLong(1, managerPid);
        insertManagerDeviceTableStmt.setLong(2, devicePid);
        insertManagerDeviceTableStmt.executeUpdate();
    }
    
    public static Authentication getManagerForDevice(int index, Domain platformDomain) throws Exception
    {
        return DOFObjectID.Authentication.create(ManagerBase + index + "@" + platformDomain.getDataString() + "]");
    }
    
    public static Authentication getManagerForVersion(int index, Domain platformDomain) throws Exception
    {
        return DOFObjectID.Authentication.create(VersionManagerBase + index + "@" + platformDomain.getDataString()+"]");
    }
    
    public static OraJdbcOid getVersionForIndex(int index, Domain platformDomain) throws Exception
    {
        return OraJdbcOid.create(VersionBase + index + "@" + platformDomain.getDataString()+"]");
    }
    
    public static OraJdbcOid getDeviceForIndex(int index, Domain domainIn) throws Exception
    {
        String domain = domainIn.getDataString();
        return OraJdbcOid.create(AsDomainInitializer.DeviceBase + index + "@" + domain + "]");
    }
    
    public static OraJdbcOid getGatewayForIndex(int index, Domain domainIn) throws Exception
    {
        String domain = domainIn.getDataString();
        return OraJdbcOid.create(AsDomainInitializer.GatewayBase + index + "@" + domain + "]");
    }
    
    public static OraJdbcOid getGatewayDeviceForIndex(int index, Domain domainIn) throws Exception
    {
        String domain = domainIn.getDataString();
        return OraJdbcOid.create(AsDomainInitializer.SubGatewayDeviceBase + index + "@" + domain + "]");
    }

    public static byte[] getDeviceKeyForIndex(int index) throws Exception
    {
        if(index < 0 || index > 255)
            throw new Exception("value must be between 0 and 255");
        devicesKey[devicesKeyIndex] = (byte)index;
        return devicesKey;
    }
    
    public static byte[] getGatewayKeyForIndex(int index) throws Exception
    {
        if(index < 0 || index > 255)
            throw new Exception("value must be between 0 and 255");
        gatewaysKey[gatewaysKeyIndex] = (byte)index;
        return gatewaysKey;
    }
    
    public static byte[] getGatewayDeviceKeyForIndex(int index) throws Exception
    {
        if(index < 0 || index > 255)
            throw new Exception("value must be between 0 and 255");
        gwDevicesKey[gwDevicesKeyIndex] = (byte)index;
        return gwDevicesKey;
    }

    public static Authentication getHomeDomainForIndex(int index) throws Exception
    {
        return Authentication.create(HomeDomainBase + index + HomeDomainTail + "]");
    }
    
    public static String getPassword(Authentication oid) throws Exception
    {
        String name = oid.getDataString();
        int index = name.indexOf('@');
        name = name.substring(0, index);
        return name;
    }
    
    public static byte[] getManagerKeyForIndex(int index) throws Exception
    {
        if(index < 0 || index > 255)
            throw new Exception("value must be between 0 and 255");
        managersKey[managersKeyIndex] = (byte)index;
        return managersKey;
    }
    
    public static Authentication[] getManagersForDeviceIndex(int index, int count, Domain domain) throws Exception
    {
        Authentication[] managers = new Authentication[count];
        managers[0] = getManagerForDevice(0, domain);
        --count;
        for(int i=1; i < managers.length; i++)
            managers[i] = getManagerForDevice(index + i-1, domain);
        return managers;
    }
    
    public static Authentication[] getManagersForVersionIndex(int index, int count, Domain domain) throws Exception
    {
        Authentication[] managers = new Authentication[count];
        managers[0] = getManagerForVersion(0, domain);
        --count;
        for(int i=1; i < managers.length; i++)
            managers[i] = getManagerForVersion(index + i-1, domain);
        return managers;
    }
    
    private void addRowToManagerTable(ManagerRow row) throws Exception
    {
        OraResJdbc.setManagerInStatement(insertManagerTableStmt, row.manager, 1);
        insertManagerTableStmt.executeUpdate();
    }
    
    private void addRowToVersionTable(VersionRow row) throws Exception
    {
        OraJdbcOid joid = new OraJdbcOid(row.versionId);
        joid.setInStatement(insertVersionTableStmt, 1);
        insertVersionTableStmt.executeUpdate();
    }
    
    private void associateManagerVersionTable(VersionRow row) throws Exception
    {
        long vpid = OraVersionJdbc.getPid(conn, row.versionId);
        long mpid = OraManagerJdbc.getPid(conn, row.manager);
        insertManagerVersionTableStmt.setLong(1, mpid);
        insertManagerVersionTableStmt.setLong(2, vpid);
        insertManagerVersionTableStmt.executeUpdate();
    }
    
    private void addRowToDeviceTable(DeviceRow row) throws Exception
    {
        Long versionId = null;
        if(row.versionId != null)
            versionId = OraVersionJdbc.getPid(conn, row.versionId);
        Long proxyId = null;
        if(row.proxyId != null)
        {
            conn.commit();
            proxyId = OraDeviceJdbc.getPid(conn, row.proxyId);
        }

        OraJdbcOid deviceOid = new OraJdbcOid(row.device);
        OraJdbcOid factoryOid = new OraJdbcOid(row.factory);
        OraJdbcOid domainOid = new OraJdbcOid(row.domain);
        deviceOid.setInStatement(insertDeviceTableStmt, 1);
        factoryOid.setInStatement(insertDeviceTableStmt, null, 3);
        domainOid.setInStatement(insertDeviceTableStmt, null, 4);
        if(proxyId == null)
            insertDeviceTableStmt.setNull(5, OraDeviceJdbc.ColumnProxySqlType);
        else
            insertDeviceTableStmt.setLong(5, proxyId);
        if(versionId == null)
            insertDeviceTableStmt.setNull(6, OraDeviceJdbc.ColumnVersionSqlType);
        else
            insertDeviceTableStmt.setLong(6, versionId);
        OraCommon.setTimeStampInStatement(System.currentTimeMillis(), insertDeviceTableStmt, 7);
        insertDeviceTableStmt.executeUpdate();
    }
    
    private void setupInterfaceTable() throws Exception
    {
        DOFInterfaceID[] iids = new DOFInterfaceID[]
        {
            DeviceRestoreBaseInterface.IID,
            DeviceBackupBaseInterface.IID,
            LoggingInterface.ASCII_IID,
            LoggingInterface.UTF_8_IID,
            LoggingInterface.SJIS_IID,
            NamingInterface.InterfaceIdRead,
            NamingInterface.InterfaceIdWrite,
            NamingInterface.InterfaceIdActivate,
            NamingInterface.InterfaceIdReadSjis,
            NamingInterface.InterfaceIdWriteSjis,
            NamingInterface.InterfaceIdActivateSjis,
            DeviceActivateInterface.IID,
            DeviceManageInterface.IID,
            UpdateManageInterface.IID,
            DeviceSecurityConfigInterface.IID_SERVICE,
            GatewaySecurityConfigInterface.IID_SERVICE,
        };

        boolean[] managerOnly = new boolean[]
        {
            false,      //DeviceRestoreBaseInterface
            false,      //DeviceBackupBaseInterface
            false,      //LoggingInterface
            false,
            false,
            false,      //NamingInterface
            false,
            false,
            false,
            false,
            false,
            false,      //DeviceActivateInterface
            true,       //DeviceManageInterface
            true,       //UpdateManageInterface
            false,      //DeviceSecurityConfigInterface
            false,      //GatewaySecurityConfigInterface
        };
        for(int i=0; i < iids.length; i++)
        {
            insertInterfaceTableStmt.setBytes(1, iids[i].getBytes());
            insertInterfaceTableStmt.setString(2, OraCommon.booleanToChar(managerOnly[i]));
            insertInterfaceTableStmt.executeUpdate();
        }
    }
}
    

