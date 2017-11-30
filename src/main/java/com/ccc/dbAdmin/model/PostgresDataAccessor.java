/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.lang.reflect.Method;

import org.emitdo.db.common.postgres.PgConnectionPool;
import org.emitdo.db.common.postgres.PgConnectionPoolCommands;
import org.emitdo.db.common.postgres.PgDatastoreBase;

public class PostgresDataAccessor
{
    public PgConnectionPool cp;
    public PgDatastoreBase dataAccessor;
    
    public PostgresDataAccessor(ConnectionData conn) throws Exception
    {
        PgConnectionPoolCommands commands = new PgConnectionPoolCommands();
        commands.setUser(conn.getUser());
        commands.setPassword(conn.getPassword());
        String[] urlValues = conn.getUrl().split(",");
        commands.setHost(urlValues[0]);
        commands.setPort(urlValues[1]);
        commands.setDatabaseName(urlValues[2]);
        cp = new PgConnectionPool(commands);
        
        Class<?> clazz = Class.forName(conn.getDataAccessor());
        dataAccessor = (PgDatastoreBase)clazz.newInstance();
        Method method = clazz.getMethod("setConnectionPool", PgConnectionPool.class);
        method.invoke(dataAccessor, cp);
    }
    
    public void close()
    {
        cp.shutdown();
    }
}
