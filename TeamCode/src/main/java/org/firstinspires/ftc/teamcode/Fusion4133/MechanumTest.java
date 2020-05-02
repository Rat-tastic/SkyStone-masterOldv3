package org.firstinspires.ftc.teamcode.Fusion4133;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@TeleOp (name = "Mechanum : TeleOp Test", group = "2019")
public class MechanumTest extends OpMode {
    mechanumHardwareTest fourwheel = new mechanumHardwareTest();


    @Override
    public void init() {
       fourwheel.hardwareInit(hardwareMap);
       fourwheel.imuInit(hardwareMap);
    }
    @Override
    //this is just so when we hit the stop button at any point in the program the robot will stop
    public void stop() {
        super.stop();
    }
    @Override
    public void loop() {
        double xVal;
        double yVal;
        double zVal;
        //double servoPosl = fourwheel.blockArm.getPosition();
        double servoPosR = fourwheel.blockArm2.getPosition();
        double servoPosL = fourwheel.grabberSecR.getPosition();


        //these allows us to have a bit neater code
        xVal = -gamepad1.left_stick_x;
        yVal = gamepad1.left_stick_y;
        zVal = -gamepad1.right_stick_x;

        fourwheel.leftFrontDrive.setPower(yVal + xVal + zVal); //Next four rows are just for strafing
        fourwheel.leftBackDrive.setPower(yVal - xVal + zVal);
        fourwheel.rightFrontDrive.setPower(yVal - xVal - zVal);
        fourwheel.rightBackDrive.setPower(yVal + xVal - zVal);
        fourwheel.leftBackDrive.setPower(gamepad1.left_stick_y);
        fourwheel.rightBackDrive.setPower(gamepad1.right_stick_y);


        if (gamepad1.a){

            fourwheel.grabberSecR.setPosition(servoPosL + .01);


        }
        else if (gamepad1.y){
           fourwheel.grabberSecR.setPosition(servoPosL - .01);



        }
        if (gamepad2.a){

            fourwheel.blockArm2.setPosition(servoPosR + .01);


        }
        else if (gamepad2.y){
            fourwheel.blockArm2.setPosition(servoPosR - .01);



        }
        if (gamepad1.dpad_up){
            fourwheel.colorStick.setPower(1.0);
        }
        else if (gamepad1.dpad_down){
            fourwheel.colorStick.setPower(-1.0);
        }
        else {
            fourwheel.colorStick.setPower(0.0);
        }

        telemetry.addData("servograber", fourwheel.grabberSecR.getPosition());
        telemetry.addData("servoblocka", fourwheel.blockArm2.getPosition());

        telemetry.addData("alpha", fourwheel.color.alpha());
        //telemetry.addData("servoL", fourwheel.platformHolderL.getPosition());
    }
}
