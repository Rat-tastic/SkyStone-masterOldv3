package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TrainingRobot {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    public enum DriveDireection {
        FORWARD,
        BACKWARD
    }

    public void drive(double iPower, int iTime, DriveDireection iDir) throws InterruptedException {
        if (iDir == DriveDireection.FORWARD) {
            frontLeft.setPower(iPower);
            frontRight.setPower(iPower);
            backLeft.setPower(iPower);
            backRight.setPower(iPower);
        } else {
            frontLeft.setPower(-iPower);
            frontRight.setPower(-iPower);
            backLeft.setPower(-iPower);
            backRight.setPower(-iPower);
        }
        wait(iTime);
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backLeft.setPower(0.0);
        backRight.setPower(0.0);
    }

    public void robotInit(HardwareMap hwMap) {
        frontLeft  = hwMap.dcMotor.get("lf");
        frontRight = hwMap.dcMotor.get("rf");
        backLeft   = hwMap.dcMotor.get("lb");
        backRight  = hwMap.dcMotor.get("rb");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
