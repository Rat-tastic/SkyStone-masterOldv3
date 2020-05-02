package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RhettHardwareSetup {
    DcMotor LeftDrive;
    DcMotor RightDrive;


    public void init(HardwareMap hwMap){
        LeftDrive = hwMap.dcMotor.get("ld");
        RightDrive = hwMap.dcMotor.get("rd");

        RightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

}
