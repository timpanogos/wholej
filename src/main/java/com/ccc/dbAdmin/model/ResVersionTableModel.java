/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.model;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.emitdo.oal.DOFObjectID;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.research.app.swing.OidCellEditor;
import org.emitdo.research.app.swing.OidCellEditor.OidRenderer;

public class ResVersionTableModel extends DefaultTableModel
{
    public ResVersionTableModel()
    {
    }

    public void setTable(JTable table)
    {
        TableColumn column = null;
        for (int i = 0; i < VersionRow.PreferredWidths.length; i++)
        {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(VersionRow.PreferredWidths[i]);
        }
        for (int i = 0; i < VersionRow.Renderers.length; i++)
        {
            if(VersionRow.Renderers[i] != null)
                table.setDefaultRenderer(VersionRow.Classes[i], VersionRow.Renderers[i]);
        }
        for (int i = 0; i < VersionRow.Editors.length; i++)
        {
            if(VersionRow.Editors[i] != null)
                table.setDefaultEditor(VersionRow.Classes[i], VersionRow.Editors[i]);
        }
    }
    
    @Override
    public int getColumnCount()
    {
        return VersionRow.ColumnNames.length;
    }

    @Override
    public String getColumnName(int index)
    {
        return VersionRow.ColumnNames[index];
    }
    
    @Override
    public Class<?> getColumnClass(int index)
    {
        return VersionRow.Classes[index];
    }
    
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return VersionRow.Editable[col];
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
    
    public static class VersionRow
    {
        public static final String[] ColumnNames;
        public static final Class<?>[] Classes;
        public static final int[] PreferredWidths;
        public static final boolean[] Editable;
        public static final DefaultTableCellRenderer[] Renderers;
        public static final DefaultCellEditor[] Editors;
        
        public static int VersionCol = 0;
        public static int ManagerCol = 1;
        
        static
        {
            ColumnNames = new String[2];
            ColumnNames[VersionCol] = "version";
            ColumnNames[ManagerCol] = "manager";
            
            Classes = new Class<?>[2];
            Classes[VersionCol] = DOFObjectID.class;
            Classes[ManagerCol] = Authentication.class;
            
            PreferredWidths = new int[2];
            PreferredWidths[VersionCol] = 60;
            PreferredWidths[ManagerCol] = 60;
            
            OidRenderer oidR = new OidRenderer();
            Renderers = new DefaultTableCellRenderer[2];
            Renderers[VersionCol] = oidR;
            Renderers[ManagerCol] = oidR;
            
            Editable = new boolean[2];
            Editable[VersionCol] = true;
            Editable[ManagerCol] = true;
            
            OidCellEditor oidE = new OidCellEditor();
            Editors = new DefaultCellEditor[2];
            Editors[VersionCol] = oidE;
            Editors[ManagerCol] = oidE;
        }
                                                        
        public DOFObjectID versionId;
        public Authentication manager;
        
        public VersionRow()
        {
        }
        
        public VersionRow(ResVersionTableModel model, int index)
        {
            Vector<?> row = (Vector<?>) model.getDataVector().get(index);
            versionId = (DOFObjectID)row.get(VersionCol);
            manager = (Authentication)row.get(ManagerCol);
        }

        public Object[] getRowData()
        {
            Object[] row = new Object[2];
            row[VersionCol] = versionId;
            row[ManagerCol] = manager;
            return row;
        }
    }
}
