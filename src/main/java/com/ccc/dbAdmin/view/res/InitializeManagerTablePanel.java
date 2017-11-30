/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.res;

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
import org.emitdo.research.app.dbAdmin.model.ResManagerTableModel;

public class InitializeManagerTablePanel extends JPanel   
{
    private final JTable managerTable;
    private ResManagerTableModel managerTableModel;

    public InitializeManagerTablePanel(CannedResData data)
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel managerTablePanel = new JPanel();
        managerTablePanel.setOpaque(true);
        managerTablePanel.setLayout(new BoxLayout(managerTablePanel, BoxLayout.Y_AXIS));
        managerTablePanel.setBorder(new TitledBorder("Manager table"));

        managerTableModel = new ResManagerTableModel();
        managerTable = new JTable(managerTableModel);
        managerTable.setRowSelectionAllowed(true);
        managerTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        managerTableModel.setTable(managerTable);
        
        JScrollPane scrollPane = new JScrollPane(managerTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(ResPanel.TableWidth, ResPanel.TableHeight));
        managerTable.setFillsViewportHeight(true);
        managerTablePanel.add(scrollPane);
        add(managerTablePanel);
    }

    public void clear()
    {
        managerTableModel.clear();
    }

    public void addRow(Object[] row)
    {
        managerTableModel.addRow(row);
    }
}
