/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.asdb.authentication;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.emitdo.app.res.db.oracle.OraDeviceJdbc;
import org.emitdo.as.jdb.domain.JdbAuthenticationNodeJdbc;
import org.emitdo.research.app.dbAdmin.AsManagerFrame;
import org.emitdo.research.app.dbAdmin.AsManagerFrame.AsdbTable;
import org.emitdo.research.app.dbAdmin.model.asdb.AuthNodeTableModel;
import org.emitdo.research.app.dbAdmin.model.asdb.AuthNodeTableModel.AuthNodeRow;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.FrameBase.DialogBase;

public class AuthNodeTablePanel extends JPanel implements ActionListener, ListSelectionListener, MouseListener
{
    private final AsManagerFrame asManager;
    private final AuthNodeDialog dialog;
    private final JTextField device;
    
    private final JTable table;
    private AuthNodeTableModel tableModel;
    private final ListSelectionModel listModel;
    private final JButton addButton;
    private final JButton deleteButton;
    private final JButton managersButton;

    
    public AuthNodeTablePanel(AsManagerFrame asManager)
    {
        this(asManager, null, null);
    }
    
    public AuthNodeTablePanel(AsManagerFrame asManager, AuthNodeDialog dialog, JTextField device)
    {
        this.asManager = asManager;
        this.dialog = dialog;
        this.device = device;
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder(OraDeviceJdbc.TableName));
        
        tableModel = new AuthNodeTableModel();
        table = new JTable(tableModel);
        listModel = table.getSelectionModel();
        listModel.addListSelectionListener(this);
        table.setRowSelectionAllowed(true);
        if(device != null)
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        else
            table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.addMouseListener(this);
        tableModel.setTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1024, 300));
        table.setFillsViewportHeight(true);
        
        panel.add(scrollPane);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        if(device == null)
        {
            addButton = FrameBase.addButtonToPanel("Add", true, "Add a new device", buttonPanel, this);
            deleteButton = FrameBase.addButtonToPanel("Delete", false, "Delete the selected devices", buttonPanel, this);
        }else
        {
            addButton = null;
            deleteButton = null;
        }
        managersButton = FrameBase.addButtonToPanel("Managers", false, "Show all managers associated with the selected device", buttonPanel, this);
        add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel, BorderLayout.CENTER);
        
        Runnable runner = new Runnable(){@Override public void run(){table.requestFocus();}};
        SwingUtilities.invokeLater(runner);
        asManager.readTable(AsdbTable.AuthNode, tableModel);
    }
    
    public void refresh()
    {
        tableModel = new AuthNodeTableModel();
        table.setModel(tableModel);
        managersButton.setEnabled(false);
        asManager.readTable(AsdbTable.AuthNode, tableModel);
    }
    
    public AuthNodeTableModel getTableModel()
    {
        return tableModel;
    }
    
    public AsManagerFrame getAsManager()
    {
        return asManager;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if(src == managersButton)
        {
            int index = listModel.getLeadSelectionIndex();
            AuthNodeRow deviceRow = new AuthNodeRow(tableModel, index);
//            new ManagerDeviceListDialog(asManager, table, null, deviceRow.device.toString(), true);
            return;
        }
        if(src == addButton)
        {
            int index = listModel.getAnchorSelectionIndex();
            new AuthRowDialog(asManager, addButton, this, index, true);
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
            int index = listModel.getAnchorSelectionIndex();
            if(device != null)
            {
                AuthNodeRow authNodeRow = new AuthNodeRow(tableModel, index);
                device.setText(authNodeRow.oid.toString());
                dialog.dispose();
            }else
                new AuthRowDialog(asManager, event.getComponent(), this, index, false);
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
        boolean onlyOne = listModel.getMaxSelectionIndex() - listModel.getMinSelectionIndex() == 0;
        if(!onlyOne)
            managersButton.setEnabled(false);
        else
            managersButton.setEnabled(true);
        
        // going to copy the selected anchor on insert
        if(listModel.isSelectionEmpty())
            addButton.setEnabled(false);
        else
            addButton.setEnabled(true);
    }
    
    public static class AuthNodeDialog extends DialogBase
    {
        private final AsManagerFrame asManager;
        public AuthNodeDialog(AsManagerFrame asManager, Component source, JTextField device)
        {
            super(
                    asManager, 
                    asManager.getFramePreference(JdbAuthenticationNodeJdbc.TableName),
                    JdbAuthenticationNodeJdbc.TableName,
                    device != null);
            this.asManager = asManager;
            setContentPane(new AuthNodeTablePanel(asManager, this, device));
            pack();
            setVisible(true);
            setToFramePreferences(245, 160, source, OraDeviceJdbc.TableName);
        }
        
        @Override
        public void refresh()
        {
            ((AuthNodeTablePanel)getContentPane()).refresh();
        }
        
        @Override 
        public void windowClosed(WindowEvent e)
        {
            super.windowClosed(e);
            asManager.dialogClosed(OraDeviceJdbc.TableName, this);
        }
    }
}
