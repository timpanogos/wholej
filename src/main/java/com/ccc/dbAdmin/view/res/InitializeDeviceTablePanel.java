/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.res;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.emitdo.research.app.dbAdmin.model.CannedResData;
import org.emitdo.research.app.dbAdmin.model.ResDeviceTableModel;

public class InitializeDeviceTablePanel extends JPanel   
{
    private final JTable deviceTable;
    private ResDeviceTableModel deviceTableModel;

    public InitializeDeviceTablePanel(CannedResData data)
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel deviceTablePanel = new JPanel();
        deviceTablePanel.setOpaque(true);
        deviceTablePanel.setLayout(new BoxLayout(deviceTablePanel, BoxLayout.Y_AXIS));
        deviceTablePanel.setBorder(new TitledBorder("Device table"));
        
        deviceTableModel = new ResDeviceTableModel();
        deviceTable = new JTable(deviceTableModel);
        deviceTable.setRowSelectionAllowed(true);
        deviceTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        deviceTableModel.setTable(deviceTable);
        
        JScrollPane scrollPane = new JScrollPane(deviceTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(ResPanel.TableWidth, ResPanel.TableHeight));
        deviceTable.setFillsViewportHeight(true);
        deviceTablePanel.add(scrollPane);
        add(deviceTablePanel, BorderLayout.CENTER);
    }

    public void clear()
    {
        deviceTableModel.clear();
    }

    public void addRow(Object[] row)
    {
        deviceTableModel.addRow(row);
    }
}
