/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.emitdo.app.logging.LoggerCommands;
import org.emitdo.app.util.Commands;
import org.emitdo.internal.as.storage.jdb.JdbAsdbSqlGenerator;
import org.emitdo.research.app.dbAdmin.model.DerbyServerData;
import org.emitdo.research.app.dbAdmin.model.RmiData;
import org.emitdo.research.app.dbAdmin.model.RmiJdbData;
import org.emitdo.research.app.dbAdmin.view.ServerPanel;
import org.emitdo.research.app.dbAdmin.view.connection.AsConnectionPanel;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.FrameBase.CloseListener;

public final class DbAdmin extends FrameBase implements CloseListener
{
    public static DbAdminCommands asAdminCommands;
    public static final JdbAsdbSqlGenerator AsGenerator = new JdbAsdbSqlGenerator("asdb");
    public static final String DbAdminPrefixKey = "org.emitdo.app.dbAdmin";
    public static final String AsAdminPreferencesNode = "org/emitdo/research/app/dbAdmin";
    
    private static final String DefaultLogFile = "/var/opt/enc/app/log/dbAdmin.log";
    
    private static final String ApplicationTitle = "Db Admin";
    
    private final StorageControl storageControl;
    private final ConnectionControl connectionControl;
    private final DerbyServerData derbyServerData;
    private final RmiData rmiData;
    @SuppressWarnings("unused")
    private final RmiJdbData rmiJdbClusterData;
    @SuppressWarnings("unused")
    private final RmiJdbData rmiJdbPlatformData;
    @SuppressWarnings("unused")
    private final RmiJdbData rmiJdbFactoryData;
    
    public DbAdmin()
    {
        super(ApplicationTitle, AsAdminPreferencesNode);
        connectionControl = new ConnectionControl(this);
        storageControl = new StorageControl(this);
        addCloseListener(this);
        derbyServerData = new DerbyServerData(getUserRoot());
        rmiData = new RmiData(userRoot);
        rmiJdbClusterData = new RmiJdbData(DomainType.Cluster, rmiData, userRoot);
        rmiJdbPlatformData = new RmiJdbData(DomainType.Res, rmiData, userRoot);
        rmiJdbFactoryData = new RmiJdbData(DomainType.Factory, rmiData, userRoot);
    }

    @Override
    public void setBusy(boolean busy)
    {
        storageControl.setBusy(busy);
        connectionControl.setBusy(busy);
    }
    
    public ConnectionControl getConnectionControl()
    {
        return connectionControl;
    }
    
    public StorageControl getStorageControl()
    {
        return storageControl;
    }
    
    @Override
    public JMenuBar createMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        toolBar = new JToolBar();
        toolBar.setRollover(true);

        // file Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(MenuFileMnemonic);
        new ServerAction(fileMenu, toolBar);
        new ClearUserPreferencesAction(fileMenu, toolBar, this);
        fileMenu.addSeparator();
        new ExitAction(fileMenu, toolBar);
        menuBar.add(fileMenu);
        
        connectionControl.addMenu(menuBar, toolBar);
        storageControl.addMenu(menuBar, toolBar);
        return menuBar;
    }

/*
    File
        Derby server
        Clear Preferences
        Exit
    Connections
        AS
        RES
        Backup
        Naming
        Ancillary Power
    Storage
        Cluster
        Platform
        Factory
        Rg
        RES
        Backup
        Naming
        Ancillary Power
*/
    // Mnemonics
    public static int MenuFileMnemonic = KeyEvent.VK_F;
    public static int ClearPreferencesActionMnemonic = KeyEvent.VK_C; // in FrameBase - this will not change it
    public static int ServerActionMnemonic = KeyEvent.VK_D;
    public static int ExitActionMnemonic = KeyEvent.VK_X;   // in FrameBase, this will not change it
    public static int RmiJdbActionMnemonic = KeyEvent.VK_M;
    
    public static int MenuConnectionsMnemonic = KeyEvent.VK_C;
    public static int AsConnectionActionMnemonic = KeyEvent.VK_A;
    public static int BackupConnectionActionMnemonic = KeyEvent.VK_B;
    public static int NamingConnectionActionMnemonic = KeyEvent.VK_N;
    public static int ResConnectionActionMnemonic = KeyEvent.VK_R;
    public static int AncillaryConnectionActionMnemonic = KeyEvent.VK_P;
    public static int BatteryConnectionActionMnemonic = KeyEvent.VK_T;
    
    public static int MenuStorageMnemonic = KeyEvent.VK_M;
    public static int BackupManagerActionMnemonic = KeyEvent.VK_B;
    public static int AsClusterManagerActionMnemonic = KeyEvent.VK_C;
    public static int AsFactoryManagerActionMnemonic = KeyEvent.VK_F;
    public static int AsRgManagerActionMnemonic = KeyEvent.VK_G;
    public static int NamingManagerActionMnemonic = KeyEvent.VK_N;
    public static int AsPlatformManagerActionMnemonic = KeyEvent.VK_P;
    public static int AsBatteryManagerActionMnemonic = KeyEvent.VK_B;
    public static int AsAncillaryManagerActionMnemonic = KeyEvent.VK_A;
    public static int ResManagerActionMnemonic = KeyEvent.VK_R;
    public static int AncillaryManagerActionMnemonic = KeyEvent.VK_A;
    public static int BatteryManagerActionMnemonic = KeyEvent.VK_T;
    
    // Accelerators
    public static int ExitActionAccelerator = KeyEvent.VK_F4; // in FrameBase, this will not change it
    public static int BackupManagerActionAccelerator = KeyEvent.VK_B; 
    public static int AsClusterManagerActionAccelerator = KeyEvent.VK_C; 
    public static int AsFactoryManagerActionAccelerator = KeyEvent.VK_F; 
    public static int AsRgManagerActionAccelerator = KeyEvent.VK_G; 
    public static int NamingManagerActionAccelerator = KeyEvent.VK_N; 
    public static int AsPlatformManagerActionAccelerator = KeyEvent.VK_P; 
    public static int AsBatteryManagerActionAccelerator = KeyEvent.VK_P; 
    public static int AsAncillaryManagerActionAccelerator = KeyEvent.VK_P; 
    public static int ResManagerActionAccelerator = KeyEvent.VK_R;
    public static int ServerActionAccelerator = KeyEvent.VK_S;
    public static int RmiJdbActionAccelerator = KeyEvent.VK_M;
    public static int AncillaryManagerActionAccelerator = KeyEvent.VK_A;
    public static int BatteryManagerActionAccelerator = KeyEvent.VK_T;
    
    public static int BatteryConnectionActionAccelerator = KeyEvent.VK_T; 
    public static int AncillaryConnectionActionAccelerator = KeyEvent.VK_U; 
    public static int ResConnectionActionAccelerator = KeyEvent.VK_V; 
    public static int AsConnectionActionAccelerator = KeyEvent.VK_W; 
    public static int BackupConnectionActionAccelerator = KeyEvent.VK_Y; 
    public static int NamingConnectionActionAccelerator = KeyEvent.VK_Z; 
    
    private void nextStartupPhase()
    {
        try
        {
            nextStartupPhase(new Dimension(660, 460));
        } catch (Exception e)
        {
            displayException(null, this, "GUI initialization failed", "", e);
        }
    }
    
    @Override
    protected void initGui()
    {
        swapContentPane(new AsConnectionPanel(this));
        super.initGui();
    }
    
    @Override
    public void close()
    {
        storageControl.close();
        connectionControl.close();
        System.exit(0);
    }
    
    final class ServerAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Derby Server...";
        public static final String MouseOver = "Control the Derby/JavaDB Server";
        public final int Mnemonic = DbAdmin.ServerActionMnemonic;
        public final int Accelerator = DbAdmin.ServerActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;

        public ServerAction(JMenu menu, JToolBar toolBar)
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
            swapContentPane(new ServerPanel(derbyServerData, DbAdmin.this));
        }
    }
    
    public enum DomainType
    {
        None, Cluster, Res, Factory, Rg, Battery, Ancillary;
        public static DomainType getType(int ordinal)
        {
            switch (ordinal)
            {
                case 1:
                    return Cluster;
                case 2:
                    return Res;
                case 3:
                    return Factory;
                case 4:
                    return Rg;
                case 5:
                    return Battery;
                case 6:
                    return Ancillary;
                default:
                    return None;
            }
        }
    };
    public enum StorageType {As, Res, Backup, Naming, Battery, Ancillary};
    public enum VendorType {Oracle, JavaDb, Postgres};
    public enum ConnType {Jdbc, Web};
    
    public static void main(String[] args)
    {
        try 
        {
            AsGenerator.setUseSameConnection(true, true);
            
            Commands[] commands = new Commands[3];
            commands[0] = AsGenerator;
            asAdminCommands = new DbAdminCommands(args);
            asAdminCommands.setLog4j(true, LoggerCommands.DefaultLog4jConfig, null, true, false);
            asAdminCommands.setLogFile(DefaultLogFile, false);
            new DbAdmin().nextStartupPhase();
        }catch(Exception e) 
        {
            e.printStackTrace();
        }
    }
}
