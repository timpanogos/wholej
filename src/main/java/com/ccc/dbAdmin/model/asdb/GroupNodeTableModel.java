/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model.asdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.emitdo.as.jdb.domain.JdbStorageNodeJdbc;
import org.emitdo.as.jdb.domain.JdbStorageNodeJdbc.StorageNodeRow;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.research.app.swing.OidCellEditor;
import org.emitdo.research.app.swing.OidCellEditor.OidRenderer;

public class GroupNodeTableModel extends DefaultTableModel
{
    public GroupNodeTableModel()
    {
    }

    public void setTable(JTable table)
    {
        TableColumn column = null;
        for (int i = 0; i < GroupNodeRow.PreferredWidths.length; i++)
        {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(GroupNodeRow.PreferredWidths[i]);
        }
        for (int i = 0; i < GroupNodeRow.Renderers.length; i++)
        {
            if(GroupNodeRow.Renderers[i] != null)
                table.setDefaultRenderer(GroupNodeRow.Classes[i], GroupNodeRow.Renderers[i]);
        }
        for (int i = 0; i < GroupNodeRow.Editors.length; i++)
        {
            if(GroupNodeRow.Editors[i] != null)
                table.setDefaultEditor(GroupNodeRow.Classes[i], GroupNodeRow.Editors[i]);
        }
    }
    
    public void addRow(Connection connection, ResultSet rs) throws Exception
    {
        GroupNodeRow row = new GroupNodeRow(connection, rs);
        addRow(row.getRowData());
    }

    @Override
    public int getColumnCount()
    {
        return GroupNodeRow.ColumnNames.length;
    }

    @Override
    public String getColumnName(int index)
    {
        return GroupNodeRow.ColumnNames[index];
    }
    
    @Override
    public Class<?> getColumnClass(int index)
    {
        return GroupNodeRow.Classes[index];
    }
    
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return GroupNodeRow.Editable[col];
    }
    
    @Override
    public void setValueAt(Object value, int row, int col)
    {
        super.setValueAt(value, row, col);
    }
    
    public void clear()
    {
        int size = getRowCount() - 1;
        for(int i=size; i >= 0; i--)
            removeRow(i);
    }
    
    public static class GroupNodeRow
    {
        public static final String[] ColumnNames;
        public static final Class<?>[] Classes;
        public static final int[] PreferredWidths;
        public static final boolean[] Editable;
        public static final DefaultTableCellRenderer[] Renderers;
        public static final DefaultCellEditor[] Editors;
        
        public static int OidCol = 0;
        public static int EnabledCol = 1;
        public static int StateCol = 2;
        public static int EpochCol = 3;
        public static int SourceIdCol = 4;
        
        static
        {
            ColumnNames = new String[5];
            ColumnNames[OidCol] = "oid";
            ColumnNames[EnabledCol] = "enabled";
            ColumnNames[StateCol] = "state";
            ColumnNames[EpochCol] = "epoch";
            ColumnNames[SourceIdCol] = "sourceId";
            
            Classes = new Class<?>[5];
            Classes[OidCol] = Authentication.class; 
            Classes[EnabledCol] = Boolean.class;
            Classes[StateCol] = Integer.class;
            Classes[EpochCol] = Integer.class;
            Classes[SourceIdCol] = Integer.class;
            
            PreferredWidths = new int[5];
            PreferredWidths[OidCol] = 60;
            PreferredWidths[EnabledCol] = 10;
            PreferredWidths[StateCol] = 10;
            PreferredWidths[EpochCol] = 10;
            PreferredWidths[SourceIdCol] = 10;
            
            OidRenderer oidR = new OidRenderer();
            Renderers = new DefaultTableCellRenderer[5];
            Renderers[OidCol] = oidR;
            Renderers[EnabledCol] = null;
            Renderers[StateCol] = null;
            Renderers[EpochCol] = null;
            Renderers[SourceIdCol] = null;
            
            Editable = new boolean[5];
            Editable[OidCol] = true;
            Editable[EnabledCol] = true;
            Editable[StateCol] = true;
            Editable[EpochCol] = true;
            Editable[SourceIdCol] = true;
            
            OidCellEditor oidE = new OidCellEditor();
            Editors = new DefaultCellEditor[5];
            Editors[OidCol] = oidE;
            Editors[EnabledCol] = null;
            Editors[StateCol] = null;
            Editors[EpochCol] = null;
            Editors[SourceIdCol] = null;
        }
                                                        
        public Authentication oid;
        public boolean enabled;
        public int state;
        public int sourceId;
        public int epoch;
        
        public GroupNodeRow()
        {
        }
        
        public GroupNodeRow(Connection connection, ResultSet rs) throws Exception
        {
            long id = rs.getLong(1);
            state = rs.getInt(2);
            sourceId = rs.getInt(3);
            epoch = rs.getInt(4);
            StorageNodeRow snrow = JdbStorageNodeJdbc.getRow(connection, id);
            oid = snrow.getOid();
            enabled = snrow.enabled;
        }
        
        public GroupNodeRow(GroupNodeTableModel model, int index)
        {
            Vector<?> row = (Vector<?>) model.getDataVector().get(index);
            oid = (Authentication)row.get(OidCol);
            enabled = (Boolean) row.get(EnabledCol);
            state = (Integer) row.get(StateCol);
            epoch = (Integer) row.get(EpochCol);
            sourceId = (Integer) row.get(SourceIdCol);
        }

        public Object[] getRowData()
        {
            Object[] row = new Object[5];
            row[OidCol] = oid;
            row[EnabledCol] = enabled;
            row[StateCol] = state;
            row[EpochCol] = epoch;
            row[SourceIdCol] = sourceId;
            return row;
        }
    }
}
