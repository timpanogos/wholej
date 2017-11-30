/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.ancpwr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.emitdo.research.app.dbAdmin.AncPwrManagerFrame;
import org.emitdo.research.app.swing.CalendarDialog;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;

public class FrPanel extends JPanel implements  ActionListener, WindowFocusListener, SwappedFocusListener, DocumentListener
{
    private final int numberOfColumns = 52;
    private final AncPwrManagerFrame ancPwrManager;
    
    private JTextField startField;
    private JTextField endField;

    public FrPanel(AncPwrManagerFrame ancPwrManager)
    {
        this.ancPwrManager = ancPwrManager;
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder("Frequency Regulation History"));
        ancPwrManager.addWindowFocusListener(this);
        
        AtomicInteger maxWidth = new AtomicInteger();
        JLabel startLabel = FrameBase.getLabel("Start time:", maxWidth);
        JLabel endLabel = FrameBase.getLabel("End time:", maxWidth);
        int width = maxWidth.get();

        SimpleDateFormat sdf = new SimpleDateFormat(CalendarDialog.DefaultSimpleDateFormat);
        long now = System.currentTimeMillis();
        Date start = new Date(now - 1000 * 60 * 60); // 1 hr back
        Date end = new Date(now); // 1 hr back
        
        startField = new JTextField(sdf.format(start), numberOfColumns);
        FrameBase.addTextParamToPanel(startLabel, startField, width, -1, "Starting time/date for data to display", mainPanel, this);
        endField = new JTextField(sdf.format(end), numberOfColumns);
        FrameBase.addTextParamToPanel(endLabel, endField, width, -1, "Ending time/date for data to display", mainPanel, this);
        
//        CalendarDialog cd = new CalendarDialog(ancPwrManager, this, startField);
//        CalendarDialog cd2 = new CalendarDialog(ancPwrManager, this, endField);
        
//        XYSeries series1 = new XYSeries("First");
//        series1.add(1.0, 1.0);
//        series1.add(2.0, 4.0);
//        series1.add(3.0, 3.0);
//        series1.add(4.0, 5.0);
//        series1.add(5.0, 5.0);
//        series1.add(6.0, 7.0);
//        series1.add(7.0, 7.0);
//        series1.add(8.0, 8.0);
//        XYSeries series2 = new XYSeries("Second");
//        series2.add(1.0, 5.0);
//        series2.add(2.0, 7.0);
//        series2.add(3.0, 6.0);
//        series2.add(4.0, 8.0);
//        series2.add(5.0, 4.0);
//        series2.add(6.0, 4.0);
//        series2.add(7.0, 2.0);
//        series2.add(8.0, 1.0);
//        XYSeries series3 = new XYSeries("Third");
//        series3.add(3.0, 4.0);
//        series3.add(4.0, 3.0);
//        series3.add(5.0, 2.0);
//        series3.add(6.0, 3.0);
//        series3.add(7.0, 6.0);
//        series3.add(8.0, 3.0);
//        series3.add(9.0, 4.0);
//        series3.add(10.0, 3.0);
//        XYSeriesCollection dataset = new XYSeriesCollection();
//        dataset.addSeries(series1);
//        dataset.addSeries(series2);
//        dataset.addSeries(series3);
//        JFreeChart chart = ChartFactory.createXYLineChart("Line Chart Demo", "X", "Y", dataset, PlotOrientation.VERTICAL, true, true, true);
//        ChartPanel cpanel = new ChartPanel(chart);
//        mainPanel.add(cpanel);
        
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
        
        
        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(640, 480));
        add(scrollPane, BorderLayout.CENTER);
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
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
//        Object src = e.getSource();
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
//        if(context instanceof Throwable)
//        {
//            acknowledge = null;
//            FrameBase.displayException(null, ancPwrManager, "Worker thread exception", "Failed to connect and/or initialize", (Throwable)context);
//        }
//        windowGainedFocus(null);
//        if(acknowledge != null)
//            JOptionPane.showMessageDialog(ancPwrManager, acknowledge);
//        acknowledge = null;
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
}
