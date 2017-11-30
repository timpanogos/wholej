/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.emitdo.research.app.dbAdmin.ConnectionControl;
import org.emitdo.research.app.dbAdmin.model.ConnectionData;
import org.emitdo.research.app.dbAdmin.model.ConnectionsData;
import org.emitdo.research.app.dbAdmin.model.DomainData;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class AvailableConnectionsPanel extends JPanel implements ActionListener, SwappedFocusListener
{
    private final int numberOfColumns = 52;
    private final ConnectionsData connections;
    private JRadioButton oracleJdbcRadio; 
    private JRadioButton oracleWebRadio;
    private JRadioButton jdbJdbcRadio;
    private JRadioButton jdbWebRadio;
    private JRadioButton pgJdbcRadio;
    private JRadioButton pgWebRadio;
    
    private JCheckBox oracleJdbcCheck;
    private JCheckBox oracleWebCheck;
    private JCheckBox jdbJdbcCheck;
    private JCheckBox jdbWebCheck;
    private JCheckBox pgJdbcCheck;
    private JCheckBox pgWebCheck;
    
    private JTextField userField;
    private JTextField passwordField;
    private JTextField urlField;
    private JTextField daField;
    private JTextField clusterDomainField;
    
    private JButton connectButton;
    private JButton disconnectButton;
    private final ActionListener callersActionListener;
    private final DomainData clusterData;
    private final boolean supportsOracle;
    private final boolean supportsJdb;
    private final boolean supportsPg;
    
    public AvailableConnectionsPanel(
            ConnectionsData connections, String title, 
            boolean allowConnect, boolean supportsOracle, 
            boolean supportsJdb, boolean supportsPg, DomainData clusterData,
            ActionListener actionListener)
    {
        this.connections = connections;
        this.callersActionListener = actionListener;
        this.clusterData = clusterData;
        this.supportsOracle = supportsOracle;
        this.supportsJdb = supportsJdb;
        this.supportsPg = supportsPg;
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder(title));
        
        AtomicInteger maxWidth = new AtomicInteger();
        JLabel oracleJdbcLabel = FrameBase.getLabel("Oracle JDBC:", maxWidth);
        JLabel oracleWebLabel = FrameBase.getLabel("Oracle Web:", maxWidth);
        JLabel jdbJdbcLabel = FrameBase.getLabel("JavaDB JDBC:", maxWidth);
        JLabel jdbWebLabel = FrameBase.getLabel("JavaDB Web:", maxWidth);
        JLabel pgJdbcLabel = FrameBase.getLabel("Postgres JDBC:", maxWidth);
        JLabel pgWebLabel = FrameBase.getLabel("Postgress Web:", maxWidth);
        JLabel oracleJdbcConnLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel oracleWebConnLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel jdbJdbcConnLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel jdbWebConnLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel pgJdbcConnLabel = FrameBase.getLabel("Connected:", maxWidth);
        JLabel pgWebConnLabel = FrameBase.getLabel("Connected:", maxWidth);
        
        JLabel userLabel = FrameBase.getLabel("User:", maxWidth);
        JLabel passwordLabel = FrameBase.getLabel("Password:", maxWidth);
        JLabel urlLabel = FrameBase.getLabel("URL:", maxWidth);
        JLabel daLabel = FrameBase.getLabel("Data accessor:", maxWidth);
        JLabel clusterDomainLabel = FrameBase.getLabel("Cluster domain:", maxWidth);
        int width = maxWidth.get();
        
        ButtonGroup group = new ButtonGroup();
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        
        boolean firstSelect = true;
        if(supportsJdb)
        {
            jdbJdbcRadio = FrameBase.addRadioButtonToPanel(jdbJdbcLabel, firstSelect, true, width, -1, "JavaDB via JDBC connection", rowPanel, this);
            firstSelect = false;
            jdbJdbcCheck = FrameBase.addCheckBoxToPanel(jdbJdbcConnLabel, connections.jdbJdbc.connected, false, width, -1, "Currently connected", rowPanel);
            connections.jdbJdbc.checkBox = jdbJdbcCheck;
            connections.jdbJdbc.radioButton = jdbJdbcRadio;
            panel.add(rowPanel);
            
            rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            jdbWebRadio = FrameBase.addRadioButtonToPanel(jdbWebLabel, false, true, width, -1, "JavaDB via Web Services", rowPanel, this);
            jdbWebCheck = FrameBase.addCheckBoxToPanel(jdbWebConnLabel, connections.jdbWeb.connected, false, width, -1, "Currently connected", rowPanel);
            connections.jdbWeb.checkBox = jdbWebCheck;
            connections.jdbWeb.radioButton = jdbWebRadio;
            panel.add(rowPanel);
            group.add(jdbJdbcRadio);
            group.add(jdbWebRadio);
        }
        
        if(supportsPg)
        {
            rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            pgJdbcRadio = FrameBase.addRadioButtonToPanel(pgJdbcLabel, firstSelect, true, width, -1, "JavaDB via JDBC connection", rowPanel, this);
            firstSelect = false;
            pgJdbcCheck = FrameBase.addCheckBoxToPanel(pgJdbcConnLabel, connections.pgJdbc.connected, false, width, -1, "Currently connected", rowPanel);
            connections.pgJdbc.checkBox = pgJdbcCheck;
            connections.pgJdbc.radioButton = pgJdbcRadio;
            panel.add(rowPanel);
            
            rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            pgWebRadio = FrameBase.addRadioButtonToPanel(pgWebLabel, false, true, width, -1, "JavaDB via Web Services", rowPanel, this);
            pgWebCheck = FrameBase.addCheckBoxToPanel(pgWebConnLabel, connections.pgWeb.connected, false, width, -1, "Currently connected", rowPanel);
            connections.pgWeb.checkBox = pgWebCheck;
            connections.pgWeb.radioButton = pgWebRadio;
            panel.add(rowPanel);
            group.add(pgJdbcRadio);
            group.add(pgWebRadio);
        }
        
        if(supportsOracle)
        {
            rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            oracleJdbcRadio = FrameBase.addRadioButtonToPanel(oracleJdbcLabel, firstSelect, true, width, -1, "Oracle via JDBC connection", rowPanel, this);
            oracleJdbcCheck = FrameBase.addCheckBoxToPanel(oracleJdbcConnLabel, false, false, width, -1, "Currently connected", rowPanel);
            connections.oracleJdbc.checkBox = oracleJdbcCheck;
            connections.oracleJdbc.radioButton = oracleJdbcRadio;
            panel.add(rowPanel);
            
            rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            oracleWebRadio = FrameBase.addRadioButtonToPanel(oracleWebLabel, false, true, width, -1, "Oracle via Web Services connection", rowPanel, this);
            oracleWebCheck = FrameBase.addCheckBoxToPanel(oracleWebConnLabel, false, false, width, -1, "Currently connected", rowPanel);
            connections.oracleWeb.checkBox = oracleWebCheck;
            connections.oracleWeb.radioButton = oracleWebRadio;
            panel.add(rowPanel);
            group.add(oracleJdbcRadio);
            group.add(oracleWebRadio);
        }

        if(allowConnect)
        {
            JPanel connpanel = new JPanel();
            connpanel.setLayout(new BoxLayout(connpanel, BoxLayout.Y_AXIS));
            connpanel.setBorder(new TitledBorder("Connection information"));
            
            userField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(userLabel, userField, width, -1, "The connection user", connpanel);
            userField.setEditable(false);
            passwordField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(passwordLabel, passwordField, width, -1, "The connection password", connpanel);
            passwordField.setEditable(false);
            urlField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(urlLabel, urlField, width, -1, "The connection URL", connpanel);
            urlField.setEditable(false);
            daField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(daLabel, daField, width, -1, "The Data Accessor to be used", connpanel);
            daField.setEditable(false);
            panel.add(connpanel);
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(true);
            connectButton = FrameBase.addButtonToPanel(ConnectionControl.ConnectButtonLabel, false, "Connect to the selected database", buttonPanel, this);
            disconnectButton = FrameBase.addButtonToPanel(ConnectionControl.DisconnectButtonLabel, false, "Disconnect from the selected database", buttonPanel, this);
            panel.add(buttonPanel);
        }else
        {
            clusterDomainField = new JTextField(clusterData.getDomain(), numberOfColumns);
            FrameBase.addTextParamToPanel(clusterDomainLabel, clusterDomainField, width, -1, "The Cluster domain Initialize will associate this domain with", panel);
            clusterDomainField.setEditable(false);
        }
        
        add(panel);
        setState();
        adjustForSelection();
    }

    public void refresh()
    {
        setState();
        ConnectedEvent event = adjustForSelection();
        if(callersActionListener != null)
            callersActionListener.actionPerformed(event);
    }
    
    public boolean isSelectionConnected()
    {
        adjustForSelection();
        return connections.currentlySelected.connected;
    }
    
    private void setState()
    {
        ConnectionData ci = null;
        if(supportsJdb)
        {
            ci = connections.jdbJdbc; 
            ci.checkBox.setSelected(ci.connected);
            ci.radioButton.setEnabled(ci.supported);
            ci = connections.jdbWeb; 
            ci.checkBox.setSelected(ci.connected);
            ci.radioButton.setEnabled(ci.supported);
        }
        if(supportsOracle)
        {
            ci = connections.oracleJdbc; 
            ci.checkBox.setSelected(ci.connected);
            ci.radioButton.setEnabled(ci.supported);
            ci = connections.oracleWeb; 
            ci.checkBox.setSelected(ci.connected);
            ci.radioButton.setEnabled(ci.supported);
        }
        if(supportsPg)
        {
            ci = connections.pgJdbc; 
            ci.checkBox.setSelected(ci.connected);
            ci.radioButton.setEnabled(ci.supported);
            ci = connections.pgWeb; 
            ci.checkBox.setSelected(ci.connected);
            ci.radioButton.setEnabled(ci.supported);
        }
    }
    
    private ConnectedEvent adjustForSelection()
    {
        ConnectionData selected = null;
        if(supportsJdb)
        {
            if(jdbJdbcRadio.isSelected()) 
                selected = connections.jdbJdbc;
            else if(jdbWebRadio.isSelected())
                selected = connections.jdbWeb;
        }
        if(supportsOracle)
        {
            if(oracleJdbcRadio.isSelected())
                selected = connections.oracleJdbc;
            else if(oracleWebRadio.isSelected())
                selected = connections.oracleWeb;
        }
        if(supportsPg)
        {
            if(pgJdbcRadio.isSelected()) 
                selected = connections.pgJdbc;
            else if(pgWebRadio.isSelected())
                selected = connections.pgWeb;
        }
        if(selected == null)
            return null;
        connections.currentlySelected = selected;
        setState();
        if(userField == null)
            return new ConnectedEvent(this, ConnectedEvent.NotConnected, ConnectionControl.ConnectedActionCommand);
        userField.setText(selected.getUser());
        passwordField.setText(selected.getPassword());
        urlField.setText(selected.getUrl());
        daField.setText(selected.getDataAccessor());
        if(clusterDomainField != null)
            clusterDomainField.setText(clusterData.getDomain());
        if(!selected.supported)
        {
            connectButton.setEnabled(false);
            disconnectButton.setEnabled(false);
            
            userField.setEnabled(false);
            passwordField.setEnabled(false);
            urlField.setEnabled(false);
            daField.setEnabled(false);
            return new ConnectedEvent(this, ConnectedEvent.NotConnected, ConnectionControl.ConnectedActionCommand);
        }
        userField.setEnabled(true);
        passwordField.setEnabled(true);
        urlField.setEnabled(true);
        daField.setEnabled(true);
        connectButton.setEnabled(!selected.connected);
        disconnectButton.setEnabled(selected.connected);
        return new ConnectedEvent(this, selected.connected ? ConnectedEvent.Connected : ConnectedEvent.NotConnected, ConnectionControl.ConnectedActionCommand);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if(src instanceof JRadioButton)
        {
            ConnectedEvent event = adjustForSelection();
            if(callersActionListener != null)
                callersActionListener.actionPerformed(event);
            return;
        }
        if(src == connectButton)
        {
            if(callersActionListener != null)
                callersActionListener.actionPerformed(new ConnectedEvent(this, ConnectedEvent.ConnectButton, ConnectionControl.ConnectButtonLabel));
            return;
        }
        if(src == disconnectButton)
        {
            if(callersActionListener != null)
                callersActionListener.actionPerformed(new ConnectedEvent(this, ConnectedEvent.DisconnectButton, ConnectionControl.DisconnectButtonLabel));
            return;
        }
    }

    @Override
    public void focusRequested(Object context)
    {
        refresh();
    }
    
    public static class ConnectedEvent extends ActionEvent
    {
        public static final int NotConnected = 0;
        public static final int Connected = 1;
        public static final int ConnectButton = 2;
        public static final int DisconnectButton = 3;
        
        public ConnectedEvent(Object source, int id, String command)
        {
            super(source, id, command);
        }
    }
}
