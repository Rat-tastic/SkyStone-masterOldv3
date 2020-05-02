package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class FRCHardwareSetup {
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor centerDrive;

    public void hardwareInit (HardwareMap hwMap){
        leftDrive      = hwMap.dcMotor.get("m1");
        rightDrive       = hwMap.dcMotor.get("m2");
        centerDrive       = hwMap.dcMotor.get("m3");
    }
}
