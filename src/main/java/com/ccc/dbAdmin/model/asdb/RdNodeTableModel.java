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

public class RdNodeTableModel extends DefaultTableModel
{
    public RdNodeTableModel()
    {
    }

    public void setTable(JTable table)
    {
        TableColumn column = null;
        for (int i = 0; i < RdNodeRow.PreferredWidths.length; i++)
        {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(RdNodeRow.PreferredWidths[i]);
        }
        for (int i = 0; i < RdNodeRow.Renderers.length; i++)
        {
            if(RdNodeRow.Renderers[i] != null)
                table.setDefaultRenderer(RdNodeRow.Classes[i], RdNodeRow.Renderers[i]);
        }
        for (int i = 0; i < RdNodeRow.Editors.length; i++)
        {
            if(RdNodeRow.Editors[i] != null)
                table.setDefaultEditor(RdNodeRow.Classes[i], RdNodeRow.Editors[i]);
        }
    }
    
    public void addRow(Connection connection, ResultSet rs) throws Exception
    {
        RdNodeRow row = new RdNodeRow(connection, rs);
        addRow(row.getRowData());
    }

    @Override
    public int getColumnCount()
    {
        return RdNodeRow.ColumnNames.length;
    }

    @Override
    public String getColumnName(int index)
    {
        return RdNodeRow.ColumnNames[index];
    }
    
    @Override
    public Class<?> getColumnClass(int index)
    {
        return RdNodeRow.Classes[index];
    }
    
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return RdNodeRow.Editable[col];
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
    
    public static class RdNodeRow
    {
        public static final String[] ColumnNames;
        public static final Class<?>[] Classes;
        public static final int[] PreferredWidths;
        public static final boolean[] Editable;
        public static final DefaultTableCellRenderer[] Renderers;
        public static final DefaultCellEditor[] Editors;
        
        public static int OidCol = 0;
        public static int EnabledCol = 1;
        public static int RdIdCol = 2;
        public static int LocalNodeCol = 3;
        public static int TypeCol = 4;
        public static int PidCol = 5;
        
        static
        {
            ColumnNames = new String[6];
            ColumnNames[OidCol] = "oid";
            ColumnNames[EnabledCol] = "enabled";
            ColumnNames[RdIdCol] = "sourceId";
            ColumnNames[LocalNodeCol] = "defaultLocalNode";
            ColumnNames[TypeCol] = "type";
            ColumnNames[PidCol] = "pid";
            
            Classes = new Class<?>[6];
            Classes[OidCol] = Authentication.class; 
            Classes[EnabledCol] = Boolean.class;
            Classes[RdIdCol] = Integer.class;
            Classes[LocalNodeCol] = Authentication.class;
            Classes[TypeCol] = Long.class;
            Classes[PidCol] = Long.class;
            
            PreferredWidths = new int[6];
            PreferredWidths[OidCol] = 60;
            PreferredWidths[EnabledCol] = 10;
            PreferredWidths[RdIdCol] = 10;
            PreferredWidths[LocalNodeCol] = 60;
            PreferredWidths[TypeCol] = 10;
            PreferredWidths[PidCol] = 1;
            
            OidRenderer oidR = new OidRenderer();
            Renderers = new DefaultTableCellRenderer[6];
            Renderers[OidCol] = oidR;
            Renderers[EnabledCol] = null;
            Renderers[RdIdCol] = null;
            Renderers[LocalNodeCol] = oidR;
            Renderers[TypeCol] = null;
            Renderers[PidCol] = null;
            
            Editable = new boolean[6];
            Editable[OidCol] = true;
            Editable[EnabledCol] = true;
            Editable[RdIdCol] = true;
            Editable[LocalNodeCol] = true;
            Editable[TypeCol] = true;
            Editable[PidCol] = true;
            
            OidCellEditor oidE = new OidCellEditor();
            Editors = new DefaultCellEditor[6];
            Editors[OidCol] = oidE;
            Editors[EnabledCol] = null;
            Editors[RdIdCol] = null;
            Editors[LocalNodeCol] = oidE;
            Editors[TypeCol] = null;
            Editors[PidCol] = null;
        }
                                                        
        public Authentication oid;
        public boolean enabled;
        public int rdid;
        public Authentication localNode;
        public long type;
        public long pid;
        
        public RdNodeRow()
        {
        }
        
        public RdNodeRow(Connection connection, ResultSet rs) throws Exception
        {
            long id = rs.getLong(1);
            rdid = rs.getInt(2);
            long lnid = rs.getLong(3);
            StorageNodeRow anrow = JdbStorageNodeJdbc.getRow(connection, lnid);
            localNode = anrow.getOid();
            StorageNodeRow snrow = JdbStorageNodeJdbc.getRow(connection, id);
            oid = snrow.getOid();
            enabled = snrow.enabled;
        }
        
        public RdNodeRow(RdNodeTableModel model, int index)
        {
            Vector<?> row = (Vector<?>) model.getDataVector().get(index);
            oid = (Authentication)row.get(OidCol);
            enabled = (Boolean) row.get(EnabledCol);
            rdid = (Integer) row.get(RdIdCol);
            localNode = (Authentication) row.get(RdIdCol);
            type = (Long)row.get(TypeCol);
            pid = (Long)row.get(PidCol);
        }

        public Object[] getRowData()
        {
            Object[] row = new Object[6];
            row[OidCol] = oid;
            row[EnabledCol] = enabled;
            row[RdIdCol] = rdid;
            row[LocalNodeCol] = localNode;
            row[TypeCol] = type;
            row[PidCol] = pid;
            return row;
        }
    }
}
