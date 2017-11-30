/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.res;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.emitdo.oal.DOFObjectID;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.oal.DOFObjectID.Domain;
import org.emitdo.oal.DOFUtil;
import org.emitdo.research.app.dbAdmin.ConnectionControl;
import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.ResManagerFrame;
import org.emitdo.research.app.dbAdmin.model.CannedResData;
import org.emitdo.research.app.dbAdmin.model.DomainData;
import org.emitdo.research.app.dbAdmin.model.OracleCannedResInitializer;
import org.emitdo.research.app.dbAdmin.model.ResDeviceTableModel.DeviceRow;
import org.emitdo.research.app.dbAdmin.model.ResManagerTableModel.ManagerRow;
import org.emitdo.research.app.dbAdmin.model.ResVersionTableModel.VersionRow;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel.ConnectedEvent;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;


public class InitializeDataPanel extends JPanel implements  ActionListener, WindowFocusListener, SwappedFocusListener, DocumentListener
{
    private final int numberOfColumns = 10;

    private final ResManagerFrame resManager;
    private final CannedResData data;
    private final DomainData platformData;
    private final DomainData factoryData;
    
    private JTextField tablespaceField;
    private JTextField deviceCountField;
    private JTextField gatewayCountField;
    private JTextField gatewayDeviceCountField;
    private JTextField managersPerDeviceField;
    private JTextField managersPerGatewayField;
    private JTextField managersPerGatewayDeviceField;
    private JTextField managersPerVersionField;
    private JCheckBox autoCreateCheck;
    
    private JButton initializeButton;
    private JButton manageButton;
    private JButton saveButton;
    private JButton calculateButton;
    
    private AllDomainsConnectionPanel domainConnectionsPanel;
    private final AvailableConnectionsPanel connectionsPanel;
    
    public final AtomicBoolean painted;
    public final AtomicBoolean ttcConnected;
    public final AtomicBoolean connected;
    public String acknowledge;

    private final AtomicInteger nextDeviceScope;
    private final ResPanel resPanel;
    
    public InitializeDataPanel(CannedResData data, ResPanel resPanel, ResManagerFrame resManager)
    {
        this.data = data;
        this.resManager = resManager;
        this.resPanel = resPanel;
        platformData = resManager.getStorageControl().getDomainData(DomainType.Res);
        factoryData = resManager.getStorageControl().getDomainData(DomainType.Factory);
        painted = new AtomicBoolean(false);
        ttcConnected = new AtomicBoolean(false);
        connected = new AtomicBoolean(false);
        nextDeviceScope = new AtomicInteger(data.deviceScopeStartIndex - 1);
        
//        setLayout(new BorderLayout());
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder("Residential Platform"));

        resManager.addWindowFocusListener(this);

        JPanel dataPanel = new JPanel();
        dataPanel.setOpaque(true);
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder("Initialize parameters"));
        
        AtomicInteger maxWidth = new AtomicInteger();
        JLabel tablespaceLabel = FrameBase.getLabel("Tablespace:", maxWidth);
        JLabel deviceCountLabel = FrameBase.getLabel("Devices:", maxWidth);
        JLabel gatewayCountLabel = new JLabel("Gateways:", SwingConstants.RIGHT);
        JLabel subGatwayCountLabel = FrameBase.getLabel("Gateway devices:", maxWidth);
        JLabel managersPerDeviceLabel = FrameBase.getLabel("Device managers:", maxWidth);
        JLabel managersPerGatewayLabel = FrameBase.getLabel("Gateway managers:", maxWidth);
        JLabel managersPerSubGatwayLabel = FrameBase.getLabel("Gateway device managers:", maxWidth);
        JLabel managersPerVersionLabel = FrameBase.getLabel("Version managers:", maxWidth);
        JLabel autoInitLabel = FrameBase.getLabel("Auto create:", maxWidth);
        int width = maxWidth.get();

        tablespaceField = new JTextField(CannedResData.DefaultTablespace, numberOfColumns);
        FrameBase.addTextParamToPanel(tablespaceLabel, tablespaceField, width, -1, "Enter tablespace name", dataPanel, this);

        deviceCountField = new JTextField(""+data.deviceCount, numberOfColumns);
        FrameBase.addTextParamToPanel(deviceCountLabel, deviceCountField, width, -1, "Enter the number of devices to add", dataPanel, this);

        gatewayCountField = new JTextField(""+data.gatewayCount, numberOfColumns);
        FrameBase.addTextParamToPanel(gatewayCountLabel, gatewayCountField, width, -1, "Enter the number of Gateways to add", dataPanel, this);
        
        gatewayDeviceCountField = new JTextField(""+data.gatewayDeviceCount, numberOfColumns);
        FrameBase.addTextParamToPanel(subGatwayCountLabel, gatewayDeviceCountField, width, -1, "Enter the number of Gateway devices per Gateway to add", dataPanel, this);
        
        managersPerDeviceField = new JTextField(""+data.managersPerDevice, numberOfColumns);
        FrameBase.addTextParamToPanel(managersPerDeviceLabel, managersPerDeviceField, width, -1, "Enter the number of managers per device", dataPanel, this);
        
        managersPerGatewayField = new JTextField(""+data.managersPerGateway, numberOfColumns);
        FrameBase.addTextParamToPanel(managersPerGatewayLabel, managersPerGatewayField, width, -1, "Enter the number of managers per gateway", dataPanel, this);
        
        managersPerGatewayDeviceField = new JTextField(""+data.managersPerGatewayDevice, numberOfColumns);
        FrameBase.addTextParamToPanel(managersPerSubGatwayLabel, managersPerGatewayDeviceField, width, -1, "Enter the number of managers per gateway device", dataPanel, this);
        
        managersPerVersionField = new JTextField(""+data.managersPerVersion, numberOfColumns);
        FrameBase.addTextParamToPanel(managersPerVersionLabel, managersPerVersionField, width, -1, "Enter the number of managers per version", dataPanel, this);
        
        autoCreateCheck = FrameBase.addCheckBoxToPanel(autoInitLabel, true, true, width, -1, "Supports auto creation of schema", dataPanel, this);
        
        mainPanel.add(dataPanel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        calculateButton = FrameBase.addButtonToPanel("Calculate", false, "Calculate and present initialization table based on current values", buttonPanel, this);
        saveButton = FrameBase.addButtonToPanel("Save", false, "Manage the selected AS Storage", buttonPanel, this);
        mainPanel.add(buttonPanel);

        JPanel dPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dPanel.add(mainPanel);
        
        connectionsPanel = new AvailableConnectionsPanel(
                data.connections, "Supported Residential Platform connections", 
                true, true, false, true, null, this);
        mainPanel.add(connectionsPanel);
        
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        manageButton = FrameBase.addButtonToPanel("Manage", false, "Manage the selected RES Storage", buttonPanel, this);
        initializeButton = FrameBase.addButtonToPanel("Initialize", false, "First time initialization of selected RES", buttonPanel, this);
        mainPanel.add(buttonPanel);
        
        JScrollPane scrollPane = new JScrollPane(dPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 530));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setDomainConnectionsPanel(AllDomainsConnectionPanel domainConns)
    {
        domainConnectionsPanel = domainConns;
    }

    Hashtable<DOFObjectID, List<Authentication>> deviceManagersOnVersionMap = new Hashtable<DOFObjectID, List<Authentication>>();
    public void refresh()
    {
        nextDeviceScope.set(data.deviceScopeStartIndex - 1);
        try
        {
            Domain platform = platformData.getDomainOid();
            Domain factory = factoryData.getDomainOid();
            int deviceVersion = 0;
            int gatewayVersion = 1;
            int gatewayDeviceVersion = 2;
            int deviceManagerIndex = 1;
            int gatewayManagerIndex = deviceManagerIndex + data.getManagersPerDevice() - 1;
            int gatewayDeviceManagerIndex = gatewayManagerIndex + data.getManagersPerGateway() - 1;
            
            
            data.totalManagers = gatewayDeviceManagerIndex + data.getGatewayDeviceCount() - 1;
            data.totalVersions = 2 + data.getGatewayDeviceCount();
            if(data.getDeviceCount() > 1)
            {
                data.totalVersions++;
                gatewayVersion++;
                gatewayDeviceVersion++;
            }
            if(data.getGatewayCount() > 1)
            {
                data.totalVersions++;
                gatewayDeviceVersion++;
            }
            
            for(int i=0; i < data.getDeviceCount(); i++)
            {
                DeviceRow row = new DeviceRow();
                row.device = OracleCannedResInitializer.getDeviceForIndex(i, platform).oid;
                row.factory = OracleCannedResInitializer.getDeviceForIndex(i, factory).getAuthentication();
                int version = (i % 2) == 0 ? deviceVersion : deviceVersion + 1;
                row.versionId = OracleCannedResInitializer.getVersionForIndex(version, platform).oid;
                Authentication[] managers = OracleCannedResInitializer.getManagersForDeviceIndex(deviceManagerIndex, data.getManagersPerDevice(), platform);
                row.manager = managers[0];
                row.scope = ""+nextDeviceScope.incrementAndGet();
                row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getDeviceKeyForIndex(i));
                resPanel.addRowToDevice(row.getRowData());
                addManagerToVersionMap(row.versionId, managers[0]);                
                for(int j=1; j < data.getManagersPerDevice(); j++)
                {
                    DeviceRow mrow = new DeviceRow();
                    mrow.manager = managers[j];
                    resPanel.addRowToDevice(mrow.getRowData());
                    addManagerToVersionMap(row.versionId, managers[j]);                
                }
            }
            int nextGatewayDeviceIndex = 0;
            for(int i=0; i < data.getGatewayCount(); i++)
            {
                DOFObjectID home = null;
                DeviceRow row = new DeviceRow();
                row.device = OracleCannedResInitializer.getGatewayForIndex(i, platform).oid;
                row.factory = OracleCannedResInitializer.getGatewayForIndex(i, factory).getAuthentication();
                row.domain = OracleCannedResInitializer.getHomeDomainForIndex(i);
                home = row.device;
                int version = (i % 2) == 0 ? gatewayVersion : gatewayVersion + 1;
                row.versionId = OracleCannedResInitializer.getVersionForIndex(version, platform).oid;
                Authentication[] managers = OracleCannedResInitializer.getManagersForDeviceIndex(gatewayManagerIndex, data.getManagersPerGateway(), platform);
                row.manager = managers[0];
                row.scope = ""+nextDeviceScope.incrementAndGet();
                row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getGatewayKeyForIndex(i));
                resPanel.addRowToDevice(row.getRowData());
                addManagerToVersionMap(row.versionId, managers[0]);                
                for(int j=1; j < data.getManagersPerGateway(); j++)
                {
                    DeviceRow mrow = new DeviceRow();
                    mrow.manager = managers[j];
                    resPanel.addRowToDevice(mrow.getRowData());
                    addManagerToVersionMap(row.versionId, managers[j]);                
                }
                for(int j=0; j < data.getGatewayDeviceCount(); j++)
                {
                    row = new DeviceRow();
                    row.device = OracleCannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex, platform).oid;
                    row.factory = OracleCannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex++, factory).getAuthentication();
                    row.proxyId = home;
                    version = gatewayDeviceVersion + j;
                    row.versionId = OracleCannedResInitializer.getVersionForIndex(version, platform).oid;
                    managers = OracleCannedResInitializer.getManagersForDeviceIndex(gatewayDeviceManagerIndex, data.getManagersPerGatewayDevice(), platform);
                    row.manager = managers[0];
                    row.scope = ""+nextDeviceScope.incrementAndGet();
                    row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getGatewayDeviceKeyForIndex(j));
                    resPanel.addRowToDevice(row.getRowData());
                    addManagerToVersionMap(row.versionId, managers[0]);                
                    for(int k=1; k < data.getManagersPerGatewayDevice(); k++)
                    {
                        DeviceRow mrow = new DeviceRow();
                        mrow.manager = managers[k];
                        resPanel.addRowToDevice(mrow.getRowData());
                        addManagerToVersionMap(row.versionId, managers[k]);                
                    }
                }
            }
            for(int i=0; i < data.totalVersions; i++)
            {
                VersionRow row = new VersionRow();
                row.versionId = OracleCannedResInitializer.getVersionForIndex(i, platform).oid;
                Authentication[] managers = OracleCannedResInitializer.getManagersForVersionIndex(deviceManagerIndex, data.getManagersPerVersion(), platform);
                row.manager = managers[0];
                addManagerToVersionMap(row.versionId, managers[0]);                
                for(int j=1; j < data.getManagersPerVersion(); j++)
                    addManagerToVersionMap(row.versionId, managers[j]);                
            }
            
            for(int i=0; i < data.totalVersions; i++)
            {
                DOFObjectID versionId = OracleCannedResInitializer.getVersionForIndex(i, platform).oid;
                List<Authentication> managers = deviceManagersOnVersionMap.get(versionId);
                int size = managers.size();
                for(int j=0; j < size; j++)
                {
                    VersionRow row = new VersionRow();
                    row.versionId = versionId;
                    row.manager = managers.get(j);
                    resPanel.addRowToVersion(row.getRowData());
                }
            }            
            
            for(int i=0; i < data.totalManagers; i++)
            {
                ManagerRow row = new ManagerRow();
                row.manager = OracleCannedResInitializer.getManagerForDevice(i, platform);
                row.password = OracleCannedResInitializer.getPassword(row.manager);
                row.key = DOFUtil.bytesToHexString(OracleCannedResInitializer.getManagerKeyForIndex(i));
                resPanel.addRowToManager(row.getRowData());
            }
        }catch(Exception e)
        {
            FrameBase.displayException(null, resManager, "bad news", "failed to generate example table", e);
        }
        resManager.swapAndSetFocus(this, deviceCountField);
    }
    
    private void addManagerToVersionMap(DOFObjectID version, Authentication manager)
    {
        List<Authentication> managers = deviceManagersOnVersionMap.get(version);
        if(managers == null)
        {
            managers = new ArrayList<Authentication>();
            deviceManagersOnVersionMap.put(version, managers);
        }
        if(managers.contains(manager))
            return;
        managers.add(manager);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        if(src == domainConnectionsPanel)
        {
            String command = e.getActionCommand(); 
            if(command.equals(ConnectionControl.ConnectedActionCommand))
            {
                ttcConnected.set(e.getID() == ConnectedEvent.Connected ? true : false);
                initializeButton.setEnabled((autoCreateCheck.isSelected() || connected.get()) && ttcConnected.get());
                manageButton.setEnabled(connected.get() && ttcConnected.get());
                return;
            }
        }
        if(src == autoCreateCheck)
        {
            initializeButton.setEnabled((autoCreateCheck.isSelected() || connected.get()) && ttcConnected.get());
        }
        
        if(src == connectionsPanel)
        {
            String command = e.getActionCommand(); 
            if(command.equals(ConnectionControl.ConnectedActionCommand))
            {
                connected.set(e.getID() == ConnectedEvent.Connected ? true : false);
                initializeButton.setEnabled((autoCreateCheck.isSelected() || connected.get()) && ttcConnected.get());
                manageButton.setEnabled(connected.get() && ttcConnected.get());
                return;
            }
            if(command.equals(ConnectionControl.ConnectButtonLabel))
            {
                resManager.getConnectionControl().
                    connect(data.connections.currentlySelected, null, true, this, null);
                data.connData = data.connections.currentlySelected;
               return;
            }
            if(command.equals(ConnectionControl.DisconnectButtonLabel))
            {
                resManager.getConnectionControl().
                    disconnect(data.connections.currentlySelected, this);
               return;
            }
            return;
        }
        
        if(src == saveButton)
        {
            saveButton.setEnabled(false);
            data.tablespace = tablespaceField.getText().trim();
            data.deviceCount = Integer.parseInt(deviceCountField.getText().trim());
            data.gatewayCount = Integer.parseInt(gatewayCountField.getText().trim());
            data.gatewayDeviceCount = Integer.parseInt(gatewayDeviceCountField.getText().trim());
            data.managersPerDevice = Integer.parseInt(managersPerDeviceField.getText().trim());
            data.managersPerGateway = Integer.parseInt(managersPerGatewayField.getText().trim());
            data.managersPerGatewayDevice = Integer.parseInt(managersPerGatewayDeviceField.getText().trim());
            data.managersPerVersion = Integer.parseInt(managersPerVersionField.getText().trim());
            data.savePreferences();
            refresh();
            return;
        }
        if(src == calculateButton)
        {
            resPanel.clear();
            calculateButton.setEnabled(false);
            refresh();
            return;
        }
        
        if(src == initializeButton)
        {
            int option = JOptionPane.showConfirmDialog(
                    resManager, 
                    "WARNING:\nYou are about to initialize the RES Storage domain that is currently selected.\n" +
                    "Auto create is selected for this RES Storage.\nIf the currently selected storage exits it will be deleted and rebuilt.\nIf it does not exist it will be created." +
                    "\n\nAre you sure you want to continue?",
                    "Initialize Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(option != JOptionPane.OK_OPTION)
                return;
            acknowledge = "RES storage initialization completed";
            
            Runnable runner = new Runnable(){@Override public void run(){initializeButton.setEnabled(false);}};
            SwingUtilities.invokeLater(runner);
            
            resManager.getConnectionControl().initializeRes(data, autoCreateCheck.isSelected(), this);
            return;
        }
    }

    @Override
    public void focusRequested(Object context)
    {
        if(context instanceof Throwable)
        {
            acknowledge = null;
            FrameBase.displayException(null, resManager, "Worker thread exception", "Failed to connect and/or initialize", (Throwable)context);
        }
        painted.set(true);
        windowGainedFocus(null);
        if(acknowledge != null)
            JOptionPane.showMessageDialog(resManager, acknowledge);
        acknowledge = null;
    }

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        connectionsPanel.refresh();
    }

    @Override public void windowLostFocus(WindowEvent e){}

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        if(!painted.get())
            return;
        data.guiTablespace = tablespaceField.getText();
        data.guiDeviceCount = deviceCountField.getText();
        data.guiGatewayCount = gatewayCountField.getText();
        data.guiGatewayDeviceCount = gatewayDeviceCountField.getText();
        data.guiManagersPerDevice = managersPerDeviceField.getText();
        data.guiManagersPerGateway = managersPerGatewayField.getText();
        data.guiManagersPerGatewayDevice = managersPerGatewayDeviceField.getText();
        data.guiManagersPerVersion = managersPerVersionField.getText();
        saveButton.setEnabled(true);
        calculateButton.setEnabled(true);
    }
    @Override public void insertUpdate(DocumentEvent e){changedUpdate(e);}
    @Override public void removeUpdate(DocumentEvent e){changedUpdate(e);}
}
