package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Disabled
@TeleOp(name="Neutrino: Demo", group="Neutrino")
public class NeutrinoDemoTeleOp extends OpMode {
    DcMotor LeftDrive;
    DcMotor RightDrive;

    @Override
    public void init() {
        LeftDrive = hardwareMap.dcMotor.get("ld");
        RightDrive = hardwareMap.dcMotor.get("rd");

        LeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }


    @Override
    public void loop() {
        LeftDrive.setPower(-gamepad1.left_stick_y);
        RightDrive.setPower(-gamepad1.right_stick_y);
    }
}
