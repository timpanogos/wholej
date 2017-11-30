/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.sql.Connection;
import java.sql.SQLException;

import org.emitdo.app.res.db.postgres.management.PgResPlatformManagerImpl;
import org.emitdo.app.res.db.postgres.res.PgDeviceJdbc;
import org.emitdo.app.res.management.DeviceAlreadyExistsException;
import org.emitdo.app.res.management.DeviceFactoryIdAlreadyExistsException;
import org.emitdo.app.res.management.DeviceNotFoundException;
import org.emitdo.app.res.management.ManagerNotFoundException;
import org.emitdo.app.res.management.ProxyNotFoundException;
import org.emitdo.app.res.management.ResidentialDomainAlreadyExistsException;
import org.emitdo.app.res.management.VersionNotFoundException;
import org.emitdo.oal.DOFInterfaceID;
import org.emitdo.oal.DOFObjectID;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.oal.DOFObjectID.Domain;


public class PgResManager implements ResManager
{
    PgResPlatformManagerImpl da;
    
    public PgResManager(PgResPlatformManagerImpl da)
    {
        this.da = da;
    }
    
    @Override
    public void addDevice(DOFObjectID deviceOid, Authentication factoryOid,
            Domain residentialDomainOid, Authentication proxyOid)
            throws DeviceAlreadyExistsException, DeviceFactoryIdAlreadyExistsException, 
            ResidentialDomainAlreadyExistsException, ProxyNotFoundException, Exception
    {
        da.addDevice(deviceOid, factoryOid, residentialDomainOid, proxyOid);
    }

    @Override
    public void removeDevice(DOFObjectID deviceOid) throws DeviceNotFoundException, Exception
    {
        da.removeDevice(deviceOid);                
    }

    @Override
    public void setDeviceFactoryId(DOFObjectID deviceOid, Authentication factoryOid) throws DeviceNotFoundException, DeviceFactoryIdAlreadyExistsException, Exception
    {
        da.setDeviceFactoryId(deviceOid, factoryOid);
    }

    @Override
    public void setResidentialDomain(DOFObjectID deviceOid, Domain residentialDomainOid) throws DeviceNotFoundException, ResidentialDomainAlreadyExistsException, Exception
    {
        da.setResidentialDomain(deviceOid, residentialDomainOid);
    }

    @Override
    public void setProxyGateway(DOFObjectID deviceOid, Authentication proxyOid) throws DeviceNotFoundException, ProxyNotFoundException, Exception
    {
        da.setProxyGateway(deviceOid, proxyOid);
    }

    @Override
    public void addInterface(DOFInterfaceID interfaceId, boolean managerOnly) throws Exception
    {
        da.addInterface(interfaceId, managerOnly);
    }

    @Override
    public void removeInterface(DOFInterfaceID interfaceIid) throws Exception
    {
        da.removeInterface(interfaceIid);
    }

    @Override
    public void associateManagerToDevice(Authentication managerOid, DOFObjectID deviceOid) throws ManagerNotFoundException, DeviceNotFoundException, Exception
    {
        da.associateManagerToDevice(managerOid, deviceOid);
    }

    @Override
    public void disassociateManagerFromDevice(Authentication managerOid, DOFObjectID deviceOid) throws Exception
    {
        da.disassociateManagerFromDevice(managerOid, deviceOid);
    }

    @Override
    public void associateManagerToVersion(Authentication managerOid, DOFObjectID versionOid) throws ManagerNotFoundException, VersionNotFoundException, Exception
    {
        da.associateManagerToVersion(managerOid, versionOid);
    }

    @Override
    public void disassociateManagerFromVersion(Authentication managerOid, DOFObjectID versionOid) throws Exception
    {
        da.disassociateManagerFromVersion(managerOid, versionOid);
    }

    @Override
    public void addManager(Authentication managerOid) throws Exception
    {
        da.addManager(managerOid);
    }

    @Override
    public void removeManager(Authentication managerOid) throws Exception
    {
        da.removeManager(managerOid);
    }

    @Override
    public void addVersion(DOFObjectID versionOid) throws Exception
    {
        da.addVersion(versionOid);
    }

    @Override
    public void removeVersion(DOFObjectID versionOid) throws Exception
    {
        da.removeVersion(versionOid);
    }

    @Override
    public void beginTransaction() throws Exception
    {
        da.beginTransaction();
    }

    @Override
    public void commit() throws Exception
    {
        da.commit();
    }

    @Override
    public void rollback() throws Exception
    {
        da.rollback();
    }

    @Override
    public void setVersion(DOFObjectID deviceOid, DOFObjectID versionOid, Authentication managerOid) throws SQLException
    {
        Connection conn = da.getConnection();
        try
        {
            PgDeviceJdbc.setVersion(conn, deviceOid, versionOid, managerOid);
        }
        finally
        {
            da.returnConnection(conn);
        }
    }
}
