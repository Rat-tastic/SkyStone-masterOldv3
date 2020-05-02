package org.firstinspires.ftc.teamcode.Fusion4133;
import android.icu.text.UnicodeSetSpanner;

import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp (name = "Mechanum : TeleOp", group = "2019")
public class MechanumTeleOp extends OpMode {
    mechanumHardwareTest fourwheel = new mechanumHardwareTest();

    double xVal;
    double yVal;
    double zVal;
    double sweeperPower;

    @Override
    public void init() {
        fourwheel.hardwareInit(hardwareMap);
    }
    @Override
    //this is just so when we hit the stop button at any point in the program the robot will stop
    public void stop() {
        super.stop();
    }

    int toggle = 1;
    @Override
    public void loop() {

        double speed = .6;
        double servoPosR = fourwheel.platformHolderR.getPosition();
        double servoPosL = fourwheel.platformHolderL.getPosition();

        //these allows us to have a bit neater code
        xVal = -gamepad1.left_stick_x;
        yVal = gamepad1.left_stick_y;
        zVal = -gamepad1.right_stick_x;
        if (gamepad1.right_bumper){
            speed = 1;
        }
        else {
            speed = .6;
        }

        fourwheel.leftFrontDrive.setPower((yVal + xVal + zVal)* speed); //Next four rows are just for strafing
        fourwheel.leftBackDrive.setPower((yVal - xVal + zVal)*speed);
        fourwheel.rightFrontDrive.setPower((yVal - xVal - zVal)*speed);
        fourwheel.rightBackDrive.setPower((yVal + xVal - zVal)*speed);



        if (gamepad1.b) {
            fourwheel.platformHolderR.setPosition(.679);
            fourwheel.platformHolderL.setPosition(.32);
        }
        else if (gamepad1.y){
            fourwheel.platformHolderR.setPosition(1);
            fourwheel.platformHolderL.setPosition(0);
        }
        else if (gamepad1.a){
            fourwheel.platformHolderR.setPosition(.47);
            fourwheel.platformHolderL.setPosition(.52);
        }


        if (gamepad2.left_bumper) {
            fourwheel.graber.setPosition(.9 );

        }
        else if (gamepad2.right_bumper){
            fourwheel.graber.setPosition(.5);


        }
        else {
            //fourwheel.graberF.setPower(0);
            //fourwheel.graberB.setPower(0);
        }
        if (gamepad2.left_stick_y < -.25){
            fourwheel.liftMotor.setPower(-gamepad2.left_stick_y);
        }
        else if (gamepad2.left_stick_y > .25){
            fourwheel.liftMotor.setPower(-gamepad2.left_stick_y);
        }
        else{
            fourwheel.liftMotor.setPower(.25);
        }

sweeperPower = 0.0;




//        if (gamepad2.dpad_left && gamepad1.left_trigger < 2 && gamepad1.right_trigger < 2){
            if (gamepad2.dpad_left){
            fourwheel.extendorS.setPower(-.75);
            sweeperPower = 1.0;
//            fourwheel.leftSweeper.setPower(1);
//            fourwheel.rightSweeper.setPower(1);
        }
        else if (gamepad2.dpad_right){
            fourwheel.extendorS.setPower(.75);
            sweeperPower = -1.0;
//            fourwheel.leftSweeper.setPower(-1);
//            fourwheel.rightSweeper.setPower(-1);
        }
        else{
//            fourwheel.leftSweeper.setPower(0);
//            fourwheel.rightSweeper.setPower(0);
            fourwheel.extendorS.setPower(.1);
        }

        if (gamepad1.right_trigger > .2){
            fourwheel.leftCollection.setPower(-.8);
            fourwheel.rightCollection.setPower(-.8);
            sweeperPower = -1.0;
//            fourwheel.leftSweeper.setPower(-1);
//            fourwheel.rightSweeper.setPower(-1);
//            fourwheel.extendorS.setPower(.1);
        }
        else if (gamepad1.left_trigger > .2){
            fourwheel.leftCollection.setPower(.75);
            fourwheel.rightCollection.setPower(.75);
            sweeperPower = 1.0;
//            fourwheel.leftSweeper.setPower(1);
//            fourwheel.rightSweeper.setPower(1);
//            fourwheel.extendorS.setPower(.1);
        }
        else {
            fourwheel.leftCollection.setPower(0);
            fourwheel.rightCollection.setPower(0);
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


            fourwheel.leftSweeper.setPower(sweeperPower);
            fourwheel.rightSweeper.setPower(sweeperPower);


    }
}
