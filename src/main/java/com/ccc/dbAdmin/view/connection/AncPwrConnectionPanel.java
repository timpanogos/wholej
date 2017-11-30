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
import org.emitdo.research.app.dbAdmin.DbAdmin.StorageType;
import org.emitdo.research.app.dbAdmin.DbAdmin.VendorType;
import org.emitdo.research.app.dbAdmin.model.ConnectionData;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class AncPwrConnectionPanel extends JPanel implements ActionListener, DocumentListener, SwappedFocusListener, WindowFocusListener
{
    private final int numberOfColumns = 52;
    private final DbAdmin dbAdmin;
    private final ConnectionControl control;
    private ConnectionData connection;
    
    private JRadioButton oracleAncPwrRadio; 
    private JRadioButton jdbAncPwrRadio; 
    private JRadioButton pgAncPwrRadio; 
    
    private JCheckBox oracleAncPwrCheck;
    private JCheckBox jdbAncPwrCheck;
    private JCheckBox pgAncPwrCheck;
    
    private JRadioButton jdbcRadio;
    private JRadioButton webRadio;
    
    private JTextField userField;
    private JTextField passwordField;
    private JTextField urlField;
    private JTextField daField;
    private JTextField homeField;
    
    private JButton saveButton;
    private JButton resetButton;
    private JButton preferencesButton;
    private JButton defaultButton;
    
    private final AtomicBoolean painted;
    
    public AncPwrConnectionPanel(DbAdmin dbAdmin)
    {
        this.dbAdmin = dbAdmin;
        control = dbAdmin.getConnectionControl();
        painted = new AtomicBoolean(false);
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder("Ancillary Power Connections"));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Oracle Connections"));
        
        AtomicInteger maxWidth = new AtomicInteger();
        JLabel oracleAncPwrLabel = FrameBase.getLabel("AncPwr:", maxWidth);
        JLabel jdbAncPwrLabel = FrameBase.getLabel("AncPwr:", maxWidth);
        JLabel pgAncPwrLabel = FrameBase.getLabel("AncPwr:", maxWidth);
        
        JLabel oracleAncPwrConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel jdbAncPwrConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel pgAncPwrConnectedLabel = FrameBase.getLabel("Connected:", maxWidth);
                
        JLabel jdbcLabel = FrameBase.getLabel("JDBC:", maxWidth);
        JLabel webLabel = FrameBase.getLabel("Web:", maxWidth);
        
        JLabel userLabel = FrameBase.getLabel("User:", maxWidth);
        JLabel passwordLabel = FrameBase.getLabel("Password:", maxWidth);
        JLabel urlLabel = FrameBase.getLabel("URL:", maxWidth);
        JLabel daLabel = FrameBase.getLabel("Data accessor:", maxWidth);
        JLabel homeLabel = FrameBase.getLabel("Home:", maxWidth);
        int width = maxWidth.get();
        
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        oracleAncPwrRadio = FrameBase.addRadioButtonToPanel(oracleAncPwrLabel, false, true, width, -1, "Oracle Ancillary Power DB connection", rowPanel, this);
        oracleAncPwrCheck = FrameBase.addCheckBoxToPanel(oracleAncPwrConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(VendorType.Oracle, oracleAncPwrRadio, oracleAncPwrCheck);
        panel.add(rowPanel);
        
        ButtonGroup group = new ButtonGroup();
        group.add(oracleAncPwrRadio);

        mainPanel.add(panel);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("JavaDB Connections"));
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        jdbAncPwrRadio = FrameBase.addRadioButtonToPanel(jdbAncPwrLabel, false, true, width, -1, "JavaDB Ancillary Power DB connection", rowPanel, this);
        jdbAncPwrCheck = FrameBase.addCheckBoxToPanel(jdbAncPwrConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(VendorType.JavaDb, jdbAncPwrRadio, jdbAncPwrCheck);
        panel.add(rowPanel);
        
        group.add(jdbAncPwrRadio);
        
        mainPanel.add(panel);
        
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Postgres Connections"));
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        pgAncPwrRadio = FrameBase.addRadioButtonToPanel(pgAncPwrLabel, true, true, width, -1, "Postgres Ancillary Power DB connection", rowPanel, this);
        pgAncPwrCheck = FrameBase.addCheckBoxToPanel(pgAncPwrConnectedLabel, false, false, width, -1, "Currently connected", rowPanel);
        associateState(VendorType.Postgres, pgAncPwrRadio, pgAncPwrCheck);
        panel.add(rowPanel);
        
        group.add(pgAncPwrRadio);
        
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
        FrameBase.addTextParamToPanel(passwordLabel, passwordField, width, -1, "Enter the Oracle database users password", panel, this);
        urlField = new JTextField("", numberOfColumns);
        FrameBase.addTextParamToPanel(urlLabel, urlField, width, -1, "Enter the Oracle database URL", panel, this);
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

    private void associateState(VendorType vtype, JRadioButton radio, JCheckBox check)
    {
        ConnectionData ci = control.getConnection(StorageType.Ancillary, vtype, null, ConnType.Jdbc);
        ci.checkBox = check;
        ci.radioButton = radio;
        ci = control.getConnection(StorageType.Ancillary, vtype, null, ConnType.Web);
        ci.checkBox = check;
        ci.radioButton = radio;
    }
    
    private void setState()
    {
        for(int j=0; j < 2; j++)
        {
            VendorType vtype = VendorType.JavaDb;
            if(j == 1)
                vtype = VendorType.Oracle;
            
            ConnType ctype = ConnType.Jdbc;
            if(!jdbcRadio.isSelected())
                ctype = ConnType.Web;
            ConnectionData ci = control.getConnection(StorageType.Ancillary, vtype, null, ctype);
            ci.checkBox.setSelected(ci.connected);
            ci.radioButton.setEnabled(ci.supported);
        }
    }
    
    private void adjustForSelection()
    {
        ConnType ctype = ConnType.Jdbc;
        if(!jdbcRadio.isSelected())
            ctype = ConnType.Web;
        VendorType vtype = null;
        boolean jdb = false;
        if(oracleAncPwrRadio.isSelected()) 
        {
            vtype = VendorType.Oracle;
        }else
        if(jdbAncPwrRadio.isSelected())
        {
            vtype = VendorType.JavaDb;
            jdb = true;
        }else
        if(pgAncPwrRadio.isSelected())
        {
            vtype = VendorType.Postgres;
            jdb = false;
        }
        connection = control.getConnection(StorageType.Ancillary, vtype, null, ctype);
        painted.set(false);
        userField.setText(connection.getUser());
        passwordField.setText(connection.getPassword());
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
            urlField.setEnabled(false);
            daField.setEnabled(false);
            return;
        }
        userField.setEnabled(true);
        passwordField.setEnabled(true);
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
