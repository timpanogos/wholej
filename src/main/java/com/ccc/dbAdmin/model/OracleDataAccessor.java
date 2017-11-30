/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.lang.reflect.Method;

import org.emitdo.db.common.oracle.OraDatastoreBase;
import org.emitdo.db.common.oracle.OracleConnectionPool;
import org.emitdo.db.common.oracle.OracleConnectionPoolCommands;

public class OracleDataAccessor
{
    public OracleConnectionPool ocp;
    public OraDatastoreBase dataAccessor;
    
    public OracleDataAccessor(ConnectionData conn) throws Exception
    {
        OracleConnectionPoolCommands commands = new OracleConnectionPoolCommands();
        commands.setUser(conn.getUser());
        commands.setPassword(conn.getPassword());
        commands.setUrl(conn.getUrl());
        ocp = new OracleConnectionPool(commands);
        
        Class<?> clazz = Class.forName(conn.getDataAccessor());
        dataAccessor = (OraDatastoreBase)clazz.newInstance();
        Method method = clazz.getMethod("setConnectionPool", OracleConnectionPool.class);
        method.invoke(dataAccessor, ocp);
    }
    
    public void close()
    {
        ocp.shutdown();
    }
}
