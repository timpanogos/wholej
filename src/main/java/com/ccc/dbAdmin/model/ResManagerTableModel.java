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

public class ResManagerTableModel extends DefaultTableModel
{
    public ResManagerTableModel()
    {
    }

    public void setTable(JTable table)
    {
        TableColumn column = null;
        for (int i = 0; i < ManagerRow.PreferredWidths.length; i++)
        {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(ManagerRow.PreferredWidths[i]);
        }
        for (int i = 0; i < ManagerRow.Renderers.length; i++)
        {
            if(ManagerRow.Renderers[i] != null)
                table.setDefaultRenderer(ManagerRow.Classes[i], ManagerRow.Renderers[i]);
        }
        for (int i = 0; i < ManagerRow.Editors.length; i++)
        {
            if(ManagerRow.Editors[i] != null)
                table.setDefaultEditor(ManagerRow.Classes[i], ManagerRow.Editors[i]);
        }
    }
    
    @Override
    public int getColumnCount()
    {
        return ManagerRow.ColumnNames.length;
    }

    @Override
    public String getColumnName(int index)
    {
        return ManagerRow.ColumnNames[index];
    }
    
    @Override
    public Class<?> getColumnClass(int index)
    {
        return ManagerRow.Classes[index];
    }
    
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return ManagerRow.Editable[col];
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
    
    public static class ManagerRow
    {
        public static final String[] ColumnNames;
        public static final Class<?>[] Classes;
        public static final int[] PreferredWidths;
        public static final boolean[] Editable;
        public static final DefaultTableCellRenderer[] Renderers;
        public static final DefaultCellEditor[] Editors;
        
        public static int ManagerCol = 0;
        public static int PasswordCol = 1;
        public static int KeyCol = 2;
        
        static
        {
            ColumnNames = new String[3];
            ColumnNames[ManagerCol] = "manager";
            ColumnNames[PasswordCol] = "password";
            ColumnNames[KeyCol] = "key";
            
            Classes = new Class<?>[3];
            Classes[ManagerCol] = Authentication.class;
            Classes[PasswordCol] = DOFObjectID.class;
            Classes[KeyCol] = String.class;
            
            PreferredWidths = new int[3];
            PreferredWidths[ManagerCol] = 60;
            PreferredWidths[PasswordCol] = 60;
            PreferredWidths[KeyCol] = 60;
            
            OidRenderer oidR = new OidRenderer();
            Renderers = new DefaultTableCellRenderer[3];
            Renderers[ManagerCol] = oidR;
            Renderers[PasswordCol] = null;
            Renderers[KeyCol] = null;
            
            Editable = new boolean[3];
            Editable[ManagerCol] = true;
            Editable[PasswordCol] = true;
            Editable[KeyCol] = true;
            
            OidCellEditor oidE = new OidCellEditor();
            Editors = new DefaultCellEditor[7];
            Editors[ManagerCol] = oidE;
            Editors[PasswordCol] = null;
            Editors[KeyCol] = null;
        }
                                                        
        public Authentication manager;
        public String password;
        public String key;
        
        public ManagerRow()
        {
        }
        
        public ManagerRow(ResManagerTableModel model, int index)
        {
            Vector<?> row = (Vector<?>) model.getDataVector().get(index);
            manager = (Authentication)row.get(ManagerCol);
            password = (String)row.get(PasswordCol);
            key = (String)row.get(KeyCol);
        }

        public Object[] getRowData()
        {
            Object[] row = new Object[3];
            row[ManagerCol] = manager;
            row[PasswordCol] = password;
            row[KeyCol] = key;
            return row;
        }
    }
}
