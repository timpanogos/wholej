/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package com.ccc.wholej.swing.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

//import org.emitdo.research.app.dbAdmin.ConnectionControl;
import com.ccc.tools.app.swing.FrameBase;
import com.ccc.tools.app.swing.SwappedFocusListener;
import com.ccc.wholej.WholejData;
import com.ccc.wholej.swing.Wholej;
import com.ccc.wholej.swing.Wholej.ConnType;
import com.ccc.wholej.swing.Wholej.DomainType;
import com.ccc.wholej.swing.Wholej.VendorType;
//import org.emitdo.research.app.dbAdmin.model.ConnectionData;
//import org.emitdo.research.app.swing.FrameBase;
//import org.emitdo.research.app.swing.SwappedFocusListener;

//    public enum BattleShip
//    {
//        Apocalypse(97100000), Armageddon(105200000), Abaddon(103200000),    // ammar
//        Scorpion(103600000), Rokh(105300000), Raven(99300000),              // caldari
//        Megathron(98400000), Dominix(100250000), Hyperion(100200000),       // gallente
//        Typhoon(100600000), Maelstrom(103600000), Tempest(99500000);        // minmatar



public class HolePanel extends JPanel implements ActionListener, DocumentListener, SwappedFocusListener, WindowFocusListener, KeyListener
{
    private final int numberOfColumns = 10;
    private final Wholej wholej;

    private JRadioButton apocalypseRadio;
    private JRadioButton armageddonRadio;
    private JRadioButton abaddonRadio;

    private JRadioButton scorpionRadio;
    private JRadioButton rokhRadio;
    private JRadioButton ravenRadio;
    
    private JRadioButton megathronRadio;
    private JRadioButton dominixRadio;
    private JRadioButton hyperionRadio;

    private JRadioButton typhoonRadio;
    private JRadioButton maelstromRadio;
    private JRadioButton tempestRadio;

    private JRadioButton lightRadio;
    private JRadioButton heavyRadio;
    private JRadioButton higgsLightRadio;
    private JRadioButton higgsHeavyRadio;

    private JTextField wormholeTypeField;
    private JTextField bsLightMassField;
    private JTextField bsHeavyMassField;
    private JTextField bsHiggsLightMassField;
    private JTextField bsHigssHeavyMassField;

    private JTextField whClassField;
    private JTextField whMaxPossibleMassField;
    private JTextField whMaxJumpMassField;
    private JTextField whMassDownField;
    private JTextField whMassCriticalField;
    private JTextField whMassLeftField;
    private JTextField suggestedMovesField;
    private JTextField whLifeField;

    private JButton submitButton;
    private JButton massDownButton;
    private JButton jumpButton;

    private transient WholejData.BattleShip selectedBs;
    private transient MassModifier selectedMassModifier;
    private final AtomicLong massDownMass;

    private final AtomicLong massRemaining;
    private final AtomicBoolean painted;
    
    public HolePanel(Wholej wholej)
    {
        this.wholej = wholej;
        painted = new AtomicBoolean(false);
        massRemaining = new AtomicLong();
        massDownMass = new AtomicLong();
        
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new TitledBorder("Crush Statistics"));

        JPanel bsPanel = new JPanel();
        bsPanel.setLayout(new BoxLayout(bsPanel, BoxLayout.Y_AXIS));
        bsPanel.setBorder(new TitledBorder("Battleships"));

        AtomicInteger maxWidth = new AtomicInteger();
        JLabel apocalypseLabel = FrameBase.getLabel(WholejData.BattleShip.Apocalypse.name() + ":", maxWidth);
        JLabel armageddonLabel = FrameBase.getLabel(WholejData.BattleShip.Armageddon.name() + ":", maxWidth);
        JLabel abaddonLabel = FrameBase.getLabel(WholejData.BattleShip.Abaddon.name() + ":", maxWidth);

        JLabel scorpionLabel = FrameBase.getLabel(WholejData.BattleShip.Scorpion.name() + ":", maxWidth);
        JLabel rokhLabel = FrameBase.getLabel(WholejData.BattleShip.Rokh.name() + ":", maxWidth);
        JLabel ravenLabel = FrameBase.getLabel(WholejData.BattleShip.Raven.name() + ":", maxWidth);
        JLabel megathronLabel = FrameBase.getLabel(WholejData.BattleShip.Megathron.name() + ":", maxWidth);
        JLabel dominixLabel = FrameBase.getLabel(WholejData.BattleShip.Dominix.name() + ":", maxWidth);
        JLabel hyperionLabel = FrameBase.getLabel(WholejData.BattleShip.Hyperion.name() + ":", maxWidth);
        JLabel typhoonLabel = FrameBase.getLabel(WholejData.BattleShip.Typhoon.name() + ":", maxWidth);
        JLabel maelstromLabel = FrameBase.getLabel(WholejData.BattleShip.Maelstrom.name() + ":", maxWidth);
        JLabel tempestLabel = FrameBase.getLabel(WholejData.BattleShip.Tempest.name() + ":", maxWidth);
        int bsNameWidth = maxWidth.get();
        maxWidth.set(0);

        JLabel lightLabel = FrameBase.getLabel("Light:", maxWidth);
        JLabel heavyLabel = FrameBase.getLabel("Heavy:", maxWidth);
        JLabel higgsLightLabel = FrameBase.getLabel("Higgs Light:", maxWidth);
        JLabel higgsHeavyLabel = FrameBase.getLabel("Higgs Heavy:", maxWidth);
        int massModWidth = maxWidth.get();
        maxWidth.set(0);

        JLabel bsLightLabel = FrameBase.getLabel("Light mass:", maxWidth);
        JLabel bsHeavyLabel = FrameBase.getLabel("Heavy mass:", maxWidth);
        JLabel bsHiggsLightLabel = FrameBase.getLabel("Higgs light mass:", maxWidth);
        JLabel bsHiggsHeavyLabel = FrameBase.getLabel("Higgs heavy mass:", maxWidth);
        int bsStatsWidth = maxWidth.get();
        maxWidth.set(0);

        JLabel wormholeTypeLabel = FrameBase.getLabel("Wormhole Type:", maxWidth);
        int whTypeWidth = maxWidth.get();
        maxWidth.set(0);

        JLabel whClassLabel = FrameBase.getLabel("Class:", maxWidth);
        JLabel whMaxMassLabel = FrameBase.getLabel("Max Possible Mass:", maxWidth);
        JLabel whMaxJumpMassLabel = FrameBase.getLabel("Max Jump Mass:", maxWidth);
        JLabel whMassDownLabel = FrameBase.getLabel("Mass down:", maxWidth);
        JLabel whMassCritLabel = FrameBase.getLabel("Mass critical:", maxWidth);
        JLabel whMassLeftLabel = FrameBase.getLabel("Mass remaining:", maxWidth);
        JLabel whSuggestedLabel = FrameBase.getLabel("Suggested: ", maxWidth);
        JLabel whLifeLabel = FrameBase.getLabel("Max Life:", maxWidth);
        int whStatsWidth = maxWidth.get();

        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBorder(new TitledBorder("Amarr"));
        apocalypseRadio = FrameBase.addRadioButtonToPanel(apocalypseLabel, false, true, bsNameWidth, "Amarr Apocalypse Battleship", rowPanel, this);
        armageddonRadio = FrameBase.addRadioButtonToPanel(armageddonLabel, false, true, bsNameWidth, "Amarr Apocalypse Battleship", rowPanel, this);
        abaddonRadio = FrameBase.addRadioButtonToPanel(abaddonLabel, false, true, bsNameWidth, "Amarr Apocalypse Battleship", rowPanel, this);
        bsPanel.add(rowPanel);

        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBorder(new TitledBorder("Caldari"));
        scorpionRadio = FrameBase.addRadioButtonToPanel(scorpionLabel, false, true, bsNameWidth,  "Caldari Scorpion Battleship", rowPanel, this);
        rokhRadio = FrameBase.addRadioButtonToPanel(rokhLabel, false, true, bsNameWidth,  "Caldari Rokh Battleship", rowPanel, this);
        ravenRadio = FrameBase.addRadioButtonToPanel(ravenLabel, false, true, bsNameWidth, "Caldari Raven Battleship", rowPanel, this);
        bsPanel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBorder(new TitledBorder("Gallente"));
        megathronRadio = FrameBase.addRadioButtonToPanel(megathronLabel, false, true, bsNameWidth, "Gallente Megathron Battleship", rowPanel, this);
        dominixRadio = FrameBase.addRadioButtonToPanel(dominixLabel, true, true, bsNameWidth, "Gallente Dominix Battleship", rowPanel, this);
        hyperionRadio = FrameBase.addRadioButtonToPanel(hyperionLabel, false, true, bsNameWidth, "Gallente Hyperion Battleship", rowPanel, this);
        bsPanel.add(rowPanel);
        
        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBorder(new TitledBorder("Minmatar"));
        typhoonRadio = FrameBase.addRadioButtonToPanel(typhoonLabel, false, true, bsNameWidth, "Minmatar Typhoon Battleship", rowPanel, this);
        maelstromRadio = FrameBase.addRadioButtonToPanel(maelstromLabel, false, true, bsNameWidth, "Minmatar Maelstrom Battleship", rowPanel, this);
        tempestRadio = FrameBase.addRadioButtonToPanel(tempestLabel, false, true, bsNameWidth, "Minmatar Typhoon Battleship", rowPanel, this);
        bsPanel.add(rowPanel);

        ButtonGroup group = new ButtonGroup();
        group.add(apocalypseRadio);
        group.add(armageddonRadio);
        group.add(abaddonRadio);
        group.add(scorpionRadio);
        group.add(rokhRadio);
        group.add(ravenRadio);
        group.add(megathronRadio);
        group.add(dominixRadio);
        group.add(hyperionRadio);
        group.add(typhoonRadio);
        group.add(maelstromRadio);
        group.add(tempestRadio);
        mainPanel.add(bsPanel);

        JPanel bsStatsPanel = new JPanel();
        bsStatsPanel.setLayout(new BoxLayout(bsStatsPanel, BoxLayout.Y_AXIS));
        bsLightMassField = new JTextField("", numberOfColumns);
        bsLightMassField.setEditable(false);
        FrameBase.addTextParamToPanel(bsLightLabel, bsLightMassField, bsStatsWidth, -1, "Selected Battleship light mass", bsStatsPanel);
        bsHeavyMassField = new JTextField("", numberOfColumns);
        bsHeavyMassField.setEditable(false);
        FrameBase.addTextParamToPanel(bsHeavyLabel, bsHeavyMassField, bsStatsWidth, -1, "Selected Battleship heavy mass", bsStatsPanel);
        bsHiggsLightMassField = new JTextField("", numberOfColumns);
        bsHiggsLightMassField.setEditable(false);
        FrameBase.addTextParamToPanel(bsHiggsLightLabel, bsHiggsLightMassField, bsStatsWidth, -1, "Selected Battleship higgs light mass", bsStatsPanel);
        bsHigssHeavyMassField = new JTextField("", numberOfColumns);
        bsHigssHeavyMassField.setEditable(false);
        FrameBase.addTextParamToPanel(bsHiggsHeavyLabel, bsHigssHeavyMassField, bsStatsWidth, -1, "Selected Battleship higgs heavy mass", bsStatsPanel);
        mainPanel.add(bsStatsPanel);

        JPanel massModPanel = new JPanel();
        massModPanel.setLayout(new BoxLayout(massModPanel, BoxLayout.Y_AXIS));
        massModPanel.setBorder(new TitledBorder("Jump mass modifiers"));

        rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        higgsLightRadio = FrameBase.addRadioButtonToPanel(higgsLightLabel, true, true, massModWidth, "Battleship without higgs jumping light", rowPanel, this);
        selectedMassModifier = MassModifier.HiggsLight;
        higgsHeavyRadio = FrameBase.addRadioButtonToPanel(higgsHeavyLabel, false, true, massModWidth,  "Battleship without higgs jumping light", rowPanel, this);
        lightRadio = FrameBase.addRadioButtonToPanel(lightLabel, false, true, massModWidth, "Battleship without higgs jumping light", rowPanel, this);
        heavyRadio = FrameBase.addRadioButtonToPanel(heavyLabel, false, true, massModWidth, "Battleship without higgs jumping light", rowPanel, this);
        massModPanel.add(rowPanel);

        group = new ButtonGroup();
        group.add(lightRadio);
        group.add(heavyRadio);
        group.add(higgsLightRadio);
        group.add(higgsHeavyRadio);
        mainPanel.add(massModPanel);

        wormholeTypeField = new JTextField("", numberOfColumns);
        wormholeTypeField.addKeyListener(this);
        FrameBase.addTextParamToPanel(wormholeTypeLabel, wormholeTypeField, whTypeWidth, -1, "Enter the wormhole name/type", mainPanel, this);

        whMassLeftField = new JTextField("", numberOfColumns);
        whMassLeftField.setEditable(false);
        FrameBase.addTextParamToPanel(whMassLeftLabel, whMassLeftField, whStatsWidth, -1, "The wormhole's estimated mass remaining", mainPanel);
        suggestedMovesField = new JTextField("", numberOfColumns);
        suggestedMovesField.setEditable(false);
        FrameBase.addTextParamToPanel(whSuggestedLabel, suggestedMovesField, whStatsWidth, -1, "The suggested mass modifier jumps to make", mainPanel);

        whClassField = new JTextField("", numberOfColumns);
        whClassField.setEditable(false);
        FrameBase.addTextParamToPanel(whClassLabel, whClassField, whStatsWidth, -1, "The wormhole's class", mainPanel);
        whMaxPossibleMassField = new JTextField("", numberOfColumns);
        whMaxPossibleMassField.setEditable(false);
        FrameBase.addTextParamToPanel(whMaxMassLabel, whMaxPossibleMassField, whStatsWidth, -1, "The wormhole's maximum possible mass", mainPanel);
        whMaxJumpMassField = new JTextField("", numberOfColumns);
        whMaxJumpMassField.setEditable(false);
        FrameBase.addTextParamToPanel(whMaxJumpMassLabel, whMaxJumpMassField, whStatsWidth, -1, "The wormhole's maximum per jump mass", mainPanel);
        whMassDownField = new JTextField("", numberOfColumns);
        whMassDownField.setEditable(false);
        FrameBase.addTextParamToPanel(whMassDownLabel, whMassDownField, whStatsWidth, -1, "The wormhole's mass down range", mainPanel);
        whMassCriticalField = new JTextField("", numberOfColumns);
        whMassCriticalField.setEditable(false);
        FrameBase.addTextParamToPanel(whMassCritLabel, whMassCriticalField, whStatsWidth, -1, "The wormhole's mass critical range", mainPanel);
        whLifeField = new JTextField("", numberOfColumns);
        whLifeField.setEditable(false);
        FrameBase.addTextParamToPanel(whLifeLabel, whLifeField, whStatsWidth, -1, "The wormhole's maximum life in hours", mainPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        submitButton = FrameBase.addButtonToPanel("Submit", false, "Obtain the given Wormhole Type information", buttonPanel, this);
        massDownButton = FrameBase.addButtonToPanel("Mass Down", true, "The wormhole is mass down", buttonPanel, this);
        jumpButton = FrameBase.addButtonToPanel("Jump", false, "Recalculate stats based on a jump with the given mass modification option", buttonPanel, this);
        mainPanel.add(buttonPanel);
        
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dataPanel.add(mainPanel);
        
        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(575, 520));
        add(scrollPane, BorderLayout.CENTER);
        setState();
        adjustForSelection();
        wholej.swapAndSetFocus(this, wormholeTypeField);
    }

    public void refresh()
    {
        setState();
        adjustForSelection();
    }
    
    @Override
    public void focusRequested(Object context)
    {
        painted.set(true);
    }

    private void associateState(DomainType dtype, VendorType vtype, JRadioButton radio, JCheckBox check)
    {
//        ConnectionData ci = control.getConnection(StorageType.As, vtype, dtype, ConnType.Jdbc);
//        ci.checkBox = check;
//        ci.radioButton = radio;
//        ci = control.getConnection(StorageType.As, vtype, dtype, ConnType.Web);
//        ci.checkBox = check;
//        ci.radioButton = radio;
    }
    
    private void setState()
    {
//        for(int i=1; i < 5; i++)
//        {
//            DomainType dtype = DomainType.getType(i);
//            for(int j=0; j < 2; j++)
//            {
//                VendorType vtype = VendorType.JavaDb;
//                if(j == 1)
//                    vtype = VendorType.Oracle;
//
//                ConnType ctype = ConnType.Jdbc;
//                if(!jdbcRadio.isSelected())
//                    ctype = ConnType.Web;
////                ConnectionData ci = control.getConnection(StorageType.As, vtype, dtype, ctype);
////                ci.checkBox.setSelected(ci.connected);
////                ci.radioButton.setEnabled(ci.supported);
//            }
//        }
    }
    
    private void adjustForSelection()
    {
        if(apocalypseRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Apocalypse);
        else if(armageddonRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Apocalypse);
        else if(rokhRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Rokh);
        else if(ravenRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Raven);
        else if(abaddonRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Abaddon);
        else if(scorpionRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Scorpion);
        else if(megathronRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Megathron);
        else if(dominixRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Dominix);
        else if(hyperionRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Hyperion);
        else if(typhoonRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Typhoon);
        else if(maelstromRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Maelstrom);
        else if(tempestRadio.isSelected())
            setSelectedBs(WholejData.BattleShip.Tempest);


        if(lightRadio.isSelected())
        {
            selectedMassModifier = MassModifier.Light;
            return;
        }
        if(heavyRadio.isSelected())
        {
            selectedMassModifier = MassModifier.Heavy;
            return;
        }
        if(higgsLightRadio.isSelected())
        {
            selectedMassModifier = MassModifier.HiggsLight;
            return;
        }
        if(higgsHeavyRadio.isSelected())
        {
            selectedMassModifier = MassModifier.HiggsHeavy;
            return;
        }

        painted.set(false);
        setState();
        wholej.swapAndSetFocus(this, wormholeTypeField);
        wormholeTypeField.setEnabled(true);
//        jumpButton.setEnabled(false);
//        submitButton.setEnabled(false);
    }

    private void setSelectedBs(WholejData.BattleShip bs)
    {
        bsLightMassField.setText(WholejData.HoleInfo.getNormalizedMass(bs.getLight()));
        bsHeavyMassField.setText(WholejData.HoleInfo.getNormalizedMass(bs.getHeavy()));
        bsHiggsLightMassField.setText(WholejData.HoleInfo.getNormalizedMass(bs.getHiggsLight()));
        bsHigssHeavyMassField.setText(WholejData.HoleInfo.getNormalizedMass(bs.getHiggsHeavy()));
        selectedBs = bs;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();
        if(src instanceof JRadioButton)
        {
            adjustForSelection();
            return;
        }
        if(src == submitButton)
        {
            String type = wormholeTypeField.getText().toUpperCase();
            WholejData.HoleInfo info = wholej.getWormHoleData().getWormholeInfo(type);
            if(info == null)
            {
                FrameBase.displayException(null, wholej, "Not found", "The given wormhole type: " + type + " is not known", null);
                submitButton.setEnabled(false);
                jumpButton.setEnabled(false);
                return;
            }
            whClassField.setText(info.type.getClassName());
            whMaxPossibleMassField.setText(info.getTotalMassRange());
            whMaxJumpMassField.setText(info.getMaxJumpMass());
            whMassDownField.setText(info.getMassDownRange());
            massDownMass.set(info.getMassDown());
            whMassCriticalField.setText(info.getMassCriticalRange());
            massRemaining.set(info.maxStableMass.getMass());
            whMassLeftField.setText(WholejData.HoleInfo.getNormalizedMass(massRemaining.get()));
            whLifeField.setText(info.maxStableTime.getHours());
            submitButton.setEnabled(false);
            jumpButton.setEnabled(true);
            return;
        }
        if(src == jumpButton)
        {
            long mass = selectedMassModifier.getMass(selectedBs);
            long previousMass = massRemaining.get();
            mass = previousMass - mass;
            massRemaining.set(mass);
            whMassLeftField.setText(WholejData.HoleInfo.getNormalizedMass(mass));
            return;
        }
        if(src == massDownButton)
        {
            massRemaining.set(massDownMass.get());
            whMassLeftField.setText(WholejData.HoleInfo.getNormalizedMass(massRemaining.get()));
            return;
        }
    }

    private void setMassRemaining(boolean isJump)
    {
        long mass = selectedMassModifier.getMass(selectedBs);
        long previousMass = massRemaining.get();
        if(isJump)
        {
            mass = previousMass - mass;
            massRemaining.set(mass);
        }
        whMassLeftField.setText(WholejData.HoleInfo.getNormalizedMass(mass));

    }
    
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        if(!painted.get())
            return;
        submitButton.setEnabled(true);
    }

    @Override public void insertUpdate(DocumentEvent e){changedUpdate(null);}
    @Override public void removeUpdate(DocumentEvent e){changedUpdate(null);}

    @Override
    public void windowGainedFocus(WindowEvent e)
    {
        refresh();
    }

    @Override
    public void windowLostFocus(WindowEvent e)
    {
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        if(e.getKeyChar() == '\n')
        {
            ActionEvent ae = new ActionEvent(submitButton, 0, null);
            actionPerformed(ae);
            return;
        }
        jumpButton.setEnabled(false);
        massRemaining.set(0);
        whMassLeftField.setText(WholejData.HoleInfo.getNormalizedMass(massRemaining.get()));
    }

    @Override
    public void keyPressed(KeyEvent e){}

    @Override
    public void keyReleased(KeyEvent e){}

    private enum MassModifier
    {
        Light, Heavy, HiggsLight, HiggsHeavy;

        public long getMass(WholejData.BattleShip bs)
        {
            if(this == Light)
                return bs.getLight();
            if(this == Heavy)
                return bs.getHeavy();
            if(this == HiggsLight)
                return bs.getHiggsLight();
            return bs.getHiggsHeavy();
        }
    }
}
