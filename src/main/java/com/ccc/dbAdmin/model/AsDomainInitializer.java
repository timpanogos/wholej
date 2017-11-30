/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import org.emitdo.app.as.ASPermission;
import org.emitdo.app.as.common.DomainStorageManagement;
import org.emitdo.oal.DOFCredentials;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.oal.DOFSecurityScope;
import org.emitdo.oal.DOFUtil;
import org.emitdo.oal.security.DOFPermission;
import org.emitdo.oal.security.DOFPermission.Binding;

public class AsDomainInitializer
{
    public static final String DeviceBase = "[3:device_";
    public static final String GatewayBase = "[3:gateway_";
    public static final String SubGatewayDeviceBase = "[3:gwDevice_";

    public static int AllPermissionGroups = DOFSecurityScope.ALL_SCOPES_ID;
    
    private final DomainStorageManagement domainDa;
    private final DomainStorageManagement clusterDa;
    private final DomainData data;
    private final DomainData clusterData;
    
    public AsDomainInitializer(DomainStorageManagement domainDa, DomainStorageManagement clusterDa, DomainData data, DomainData clusterData)
    {
        this.domainDa = domainDa;
        this.clusterDa = clusterDa;
        this.data = data;
        this.clusterData = clusterData;
    }
    
    public void run() throws Exception
    {
        Authentication hubOid = getAuthOid(data.getHubOid());
        
        Authentication managerOid = getAuthOid(data.getManagerOid());
        if(managerOid == null)
            throw new Exception("managerOid must be specified");

        String managerPass = emptyToNull(data.getManagerPassword());
        byte[] managerKey = getPresharedKey(data.getManagerPreshared());
        
        Authentication routerOid = getAuthOid(data.getRouterOid());
        String routerPass = emptyToNull(data.getRouterPassword());
        byte[] routerKey = getPresharedKey(data.getRouterPreshared());
        
        Authentication ctRouterOid = getAuthOid(data.getCtRouterOid());
        String ctRouterPass = emptyToNull(data.getCtRouterPassword());
        byte[] ctRouterKey = getPresharedKey(data.getCtrouterPreshared());
        
        DOFPermission.Binding.Builder builder = new DOFPermission.Binding.Builder(Binding.ACTION_SESSION | Binding.ACTION_PROVIDE | Binding.ACTION_READ |
                                                                                  Binding.ACTION_WRITE | Binding.ACTION_EXECUTE);
        builder.setAllAttributesAllowed(true);
        Binding binding = builder.build();
        
        if(hubOid != null)
        {
            domainDa.addSecureGroupNode(hubOid);
            domainDa.setCredentials(hubOid, DOFCredentials.Key.create(hubOid, DOFUtil.getRandomBytes(32)));
            domainDa.grantPermission(hubOid, binding, AllPermissionGroups);
            domainDa.grantPermission(hubOid, new DOFPermission.ActAsAny(), AllPermissionGroups);
        }
        
        if(managerOid != null)
        {
            domainDa.addAuthenticationNode(managerOid);
            if(managerPass != null)
                domainDa.setCredentials(managerOid, DOFCredentials.Password.create(managerOid, managerPass)); 
            if(managerKey != null)
                domainDa.setCredentials(managerOid, DOFCredentials.Key.create(managerOid, managerKey));
            domainDa.grantPermission(managerOid, binding, AllPermissionGroups);
            domainDa.grantPermission(managerOid, new DOFPermission.ActAsAny(), AllPermissionGroups);
            if(hubOid != null)
                domainDa.addToSecureGroup(managerOid, hubOid);
        }
        if(routerOid != null && clusterDa == null)
        {
            domainDa.addAuthenticationNode(routerOid);
            if(routerPass != null)
                domainDa.setCredentials(routerOid, DOFCredentials.Password.create(routerOid, routerPass)); 
            if(routerKey != null)
                domainDa.setCredentials(routerOid, DOFCredentials.Key.create(routerOid, routerKey));
//            domainDa.grantPermission(routerOid, binding, AllPermissionGroups);
            domainDa.grantPermission(routerOid, new ASPermission.Router(), AllPermissionGroups);
            if(hubOid != null)
                domainDa.addToSecureGroup(routerOid, hubOid);
        }
        if(ctRouterOid != null && clusterDa == null)
        {
            domainDa.addAuthenticationNode(ctRouterOid);
            if(ctRouterPass != null)
                domainDa.setCredentials(ctRouterOid, DOFCredentials.Password.create(ctRouterOid, ctRouterPass)); 
            if(ctRouterKey != null)
                domainDa.setCredentials(ctRouterOid, DOFCredentials.Key.create(ctRouterOid, ctRouterKey));
  //          domainDa.grantPermission(ctRouterOid, binding, AllPermissionGroups);
            domainDa.grantPermission(ctRouterOid, new ASPermission.RouterNoTunnel(), AllPermissionGroups);
            if(hubOid != null)
                domainDa.addToSecureGroup(ctRouterOid, hubOid);
        }
        
        if(clusterDa != null)
        {
            Authentication myDomain = getAuthOid(data.getDomain());
            Authentication clusterDomain = getAuthOid(clusterData.getDomain());
            Authentication clusterManager = getAuthOid(clusterData.getManagerOid());
            if(myDomain == null)
                throw new Exception("Local domain not given");
            if(clusterDomain == null)
                throw new Exception("Cluster domain not given");
            if(clusterManager == null)
                throw new Exception("Cluster manager not given");
            byte[] key = DOFUtil.getRandomBytes(32);
            
            domainDa.addRemoteDomainNode(clusterDomain);
            domainDa.setCredentials(clusterDomain, DOFCredentials.Key.create(clusterDomain, key));
            
            clusterDa.addRemoteDomainNode(myDomain);
            clusterDa.setCredentials(myDomain, DOFCredentials.Key.create(myDomain, key));
            
            domainDa.setRemoteDomainLocalNode(clusterDomain, null, managerOid);
        }else
            System.err.println("warning clusterDa was not set");
    }

    private String emptyToNull(String value)
    {
        if(value != null && value.length() > 0)
            return value;
        return null;
    }
    
    private Authentication getAuthOid(String param)
    {
        param = emptyToNull(param);
        if(param == null)
            return null;
        return Authentication.create(param);
    }
    
    private byte[] getPresharedKey(String key) throws Exception
    {
        key = emptyToNull(key);
        if(key == null)
            return null;
        byte[] secret = null;
        secret = DOFUtil.hexStringToBytes(key);
        if(secret.length != 32)
            throw new Exception("preshared key is not 32 bytes in length");
        return secret;
    }
}
