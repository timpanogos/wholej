/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */

package org.emitdo.research.app.dbAdmin.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.emitdo.db.common.oracle.OraCommon.OraJdbcOid;
import org.emitdo.db.common.oracle.OraDatastoreBase;
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

public class CannedResInitializer extends OraDatastoreBase
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
    
//
//    public static final String InsertManagerDeviceTableSql =
//        "insert into " + OracleManagerDeviceJdbc.TableName + " (" + OracleManagerDeviceJdbc.ColumnManagerName + ", " + OracleManagerDeviceJdbc.ColumnDeviceName + ") values (?, ?)";
//
//    public static final String InsertInterfaceTableSql =
//        "insert into " + OracleInterfaceJdbc.TableName + " (" + OracleInterfaceJdbc.ColumnIidName + ", " + OracleInterfaceJdbc.ColumnManagerOnlyName + ") values (?, ?)";
//
//    private PreparedStatement insertManagerTableStmt;
//    private PreparedStatement insertVersionTableStmt;
//    private PreparedStatement insertManagerVersionTableStmt;
//    private PreparedStatement insertDeviceTableStmt;
//    private PreparedStatement insertManagerDeviceTableStmt;
//    private PreparedStatement insertInterfaceTableStmt;
    private CannedResData data;
    private Domain platformDomain;
    private Domain factoryDomain;
    private AtomicInteger nextDeviceScope;
//    private Connection conn; 
    private final Hashtable<DOFObjectID, List<Authentication>> deviceManagersOnVersionMap;
    private final ResManager dataAccessor;
    
    public CannedResInitializer(ResManager da)
    {
        dataAccessor = da;
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
//        conn = connectionPool.getConnection();
////        conn.setAutoCommit(false);
//        insertManagerTableStmt = conn.prepareStatement(InsertManagerTableSql);
//        insertVersionTableStmt = conn.prepareStatement(InsertVersionTableSql);
//        insertManagerVersionTableStmt = conn.prepareStatement(InsertManagerVersionTableSql);
//        insertDeviceTableStmt = conn.prepareStatement(InsertDeviceTableSql);
//        insertManagerDeviceTableStmt = conn.prepareStatement(InsertManagerDeviceTableSql);
//        insertInterfaceTableStmt = conn.prepareStatement(InsertInterfaceTableSql);
        
        try
        {
            setupTables();
//            conn.commit();
        }catch(Exception e)
        {
//            conn.rollback();
            throw e;
        }finally
        {
//            conn.close();
        }
    }
    
    public void setupTables() throws Exception
    {
        dataAccessor.beginTransaction();
        data.clusterDa.beginTransaction();
        data.platformDa.beginTransaction();
        data.factoryDa.beginTransaction();

        try
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
                row.manager = CannedResInitializer.getManagerForDevice(i, platformDomain);
                row.password = CannedResInitializer.getPassword(row.manager);
                row.key = DOFUtil.bytesToHexString(CannedResInitializer.getManagerKeyForIndex(i));
                addManagerTableRow(row);
            }
            
            int size = data.getManagersPerVersion();
            for(int i=0; i < size; i++)
            {
                ManagerRow row = new ManagerRow();
                row.manager = CannedResInitializer.getManagerForVersion(i, platformDomain);
                row.password = CannedResInitializer.getPassword(row.manager);
                row.key = DOFUtil.bytesToHexString(CannedResInitializer.getManagerKeyForIndex(i));
                addManagerTableRow(row);
            }
            
            for(int i=0; i < data.totalVersions; i++)
            {
                VersionRow row = new VersionRow();
                row.versionId = CannedResInitializer.getVersionForIndex(i, platformDomain).oid;
                Authentication[] managers = CannedResInitializer.getManagersForVersionIndex(deviceManagerIndex, data.getManagersPerVersion(), platformDomain);
                row.manager = managers[0];
                dataAccessor.addVersion(row.versionId);
    //            addRowToVersionTable(row);
    //            conn.commit();
                dataAccessor.associateManagerToVersion(row.manager, row.versionId);
    //            associateManagerVersionTable(row);
                for(int j=1; j < data.getManagersPerVersion(); j++)
                {
                    VersionRow mrow = new VersionRow();
                    mrow.versionId = row.versionId;
                    mrow.manager = managers[j];
                    dataAccessor.associateManagerToVersion(mrow.manager, mrow.versionId);
    //                associateManagerVersionTable(mrow);
                }
            }
            
            for(int i=0; i < data.getDeviceCount(); i++)
            {
                DeviceRow row = new DeviceRow();
                row.device = CannedResInitializer.getDeviceForIndex(i, platformDomain).oid;
                row.factory = CannedResInitializer.getDeviceForIndex(i, factoryDomain).getAuthentication();
                int version = (i % 2) == 0 ? deviceVersion : deviceVersion + 1;
                row.versionId = CannedResInitializer.getVersionForIndex(version, platformDomain).oid;
                Authentication[] managers = CannedResInitializer.getManagersForDeviceIndex(deviceManagerIndex, data.getManagersPerDevice(), platformDomain);
                row.manager = managers[0];
//                dataAccessor.associateManagerToVersion(managers[0], row.versionId);
                row.key = DOFUtil.bytesToHexString(CannedResInitializer.getDeviceKeyForIndex(i));
                addDeviceTableRow(row, true, nextDeviceScope.incrementAndGet());
    //            conn.commit();
                addManagerToVersionMap(row.versionId, managers[0]);                
                for(int j=1; j < data.getManagersPerDevice(); j++)
                {
                    DeviceRow mrow = new DeviceRow();
                    mrow.device = row.device;
                    mrow.manager = managers[j];
                    dataAccessor.disassociateManagerFromVersion(mrow.manager, mrow.device);
//                    dataAccessor.associateManagerToVersion(managers[j], row.versionId);
                    addManagerToVersionMap(row.versionId, managers[j]);                
                }
            }
            int nextGatewayDeviceIndex = 0;
            for(int i=0; i < data.getGatewayCount(); i++)
            {
                DOFObjectID home = null;
                DeviceRow row = new DeviceRow();
                row.device = CannedResInitializer.getGatewayForIndex(i, platformDomain).oid;
                row.factory = CannedResInitializer.getGatewayForIndex(i, factoryDomain).getAuthentication();
                row.domain = CannedResInitializer.getHomeDomainForIndex(i);
                home = row.device;
                int version = (i % 2) == 0 ? gatewayVersion : gatewayVersion + 1;
                row.versionId = CannedResInitializer.getVersionForIndex(version, platformDomain).oid;
                Authentication[] managers = CannedResInitializer.getManagersForDeviceIndex(gatewayManagerIndex, data.getManagersPerGateway(), platformDomain);
                row.manager = managers[0];
                row.key = DOFUtil.bytesToHexString(CannedResInitializer.getGatewayKeyForIndex(i));
//                dataAccessor.associateManagerToVersion(managers[0], row.versionId);
                addDeviceTableRow(row, true, nextDeviceScope.incrementAndGet());
    //            conn.commit();
                addManagerToVersionMap(row.versionId, managers[0]);                
                for(int j=1; j < data.getManagersPerGateway(); j++)
                {
                    DeviceRow mrow = new DeviceRow();
                    mrow.manager = managers[j];
                    mrow.device = row.device;
                    dataAccessor.disassociateManagerFromVersion(mrow.manager, mrow.device);
//                    dataAccessor.associateManagerToVersion(managers[j], row.versionId);
                    addManagerToVersionMap(row.versionId, managers[j]);                
                }
                for(int j=0; j < data.getGatewayDeviceCount(); j++)
                {
                    row = new DeviceRow();
                    row.device = CannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex, platformDomain).oid;
                    row.factory = CannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex++, factoryDomain).getAuthentication();
                    row.proxyId = home;
                    version = gatewayDeviceVersion + j;
                    row.versionId = CannedResInitializer.getVersionForIndex(version, platformDomain).oid;
                    managers = CannedResInitializer.getManagersForDeviceIndex(gatewayDeviceManagerIndex, data.getManagersPerGatewayDevice(), platformDomain);
                    row.manager = managers[0];
                    row.key = DOFUtil.bytesToHexString(CannedResInitializer.getGatewayDeviceKeyForIndex(j));
//                    dataAccessor.associateManagerToVersion(managers[0], row.versionId);
                    addDeviceTableRow(row, false, 0);
                    addFactoryAuthNode(row.factory, row.key, data.factoryScope);
    //                conn.commit();
                    addManagerToVersionMap(row.versionId, managers[0]);                
                    for(int k=1; k < data.getManagersPerGatewayDevice(); k++)
                    {
                        DeviceRow mrow = new DeviceRow();
                        mrow.manager = managers[k];
                        mrow.device = row.device;
                        dataAccessor.disassociateManagerFromVersion(mrow.manager, mrow.device);
//                        dataAccessor.associateManagerToVersion(managers[k], row.versionId);
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
                    dataAccessor.associateManagerToVersion(row.manager, row.versionId);
    //                associateManagerVersionTable(row);
                }
            }
            setupInterfaceTable();
            dataAccessor.commit();
            data.clusterDa.commit();
            data.platformDa.commit();
            data.factoryDa.commit();
        }catch(Exception e)
        {
            dataAccessor.rollback();
            data.clusterDa.rollback();
            data.platformDa.rollback();
            data.factoryDa.rollback();
            throw e;
        }
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
        dataAccessor.addManager(row.manager);
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
//        conn.commit();
        dataAccessor.disassociateManagerFromVersion(row.manager, row.device);
    }
    
//    private void associateManagerToDevice(DeviceRow row) throws Exception
//    {
//        long devicePid = OracleDeviceJdbc.getPid(conn, row.device);
//        long managerPid = OracleManagerJdbc.getPid(conn, row.manager);
//        insertManagerDeviceTableStmt.setLong(1, managerPid);
//        insertManagerDeviceTableStmt.setLong(2, devicePid);
//        insertManagerDeviceTableStmt.executeUpdate();
//    }
    
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
    
//    private void addRowToManagerTable(ManagerRow row) throws Exception
//    {
//        OracleResJdbc.setManagerInStatement(insertManagerTableStmt, row.manager, 1);
//        insertManagerTableStmt.executeUpdate();
//    }
    
//    private void addRowToVersionTable(VersionRow row) throws Exception
//    {
//        OracleJdbcOid joid = new OracleJdbcOid(row.versionId);
//        joid.setInStatement(insertVersionTableStmt, 1);
//        insertVersionTableStmt.executeUpdate();
//    }
    
//    private void associateManagerVersionTable(VersionRow row) throws Exception
//    {
//        long vpid = OracleVersionJdbc.getPid(conn, row.versionId);
//        long mpid = OracleManagerJdbc.getPid(conn, row.manager);
//        insertManagerVersionTableStmt.setLong(1, mpid);
//        insertManagerVersionTableStmt.setLong(2, vpid);
//        insertManagerVersionTableStmt.executeUpdate();
//    }
    
    private void addRowToDeviceTable(DeviceRow row) throws Exception
    {
        Domain domain = null;
        if(row.domain != null)
            domain = Domain.create(row.domain);
        Authentication proxy = null;
        if(row.proxyId != null)
            proxy = Authentication.create(row.proxyId);
        dataAccessor.addDevice(row.device, row.factory, domain, proxy);
        dataAccessor.setVersion(row.device, row.versionId, null);
//        Long versionId = null;
//        if(row.versionId != null)
//            versionId = OracleVersionJdbc.getPid(conn, row.versionId);
//        Long proxyId = null;
//        if(row.proxyId != null)
//        {
//            conn.commit();
//            proxyId = OracleDeviceJdbc.getPid(conn, row.proxyId);
//        }
//
//        OracleJdbcOid deviceOid = new OracleJdbcOid(row.device);
//        OracleJdbcOid factoryOid = new OracleJdbcOid(row.factory);
//        OracleJdbcOid domainOid = new OracleJdbcOid(row.domain);
//        deviceOid.setInStatement(insertDeviceTableStmt, 1);
//        factoryOid.setInStatement(insertDeviceTableStmt, null, 3);
//        domainOid.setInStatement(insertDeviceTableStmt, null, 4);
//        if(proxyId == null)
//            insertDeviceTableStmt.setNull(5, OracleDeviceJdbc.ColumnProxySqlType);
//        else
//            insertDeviceTableStmt.setLong(5, proxyId);
//        if(versionId == null)
//            insertDeviceTableStmt.setNull(6, OracleDeviceJdbc.ColumnVersionSqlType);
//        else
//            insertDeviceTableStmt.setLong(6, versionId);
//        OracleCommon.setTimeStampInStatement(System.currentTimeMillis(), insertDeviceTableStmt, 7);
//        insertDeviceTableStmt.executeUpdate();
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
            dataAccessor.addInterface(iids[i], managerOnly[i]);
//            insertInterfaceTableStmt.setBytes(1, );
//            insertInterfaceTableStmt.setString(2, OracleCommon.booleanToChar());
//            insertInterfaceTableStmt.executeUpdate();
//        }
    }
}
    

