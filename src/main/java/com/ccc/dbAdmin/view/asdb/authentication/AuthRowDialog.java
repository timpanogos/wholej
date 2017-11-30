/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.asdb.authentication;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.emitdo.as.jdb.domain.JdbAuthenticationNodeJdbc;
import org.emitdo.research.app.dbAdmin.AsManagerFrame;
import org.emitdo.research.app.dbAdmin.model.asdb.AuthNodeTableModel;
import org.emitdo.research.app.dbAdmin.model.asdb.AuthNodeTableModel.AuthNodeRow;
import org.emitdo.research.app.swing.AppliedCallback;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class AuthRowDialog extends JDialog 
{
    public AuthRowDialog(AsManagerFrame asManager, Component source, AuthNodeTablePanel tablePanel, int index, boolean insert) 
    {
        super(asManager, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(new AuthNodeRowSelectionPanel(this, tablePanel, index, insert));
        setLocationRelativeTo(source);
        setMinimumSize(new Dimension(245, 160));
        pack();
        setVisible(true);
    }
    
    static public class AuthNodeRowSelectionPanel extends JPanel implements ActionListener, SwappedFocusListener, AppliedCallback 
    {
        private final int numberOfColumns = 52;
        
        private final AsManagerFrame asManager;
        private final AuthRowDialog dialog;
        private final AuthNodeTableModel tableModel;
        private final AuthNodeRow authNodeRow;
        private final boolean insert;
        private final JButton applyButton;
        private final JButton cancelButton;
        
        private final JTextField oidField;
        private final JCheckBox enabledCheck;
        private final JTextField sourceIdField;
        
        public AuthNodeRowSelectionPanel(AuthRowDialog dialog, AuthNodeTablePanel tablePanel, int index, boolean insert)
        {
            this.dialog = dialog;
            asManager = tablePanel.getAsManager();
            this.tableModel = tablePanel.getTableModel();
            authNodeRow = new AuthNodeRow(tableModel, index);
            this.insert = insert;
            
            setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new TitledBorder("Authentication nodes"));
            
            AtomicInteger maxWidth = new AtomicInteger();
            JLabel oidLabel = FrameBase.getLabel("Identity:", maxWidth);
            JLabel enabledLabel = FrameBase.getLabel("Enabled:", maxWidth);
            JLabel sourceIdLabel = FrameBase.getLabel("SourceId:", maxWidth);
            int width = maxWidth.get();
            
            oidField = new JTextField(authNodeRow.oid.toString(), numberOfColumns);
            FrameBase.addTextParamToPanel(oidLabel, oidField, width, -1, "The authentication nodes identity", panel);
            oidField.setEditable(false);
            sourceIdField = new JTextField(""+authNodeRow.sourceId, numberOfColumns);
            FrameBase.addTextParamToPanel(sourceIdLabel, sourceIdField, width, -1, "The authentication nodes sourceId", panel);
            sourceIdField.setEditable(false);
            
            enabledCheck = FrameBase.addCheckBoxToPanel(enabledLabel, authNodeRow.enabled, true, width, -1, "Is the node currently enabled", panel, this);
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(true);
            
            applyButton = FrameBase.addButtonToPanel("Apply", false, "Commit the current modifications", buttonPanel, this);
            cancelButton = FrameBase.addButtonToPanel("Cancel", false, "Cancel any edits and exit", buttonPanel, this);
            panel.add(buttonPanel);
            
            panel.setSize(800, 600);
            JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            dataPanel.add(panel);
                  
            JScrollPane scrollPane = new JScrollPane(dataPanel);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane, BorderLayout.CENTER);
            asManager.swapAndSetFocus(this, oidField);
        }

        @Override
        public void focusRequested(Object context)
        {
            applyButton.setEnabled(false);
            cancelButton.setEnabled(false);
        }
        
        @Override
        public void complete(boolean ok, String message, Exception e, Object context)
        {
            if(ok)
            {
                asManager.refreshDialog(JdbAuthenticationNodeJdbc.TableName);
                dialog.dispose();
            }
        }

        public String getOid()
        {
            return oidField.getText();
        }
        
        public String getSourceId()
        {
            return sourceIdField.getText();
        }
        
        public boolean isNodeEnabled()
        {
            return enabledCheck.isSelected();
        }
        
        public boolean isInsert()
        {
            return insert;
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src = e.getSource();
            if(src == enabledCheck)
            {
                applyButton.setEnabled(true);
                cancelButton.setEnabled(true);
                return;
            }
            if(src == applyButton)
            {
                asManager.setEnabled(this);
                applyButton.setEnabled(false);
                return;
            }
            if(src == cancelButton)
            {
                dialog.dispose();
                applyButton.setEnabled(false);
                cancelButton.setEnabled(false);
                return;
            }
        }
    }
}