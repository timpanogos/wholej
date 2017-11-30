/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */

package org.emitdo.research.app.dbAdmin.model;

import java.sql.Connection;

import org.emitdo.db.common.postgres.PgDatastoreBase;

public class PgBackupInitializer extends PgDatastoreBase
{
    @SuppressWarnings("unused")
    private CannedResData data;
    private Connection conn; 
    
    public PgBackupInitializer()
    {
    }
    
    public void setCannedData(CannedResData data) throws Exception
    {
        this.data = data;
    }
    
    public void run() throws Exception
    {
        conn = connectionPool.getConnection();
        conn.setAutoCommit(false);
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
//            PgBackupJdbc.createBackupDevice(conn, oid);
//        }
//        
//        int nextGatewayDeviceIndex = 0;
//        for(int i=0; i < data.getGatewayCount(); i++)
//        {
//            oid = OracleCannedResInitializer.getGatewayForIndex(i, platform).oid;
//            PgBackupJdbc.createBackupDevice(conn, oid);
//            for(int j=0; j < data.getGatewayDeviceCount(); j++)
//            {
//                oid = OracleCannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex++, platform).oid;
//                PgBackupJdbc.createBackupDevice(conn, oid);
//            }
//        }
    }
}
    

