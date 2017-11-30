/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.util.prefs.Preferences;

import org.emitdo.app.as.common.EnterpriseDomainStorageManagement;

public class CannedResData
{
    public static String CannedTablespaceKey = "res.canned.tablespace";
    public static String CannedDeviceCountKey = "res.canned.deviceCount";
    public static String CannedGatewayCountKey = "res.canned.gatewayCount";
    public static String CannedSubGatewayCountKey = "res.canned.subGatewayCount";
    public static String CannedManagersPerDeviceCountKey = "res.canned.managersPerDevice";
    public static String CannedManagersPerGatewayCountKey = "res.canned.managersPerSubGateway";
    public static String CannedManagersPerSubGatewayCountKey = "res.canned.managersPerSubGateway";
    public static String CannedManagersPerVersionKey = "res.canned.managersPerVersion";
    
    public static String DefaultTablespace = "res";
    public static int DefaultDeviceCount = 3;
    public static int DefaultGatewayCount = 3;
    public static int DefaultSubGatewayCount = 3;
    public static int DefaultManagersPerDevice = 3;
    public static int DefaultManagersPerGateway = 2;
    public static int DefaultManagersPerSubGateway = 1;
    public static int DefaultManagersPerVersion = 3;
    public static int DefaultDeviceScopeStartIndex = 256;
    public static int DefaultFactoryScope = 64;
    public static boolean DefaultFailOnAsExists = true;
    
    public ConnectionsData connections;
    public ConnectionsData clusterConnections;
    public ConnectionsData platformConnections;
    public ConnectionsData factoryConnections;
    
    public ConnectionData connData;
    public EnterpriseDomainStorageManagement clusterDa;
    public EnterpriseDomainStorageManagement platformDa;
    public EnterpriseDomainStorageManagement factoryDa;
    public DomainData clusterData;
    public DomainData platformData;
    public DomainData factoryData;
    
    public String tablespace;
    public int deviceCount;
    public int gatewayCount;
    public int gatewayDeviceCount;
    public int managersPerDevice;
    public int managersPerGateway;
    public int managersPerGatewayDevice;
    public int managersPerVersion;
    public int totalManagers;
    public int totalVersions;
    public boolean failOnAsExists;
    public int deviceScopeStartIndex;
    public int factoryScope;
    public int managersScope;
    
    public String guiTablespace;
    public String guiDeviceCount;
    public String guiGatewayCount;
    public String guiGatewayDeviceCount;
    public String guiManagersPerDevice;
    public String guiManagersPerGateway;
    public String guiManagersPerGatewayDevice;
    public String guiManagersPerVersion;
    
    public final Preferences pref;
    public CannedResData(Preferences pref)
    {
        this.pref = pref;
        tablespace = pref.get(CannedTablespaceKey, DefaultTablespace);
        deviceCount = pref.getInt(CannedDeviceCountKey, DefaultDeviceCount);
        gatewayCount = pref.getInt(CannedGatewayCountKey, DefaultGatewayCount);
        gatewayDeviceCount = pref.getInt(CannedSubGatewayCountKey, DefaultSubGatewayCount);
        managersPerDevice = pref.getInt(CannedManagersPerDeviceCountKey, DefaultManagersPerDevice);
        managersPerGateway = pref.getInt(CannedManagersPerGatewayCountKey, DefaultManagersPerGateway);
        managersPerGatewayDevice = pref.getInt(CannedManagersPerSubGatewayCountKey, DefaultManagersPerSubGateway);
        managersPerVersion = pref.getInt(CannedManagersPerVersionKey, DefaultManagersPerVersion);
        deviceScopeStartIndex = DefaultDeviceScopeStartIndex;
        factoryScope = DefaultFactoryScope;
        managersScope = AsDomainInitializer.AllPermissionGroups;
        failOnAsExists = DefaultFailOnAsExists;
    }
    
    public void savePreferences()
    {
        pref.put(CannedTablespaceKey, tablespace);
        pref.putInt(CannedDeviceCountKey, deviceCount);
        pref.putInt(CannedGatewayCountKey, gatewayCount);
        pref.putInt(CannedSubGatewayCountKey, gatewayDeviceCount);
        pref.putInt(CannedManagersPerDeviceCountKey, managersPerDevice);
        pref.putInt(CannedManagersPerGatewayCountKey, managersPerGateway);
        pref.putInt(CannedManagersPerSubGatewayCountKey, managersPerGatewayDevice);
    }

    public int getByte(String value)
    {
        int rvalue = -1;
        try
        {
            rvalue = Integer.parseInt(value.trim());
            if(rvalue <= 0)
                return 1;
            if(rvalue > 255)
                return 255;
        }catch(Exception e)
        {
        }
        return rvalue;
    }
    
    public int getDeviceCount()
    {
        if(guiDeviceCount == null)
            return deviceCount;
        return getByte(guiDeviceCount);
    }
    
    public String getTablespace()
    {
        if(guiTablespace == null)
            return tablespace;
        return guiTablespace;
    }
    
    public int getGatewayCount()
    {
        if(guiGatewayCount == null)
            return gatewayCount;
        return getByte(guiGatewayCount);
    }
    
    public int getGatewayDeviceCount()
    {
        if(guiGatewayDeviceCount == null)
            return gatewayDeviceCount;
        return getByte(guiGatewayDeviceCount);
    }
    
    public int getManagersPerDevice()
    {
        if(guiManagersPerDevice == null)
            return managersPerDevice;
        return getByte(guiManagersPerDevice);
    }
    
    public int getManagersPerGateway()
    {
        if(guiManagersPerGateway == null)
            return managersPerGateway;
        return getByte(guiManagersPerGateway);
    }
    
    public int getManagersPerGatewayDevice()
    {
        if(guiManagersPerGatewayDevice == null)
            return managersPerGatewayDevice;
        return getByte(guiManagersPerGatewayDevice);
    }
    
    public int getManagersPerVersion()
    {
        if(guiManagersPerVersion == null)
            return managersPerVersion;
        return getByte(guiManagersPerVersion);
    }
}