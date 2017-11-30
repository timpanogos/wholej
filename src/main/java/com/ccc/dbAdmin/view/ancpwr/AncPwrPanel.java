/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.ancpwr;

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

import org.emitdo.research.app.dbAdmin.AncPwrManagerFrame;
import org.emitdo.research.app.dbAdmin.ConnectionControl;
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.model.ConnectionsData;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel.ConnectedEvent;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class AncPwrPanel extends JPanel implements  ActionListener, WindowFocusListener, SwappedFocusListener
{
    private final AncPwrManagerFrame ancPwrManager;
    
    private JButton initializeButton;
    private JButton manageButton;
    
    private final AvailableConnectionsPanel connectionsPanel;
    private final ConnectionsData connections;
    
    public final AtomicBoolean connected;
    public String acknowledge;

    public AncPwrPanel(AncPwrManagerFrame ancPwrManager)
    {
        this.ancPwrManager = ancPwrManager;
        connected = new AtomicBoolean(false);
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder("Ancillary Power"));

        ancPwrManager.addWindowFocusListener(this);

        connections = ConnectionsData.getAvailableConnections(ancPwrManager.getConnectionControl(), StorageType.Ancillary, null);
        connectionsPanel = new AvailableConnectionsPanel(
                connections, "Supported Ancillary Power connections", 
                true, true, false, true, null, this);
        mainPanel.add(connectionsPanel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        manageButton = FrameBase.addButtonToPanel("Manage", false, "Manage the selected RES Storage", buttonPanel, this);
        initializeButton = FrameBase.addButtonToPanel("Initialize", false, "First time initialization of selected Ancillary Database", buttonPanel, this);
        mainPanel.add(buttonPanel);
        
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refresh()
    {
        connectionsPanel.refresh();
//        listModel.clear();
//        try
//        {
//            Domain platform = data.platformData.getDomainOid();
//            
//            for(int i=0; i < data.getDeviceCount(); i++)
//                listModel.addElement(OracleCannedResInitializer.getDeviceForIndex(i, platform).oid);
//            
//            int nextGatewayDeviceIndex = 0;
//            for(int i=0; i < data.getGatewayCount(); i++)
//            {
//                listModel.addElement(OracleCannedResInitializer.getGatewayForIndex(i, platform).oid);
//                for(int j=0; j < data.getGatewayDeviceCount(); j++)
//                    listModel.addElement(OracleCannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex++, platform).oid);
//            }
//            
//        }catch(Exception e)
//        {
//            FrameBase.displayException(null, buManager, "bad news", "failed to generate example list", e);
//        }
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
                if(connected.get())
                    ancPwrManager.setCurrentlySelectedConnection(connections.currentlySelected);
                return;
            }
            if(command.equals(ConnectionControl.ConnectButtonLabel))
            {
                ancPwrManager.getConnectionControl().
                    connect(connections.currentlySelected, null, true, this, null);
//                data.connData = connections.currentlySelected;
               return;
            }
            if(command.equals(ConnectionControl.DisconnectButtonLabel))
            {
                ancPwrManager.getConnectionControl().disconnect(connections.currentlySelected, this);
                ancPwrManager.setCurrentlySelectedConnection(null);
               return;
            }
            return;
        }
        
        if(src == initializeButton)
        {
            int option = JOptionPane.showConfirmDialog(
                    ancPwrManager, 
                    "WARNING:\nYou are about to initialize the Ancillary Power Storage domain that is currently selected.\n" +
                    "Auto create is selected for this Ancillary Power Storage.\nIf the currently selected storage exits it will be deleted and rebuilt.\nIf it does not exist it will be created." +
                    "\n\nAre you sure you want to continue?",
                    "Initialize Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(option != JOptionPane.OK_OPTION)
                return;
            acknowledge = "Ancillary Power storage initialization completed";
            
            Runnable runner = new Runnable(){@Override public void run(){initializeButton.setEnabled(false);}};
            SwingUtilities.invokeLater(runner);
            
            ancPwrManager.getConnectionControl().initializeAncPwr(connections.currentlySelected, true, this);
            return;
        }
    }

    @Override
    public void focusRequested(Object context)
    {
        if(context instanceof Throwable)
        {
            acknowledge = null;
            FrameBase.displayException(null, ancPwrManager, "Worker thread exception", "Failed to connect and/or initialize", (Throwable)context);
        }
        windowGainedFocus(null);
        if(acknowledge != null)
            JOptionPane.showMessageDialog(ancPwrManager, acknowledge);
        acknowledge = null;
    }

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        refresh();
    }

    @Override public void windowLostFocus(WindowEvent e){}
}
