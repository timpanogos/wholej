/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.emitdo.research.app.dbAdmin.ConnectionControl;
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.NamingManagerFrame;
import org.emitdo.research.app.dbAdmin.model.ConnectionsData;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel.ConnectedEvent;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class NamingPanel extends JPanel implements  ActionListener, WindowFocusListener, SwappedFocusListener
{
    private final NamingManagerFrame namingManager;
    
    private JButton initializeButton;
    private JButton manageButton;
    
    private final AvailableConnectionsPanel connectionsPanel;
    private final ConnectionsData connections;
    
    public final AtomicBoolean connected;
    public String acknowledge;

    public NamingPanel(NamingManagerFrame namingManager)
    {
        this.namingManager = namingManager;
        connected = new AtomicBoolean(false);
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder("Naming"));

        namingManager.addWindowFocusListener(this);

        connections = ConnectionsData.getAvailableConnections(namingManager.getConnectionControl(), StorageType.Naming, null);
        connectionsPanel = new AvailableConnectionsPanel(
                connections, "Supported Naming connections", 
                true, false, true, false, null, this);
        mainPanel.add(connectionsPanel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        manageButton = FrameBase.addButtonToPanel("Manage", false, "Manage the selected RES Storage", buttonPanel, this);
        initializeButton = FrameBase.addButtonToPanel("Initialize", false, "First time initialization of selected RES", buttonPanel, this);
        mainPanel.add(buttonPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        add(scrollPane, BorderLayout.CENTER);
        refresh();
    }

    public void refresh()
    {
        connectionsPanel.refresh();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        if(src == connectionsPanel)
        {
            String command = e.getActionCommand(); 
            if(command.equals(ConnectionControl.ConnectedActionCommand))
            {
                connected.set(e.getID() == ConnectedEvent.Connected ? true : false);
                initializeButton.setEnabled(connected.get());
                manageButton.setEnabled(connected.get());
                return;
            }
            if(command.equals(ConnectionControl.ConnectButtonLabel))
            {
                //TODO: add requireAuthCheckbox
                namingManager.getConnectionControl().
                    connect(connections.currentlySelected, null, false, this, null);
//                data.connData = connections.currentlySelected;
               return;
            }
            if(command.equals(ConnectionControl.DisconnectButtonLabel))
            {
                namingManager.getConnectionControl().
                    disconnect(connections.currentlySelected, this);
               return;
            }
            return;
        }
        
        if(src == initializeButton)
        {
            int option = JOptionPane.showConfirmDialog(
                    namingManager, 
                    "WARNING:\nYou are about to initialize the Naming Storage domain that is currently selected.\n" +
                    "Auto create is selected for this Naming Storage.\nIf the currently selected storage exits it will be deleted and rebuilt.\nIf it does not exist it will be created." +
                    "\n\nAre you sure you want to continue?",
                    "Initialize Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(option != JOptionPane.OK_OPTION)
                return;
            acknowledge = "Naming storage initialization completed";
            
            Runnable runner = new Runnable(){@Override public void run(){initializeButton.setEnabled(false);}};
            SwingUtilities.invokeLater(runner);
            
            namingManager.getConnectionControl().initializeNaming(connections.currentlySelected, false, this);
            return;
        }
    }

    @Override
    public void focusRequested(Object context)
    {
        if(context instanceof Throwable)
        {
            acknowledge = null;
            FrameBase.displayException(null, namingManager, "Worker thread exception", "Failed to connect and/or initialize", (Throwable)context);
        }
        windowGainedFocus(null);
        if(acknowledge != null)
            JOptionPane.showMessageDialog(namingManager, acknowledge);
        acknowledge = null;
    }

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        refresh();
    }

    @Override public void windowLostFocus(WindowEvent e){}
}
