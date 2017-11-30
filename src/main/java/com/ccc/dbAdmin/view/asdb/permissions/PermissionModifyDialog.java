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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

import org.emitdo.app.as.ASPermission;
import org.emitdo.app.as.ASPermission.Router;
import org.emitdo.app.as.ASPermission.RouterNoTunnel;
import org.emitdo.oal.DOFObjectID.Authentication;
import org.emitdo.oal.security.DOFPermission;
import org.emitdo.oal.security.DOFPermission.ActAsAny;
import org.emitdo.oal.security.DOFPermission.Provider;
import org.emitdo.oal.security.DOFPermission.Requestor;
import org.emitdo.research.app.dbAdmin.AsManagerFrame;
import org.emitdo.research.app.dbAdmin.model.asdb.ScopePermission;
import org.emitdo.research.app.swing.AppliedCallback;
import org.emitdo.research.app.swing.FrameBase;
import org.emitdo.research.app.swing.SwappedFocusListener;

public class PermissionModifyDialog extends JDialog 
{
    public PermissionModifyDialog(AsManagerFrame asManager, Component source, ScopePermission pset) 
    {
        super(asManager, true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(new PermissionModifyPanel(this, asManager, pset));
        setLocationRelativeTo(asManager);
        setMinimumSize(new Dimension(245, 160));
        pack();
        setVisible(true);
    }
    
    static public class PermissionModifyPanel extends JPanel implements DocumentListener,  ActionListener, SwappedFocusListener, AppliedCallback, ListSelectionListener 
    {
        private final int numberOfColumns = 52;
        
        private final AsManagerFrame asManager;
        private final PermissionModifyDialog dialog;
        private final ArrayList<DOFPermission> permissions;
        private final ScopePermission orginalPermissionSet;
        
        private final JTextField identityField;
        private final JTextField scopeField;

        public final JRadioButton typeBinding;
        public final JRadioButton typeRequestor;
        public final JRadioButton typeProvider;
        public final JRadioButton typeDefine;
        public final JRadioButton typeIam;
        public final JRadioButton typeActAs;
        public final JRadioButton typeTunnel;
        public final JRadioButton typeRouter;
        public final JRadioButton typeRouterNoTunnel;

        private final JButton typeAddButton;
        private final JButton typeDeleteButton;
        
        private final JList permList;
        private final DefaultListModel permListModel;
        private final ListSelectionModel permSelectionModel;
        
        private final JButton applyButton;
        private final JButton cancelButton;
        
        public PermissionModifyPanel(PermissionModifyDialog dialog, AsManagerFrame asManager, ScopePermission pset)
        {
            this.dialog = dialog;
            this.asManager = asManager;
            permissions = new ArrayList<DOFPermission>();
            List<DOFPermission> perms = new ArrayList<DOFPermission>();
            if(pset.pset != null)
                perms = pset.pset.getPermissions();
            int size = perms.size();
            for(int i=0; i < size; i++)
                permissions.add(perms.get(i));
            orginalPermissionSet = pset;
            
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(new TitledBorder("Modify permissions"));
            
            AtomicInteger maxWidth = new AtomicInteger();
            JLabel identityLabel = FrameBase.getLabel("Identity:", maxWidth);
            JLabel scopeLabel = FrameBase.getLabel("Scope:", maxWidth);
            
            JLabel typeBindingLabel = FrameBase.getLabel("Binding:", maxWidth);
            JLabel typeRequestorLabel = FrameBase.getLabel("Requestor:", maxWidth);
            JLabel typeProviderLabel = FrameBase.getLabel("Provider:", maxWidth);
            JLabel typeDefineLabel = FrameBase.getLabel("Define:", maxWidth);
            JLabel typeIamLabel = FrameBase.getLabel("Iam:", maxWidth);
            JLabel typeActAsLabel = FrameBase.getLabel("ActAs:", maxWidth);
            JLabel typeTunnelLabel = FrameBase.getLabel("Tunnel:", maxWidth);
            JLabel typeRouterLabel = FrameBase.getLabel("Router:", maxWidth);
            JLabel typeRouterNoTunnelLabel = FrameBase.getLabel("CtRouter:", maxWidth);
            int width = maxWidth.get();
            
            identityField = new JTextField(pset.oid.toStandardString(), numberOfColumns);
            identityField.setEditable(false);
            FrameBase.addTextParamToPanel(identityLabel, identityField, width, -1, "The authentication nodes identity this permission set applies to.", mainPanel, this);
            
            scopeField = new JTextField(""+pset.scope, numberOfColumns);
            FrameBase.addTextParamToPanel(scopeLabel, scopeField, width, -1, "The scope of this permission set.", mainPanel, this);
            
            JPanel permissionSetPanel = new JPanel();
            permissionSetPanel.setLayout(new BoxLayout(permissionSetPanel, BoxLayout.Y_AXIS));
            permissionSetPanel.setBorder(new TitledBorder("Permission Set"));
            
            JPanel panel = new JPanel();
            permissionSetPanel.setLayout(new BoxLayout(permissionSetPanel, BoxLayout.Y_AXIS));
            permListModel = new DefaultListModel();
            permList = new JList(permListModel);
            permList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            permList.setVisibleRowCount(-1);
            permSelectionModel = permList.getSelectionModel();
            permList.addListSelectionListener(this);
            permList.setPreferredSize(new Dimension(640, 60));
            
            JScrollPane scrollPane = new JScrollPane(permList);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(640, 120));
            panel.add(scrollPane);
            permissionSetPanel.add(panel);
            
            panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new TitledBorder("Permission types"));
            
            JPanel innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
            typeBinding = FrameBase.addRadioButtonToPanel(typeBindingLabel, true, true, width, -1, "Binding permission", innerPanel, this);
            typeRequestor = FrameBase.addRadioButtonToPanel(typeRequestorLabel, false, true, width, -1, "Requestor permission", innerPanel, this);
            typeProvider = FrameBase.addRadioButtonToPanel(typeProviderLabel, false, true, width, -1, "Provider permission", innerPanel, this);
            panel.add(innerPanel);
            
            innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
            typeTunnel = FrameBase.addRadioButtonToPanel(typeTunnelLabel, false, true, width, -1, "Tunnel permission", innerPanel, this);
            typeDefine = FrameBase.addRadioButtonToPanel(typeDefineLabel, false, true, width, -1, "Define permission", innerPanel, this);
            typeIam = FrameBase.addRadioButtonToPanel(typeIamLabel, false, true, width, -1, "I am permission", innerPanel, this);
            panel.add(innerPanel);
            
            innerPanel = new JPanel();
            innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
            typeActAs = FrameBase.addRadioButtonToPanel(typeActAsLabel, false, true, width, -1, "Act As permission", innerPanel, this);
            typeRouter = FrameBase.addRadioButtonToPanel(typeRouterLabel, false, true, width, -1, "AS Router permission", innerPanel, this);
            typeRouterNoTunnel = FrameBase.addRadioButtonToPanel(typeRouterNoTunnelLabel, false, true, width, -1, "AS Router no tunnel permission", innerPanel, this);
            panel.add(innerPanel);
            permissionSetPanel.add(panel);
            
            ButtonGroup group = new ButtonGroup();
            group.add(typeBinding);
            group.add(typeRequestor);
            group.add(typeProvider);
            group.add(typeDefine);
            group.add(typeIam);
            group.add(typeActAs);
            group.add(typeTunnel);
            group.add(typeRouter);
            group.add(typeRouterNoTunnel);
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(true);
            
            typeAddButton = FrameBase.addButtonToPanel("Add", true, "Add the selected permission type to the set.", buttonPanel, this);
            typeDeleteButton = FrameBase.addButtonToPanel("Delete", false, "Delete the selected permission types from the set.", buttonPanel, this);
            panel.add(buttonPanel);
            permissionSetPanel.add(panel);
            mainPanel.add(permissionSetPanel, BorderLayout.CENTER);
            
            
            buttonPanel = new JPanel();
            buttonPanel.setOpaque(true);
            
            applyButton = FrameBase.addButtonToPanel("Apply", false, "Commit the current modifications", buttonPanel, this);
            cancelButton = FrameBase.addButtonToPanel("Cancel", false, "Cancel any edits and exit", buttonPanel, this);
            mainPanel.add(buttonPanel);
            
//            panel.setSize(800, 600);
            JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            dataPanel.add(mainPanel);
                  
            scrollPane = new JScrollPane(dataPanel);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane, BorderLayout.CENTER);
            refresh();
            asManager.swapAndSetFocus(this, scopeField);
        }

        public void refresh()
        {
            permListModel.clear();
            typeRequestor.setEnabled(true);
            typeProvider.setEnabled(true);
            typeActAs.setEnabled(true);
            typeRouter.setEnabled(true);
            typeRouterNoTunnel.setEnabled(true);
            
            int size = permissions.size();
            for(int i=0; i < size; i++)
            {
                DOFPermission perm = permissions.get(i);
                permListModel.addElement(perm);
                switch (perm.getPermissionType())
                {
                    case DOFPermission.REQUESTOR:
                        typeRequestor.setEnabled(false);
                        break;
                    case DOFPermission.PROVIDER:
                        typeProvider.setEnabled(false);
                        break;
                    case DOFPermission.ACT_AS:
                        if(perm instanceof ActAsAny)
                            typeActAs.setEnabled(false);
                        break;
                    case ASPermission.ROUTER:
                        typeRouter.setEnabled(false);
                        break;
                    case ASPermission.ROUTER_NO_TUNNEL:
                        typeRouterNoTunnel.setEnabled(false);
                        break;
                }
            }
        }
        
        @Override
        public void focusRequested(Object context)
        {
//            applyButton.setEnabled(false);
//            cancelButton.setEnabled(false);
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
        
        public ScopePermission getOriginalPermissionSet()
        {
            return orginalPermissionSet;
        }
        
        public void addPermission(DOFPermission perm)
        {
            permissions.add(perm);
            refresh();
        }

        public Authentication getIdentity()
        {
            return Authentication.create(identityField.getText());
        }
        
        public int getScope()
        {
            return Integer.parseInt(scopeField.getText());
        }
        
        public List<DOFPermission> getPermissions()
        {
            return permissions;
        }
        
        private void addPermission()
        {
            if(typeBinding.isSelected())
                new BindingPermissionDialog(asManager, this, this);
            else if(typeRequestor.isSelected())
                addPermission(new Requestor());
            else if(typeProvider.isSelected())
                addPermission(new Provider());
            else if(typeDefine.isSelected())
                new DefinePermissionDialog(asManager, this, this);
            else if(typeIam.isSelected())
                new IamPermissionDialog(asManager, this, this);
            else if(typeActAs.isSelected())
                new ActAsPermissionDialog(asManager, this, this);
            else if(typeTunnel.isSelected())
                new TunnelPermissionDialog(asManager, this, this);
            else if(typeRouter.isSelected())
                addPermission(new Router());
            else if(typeRouterNoTunnel.isSelected())
                addPermission(new RouterNoTunnel());
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
            if(src == permList)
            {
                if(permSelectionModel.isSelectionEmpty())
                {
                    typeDeleteButton.setEnabled(false);
                    return;
                }
                typeDeleteButton.setEnabled(true);
                return;
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src = e.getSource();
            if(src == typeAddButton)
            {
                applyButton.setEnabled(true);
                cancelButton.setEnabled(true);
                addPermission();
                return;
            }
            if(src == typeDeleteButton)
            {
                permissions.remove(permSelectionModel.getLeadSelectionIndex());
                applyButton.setEnabled(true);
                cancelButton.setEnabled(true);
                refresh();
                return;
            }
            if(src == applyButton)
            {
                try
                {
                    Integer.parseInt(scopeField.getText());
                    asManager.modifyPermission(this);
                    applyButton.setEnabled(false);
                }catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(this, "The scope field must be an integer value", "Invalid Integer String", JOptionPane.ERROR_MESSAGE);
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