/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.util.prefs.Preferences;

import org.emitdo.oal.DOFObjectID.Domain;
import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;

public class DomainData
{
    public static final String DomainBaseName = "[6:";
    public static final String DomainNameKey = "DomainName";
    public static final String HubKey = "serverHub";
    public static final String TablespaceKey = "tablespace";
    public static final String ManagerNameKey = "ManagerName";
    public static final String ManagerPasswordKey = "ManagerPassword";
    public static final String ManagerPresharedKey = "ManagerPreshared";
    public static final String RouterNameKey = "routerName";
    public static final String RouterPasswordKey = "routerPassword";
    public static final String RouterPresharedKey = "routerPreshared";
    public static final String CtRouterNameKey = "ctrouterName";
    public static final String CtRouterPasswordKey = "ctrouterPassword";
    public static final String CtRouterPresharedKey = "ctrouterPreshared";
    public static final String AutoCreateKey = "autoCreate";
    public static final String RequireAuthKey = "requireAuth";

    public static final String DefaultAsClusterManagerKey = "1000000000000000000000000000000000000000000000000000000000000000";
    public static final String DefaultAsClusterRouterKey = "2000000000000000000000000000000000000000000000000000000000000000";
    public static final String DefaultAsClusterCtRouterKey = "3000000000000000000000000000000000000000000000000000000000000000";
    public static final String DefaultAsResManagerKey = "4000000000000000000000000000000000000000000000000000000000000000";
    public static final String DefaultAsFactoryManagerKey = "5000000000000000000000000000000000000000000000000000000000000000";
    public static final String DefaultAsBatteryManagerKey = "6000000000000000000000000000000000000000000000000000000000000000";
    public static final String DefaultAsAncillaryManagerKey = "7000000000000000000000000000000000000000000000000000000000000000";
    
    public String title;
    public String defaultDomain;
    public String prefDomain;
    public String guiDomain;
    public String defaultHubOid;
    public String prefHubOid;
    public String guiHubOid;
    public String defaultTablespace;
    public String prefTablespace;
    public String guiTablespace;
    public String defaultRouterOid;
    public String prefRouterOid;
    public String guiRouterOid;
    public String defaultRouterPassword;
    public String prefRouterPassword;
    public String guiRouterPassword;
    public String defaultRouterPreshared;
    public String prefRouterPreshared;
    public String guiRouterPreshared;
    public String defaultCtrouterOid;
    public String prefCtrouterOid;
    public String guiCtrouterOid;
    public String defaultCtrouterPassword;
    public String prefCtrouterPassword;
    public String guiCtrouterPassword;
    public String defaultCtrouterPreshared;
    public String prefCtrouterPreshared;
    public String guiCtrouterPreshared;
    public String defaultManagerOid;
    public String prefManagerOid;
    public String guiManagerOid;
    public String defaultManagerPassword;
    public String prefManagerPassword;
    public String guiManagerPassword;
    public String defaultManagerPreshared;
    public String prefManagerPreshared;
    public String guiManagerPreshared;
    
    public boolean defaultAutoCreate;
    public boolean prefAutoCreate;
    public boolean guiAutoCreate;

    public boolean defaultRequireAuth;
    public boolean prefRequireAuth;
    public boolean guiRequireAuth;
    
    public final DomainType type;
    public final Preferences pref;
    public boolean modified;
    
    public DomainData(DomainType type, Preferences pref)
    {
        this.type = type;
        this.pref = pref;
        
        defaultRouterOid = "[3:router@cluster.com]";
        defaultRouterPassword = "router";
        defaultCtrouterOid = "[3:ctrouter@cluster.com]";
        defaultCtrouterPassword = "ctrouter";
        defaultManagerPassword = "manager";
        defaultAutoCreate = true;
        prefAutoCreate = pref.getBoolean(AutoCreateKey, true);
        defaultRequireAuth = true;
        prefRequireAuth = pref.getBoolean(RequireAuthKey, true);
        
        switch(type)
        {
            case Cluster:
                defaultManagerPreshared = DefaultAsClusterManagerKey;
                defaultRouterPreshared = DefaultAsClusterRouterKey;
                defaultCtrouterPreshared = DefaultAsClusterCtRouterKey;
                defaultDomain = DomainBaseName + "cluster.com]"; 
                defaultHubOid = "[3:serverHub@cluster.pesdca.com]";
                defaultTablespace = "asdb";
                defaultManagerOid = "[3:manager@cluster.pesdca.com]";
                title = "Cluster Domain";
                prefDomain = pref.get("cluster"+DomainNameKey, null);
                prefHubOid = pref.get("cluster"+HubKey, null);
                prefTablespace = pref.get("cluster"+TablespaceKey, null);
                prefRouterOid = pref.get(RouterNameKey, null);
                prefRouterPassword = pref.get(RouterPasswordKey, null);
                prefRouterPreshared = pref.get(RouterPresharedKey, null);
                prefCtrouterOid = pref.get(CtRouterNameKey, null);
                prefCtrouterPassword = pref.get(CtRouterPasswordKey, null);
                prefCtrouterPreshared = pref.get(CtRouterPresharedKey, null);
                prefManagerOid = pref.get("cluster"+ManagerNameKey, null);
                prefManagerPassword = pref.get("cluster"+ManagerPasswordKey, null);
                prefManagerPreshared = pref.get("cluster"+ManagerPresharedKey, defaultManagerPreshared);
                if(prefDomain == null)
                    prefDomain = getDomain(true);
                if(prefHubOid == null)
                    prefHubOid = getHubOid(true);
                if(prefTablespace == null)
                    prefTablespace = getTablespace(true);
                if(prefRouterOid == null)
                    prefRouterOid = getRouterOid(true);
                if(prefRouterPassword == null)
                    prefRouterPassword = getRouterPassword(true);
                if(prefRouterPreshared == null)
                    prefRouterPreshared = getRouterPreshared(true);
                if(prefCtrouterOid == null)
                    prefCtrouterOid = getCtRouterOid(true);
                if(prefCtrouterPassword == null)
                    prefCtrouterPassword = getCtRouterPassword(true);
                if(prefCtrouterPreshared == null)
                    prefCtrouterPreshared = getCtrouterPreshared(true);
                if(prefManagerOid == null)
                    prefManagerOid = getManagerOid(true);
                if(prefManagerPassword == null)
                    prefManagerPassword = getManagerPassword(true);
                if(prefManagerPreshared == null)
                    prefManagerPreshared = getManagerPreshared(true);
                break;
            case Factory:
                defaultManagerPreshared = DefaultAsFactoryManagerKey;
                defaultDomain = DomainBaseName + "factory.pesdca.com]"; 
                defaultHubOid = "[3:factory@factory.pesdca.com]";
                defaultTablespace = "asdb";
                defaultManagerOid = "[3:manager@factory.pesdca.com]";
                title = "Factory Domain";
                prefDomain = pref.get("factory"+DomainNameKey, null);
                prefHubOid = pref.get("factory"+HubKey, null);
                prefTablespace = pref.get("factory"+TablespaceKey, null);
                prefManagerOid = pref.get("factorycluster"+ManagerNameKey, null);
                prefManagerPassword = pref.get("factory"+ManagerPasswordKey, null);
                prefManagerPreshared = pref.get("factory"+ManagerPresharedKey, defaultManagerPreshared);
                if(prefDomain == null)
                    prefDomain = getDomain(true);
                if(prefHubOid == null)
                    prefHubOid = getHubOid(true);
                if(prefTablespace == null)
                    prefTablespace = getTablespace(true);
                if(prefManagerOid == null)
                    prefManagerOid = getManagerOid(true);
                if(prefManagerPassword == null)
                    prefManagerPassword = getManagerPassword(true);
                if(prefManagerPreshared == null)
                    prefManagerPreshared = getManagerPreshared(true);
                break;
            case Res:
                defaultManagerPreshared = DefaultAsResManagerKey;
                defaultDomain = DomainBaseName + "res.pesdca.com]"; 
                defaultHubOid = "[3:servicesHub@res.pesdca.com]";
                defaultTablespace = "asdb";
                defaultManagerOid = "[3:manager@res.pesdca.com]";
                title = "Res Domain";
                prefDomain = pref.get("res"+DomainNameKey, null);
                prefHubOid = pref.get("res"+HubKey, null);
                prefTablespace = pref.get("res"+TablespaceKey, null);
                prefManagerOid = pref.get("res"+ManagerNameKey, null);
                prefManagerPassword = pref.get("res"+ManagerPasswordKey, null);
                prefManagerPreshared = pref.get("res"+ManagerPresharedKey, null);
                if(prefDomain == null)
                    prefDomain = getDomain(true);
                if(prefHubOid == null)
                    prefHubOid = getHubOid(true);
                if(prefTablespace == null)
                    prefTablespace = getTablespace(true);
                if(prefManagerOid == null)
                    prefManagerOid = getManagerOid(true);
                if(prefManagerPassword == null)
                    prefManagerPassword = getManagerPassword(true);
                if(prefManagerPreshared == null)
                    prefManagerPreshared = getManagerPreshared(true);
                break;
            case Battery:
                defaultManagerPreshared = DefaultAsBatteryManagerKey;
                defaultDomain = DomainBaseName + "battery.pewla.com]"; 
                defaultHubOid = "[3:servicesHub@battery.pewla.com]";
                defaultTablespace = "asdb";
                defaultManagerOid = "[3:manager@battery.pewla.com]";
                title = "Battery Domain";
                prefDomain = pref.get("battery"+DomainNameKey, null);
                prefHubOid = pref.get("battery"+HubKey, null);
                prefTablespace = pref.get("battery"+TablespaceKey, null);
                prefManagerOid = pref.get("battery"+ManagerNameKey, null);
                prefManagerPassword = pref.get("battery"+ManagerPasswordKey, null);
                prefManagerPreshared = pref.get("battery"+ManagerPresharedKey, null);
                if(prefDomain == null)
                    prefDomain = getDomain(true);
                if(prefHubOid == null)
                    prefHubOid = getHubOid(true);
                if(prefTablespace == null)
                    prefTablespace = getTablespace(true);
                if(prefManagerOid == null)
                    prefManagerOid = getManagerOid(true);
                if(prefManagerPassword == null)
                    prefManagerPassword = getManagerPassword(true);
                if(prefManagerPreshared == null)
                    prefManagerPreshared = getManagerPreshared(true);
                break;
            case Ancillary:
                defaultManagerPreshared = DefaultAsAncillaryManagerKey;
                defaultDomain = DomainBaseName + "ancillary.pewla.com]"; 
                defaultHubOid = "[3:servicesHub@ancillary.pewla.com]";
                defaultTablespace = "asdb";
                defaultManagerOid = "[3:manager@ancillary.pewla.com]";
                title = "Battery Domain";
                prefDomain = pref.get("ancillary"+DomainNameKey, null);
                prefHubOid = pref.get("ancillary"+HubKey, null);
                prefTablespace = pref.get("ancillary"+TablespaceKey, null);
                prefManagerOid = pref.get("ancillary"+ManagerNameKey, null);
                prefManagerPassword = pref.get("ancillary"+ManagerPasswordKey, null);
                prefManagerPreshared = pref.get("ancillary"+ManagerPresharedKey, null);
                if(prefDomain == null)
                    prefDomain = getDomain(true);
                if(prefHubOid == null)
                    prefHubOid = getHubOid(true);
                if(prefTablespace == null)
                    prefTablespace = getTablespace(true);
                if(prefManagerOid == null)
                    prefManagerOid = getManagerOid(true);
                if(prefManagerPassword == null)
                    prefManagerPassword = getManagerPassword(true);
                if(prefManagerPreshared == null)
                    prefManagerPreshared = getManagerPreshared(true);
                break;
            case None:
                title = "Unknown Domain";
                break;
            case Rg:
                title = "Residential Gateway Domain";
                defaultDomain = DomainBaseName + "home_0.com]"; 
                prefDomain = pref.get("rg"+DomainNameKey, getDomain(true));
                defaultRequireAuth = false;
                prefRequireAuth = pref.getBoolean(RequireAuthKey, false);
                break;
        }
    }
    
    public boolean isAutoCreate()
    {
        return isAutoCreate(false);
    }        
    
    public boolean isAutoCreate(boolean preferences)
    {
        if(modified && !preferences)
            return guiAutoCreate;
        return defaultAutoCreate;
    }
    
    public Domain getDomainOid()
    {
        return Domain.create(getDomain());
    }
    public String getDomain()
    {
        return getDomain(false);
    }        
    
    public String getDomain(boolean preferences)
    {
        if(modified && !preferences)
            return guiDomain;
        if(prefDomain == null)
            return defaultDomain;
        return prefDomain;
    }
    public String getHubOid()
    {
        return getHubOid(false);
    }        
    
    public String getHubOid(boolean preferences)
    {
        if(modified && !preferences)
            return guiHubOid;
        if(prefHubOid == null)
            return defaultHubOid;
        return prefHubOid;
    }
    public String getTablespace()
    {
        return getTablespace(false);
    }        
    
    public String getTablespace(boolean preferences)
    {
        if(modified && !preferences)
            return guiTablespace;
        if(prefTablespace == null)
            return defaultTablespace;
        return prefTablespace;
    }
    
    public String getRouterOid()
    {
        return getRouterOid(false);
    }        
    
    public String getRouterOid(boolean preferences)
    {
        if(modified && !preferences)
            return guiRouterOid;
        if(prefRouterOid == null)
            return defaultRouterOid;
        return prefRouterOid;
    }
    
    public String getRouterPassword()
    {
        return getRouterPassword(false);
    }        
    
    public String getRouterPassword(boolean preferences)
    {
        if(modified && !preferences)
            return guiRouterPassword;
        if(prefRouterPassword == null)
            return defaultRouterPassword;
        return prefRouterPassword;
    }
    
    public String getRouterPreshared()
    {
        return getRouterPreshared(false);
    }        
    
    public String getRouterPreshared(boolean preferences)
    {
        if(modified && !preferences)
            return guiRouterPreshared;
        if(prefRouterPreshared == null)
            return defaultRouterPreshared;
        return prefRouterPreshared;
    }
    
    public String getCtRouterOid()
    {
        return getCtRouterOid(false);
    }        
    
    public String getCtRouterOid(boolean preferences)
    {
        if(modified && !preferences)
            return guiCtrouterOid;
        if(prefCtrouterOid == null)
            return defaultCtrouterOid;
        return prefCtrouterOid;
    }
    
    public String getCtRouterPassword()
    {
        return getCtRouterPassword(false);
    }        
    
    public String getCtRouterPassword(boolean preferences)
    {
        if(modified && !preferences)
            return guiCtrouterPassword;
        if(prefCtrouterPassword == null)
            return defaultCtrouterPassword;
        return prefCtrouterPassword;
    }
    
    public String getCtrouterPreshared()
    {
        return getCtrouterPreshared(false);
    }        
    
    public String getCtrouterPreshared(boolean preferences)
    {
        if(modified && !preferences)
            return guiCtrouterPreshared;
        if(prefCtrouterPreshared == null)
            return defaultCtrouterPreshared;
        return prefCtrouterPreshared;
    }
    
    public String getManagerOid()
    {
        return getManagerOid(false);
    }        
    
    public String getManagerOid(boolean preferences)
    {
        if(modified && !preferences)
            return guiManagerOid;
        if(prefManagerOid == null)
            return defaultManagerOid;
        return prefManagerOid;
    }
    
    public String getManagerPassword()
    {
        return getManagerPassword(false);
    }        
    
    public String getManagerPassword(boolean preferences)
    {
        if(modified && !preferences)
            return guiManagerPassword;
        if(prefManagerPassword == null)
            return defaultManagerPassword;
        return prefManagerPassword;
    }
    
    public String getManagerPreshared()
    {
        return getManagerPreshared(false);
    }        
    
    public String getManagerPreshared(boolean preferences)
    {
        if(modified && !preferences)
            return guiManagerPreshared;
        if(prefManagerPreshared == null)
            return defaultManagerPreshared;
        return prefManagerPreshared;
    }
}
