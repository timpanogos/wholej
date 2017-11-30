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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.research.app.dbAdmin.AsManagerFrame;
import org.emitdo.research.app.swing.AppliedCallback;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class AuthIdModifyDialog extends JDialog 
{
    public AuthIdModifyDialog(AsManagerFrame asManager, Component source, Authentication identity) 
    {
        super(asManager, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(new AuthIdModifyPanel(this, asManager, identity));
        setLocationRelativeTo(source);
        setMinimumSize(new Dimension(245, 160));
        pack();
        setVisible(true);
    }
    
    static public class AuthIdModifyPanel extends JPanel implements DocumentListener,  ActionListener, SwappedFocusListener, AppliedCallback 
    {
        private final int numberOfColumns = 52;
        
        private final AsManagerFrame asManager;
        private final AuthIdModifyDialog dialog;
        private JTextField identityField;
        private JTextField passwordField;
        private JTextField presharedField;

        private final JButton applyButton;
        private final JButton cancelButton;
        
        public AuthIdModifyPanel(AuthIdModifyDialog dialog, AsManagerFrame asManager, Authentication identity)
        {
            this.dialog = dialog;
            this.asManager = asManager;
//            this.tableModel = tablePanel.getTableModel();
            
            setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new TitledBorder("Modify Authentication Identity"));
            
            AtomicInteger maxWidth = new AtomicInteger();
            JLabel identityLabel = FrameBase.getLabel("Identity:", maxWidth);
            JLabel passwordLabel = FrameBase.getLabel("Password:", maxWidth);
            JLabel presharedLabel = FrameBase.getLabel("Preshared:", maxWidth);
            int width = maxWidth.get();
            
            identityField = new JTextField(identity == null ? "[:]" : identity.toStandardString(), numberOfColumns);
            FrameBase.addTextParamToPanel(identityLabel, identityField, width, -1, "The authentication nodes identity", panel, this);
            if(identity != null)
                identityField.setEditable(false);
            passwordField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(passwordLabel, passwordField, width, -1, "The password - optional, password type not added if empty", panel, this);
            presharedField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(presharedLabel, presharedField, width, -1, "The preshared key - optional, key type not added if emtpy", panel, this);
            
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
            asManager.swapAndSetFocus(this, identityField);
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
                asManager.refreshDialog(AsManagerFrame.PermissionsDialog);
                dialog.dispose();
            }
        }

        public String getIdentity()
        {
            return identityField.getText();
        }
        
        public String getPassword()
        {
            return passwordField.getText();
        }
        
        public String getPresharedKey()
        {
            return presharedField.getText();
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src = e.getSource();
            if(src == applyButton)
            {
                asManager.modifyIdentity(this);
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

        @Override public void insertUpdate(DocumentEvent e){changedUpdate(e);}
        @Override public void removeUpdate(DocumentEvent e){changedUpdate(e);}
        @Override public void changedUpdate(DocumentEvent e)
        {
            applyButton.setEnabled(true);
            cancelButton.setEnabled(true);
        }
    }
}