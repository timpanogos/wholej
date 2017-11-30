/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

import org.emitdo.app.res.db.oracle.OraResJdbc;
import org.emitdo.db.common.postgres.PgDatastoreBase;
import org.emitdo.research.app.dbAdmin.model.ConnectionData;
import org.emitdo.research.app.dbAdmin.model.ConnectionsData;
import org.emitdo.research.app.dbAdmin.view.ancpwr.AncPwrPanel;
import org.emitdo.research.app.dbAdmin.view.ancpwr.FrHistoryPanel;
import org.emitdo.research.app.dbAdmin.view.ancpwr.InverterStatusPanel;
import org.emitdo.research.app.swing.AppliedCallback;
import org.emitdo.research.app.swing.BasePanel;
import org.emitdo.research.app.swing.FrameBase;

public final class AncPwrManagerFrame extends ManagerFrame
{
    public static final String AncPwrDialogTableLocations = "ancPwrTableDialogs";
//    private final CannedResData cannedData;
    private AncPwrConnectionAction ancPwrConnAction;
    private UnitsAction unitsAction;
    private FrAction frAction;
    private IsAction isAction;
    
    private Connection sqlConnection;
    private final ConnectionsData connections;
    private final Hashtable<String, DialogBase> openDialogs;
    public final Hashtable<String, FramePreference> dialogPreferences;

    public AncPwrManagerFrame(String title, String frameKey, DbAdmin dbAdmin, ConnectionsData connections)
    {
        super(title, frameKey, dbAdmin);
//        cannedData = dbAdmin.getStorageControl().getCannedResData();
        this.connections = connections;
        openDialogs = new Hashtable<String, DialogBase>();
        dialogPreferences = new Hashtable<String, FramePreference>();
        Preferences dialogNodes = dbAdmin.getUserRoot().node(AncPwrDialogTableLocations);
//        dialogPreferences.put(PgUnitsJdbc.TableName, new FramePreference(dialogNodes, PgUnitsJdbc.TableName));  
        nextStartupPhase();
    }

    public ConnectionControl getConnectionControl()
    {
        return dbAdmin.getConnectionControl();
    }
    
    public void setCurrentlySelectedConnection(ConnectionData conn)
    {
        connections.currentlySelected = conn;
        if(conn == null)
        {
            if(sqlConnection != null)
                try{sqlConnection.close();} catch (SQLException e){}
            return;
        }
        if(sqlConnection != null)
            return;
        try
        {
            sqlConnection = ((PgDatastoreBase)conn.dataAccessor).getConnectionPool().getConnection();
//            frHistoryDa = new PgFrHistoryTableDa(sqlConnection);
//            inverterStatusDa = new PgInverterStatusTableDa(sqlConnection);
        } catch (SQLException e)
        {
            BasePanel.displayException(null, this, "Database connection error", "failed to connect", e);
        }
    }
    
    public FramePreference getFramePreference(String name)
    {
        return dialogPreferences.get(name);
    }
    
    public void dialogClosed(String name, DialogBase dialog)
    {
        DialogBase od = openDialogs.get(name);
        if(od == null)
            return;
        if(od != dialog)
            return;
        openDialogs.remove(name);
    }
    
    public void closeOpenDialogs()
    {
        for(Entry<String, DialogBase> entry : openDialogs.entrySet())
            entry.getValue().dispose();
    }

    public void refreshDialog(String name)
    {
        DialogBase dialog = openDialogs.get(name);
        if(dialog != null)
            dialog.refresh();
    }
    

    public void readTable(final AbstractTableModel tableModel)
    {
        final String theTableName = "junk"; //PgUnitsJdbc.TableName;
        setLeftMessage("Reading " + theTableName + " table");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                ResultSet rs = null;
                Statement stmt = null;
                boolean ok = true;
                try
                {
                    String sql = "select * from " + theTableName;
                    stmt = sqlConnection.createStatement();
                    rs = stmt.executeQuery(sql);
                }catch(Exception e)
                {
                    if(stmt != null)
                        try{stmt.close();} catch (SQLException e1){/*best try*/}
                    ok = false;
                    FrameBase.displayException(null, null, "Read failed", "Failed to read the " + theTableName + " table", e);
                }
                if(ok)
                {
                    final Statement theStmt = stmt;
                    final ResultSet theRs = rs;
                    Runnable runner = new Runnable()
                    {
                        @SuppressWarnings("null")
                        @Override
                        public void run()
                        {
                            try
                            {
                                while(theRs.next())
;//                                    ((UnitsTableModel)tableModel).addRow(sqlConnection, theRs);
                            } catch (Exception e)
                            {
                                FrameBase.displayException(null, null, "ResultSet traversal failed", "Failed to obtain data from the SQL result set", e);
                                return;
                            }finally
                            {
                                if(theStmt != null)
                                    try{theStmt.close();} catch (SQLException e){/*best try*/}
                            }
                        }
                    };
                    SwingUtilities.invokeLater(runner);
                }
                return null;
            }
        };
        worker.execute();
    }
    
//    public void getFrHistoryData(final SwappedFocusListener callback, final DOFObjectID providerId, final long start, final long end, final XYSeriesCollection dataset)
//    {
//        dbAdmin.setLeftMessage("Attempting to get fr_history data");
//        dbAdmin.setBusy(true);
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
//        {
//            @Override
//            protected Void doInBackground() throws Exception
//            {
//                Throwable exception = null;
//                try
//                {
//                    dataset.removeAllSeries();
//                    dataset.addSeries(frHistoryDa.getFrXySeriesForDates(providerId, start, end));
//                }catch(Throwable e)
//                {
//                    exception = e;
//                }
//                final Object theException = exception;
//                Runnable runner = new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        dbAdmin.setBusy(false);
//                        callback.focusRequested(theException);
//                    }
//                };
//                SwingUtilities.invokeLater(runner);
//                return null;
//            }
//        };
//        worker.execute();
//    }
//    
//    public void getInverterStatusData(final SwappedFocusListener callback, final DOFObjectID providerId, final long start, final long end, final IsChartType type, final XYSeriesCollection dataset)
//    {
//        dbAdmin.setLeftMessage("Attempting to get inverter_status data");
//        dbAdmin.setBusy(true);
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
//        {
//            @Override
//            protected Void doInBackground() throws Exception
//            {
//                Throwable exception = null;
//                try
//                {
//                    dataset.removeAllSeries();
//                    XYSeries[] series = inverterStatusDa.getInverterXySeriesForDates(providerId, start, end, type);
//                    for(int i=0; i < series.length; i++)
//                        dataset.addSeries(series[i]);
//                }catch(Throwable e)
//                {
//                    exception = e;
//                }
//                final Object theException = exception;
//                Runnable runner = new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        dbAdmin.setBusy(false);
//                        callback.focusRequested(theException);
//                    }
//                };
//                SwingUtilities.invokeLater(runner);
//                return null;
//            }
//        };
//        worker.execute();
//    }
    
    private void fireAppliedCallback(final AppliedCallback acb, final boolean ok, final String message, final Exception e, final Object context)
    {
        Runnable runner = new Runnable(){@Override public void run(){acb.complete(ok, message, e, context);}};
        SwingUtilities.invokeLater(runner);
        String title = "Changes commited";
        int type = JOptionPane.INFORMATION_MESSAGE;
        String msg = message;
        if(!ok)
        {
            title = "Changes could not be made";
            if(e != null)
                msg += "\n" + e.getMessage();
            if(e instanceof SQLException)
                msg +="\n" + OraResJdbc.getErrorString((SQLException)e);
            type = JOptionPane.ERROR_MESSAGE;
        }
        JOptionPane.showMessageDialog(this, msg, title, type);
    }
    
//    public void setUnit(final UnitsRowSelectionPanel unitPanel)
//    {
//        final UnitType type = unitPanel.getType();
//        final String oidStr = unitPanel.getOid();
//        final String iidStr = unitPanel.getIid();
//        final boolean insert = unitPanel.isInsert();
//        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
//        {
//            @Override
//            protected Void doInBackground() throws Exception
//            {
//                String msg = "ok";
//                boolean ok = true;
//                Exception ex = null;
//                PreparedStatement stmt = null;
//                try
//                {
//                    msg = "Invalid Oid: " + oidStr;
//                    DOFObjectID oid = DOFObjectID.create(oidStr);
//                    msg = "Invalid Iid: " + iidStr;
//                    DOFInterfaceID iid = DOFInterfaceID.create(iidStr);
//                    msg = "sql update failed";
//                    if(!insert)
//                        PgUnitsJdbc.setUnit(sqlConnection, type, oid, iid);
//                    else
//                        PgUnitsJdbc.addUnit(sqlConnection, type, oid, iid);
//                    msg = "ok";
//                }catch(Exception e)
//                {
//                    ex = e;
//                    ok = false;
//                }finally
//                {
//                    if(stmt != null)
//                        stmt.close();
//                    fireAppliedCallback(unitPanel, ok, msg, ex, null);
//                }
//                return null;
//            }
//        };
//        worker.execute();
//    }
    
    
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
        ancPwrConnAction = new AncPwrConnectionAction(fileMenu, toolBar);
        new JMenuItem(ancPwrConnAction);
        new ExitAction(fileMenu, toolBar);
        menuBar.add(fileMenu);
        
        JMenu asMenu = new JMenu("Tables");
        asMenu.setMnemonic(MenuTablesMnemonic);
        unitsAction = new UnitsAction(asMenu, toolBar);
        frAction = new FrAction(asMenu, toolBar);
        isAction = new IsAction(asMenu, toolBar);
        new JMenuItem(unitsAction);
        new JMenuItem(frAction);
        new JMenuItem(isAction);
        menuBar.add(asMenu);
        return menuBar;
    }

/*
    File
        Connections
        Exit
    Table
        units
        fr_history
        inverter_status
*/
    // Mnemonics
    public static int ClearPreferencesActionMnemonic = KeyEvent.VK_C; // in FrameBase - this will not change it
    public static int ConnActionMnemonic = KeyEvent.VK_A;
    public static int ExitActionMnemonic = KeyEvent.VK_X;   // in FrameBase, this will not change it
    
    public static int MenuTablesMnemonic = KeyEvent.VK_T;
    public static int UnitsActionMnemonic = KeyEvent.VK_U;
    public static int FrActionMnemonic = KeyEvent.VK_F;
    public static int IsActionMnemonic = KeyEvent.VK_I;
    
    // Accelerators
    public static int ExitActionAccelerator = KeyEvent.VK_F4; // in FrameBase, this will not change it
    public static int ConnActionAccelerator = KeyEvent.VK_C;
    public static int UnitsActionAccelerator = KeyEvent.VK_U; 
    public static int FrActionAccelerator = KeyEvent.VK_F; 
    public static int IsActionAccelerator = KeyEvent.VK_I; 
    
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
        swapContentPane(new AncPwrPanel(this));
        super.initGui();
    }
    
    @Override
    public void close()
    {
        setCurrentlySelectedConnection(null);
    }
    
    final class UnitsAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Units...";
        public static final String MouseOver = "Units Table Setup";
        public final int Mnemonic = UnitsActionMnemonic;
        public final int Accelerator = UnitsActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public UnitsAction(JMenu menu, JToolBar toolBar)
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
//            DialogBase dialog = openDialogs.get(PgUnitsJdbc.TableName);
//            if(dialog != null)
//            {
//                dialog.requestFocus();
//                return;
//            }
//            dialog = new UnitDialog(AncPwrManagerFrame.this, AncPwrManagerFrame.this, null);
//            openDialogs.put(PgUnitsJdbc.TableName, dialog);
        }
    }
    
    final class FrAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "fr_history";
        public static final String MouseOver = "Frequence Regulation History";
        public final int Mnemonic = FrActionMnemonic;
        public final int Accelerator = FrActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public FrAction(JMenu menu, JToolBar toolBar)
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
            swapContentPane(new FrHistoryPanel(AncPwrManagerFrame.this));
        }
    }

    final class IsAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "inverter_status";
        public static final String MouseOver = "Inverter Status History";
        public final int Mnemonic = IsActionMnemonic;
        public final int Accelerator = IsActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public IsAction(JMenu menu, JToolBar toolBar)
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
            swapContentPane(new InverterStatusPanel(AncPwrManagerFrame.this));
        }
    }
    
    final class AncPwrConnectionAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Connections";
        public static final String MouseOver = "Ancillary power connections";
        public final int Mnemonic = ConnActionMnemonic;
        public final int Accelerator = ConnActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public AncPwrConnectionAction(JMenu menu, JToolBar toolBar)
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
            swapContentPane(new AncPwrPanel(AncPwrManagerFrame.this));
        }
    }
}
