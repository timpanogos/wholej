/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.ancpwr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.emitdo.oal.DOFObjectID;
import org.emitdo.research.app.dbAdmin.AncPwrManagerFrame;
import org.emitdo.research.app.swing.BasePanel;
import org.emitdo.research.app.swing.CalendarDialog;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.xy.XYSeriesCollection;

public class FrHistoryPanel extends JPanel implements  ActionListener, WindowFocusListener, SwappedFocusListener, DocumentListener, MouseListener
{
    private final int numberOfColumns = 52;
    private final AncPwrManagerFrame ancPwrManager;
    
    private JTextField oidField;
    private JTextField startField;
    private JTextField endField;
    private JTextField slideField;
//    private XYSeriesCollection dataset;
    private JPanel chartPanel;
    private JButton refreshButton;
    private JButton forwardButton;
    private JButton backwardButton;

    public FrHistoryPanel(AncPwrManagerFrame ancPwrManager)
    {
        this.ancPwrManager = ancPwrManager;
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder(""));
        ancPwrManager.addWindowFocusListener(this);
        
        AtomicInteger maxWidth = new AtomicInteger();
        JLabel oidLabel = FrameBase.getLabel("Provider oid:", maxWidth);
        JLabel startLabel = FrameBase.getLabel("Start time:", maxWidth);
        JLabel endLabel = FrameBase.getLabel("End time:", maxWidth);
        JLabel slideLabel = FrameBase.getLabel("Slide minutes:", maxWidth);
        int width = maxWidth.get();
        
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());

//        dataset = new XYSeriesCollection();
//        JFreeChart chart = ChartFactory.createXYLineChart("Frequency Regulation History", "Seconds", "Watts", dataset, PlotOrientation.VERTICAL, true, true, true);
//        ChartPanel cpanel = new ChartPanel(chart);
//        chartPanel.add(cpanel);
        
        JScrollPane scrollPane = new JScrollPane(chartPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(740, 480));
        mainPanel.add(scrollPane);
        
        SimpleDateFormat sdf = new SimpleDateFormat(CalendarDialog.DefaultSimpleDateFormat);
        long now = System.currentTimeMillis();
        Date start = new Date(now - 1000 * 60 * 60); // 1 hr back
        Date end = new Date(now); // 1 hr back
        
        oidField = new JTextField("[3:ucsd@ancillary.pewla.com]", numberOfColumns);
        FrameBase.addTextParamToPanel(oidLabel, oidField, width, -1, "The Provider OID to display", mainPanel, this, this);
        startField = new JTextField(sdf.format(start), numberOfColumns);
        FrameBase.addTextParamToPanel(startLabel, startField, width, -1, "Starting time/date for data to display", mainPanel, this, this);
        endField = new JTextField(sdf.format(end), numberOfColumns);
        FrameBase.addTextParamToPanel(endLabel, endField, width, -1, "Ending time/date for data to display", mainPanel, this, this);
        slideField = new JTextField("10", numberOfColumns);
        FrameBase.addTextParamToPanel(slideLabel, slideField, width, -1, "Number of minutes to slide forward/backwards", mainPanel, this, this);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        backwardButton = FrameBase.addButtonToPanel("Slide Back", true, "Move both start and end back 10 minutes", buttonPanel, this);
        refreshButton = FrameBase.addButtonToPanel("Refresh", true, "Refresh with current dates", buttonPanel, this);
        forwardButton = FrameBase.addButtonToPanel("Slide Forward", true, "Move both start and end forward 10 minuts", buttonPanel, this);
        mainPanel.add(buttonPanel);

//        DefaultPieDataset data = new DefaultPieDataset();
//        data.setValue("Category 1", new Double(43.22));
//        data.setValue("Category 2", new Double(27.9));
//        data.setValue("Category 3", new Double(79.5));
//        JFreeChart chart = ChartFactory.createPieChart("Sample Pie Chart", data, true, true, true);
//        DynamicTimeSeriesCollection dtsc;
//        
//        ChartPanel cpanel = new ChartPanel(chart);
//        mainPanel.add(cpanel);
        
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dataPanel.add(mainPanel);
        
        scrollPane = new JScrollPane(dataPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        add(scrollPane, BorderLayout.CENTER);
    
        DOFObjectID oid = getProviderId();
//        if(oid != null)
//            ancPwrManager.getFrHistoryData(this, oid, start.getTime(), end.getTime(), dataset);
    }

    private DOFObjectID getProviderId()
    {
        String value = oidField.getText();
        try
        {
            return DOFObjectID.create(value);
        }catch(Exception e)
        {
            BasePanel.displayException(null, this, "Bad DOFObjectID", value + " in not a valid DOFObjectID", e);
            return null;
        }
    }
    
    public void refresh()
    {
//        connectionsPanel.refresh();
//        listModel.clear();
//        try
//        {
//            Domain platform = data.platformData.getDomainOid();
//            
//            for(int i=0; i < data.getDeviceCount(); i++)
//                listModel.addElement(OracleCannedResInitializer.getDeviceForIndex(i, platform).oid);
//            
//            int nextGatewayDeviceIndex = 0;
//            for(int i=0; i < data.getGatewayCount(); i++)
//            {
//                listModel.addElement(OracleCannedResInitializer.getGatewayForIndex(i, platform).oid);
//                for(int j=0; j < data.getGatewayDeviceCount(); j++)
//                    listModel.addElement(OracleCannedResInitializer.getGatewayDeviceForIndex(nextGatewayDeviceIndex++, platform).oid);
//            }
//            
//        }catch(Exception e)
//        {
//            FrameBase.displayException(null, buManager, "bad news", "failed to generate example list", e);
//        }
    }
    
    private void getNewChartData()
    {
        DOFObjectID oid = getProviderId();
        SimpleDateFormat sdf = new SimpleDateFormat(CalendarDialog.DefaultSimpleDateFormat);
        Date start = null;
        Date end = null;
        try
        {
            start = sdf.parse(startField.getText());
            end = sdf.parse(endField.getText());
        } catch (ParseException e1)
        {
            BasePanel.displayException(null, this, "Bad date format", "The start or end date field is not in the expected format", e1);
            oid = null;
        }
//        if(oid != null && start != null && end != null)
//            ancPwrManager.getFrHistoryData(this, oid, start.getTime(), end.getTime(), dataset);
    }
    
    private void moveTmes(boolean backwards)
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(CalendarDialog.DefaultSimpleDateFormat);
            long t0 = sdf.parse(startField.getText()).getTime();
            int minutes = Integer.parseInt(slideField.getText()) * 60 * 1000;
            
            if(backwards)
            {            
                t0 -= minutes;
                startField.setText(sdf.format(new Date(t0)));
                t0 = sdf.parse(endField.getText()).getTime();
                t0 -= minutes;
                endField.setText(sdf.format(new Date(t0)));
                return;
            }
            t0 += minutes;
            startField.setText(sdf.format(new Date(t0)));
            t0 = sdf.parse(endField.getText()).getTime();
            t0 += minutes;
            endField.setText(sdf.format(new Date(t0)));
        } catch (ParseException e1)
        {
            BasePanel.displayException(null, this, "DateTime formating error", "Invalid date/time format", e1);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if(command.equals(CalendarDialog.DateChanged))
        {
            getNewChartData();
            return;
        }
        
        Object src = e.getSource();
        if(src == refreshButton)
        {
            getNewChartData();
            return;
        }
        if(src == backwardButton)
        {
            moveTmes(true);
            getNewChartData();
            return;
        }
        if(src == forwardButton)
        {
            moveTmes(false);
            getNewChartData();
            return;
        }
//        
//        if(src == connectionsPanel)
//        {
//            String command = e.getActionCommand(); 
//            if(command.equals(ConnectionControl.ConnectedActionCommand))
//            {
//                connected.set(e.getID() == ConnectedEvent.Connected ? true : false);
//                initializeButton.setEnabled(connected.get());
//                manageButton.setEnabled(connected.get());
//                ancPwrManager.setCurrentlySelectedConnection(connections.currentlySelected);
//                return;
//            }
//            if(command.equals(ConnectionControl.ConnectButtonLabel))
//            {
//                ancPwrManager.getConnectionControl().
//                    connect(connections.currentlySelected, null, true, this, null);
//               return;
//            }
//            if(command.equals(ConnectionControl.DisconnectButtonLabel))
//            {
//                ancPwrManager.getConnectionControl().disconnect(connections.currentlySelected, this);
//                ancPwrManager.setCurrentlySelectedConnection(null);
//               return;
//            }
//            return;
//        }
//        
//        if(src == initializeButton)
//        {
//            int option = JOptionPane.showConfirmDialog(
//                    ancPwrManager, 
//                    "WARNING:\nYou are about to initialize the Ancillary Power Storage domain that is currently selected.\n" +
//                    "Auto create is selected for this Ancillary Power Storage.\nIf the currently selected storage exits it will be deleted and rebuilt.\nIf it does not exist it will be created." +
//                    "\n\nAre you sure you want to continue?",
//                    "Initialize Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//            if(option != JOptionPane.OK_OPTION)
//                return;
//            acknowledge = "Ancillary Power storage initialization completed";
//            
//            Runnable runner = new Runnable(){@Override public void run(){initializeButton.setEnabled(false);}};
//            SwingUtilities.invokeLater(runner);
//            
//            ancPwrManager.getConnectionControl().initializeAncPwr(connections.currentlySelected, true, this);
//            return;
//        }
    }

    @Override
    public void focusRequested(Object context)
    {
        if(context != null)
            FrameBase.displayException(null, ancPwrManager, "Worker thread exception", "Failed to connect obtain data", (Throwable)context);
    }

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        refresh();
    }

    @Override public void windowLostFocus(WindowEvent e){}
    
    @Override
    public void changedUpdate(DocumentEvent e)
    {
//        saveButton.setEnabled(true);
//        data.host = hostField.getText();
//        data.port = portField.getText();
//        data.home = homeField.getText();
//        data.maxThreads = maxThreadsField.getText();
//        data.timeslice = timesliceField.getText();
//        data.logConnections = logConnectionsCheck.isSelected();
    }
    @Override public void insertUpdate(DocumentEvent e){changedUpdate(null);}
    @Override public void removeUpdate(DocumentEvent e){changedUpdate(null);}

    @Override
    public void mouseClicked(MouseEvent e)
    {
        Object src = e.getSource();
        if(src == startField)
        {
            new CalendarDialog(ancPwrManager, this, startField, this);
            return;
        }
        if(src == endField)
        {
            new CalendarDialog(ancPwrManager, this, endField, this);
            return;
        }
    }

    @Override public void mouseEntered(MouseEvent e){}

    @Override public void mouseExited(MouseEvent e){}

    @Override public void mousePressed(MouseEvent e){}

    @Override public void mouseReleased(MouseEvent e){}
}
