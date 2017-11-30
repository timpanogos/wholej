/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.asdb.permissions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.emitdo.app.res.db.oracle.OraDeviceJdbc;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.research.app.dbAdmin.AsManagerFrame;
import org.emitdo.research.app.dbAdmin.model.asdb.ScopePermission;
import org.emitdo.research.app.dbAdmin.view.asdb.authentication.AuthIdModifyDialog;
import org.emitdo.research.app.swing.AppliedCallback;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.FrameBase.DialogBase;

public class PermissionsPanel extends JPanel implements ActionListener, ListSelectionListener, MouseListener, AppliedCallback
{
    private final AsManagerFrame asManager;
    
    private final JList oidList;
    private final DefaultListModel oidListModel;
    private final ListSelectionModel oidSelectionModel;
    
    private final JList permList;
    private final DefaultListModel permListModel;
    private final ListSelectionModel permSelectionModel;
//    private final JTable table;
//    private AuthNodeTableModel tableModel;
    private final JButton addOidButton;
    private final JButton editOidButton;
    private final JButton deleteOidButton;
    private final JButton addPermButton;
    private final JButton editPermButton;
    private final JButton deletePermButton;
    private final JButton localNodeButton;
    
    public PermissionsPanel(AsManagerFrame asManager)
    {
        this.asManager = asManager;
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//        mainPanel.setBorder(new TitledBorder("Authentication Nodes"));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Authentication Nodes"));

        oidListModel = new DefaultListModel();
        oidList = new JList(oidListModel);
        oidList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        oidList.setVisibleRowCount(-1);
        oidSelectionModel = oidList.getSelectionModel();
        oidList.addListSelectionListener(this);

        JScrollPane scrollPane = new JScrollPane(oidList);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1024, 300));
        panel.add(scrollPane);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        addOidButton = FrameBase.addButtonToPanel("Add", true, "Add a Authentication OID", buttonPanel, this);
        editOidButton = FrameBase.addButtonToPanel("Edit", false, "Modify the credentials on the selected identity", buttonPanel, this);
        deleteOidButton = FrameBase.addButtonToPanel("Delete", false, "Delete the selected Authentication identity", buttonPanel, this);
        localNodeButton = FrameBase.addButtonToPanel("LocalNode", false, "Set the remote domains default local node to the selected Identity", buttonPanel, this);
        panel.add(buttonPanel);
        mainPanel.add(panel);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Permissions"));

        permListModel = new DefaultListModel();
        permList = new JList(permListModel);
        permList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        permList.setVisibleRowCount(-1);
        permList.addListSelectionListener(this);
        permSelectionModel = permList.getSelectionModel();

        scrollPane = new JScrollPane(permList);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1024, 300));
        panel.add(scrollPane);

        buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        addPermButton = FrameBase.addButtonToPanel("Add", false, "Add a new permission", buttonPanel, this);
        editPermButton = FrameBase.addButtonToPanel("Edit", false, "Edit a selected permission", buttonPanel, this);
        deletePermButton = FrameBase.addButtonToPanel("Delete", false, "Delete the selected permissions", buttonPanel, this);
        panel.add(buttonPanel);
        mainPanel.add(panel);
        add(mainPanel, BorderLayout.CENTER);
        
        Runnable runner = new Runnable(){@Override public void run(){oidList.requestFocus();}};
        SwingUtilities.invokeLater(runner);
        asManager.getAuthenticationNodes(oidListModel, permListModel);
    }
    
    public void refresh()
    {
        oidListModel.clear();
        permListModel.clear();
        asManager.getAuthenticationNodes(oidListModel, permListModel);
    }
    
    public Authentication getIdentity()
    {
        return (Authentication) oidListModel.get(oidSelectionModel.getLeadSelectionIndex());
    }
    
    @Override
    public void complete(boolean ok, String message, Exception e, Object context)
    {
        if(ok)
            asManager.refreshDialog(AsManagerFrame.PermissionsDialog);
    }


    public DefaultListModel getOidListModel()
    {
        return oidListModel;
    }
    
    public DefaultListModel getPermListModel()
    {
        return permListModel;
    }
    
    public AsManagerFrame getAsManager()
    {
        return asManager;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if(src == addOidButton)
        {
            new AuthIdModifyDialog(asManager, addOidButton, null);
            return;
        }
        if(src == editOidButton)
        {
            new AuthIdModifyDialog(asManager, addOidButton, (Authentication)oidListModel.get(oidSelectionModel.getLeadSelectionIndex()));
            return;
        }
        if(src == deleteOidButton)
        {
            asManager.deleteIdentity(this);
            return;
        }
        if(src == addPermButton)
        {
            ScopePermission sp = new ScopePermission((Authentication)oidListModel.get(oidSelectionModel.getLeadSelectionIndex()), null, null);
            new PermissionModifyDialog(asManager, addPermButton, sp);
            return;
        }
        if(src == editPermButton)
        {
            new PermissionModifyDialog(asManager, addPermButton, (ScopePermission)permListModel.get(permSelectionModel.getLeadSelectionIndex()));
            return;
        }
        if(src == deletePermButton)
        {
            ArrayList<ScopePermission> sps = new ArrayList<ScopePermission>();
            int[] indices = permList.getSelectedIndices();
            for(int i=0; i < indices.length; i++)
                sps.add((ScopePermission)permListModel.get(indices[i]));
            asManager.deletePermissionSets(this, sps);            
            return;
        }
        if(src == localNodeButton)
        {
            asManager.setLocalNode(this, null, (Authentication)oidListModel.get(oidSelectionModel.getLeadSelectionIndex()));
            return;
        }
    }

    /* ************************************************************************
     * MouseListener implementation
     **************************************************************************/
    @Override
    public void mouseClicked(MouseEvent event)
    {
        if(event.getClickCount() == 2)
        {
//            int index = listModel.getAnchorSelectionIndex();
//            if(device != null)
//            {
//                AuthNodeRow authNodeRow = new AuthNodeRow(tableModel, index);
//                device.setText(authNodeRow.oid.toString());
//                dialog.dispose();
//            }else
//                new AuthenticationDialog(asManager, event.getComponent(), this, index, false);
        }
    }

    @Override public void mousePressed(MouseEvent e){}
    @Override public void mouseReleased(MouseEvent e){}
    @Override public void mouseEntered(MouseEvent e){}
    @Override public void mouseExited(MouseEvent e){}

    /* ************************************************************************
     * ListSelectionListener implementation
     **************************************************************************/
    @Override
    public void valueChanged(ListSelectionEvent event)
    {
        if (event.getValueIsAdjusting()) 
            return;
        Object src = event.getSource();
        if(src == oidList)
        {
            if(oidSelectionModel.isSelectionEmpty())
            {
                permSelectionModel.clearSelection();
                localNodeButton.setEnabled(false);
                deleteOidButton.setEnabled(false);
                editOidButton.setEnabled(false);
                addPermButton.setEnabled(false);
                return;
            }
            localNodeButton.setEnabled(true);
            deleteOidButton.setEnabled(true);
            editOidButton.setEnabled(true);
            addPermButton.setEnabled(true);
            Authentication oid  = (Authentication) oidListModel.get(oidSelectionModel.getMinSelectionIndex());
            int size = permListModel.size();
            permSelectionModel.setValueIsAdjusting(true);
            permSelectionModel.clearSelection();
            for(int i=0; i < size; i++)
            {
                if(oid.equals(((ScopePermission)permListModel.get(i)).oid))
                    permSelectionModel.addSelectionInterval(i, i);
            }
            permSelectionModel.setValueIsAdjusting(false);
            return;
        }
        if(src == permList)
        {
            if(permSelectionModel.isSelectionEmpty())
            {
                deletePermButton.setEnabled(false);
                editPermButton.setEnabled(false);
                return;
            }
            deletePermButton.setEnabled(true);
            editPermButton.setEnabled(true);
            Object[] set = permList.getSelectedValues();
            if(set.length == 1)
            {
                int size = oidListModel.size();
                for(int i=0; i < size; i++)
                {
                    if(((ScopePermission)set[0]).oid.equals(oidListModel.get(i)))
                    {
                        oidList.setSelectedIndex(i);
                        break;
                    }
                }
            }
            return;
        }
    }
    
    public static class PermissionsDialog extends DialogBase
    {
        private final AsManagerFrame asManager;
        public PermissionsDialog(AsManagerFrame asManager, Component source)
        {
            super(
                    asManager, 
//                    asManager.getFramePreference(JdbAuthenticationNodeJdbc.TableName),
                    asManager.getFramePreference(AsManagerFrame.PermissionsDialog),
                    "Authentication nodes: " + asManager.getDomain(), false);
            this.asManager = asManager;
            setContentPane(new PermissionsPanel(asManager));
            pack();
            setVisible(true);
            setToFramePreferences(245, 160, source, OraDeviceJdbc.TableName);
        }
        
        @Override
        public void refresh()
        {
            ((PermissionsPanel)getContentPane()).refresh();
        }
        
        @Override 
        public void windowClosed(WindowEvent e)
        {
            super.windowClosed(e);
            asManager.dialogClosed(AsManagerFrame.PermissionsDialog, this);
        }
    }
}
