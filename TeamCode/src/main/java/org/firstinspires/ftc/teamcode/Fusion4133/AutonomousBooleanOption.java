package org.firstinspires.ftc.teamcode.Fusion4133;

/**
 * Created by Fusion on 10/30/2017.
 */

public class AutonomousBooleanOption extends AutonomousOption {

    private boolean currentValue;

    public AutonomousBooleanOption (String iName, boolean iStartVal){
        name = iName;
        optionType = OptionTypes.BOOLEAN;
        currentValue = iStartVal;
    }

    public void nextValue (){
        currentValue = true;
    }

    public void previousValue (){
        currentValue = false;
    }

    public boolean getValue (){
        return currentValue;
    }

    public void setValue (boolean iVal){
        currentValue = iVal;
    }

}
