/*
 * This work is protected by Copyright, see COPYING.txt for more information.
 */
package org.emitdo.research.app.dbAdmin;

import org.emitdo.app.logging.LoggerCommands;
import org.emitdo.app.util.Commands;
import org.emitdo.app.util.StrHelper;


public class DbAdminCommands extends LoggerCommands
{
    public static final String SqlFileKey = "sqlFile";
    public static final String SqlOnlyKey = "sqlOnly";

    
    private static final String[] mySwitches = {SqlFileKey, SqlOnlyKey,};
    
    private static final ClpType[] myTypes = {ClpType.String, ClpType.Boolean,}; 
    
    private static final String[] helpArgs = 
    {
        "[file]",
        "[true|false]",
    };
        
    private static final String[] helpStrings =
    {
        "Output file for generated SQL script.",
        "Only generate sql strings.",
    };
    
    public DbAdminCommands(String[] args)
    {
        super(args, mySwitches, myTypes);
    }
    
    public DbAdminCommands(String[] args, Commands[] aggregates, String[] localSwitches, ClpType[] localTypes)
    {
        super(args, aggregates, localSwitches, localTypes);
        addSwitches(mySwitches, myTypes);
        if(name == null)
            name = getClass().getSimpleName();
    }
    
    public DbAdminCommands(String[] args, String[] localSwitches, ClpType[] localTypes)
    {
        super(args, localSwitches, localTypes);
        addSwitches(mySwitches, myTypes);
    }
    
    @Override
    protected StringBuilder getHelp(StringBuilder help)
    {
        super.getHelp(help);
        int gap = getWidestHelpArgument();    
        return getHelp(help, getWidestSwitch(), gap);
    }
    
    @Override
    protected int getWidestSwitch()
    {
        int rvalue = super.getWidestSwitch();
        for(int i=0; i < mySwitches.length; i++)
            rvalue = Math.max(rvalue, mySwitches[i].length());
        return rvalue + 1; //"-"
    }
    
    @Override
    protected int getWidestHelpArgument()
    {
        int rvalue = super.getWidestHelpArgument();
        for(int i=0; i < helpArgs.length; i++)
            rvalue = Math.max(rvalue, helpArgs[i].length());
        return rvalue;
    }
    
    @Override
    protected StringBuilder getHelp(StringBuilder sb, int maxSwitchLength, int gap)
    {
        maxSwitchLength += 2; // "-" and space around switches
        gap += helpGap; //"[123]    "

        for(int i=0; i < mySwitches.length; i++)
        {
            StrHelper.pad(sb, "-" + mySwitches[i], maxSwitchLength);
            StrHelper.pad(sb, helpArgs[i], gap);
            sb.append(helpStrings[i]).append("\n");
        }
        return sb;
    }
    
    public String getSqlFile()
    {
        return getString(SqlFileKey, null);
    }
    
    public void setSqlFile(String value, boolean force)
    {
        setStringParameter(SqlFileKey, value, force);
    }

    public void setSqlOnly(boolean value, boolean force)
    {
        setBooleanParameter(SqlOnlyKey, value, force);
    }

    public boolean isSqlOnly(boolean defaultValue)
    {
        return getBoolean(SqlOnlyKey, defaultValue);
    }
    
    @Override
    public synchronized void toString(StringBuilder sb, int level, boolean singleLine)
    {
        toString(sb, level, singleLine, false);
    }
    
    private synchronized void toString(StringBuilder sb, int level, boolean singleLine, boolean mineOnly)
    {
        if(!mineOnly)
        {
            super.toString(sb, level, singleLine);
            ++level;
        }
        String delimit = ": ";
        String end = "";
        boolean eol = !singleLine;
        if(singleLine)
        {
            level = 0;
            delimit = "=";
            end = ",";
        }
        if(isSwitchPresent(SqlOnlyKey))
            StrHelper.tabToLevel(sb, level, eol, SqlOnlyKey, delimit, ""+isSqlOnly(false), end);
        if(isSwitchPresent(SqlFileKey))
            StrHelper.tabToLevel(sb, level, eol, SqlFileKey, delimit, getSqlFile(), end);
        sb.append(" ");
    }
}
