/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

import org.emitdo.app.as.common.CredentialsNotFoundException;
import org.emitdo.app.as.common.EnterpriseDomainStorageManagement;
import org.emitdo.app.res.db.oracle.OraDeviceJdbc;
import org.emitdo.app.res.db.oracle.OraResJdbc;
import org.emitdo.as.jdb.domain.JdbAuthenticationNodeJdbc;
import org.emitdo.as.jdb.domain.JdbRemoteDomainNodeJdbc;
import org.emitdo.as.jdb.domain.JdbSecureGroupNodeJdbc;
import org.emitdo.as.jdb.domain.JdbStorageNodeJdbc;
import org.emitdo.oal.DOFAuthenticator.AuthenticationNode;
import org.emitdo.oal.DOFAuthenticator.RemoteDomainNode;
import org.emitdo.oal.DOFCredentials;
import org.emitdo.oal.DOFCredentials.Key;
import org.emitdo.oal.DOFCredentials.Password;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.oal.DOFUtil;
import org.emitdo.oal.security.DOFPermission;
import org.emitdo.oal.security.DOFPermissionSet;
import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.model.ConnectionsData;
import org.emitdo.research.app.dbAdmin.model.asdb.AuthNodeTableModel;
import org.emitdo.research.app.dbAdmin.model.asdb.GroupNodeTableModel;
import org.emitdo.research.app.dbAdmin.model.asdb.RdNodeTableModel;
import org.emitdo.research.app.dbAdmin.model.asdb.RemoteDomainInfo;
import org.emitdo.research.app.dbAdmin.model.asdb.ScopePermission;
import org.emitdo.research.app.dbAdmin.view.DomainPanel;
import org.emitdo.research.app.dbAdmin.view.asdb.authentication.AuthIdModifyDialog.AuthIdModifyPanel;
import org.emitdo.research.app.dbAdmin.view.asdb.authentication.AuthNodeTablePanel.AuthNodeDialog;
import org.emitdo.research.app.dbAdmin.view.asdb.authentication.AuthRowDialog.AuthNodeRowSelectionPanel;
import org.emitdo.research.app.dbAdmin.view.asdb.permissions.PermissionModifyDialog.PermissionModifyPanel;
import org.emitdo.research.app.dbAdmin.view.asdb.permissions.PermissionsPanel;
import org.emitdo.research.app.dbAdmin.view.asdb.permissions.PermissionsPanel.PermissionsDialog;
import org.emitdo.research.app.dbAdmin.view.asdb.remoteDomain.RemoteDomainsPanel.RemoteDomainsDialog;
import org.emitdo.research.app.dbAdmin.view.connection.AsConnectionPanel;
import org.emitdo.research.app.swing.AppliedCallback;
import org.emitdo.research.app.swing.FrameBase;

public final class AsManagerFrame extends ManagerFrame
{
    public static final String AsdbDialogTableLocations = "asdbTableDialogs";
    public static final String PermissionsDialog = "Permissions";
    public static final String RemoteDomainsDialog = "RemoteDomains";
    
    public final Hashtable<String, FramePreference> dialogPreferences;
    private final Hashtable<String, DialogBase> openDialogs;
    private final DomainType type;
    private final ConnectionsData connections;
    private final ConnectionsData clusterConnections;
//    private AuthNodeAction authNodeAction;
//    private GroupNodeAction groupNodeAction;
    private RdNodeAction rdNodeAction;
    private PermissionsAction permAction;
    
    public AsManagerFrame(String title, String frameKey, DomainType type, ConnectionsData connections, ConnectionsData clusterConnections, DbAdmin dbAdmin)
    {
        super(title, frameKey, dbAdmin);
        this.type = type;
        this.connections = connections;
        this.clusterConnections = clusterConnections;
        openDialogs = new Hashtable<String, DialogBase>();
        dialogPreferences = new Hashtable<String, FramePreference>();
        Preferences dialogNodes = dbAdmin.getUserRoot().node(AsdbDialogTableLocations);
//        dialogPreferences.put(JdbAuthenticationNodeJdbc.TableName, new FramePreference(dialogNodes, JdbAuthenticationNodeJdbc.TableName));  
//        dialogPreferences.put(JdbSecureGroupNodeJdbc.TableName, new FramePreference(dialogNodes, JdbSecureGroupNodeJdbc.TableName));  
//        dialogPreferences.put(JdbRemoteDomainNodeJdbc.TableName, new FramePreference(dialogNodes, JdbRemoteDomainNodeJdbc.TableName));  
        dialogPreferences.put(PermissionsDialog, new FramePreference(dialogNodes, PermissionsDialog));  
        dialogPreferences.put(RemoteDomainsDialog, new FramePreference(dialogNodes, RemoteDomainsDialog));  
        nextStartupPhase();
    }

    public ConnectionControl getConnectionControl()
    {
        return dbAdmin.getConnectionControl();
    }
    
    public StorageControl getDomainControl()
    {
        return dbAdmin.getStorageControl();
    }
    
    public String getDomain()
    {
        return getDomainControl().getDomainData(type).getDomain();
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
        JMenu manageMenu = new JMenu("Manage");
//        authNodeAction = new AuthNodeAction(manageMenu, toolBar);
//        groupNodeAction = new GroupNodeAction(manageMenu, toolBar);
        permAction = new PermissionsAction(manageMenu, toolBar);
        rdNodeAction = new RdNodeAction(manageMenu, toolBar);
//        new JMenuItem(authNodeAction);
//        new JMenuItem(groupNodeAction);
        new JMenuItem(permAction);
        new JMenuItem(rdNodeAction);
        menuBar.add(manageMenu);
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
        swapContentPane(new DomainPanel(type, connections, clusterConnections, this));
        super.initGui();
    }
    
    @Override
    public void close()
    {
//        storageControl.close();
//        connectionControl.close();
    }

    
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
    
    public void setEnabled(final AuthNodeRowSelectionPanel authNodePanel)
    {   
        final String oidStr = authNodePanel.getOid();
        final boolean enabled = authNodePanel.isNodeEnabled();
        final Connection connection = connections.currentlySelected.jdbcConnection;
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                String msg = "ok";
                boolean ok = true;
                Exception ex = null;
                PreparedStatement stmt = null;
                try
                {
                    msg = "Invalid Oid: " + oidStr;
                    Authentication oid = Authentication.create(oidStr);
                    msg = "sql update failed";
                    JdbStorageNodeJdbc.setEnabled(connection, oid, enabled);
                    msg = "ok";
                    connection.commit();
                }catch(Exception e)
                {
                    connection.rollback();
                    ex = e;
                    ok = false;
                }finally
                {
                    connection.setAutoCommit(true);
                    if(stmt != null)
                        stmt.close();
                    fireAppliedCallback(authNodePanel, ok, msg, ex, null);
                }
                return null;
            }
        };
        worker.execute();
    }

    public void getAuthenticationNodes(final DefaultListModel authIdlist, final DefaultListModel permList)
    {
        final EnterpriseDomainStorageManagement dataAccess = (EnterpriseDomainStorageManagement)connections.currentlySelected.dataAccessor;
        setLeftMessage("Getting Authentication nodes");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                ArrayList<Authentication> authIds = new ArrayList<Authentication>();
                ArrayList<ScopePermission> permissions = new ArrayList<ScopePermission>();
                boolean ok = true;
                try
                {
                    Iterator<Authentication> aiter = dataAccess.getAuthenticationNodeIDs().iterator();
                    while(aiter.hasNext())
                    {
                        Authentication oid = aiter.next(); 
                        authIds.add(oid);
                        AuthenticationNode node = dataAccess.getAuthenticationNode(oid);
                        Map<Integer,DOFPermissionSet> perms = node.getPermissions();
                        for(Entry<Integer, DOFPermissionSet> entry : perms.entrySet())
                            permissions.add(new ScopePermission(oid, entry.getKey(), entry.getValue()));
                    }
                }catch(Exception e)
                {
                    ok = false;
                    FrameBase.displayException(null, null, "Get Authentication nodes failed", "Failed to obtain the Authentication nodes", e);
                }
                if(ok)
                {
                    final ArrayList<Authentication> theAuthIds = authIds;
                    final ArrayList<ScopePermission> thePermissions = permissions;
                    Runnable runner = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // fill gui here
                            int size = theAuthIds.size();
                            for(int i=0; i < size; i++)
                                authIdlist.addElement(theAuthIds.get(i));
                            size = thePermissions.size();
                            for(int i=0; i < size; i++)
                                permList.addElement(thePermissions.get(i));
                        }
                    };
                    SwingUtilities.invokeLater(runner);
                }
                return null;
            }
        };
        worker.execute();
    }

    public void getRemoteDomainNodes(final DefaultListModel authIdlist, final DefaultListModel permList)
    {
        final EnterpriseDomainStorageManagement dataAccess = (EnterpriseDomainStorageManagement)connections.currentlySelected.dataAccessor;
        setLeftMessage("Getting Remote domain nodes");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                ArrayList<Authentication> authIds = new ArrayList<Authentication>();
                ArrayList<RemoteDomainInfo> rds = new ArrayList<RemoteDomainInfo>();
                boolean ok = true;
                try
                {
                    Iterator<Authentication> rditer = dataAccess.getRemoteDomainNodeIDs().iterator();
                    while(rditer.hasNext())
                    {
                        Authentication oid = rditer.next(); 
                        authIds.add(oid);
                        RemoteDomainNode node = dataAccess.getRemoteDomainNode(oid);
                        rds.add(new RemoteDomainInfo(oid, node.getRemoteDomainIdentifier(), node.getLocalID(), node.getDefaultLocalNode()));
                    }
                }catch(Exception e)
                {
                    ok = false;
                    FrameBase.displayException(null, null, "Get Authentication nodes failed", "Failed to obtain the Authentication nodes", e);
                }
                if(ok)
                {
                    final ArrayList<Authentication> theAuthIds = authIds;
                    final ArrayList<RemoteDomainInfo> theRds = rds;
                    Runnable runner = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // fill gui here
                            int size = theAuthIds.size();
                            for(int i=0; i < size; i++)
                                authIdlist.addElement(theAuthIds.get(i));
                            size = theRds.size();
                            for(int i=0; i < size; i++)
                                permList.addElement(theRds.get(i));
                        }
                    };
                    SwingUtilities.invokeLater(runner);
                }
                return null;
            }
        };
        worker.execute();
    }
    
    public void deleteIdentity(final PermissionsPanel panel)
    {
        final EnterpriseDomainStorageManagement dataAccess = (EnterpriseDomainStorageManagement)connections.currentlySelected.dataAccessor;
        setLeftMessage("Delete Authentication identity");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                String msg = "ok";
                boolean ok = true;
                Exception ex = null;
                
                Authentication identity = panel.getIdentity();
                
                try
                {
                    dataAccess.beginTransaction();
                    msg = "failed to delete Identity: " + identity;
                    if(dataAccess.exists(identity))
                        dataAccess.removeAuthenticationNode(identity);
                    msg = "ok";
                    dataAccess.commit();
                }catch(Exception e)
                {
                    ok = false;
                    ex = e;
                    dataAccess.rollback();
                }
                finally
                {
                    fireAppliedCallback(panel, ok, msg, ex, null);
                }
                return null;
            }
        };
        worker.execute();
    }

    public void setLocalNode(final AppliedCallback panel, final Authentication initiator, final Authentication localNode)
    {
        final EnterpriseDomainStorageManagement dataAccess = (EnterpriseDomainStorageManagement)connections.currentlySelected.dataAccessor;
        setLeftMessage("Set remote domains local node");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                String msg = "ok";
                boolean ok = true;
                Exception ex = null;
                
                try
                {
                    dataAccess.beginTransaction();
                    String dstr = getDomainControl().getDomainData(DomainType.Cluster).getDomain();
                    
                    msg = "invalid domain standard form: " + dstr;
                    Authentication domain = Authentication.create(dstr);
                    msg = "failed to set local node domain: " + domain + " initiator: " + initiator + " localNode: " + localNode;
                    dataAccess.setRemoteDomainLocalNode(domain, initiator, localNode);
                    msg = "ok";
                    dataAccess.commit();
                }catch(Exception e)
                {
                    ok = false;
                    ex = e;
                    dataAccess.rollback();
                }
                finally
                {
                    fireAppliedCallback(panel, ok, msg, ex, null);
                }
                return null;
            }
        };
        worker.execute();
    }

    public void deletePermissionSets(final PermissionsPanel panel, final List<ScopePermission> psets)
    {
        final EnterpriseDomainStorageManagement dataAccess = (EnterpriseDomainStorageManagement)connections.currentlySelected.dataAccessor;
        setLeftMessage("Revoking permissions");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                String msg = "ok";
                boolean ok = true;
                Exception ex = null;
                
                try
                {
                    dataAccess.beginTransaction();
                    msg = "failed to remove permission set";
                    int size = psets.size();
                    for(int i=0; i < size; i++)
                    {
                        ScopePermission sp = psets.get(i);
                        List<DOFPermission> perms = sp.pset.getPermissions();
                        int jsize = perms.size();
                        for(int j=0; j < jsize; j++)
                            dataAccess.revokePermission(sp.oid, perms.get(j), sp.scope);
                    }
                    msg = "ok";
                    dataAccess.commit();
                }catch(Exception e)
                {
                    ok = false;
                    ex = e;
                    dataAccess.rollback();
                }
                finally
                {
                    fireAppliedCallback(panel, ok, msg, ex, null);
                }
                return null;
            }
        };
        worker.execute();
    }

    public void modifyPermission(final PermissionModifyPanel panel)
    {
        final EnterpriseDomainStorageManagement dataAccess = (EnterpriseDomainStorageManagement)connections.currentlySelected.dataAccessor;
        setLeftMessage("Updating permissions");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                String msg = "ok";
                boolean ok = true;
                Exception ex = null;
                
                ScopePermission orgPset = panel.getOriginalPermissionSet();
                boolean newPset = orgPset.scope == null;
                Authentication identity = panel.getIdentity();
                int scope = panel.getScope();
                List<DOFPermission> permissions = panel.getPermissions();
                
                try
                {
                    dataAccess.beginTransaction();
                    if(!newPset)
                    {
                        msg = "failed to remove old permission set";
                        List<DOFPermission> perms = orgPset.pset.getPermissions();
                        int size = perms.size();
                        for(int i=0; i < size; i++)
                            dataAccess.revokePermission(identity, perms.get(i), orgPset.scope);
                    }
                    msg = "failed to add new permission set";
                    int size = permissions.size();
                    for(int i=0; i < size; i++)
                        dataAccess.grantPermission(identity, permissions.get(i), scope);
                    msg = "ok";
                    dataAccess.commit();
                }catch(Exception e)
                {
                    ok = false;
                    ex = e;
                    dataAccess.rollback();
                }
                finally
                {
                    fireAppliedCallback(panel, ok, msg, ex, null);
                }
                return null;
            }
        };
        worker.execute();
    }
    
    
    public void modifyIdentity(final AuthIdModifyPanel panel)
    {
        final EnterpriseDomainStorageManagement dataAccess = (EnterpriseDomainStorageManagement)connections.currentlySelected.dataAccessor;
        setLeftMessage("Modifying Authentication identity");
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>()
        {
            @SuppressWarnings("null")
            @Override
            protected Void doInBackground() throws Exception
            {
                String msg = "ok";
                boolean ok = true;
                Exception ex = null;
                
                String identity = panel.getIdentity();
                String password = panel.getPassword();
                String key = panel.getPresharedKey();
                
                try
                {
                    dataAccess.beginTransaction();
                    msg = "Invalid Identity: " + identity;
                    Authentication oid = Authentication.create(identity);
                    msg = "failed to add Identity: " + identity;
                    if(!dataAccess.exists(oid))
                        dataAccess.addAuthenticationNode(oid);
                    msg = "failed to create password credential";
                    boolean noPassword = password == null || password.length() == 0;
                    boolean noKey = key == null || key.length() == 0;
                    msg = "A credential must be given";
                    if(noPassword && noKey)
                        throw new Exception("a credential must be given");
                        
                    if(!noPassword)
                        dataAccess.setCredentials(oid, Password.create(oid, password));
                    else
                    {
                        try{dataAccess.removeCredentials(oid, DOFCredentials.PASSWORD);}catch(CredentialsNotFoundException e){}
                    }
                    if(!noKey)
                    {
                        msg = "invalid preshared key: " + key;
                        if(key.length() != 64 || !DOFUtil.isValidHexString(key))
                            throw new Exception("invalid preshared key: " + key);
                        msg = "failed to create key credential";
                        dataAccess.setCredentials(oid, Key.create(oid, DOFUtil.hexStringToBytes(key)));
                    }
                    else
                    {
                        try{dataAccess.removeCredentials(oid, DOFCredentials.KEY);}catch(CredentialsNotFoundException e){}
                    }
                    msg = "ok";
                    dataAccess.commit();
                }catch(Exception e)
                {
                    ok = false;
                    ex = e;
                    dataAccess.rollback();
                }
                finally
                {
                    fireAppliedCallback(panel, ok, msg, ex, null);
                }
                return null;
            }
        };
        worker.execute();
    }
    

    public void readTable(final AsdbTable table, final AbstractTableModel tableModel)
    {
        final Connection connection = connections.currentlySelected.jdbcConnection;
        setLeftMessage("Reading " + table.name() + " table");
        String tableName = null;
        switch(table)
        {
            case AuthNode:
                tableName = JdbAuthenticationNodeJdbc.TableName;
                break;
            case GroupNode:
                tableName = JdbSecureGroupNodeJdbc.TableName;
                break;
            case RdNode:
                tableName = JdbRemoteDomainNodeJdbc.TableName;
                break;
        }
        final String theTableName = tableName;
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
                    stmt = connection.createStatement();
                    rs = stmt.executeQuery(sql);
                }catch(Exception e)
                {
                    if(stmt != null)
                        try{stmt.close();} catch (SQLException e1){/*best try*/}
                    ok = false;
                    FrameBase.displayException(null, null, "Read failed", "Failed to read the res_device table", e);
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
                                {
                                    switch(table)
                                    {
                                        case AuthNode:
                                            ((AuthNodeTableModel)tableModel).addRow(connection, theRs);
                                            break;
                                        case GroupNode:
                                            ((GroupNodeTableModel)tableModel).addRow(connection, theRs);
                                            break;
                                        case RdNode:
                                            ((RdNodeTableModel)tableModel).addRow(connection, theRs);
                                            break;
                                    }
                                }
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
    
    public enum AsdbTable {AuthNode, GroupNode, RdNode,};

    
//    public void updateUpdate(final UpdateRowSelectionPanel updatePanel, final UpdateRow previousData, final AppliedCallback acb)
//    {   
//    }
    
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
    
    // Mnemonics
    public static int AuthNodeActionMnemonic = KeyEvent.VK_A;
    public static int GroupNodeActionMnemonic = KeyEvent.VK_G;
    public static int RdNodeActionMnemonic = KeyEvent.VK_R;
    public static int PermissionsActionMnemonic = KeyEvent.VK_P;
    
    // Accelerators
    public static int AuthNodeActionAccelerator = KeyEvent.VK_A;
    public static int GroupNodeActionAccelerator = KeyEvent.VK_G;
    public static int RdNodeActionAccelerator = KeyEvent.VK_R;
    public static int PermissionsActionAccelerator = KeyEvent.VK_P;
    
    
    final class AuthNodeAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Authentication nodes...";
        public static final String MouseOver = "Authentication node management";
        public final int Mnemonic = AuthNodeActionMnemonic;
        public final int Accelerator = AuthNodeActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public AuthNodeAction(JMenu menu, JToolBar toolBar)
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
            DialogBase dialog = openDialogs.get(OraDeviceJdbc.TableName);
            if(dialog != null)
            {
                dialog.requestFocus();
                return;
            }
            dialog = new AuthNodeDialog(AsManagerFrame.this, AsManagerFrame.this, null);
            openDialogs.put(OraDeviceJdbc.TableName, dialog);
        }
    }
    final class GroupNodeAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Group nodes...";
        public static final String MouseOver = "Secure Group node management";
        public final int Mnemonic = GroupNodeActionMnemonic;
        public final int Accelerator = GroupNodeActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public GroupNodeAction(JMenu menu, JToolBar toolBar)
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
            dbAdmin.swapContentPane(new AsConnectionPanel(dbAdmin));
        }
    }

    final class RdNodeAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Remote domain nodes...";
        public static final String MouseOver = "Remote domain node management";
        public final int Mnemonic = RdNodeActionMnemonic;
        public final int Accelerator = RdNodeActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public RdNodeAction(JMenu menu, JToolBar toolBar)
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
            DialogBase dialog = openDialogs.get(RemoteDomainsDialog);
            if(dialog != null)
            {
                dialog.requestFocus();
                return;
            }
            dialog = new RemoteDomainsDialog(AsManagerFrame.this, AsManagerFrame.this);
            openDialogs.put(RemoteDomainsDialog, dialog);
        }
    }

    final class PermissionsAction extends AbstractAction
    {
        public final String Image = "exit.gif";
        public static final String Label = "Permissions...";
        public static final String MouseOver = "Assign Permissions";
        public final int Mnemonic = PermissionsActionMnemonic;
        public final int Accelerator = PermissionsActionAccelerator;
        public static final int AcceleratorMask = ActionEvent.ALT_MASK;
        
        public PermissionsAction(JMenu menu, JToolBar toolBar)
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
            DialogBase dialog = openDialogs.get(PermissionsDialog);
            if(dialog != null)
            {
                dialog.requestFocus();
                return;
            }
            dialog = new PermissionsDialog(AsManagerFrame.this, AsManagerFrame.this);
            openDialogs.put(PermissionsDialog, dialog);
        }
    }
}
