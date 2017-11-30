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
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import org.emitdo.oal.DOFInterfaceID;
import org.emitdo.oal.DOFObjectID;
import org.emitdo.oal.DOFObjectID.Attribute;
import org.emitdo.oal.DOFUtil;
import org.emitdo.oal.security.DOFPermission.Binding;
import org.emitdo.research.app.dbAdmin.AsManagerFrame;
import org.emitdo.research.app.dbAdmin.view.asdb.permissions.PermissionModifyDialog.PermissionModifyPanel;
import org.emitdo.research.app.swing.FrameBase;

public class BindingPermissionDialog extends JDialog 
{
    public BindingPermissionDialog(AsManagerFrame asManager, Component source, PermissionModifyPanel parentPanel) 
    {
        super(asManager, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(new BindingPanel(this, parentPanel));
        setLocationRelativeTo(source);
        setMinimumSize(new Dimension(245, 160));
        pack();
        setVisible(true);
    }
    
    private class BindingPanel extends JPanel implements DocumentListener, ActionListener, ListSelectionListener 
    {
        private final int numberOfColumns = 52;
        private final BindingPermissionDialog dialog;
        private final PermissionModifyPanel parentPanel;
        
        private final Hashtable<Document, JTextField> docMap;
        
        
        private final JTextField oidField;
        private final JTextField iidField;
        
        private final JList oidList;
        private final DefaultListModel oidListModel;
        private final ListSelectionModel oidSelectionModel;
        
        private final JList iidList;
        private final DefaultListModel iidListModel;
        private final ListSelectionModel iidSelectionModel;
        
        private final JTextField attrField;
        private final JTextField attrTypeField;
        private final JTextField wildField;
        
        private final JList attrList;
        private final DefaultListModel attrListModel;
        private final ListSelectionModel attrSelectionModel;
        
        private final JList wildList;
        private final DefaultListModel wildListModel;
        private final ListSelectionModel wildSelectionModel;
        
        public final JCheckBox actionAll;
        public final JCheckBox actionSession;
        public final JCheckBox actionProvide;
        public final JCheckBox actionRead;
        public final JCheckBox actionWrite;
        public final JCheckBox actionExcecute;
        
        public final JRadioButton attributeAll;
        public final JRadioButton attributeNone;
        public final JRadioButton attributeSpecific;
        
        public final JCheckBox attrTypeOid;
        public final JRadioButton attrTypeProvider;
        public final JRadioButton attrTypeSession;
        public final JRadioButton attrTypeGroup;
        public final JRadioButton attrTypeOther;
        
        private final JButton addOidButton;
        private final JButton addIidButton;
        private final JButton addAttrButton;
        private final JButton addWildButton;
        private final JButton deleteOidButton;
        private final JButton deleteIidButton;
        private final JButton deleteAttrButton;
        private final JButton deleteWildButton;
        
        private final JButton applyButton;
        private final JButton cancelButton;
        
        public BindingPanel(BindingPermissionDialog dialog, PermissionModifyPanel parentPanel)
        {
            this.dialog = dialog;
            this.parentPanel = parentPanel;
            docMap = new Hashtable<Document,JTextField>();
            
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(new TitledBorder("Binding Permission"));
                
            AtomicInteger maxWidth = new AtomicInteger();
            JLabel oidLabel = FrameBase.getLabel("OID:", maxWidth);
            JLabel iidLabel = FrameBase.getLabel("IID:", maxWidth);
            
            JLabel actionAllLabel = FrameBase.getLabel("All:", maxWidth);
            JLabel actionSessionLabel = FrameBase.getLabel("Session:", maxWidth);
            JLabel actionProvideLabel = FrameBase.getLabel("Provide:", maxWidth);
            JLabel actionReadLabel = FrameBase.getLabel("Read:", maxWidth);
            JLabel actionWriteLabel = FrameBase.getLabel("Write:", maxWidth);
            JLabel actionExecuteLabel = FrameBase.getLabel("Execute:", maxWidth);
            
            JLabel attributeAllLabel = FrameBase.getLabel("All:", maxWidth);
            JLabel attributeNoneLabel = FrameBase.getLabel("None:", maxWidth);
            JLabel attributeSpecificLabel = FrameBase.getLabel("Specific:", maxWidth);
            
            JLabel attrTypeProviderLabel = FrameBase.getLabel("Provider:", maxWidth);
            JLabel attrTypeSessionLabel = FrameBase.getLabel("Session:", maxWidth);
            JLabel attrTypeGroupLabel = FrameBase.getLabel("Group:", maxWidth);
            JLabel attrTypeOidLabel = FrameBase.getLabel("OID:", maxWidth);
            JLabel attrTypeOtherLabel = FrameBase.getLabel("Other:", maxWidth);
            
            JLabel attrLabel = FrameBase.getLabel("Attr:", maxWidth);
            JLabel attrTypeLabel = FrameBase.getLabel("Attr type:", maxWidth);
            JLabel wildLabel = FrameBase.getLabel("Wild Byte:", maxWidth);
            
            int width = maxWidth.get();
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.setBorder(new TitledBorder("Actions"));
            
            actionSession = FrameBase.addCheckBoxToPanel(actionSessionLabel, false, true, width, -1, "Session action", panel, this);
            actionProvide = FrameBase.addCheckBoxToPanel(actionProvideLabel, false, true, width, -1, "Provide action", panel, this);
            actionRead = FrameBase.addCheckBoxToPanel(actionReadLabel, true, true, width, -1, "Read action", panel, this);
            actionWrite = FrameBase.addCheckBoxToPanel(actionWriteLabel, false, true, width, -1, "Write action", panel, this);
            actionExcecute = FrameBase.addCheckBoxToPanel(actionExecuteLabel, false, true, width, -1, "Execute action", panel, this);
            actionAll = FrameBase.addCheckBoxToPanel(actionAllLabel, false, true, width, -1, "Select all actions", panel, this);
            mainPanel.add(panel);
            
            JPanel bindingPanel = new JPanel();
            bindingPanel.setLayout(new BoxLayout(bindingPanel, BoxLayout.Y_AXIS));
            bindingPanel.setBorder(new TitledBorder("Binding"));
            
            oidField = new JTextField("[:]", numberOfColumns);
            FrameBase.addTextParamToPanel(oidLabel, oidField, width, -1, "The OID to add to the binding", bindingPanel, this);
            docMap.put(oidField.getDocument(), oidField);
            iidField = new JTextField("[:]", numberOfColumns);
            FrameBase.addTextParamToPanel(iidLabel, iidField, width, -1, "The IID to add to the binding", bindingPanel, this);
            docMap.put(iidField.getDocument(), iidField);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(true);
            
            addOidButton = FrameBase.addButtonToPanel("Add OID", false, "Add the OID field to the OID list", buttonPanel, this);
            addIidButton = FrameBase.addButtonToPanel("Add IID", false, "Add the IID field to the IID list", buttonPanel, this);
            deleteOidButton = FrameBase.addButtonToPanel("Del OID", false, "Delete the selected OIDs from the list", buttonPanel, this);
            deleteIidButton = FrameBase.addButtonToPanel("Del IID", false, "Delete the selected IIDS from the list", buttonPanel, this);
            bindingPanel.add(buttonPanel);
            
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new TitledBorder("OIDs"));

            oidListModel = new DefaultListModel();
            oidList = new JList(oidListModel);
            oidList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            oidList.setVisibleRowCount(3);
            oidSelectionModel = oidList.getSelectionModel();
            oidList.addListSelectionListener(this);
            oidList.setPreferredSize(new Dimension(640, 60));
            
            JScrollPane scrollPane = new JScrollPane(oidList);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(640, 60));
            panel.add(scrollPane);
            bindingPanel.add(panel);
            
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new TitledBorder("IIDs"));

            iidListModel = new DefaultListModel();
            iidList = new JList(iidListModel);
            iidList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            iidList.setVisibleRowCount(3);
            iidSelectionModel = iidList.getSelectionModel();
            iidList.addListSelectionListener(this);
            iidList.setPreferredSize(new Dimension(640, 60));
            
            scrollPane = new JScrollPane(iidList);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(640, 60));
            panel.add(scrollPane);
            bindingPanel.add(panel);
            mainPanel.add(bindingPanel);
            
            JPanel attrPanel = new JPanel();
            attrPanel.setLayout(new BoxLayout(attrPanel, BoxLayout.Y_AXIS));
            attrPanel.setBorder(new TitledBorder("Attributes"));

            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            attributeAll = FrameBase.addRadioButtonToPanel(attributeAllLabel, true, true, width, -1, "All attribtues", panel, this);
            attributeNone = FrameBase.addRadioButtonToPanel(attributeNoneLabel, false, true, width, -1, "No attributes", panel, this);
            attributeSpecific = FrameBase.addRadioButtonToPanel(attributeSpecificLabel, false, true, width, -1, "Specific attributes", panel, this);
            attrPanel.add(panel);
            
            ButtonGroup group = new ButtonGroup();
            group.add(attributeAll);
            group.add(attributeNone);
            group.add(attributeSpecific);
            
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            attrTypeProvider = FrameBase.addRadioButtonToPanel(attrTypeProviderLabel, false, false, width, -1, "Provider attribute type", panel, this);
            attrTypeSession = FrameBase.addRadioButtonToPanel(attrTypeSessionLabel, false, false, width, -1, "Session attribute type", panel, this);
            attrTypeGroup = FrameBase.addRadioButtonToPanel(attrTypeGroupLabel, false, false, width, -1, "Group attribute type", panel, this);
            attrTypeOther = FrameBase.addRadioButtonToPanel(attrTypeOtherLabel, true, false, width, -1, "Other attribute type", panel, this);
            attrTypeOid = FrameBase.addCheckBoxToPanel(attrTypeOidLabel, true, false, width, -1, "Attr field is in DOFObjectID standard form if set, else Attr field must be a Hex string", panel, this);
            attrPanel.add(panel);
            
            group = new ButtonGroup();
            group.add(attrTypeProvider);
            group.add(attrTypeSession);
            group.add(attrTypeGroup);
            group.add(attrTypeOther);
            
            attrField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(attrLabel, attrField, width, -1, "The Attribute value, must be a valid Hex String", attrPanel, this);
            attrField.setEditable(false);
            docMap.put(attrField.getDocument(), attrField);
            
            attrTypeField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(attrTypeLabel, attrTypeField, width, -1, "The Attribute type", attrPanel, this);
            attrTypeField.setEditable(false);
            docMap.put(attrTypeField.getDocument(), attrTypeField);
            
            wildField = new JTextField("", numberOfColumns);
            FrameBase.addTextParamToPanel(wildLabel, wildField, width, -1, "The attribute wildcard byte.", attrPanel, this);
            wildField.setEditable(false);
            docMap.put(wildField.getDocument(), wildField);

            buttonPanel = new JPanel();
            buttonPanel.setOpaque(true);
            
            addAttrButton = FrameBase.addButtonToPanel("Add Attr", false, "Add the Attribute field to the attribute list", buttonPanel, this);
            addWildButton = FrameBase.addButtonToPanel("Add Wild", false, "Add the Wildcard field to the wildcard list", buttonPanel, this);
            deleteAttrButton = FrameBase.addButtonToPanel("Del Attr", false, "Delete the selected Attribute from the list", buttonPanel, this);
            deleteWildButton = FrameBase.addButtonToPanel("Del Wild", false, "Delete the selected Wildcard byte from the list", buttonPanel, this);
            attrPanel.add(buttonPanel);
            
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new TitledBorder("Attributes"));
            
            attrListModel = new DefaultListModel();
            attrList = new JList(attrListModel);
            attrList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            attrList.setVisibleRowCount(3);
            attrSelectionModel = attrList.getSelectionModel();
            attrList.addListSelectionListener(this);
            attrList.setPreferredSize(new Dimension(640, 60));
            
            scrollPane = new JScrollPane(attrList);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(640, 60));
            panel.add(scrollPane);
            attrPanel.add(panel);
            
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new TitledBorder("Wildcard Bytes"));
            
            wildListModel = new DefaultListModel();
            wildList = new JList(wildListModel);
            wildList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            wildList.setVisibleRowCount(3);
            wildSelectionModel = wildList.getSelectionModel();
            wildList.addListSelectionListener(this);
            wildList.setPreferredSize(new Dimension(640, 60));
            
            scrollPane = new JScrollPane(wildList);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(640, 60));
            panel.add(scrollPane);
            attrPanel.add(panel);
            mainPanel.add(attrPanel);
            
            buttonPanel = new JPanel();
            buttonPanel.setOpaque(true);
            applyButton = FrameBase.addButtonToPanel("Apply", false, "Commit the current modifications", buttonPanel, this);
            cancelButton = FrameBase.addButtonToPanel("Cancel", true, "Cancel any edits and exit", buttonPanel, this);
            mainPanel.add(buttonPanel);
            
            mainPanel.setSize(800, 600);
            JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            dataPanel.add(mainPanel);
                  
            scrollPane = new JScrollPane(dataPanel);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane, BorderLayout.CENTER);
        }
        
        private int getAction()
        {
            int action = 0;
            
            if(actionAll.isSelected())
                action |= Binding.ACTION_ALL;
            if(actionSession.isSelected())
                action |= Binding.ACTION_SESSION;
            if(actionProvide.isSelected())
                action |= Binding.ACTION_PROVIDE;
            if(actionRead.isSelected())
                action |= Binding.ACTION_READ;
            if(actionWrite.isSelected())
                action |= Binding.ACTION_WRITE;
            if(actionExcecute.isSelected())
                action |= Binding.ACTION_EXECUTE;
            return action;
        }

        private Binding getBinding()
        {
            Binding.Builder builder = new Binding.Builder(getAction());
            if(attributeAll.isSelected())
                builder.setAllAttributesAllowed(true);
            else if(attributeSpecific.isSelected())
            {
                int size = attrListModel.size();
                for(int i=0; i < size; i++)
                    builder.addRequiredAttribute((Attribute)attrListModel.get(i));
                size = wildListModel.size();
                for(int i=0; i < size; i++)
                    builder.addWildcardAttribute((Byte)wildListModel.get(i));
            }else
                builder.setAllAttributesAllowed(false);
            int size = oidListModel.size();
            for(int i=0; i < size; i++)
                builder.addObjectID((DOFObjectID)oidListModel.get(i));
            size = iidListModel.size();
            for(int i=0; i < size; i++)
                builder.addInterfaceID((DOFInterfaceID)iidListModel.get(i));
            return builder.build();
        }

        private void checkBoxChanged()
        {
            if(actionAll.isSelected())
            {
                actionSession.setSelected(true);
                actionProvide.setSelected(true);
                actionRead.setSelected(true);
                actionWrite.setSelected(true);
                actionExcecute.setSelected(true);
            }
            applyButton.setEnabled(true);
        }
        
        private void checkRadioChanged()
        {
            if(!attributeSpecific.isSelected())
            {
                attrTypeProvider.setEnabled(false);
                attrTypeSession.setEnabled(false);
                attrTypeGroup.setEnabled(false);
                attrTypeOid.setEnabled(false);
                attrTypeOther.setEnabled(false);
                attrField.setEditable(false);
                attrTypeField.setEditable(false);
                wildField.setEditable(false);
                addAttrButton.setEnabled(false);
                addWildButton.setEnabled(false);
                deleteAttrButton.setEnabled(false);
                deleteWildButton.setEnabled(false);
                attrListModel.clear();
                wildListModel.clear();
            }else 
            {
                attrTypeProvider.setEnabled(true);
                attrTypeSession.setEnabled(true);
                attrTypeGroup.setEnabled(true);
                attrTypeOid.setEnabled(true);
                attrTypeOther.setEnabled(true);
                wildField.setEditable(true);
                attrField.setEditable(true);
                if(attrTypeOther.isSelected())
                    attrTypeField.setEditable(true);
                else
                    attrTypeField.setEditable(false);
            }
        }
        
        private Attribute getAttribute()
        {
            byte[] raw = null;
            if(attrTypeOid.isSelected())
            {
                try
                {
                    raw = DOFObjectID.create(attrField.getText()).getBytes();
                }catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this, "The attribute OID value checkbox is set, but the value is not valid standared form", "Invalid OID", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }else
            {
                try
                {
                    raw = DOFUtil.hexStringToBytes(attrField.getText());    
                }catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this, "The attribute OID value checkbox is not set, but the value is not valid Hex string", "Invalid Hex String", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
                
            if(attrTypeProvider.isSelected())
                return Attribute.create(Attribute.PROVIDER, raw);
            else if(attrTypeSession.isSelected())
                return Attribute.create(Attribute.SESSION, raw);
            else if(attrTypeGroup.isSelected())
                return Attribute.create(Attribute.GROUP, raw);
            else
            {
                try
                {
                    int type = Integer.parseInt(attrTypeField.getText());
                    if(type < 0 || type > 255)
                        throw new Exception("too big");
                    return Attribute.create((byte)type, raw);
                }catch(Exception e)
                {
                    JOptionPane.showMessageDialog(this, "The attribute type must be an integer that is grearter than -1 and less than 256", "Invalid Byte String", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src = e.getSource();
            
            if(src instanceof JCheckBox)
            {
                checkBoxChanged();
                return;
            }
            if(src instanceof JRadioButton)
            {
                checkRadioChanged();
                return;
            }
                    
            if(src == addOidButton)
            {
                try
                {
                    DOFObjectID oid = DOFObjectID.create(oidField.getText());
                    oidListModel.addElement(oid);
                    addOidButton.setEnabled(false);
                    applyButton.setEnabled(true);
                }catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(this, "Invalid DOFObjectID", "Invalid OID", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            
            if(src == addIidButton)
            {
                try
                {
                    DOFInterfaceID iid = DOFInterfaceID.create(iidField.getText());
                    iidListModel.addElement(iid);
                    addIidButton.setEnabled(false);
                    applyButton.setEnabled(true);
                }catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(this, "Invalid DOFInterfaceID", "Invalid IID", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            
            if(src == addAttrButton)
            {
                Attribute attr = getAttribute();
                if(attr != null)
                {
                    attrListModel.addElement(attr);
                    addAttrButton.setEnabled(false);
                    applyButton.setEnabled(true);
                }
                return;
            }
            
            if(src == addWildButton)
            {
                try
                {
                    int type = Integer.parseInt(wildField.getText());
                    if(type < 0 || type > 255)
                        throw new Exception("too big");
                    wildListModel.addElement(new Byte((byte)type));
                    addWildButton.setEnabled(false);
                    applyButton.setEnabled(true);
                }catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(this, "The wildcard must be an integer that is grearter than -1 and less than 256", "Invalid Byte String", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            
            if(src == deleteOidButton)
            {
                oidListModel.remove(oidSelectionModel.getLeadSelectionIndex());
                return;
            }
            
            if(src == deleteIidButton)
            {
                iidListModel.remove(iidSelectionModel.getLeadSelectionIndex());
                return;
            }
            
            if(src == deleteAttrButton)
            {
                attrListModel.remove(attrSelectionModel.getLeadSelectionIndex());
                return;
            }
            
            if(src == deleteWildButton)
            {
                wildListModel.remove(wildSelectionModel.getLeadSelectionIndex());
                return;
            }
            
            if(src == applyButton)
            {
                try
                {
                    parentPanel.addPermission(getBinding());
                    dialog.dispose();
                }catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(this, "Unable to build Binding permission: " + ex.getClass().getName() + " : " + ex.getMessage(), "Invalid Binding information", JOptionPane.ERROR_MESSAGE);
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
            JTextField field = docMap.get(e.getDocument());
            if(field == oidField)
                addOidButton.setEnabled(true);
            else if(field == iidField)
                addIidButton.setEnabled(true);
            else if(field == attrField)
                addAttrButton.setEnabled(true);
            else if(field == wildField)
                addWildButton.setEnabled(true);
        }
        
/* ************************************************************************
 * ListSelectionListener implementation
 **************************************************************************/
        @Override
        public void valueChanged(ListSelectionEvent event)
        {
            if (event.getValueIsAdjusting()) 
                return;
            Object src = event.getSource();
            if(src == oidList)
            {
                if(oidSelectionModel.isSelectionEmpty())
                {
                    deleteOidButton.setEnabled(false);
                    return;
                }
                deleteOidButton.setEnabled(true);
                return;
            }
            if(src == iidList)
            {
                if(iidSelectionModel.isSelectionEmpty())
                {
                    deleteIidButton.setEnabled(false);
                    return;
                }
                deleteIidButton.setEnabled(true);
                return;
            }
            if(src == attrList)
            {
                if(attrSelectionModel.isSelectionEmpty())
                {
                    deleteAttrButton.setEnabled(false);
                    return;
                }
                deleteAttrButton.setEnabled(true);
                return;
            }
            if(src == wildList)
            {
                if(wildSelectionModel.isSelectionEmpty())
                {
                    deleteWildButton.setEnabled(false);
                    return;
                }
                deleteWildButton.setEnabled(true);
                return;
            }
        }
    }
}