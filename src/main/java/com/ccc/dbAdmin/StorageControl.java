/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;

import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.model.CannedResData;
import org.emitdo.research.app.dbAdmin.model.ConnectionsData;
import org.emitdo.research.app.dbAdmin.model.DomainData;

public class StorageControl
{
    private static final String AsClusterManager = "asClusterManager";
    private static final String AsResManager = "asResManager";
    private static final String AsBatteryManager = "asBatteryManager";
    private static final String AsAncillaryManager = "asAncillaryManager";
    private static final String AsFactoryManager = "asFactoryManager";
    private static final String RgManager = "rgManager";
    private static final String ResManager = "resManager";
    private static final String BatteryManager = "batteryManager";
    private static final String AncillaryManager = "ancillaryManager";
    private static final String BackupManager = "backupManager";
    private static final String NamingManager = "namingManager";
    
    private final DbAdmin dbAdmin;
    private ClusterManagerAction clusterManagerAction;
    private AsResManagerAction asResManagerAction;
    private AsBatteryManagerAction asBatteryManagerAction;
    private AsAncillaryManagerAction asAncillaryManagerAction;
    private FactoryManagerAction factoryManagerAction;
    private RgManagerAction rgManagerAction;
    private ResManagerAction resManagerAction;
    private BatteryManagerAction batteryManagerAction;
    private AncillaryManagerAction ancillaryManagerAction;
    private BackupManagerAction backupManagerAction;
    private NamingManagerAction namingManagerAction;
    
    private final Hashtable<String, ManagerFrame> managers;
    private final ConnectionControl connectionControl;
    private final Hashtable<String, DomainData> domainData;
    
    public StorageControl(DbAdmin dbAdmin)
    {
        this.dbAdmin = dbAdmin;
        connectionControl = dbAdmin.getConnectionControl();
        managers = new Hashtable<String, ManagerFrame>();
        domainData = new Hashtable<String, DomainData>();
        
        DomainData data = new DomainData(DomainType.Cluster, dbAdmin.getUserRoot());
        domainData.put(DomainType.Cluster.name(), data);
        data = new DomainData(DomainType.Res, dbAdmin.getUserRoot());
        domainData.put(DomainType.Res.name(), data);
        data = new DomainData(DomainType.Battery, dbAdmin.getUserRoot());
        domainData.put(DomainType.Battery.name(), data);
        data = new DomainData(DomainType.Ancillary, dbAdmin.getUserRoot());
        domainData.put(DomainType.Ancillary.name(), data);
        data = new DomainData(DomainType.Factory, dbAdmin.getUserRoot());
        domainData.put(DomainType.Factory.name(), data);
        data = new DomainData(DomainType.Rg, dbAdmin.getUserRoot());
        domainData.put(DomainType.Rg.name(), data);
    }

    public void addMenu(JMenuBar menuBar, JToolBar toolBar)
    {
        // Storage Menu
        JMenu storageMenu = new JMenu("Managers");
        storageMenu.setMnemonic(DbAdmin.MenuStorageMnemonic);
        clusterManagerAction = new ClusterManagerAction(storageMenu, toolBar);
        asResManagerAction = new AsResManagerAction(storageMenu, toolBar);
        asBatteryManagerAction = new AsBatteryManagerAction(storageMenu, toolBar);
        asAncillaryManagerAction = new AsAncillaryManagerAction(storageMenu, toolBar);
        factoryManagerAction = new FactoryManagerAction(storageMenu, toolBar);
        rgManagerAction = new RgManagerAction(storageMenu, toolBar);
        resManagerAction = new ResManagerAction(storageMenu, toolBar);
        batteryManagerAction = new BatteryManagerAction(storageMenu, toolBar);
        ancillaryManagerAction = new AncillaryManagerAction(storageMenu, toolBar);
        backupManagerAction = new BackupManagerAction(storageMenu, toolBar);
        namingManagerAction = new NamingManagerAction(storageMenu, toolBar);
        menuBar.add(storageMenu);
    }

    public DomainData getDomainData(DomainType type)
    {
        DomainData data = domainData.get(type.name()); 
        return data;
    }
    
    public CannedResData getCannedResData()
    {
        return ((ResManagerFrame)managers.get(ResManager)).getCannedData();
    }
    
    public void close()
    {
        for(Entry<String, ManagerFrame> entry : managers.entrySet())
            entry.getValue().close();
        managers.clear();
    }
    
    public void setBusy(boolean busy)
    {
        clusterManagerAction.setEnabled(!busy);
        asResManagerAction.setEnabled(!busy);
        asBatteryManagerAction.setEnabled(!busy);
        asAncillaryManagerAction.setEnabled(!busy);
        factoryManagerAction.setEnabled(!busy);
        rgManagerAction.setEnabled(!busy);
        resManagerAction.setEnabled(!busy);
        batteryManagerAction.setEnabled(!busy);
        ancillaryManagerAction.setEnabled(!busy);
        backupManagerAction.setEnabled(!busy);
        namingManagerAction.setEnabled(!busy);
    }
    
    final class ClusterManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "AS Cluster...";
        public static final String MouseOver = "AS cluster domain storage manager";
        public final int Mnemonic = DbAdmin.AsClusterManagerActionMnemonic;
        public final int Accelerator = DbAdmin.AsClusterManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public ClusterManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ConnectionsData connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Cluster);
            managers.put(
                    AsClusterManager, 
                    new AsManagerFrame("AS Cluster Manager", AsClusterManager, DomainType.Cluster, connections, null, dbAdmin));
        }
    }
    
    final class AsResManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "AS Res...";
        public static final String MouseOver = "AS platfom domain storage manager";
        public final int Mnemonic = DbAdmin.AsPlatformManagerActionMnemonic;
        public final int Accelerator = DbAdmin.AsPlatformManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public AsResManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ConnectionsData clusterConnections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Cluster);
            ConnectionsData connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Res);
            managers.put(
                    AsResManager, 
                    new AsManagerFrame("AS Res Manager", AsResManager, DomainType.Res, connections, clusterConnections, dbAdmin));
        }
    }
    
    final class AsBatteryManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "AS Battery...";
        public static final String MouseOver = "AS battery domain storage manager";
        public final int Mnemonic = DbAdmin.AsBatteryManagerActionMnemonic;
        public final int Accelerator = DbAdmin.AsBatteryManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public AsBatteryManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ConnectionsData clusterConnections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Cluster);
            ConnectionsData connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Battery);
            managers.put(
                    AsBatteryManager, 
                    new AsManagerFrame("AS Battery Manager", AsBatteryManager, DomainType.Battery, connections, clusterConnections, dbAdmin));
        }
    }
    
    final class AsAncillaryManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "AS Ancillary...";
        public static final String MouseOver = "AS battery domain storage manager";
        public final int Mnemonic = DbAdmin.AsAncillaryManagerActionMnemonic;
        public final int Accelerator = DbAdmin.AsAncillaryManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public AsAncillaryManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ConnectionsData clusterConnections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Cluster);
            ConnectionsData connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Ancillary);
            managers.put(
                    AsAncillaryManager, 
                    new AsManagerFrame("AS Ancillary Manager", AsAncillaryManager, DomainType.Ancillary, connections, clusterConnections, dbAdmin));
        }
    }
    
    
    
    final class FactoryManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "AS Factory...";
        public static final String MouseOver = "AS factory domain storage manager";
        public final int Mnemonic = DbAdmin.AsFactoryManagerActionMnemonic;
        public final int Accelerator = DbAdmin.AsFactoryManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public FactoryManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ConnectionsData clusterConnections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Cluster);
            ConnectionsData connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Factory);
            managers.put(
                    AsFactoryManager,
                    new AsManagerFrame("AS Factory Manager", AsFactoryManager, DomainType.Factory, connections,clusterConnections, dbAdmin));
        }
    }
    
    final class RgManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Rg Manager...";
        public static final String MouseOver = "Residential Gateway Storage Manager";
        public final int Mnemonic = DbAdmin.AsRgManagerActionMnemonic;
        public final int Accelerator = DbAdmin.AsRgManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public RgManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ConnectionsData connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Rg);
            managers.put(
                    RgManager,
                    new AsManagerFrame("AS Residential Gateway Manager", RgManager, DomainType.Rg, connections, null, dbAdmin));
        }
    }
    
    final class ResManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Res Manager...";
        public static final String MouseOver = "Res platform Manager";
        public final int Mnemonic = DbAdmin.ResManagerActionMnemonic;
        public final int Accelerator = DbAdmin.ResManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public ResManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            managers.put(
                    ResManager,
                    new ResManagerFrame("RES Manager", ResManager, dbAdmin));
        }
    }
    
    final class BatteryManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Battery Manager...";
        public static final String MouseOver = "Battery platform manager";
        public final int Mnemonic = DbAdmin.BatteryManagerActionMnemonic;
        public final int Accelerator = DbAdmin.BatteryManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public BatteryManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ConnectionsData connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.Ancillary, null);
            managers.put(
                    BatteryManager,
                    new BatteryManagerFrame("Battery Manager", BatteryManager, dbAdmin, connections));
        }
    }
    
    final class AncillaryManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Ancillary Manager...";
        public static final String MouseOver = "Ancillary platform manager";
        public final int Mnemonic = DbAdmin.AncillaryManagerActionMnemonic;
        public final int Accelerator = DbAdmin.AncillaryManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public AncillaryManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            ConnectionsData connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.Ancillary, null);
            managers.put(
                    AncillaryManager,
                    new AncPwrManagerFrame("Ancillary Power Manager", AncillaryManager, dbAdmin, connections));
        }
    }
    
    final class BackupManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Backup Manager...";
        public static final String MouseOver = "Backup Storage Manager";
        public final int Mnemonic = DbAdmin.BackupManagerActionMnemonic;
        public final int Accelerator = DbAdmin.BackupManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public BackupManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
//            if(managers.get(ResManager) == null)
//                managers.put(
//                        ResManager,
//                        new ResManagerFrame("RES Manager", "resManager", dbAdmin));
//            
            managers.put(
                    BackupManager,
                    new BackupManagerFrame("Backup Manager", BackupManager, dbAdmin));
        }
    }
    
    final class NamingManagerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Naming Manager...";
        public static final String MouseOver = "Naming Storage Manager";
        public final int Mnemonic = DbAdmin.NamingManagerActionMnemonic;
        public final int Accelerator = DbAdmin.NamingManagerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public NamingManagerAction(JMenu menu, JToolBar toolBar)
        {
            putValue(NAME, Label);
            putValue(ACTION_COMMAND_KEY, Label);
            putValue(MNEMONIC_KEY, new Integer(Mnemonic));
            putValue(SHORT_DESCRIPTION, MouseOver);

            JMenuItem item = menu.add(this);
            item.setAccelerator(KeyStroke.getKeyStroke(Accelerator, AcceleratorMask));
            setEnabled(true);
            toolBar.addSeparator();
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            managers.put(
                    NamingManager,
                    new NamingManagerFrame("Naming Manager", NamingManager, dbAdmin));
        }
    }
}
