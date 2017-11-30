/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */

package org.emitdo.research.app.dbAdmin.model;

import java.sql.Connection;

import org.emitdo.app.util.Commands;
import org.emitdo.db.common.oracle.OraDatastoreBase;
import org.emitdo.db.common.oracle.OracleConnectionPoolCommands;
import org.emitdo.internal.app.service.backup.db.oracle.OraBackupSqlGenerator;
import org.emitdo.internal.db.common.oracle.OraBaseGenerator;
import org.emitdo.internal.db.common.oracle.OraSqlUtil;

public class OraBackupInitializer extends OraDatastoreBase
{
    private CannedResData data;
    private ConnectionData connectionData;
    private Connection conn; 
    
    public OraBackupInitializer()
    {
    }
    
    public void setCannedData(CannedResData data) throws Exception
    {
        this.data = data;
    }
    
    public void setConnectionData(ConnectionData connectionData) throws Exception
    {
        this.connectionData = connectionData;
    }
    
    
    public void run() throws Exception
    {
        OraBackupSqlGenerator backupGenerator = new OraBackupSqlGenerator("backup");
        backupGenerator.setBackup(true, true);
        backupGenerator.setUseSameConnection(true, true);
        backupGenerator.setUser(data.connData.getUser(), true);
        backupGenerator.setPassword(data.connData.getPassword(), true);
        backupGenerator.setUrl(data.connData.getUrl(), true);
        OracleConnectionPoolCommands ocpc = new OracleConnectionPoolCommands();
        ocpc.setUrl(backupGenerator.getUrl());
        backupGenerator.setConnectionPoolCommands(data.connData.getUser(), ocpc);
        OraBaseGenerator oraclebaseGenerator = new OraBaseGenerator(new String[]{}, new Commands[]{backupGenerator});
        oraclebaseGenerator.setDelete(true, true);
        oraclebaseGenerator.setCreate(true, true);
        oraclebaseGenerator.setAdminSchema(false, true);
        oraclebaseGenerator.setUrl(connectionData.getUrl(), true);
        oraclebaseGenerator.setMasters(false, false);
        OraSqlUtil util = new OraSqlUtil(oraclebaseGenerator);
        util.run();
        util.close();
        
        conn = connectionPool.getConnection();
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
//        Domain platform = data.platformData.getDomainOid();
//        DOFObjectID oid = null;
//        for(int i=0; i < data.getDeviceCount(); i++)
//        {
//            oid = OracleCannedResInitializer.getDeviceForIndex(i, platform).oid;
//            OracleBackupJdbc.createBackupDevice(conn, oid);
//        }
//        
//        int nextGatewayDeviceIndex = 0;
//        for(int i=0; i < data.getGatewayCount(); i++)
//        {
//            oid = OracleCannedResInitializer.getGatewayForIndex(i, platform).oid;
//            OracleBackupJdbc.createBackupDevice(conn, oid);
//            for(int j=0; j < data.getGatewayDeviceCount(); j++)
//            {
//                oid = OracleCannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex++, platform).oid;
//                OracleBackupJdbc.createBackupDevice(conn, oid);
//            }
//        }
    }
}
    

