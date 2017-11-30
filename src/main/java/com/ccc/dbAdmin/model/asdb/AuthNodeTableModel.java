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

public class AuthNodeTableModel extends DefaultTableModel
{
    public AuthNodeTableModel()
    {
    }

    public void setTable(JTable table)
    {
        TableColumn column = null;
        for (int i = 0; i < AuthNodeRow.PreferredWidths.length; i++)
        {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(AuthNodeRow.PreferredWidths[i]);
        }
        for (int i = 0; i < AuthNodeRow.Renderers.length; i++)
        {
            if(AuthNodeRow.Renderers[i] != null)
                table.setDefaultRenderer(AuthNodeRow.Classes[i], AuthNodeRow.Renderers[i]);
        }
        for (int i = 0; i < AuthNodeRow.Editors.length; i++)
        {
            if(AuthNodeRow.Editors[i] != null)
                table.setDefaultEditor(AuthNodeRow.Classes[i], AuthNodeRow.Editors[i]);
        }
    }
    
    public void addRow(Connection connection, ResultSet rs) throws Exception
    {
        AuthNodeRow row = new AuthNodeRow(connection, rs);
        addRow(row.getRowData());
    }

    @Override
    public int getColumnCount()
    {
        return AuthNodeRow.ColumnNames.length;
    }

    @Override
    public String getColumnName(int index)
    {
        return AuthNodeRow.ColumnNames[index];
    }
    
    @Override
    public Class<?> getColumnClass(int index)
    {
        return AuthNodeRow.Classes[index];
    }
    
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return AuthNodeRow.Editable[col];
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
    
    public static class AuthNodeRow
    {
        public static final String[] ColumnNames;
        public static final Class<?>[] Classes;
        public static final int[] PreferredWidths;
        public static final boolean[] Editable;
        public static final DefaultTableCellRenderer[] Renderers;
        public static final DefaultCellEditor[] Editors;
        
        public static int OidCol = 0;
        public static int EnabledCol = 1;
        public static int SourceIdCol = 2;
        
        static
        {
            ColumnNames = new String[3];
            ColumnNames[OidCol] = "oid";
            ColumnNames[EnabledCol] = "enabled";
            ColumnNames[SourceIdCol] = "sourceId";
            
            Classes = new Class<?>[3];
            Classes[OidCol] = Authentication.class; 
            Classes[EnabledCol] = Boolean.class;
            Classes[SourceIdCol] = Integer.class;
            
            PreferredWidths = new int[3];
            PreferredWidths[OidCol] = 60;
            PreferredWidths[EnabledCol] = 10;
            PreferredWidths[SourceIdCol] = 10;
            
            OidRenderer oidR = new OidRenderer();
            Renderers = new DefaultTableCellRenderer[3];
            Renderers[OidCol] = oidR;
            Renderers[EnabledCol] = null;
            Renderers[SourceIdCol] = null;
            
            Editable = new boolean[3];
            Editable[OidCol] = true;
            Editable[EnabledCol] = true;
            Editable[SourceIdCol] = true;
            
            OidCellEditor oidE = new OidCellEditor();
            Editors = new DefaultCellEditor[3];
            Editors[OidCol] = oidE;
            Editors[EnabledCol] = null;
            Editors[SourceIdCol] = null;
        }
                                                        
        public Authentication oid;
        public boolean enabled;
        public int sourceId;
        
        public AuthNodeRow()
        {
        }
        
        public AuthNodeRow(Connection connection, ResultSet rs) throws Exception
        {
            long id = rs.getLong(1);
            sourceId = rs.getInt(2);
            StorageNodeRow snrow = JdbStorageNodeJdbc.getRow(connection, id);
            oid = snrow.getOid();
            enabled = snrow.enabled;
        }
        
        public AuthNodeRow(AuthNodeTableModel model, int index)
        {
            Vector<?> row = (Vector<?>) model.getDataVector().get(index);
            oid = (Authentication)row.get(OidCol);
            enabled = (Boolean) row.get(EnabledCol);
            sourceId = (Integer) row.get(SourceIdCol);
        }

        public Object[] getRowData()
        {
            Object[] row = new Object[3];
            row[OidCol] = oid;
            row[EnabledCol] = enabled;
            row[SourceIdCol] = sourceId;
            return row;
        }
    }
}
