package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class TrainingAuto extends LinearOpMode {
    TrainingRobot robot;

    @Override
    public void runOpMode() throws InterruptedException{
        robot.robotInit(hardwareMap);

        waitForStart();

        robot.drive(0.5, 1000, TrainingRobot.DriveDireection.FORWARD);

        robot.frontLeft.setPower(-0.5);
        robot.frontRight.setPower(0.5);
        robot.backLeft.setPower(-0.5);
        robot.backRight.setPower(0.5);
        wait(1000);
        robot.frontLeft.setPower(0.0);
        robot.frontRight.setPower(0.0);
        robot.backLeft.setPower(0.0);
        robot.backRight.setPower(0.0);

        robot.drive(1.0, 500, TrainingRobot.DriveDireection.BACKWARD);    }
}
