package org.firstinspires.ftc.teamcode.Fusion4133;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Fusion on 10/23/2018.
 */

@TeleOp(name="Icarus : TeleOpTest", group="Icarus")
public class FourWheelTeleOpTest extends OpMode{
    //hwMap Pull from fourwheel Hardware setup.
    FourWheelHardwareSetup fourWheel = new FourWheelHardwareSetup();

    double flapPower;
    double liftHoldVal;

    public double clipVal(double val, double max, double min)
    {
        return (Math.max(Math.min(val, min), max));
    }

    @Override
    public void init () {
        // Save reference to Hardware map
        fourWheel.hardwareInit(hardwareMap);
        fourWheel.imuInit(hardwareMap);
       //fourWheel.cameraInit(hardwareMap);
        fourWheel.servoInitTele(hardwareMap);



        flapPower = 0;

    }
    @Override
    //this is just so when we hit the stop button at any point in the program the robot will stop
    public void stop() {
        super.stop();
    }

    @Override
    //runnable code
    public void loop () {

        //set up double values
        //fourWheel.liftPower = gamepad2.right_stick_y;
        fourWheel.rightPower = gamepad1.right_stick_y;
        fourWheel.leftPower = gamepad1.left_stick_y;
        double current = fourWheel.fingerServo.getPosition();
        //double xPos = fourWheel.detector.getXPosition();
        //liftHoldVal = (float) fourWheel.potentiometer.getVoltage();



        double closeTime = 2700;
        double openTime  = 2600;

        ElapsedTime movementTime   = new ElapsedTime();

        //set four drive motors powers to double vals
        if (gamepad1.left_stick_y > .2 || gamepad1.left_stick_y < -.2) {
            fourWheel.leftMotorFront.setPower(-gamepad1.left_stick_y);
            fourWheel.leftMotorBack.setPower(-gamepad1.left_stick_y);
            fourWheel.rightMotorFront.setPower(-gamepad1.left_stick_y);
            fourWheel.rightMotorBack.setPower(-gamepad1.left_stick_y);
        }
        if (gamepad1.right_stick_x > .2 || gamepad1.right_stick_x < -.2) {
            fourWheel.leftMotorFront.setPower(gamepad1.right_stick_x);
            fourWheel.leftMotorBack.setPower(gamepad1.right_stick_x);
            fourWheel.rightMotorFront.setPower(-gamepad1.right_stick_x);
            fourWheel.rightMotorBack.setPower(-gamepad1.right_stick_x);
        }
        else {
            fourWheel.leftMotorFront.setPower(0);
            fourWheel.leftMotorBack.setPower(0);
            fourWheel.rightMotorFront.setPower(0);
            fourWheel.rightMotorBack.setPower(0);
        }
        if (gamepad2.x){
            fourWheel.fingerServo.setPosition(current + .01);
        }
        else if (gamepad2.y){
            fourWheel.fingerServo.setPosition(current - .01);
        }

        if (gamepad2.left_stick_y < -.2){
            fourWheel.collectionServoRight.setPower(.75);
        }
        else if (gamepad2.left_stick_y > .2){
            fourWheel.collectionServoRight.setPower(-.75);
        }
        else {
            fourWheel.collectionServoRight.setPower(0.0);
        }

        fourWheel.phoneServo.setPosition(fourWheel.phonesPosDown);

       // if (fourWheel.phoneServo.getPosition() == fourWheel.phonesPosDown) {
           // xPos = fourWheel.detector.getXPosition();
       // }
        if (gamepad1.dpad_down){
            fourWheel.phoneServo.setPosition(fourWheel.phonesPosDown);
        }
        else if (gamepad1.dpad_up){
            //fourWheel.blockStopperServo.setPosition(fourWheel.phonesPosUp);
        }
        if (gamepad1.y){

            fourWheel.blockStopperServo.setPosition(.97);

        }
       if (gamepad1.a){
           fourWheel.blockStopperServo.setPosition(.77);

        }
        if (gamepad1.left_bumper) {
            fourWheel.leadScrewLander.setPower(.8);
        }
        else if (gamepad1.right_bumper){
            fourWheel.leadScrewLander.setPower(-.8);
        }
        else {
            fourWheel.leadScrewLander.setPower(0.0);
        }
        fourWheel.leftliftMotor.setPower(gamepad2.right_stick_y);
        fourWheel.rightLiftMotor.setPower(gamepad2.right_stick_y);
        //liftHoldVal = fourWheel.potentiometer.getVoltage();




        //set two lift motors powers with same double vals
        //fourWheel.leftliftMotor.setPower(fourWheel.liftPower);
        //fourWheel.rightLiftMotor.setPower(fourWheel.liftPower);
        //Color.RGBToHSV((int) (fourWheel.color.red() * fourWheel.SCALE_FACTOR),
                //(int) (fourWheel.color.green() * fourWheel.SCALE_FACTOR),
                //(int) (fourWheel.color.blue() * fourWheel.SCALE_FACTOR),
                //fourWheel.hsvValues);
        //telemetry.addData("servoLeft", fourWheel.collectionServoLeft.getPosition());
        //telemetry.addData("servoRight", fourWheel.collectionServoRight.getPosition());
        //telemetry.addData("button", fourWheel.liftButton.getValue());
        //telemetry.addData("poten", potVolt);
        telemetry.addData("pot", liftHoldVal);
        telemetry.addData("Flap Power", flapPower);
        telemetry.addData("leadScrewLander", fourWheel.leadScrewLander.getCurrentPosition());
       // telemetry.addData("xPos of block", xPos);
        telemetry.addData("servoPos", fourWheel.fingerServo.getPosition());
        //telemetry.addData("leadScrewLander","Value: %7d", fourWheel.leadScrewLander.getCurrentPosition());
        telemetry.addData("leftfront", fourWheel.leftMotorFront.getCurrentPosition() / 41.733);
        telemetry.addData("leftback", fourWheel.leftMotorBack.getCurrentPosition() / 41.733);
        telemetry.addData("rightfront", fourWheel.rightMotorFront.getCurrentPosition() / 41.733);
        telemetry.addData("rightback", fourWheel.rightMotorBack.getCurrentPosition() / 41.733);
        //telemetry.addData("imu", fourWheel.imu.getAngularOrientation());
        //telemetry.addData("Alpha", fourWheel.color.alpha());
        //telemetry.addData("Red  ", fourWheel.color.red());
        //telemetry.addData("Green", fourWheel.color.green());
        //telemetry.addData("Blue ", fourWheel.color.blue());
        //telemetry.addData("Hue", fourWheel.hsvValues[0]);
        telemetry.update();
    }
}