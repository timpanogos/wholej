/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.asdb.permissions;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.emitdo.oal.DOFInterfaceID;
import org.emitdo.oal.security.DOFPermission.Define;
import org.emitdo.research.app.dbAdmin.AsManagerFrame;
import org.emitdo.research.app.dbAdmin.view.asdb.permissions.PermissionModifyDialog.PermissionModifyPanel;
import org.emitdo.research.app.swing.FrameBase;

public class DefinePermissionDialog extends JDialog 
{
    public DefinePermissionDialog(AsManagerFrame asManager, Component source, PermissionModifyPanel parentPanel) 
    {
        super(asManager, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(new DefinePanel(this, parentPanel));
        setLocationRelativeTo(source);
        setMinimumSize(new Dimension(245, 160));
        pack();
        setVisible(true);
    }
    
    private class DefinePanel extends JPanel implements DocumentListener, ActionListener 
    {
        private final int numberOfColumns = 52;
        private final DefinePermissionDialog dialog;
        
        private final PermissionModifyPanel parentPanel;
        private final JTextField iidField;
        private final JButton applyButton;
        private final JButton cancelButton;
        
        public DefinePanel(DefinePermissionDialog dialog, PermissionModifyPanel parentPanel)
        {
            this.dialog = dialog;
            this.parentPanel = parentPanel;
            setLayout(new BorderLayout());
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new TitledBorder("Define Permission"));
                
            AtomicInteger maxWidth = new AtomicInteger();
            JLabel idLabel = FrameBase.getLabel("IID:", maxWidth);
            int width = maxWidth.get();
                
            iidField = new JTextField("[:]", numberOfColumns);
            FrameBase.addTextParamToPanel(idLabel, iidField, width, -1, "The DOFInterfaceID to define", panel, this);
                
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(true);
            
            applyButton = FrameBase.addButtonToPanel("Apply", false, "Commit the current modifications", buttonPanel, this);
            cancelButton = FrameBase.addButtonToPanel("Cancel", true, "Cancel any edits and exit", buttonPanel, this);
            panel.add(buttonPanel);
                
            panel.setSize(800, 600);
            JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            dataPanel.add(panel);
                  
            JScrollPane scrollPane = new JScrollPane(dataPanel);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane, BorderLayout.CENTER);
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src = e.getSource();
            if(src == applyButton)
            {
                try
                {
                    parentPanel.addPermission(new Define(DOFInterfaceID.create(iidField.getText())));
                    dialog.dispose();
                }catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(this, "Invalid DOFInterfaceID.Source", "Invalid Interface ID", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            if(src == cancelButton)
            {
                dialog.dispose();
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