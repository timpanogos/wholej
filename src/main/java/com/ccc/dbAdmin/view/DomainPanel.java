/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
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

import org.emitdo.research.app.dbAdmin.AsManagerFrame;
import org.emitdo.research.app.dbAdmin.ConnectionControl;
import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.model.ConnectionsData;
import org.emitdo.research.app.dbAdmin.model.DomainData;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel;
import org.emitdo.research.app.dbAdmin.view.connection.AvailableConnectionsPanel.ConnectedEvent;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class DomainPanel extends JPanel implements 
    ActionListener, DocumentListener, WindowFocusListener, SwappedFocusListener
{
    private final int numberOfColumns = 52;
    private final ViewStorageData view;

    private final ConnectionsData connections;
    private final ConnectionsData clusterConnections;
    
    private JTextField domainField;
    private JTextField hubNameField;
    private JTextField tablespaceField;
    private JTextField routerNameField;
    private JTextField routerPasswordField;
    private JTextField routerPresharedField;
    private JTextField ctrouterNameField;
    private JTextField ctrouterPasswordField;
    private JTextField ctrouterPresharedField;
    private JTextField managerNameField;
    private JTextField managerPasswordField;
    private JTextField managerPresharedField;
    private JCheckBox autoCreateCheck;
    private JCheckBox requireAuthCheck;
    private JCheckBox createGlobalCheck;
    
    private JButton initializeButton;
    private JButton manageButton;
    private JButton saveButton;
    private JButton resetButton;
    private JButton preferencesButton;
    private JButton defaultButton;
    
    private final AsManagerFrame asManager;
    private final AvailableConnectionsPanel connectionsPanel;
    private final AvailableConnectionsPanel clusterConnectionsPanel;
    public final AtomicBoolean painted;
    public String acknowledge;
    
    public DomainPanel(DomainType domainType, ConnectionsData connections, ConnectionsData clusterConnections, AsManagerFrame asManager)
    {
        view = new ViewStorageData(asManager.getDomainControl().getDomainData(domainType));
        this.asManager = asManager;
        this.connections = connections;
        this.clusterConnections = clusterConnections;
        painted = new AtomicBoolean(false);
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder(view.data.title));
        
        AtomicInteger maxWidth = new AtomicInteger();
        JLabel domainLabel = FrameBase.getLabel("Domain:", maxWidth);
        JLabel hubNameLabel = new JLabel("Hub:", SwingConstants.RIGHT);
        JLabel tablespaceLabel = new JLabel("Tablespace:", SwingConstants.RIGHT);
        JLabel routerNameLabel = FrameBase.getLabel("Router:", maxWidth);
        JLabel routerPasswordLabel = FrameBase.getLabel("Password:", maxWidth);
        JLabel routerPresharedLabel = FrameBase.getLabel("Preshared:", maxWidth);
        JLabel ctrouterNameLabel = FrameBase.getLabel("CtRouter:", maxWidth);
        JLabel ctrouterPasswordLabel = FrameBase.getLabel("Password:", maxWidth);
        JLabel ctrouterPresharedLabel = FrameBase.getLabel("Preshared:", maxWidth);
        JLabel managerNameLabel = FrameBase.getLabel("Manager:", maxWidth);
        JLabel managerPasswordLabel = FrameBase.getLabel("Password:", maxWidth);
        JLabel managerPresharedLabel = FrameBase.getLabel("Preshared:", maxWidth);
        JLabel autoInitLabel = FrameBase.getLabel("Auto create:", maxWidth);
        JLabel requiresAuthLabel = FrameBase.getLabel("Requires auth:", maxWidth);
        JLabel createGlobalLabel = FrameBase.getLabel("Create global:", maxWidth);
        int width = maxWidth.get();

        JPanel domainPanel = new JPanel();
        domainPanel.setOpaque(true);
        domainPanel.setLayout(new BoxLayout(domainPanel, BoxLayout.Y_AXIS));
        domainPanel.setBorder(new TitledBorder("Domain information"));
        
        domainField = new JTextField(view.data.prefDomain, numberOfColumns);
        FrameBase.addTextParamToPanel(domainLabel, domainField, width, -1, "Enter a valid DOFObjectID.Domain value for the domain", domainPanel, this);

        hubNameField = new JTextField(view.data.prefHubOid, numberOfColumns);
        FrameBase.addTextParamToPanel(hubNameLabel, hubNameField, width, -1, "Enter a valid DOFObjectID.Authentication value for the domains Hub", domainPanel, this);

        tablespaceField = new JTextField(view.data.prefTablespace, numberOfColumns);
        FrameBase.addTextParamToPanel(tablespaceLabel, tablespaceField, width, -1, "Enter tablespace name", domainPanel, this);

        JPanel routerPanel = new JPanel();
        routerPanel.setOpaque(true);
        routerPanel.setLayout(new BoxLayout(routerPanel, BoxLayout.Y_AXIS));
        routerPanel.setBorder(new TitledBorder("Router credentials"));
        
        routerNameField = new JTextField(view.data.prefRouterOid, numberOfColumns);
        FrameBase.addTextParamToPanel(routerNameLabel, routerNameField, width, -1, "Enter a valid DOFObjectID.Authentication value for the Router Credentials", routerPanel, this);
        
        routerPasswordField = new JTextField(view.data.prefRouterPassword, numberOfColumns);
        FrameBase.addTextParamToPanel(routerPasswordLabel, routerPasswordField, width, -1, "Enter a Password value for the Router Credentials", routerPanel, this);

        routerPresharedField = new JTextField(view.data.prefRouterPreshared, numberOfColumns);
        FrameBase.addTextParamToPanel(routerPresharedLabel, routerPresharedField, width, -1, "Enter a valid Preshared Key value for the Router Credentials", routerPanel, this);
        
        JPanel ctrouterPanel = new JPanel();
        ctrouterPanel.setOpaque(true);
        ctrouterPanel.setLayout(new BoxLayout(ctrouterPanel, BoxLayout.Y_AXIS));
        ctrouterPanel.setBorder(new TitledBorder("CtRouter credentials"));
        
        ctrouterNameField = new JTextField(view.data.prefCtrouterOid, numberOfColumns);
        FrameBase.addTextParamToPanel(ctrouterNameLabel, ctrouterNameField, width, -1, "Enter a valid DOFObjectID.Authentication value for the CtRouter Credentials", ctrouterPanel, this);

        ctrouterPasswordField = new JTextField(view.data.prefCtrouterPassword, numberOfColumns);
        FrameBase.addTextParamToPanel(ctrouterPasswordLabel, ctrouterPasswordField, width, -1, "Enter a Password value for the CtRouter Credentials", ctrouterPanel, this);

        ctrouterPresharedField = new JTextField(view.data.prefCtrouterPreshared, numberOfColumns);
        FrameBase.addTextParamToPanel(ctrouterPresharedLabel, ctrouterPresharedField, width, -1, "Enter a valid Preshared Key value for the CtRouter Credentials", ctrouterPanel, this);

        JPanel managerPanel = new JPanel(new BorderLayout());
        managerPanel.setOpaque(true);
        managerPanel.setLayout(new BoxLayout(managerPanel, BoxLayout.Y_AXIS));
        managerPanel.setBorder(new TitledBorder("Manager credentials"));
        
        managerNameField = new JTextField(view.data.prefManagerOid, numberOfColumns);
        FrameBase.addTextParamToPanel(managerNameLabel, managerNameField, width, -1, "Enter a valid DOFObjectID.Authentication value for the Manager Credentials", managerPanel, this);

        managerPasswordField = new JTextField(view.data.prefManagerPassword, numberOfColumns);
        FrameBase.addTextParamToPanel(managerPasswordLabel, managerPasswordField, width, -1, "Enter a Password value for the Manager Credentials", managerPanel, this);

        managerPresharedField = new JTextField(view.data.prefManagerPreshared, numberOfColumns);
        FrameBase.addTextParamToPanel(managerPresharedLabel, managerPresharedField, width, -1, "Enter a valid Preshared Key value for the Manager Credentials", managerPanel, this);

        domainPanel.add(managerPanel);
        
        if(domainType == DomainType.Cluster)
        {
            domainPanel.add(routerPanel);
            domainPanel.add(ctrouterPanel);
        }
        autoCreateCheck = FrameBase.addCheckBoxToPanel(autoInitLabel, true, true, width, -1, "Supports auto creation of schema", domainPanel, this);
        requireAuthCheck = FrameBase.addCheckBoxToPanel(requiresAuthLabel, true, true, width, -1, "Requires Authentication", domainPanel, this);
        createGlobalCheck = FrameBase.addCheckBoxToPanel(createGlobalLabel, true, true, width, -1, "Create Global Schema", domainPanel, this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        saveButton = FrameBase.addButtonToPanel("Save", false, "Save the current entries", buttonPanel, this);
        resetButton = FrameBase.addButtonToPanel("Reset", false, "Reset to previous edited state", buttonPanel, this);
        preferencesButton = FrameBase.addButtonToPanel("Preferences", false, "Set all fields to their preference values", buttonPanel, this);
        defaultButton = FrameBase.addButtonToPanel("Default", false, "Set all fields to their default values", buttonPanel, this);
        
        domainPanel.add(buttonPanel);
        if(domainType != DomainType.Rg)
            mainPanel.add(domainPanel);
        else
        {
            FrameBase.addTextParamToPanel(domainLabel, domainField, width, -1, "Enter a valid DOFObjectID.Domain value for the domain", mainPanel, this);
            requireAuthCheck = FrameBase.addCheckBoxToPanel(requiresAuthLabel, true, false, width, -1, "Requires Authentication", mainPanel, this);
            mainPanel.add(buttonPanel);
        }
        
        DomainData clusterData = asManager.getDomainControl().getDomainData(DomainType.Cluster);
        boolean supportsOracle = domainType != DomainType.Rg;
        connectionsPanel = new AvailableConnectionsPanel(
                connections, "Supported " + domainType.name() + " domain connections", 
                true, supportsOracle, true, true, clusterData, this);
        mainPanel.add(connectionsPanel);
        
        if(domainType != DomainType.Cluster && domainType != DomainType.Rg)
        {
            clusterConnectionsPanel = new AvailableConnectionsPanel(
                    clusterConnections, "Available cluster domain connections", 
                    false, true, true, true, clusterData, null);
            mainPanel.add(clusterConnectionsPanel);
        }else
            clusterConnectionsPanel = null;
        
        asManager.addWindowFocusListener(this);

        buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        manageButton = FrameBase.addButtonToPanel("Manage", false, "Manage the selected AS Storage", buttonPanel, this);
        initializeButton = FrameBase.addButtonToPanel("Initialize", false, "First time initialization of basic required TTC values", buttonPanel, this);
        
        mainPanel.add(buttonPanel);
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dataPanel.add(mainPanel);
        
        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        add(scrollPane, BorderLayout.CENTER);
        
        view.refresh();
        Runnable runner = new Runnable(){@Override public void run(){domainField.requestFocus();}};
        SwingUtilities.invokeLater(runner);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        
        if(src == autoCreateCheck)
        {
            if(autoCreateCheck.isSelected())
                createGlobalCheck.setEnabled(true);
            else
                createGlobalCheck.setEnabled(false);
            boolean connected = connectionsPanel.isSelectionConnected();
            initializeButton.setEnabled(connected || autoCreateCheck.isSelected() ? true : false);
            return;
        }
        
        if(src == connectionsPanel)
        {
            String command = e.getActionCommand(); 
            if(command.equals(ConnectionControl.ConnectedActionCommand))
            {
                boolean connected = e.getID() == ConnectedEvent.NotConnected ? false : true;
                manageButton.setEnabled(connected);
                initializeButton.setEnabled(connected || autoCreateCheck.isSelected() ? true : false);
                return;
            }
            
            if(command.equals(ConnectionControl.ConnectButtonLabel))
            {
                asManager.getConnectionControl().
                    connect(connections.currentlySelected, domainField.getText(), requireAuthCheck.isSelected(), this, null);
               return;
            }
            
            if(command.equals(ConnectionControl.DisconnectButtonLabel))
            {
                asManager.getConnectionControl().
                    disconnect(connections.currentlySelected, this);
               return;
            }
            return;
        }
        
        if(src == initializeButton)
        {
            int option = JOptionPane.showConfirmDialog(
                    asManager, 
                    "WARNING:\nYou are about to initialize the AS Storage domain that is currently selected.\n" +
                    (autoCreateCheck.isSelected() ? 
                    "Auto create is selected for this AS Storage.\nIf the currently selected storage exits it will be deleted and rebuilt.\nIf it does not exist it will be created." :
                    "It is expected that the currently selected storage has it's schema installed\nbut that initial setup has not been done before." +
                    "\n\nAre you sure you want to continue?"),
                    "Initialize Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(option != JOptionPane.OK_OPTION)
                return;
            acknowledge = "AS storage initialization completed";
            DomainData clusterData = asManager.getDomainControl().getDomainData(DomainType.Cluster);
            
            Runnable runner = new Runnable(){@Override public void run(){initializeButton.setEnabled(false);}};
            SwingUtilities.invokeLater(runner);
            
            asManager.getConnectionControl().
                initializeAs(
                    connections.currentlySelected,
                    autoCreateCheck.isSelected(), 
                    requireAuthCheck.isSelected(),
                    createGlobalCheck.isSelected(),
                    view.data, clusterData, 
                    clusterConnections == null ? null : clusterConnections.currentlySelected.dataAccessor, this);
            return;
        }
        
        if(src == saveButton)
        {
            view.captureGui();
            view.savePreferences();
            saveButton.setEnabled(false);
            resetButton.setEnabled(false);
            preferencesButton.setEnabled(false);
            defaultButton.setEnabled(true);
            return;
        }
        if(src == resetButton)
        {
            resetButton.setEnabled(false);
            preferencesButton.setEnabled(view.data.modified);
            defaultButton.setEnabled(true);
            view.refresh();
            return;
        }
        if(src == defaultButton)
        {
            resetButton.setEnabled(true);
            preferencesButton.setEnabled(true);
            defaultButton.setEnabled(false);
            view.setDefaults();
            return;
        }
        if(src == preferencesButton)
        {
            resetButton.setEnabled(true);
            preferencesButton.setEnabled(false);
            defaultButton.setEnabled(true);
            view.setPreferences();
            return;
        }
    }

    /* ************************************************************************
     * DocumentListener implementation
     **************************************************************************/
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        if(!painted.get())
            return;
        saveButton.setEnabled(true);
        preferencesButton.setEnabled(true);
        view.data.modified = true;
        view.captureGui();
    }
    @Override public void insertUpdate(DocumentEvent e){changedUpdate(e);}
    @Override public void removeUpdate(DocumentEvent e){changedUpdate(e);}
    
    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        connectionsPanel.refresh();
        if(clusterConnections != null)
            clusterConnectionsPanel.refresh();
    }
    @Override public void windowLostFocus(WindowEvent e){}
    
    @Override
    public void focusRequested(Object context)
    {
        if(context instanceof Throwable)
        {
            acknowledge = null;
            FrameBase.displayException(null, asManager, "Worker thread exception", "Failed to connect and/or initialize", (Throwable)context);
        }
        painted.set(true);
        windowGainedFocus(null);
        if(acknowledge != null)
            JOptionPane.showMessageDialog(asManager, acknowledge);
        acknowledge = null;
    }
    
    public class ViewStorageData
    {
        private final DomainData data;
        
        public ViewStorageData(DomainData data)
        {
            this.data = data;
        }
        
        public void savePreferences()
        {
            data.prefDomain = domainField.getText();
            data.prefHubOid = hubNameField.getText();
            data.prefTablespace = tablespaceField.getText();
            data.prefAutoCreate = autoCreateCheck.isSelected();
            data.prefRequireAuth = requireAuthCheck.isSelected();
            data.pref.putBoolean(DomainData.AutoCreateKey, data.prefAutoCreate);
            data.prefManagerOid = managerNameField.getText();
            data.prefManagerPassword = managerPasswordField.getText();
            data.prefManagerPreshared = managerPresharedField.getText();
            switch(data.type)
            {
                case Cluster:
                    data.prefRouterOid = routerNameField.getText();
                    data.prefRouterPassword = routerPasswordField.getText();
                    data.prefRouterPreshared = routerPresharedField.getText();
                    data.prefCtrouterOid = ctrouterNameField.getText();
                    data.prefCtrouterPassword = ctrouterPasswordField.getText();
                    data.prefCtrouterPreshared = ctrouterPresharedField.getText();
                    data.pref.put("cluster"+DomainData.DomainNameKey, data.prefDomain);
                    data.pref.put("cluster"+DomainData.HubKey, data.prefHubOid);
                    data.pref.put(DomainData.RouterNameKey, data.prefRouterOid);
                    data.pref.put(DomainData.RouterPasswordKey, data.prefRouterPassword);
                    data.pref.put(DomainData.RouterPresharedKey, data.prefRouterPreshared);
                    data.pref.put(DomainData.CtRouterNameKey, data.prefCtrouterOid);
                    data.pref.put(DomainData.CtRouterPasswordKey, data.prefCtrouterPassword);
                    data.pref.put(DomainData.CtRouterPresharedKey, data.prefCtrouterPreshared);
                    data.pref.put("cluster"+DomainData.ManagerNameKey, data.prefManagerOid);
                    data.pref.put("cluster"+DomainData.ManagerPasswordKey, data.prefManagerPassword);
                    data.pref.put("cluster"+DomainData.ManagerPresharedKey, data.prefManagerPreshared);
                    break;
                case Factory:
                    data.pref.put("factory"+DomainData.DomainNameKey, data.prefDomain);
                    data.pref.put("factory"+DomainData.HubKey, data.prefHubOid);
                    data.pref.put("factorycluster"+DomainData.ManagerNameKey, data.prefManagerOid);
                    data.pref.put("factory"+DomainData.ManagerPasswordKey, data.prefManagerPassword);
                    data.pref.put("factory"+DomainData.ManagerPresharedKey, data.prefManagerPreshared);
                    break;
                case None:
                    break;
                case Res:
                    data.pref.put("res"+DomainData.DomainNameKey, data.prefDomain);
                    data.pref.put("res"+DomainData.HubKey, data.prefHubOid);
                    data.pref.put("res"+DomainData.ManagerNameKey, data.prefManagerOid);
                    data.pref.put("res"+DomainData.ManagerPasswordKey, data.prefManagerPassword);
                    data.pref.get("res"+DomainData.ManagerPresharedKey, data.prefManagerPreshared);
                    break;
                case Battery:
                    data.pref.put("battery"+DomainData.DomainNameKey, data.prefDomain);
                    data.pref.put("battery"+DomainData.HubKey, data.prefHubOid);
                    data.pref.put("battery"+DomainData.ManagerNameKey, data.prefManagerOid);
                    data.pref.put("battery"+DomainData.ManagerPasswordKey, data.prefManagerPassword);
                    data.pref.get("battery"+DomainData.ManagerPresharedKey, data.prefManagerPreshared);
                    break;
                case Ancillary:
                    data.pref.put("ancillary"+DomainData.DomainNameKey, data.prefDomain);
                    data.pref.put("ancillary"+DomainData.HubKey, data.prefHubOid);
                    data.pref.put("ancillary"+DomainData.ManagerNameKey, data.prefManagerOid);
                    data.pref.put("ancillary"+DomainData.ManagerPasswordKey, data.prefManagerPassword);
                    data.pref.get("ancillary"+DomainData.ManagerPresharedKey, data.prefManagerPreshared);
                    break;
                case Rg:
                    data.pref.put("rg"+DomainData.DomainNameKey, data.prefDomain);
                    break;
            }
        }
        
        public void setDefaults()
        {
            painted.set(false);
            autoCreateCheck.setSelected(true);
            autoCreateCheck.setSelected(data.defaultRequireAuth);
            domainField.setText(data.defaultDomain); 
            hubNameField.setText(data.defaultHubOid);
            tablespaceField.setText(data.defaultTablespace);
            routerNameField.setText(data.defaultRouterOid);
            routerPasswordField.setText("");
            routerPresharedField.setText(data.defaultRouterPreshared);
            ctrouterNameField.setText(data.defaultCtrouterOid);
            ctrouterPasswordField.setText("");
            ctrouterPresharedField.setText(data.defaultCtrouterPreshared);
            managerNameField.setText(data.defaultManagerOid);
            managerPasswordField.setText("");
            managerPresharedField.setText(data.defaultManagerPreshared);
            manageButton.setEnabled(false);
            asManager.swapAndSetFocus(DomainPanel.this, domainField);
            return;
        }
        
        public void setPreferences()
        {
            painted.set(false);
            autoCreateCheck.setSelected(data.prefAutoCreate);
            requireAuthCheck.setSelected(data.prefRequireAuth);
            domainField.setText(data.prefDomain); 
            hubNameField.setText(data.prefHubOid);
            tablespaceField.setText(data.prefTablespace);
            routerNameField.setText(data.prefRouterOid);
            routerPasswordField.setText(data.prefRouterPassword);
            routerPresharedField.setText(data.prefRouterPreshared);
            ctrouterNameField.setText(data.prefCtrouterOid);
            ctrouterPasswordField.setText(data.prefCtrouterPassword);
            ctrouterPresharedField.setText(data.prefCtrouterPreshared);
            managerNameField.setText(data.prefManagerOid);
            managerPasswordField.setText(data.prefManagerPassword);
            managerPresharedField.setText(data.prefManagerPreshared);
            asManager.swapAndSetFocus(DomainPanel.this, domainField);
        }
        
        public void refresh()
        {
            painted.set(false);
            autoCreateCheck.setSelected(isAutoCreate());
            requireAuthCheck.setSelected(isRequireAuth());
            domainField.setText(getDomain()); 
            hubNameField.setText(getHubOid());
            tablespaceField.setText(getTablespace());
            routerNameField.setText(getRouterOid());
            routerPasswordField.setText(getRouterPassword());
            routerPresharedField.setText(getRouterPreshared());
            ctrouterNameField.setText(getCtRouterOid());
            ctrouterPasswordField.setText(getCtRouterPassword());
            ctrouterPresharedField.setText(getCtrouterPreshared());
            managerNameField.setText(getManagerOid());
            managerPasswordField.setText(getManagerPassword());
            managerPresharedField.setText(getManagerPreshared());
            saveButton.setEnabled(data.modified);
            resetButton.setEnabled(false);
            defaultButton.setEnabled(true);
            preferencesButton.setEnabled(data.modified);
            if(autoCreateCheck.isSelected())
                initializeButton.setEnabled(true);
            else
                if(!connectionsPanel.isSelectionConnected())
                    initializeButton.setEnabled(false);
            asManager.swapAndSetFocus(DomainPanel.this, domainField);
        }
        
        public void captureGui()
        {
            data.guiAutoCreate = autoCreateCheck.isSelected();
            data.guiRequireAuth = requireAuthCheck.isSelected();
            data.guiDomain = domainField.getText(); 
            data.guiHubOid = hubNameField.getText();
            data.guiTablespace = tablespaceField.getText();
            data.guiRouterOid = routerNameField.getText();
            data.guiRouterPassword = routerPasswordField.getText();
            data.guiRouterPreshared = routerPresharedField.getText();
            data.guiCtrouterOid = ctrouterNameField.getText();
            data.guiCtrouterPassword = ctrouterPasswordField.getText();
            data.guiCtrouterPreshared = ctrouterPresharedField.getText();
            data.guiManagerOid = managerNameField.getText();
            data.guiManagerPassword = managerPasswordField.getText();
            data.guiManagerPreshared = managerPresharedField.getText();
        }
        
        public boolean isRequireAuth()
        {
            return isRequireAuth(false);
        }        
        
        public boolean isRequireAuth(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiRequireAuth;
            return data.defaultRequireAuth;
        }
        public boolean isAutoCreate()
        {
            return isAutoCreate(false);
        }        
        
        public boolean isAutoCreate(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiAutoCreate;
            return data.defaultAutoCreate;
        }
        public String getDomain()
        {
            return getDomain(false);
        }        
        
        public String getDomain(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiDomain;
            if(data.prefDomain == null)
                return data.defaultDomain;
            return data.prefDomain;
        }
        public String getHubOid()
        {
            return getHubOid(false);
        }        
        
        public String getHubOid(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiHubOid;
            if(data.prefHubOid == null)
                return data.defaultHubOid;
            return data.prefHubOid;
        }
        public String getTablespace()
        {
            return getTablespace(false);
        }        
        
        public String getTablespace(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiTablespace;
            if(data.prefTablespace == null)
                return data.defaultTablespace;
            return data.prefTablespace;
        }
        
        public String getRouterOid()
        {
            return getRouterOid(false);
        }        
        
        public String getRouterOid(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiRouterOid;
            if(data.prefRouterOid == null)
                return data.defaultRouterOid;
            return data.prefRouterOid;
        }
        
        public String getRouterPassword()
        {
            return getRouterPassword(false);
        }        
        
        public String getRouterPassword(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiRouterPassword;
            if(data.prefRouterPassword == null)
                return data.defaultRouterPassword;
            return data.prefRouterPassword;
        }
        
        public String getRouterPreshared()
        {
            return getRouterPreshared(false);
        }        
        
        public String getRouterPreshared(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiRouterPreshared;
            if(data.prefRouterPreshared == null)
                return data.defaultRouterPreshared;
            return data.prefRouterPreshared;
        }
        
        public String getCtRouterOid()
        {
            return getCtRouterOid(false);
        }        
        
        public String getCtRouterOid(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiCtrouterOid;
            if(data.prefCtrouterOid == null)
                return data.defaultCtrouterOid;
            return data.prefCtrouterOid;
        }
        
        public String getCtRouterPassword()
        {
            return getCtRouterPassword(false);
        }        
        
        public String getCtRouterPassword(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiCtrouterPassword;
            if(data.prefCtrouterPassword == null)
                return data.defaultCtrouterPassword;
            return data.prefCtrouterPassword;
        }
        
        public String getCtrouterPreshared()
        {
            return getCtrouterPreshared(false);
        }        
        
        public String getCtrouterPreshared(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiCtrouterPreshared;
            if(data.prefCtrouterPreshared == null)
                return data.defaultCtrouterPreshared;
            return data.prefCtrouterPreshared;
        }
        
        public String getManagerOid()
        {
            return getManagerOid(false);
        }        
        
        public String getManagerOid(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiManagerOid;
            if(data.prefManagerOid == null)
                return data.defaultManagerOid;
            return data.prefManagerOid;
        }
        
        public String getManagerPassword()
        {
            return getManagerPassword(false);
        }        
        
        public String getManagerPassword(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiManagerPassword;
            if(data.prefManagerPassword == null)
                return data.defaultManagerPassword;
            return data.prefManagerPassword;
        }
        
        public String getManagerPreshared()
        {
            return getManagerPreshared(false);
        }        
        
        public String getManagerPreshared(boolean preferences)
        {
            if(data.modified && !preferences)
                return data.guiManagerPreshared;
            if(data.prefManagerPreshared == null)
                return data.defaultManagerPreshared;
            return data.prefManagerPreshared;
        }
    }
}
