/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.FrameBase.CloseListener;

public abstract class ManagerFrame extends FrameBase implements CloseListener
{
    public static final String FramePrefixKey = DbAdmin.DbAdminPrefixKey + ".frames.";
//    private final StorageControl storageControl;
//    private final ConnectionControl connectionControl;
    protected final DbAdmin dbAdmin;
    
    public ManagerFrame(String title, String frameKey, DbAdmin dbAdmin)
    {
        super(title, dbAdmin.getUserRoot(), FramePrefixKey + frameKey);
        this.dbAdmin = dbAdmin;
//        storageControl = new StorageControl(this);
//        connectionControl = new ConnectionControl(this);
        addCloseListener(this);
    }

//    public void setBusy(boolean busy)
//    {
////        storageControl.setBusy(busy);
////        connectionControl.setBusy(busy);
//    }
    
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
        
//        connectionControl.addMenu(menuBar, toolBar);
//        storageControl.addMenu(menuBar, toolBar);
        return menuBar;
    }

/*
    File
        Exit
*/
    // Mnemonics
    public static int ClearPreferencesActionMnemonic = KeyEvent.VK_C; // in FrameBase - this will not change it
    public static int ExitActionMnemonic = KeyEvent.VK_X;   // in FrameBase, this will not change it
    public static int MenuFileMnemonic = KeyEvent.VK_F;

    
    // Accelerators
    public static int ExitActionAccelerator = KeyEvent.VK_F4; // in FrameBase, this will not change it
    
//    private void nextStartupPhase()
//    {
//        try
//        {
//            nextStartupPhase(new Dimension(660, 460));
//        } catch (Exception e)
//        {
//            displayException(null, this, "GUI initialization failed", "", e);
//        }
//    }
    
//    @Override
//    protected void initGui()
//    {
//        swapContentPane(new AsPanel(DomainType.Cluster, dbAdmin, this));
//        super.initGui();
//    }
    
//    @Override
//    public void close()
//    {
////        storageControl.close();
////        connectionControl.close();
//    }
    
}
