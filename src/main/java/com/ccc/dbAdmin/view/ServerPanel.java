/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.InetAddress;
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
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.derby.drda.NetworkServerControl;
import org.emitdo.db.common.javadb.JdbCommon;
import org.emitdo.research.app.dbAdmin.DbAdmin;
import org.emitdo.research.app.dbAdmin.model.DerbyServerData;
import org.emitdo.research.app.swing.FrameBase;

public class ServerPanel extends JPanel implements ActionListener, DocumentListener
{
    private final int numberOfColumns = 52;

    private JTextField hostField;
    private JTextField portField;
    private JTextField homeField;
    private JTextField maxThreadsField;
    private JTextField timesliceField;
    private JCheckBox logConnectionsCheck;
    private JButton startButton;
    private JButton stopButton;
    private JButton runInfoButton;
    private JButton sysInfoButton;
    private JButton pingButton;
    private JButton saveButton;
    
    private final DbAdmin asAdmin;
    private final DerbyServerData data;

    public ServerPanel(DerbyServerData data, DbAdmin asAdmin)
    {
        this.data = data;
        this.asAdmin = asAdmin;
        
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Derby Server"));
        
        AtomicInteger maxWidth = new AtomicInteger();
        JLabel hostLabel = FrameBase.getLabel("Host:", maxWidth);
        JLabel portLabel = FrameBase.getLabel("Port:", maxWidth);
        JLabel homeLabel = FrameBase.getLabel("Home path:", maxWidth);
        JLabel maxThreadsLabel = FrameBase.getLabel("Max threads:", maxWidth);
        JLabel timesliceLabel = FrameBase.getLabel("Timeslice:", maxWidth);
        JLabel logConnectionsLabel = FrameBase.getLabel("Log conns:", maxWidth);
        
        int width = maxWidth.get();
        
        hostField = new JTextField(data.host, numberOfColumns);
        FrameBase.addTextParamToPanel(hostLabel, hostField, width, -1, "Enter host/address that the Derby Server should listen for connections on", panel, this);

        portField = new JTextField(data.port, numberOfColumns);
        FrameBase.addTextParamToPanel(portLabel, portField, width, -1, "Enter port that the Derby Server should listen for connections on", panel, this);

        homeField = new JTextField(data.home, numberOfColumns);
        FrameBase.addTextParamToPanel(homeLabel, homeField, width, -1, "Enter file system path where databases will be created.", panel, this);
        
        maxThreadsField = new JTextField(data.maxThreads, numberOfColumns);
        FrameBase.addTextParamToPanel(maxThreadsLabel, maxThreadsField, width, -1, "Enter max threads allowed for connetions (<= 0 is no limit).", panel, this);
        
        timesliceField = new JTextField(data.timeslice, numberOfColumns);
        FrameBase.addTextParamToPanel(timesliceLabel, timesliceField, width, -1, "Number of milliseconds between thread switchs if maxThreads >0.", panel, this);
        
        logConnectionsCheck = FrameBase.addCheckBoxToPanel(logConnectionsLabel, data.logConnections, true, width, -1, "Enable logging of connections", panel, this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        startButton = FrameBase.addButtonToPanel("Start", !data.running, "Start the Derby JavaDB server", buttonPanel, this);
        stopButton = FrameBase.addButtonToPanel("Stop", data.running, "Stop the Derby JavaDB server", buttonPanel, this);
        runInfoButton = FrameBase.addButtonToPanel("RunInfo", data.running, "Get Runtime information", buttonPanel, this);
        sysInfoButton = FrameBase.addButtonToPanel("SysInfo", data.running, "Get system information", buttonPanel, this);
        pingButton = FrameBase.addButtonToPanel("Ping", data.running, "Ping the server", buttonPanel, this);
        saveButton = FrameBase.addButtonToPanel("Save", false, "Save current settings", buttonPanel, this);

        panel.add(buttonPanel);
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dataPanel.add(panel);
        
        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        add(scrollPane, BorderLayout.CENTER);
        
        Runnable runner = new Runnable(){@Override public void run(){hostField.requestFocus();}};
        SwingUtilities.invokeLater(runner);
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        Object src = event.getSource();
        if(src == saveButton)
        {
            data.savePreferences();
            saveButton.setEnabled(false);
            return;
        }
        if(src == startButton)
        {
            try
            {
                System.setProperty(JdbCommon.DerbyHomeKey, homeField.getText());
                if(data.server == null)
                    data.server = new NetworkServerControl(InetAddress.getByName(hostField.getText()), Integer.parseInt(portField.getText()));
                data.server.start(new PrintWriter(System.out, true));
                setServerParams();
            } catch (Throwable e)
            {
                FrameBase.displayException(null, asAdmin, "Derby Server", "Failed to start", e);
                return;
            }
            data.running = true;
            stopButton.setEnabled(true);
            startButton.setEnabled(false);
            runInfoButton.setEnabled(true);
            sysInfoButton.setEnabled(true);
            pingButton.setEnabled(true);
            return;
        }
                    
        if(src == stopButton)
        {
            try
            {
                data.server.shutdown();
                data.server = null;
            } catch (Exception e)
            {
                FrameBase.displayException(null, asAdmin, "Derby Server", "Failed to stop", e);
            }
            data.running = false;
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            runInfoButton.setEnabled(false);
            sysInfoButton.setEnabled(false);
            pingButton.setEnabled(false);
        }
        if(src == runInfoButton)
        {
            String msg = null;
            try
            {
                msg = data.server.getRuntimeInfo();
            } catch (Exception e)
            {
            }
            if(msg == null)
                JOptionPane.showMessageDialog(asAdmin, "server.getRuntimeInfo failed");
            else
                JOptionPane.showMessageDialog(asAdmin, msg);
            return;
        }
        if(src == sysInfoButton)
        {
            String msg = null;
            try
            {
                msg = data.server.getSysinfo();
            } catch (Exception e)
            {
            }
            if(msg == null)
                JOptionPane.showMessageDialog(asAdmin, "server.getRuntimeInfo failed");
            else
            {
                StringBuilder sb = new StringBuilder();
                do
                {
                    String line;
                    int index = msg.indexOf("\r\n");
                    if(index == -1)
                        break;
                    line = msg.substring(++index);
                    msg = msg.substring(index);
                    if(line.length() > 120)
                        sb.append(line.substring(0, 120)).append("\n");
                    else
                        sb.append(line);
                }while(true);
//                JOptionPane.showMessageDialog(asAdmin, sb.toString());
                System.out.println(sb.toString());
            }
            return;
        }
        if(src == pingButton)
        {
            boolean ok = false;
            String msg = null;
            try
            {
                data.server.ping();
                ok = true;
            } catch (Exception e)
            {
                msg = e.getMessage();
                if(msg == null)
                    msg = e.getClass().getName();
            }
            if(ok)
                JOptionPane.showMessageDialog(asAdmin, "Server is available");
            else
                JOptionPane.showMessageDialog(asAdmin, "ping failed\n"+msg);
            return;
        }
    }
    
    private void setServerParams() throws Exception
    {
        int count = 0;
        do
        {
            try
            {
                data.server.ping();
                break;
            }catch(Exception e)
            {
                Thread.sleep(100);
                if(++count > 5)
                    throw new Exception("Server did never became available");
            }
        }while(true);
        data.server.setMaxThreads(Integer.parseInt(maxThreadsField.getText()));
        data.server.setTimeSlice(Integer.parseInt(timesliceField.getText()));
        data.server.logConnections(logConnectionsCheck.isSelected());
    }
    
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        saveButton.setEnabled(true);
        data.host = hostField.getText();
        data.port = portField.getText();
        data.home = homeField.getText();
        data.maxThreads = maxThreadsField.getText();
        data.timeslice = timesliceField.getText();
        data.logConnections = logConnectionsCheck.isSelected();
    }
    @Override public void insertUpdate(DocumentEvent e){changedUpdate(null);}
    @Override public void removeUpdate(DocumentEvent e){changedUpdate(null);}
}
