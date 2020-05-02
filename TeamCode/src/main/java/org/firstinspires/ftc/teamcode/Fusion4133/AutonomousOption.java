package org.firstinspires.ftc.teamcode.Fusion4133;

/**
 * Created by Fusion on 10/30/2017.
 */

abstract class AutonomousOption {
    String name;
    public enum OptionTypes {STRING, INT, BOOLEAN};
    OptionTypes optionType;

    abstract void nextValue ();

    abstract void previousValue ();
}
