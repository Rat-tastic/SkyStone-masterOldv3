package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.SwitchableLight;

/**
 * Created by Fusion on 11/8/2017.
 */
//this is our runnable teleop program that we use in competition
@Disabled
@TeleOp (name = "Hadron : TeleOp", group = "Hadron")
public class HadronTeleOp extends OpMode {
  //this is how we can pull code from the hadron hardware setup
  MecanumHardwareSetup hadron = new MecanumHardwareSetup();
  boolean firstTime = true;
  int liftHoldVal;


  @Override
  public void init() {
    hadron.hardwareInit(hardwareMap);
    //this turns off the color sensors light in TeleOp
    if (hadron.color instanceof SwitchableLight) {
      ((SwitchableLight) hadron.color).enableLight(false);
    }
    //this just set the lift hold value to the position it is at at the beginning of the
    liftHoldVal = hadron.liftDrive.getCurrentPosition();
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
    double csInc = .04;
    //because of rule changes that when in the Init phase we can't have moving parts, this makes it so the robot does not move until the play button has been touched.
    if (firstTime){
      hadron.servoInit(hardwareMap);

    }
    firstTime = false;

    //these allows us to have a bit neater code
    xVal = gamepad1.left_stick_x;
    yVal = -gamepad1.left_stick_y;
    zVal = gamepad1.right_stick_x;

    hadron.leftFrontDrive.setPower(yVal + xVal + zVal); //Next four rows are just for strafing
    hadron.leftBackDrive.setPower(yVal - xVal + zVal);
    hadron.rightFrontDrive.setPower(yVal - xVal - zVal);
    hadron.rightBackDrive.setPower(yVal + xVal - zVal);
    hadron.collectionMotorLeft.setPower(gamepad2.right_trigger);    //Next four lines for opening and closing the collection motors.
    hadron.collectionMotorRight.setPower(gamepad2.right_trigger);
    hadron.collectionMotorLeft.setPower(-gamepad2.left_trigger);
    hadron.collectionMotorRight.setPower(-gamepad2.left_trigger);
    //val .2 is there because we want to treat the joystick instead of analog to a button like device.
    //liftHoldVal is what the position the lift was upon stopping the lift
    if (Math.abs(gamepad2.left_stick_y) >= .2)  {
      hadron.liftDrive.setPower(gamepad2.left_stick_y);
      liftHoldVal = hadron.liftDrive.getCurrentPosition();

    }
    //this if is so that when we let go of the joystick controlling the lift the lift does not lower itself.
    else {
      hadron.liftDrive.setPower((double) (liftHoldVal - hadron.liftDrive.getCurrentPosition()) / 2000.0);
    }



    hadron.relicArm.setPower(gamepad2.right_stick_y);

    telemetry.addData("lift ENC ","value: %7d", hadron.liftDrive.getCurrentPosition());
    telemetry.addData("lift power","val: %2f",gamepad2.left_stick_y);
    //telemetry.addData("hold val", "Val: %2i", liftHoldVal);

  }
}
