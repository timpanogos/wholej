/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.model.CannedResData;
import org.emitdo.research.app.dbAdmin.model.ConnectionsData;
import org.emitdo.research.app.dbAdmin.view.res.ResPanel;

public final class ResManagerFrame extends ManagerFrame
{
    private final CannedResData cannedData;
    
    public ResManagerFrame(String title, String frameKey, DbAdmin dbAdmin)
    {
        super(title, frameKey, dbAdmin);
        cannedData = new CannedResData(getUserRoot());
        ConnectionControl connectionControl = dbAdmin.getConnectionControl();
        cannedData.clusterConnections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Cluster);
        cannedData.platformConnections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Res);
        cannedData.factoryConnections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.As, DomainType.Factory);
        cannedData.connections = ConnectionsData.getAvailableConnections(connectionControl, StorageType.Res, null);
        
        StorageControl storageControl = dbAdmin.getStorageControl();
        cannedData.clusterData =  storageControl.getDomainData(DomainType.Cluster);
        cannedData.platformData =  storageControl.getDomainData(DomainType.Res);
        cannedData.factoryData =  storageControl.getDomainData(DomainType.Factory);
        nextStartupPhase();
    }

    public ConnectionControl getConnectionControl()
    {
        return dbAdmin.getConnectionControl();
    }
    
    public StorageControl getStorageControl()
    {
        return dbAdmin.getStorageControl();
    }
    
    public CannedResData getCannedData()
    {
        return cannedData;
    }
    
    @Override
    public void setBusy(boolean busy)
    {
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
        new ExitAction(fileMenu, toolBar);
        menuBar.add(fileMenu);
        return menuBar;
    }

/*
    File
        Exit
*/
    // Mnemonics
    public static int ClearPreferencesActionMnemonic = KeyEvent.VK_C; // in FrameBase - this will not change it
    public static int ExitActionMnemonic = KeyEvent.VK_X;   // in FrameBase, this will not change it
    
    
    // Accelerators
    public static int ExitActionAccelerator = KeyEvent.VK_F4; // in FrameBase, this will not change it
    
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
        swapContentPane(new ResPanel(cannedData, this));
        super.initGui();
    }
    
    @Override
    public void close()
    {
//        storageControl.close();
//        connectionControl.close();
    }
    
}
