/*
**  Copyright (c) 2016, Chad Adams.
**
**  This program is free software: you can redistribute it and/or modify
**  it under the terms of the GNU Lesser General Public License as 
**  published by the Free Software Foundation, either version 3 of the 
**  License, or any later version.
**
**  This program is distributed in the hope that it will be useful,
**  but WITHOUT ANY WARRANTY; without even the implied warranty of
**  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
**  GNU General Public License for more details.

**  You should have received copies of the GNU GPLv3 and GNU LGPLv3
**  licenses along with this program.  If not, see http://www.gnu.org/licenses
*/
package com.ccc.wholej;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("javadoc")
public class WholejData
{
    private final Map<String, HoleInfo> map;

    public WholejData()
    {
        map = new HashMap<>();
        init();
    }

    public HoleInfo getWormholeInfo(String type)
    {
        return map.get(type);
    }

    private void init()
    {
        map.put("A009", new HoleInfo("A009", WhClass.Thirteen, MaxStableMass.FiveK, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("A239", new HoleInfo("A239", WhClass.Ls, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("A641", new HoleInfo("A641", WhClass.Hs, MaxStableMass.Two, MaxJumpMass.OneK, MaxStableTime.Short));
        map.put("A982", new HoleInfo("A982", WhClass.Six, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("B041", new HoleInfo("B041", WhClass.Six, MaxStableMass.Three, MaxJumpMass.OneK, MaxStableTime.Long));
        map.put("B274", new HoleInfo("B274", WhClass.Hs, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("B449", new HoleInfo("B449", WhClass.Hs, MaxStableMass.Two, MaxJumpMass.OneK, MaxStableTime.Short));
        map.put("B520", new HoleInfo("B520", WhClass.Hs, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Long));
        map.put("B735", new HoleInfo("B735", WhClass.Barbican, MaxStableMass.SevenFifty, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("C008", new HoleInfo("C008", WhClass.Five, MaxStableMass.OneK, MaxJumpMass.Five, MaxStableTime.Short));
        map.put("C125", new HoleInfo("C125", WhClass.Two, MaxStableMass.OneK, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("C140", new HoleInfo("C140", WhClass.Ls, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Medium));
        map.put("C247", new HoleInfo("C247", WhClass.Three, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("C248", new HoleInfo("C248", WhClass.Null, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Short));
        map.put("C391", new HoleInfo("C391", WhClass.Ls, MaxStableMass.Three, MaxJumpMass.OneK, MaxStableTime.Long));
        map.put("C414", new HoleInfo("C414", WhClass.Conflux, MaxStableMass.SevenFifty, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("D364", new HoleInfo("D364", WhClass.Two, MaxStableMass.OneK, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("D382", new HoleInfo("D382", WhClass.Two, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("D792", new HoleInfo("D792", WhClass.Hs, MaxStableMass.Three, MaxJumpMass.OneK, MaxStableTime.Medium));
        map.put("D845", new HoleInfo("D845", WhClass.Hs, MaxStableMass.FiveK, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("E004", new HoleInfo("E004", WhClass.One, MaxStableMass.OneK, MaxJumpMass.Five, MaxStableTime.Short));
        map.put("E175", new HoleInfo("E175", WhClass.Four, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("E545", new HoleInfo("E545", WhClass.Null, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("E587", new HoleInfo("E587", WhClass.Null, MaxStableMass.Three, MaxJumpMass.OneK, MaxStableTime.Short));
        map.put("F135", new HoleInfo("F135", WhClass.Thera, MaxStableMass.SevenFifty, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("F355", new HoleInfo("F355", WhClass.Thera, MaxStableMass.One, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("G008", new HoleInfo("G008", WhClass.Six, MaxStableMass.OneK, MaxJumpMass.Five, MaxStableTime.Short));
        map.put("G024", new HoleInfo("G024", WhClass.Two, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("H121", new HoleInfo("H121", WhClass.One, MaxStableMass.Five, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("H296", new HoleInfo("H296", WhClass.Five, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Medium));
        map.put("H900", new HoleInfo("H900", WhClass.Five, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("I182", new HoleInfo("I182", WhClass.Two, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("J244", new HoleInfo("J244", WhClass.Ls, MaxStableMass.OneK, MaxJumpMass.Twenty, MaxStableTime.Medium));
        map.put("K162", new HoleInfo("K162", WhClass.Unknown, null, null, null));
        map.put("K329", new HoleInfo("K329", WhClass.Null, MaxStableMass.FiveK, MaxJumpMass.OneEight, MaxStableTime.Short));
        map.put("K346", new HoleInfo("K346", WhClass.Null, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("L005", new HoleInfo("L005", WhClass.Two, MaxStableMass.OneK, MaxJumpMass.Five, MaxStableTime.Short));
        map.put("L031", new HoleInfo("L031", WhClass.Thera, MaxStableMass.Three, MaxJumpMass.OneK, MaxStableTime.Short));
        map.put("L477", new HoleInfo("L477", WhClass.Three, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("L614", new HoleInfo("L614", WhClass.Five, MaxStableMass.OneK, MaxJumpMass.Twenty, MaxStableTime.Medium));
        map.put("M001", new HoleInfo("M001", WhClass.Four, MaxStableMass.OneK, MaxJumpMass.Five, MaxStableTime.Short));
        map.put("M164", new HoleInfo("M164", WhClass.Thera, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("M267", new HoleInfo("M267", WhClass.Three, MaxStableMass.OneK, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("M555", new HoleInfo("M555", WhClass.Five, MaxStableMass.Three, MaxJumpMass.OneK, MaxStableTime.Medium));
        map.put("M609", new HoleInfo("M609", WhClass.Four, MaxStableMass.OneK, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("N062", new HoleInfo("N062", WhClass.Five, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("N110", new HoleInfo("N110", WhClass.Hs, MaxStableMass.OneK, MaxJumpMass.Twenty, MaxStableTime.Medium));
        map.put("N290", new HoleInfo("N290", WhClass.Ls, MaxStableMass.Three, MaxJumpMass.OneEight, MaxStableTime.Medium));
        map.put("N432", new HoleInfo("N432", WhClass.Five, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Medium));
        map.put("N766", new HoleInfo("N766", WhClass.Two, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("N770", new HoleInfo("N770", WhClass.Five, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("N944", new HoleInfo("N944", WhClass.Ls, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Medium));
        map.put("N968", new HoleInfo("N968", WhClass.Three, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("O128", new HoleInfo("O128", WhClass.Four, MaxStableMass.OneK, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("O477", new HoleInfo("O477", WhClass.Three, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("O883", new HoleInfo("O883", WhClass.Three, MaxStableMass.OneK, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("P060", new HoleInfo("P060", WhClass.One, MaxStableMass.Five, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("Q003", new HoleInfo("Q003", WhClass.Null, MaxStableMass.OneK, MaxJumpMass.Five, MaxStableTime.Short));
        map.put("Q063", new HoleInfo("Q063", WhClass.Hs, MaxStableMass.Five, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("Q317", new HoleInfo("Q317", WhClass.One, MaxStableMass.Five, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("R051", new HoleInfo("R051", WhClass.Ls, MaxStableMass.Three, MaxJumpMass.OneK, MaxStableTime.Short));
        map.put("R259", new HoleInfo("R259", WhClass.Redoubt, MaxStableMass.SevenFifty, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("R474", new HoleInfo("R474", WhClass.Six, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("R943", new HoleInfo("R943", WhClass.Two, MaxStableMass.SevenFifty, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("S047", new HoleInfo("S047", WhClass.Hs, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("S199", new HoleInfo("S199", WhClass.Null, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Short));
        map.put("S804", new HoleInfo("S804", WhClass.Six, MaxStableMass.OneK, MaxJumpMass.Twenty, MaxStableTime.Medium));
        map.put("S877", new HoleInfo("S877", WhClass.Sentinel, MaxStableMass.SevenFifty, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("T405", new HoleInfo("T405", WhClass.Four, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("T458", new HoleInfo("T458", WhClass.Thera, MaxStableMass.Five, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("U210", new HoleInfo("U210", WhClass.Ls, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("U319", new HoleInfo("U319", WhClass.Six, MaxStableMass.Three, MaxJumpMass.OneEight, MaxStableTime.Long));
        map.put("U574", new HoleInfo("U574", WhClass.Six, MaxStableMass.Three, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("V283", new HoleInfo("V283", WhClass.Null, MaxStableMass.Three, MaxJumpMass.OneK, MaxStableTime.Short));
        map.put("V301", new HoleInfo("V301", WhClass.One, MaxStableMass.Five, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("V753", new HoleInfo("V753", WhClass.Six, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Medium));
        map.put("V898", new HoleInfo("V898", WhClass.Ls, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("V911", new HoleInfo("V911", WhClass.Five, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Medium));
        map.put("V928", new HoleInfo("V928", WhClass.Vidette, MaxStableMass.SevenFifty, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("W237", new HoleInfo("W237", WhClass.Six, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Medium));
        map.put("X702", new HoleInfo("X702", WhClass.Three, MaxStableMass.OneK, MaxJumpMass.ThreeH, MaxStableTime.Medium));
        map.put("X877", new HoleInfo("X877", WhClass.Four, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("Y683", new HoleInfo("Y683", WhClass.Four, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("Y790", new HoleInfo("Y790", WhClass.One, MaxStableMass.Five, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("Z006", new HoleInfo("Z006", WhClass.Three, MaxStableMass.OneK, MaxJumpMass.Five, MaxStableTime.Short));
        map.put("Z060", new HoleInfo("Z060", WhClass.Null, MaxStableMass.OneK, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("Z142", new HoleInfo("Z142", WhClass.Null, MaxStableMass.Three, MaxJumpMass.OneThreeFifty, MaxStableTime.Short));
        map.put("Z457", new HoleInfo("Z457", WhClass.Four, MaxStableMass.Two, MaxJumpMass.ThreeH, MaxStableTime.Short));
        map.put("Z647", new HoleInfo("Z647", WhClass.One, MaxStableMass.Five, MaxJumpMass.Twenty, MaxStableTime.Short));
        map.put("Z971", new HoleInfo("Z971", WhClass.One, MaxStableMass.One, MaxJumpMass.Twenty, MaxStableTime.Short));
    }

    public enum WhClass
    {
        Unknown("unknown"), Null("NULL"), Ls("Low-Sec"), Hs("Hi-Sec"),
        One("C1"), Two("C2"), Three("C3"), Four("C4"), Five("C5"), Six("C6"),
        Thirteen("C13"), Thera("Thera"),
        Barbican("Barbican"), Conflux("Conflux"), Vidette("Vidette"), Sentinel("Sentinel"), Redoubt("Redoubt");

        WhClass(String value)
        {
            this.value = value;
        }
        public String getClassName()
        {
            return value;
        }
        private String value;
    }

    public enum MaxStableMass
    {
        One(100), Five(500), SevenFifty(750), OneK(1000), Two(2000), Three(3000), Four(4000), FiveK(5000);
        MaxStableMass(long gg)
        {
            this.gg = gg;
        }
        public long getMass() { return gg * 1000000; }
        private long gg;
    }

    private enum MaxJumpMass
    {
        Five(5), Twenty(20), OneK(1000), OneThreeFifty(1350), OneEight(1800), ThreeH(300);
        MaxJumpMass(long gg)
        {
            this.gg = gg;
        }
        private long getMass() { return gg * 1000000; }
        private long gg;
    }

    public enum MaxStableTime
    {
        Short(16), Medium(24), Long(48);
        MaxStableTime(int value)
        {
            this.value = value;
        }
        public String getHours() { return value + "hrs"; }
        private int value;
    }

    public enum BattleShip
    {
        Apocalypse(97100000), Armageddon(105200000), Abaddon(103200000),    // ammar
        Scorpion(103600000), Rokh(105300000), Raven(99300000),              // caldari
        Megathron(98400000), Dominix(100250000), Hyperion(100200000),       // gallente
        Typhoon(100600000), Maelstrom(103600000), Tempest(99500000);        // minmatar

        BattleShip(long value)
        {
            this.value = value;
        }
        public long getLight()
        {
            return value;
        }
        public long getHiggsLight()
        {
            return value * 2;
        }
        public long getHeavy()
        {
            return value + (value / 2);
        }
        public long getHiggsHeavy()
        {
            return (value * 2) + (value / 2);
        }
        private long value;
    }

    public static class HoleInfo
    {
        private static String MassDecimalFormat = "###,###.###";
        public final String name;
        public final WhClass type;
        public final MaxStableMass maxStableMass;
        public final MaxJumpMass maxJumpMass;
        public final MaxStableTime maxStableTime;

        private HoleInfo(String name, WhClass type, MaxStableMass maxStableMass, MaxJumpMass maxJumpMass, MaxStableTime maxStableTime)
        {
            this.name = name;
            this.type = type;
            this.maxStableMass = maxStableMass;
            this.maxJumpMass = maxJumpMass;
            this.maxStableTime = maxStableTime;
        }

        public static String getNormalizedMass(long mass)
        {
            double dmass = mass / 1000000.0;
            DecimalFormat df = new DecimalFormat(MassDecimalFormat);
            return df.format(dmass) + "gg";

//            if(mass >= 1000000000)
//                return (mass / 1000000.0) + "gg";
//            else
//                return mass + "kg";
        }

        public String getMaxJumpMass()
        {
            return getNormalizedMass(maxJumpMass.getMass());
        }

        public String getTotalMassRange()
        {
            long mass = maxStableMass.getMass();
            long err = mass / 10;
            long top = mass + err;
            long bottom = mass - err;
            return
                getNormalizedMass(mass) + " (" +
                getNormalizedMass(bottom) + " to " +
                getNormalizedMass(top) + ")";
        }

        public long getMassDown()
        {
            long mass = maxStableMass.getMass();
            long err = mass / 10;
            long bottom = mass - err;
            bottom /= 2;
            return bottom;
        }

        public String getMassDownRange()
        {
            long mass = maxStableMass.getMass();
            long err = mass / 10;
            long top = mass + err;
            long bottom = mass - err;
            top /= 2;
            bottom /= 2;
            return
                getNormalizedMass(bottom) + " to " +
                getNormalizedMass(top);
        }

        public String getMassCriticalRange()
        {
            long mass = maxStableMass.getMass();
            long err = mass / 10;
            long top = mass + err;
            long bottom = mass - err;
            top /= 10;
            bottom /= 10;
            return
                getNormalizedMass(bottom) + " to " +
                getNormalizedMass(top);
        }
    }
}
