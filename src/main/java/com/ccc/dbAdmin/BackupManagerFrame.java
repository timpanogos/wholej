/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import org.emitdo.research.app.dbAdmin.view.BackupPanel;

public final class BackupManagerFrame extends ManagerFrame
{
//    private final CannedResData cannedData;
    
    public BackupManagerFrame(String title, String frameKey, DbAdmin dbAdmin)
    {
        super(title, frameKey, dbAdmin);
//        cannedData = dbAdmin.getStorageControl().getCannedResData();
        nextStartupPhase();
    }

    public ConnectionControl getConnectionControl()
    {
        return dbAdmin.getConnectionControl();
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
        swapContentPane(new BackupPanel(this));
        super.initGui();
    }
    
    @Override
    public void close()
    {
//        storageControl.close();
//        connectionControl.close();
    }
    
}
