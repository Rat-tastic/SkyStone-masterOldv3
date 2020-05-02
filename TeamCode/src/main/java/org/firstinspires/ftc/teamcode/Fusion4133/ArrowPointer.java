package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Arrow Pointer", group = "2019")
public class ArrowPointer extends OpMode {
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor leftDrive2;
    DcMotor rightDrive2;
  //  Servo arrowPointer;

    int xVal;
    int yVal;
 /*   public void aim(int xIn, int yIn){

        int x2 = Math.abs(xIn);
        int y2 = Math.abs(yIn);

        if(xIn == 0){
            arrowPointer.setPosition(0.0);
        }

        else{
            double degAngle = Math.toDegrees(Math.atan(y2/x2));
            if (xIn >0){
                arrowPointer.setPosition(degAngle/90);
            }
            else if (xIn < 0){
                arrowPointer.setPosition(-degAngle/90);
            }
            else{
                arrowPointer.setPosition(0.0);
            }

        }


    } */
    @Override
    public void init() {
        leftDrive = hardwareMap.dcMotor.get("lb");
        rightDrive = hardwareMap.dcMotor.get("rb");
        leftDrive2 = hardwareMap.dcMotor.get("lf");
        rightDrive2 = hardwareMap.dcMotor.get("rf");
      //  arrowPointer = hardwareMap.servo.get("ps");

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);

     //   xVal = 5915;
     //   yVal = 6098;
     //   aim(xVal,yVal);


    }

    @Override
    public void loop() {
  //      aim(leftDrive.getCurrentPosition() + xVal, yVal);
        leftDrive.setPower(gamepad1.left_stick_y);
        rightDrive.setPower(gamepad1.left_stick_y);
        leftDrive2.setPower(gamepad1.left_stick_y);
        rightDrive2.setPower(gamepad1.left_stick_y);

    }
}
