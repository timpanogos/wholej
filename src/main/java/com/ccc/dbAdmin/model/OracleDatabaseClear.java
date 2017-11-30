package org.emitdo.research.app.dbAdmin.model;

import java.util.HashMap;
import java.util.Map;

import org.emitdo.app.common.CloseableIterable;
import org.emitdo.as.oracle.OracleStorageManagement;
import org.emitdo.oal.DOFObjectID;
import org.emitdo.oal.DOFObjectID.Domain;

public class OracleDatabaseClear 
{
    public static Domain CLUSTER_DOMAIN_ID = DOFObjectID.Domain.create("[6:cluster.pewla.com]");
    public static Domain FACTORY_DOMAIN_ID = DOFObjectID.Domain.create("[6:factory.pewla.com]");
    public static Domain PLATFORM_DOMAIN_ID = DOFObjectID.Domain.create("[6:platform.pewla.com]");
    
    private final Domain domainId;
    private final String user;
    private final String pass;
    private final String url;
    
    public OracleDatabaseClear(Domain domainId, String user, String pass, String url)
    {
        this.domainId = domainId;
        this.user = user;
        this.pass = pass;
        this.url = url;
    }
    
    public void run() throws Exception
    {
        OracleStorageManagement manager = new OracleStorageManagement();
        Map<String, String> params = new HashMap<String, String>();;
        params.put(OracleStorageManagement.URL_KEY, url);
        params.put(OracleStorageManagement.USER_KEY, user);
        params.put(OracleStorageManagement.PASSWORD_KEY, pass);
        
        try
        {
            manager.init(domainId, params);
            manager.beginTransaction();
            CloseableIterable<DOFObjectID.Authentication> authNodes = manager.getAuthenticationNodeIDs();
            CloseableIterable<DOFObjectID.Authentication> groupNodes = manager.getSecureGroupNodeIDs();
            CloseableIterable<DOFObjectID.Authentication> domainNodes = manager.getRemoteDomainNodeIDs();
            
            for(DOFObjectID.Authentication nodeID: authNodes)
                manager.removeAuthenticationNode(nodeID);
            
            for(DOFObjectID.Authentication nodeID: groupNodes)
                manager.removeSecureGroupNode(nodeID);
            
            for(DOFObjectID.Authentication nodeID: domainNodes)
                manager.removeRemoteDomainNode(nodeID);
            
            authNodes.close();
            groupNodes.close();
            domainNodes.close();
            manager.commit();
            manager.destroy();
        }
        catch(Exception e)
        {
            try {if(manager != null)manager.rollback();} catch (Exception e1) { }
            throw e;
        }
    }
    
    public static void main(String[] args)
    {
        clear(CLUSTER_DOMAIN_ID, "as_cluster_admin", "As_Cluster_Admin");
        clear(FACTORY_DOMAIN_ID, "as_factory_admin", "As_Factory_Admin");
        clear(PLATFORM_DOMAIN_ID, "as_platform_admin", "As_Platform_Admin");
    }
    
    private static void clear(DOFObjectID.Domain domainID, String user, String password)
    {
        OracleStorageManagement manager = new OracleStorageManagement();
        
        Map<String, String> params = new HashMap<String, String>();;
        params.put(OracleStorageManagement.URL_KEY, "jdbc:oracle:thin:@engorcl:1521:orcl");
        params.put(OracleStorageManagement.USER_KEY, user);
        params.put(OracleStorageManagement.PASSWORD_KEY, password);
        
        try{
            manager.init(domainID, params);
            manager.beginTransaction();
            
            CloseableIterable<DOFObjectID.Authentication> authNodes = manager.getAuthenticationNodeIDs();
            CloseableIterable<DOFObjectID.Authentication> groupNodes = manager.getSecureGroupNodeIDs();
            CloseableIterable<DOFObjectID.Authentication> domainNodes = manager.getRemoteDomainNodeIDs();
            
            for(DOFObjectID.Authentication nodeID: authNodes){
                System.out.println("Removing auth node: " + nodeID);
                manager.removeAuthenticationNode(nodeID);
            }
            
            for(DOFObjectID.Authentication nodeID: groupNodes){
                System.out.println("Removing group node: " + nodeID);
                manager.removeSecureGroupNode(nodeID);
            }
            
            for(DOFObjectID.Authentication nodeID: domainNodes){
                System.out.println("Removing domain node: " + nodeID);
                manager.removeRemoteDomainNode(nodeID);
            }
            
            authNodes.close();
            groupNodes.close();
            domainNodes.close();
            
            manager.commit();
        }
        catch(Exception e){
            try {if(manager != null)manager.rollback();} catch (Exception e1) { }
            System.out.println("Failed: " + e);
            e.printStackTrace();
        }
    }
}
