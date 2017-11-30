/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.connection;

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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.emitdo.research.app.dbAdmin.ConnectionControl;
import org.emitdo.research.app.dbAdmin.DbAdmin;
import org.emitdo.research.app.dbAdmin.DbAdmin.ConnType;
import org.emitdo.research.app.dbAdmin.DbAdmin.DomainType;
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.DbAdmin.VendorType;
import org.emitdo.research.app.dbAdmin.model.ConnectionData;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class AsConnectionPanel extends JPanel implements ActionListener, DocumentListener, SwappedFocusListener, WindowFocusListener
{
    private final int numberOfColumns = 52;
    private final DbAdmin dbAdmin;
    private final ConnectionControl control;
    private ConnectionData connection;
    
    private JRadioButton oracleClusterRadio; 
    private JRadioButton oracleResRadio;
    private JRadioButton oracleFactoryRadio;
    private JRadioButton oracleRgRadio;
    private JRadioButton oracleBatteryRadio;
    private JRadioButton oracleAncillaryRadio;
    
    private JRadioButton jdbClusterRadio; 
    private JRadioButton jdbResRadio;
    private JRadioButton jdbFactoryRadio;
    private JRadioButton jdbRgRadio;
    private JRadioButton pgClusterRadio; 
    private JRadioButton pgResRadio;
    private JRadioButton pgFactoryRadio;
    private JRadioButton pgBatteryRadio;
    private JRadioButton pgAncillaryRadio;
    
    private JCheckBox oracleClusterCheck;
    private JCheckBox oracleResCheck;
    private JCheckBox oracleBatteryCheck;
    private JCheckBox oracleAncillaryCheck;
    private JCheckBox oracleFactoryCheck;
    private JCheckBox oracleRgCheck;
    private JCheckBox jdbClusterCheck;
    private JCheckBox jdbResCheck;
    private JCheckBox jdbFactoryCheck;
    private JCheckBox jdbRgCheck;
    private JCheckBox pgClusterCheck;
    private JCheckBox pgResCheck;
    private JCheckBox pgBatteryCheck;
    private JCheckBox pgAncillaryCheck;
    private JCheckBox pgFactoryCheck;
    
    private JRadioButton jdbcRadio;
    private JRadioButton webRadio;
    
    private JTextField userField;
    private JTextField passwordField;
    private JTextField userBaseField;
    private JTextField urlField;
    private JTextField daField;
    private JTextField homeField;
    
    private JButton saveButton;
    private JButton resetButton;
    private JButton preferencesButton;
    private JButton defaultButton;
    
    private final AtomicBoolean painted;
    
    public AsConnectionPanel(DbAdmin dbAdmin)
    {
        this.dbAdmin = dbAdmin;
        control = dbAdmin.getConnectionControl();
        painted = new AtomicBoolean(false);
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder("AS Connections"));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Oracle Connections"));
        
        AtomicInteger maxWidth = new AtomicInteger();
        JLabel oracleClusterLabel = FrameBase.getLabel("Cluster:", maxWidth);
        JLabel oracleResLabel = FrameBase.getLabel("Res:", maxWidth);
        JLabel oracleBatteryLabel = FrameBase.getLabel("Battery:", maxWidth);
        JLabel oracleAncillaryLabel = FrameBase.getLabel("Ancillary:", maxWidth);
        JLabel oracleFactoryLabel = FrameBase.getLabel("Factory:", maxWidth);
        JLabel oracleRgLabel = FrameBase.getLabel("Res Gateway:", maxWidth);
        JLabel jdbClusterLabel = FrameBase.getLabel("Cluster:", maxWidth);
        JLabel jdbResLabel = FrameBase.getLabel("Res:", maxWidth);
        JLabel jdbFactoryLabel = FrameBase.getLabel("Factory:", maxWidth);
        JLabel jdbRgLabel = FrameBase.getLabel("Res Gateway:", maxWidth);
        JLabel pgClusterLabel = FrameBase.getLabel("Cluster:", maxWidth);
        JLabel pgResLabel = FrameBase.getLabel("Res:", maxWidth);
        JLabel pgBatteryLabel = FrameBase.getLabel("Battery:", maxWidth);
        JLabel pgAncillaryLabel = FrameBase.getLabel("Ancillary:", maxWidth);
        JLabel pgFactoryLabel = FrameBase.getLabel("Factory:", maxWidth);
        
        JLabel oracleClusterConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel oracleResConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel oracleBatteryConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel oracleAncillaryConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel oracleFactoryConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel oracleRgConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel jdbClusterConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel jdbResConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel jdbFactoryConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel jdbRgConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel pgClusterConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel pgResConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel pgBatteryConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel pgAncillaryConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel pgFactoryConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
                
        JLabel jdbcLabel = FrameBase.getLabel("JDBC:", maxWidth);
        JLabel webLabel = FrameBase.getLabel("Web:", maxWidth);
        
        JLabel userLabel = FrameBase.getLabel("User:", maxWidth);
        JLabel userBaseLabel = FrameBase.getLabel("Base user:", maxWidth);
        JLabel passwordLabel = FrameBase.getLabel("Password:", maxWidth);
        JLabel urlLabel = FrameBase.getLabel("URL:", maxWidth);
        JLabel daLabel = FrameBase.getLabel("Data accessor:", maxWidth);
        JLabel homeLabel = FrameBase.getLabel("Home:", maxWidth);
        int width = maxWidth.get();
        
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        oracleClusterRadio = FrameBase.addRadioButtonToPanel(oracleClusterLabel, true, true, width, -1, "Oracle Cluster DB connection", rowPanel, this);
        oracleClusterCheck = FrameBase.addCheckBoxToPanel(oracleClusterConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Cluster, VendorType.Oracle, oracleClusterRadio, oracleClusterCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        oracleResRadio = FrameBase.addRadioButtonToPanel(oracleResLabel, false, true, width, -1, "Oracle Res DB connection", rowPanel, this);
        oracleResCheck = FrameBase.addCheckBoxToPanel(oracleResConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Res, VendorType.Oracle, oracleResRadio, oracleResCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        oracleBatteryRadio = FrameBase.addRadioButtonToPanel(oracleBatteryLabel, false, true, width, -1, "Oracle Battery DB connection", rowPanel, this);
        oracleBatteryCheck = FrameBase.addCheckBoxToPanel(oracleBatteryConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Battery, VendorType.Oracle, oracleBatteryRadio, oracleBatteryCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        oracleAncillaryRadio = FrameBase.addRadioButtonToPanel(oracleAncillaryLabel, false, true, width, -1, "Oracle Ancillary DB connection", rowPanel, this);
        oracleAncillaryCheck = FrameBase.addCheckBoxToPanel(oracleAncillaryConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Ancillary, VendorType.Oracle, oracleAncillaryRadio, oracleAncillaryCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        oracleFactoryRadio = FrameBase.addRadioButtonToPanel(oracleFactoryLabel, false, true, width, -1, "Oracle Factory DB connection", rowPanel, this);
        oracleFactoryCheck = FrameBase.addCheckBoxToPanel(oracleFactoryConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Factory, VendorType.Oracle, oracleFactoryRadio, oracleFactoryCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        oracleRgRadio = FrameBase.addRadioButtonToPanel(oracleRgLabel, false, true, width, -1, "Oracle Residential Gateway DB connection", rowPanel, this);
        oracleRgCheck = FrameBase.addCheckBoxToPanel(oracleRgConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Rg, VendorType.Oracle, oracleRgRadio, oracleRgCheck);
        panel.add(rowPanel);
        
        ButtonGroup group = new ButtonGroup();
        group.add(oracleClusterRadio);
        group.add(oracleResRadio);
        group.add(oracleBatteryRadio);
        group.add(oracleAncillaryRadio);
        group.add(oracleFactoryRadio);
        group.add(oracleRgRadio);

        mainPanel.add(panel);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("JavaDB Connections"));
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        jdbClusterRadio = FrameBase.addRadioButtonToPanel(jdbClusterLabel, false, true, width, -1, "JavaDB Cluster DB connection", rowPanel, this);
        jdbClusterCheck = FrameBase.addCheckBoxToPanel(jdbClusterConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Cluster, VendorType.JavaDb, jdbClusterRadio, jdbClusterCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        jdbResRadio = FrameBase.addRadioButtonToPanel(jdbResLabel, false, true, width, -1, "JavaDB Res DB connection", rowPanel, this);
        jdbResCheck = FrameBase.addCheckBoxToPanel(jdbResConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Res, VendorType.JavaDb, jdbResRadio, jdbResCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        jdbFactoryRadio = FrameBase.addRadioButtonToPanel(jdbFactoryLabel, false, true, width, -1, "JavaDB Factory DB connection", rowPanel, this);
        jdbFactoryCheck = FrameBase.addCheckBoxToPanel(jdbFactoryConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Factory, VendorType.JavaDb, jdbFactoryRadio, jdbFactoryCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        jdbRgRadio = FrameBase.addRadioButtonToPanel(jdbRgLabel, false, true, width, -1, "JavaDB Residential Gateway DB connection", rowPanel, this);
        jdbRgCheck = FrameBase.addCheckBoxToPanel(jdbRgConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Rg, VendorType.JavaDb, jdbRgRadio, jdbRgCheck);
        panel.add(rowPanel);
        
        group.add(jdbClusterRadio);
        group.add(jdbResRadio);
        group.add(jdbResRadio);
        group.add(jdbFactoryRadio);
        group.add(jdbRgRadio);
        
        mainPanel.add(panel);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Postgres Connections"));
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        pgClusterRadio = FrameBase.addRadioButtonToPanel(pgClusterLabel, false, true, width, -1, "Postgres Cluster DB connection", rowPanel, this);
        pgClusterCheck = FrameBase.addCheckBoxToPanel(pgClusterConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Cluster, VendorType.Postgres, pgClusterRadio, pgClusterCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        pgResRadio = FrameBase.addRadioButtonToPanel(pgResLabel, false, true, width, -1, "Postgres Res DB connection", rowPanel, this);
        pgResCheck = FrameBase.addCheckBoxToPanel(pgResConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Res, VendorType.Postgres, pgResRadio, pgResCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        pgBatteryRadio = FrameBase.addRadioButtonToPanel(pgBatteryLabel, false, true, width, -1, "Postgres Battery DB connection", rowPanel, this);
        pgBatteryCheck = FrameBase.addCheckBoxToPanel(pgBatteryConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Battery, VendorType.Postgres, pgBatteryRadio, pgBatteryCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        pgAncillaryRadio = FrameBase.addRadioButtonToPanel(pgAncillaryLabel, false, true, width, -1, "Postgres Ancillary DB connection", rowPanel, this);
        pgAncillaryCheck = FrameBase.addCheckBoxToPanel(pgAncillaryConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Ancillary, VendorType.Postgres, pgAncillaryRadio, pgAncillaryCheck);
        panel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        pgFactoryRadio = FrameBase.addRadioButtonToPanel(pgFactoryLabel, false, true, width, -1, "Postgres Factory DB connection", rowPanel, this);
        pgFactoryCheck = FrameBase.addCheckBoxToPanel(pgFactoryConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(DomainType.Factory, VendorType.Postgres, pgFactoryRadio, pgFactoryCheck);
        panel.add(rowPanel);
        
        group.add(pgClusterRadio);
        group.add(pgResRadio);
        group.add(pgBatteryRadio);
        group.add(pgAncillaryRadio);
        group.add(pgFactoryRadio);
        
        mainPanel.add(panel);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Connectivity type"));

        jdbcRadio = FrameBase.addRadioButtonToPanel(jdbcLabel, true, true, width, -1, "Use JDBC connectivity", panel, this);
        webRadio = FrameBase.addRadioButtonToPanel(webLabel, false, true, width, -1, "Use web services connectivity", panel, this);
        group = new ButtonGroup();
        group.add(jdbcRadio);
        group.add(webRadio);
        mainPanel.add(panel);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Connection information"));
        
        userField = new JTextField("", numberOfColumns);
        FrameBase.addTextParamToPanel(userLabel, userField, width, -1, "Enter the connection user", panel, this);
        passwordField = new JTextField("", numberOfColumns);
        FrameBase.addTextParamToPanel(passwordLabel, passwordField, width, -1, "Enter the database users password", panel, this);
        userBaseField = new JTextField("", numberOfColumns);
        FrameBase.addTextParamToPanel(userBaseLabel, userBaseField, width, -1, "Enter the user base, _user, _manager, _admin will be created.", panel, this);
        urlField = new JTextField("", numberOfColumns);
        FrameBase.addTextParamToPanel(urlLabel, urlField, width, -1, "Enter the database URL", panel, this);
        daField = new JTextField("", numberOfColumns);
        FrameBase.addTextParamToPanel(daLabel, daField, width, -1, "Enter the Java Data Accessor class name for this connection", panel, this);
        homeField = new JTextField("", numberOfColumns);
        FrameBase.addTextParamToPanel(homeLabel, homeField, width, -1, "For Auto Create only, Enter the file system path where the JavaDB is to be created", panel, this);
        mainPanel.add(panel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        saveButton = FrameBase.addButtonToPanel("Save", false, "Save the current values to user preferences", buttonPanel, this);
        resetButton = FrameBase.addButtonToPanel("Reset", false, "Reset to the previous state", buttonPanel, this);
        preferencesButton = FrameBase.addButtonToPanel("Preferences", false, "Set all fields to thier current preference values", buttonPanel, this);
        defaultButton = FrameBase.addButtonToPanel("Default", false, "Set all fields to thier default values", buttonPanel, this);
        
        mainPanel.add(buttonPanel);
        
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dataPanel.add(mainPanel);
        
        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(575, 520));
        add(scrollPane, BorderLayout.CENTER);
        setState();
        adjustForSelection();
        
        dbAdmin.swapAndSetFocus(this, userField);
    }

    public void refresh()
    {
        setState();
        adjustForSelection();
    }
    
    @Override
    public void focusRequested(Object context)
    {
        painted.set(true);
    }

    private void associateState(DomainType dtype, VendorType vtype, JRadioButton radio, JCheckBox check)
    {
        ConnectionData ci = control.getConnection(StorageType.As, vtype, dtype, ConnType.Jdbc);
        ci.checkBox = check;
        ci.radioButton = radio;
        ci = control.getConnection(StorageType.As, vtype, dtype, ConnType.Web);
        ci.checkBox = check;
        ci.radioButton = radio;
    }
    
    private void setState()
    {
        for(int i=1; i < 5; i++)
        {
            DomainType dtype = DomainType.getType(i);
            for(int j=0; j < 2; j++)
            {
                VendorType vtype = VendorType.JavaDb;
                if(j == 1)
                    vtype = VendorType.Oracle;
                
                ConnType ctype = ConnType.Jdbc;
                if(!jdbcRadio.isSelected())
                    ctype = ConnType.Web;
                ConnectionData ci = control.getConnection(StorageType.As, vtype, dtype, ctype);
                ci.checkBox.setSelected(ci.connected);
                ci.radioButton.setEnabled(ci.supported);
            }
        }
    }
    
    private void adjustForSelection()
    {
        ConnType ctype = ConnType.Jdbc;
        if(!jdbcRadio.isSelected())
            ctype = ConnType.Web;
        VendorType vtype = null;
        DomainType dtype = null;
        boolean jdb = false;
        if(oracleClusterRadio.isSelected()) 
        {
            vtype = VendorType.Oracle;
            dtype = DomainType.Cluster;
        }else
        if(oracleResRadio.isSelected())
        {
            vtype = VendorType.Oracle;
            dtype = DomainType.Res;
        }else
        if(oracleBatteryRadio.isSelected())
        {
             vtype = VendorType.Oracle;
             dtype = DomainType.Battery;
        }else
        if(oracleAncillaryRadio.isSelected())
        {
            vtype = VendorType.Oracle;
            dtype = DomainType.Ancillary;
        }else
        if(oracleFactoryRadio.isSelected())
        {
            vtype = VendorType.Oracle;
            dtype = DomainType.Factory;
        }else
        if(oracleRgRadio.isSelected())
        {
            vtype = VendorType.Oracle;
            dtype = DomainType.Rg;
        }else
        if(jdbClusterRadio.isSelected())
        {
            vtype = VendorType.JavaDb;
            dtype = DomainType.Cluster;
            jdb = true;
        }else
        if(jdbResRadio.isSelected())
        {
            vtype = VendorType.JavaDb;
            dtype = DomainType.Res;
            jdb = true;
        }else
        if(jdbFactoryRadio.isSelected())
        {
            vtype = VendorType.JavaDb;
            dtype = DomainType.Factory;
            jdb = true;
        }else
        if(jdbRgRadio.isSelected())
        {
            vtype = VendorType.JavaDb;
            dtype = DomainType.Rg;
            jdb = true;
        }else
        if(pgClusterRadio.isSelected())
        {
            vtype = VendorType.Postgres;
            dtype = DomainType.Cluster;
            jdb = false;
        }else
        if(pgResRadio.isSelected())
        {
            vtype = VendorType.Postgres;
            dtype = DomainType.Res;
            jdb = false;
        }else
        if(pgBatteryRadio.isSelected())
        {
            vtype = VendorType.Postgres;
            dtype = DomainType.Battery;
            jdb = false;
        }else
        if(pgAncillaryRadio.isSelected())
        {
            vtype = VendorType.Postgres;
            dtype = DomainType.Ancillary;
            jdb = false;
        }else
        if(pgFactoryRadio.isSelected())
        {
            vtype = VendorType.Postgres;
            dtype = DomainType.Factory;
            jdb = false;
        }
        connection = control.getConnection(StorageType.As, vtype, dtype, ctype);
        painted.set(false);
        userField.setText(connection.getUser());
        passwordField.setText(connection.getPassword());
        userBaseField.setText(connection.getUserBase());
        urlField.setText(connection.getUrl());
        daField.setText(connection.getDataAccessor());
        homeField.setText(connection.getHome());
        setState();
        homeField.setEditable(jdb ? true : false);
        dbAdmin.swapAndSetFocus(this, userField);
        if(!connection.supported)
        {
            saveButton.setEnabled(false);
            resetButton.setEnabled(false);
            defaultButton.setEnabled(false);
            preferencesButton.setEnabled(false);
            
            userField.setEnabled(false);
            passwordField.setEnabled(false);
            userBaseField.setEnabled(false);
            urlField.setEnabled(false);
            daField.setEnabled(false);
            return;
        }
        userField.setEnabled(true);
        passwordField.setEnabled(true);
        userBaseField.setEnabled(true);
        urlField.setEnabled(true);
        daField.setEnabled(true);
        homeField.setEnabled(true);
        saveButton.setEnabled(connection.modified);
        resetButton.setEnabled(false);
        defaultButton.setEnabled(true);
        preferencesButton.setEnabled(connection.modified);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if(src instanceof JRadioButton)
        {
            adjustForSelection();
            return;
        }
        if(src == saveButton)
        {
            connection.guiUser = userField.getText();
            connection.guiPassword = passwordField.getText();
            connection.guiUserBase = userBaseField.getText();
            connection.guiUrl = urlField.getText();
            connection.guiDataAccessor = daField.getText();
            connection.guiHome = homeField.getText();
            connection.savePreferences();
            saveButton.setEnabled(false);
            resetButton.setEnabled(false);
            preferencesButton.setEnabled(false);
            defaultButton.setEnabled(true);
            return;
        }
        if(src == resetButton)
        {
            resetButton.setEnabled(false);
            preferencesButton.setEnabled(connection.modified);
            defaultButton.setEnabled(true);
            adjustForSelection();
            return;
        }
        if(src == defaultButton)
        {
            painted.set(false);
            resetButton.setEnabled(true);
            preferencesButton.setEnabled(true);
            defaultButton.setEnabled(false);
            userField.setText(connection.defaultUser);
            passwordField.setText(connection.defaultPassword);
            userBaseField.setText(connection.defaultUserBase);
            urlField.setText(connection.defaultUrl);
            daField.setText(connection.defaultDataAccessor);
            homeField.setText(connection.defaultHome);
            dbAdmin.swapAndSetFocus(this, userField);
            return;
        }
        if(src == preferencesButton)
        {
            painted.set(false);
            resetButton.setEnabled(true);
            preferencesButton.setEnabled(false);
            defaultButton.setEnabled(true);
            userField.setText(connection.prefUser);
            passwordField.setText(connection.prefPassword);
            userBaseField.setText(connection.prefUserBase);
            urlField.setText(connection.prefUrl);
            daField.setText(connection.prefDataAccessor);
            homeField.setText(connection.prefHome);
            dbAdmin.swapAndSetFocus(this, userField);
            return;
        }
    }
    
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        if(!painted.get())
            return;
        saveButton.setEnabled(true);
        preferencesButton.setEnabled(true);
        connection.modified = true;
        connection.guiUser = userField.getText();
        connection.guiPassword = passwordField.getText();
        connection.guiUserBase = userBaseField.getText();
        connection.guiUrl = urlField.getText();
        connection.guiDataAccessor = daField.getText();
        connection.guiHome = homeField.getText();
    }
    @Override public void insertUpdate(DocumentEvent e){changedUpdate(null);}
    @Override public void removeUpdate(DocumentEvent e){changedUpdate(null);}

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        refresh();
    }

    @Override
    public void windowLostFocus(WindowEvent e)
    {
    }
}
