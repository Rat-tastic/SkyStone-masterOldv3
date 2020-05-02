package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Fusion on 9/23/2018.
 */
@TeleOp(name="Icarus : TeleOp", group="Icarus")
public class FourWheel extends OpMode{
//hwMap Pull from fourwheel Hardware setup.
    FourWheelHardwareSetup fourWheel = new FourWheelHardwareSetup();
    double liftHoldVal;
    //int lifHoldValRight;
    double liftHoldScale;
    double rightHoldPower;
    double liftHoldPower;


    @Override
    public void init () {
    // Save reference to Hardware map
    fourWheel.hardwareInit(hardwareMap);
    fourWheel.servoInitTele(hardwareMap);


    //lifHoldValRight = fourWheel.rightLiftMotor.getCurrentPosition();
    liftHoldScale   = 0;

    }
    @Override
    //this is just so when we hit the stop button at any point in the program the robot will stop
    public void stop() { super.stop(); }

    @Override
    //runnable code
    public void loop () {
        //set up double values

        double closeTime = 2700;
        double openTime  = 2600;
        double flag = 1;


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
        if (gamepad2.dpad_down){
            flag = .5;
        }
        if (gamepad2.y){

            fourWheel.blockStopperServo.setPosition(.97);

        }
        else if (gamepad2.a){
            fourWheel.blockStopperServo.setPosition(.77);

        }



        if (Math.abs(gamepad2.right_stick_y) >= .2)  {
            liftHoldPower = ((-1.0*(gamepad2.right_stick_y)) * .3);
            fourWheel.rightLiftMotor.setPower(liftHoldPower);
            fourWheel.leftliftMotor.setPower(liftHoldPower);

        }
         else {
            liftHoldPower = liftHoldScale;
            fourWheel.leftliftMotor.setPower(0.0);
            fourWheel.rightLiftMotor.setPower(0.0);
        }


        if (gamepad2.left_stick_y >= .2)  {
            fourWheel.collectionServoRight.setPower(-.75);
            liftHoldVal = (float)fourWheel.potentiometer.getVoltage();
        }
        else if (gamepad2.left_stick_y <= -.2){
            fourWheel.collectionServoRight.setPower(.75);
            liftHoldVal = (float)fourWheel.potentiometer.getVoltage();
        }
        //this if is so that when we let go of the joystick controlling the lift the lift does not lower itself.
        //else {
        //    //fourWheel.collectionServoRight.setPower(clipVal((liftHoldVal - fourWheel.potentiometer.getVoltage())/.1, .75, -.75));
        //    double powerFactor = (liftHoldVal - fourWheel.potentiometer.getVoltage())/.1;
        //    if(fourWheel.potentiometer.getVoltage() < liftHoldVal){
        //        fourWheel.collectionServoRight.setPower(.2);

        //    }
        //    else{
        //        fourWheel.collectionServoRight.setPower(-.2);
        //    }
        //}

        else{
            fourWheel.collectionServoRight.setPower(0);
        }



        if (gamepad2.left_bumper) {
            fourWheel.leadScrew.setPower(.8);
        }
        else if (gamepad2.right_bumper){
            fourWheel.leadScrew.setPower(-.8);
        }
        else {
            fourWheel.leadScrew.setPower(0.0);
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
        if (gamepad2.right_trigger > .3){
            fourWheel.flapServo.setPower(.75);
        }
        else if (gamepad2.left_trigger > .3){
            fourWheel.flapServo.setPower(-.75);
        }
        else{
            fourWheel.flapServo.setPower(.0);
        }

        if (gamepad2.x) {
            liftHoldScale = liftHoldScale - 0.05;
        } else if (gamepad2.y) {
            liftHoldScale = liftHoldScale + 0.05;
        }

        telemetry.addData("RightStickVal", gamepad2.right_stick_y);
        telemetry.addData("Lift Power", liftHoldPower);
        telemetry.addData("Lift Scale", liftHoldScale);

    }
}
