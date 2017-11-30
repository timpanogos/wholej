/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import org.emitdo.research.app.dbAdmin.ConnectionControl;
import org.emitdo.research.app.dbAdmin.DbAdmin.ConnType;
import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.DbAdmin.VendorType;

public class ConnectionsData
{
    public final ConnectionData jdbJdbc;
    public final ConnectionData jdbWeb;
    public final ConnectionData oracleJdbc;
    public final ConnectionData oracleWeb;
    public final ConnectionData pgJdbc;
    public final ConnectionData pgWeb;
    public ConnectionData currentlySelected;
    
    public ConnectionsData(
            ConnectionData jdbJdbc, ConnectionData oracleJdbc, ConnectionData pgJdbc, 
            ConnectionData jdbWeb, ConnectionData oracleWeb, ConnectionData pgWeb)
    {
        this.jdbJdbc = jdbJdbc;
        this.oracleJdbc = oracleJdbc;
        this.pgJdbc = pgJdbc;
        this.jdbWeb = jdbWeb;
        this.oracleWeb = oracleWeb;
        this.pgWeb = pgWeb;
    }
    
    public static ConnectionsData getAvailableConnections(ConnectionControl control, StorageType stype, DomainType dtype)
    {
        return new ConnectionsData(
                control.getConnection(stype, VendorType.JavaDb, dtype, ConnType.Jdbc),
                control.getConnection(stype, VendorType.Oracle, dtype, ConnType.Jdbc),
                control.getConnection(stype, VendorType.Postgres, dtype, ConnType.Jdbc),
                control.getConnection(stype, VendorType.JavaDb, dtype, ConnType.Web),
                control.getConnection(stype, VendorType.Oracle, dtype, ConnType.Web),
                control.getConnection(stype, VendorType.Postgres, dtype, ConnType.Web));
    }
}
