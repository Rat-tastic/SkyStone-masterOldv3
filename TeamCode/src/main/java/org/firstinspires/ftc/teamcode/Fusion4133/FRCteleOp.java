package org.firstinspires.ftc.teamcode.Fusion4133;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp(name = "FRC TeleOp", group = "2019")
public class FRCteleOp extends OpMode {

    CRServo frontDrive;
    CRServo leftDrive;
    CRServo rightDrive;


    @Override
    public void init() {
        leftDrive = hardwareMap.crservo.get("ld");
        rightDrive = hardwareMap.crservo.get("rd");
        frontDrive = hardwareMap.crservo.get("fd");

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {

        if(-gamepad1.left_stick_y > .2){
            leftDrive.setPower(gamepad1.left_stick_y);
            rightDrive.setPower(-gamepad1.left_stick_y);
            frontDrive.setPower(-gamepad1.left_stick_y);
        }
        if(gamepad1.left_stick_y < -.2){
            leftDrive.setPower(-gamepad1.left_stick_y);
            rightDrive.setPower(gamepad1.left_stick_y);
            frontDrive.setPower(gamepad1.left_stick_y);
        }
        if(gamepad1.b){
            leftDrive.setPower(.5);
            rightDrive.setPower(-.5);
            frontDrive.setPower(-.5);
        }
        else if(gamepad1.x){
            leftDrive.setPower(-.5);
            rightDrive.setPower(.5);
            frontDrive.setPower(.5);
        }
        else{
            leftDrive.setPower(0);
            rightDrive.setPower(0);
            frontDrive.setPower(0.0);
        }
    }
}
