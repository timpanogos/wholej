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
import org.emitdo.oal.DOFObjectID.Domain;
import org.emitdo.research.app.swing.OidCellEditor;
import org.emitdo.research.app.swing.OidCellEditor.OidRenderer;

public class ResDeviceTableModel extends DefaultTableModel
{
    public ResDeviceTableModel()
    {
    }

    public void setTable(JTable table)
    {
        TableColumn column = null;
        for (int i = 0; i < DeviceRow.PreferredWidths.length; i++)
        {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(DeviceRow.PreferredWidths[i]);
        }
        for (int i = 0; i < DeviceRow.Renderers.length; i++)
        {
            if(DeviceRow.Renderers[i] != null)
                table.setDefaultRenderer(DeviceRow.Classes[i], DeviceRow.Renderers[i]);
        }
        for (int i = 0; i < DeviceRow.Editors.length; i++)
        {
            if(DeviceRow.Editors[i] != null)
                table.setDefaultEditor(DeviceRow.Classes[i], DeviceRow.Editors[i]);
        }
    }
    
    @Override
    public int getColumnCount()
    {
        return DeviceRow.ColumnNames.length;
    }

    @Override
    public String getColumnName(int index)
    {
        return DeviceRow.ColumnNames[index];
    }
    
    @Override
    public Class<?> getColumnClass(int index)
    {
        return DeviceRow.Classes[index];
    }
    
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return DeviceRow.Editable[col];
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
    
    public static class DeviceRow
    {
        public static final String[] ColumnNames;
        public static final Class<?>[] Classes;
        public static final int[] PreferredWidths;
        public static final boolean[] Editable;
        public static final DefaultTableCellRenderer[] Renderers;
        public static final DefaultCellEditor[] Editors;
        
        public static int DeviceCol = 0;
        public static int FactoryCol = 1;
        public static int DomainCol = 2;
        public static int ProxyCol = 3;
        public static int ManagerCol = 4;
        public static int VersionCol = 5;
        public static int ScopeCol = 6;
        public static int KeyCol = 7;
        
        static
        {
            ColumnNames = new String[8];
            ColumnNames[DeviceCol] = "device";
            ColumnNames[FactoryCol] = "factory";
            ColumnNames[DomainCol] = "domain";
            ColumnNames[ProxyCol] = "proxy";
            ColumnNames[VersionCol] = "version";
            ColumnNames[ManagerCol] = "manager";
            ColumnNames[ScopeCol] = "scope";
            ColumnNames[KeyCol] = "key";
            
            Classes = new Class<?>[8];
            Classes[DeviceCol] = DOFObjectID.class; 
            Classes[FactoryCol] = Authentication.class;
            Classes[DomainCol] = Domain.class;
            Classes[ProxyCol] = DOFObjectID.class;
            Classes[VersionCol] = DOFObjectID.class;
            Classes[ManagerCol] = Authentication.class;
            Classes[ScopeCol] = String.class;
            Classes[KeyCol] = String.class;
            
            PreferredWidths = new int[8];
            PreferredWidths[DeviceCol] = 60;
            PreferredWidths[FactoryCol] = 60;
            PreferredWidths[DomainCol] = 60;
            PreferredWidths[ProxyCol] = 60;
            PreferredWidths[VersionCol] = 60;
            PreferredWidths[ManagerCol] = 60;
            PreferredWidths[ScopeCol] = 5;
            PreferredWidths[KeyCol] = 60;
            
            OidRenderer oidR = new OidRenderer();
            Renderers = new DefaultTableCellRenderer[8];
            Renderers[DeviceCol] = oidR;
            Renderers[FactoryCol] = oidR;
            Renderers[DomainCol] = oidR;
            Renderers[ProxyCol] = oidR;
            Renderers[VersionCol] = oidR;
            Renderers[ManagerCol] = oidR;
            Renderers[ScopeCol] = null;
            Renderers[KeyCol] = null;
            
            Editable = new boolean[8];
            Editable[DeviceCol] = true;
            Editable[FactoryCol] = true;
            Editable[DomainCol] = true;
            Editable[ProxyCol] = true;
            Editable[VersionCol] = true;
            Editable[ManagerCol] = true;
            Editable[ScopeCol] = false;
            Editable[KeyCol] = true;
            
            OidCellEditor oidE = new OidCellEditor();
            Editors = new DefaultCellEditor[8];
            Editors[DeviceCol] = oidE;
            Editors[FactoryCol] = oidE;
            Editors[DomainCol] = oidE;
            Editors[ProxyCol] = oidE;
            Editors[VersionCol] = oidE;
            Editors[ManagerCol] = oidE;
            Editors[ScopeCol] = null;
            Editors[KeyCol] = null;
        }
                                                        
        public DOFObjectID device;
        public Authentication factory;
        public Authentication domain;
        public DOFObjectID proxyId;
        public DOFObjectID versionId;
        public Authentication manager;
        public String scope;
        public String key;
        
        public DeviceRow()
        {
        }
        
        public DeviceRow(ResDeviceTableModel model, int index)
        {
            Vector<?> row = (Vector<?>) model.getDataVector().get(index);
            device = (DOFObjectID)row.get(DeviceCol);
            factory = (Authentication) row.get(FactoryCol);
            domain = (Authentication) row.get(DomainCol);
            proxyId = (DOFObjectID)row.get(ProxyCol);
            versionId = (DOFObjectID)row.get(VersionCol);
            manager = (Authentication)row.get(ManagerCol);
            scope = (String)row.get(ScopeCol);
            key = (String)row.get(KeyCol);
        }

        public Object[] getRowData()
        {
            Object[] row = new Object[8];
            row[DeviceCol] = device;
            row[FactoryCol] = factory;
            row[DomainCol] = domain;
            row[ProxyCol] = proxyId;
            row[VersionCol] = versionId;
            row[ManagerCol] = manager;
            row[ScopeCol] = scope;
            row[KeyCol] = key;
            return row;
        }
    }
}
