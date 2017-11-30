/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin.view.res;

import java.awt.FlowLayout;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.emitdo.research.app.dbAdmin.model.CannedResData;
import org.emitdo.research.app.swing.FrameBase;

public class InitializeMiscPanel extends JPanel      
{
    private final int numberOfColumns = 10;
    
    public InitializeMiscPanel(CannedResData data)
    {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel miscPanel = new JPanel();
        miscPanel.setOpaque(true);
        miscPanel.setLayout(new BoxLayout(miscPanel, BoxLayout.Y_AXIS));
        miscPanel.setBorder(new TitledBorder("Miscellaneous  data"));

        AtomicInteger maxWidth = new AtomicInteger();
        JLabel failOnAsExistsLabel = FrameBase.getLabel("Fail on AS exists:", maxWidth);
        JLabel deviceScopeLabel = FrameBase.getLabel("Device scope:", maxWidth);
        JLabel factoryScopeLabel = FrameBase.getLabel("Factory scope:", maxWidth);
        JLabel managersScopeLabel = FrameBase.getLabel("Managers scope:", maxWidth);
        int width = maxWidth.get();

        JTextField deviceScopeField = new JTextField(""+data.deviceScopeStartIndex, numberOfColumns);
        FrameBase.addTextParamToPanel(deviceScopeLabel, deviceScopeField, width, -1, "The starting index for platform domain scope's that will be assigned to device's", miscPanel);
        deviceScopeField.setEnabled(false);
        
        JTextField factoryScopeField = new JTextField(""+data.factoryScope, numberOfColumns);
        FrameBase.addTextParamToPanel(factoryScopeLabel, factoryScopeField, width, -1, "The factory domain scope that factory devices's Binding permissions will be granted", miscPanel);
        factoryScopeField.setEnabled(false);
        
        JTextField managersScopeField = new JTextField(""+data.managersScope, numberOfColumns);
        FrameBase.addTextParamToPanel(managersScopeLabel, managersScopeField, width, -1, "The platform domain scope that managers devices's ResidentialPermission.All will be granted", miscPanel);
        managersScopeField.setEnabled(false);
        
        @SuppressWarnings("unused")
        JCheckBox failOnAsExistsCheck = FrameBase.addCheckBoxToPanel(failOnAsExistsLabel, data.failOnAsExists, false, width, -1, "Fail if RES managers/devices OID's already exist as auth nodes in the AS", miscPanel);

        JPanel miscDataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        miscDataPanel.add(miscPanel);
        add(miscDataPanel);
    }
}
