/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.res;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;

import org.emitdo.research.app.dbAdmin.ResManagerFrame;
import org.emitdo.research.app.dbAdmin.model.CannedResData;

public class ResPanel extends JPanel
{
    public static final int TableWidth = 960;
    public static final int TableHeight = 480;
    
    private final JTabbedPane tabs;
    private final InitializeDeviceTablePanel devicePanel;
    private final InitializeManagerTablePanel managerPanel;
    private final InitializeVersionTablePanel versionPanel;
    private final AllDomainsConnectionPanel domainConnectionsPanel; 

    public ResPanel(CannedResData data, ResManagerFrame resManager)
    {
        setLayout(new BorderLayout());
        InitializeDataPanel dataPanel = new InitializeDataPanel(data, this, resManager);
        domainConnectionsPanel = new AllDomainsConnectionPanel(data, dataPanel, resManager); 
        dataPanel.setDomainConnectionsPanel(domainConnectionsPanel);
        resManager.addWindowFocusListener(domainConnectionsPanel);
        devicePanel = new InitializeDeviceTablePanel(data);
        managerPanel = new InitializeManagerTablePanel(data);
        versionPanel = new InitializeVersionTablePanel(data);

        tabs = new JTabbedPane();
        tabs.addTab("Initialize", dataPanel);
        tabs.addTab("AS select", domainConnectionsPanel);
        tabs.addTab("Device", devicePanel);
        tabs.addTab("Managers", managerPanel);
        tabs.addTab("Versions", versionPanel);
        tabs.addTab("Misc", new InitializeMiscPanel(data));
        
        JScrollPane scrollPane = new JScrollPane(tabs);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        add(scrollPane, BorderLayout.CENTER);
        dataPanel.refresh();
    }
    
    public AllDomainsConnectionPanel getDomainConnectionsPanel()
    {
        return domainConnectionsPanel;
    }

    public void addRowToDevice(Object[] row)
    {
        devicePanel.addRow(row);
    }
    
    public void addRowToManager(Object[] row)
    {
        managerPanel.addRow(row);
    }
    
    public void addRowToVersion(Object[] row)
    {
        versionPanel.addRow(row);
    }
    
    public void clear()
    {
        devicePanel.clear();
        managerPanel.clear();
        versionPanel.clear();
    }

}
