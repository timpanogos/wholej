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
import org.emitdo.research.app.dbAdmin.model.ResVersionTableModel;

public class InitializeVersionTablePanel extends JPanel   
{
    private final JTable versionTable;
    private ResVersionTableModel versionTableModel;

    public InitializeVersionTablePanel(CannedResData data)
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel versionTablePanel = new JPanel();
        versionTablePanel.setOpaque(true);
        versionTablePanel.setLayout(new BoxLayout(versionTablePanel, BoxLayout.Y_AXIS));
        versionTablePanel.setBorder(new TitledBorder("Version table"));
        
        versionTableModel = new ResVersionTableModel();
        versionTable = new JTable(versionTableModel);
        versionTable.setRowSelectionAllowed(true);
        versionTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        versionTableModel.setTable(versionTable);
        
        JScrollPane scrollPane = new JScrollPane(versionTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(ResPanel.TableWidth, ResPanel.TableHeight));
        versionTable.setFillsViewportHeight(true);
        versionTablePanel.add(scrollPane);
        add(versionTablePanel);
    }

    public void clear()
    {
        versionTableModel.clear();
    }

    public void addRow(Object[] row)
    {
        versionTableModel.addRow(row);
    }
}
