package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Disabled
@TeleOp(name="Dumb : TeleOp", group="Dumb")
public class DumbRobot extends OpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    public void robotInit(HardwareMap hwMap) {
        frontLeft  = hwMap.dcMotor.get("lf");
        frontRight = hwMap.dcMotor.get("rf");
        backLeft   = hwMap.dcMotor.get("lb");
        backRight  = hwMap.dcMotor.get("rb");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    @Override
    public void init (){
        robotInit(hardwareMap);
    }
    @Override
    public void stop() { super.stop(); }

    @Override
    public void loop () {

        if (gamepad1.left_stick_y > .2 || gamepad1.left_stick_y < -.2) {
            frontLeft.setPower(-gamepad1.left_stick_y);
            backLeft.setPower(-gamepad1.left_stick_y);
            frontRight.setPower(-gamepad1.left_stick_y);
            backRight.setPower(-gamepad1.left_stick_y);
        }
        if (gamepad1.right_stick_x > .2 || gamepad1.right_stick_x < -.2) {
            frontLeft.setPower(gamepad1.right_stick_x);
            backLeft.setPower(gamepad1.right_stick_x);
            frontRight.setPower(-gamepad1.right_stick_x);
            backRight.setPower(-gamepad1.right_stick_x);
        }
        else {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
        }
    }
}
