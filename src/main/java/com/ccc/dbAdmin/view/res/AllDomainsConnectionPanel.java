/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.res;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.emitdo.app.as.common.EnterpriseDomainStorageManagement;
import org.emitdo.research.app.dbAdmin.ConnectionControl;
import org.emitdo.research.app.dbAdmin.ResManagerFrame;
import org.emitdo.research.app.dbAdmin.model.CannedResData;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel.ConnectedEvent;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class AllDomainsConnectionPanel extends JPanel implements ActionListener, SwappedFocusListener, WindowFocusListener
{
    private final CannedResData data;
    private final ResManagerFrame resManager;
    
    private final AvailableConnectionsPanel clusterConnectionsPanel;
    private final AvailableConnectionsPanel platformConnectionsPanel;
    private final AvailableConnectionsPanel factoryConnectionsPanel;
    private final AtomicBoolean clusterConnected;
    private final AtomicBoolean platformConnected;
    private final AtomicBoolean factoryConnected;
    
    private final ActionListener callersActionListener;
    
    public AllDomainsConnectionPanel(CannedResData data, ActionListener actionListener, ResManagerFrame resManager)
    {
        this.data = data;
        this.resManager = resManager;
        this.callersActionListener = actionListener;
        clusterConnected = new AtomicBoolean(false);
        platformConnected = new AtomicBoolean(false);
        factoryConnected = new AtomicBoolean(false);
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Supported TTC domains"));
        
        clusterConnectionsPanel = new AvailableConnectionsPanel(
                data.clusterConnections, "Supported Cluster domain connections", 
                true, true, true, true, data.clusterData, this);
        panel.add(clusterConnectionsPanel);
        
        platformConnectionsPanel = new AvailableConnectionsPanel(
                data.platformConnections, "Supported Platform domain connections", 
                true, true, true, true, data.platformData, this);
        panel.add(platformConnectionsPanel);
        
        factoryConnectionsPanel = new AvailableConnectionsPanel(
                data.factoryConnections, "Supported Factory domain connections", 
                true, true, true, true, data.factoryData, this);
        panel.add(factoryConnectionsPanel);
        
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        add(scrollPane);
    }

    public void refresh()
    {
        clusterConnectionsPanel.refresh();
        platformConnectionsPanel.refresh();
        factoryConnectionsPanel.refresh();
        data.clusterDa = (EnterpriseDomainStorageManagement)data.clusterConnections.currentlySelected.dataAccessor;
        data.platformDa = (EnterpriseDomainStorageManagement)data.platformConnections.currentlySelected.dataAccessor;
        data.factoryDa = (EnterpriseDomainStorageManagement)data.factoryConnections.currentlySelected.dataAccessor;
    }

    public boolean starsAligned()
    {
        return 
            clusterConnected.get() &&
            platformConnected.get() &&
            factoryConnected.get();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        String command = e.getActionCommand();
        Boolean connected = null;
        if(command.equals(ConnectionControl.ConnectedActionCommand))
            connected = e.getID() == 0 ? false : true;
        boolean connectButton = command.equals(ConnectionControl.ConnectButtonLabel);
        boolean disconnectButton = command.equals(ConnectionControl.DisconnectButtonLabel);
        
        if(src == clusterConnectionsPanel)
        {
            if(connected != null)
            {
                clusterConnected.set(connected);
                int id = ConnectedEvent.NotConnected;
                if(starsAligned())
                    id = ConnectedEvent.Connected;
                callersActionListener.actionPerformed(new ConnectedEvent(this, id, ConnectionControl.ConnectedActionCommand));
                return;
            }
            if(connectButton)
            {
                resManager.getConnectionControl().
                    connect(data.clusterConnections.currentlySelected, data.clusterData.getDomain(), true, this, clusterConnectionsPanel);
               return;
            }
            if(disconnectButton)
            {
                resManager.getConnectionControl().
                    disconnect(data.clusterConnections.currentlySelected, this);
               return;
            }
        }
        if(src == platformConnectionsPanel)
        {
            if(connected != null)
            {
                platformConnected.set(connected);
                int id = ConnectedEvent.NotConnected;
                if(starsAligned())
                    id = ConnectedEvent.Connected;
                callersActionListener.actionPerformed(new ConnectedEvent(this, id, ConnectionControl.ConnectedActionCommand));
                return;
            }
            if(connectButton)
            {
                resManager.getConnectionControl().
                    connect(data.platformConnections.currentlySelected, data.platformData.getDomain(), true, this, platformConnectionsPanel);
               return;
            }
            if(disconnectButton)
            {
                resManager.getConnectionControl().
                    disconnect(data.platformConnections.currentlySelected, this);
               return;
            }
        }
        if(src == factoryConnectionsPanel)
        {
            if(connected != null)
            {
                factoryConnected.set(connected);
                int id = ConnectedEvent.NotConnected;
                if(starsAligned())
                    id = ConnectedEvent.Connected;
                callersActionListener.actionPerformed(new ConnectedEvent(this, id, ConnectionControl.ConnectedActionCommand));
                return;
            }
            if(connectButton)
            {
                resManager.getConnectionControl().
                    connect(data.factoryConnections.currentlySelected, data.factoryData.getDomain(), true, this, factoryConnectionsPanel);
               return;
            }
            if(disconnectButton)
            {
                resManager.getConnectionControl().
                    disconnect(data.factoryConnections.currentlySelected, this);
               return;
            }
        }
    }

    @Override
    public void focusRequested(Object context)
    {
        if(context instanceof Throwable)
            FrameBase.displayException(null, resManager, "Worker thread exception", "Failed to connect", (Throwable)context);
//        else
//        {
//            if(context == clusterConnectionsPanel)
//                data.clusterDa = (ASDomainBuilder2)data.clusterConnections.currentlySelected.dataAccessor;
//            else if(context == platformConnectionsPanel)
//                data.platformDa = (ASDomainBuilder2)data.platformConnections.currentlySelected.dataAccessor;
//            else if(context == factoryConnectionsPanel)
//                data.factoryDa = (ASDomainBuilder2)data.factoryConnections.currentlySelected.dataAccessor;
//        }
        clusterConnectionsPanel.refresh();
        platformConnectionsPanel.refresh();
        factoryConnectionsPanel.refresh();
    }
    
    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        refresh();
    }
    @Override public void windowLostFocus(WindowEvent e){}
}
