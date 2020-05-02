package org.firstinspires.ftc.teamcode.Fusion4133;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.teamcode.GoldAlignExample;

/**
 * Created by Fusion on 10/21/2018.
 */

public abstract class FourWheelAutoSetup extends LinearOpMode {
  //This is where the options values are defined.
  AutonomousTextOption allianceColor = new AutonomousTextOption("Alliance Color", "blue", new String[]{"blue", "red"});
  AutonomousTextOption startHanging = new AutonomousTextOption("hanging", "hanging", new String[]{"hanging", "floor"});
  AutonomousTextOption startHook = new AutonomousTextOption("Start Hook", "ballSide", new String[]{"ballSide", "blockSide"});
  AutonomousTextOption parkCrater = new AutonomousTextOption("Park crater", "BlueCrater", new String[]{"BlueCrater", "RedCrater", "NoCrater"});
  AutonomousIntOption waitStart = new AutonomousIntOption("Wait at Start", 0, 0, 20);
  AutonomousBooleanOption scoreTeamMarker = new AutonomousBooleanOption("Score Marker", true);
  AutonomousBooleanOption sample = new AutonomousBooleanOption("sample", true);
  AutonomousBooleanOption doubleSample = new AutonomousBooleanOption("Double Sample", true);

  //This is the order of our options and setting them all to there preset value.
  AutonomousOption[] autoOptions = {allianceColor,startHanging, startHook, waitStart, parkCrater, scoreTeamMarker, sample, doubleSample};
  int currentOption = 0;

  //this setting the buttons to false to make sure options are not being chosen for us.
  boolean aPressed = false;
  boolean bPressed = false;
  boolean xPressed = false;
  boolean yPressed = false;

  FourWheelHardwareSetup fourWheel = new FourWheelHardwareSetup();

  final double ticksPerInch       = (41.733);  //188;  //tick of the encoder * gear ratio / circumference of the wheel
  final  int   tickOverRun        = 0;   //number of tick robot overruns target after stop
  final double inchesPerDeg       = 1.5;  //wheel base of robot * pi / 360
  final double inchesPerDegArm    = .01;
  final double tickPerDeg         = 16 * inchesPerDeg;
  final double tickPerDegArm      = 16 *inchesPerDegArm;

  ElapsedTime movementTime = new ElapsedTime();

  //This is so later we have pre deffined what our directions that are possible in drives and turns.
  public enum driveDirections {
    FORWARD, BACKWARD
  }

  public enum turnDirections {
    LEFT, RIGHT
  }

  public void GoldDetector(){

  }


  // This is how we get our autonomous options to show up on our phones.
  public void showOptions() {
    int index = 0;
    String str = "";

    while (index < autoOptions.length) {
      switch (autoOptions[index].optionType) {
        case STRING:
          str = ((AutonomousTextOption) autoOptions[index]).getValue();
          break;
        case INT:
          str = Integer.toString(((AutonomousIntOption) autoOptions[index]).getValue());
          break;
        case BOOLEAN:
          str = String.valueOf(((AutonomousBooleanOption) autoOptions[index]).getValue());
          break;
      }

      if (index == currentOption) {
        telemetry.addData(Integer.toString(index) + ") ==> " + autoOptions[index].name, str);
      } else {
        telemetry.addData(Integer.toString(index) + ")     " + autoOptions[index].name, str);
      }

      index = index + 1;
    }
    telemetry.update();
  }

  // This is how we select our auto options
  public void selectOptions() {

    while (currentOption < autoOptions.length && !opModeIsActive()) {
      showOptions();

      if (gamepad1.a && !aPressed) {
        currentOption = currentOption + 1;
      }
      aPressed = gamepad1.a;

      if (gamepad1.y && !yPressed) {
        currentOption = currentOption - 1;
      }
      yPressed = gamepad1.y;

      if (gamepad1.b && !bPressed) {
        autoOptions[currentOption].nextValue();
      }
      bPressed = gamepad1.b;

      if (gamepad1.x && !xPressed) {
        autoOptions[currentOption].previousValue();
      }
      xPressed = gamepad1.x;

      telemetry.update();
      Thread.yield();
    }

    telemetry.addData("Robot", "READY!!");
    telemetry.update();
  }

  public void offLander() {

  }

  //This is our most basic drive that uses time to determine the distance it goes.
  public void driveTime(double iSpeed, int iTime, driveDirections iDir) {

    double dirAdj;

    if (iDir == driveDirections.BACKWARD) {
      dirAdj = -1.0;
    } else {
      dirAdj = 1.0;
    }
    fourWheel.leftMotorBack.setPower(iSpeed * dirAdj);
    fourWheel.leftMotorFront.setPower(iSpeed * dirAdj);
    fourWheel.rightMotorBack.setPower(iSpeed * dirAdj);
    fourWheel.rightMotorFront.setPower(iSpeed * dirAdj);

    while (opModeIsActive() && movementTime.milliseconds() < iTime){
    }

    fourWheel.leftMotorBack.setPower(0.0);
    fourWheel.leftMotorFront.setPower(0.0);
    fourWheel.rightMotorBack.setPower(0.0);
    fourWheel.rightMotorFront.setPower(0.0);


  }

  public void fusionSleep (long milliseconds){
    ElapsedTime timer = new ElapsedTime();
    timer.reset();
    while (timer.milliseconds() < milliseconds && opModeIsActive()){

    }

  }
  public void collectDriveEnc (double iSpeed, int iDist, driveDirections iDir) {
      double vSpeed = iSpeed;

      double dirAdj;

      int leftTargetFront;
      int leftTargetBack;
      int rightTargetFront;
      int rightTargetBack;

      int leftStartFront = 0;
      int leftStartBack = 0;
      int rightStartFront = 0;
      int rightStartBack = 0;

      int leftFinalFront = 0;
      int leftFinalBack = 0;
      int rightFinalFront = 0;
      int rightFinalBack = 0;

      int leftAdjTargetFront;
      int leftAdjTargetBack;
      int rightAdjTargetFront;
      int rightAdjTargetBack;

      if (iDir == driveDirections.FORWARD) {
          dirAdj = 1.0;
      } else {
          dirAdj = -1.0;
          vSpeed = vSpeed * dirAdj;
      }

      leftStartFront = fourWheel.leftMotorFront.getCurrentPosition();
      leftStartBack = fourWheel.leftMotorBack.getCurrentPosition();
      rightStartFront = fourWheel.rightMotorFront.getCurrentPosition();
      rightStartBack = fourWheel.rightMotorBack.getCurrentPosition();

      leftTargetFront = leftStartFront + (int) (iDist * ticksPerInch * dirAdj);
      leftTargetBack = leftStartBack + (int) (iDist * ticksPerInch * dirAdj);
      rightTargetFront = rightStartFront + (int) (iDist * ticksPerInch * dirAdj);
      rightTargetBack = rightStartBack + (int) (iDist * ticksPerInch * dirAdj);

      leftAdjTargetFront  = leftTargetFront  - (int)(tickOverRun * dirAdj);
      leftAdjTargetBack   = leftTargetBack   - (int)(tickOverRun * dirAdj);
      rightAdjTargetFront = rightTargetFront - (int)(tickOverRun * dirAdj);
      rightAdjTargetBack  = rightTargetBack  - (int)(tickOverRun * dirAdj);

      //telemetry that allows us see if our target is correct.
      telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
      telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
      telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
      telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

      telemetry.update();

      //vSpeed is a variable that is bases on what we set iSpeed to when putting this void into the program.
      fourWheel.leftMotorBack.setPower(iSpeed * dirAdj);
      fourWheel.leftMotorFront.setPower(iSpeed * dirAdj);
      fourWheel.rightMotorBack.setPower(iSpeed * dirAdj);
      fourWheel.rightMotorFront.setPower(iSpeed * dirAdj);
      fourWheel.flapServo.setPower(.75);

      if (iDir == driveDirections.FORWARD) {

          //while loop set motor at vSpeed until these conditions are not ture any longer.
          while (opModeIsActive() &&
                  fourWheel.leftMotorBack.getCurrentPosition() < leftAdjTargetBack &&
                  fourWheel.leftMotorFront.getCurrentPosition() < leftAdjTargetFront &&
                  fourWheel.rightMotorFront.getCurrentPosition() < rightAdjTargetFront &&
                  fourWheel.rightMotorBack.getCurrentPosition() < rightAdjTargetBack ) {
          }
      }
      else {
          while (opModeIsActive() &&
                  fourWheel.leftMotorBack.getCurrentPosition() > leftAdjTargetBack &&
                  fourWheel.leftMotorFront.getCurrentPosition() > leftAdjTargetFront &&
                  fourWheel.rightMotorFront.getCurrentPosition() > rightAdjTargetFront &&
                  fourWheel.rightMotorBack.getCurrentPosition() > rightAdjTargetBack ) {
          }
      }

      fourWheel.leftMotorBack.setPower(0.0);
      fourWheel.leftMotorFront.setPower(0.0);
      fourWheel.rightMotorBack.setPower(0.0);
      fourWheel.rightMotorFront.setPower(0.0);
      fourWheel.flapServo.setPower(0.0);

      leftFinalFront = fourWheel.leftMotorFront.getCurrentPosition();
      leftFinalBack = fourWheel.leftMotorBack.getCurrentPosition();
      rightFinalFront = fourWheel.rightMotorFront.getCurrentPosition();
      rightFinalBack = fourWheel.rightMotorBack.getCurrentPosition();

      telemetry.addData("leftFront",Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront)+ "; " + Integer.toString(leftFinalFront)+ "; " + Integer.toString(leftAdjTargetFront));
      telemetry.addData("leftBack",Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack)+ "; " + Integer.toString(leftFinalBack)+ "; " + Integer.toString(leftAdjTargetBack));
      telemetry.addData("rightFront",Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront)+ "; " + Integer.toString(rightFinalFront) + "; " + Integer.toString(rightAdjTargetFront));
      telemetry.addData("rightBack",Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack)+ "; " + Integer.toString(rightFinalBack) + "; " + Integer.toString(rightAdjTargetBack));

      telemetry.update();
  }
  //This is a more complicated drive that uses encoder ticks to determine its' distance traveled.
  public void driveEnc (double iSpeed, int iDist, driveDirections iDir) {
    double vSpeed = iSpeed;

    double dirAdj;

    int leftTargetFront;
    int leftTargetBack;
    int rightTargetFront;
    int rightTargetBack;

    int leftStartFront = 0;
    int leftStartBack = 0;
    int rightStartFront = 0;
    int rightStartBack = 0;

    int leftFinalFront = 0;
    int leftFinalBack = 0;
    int rightFinalFront = 0;
    int rightFinalBack = 0;

    int leftAdjTargetFront;
    int leftAdjTargetBack;
    int rightAdjTargetFront;
    int rightAdjTargetBack;

    if (iDir == driveDirections.FORWARD) {
      dirAdj = 1.0;
    } else {
      dirAdj = -1.0;
      vSpeed = vSpeed * dirAdj;
    }

    leftStartFront = fourWheel.leftMotorFront.getCurrentPosition();
    leftStartBack = fourWheel.leftMotorBack.getCurrentPosition();
    rightStartFront = fourWheel.rightMotorFront.getCurrentPosition();
    rightStartBack = fourWheel.rightMotorBack.getCurrentPosition();

    leftTargetFront = leftStartFront + (int) (iDist * ticksPerInch * dirAdj);
    leftTargetBack = leftStartBack + (int) (iDist * ticksPerInch * dirAdj);
    rightTargetFront = rightStartFront + (int) (iDist * ticksPerInch * dirAdj);
    rightTargetBack = rightStartBack + (int) (iDist * ticksPerInch * dirAdj);

    leftAdjTargetFront  = leftTargetFront  - (int)(tickOverRun * dirAdj);
    leftAdjTargetBack   = leftTargetBack   - (int)(tickOverRun * dirAdj);
    rightAdjTargetFront = rightTargetFront - (int)(tickOverRun * dirAdj);
    rightAdjTargetBack  = rightTargetBack  - (int)(tickOverRun * dirAdj);

    //telemetry that allows us see if our target is correct.
    telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
    telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
    telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
    telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

    telemetry.update();

    //vSpeed is a variable that is bases on what we set iSpeed to when putting this void into the program.
    fourWheel.leftMotorBack.setPower(iSpeed * dirAdj);
    fourWheel.leftMotorFront.setPower(iSpeed * dirAdj);
    fourWheel.rightMotorBack.setPower(iSpeed * dirAdj);
    fourWheel.rightMotorFront.setPower(iSpeed * dirAdj);

    if (iDir == driveDirections.FORWARD) {

      //while loop set motor at vSpeed until these conditions are not ture any longer.
      while (opModeIsActive() &&
              fourWheel.leftMotorBack.getCurrentPosition() < leftAdjTargetBack &&
              fourWheel.leftMotorFront.getCurrentPosition() < leftAdjTargetFront &&
              fourWheel.rightMotorFront.getCurrentPosition() < rightAdjTargetFront &&
              fourWheel.rightMotorBack.getCurrentPosition() < rightAdjTargetBack ) {
      }
    }
    else {
      while (opModeIsActive() &&
              fourWheel.leftMotorBack.getCurrentPosition() > leftAdjTargetBack &&
              fourWheel.leftMotorFront.getCurrentPosition() > leftAdjTargetFront &&
              fourWheel.rightMotorFront.getCurrentPosition() > rightAdjTargetFront &&
              fourWheel.rightMotorBack.getCurrentPosition() > rightAdjTargetBack ) {
      }
    }

    fourWheel.leftMotorBack.setPower(0.0);
    fourWheel.leftMotorFront.setPower(0.0);
    fourWheel.rightMotorBack.setPower(0.0);
    fourWheel.rightMotorFront.setPower(0.0);

    leftFinalFront = fourWheel.leftMotorFront.getCurrentPosition();
    leftFinalBack = fourWheel.leftMotorBack.getCurrentPosition();
    rightFinalFront = fourWheel.rightMotorFront.getCurrentPosition();
    rightFinalBack = fourWheel.rightMotorBack.getCurrentPosition();

    telemetry.addData("leftFront",Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront)+ "; " + Integer.toString(leftFinalFront)+ "; " + Integer.toString(leftAdjTargetFront));
    telemetry.addData("leftBack",Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack)+ "; " + Integer.toString(leftFinalBack)+ "; " + Integer.toString(leftAdjTargetBack));
    telemetry.addData("rightFront",Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront)+ "; " + Integer.toString(rightFinalFront) + "; " + Integer.toString(rightAdjTargetFront));
    telemetry.addData("rightBack",Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack)+ "; " + Integer.toString(rightFinalBack) + "; " + Integer.toString(rightAdjTargetBack));

    telemetry.update();
  }
  //This is a basic spin turn that reverses one side to make a turn.
  public void spinEnc (double iSpeed, int iDist, turnDirections iDir) {
    double vSpeed = iSpeed;

    double leftDirAdj;
    double rightDirAdj;

    int leftTargetFront;
    int leftTargetBack;
    int rightTargetFront;
    int rightTargetBack;

    int leftStartFront = 0;
    int leftStartBack = 0;
    int rightStartFront = 0;
    int rightStartBack = 0;

    int leftFinalFront = 0;
    int leftFinalBack = 0;
    int rightFinalFront = 0;
    int rightFinalBack = 0;

    int leftAdjTargetFront;
    int leftAdjTargetBack;
    int rightAdjTargetFront;
    int rightAdjTargetBack;

    if (iDir == FourWheelAutoSetup.turnDirections.RIGHT) {
      leftDirAdj = 1.0;
      rightDirAdj = -1.0;
    } else {
      leftDirAdj = -1.0;
      rightDirAdj = 1.0;
    }

    leftStartFront = fourWheel.leftMotorFront.getCurrentPosition();
    leftStartBack = fourWheel.leftMotorBack.getCurrentPosition();
    rightStartFront = fourWheel.rightMotorFront.getCurrentPosition();
    rightStartBack = fourWheel.rightMotorBack.getCurrentPosition();

    leftTargetFront = leftStartFront + (int) (iDist * tickPerDeg * leftDirAdj);
    leftTargetBack = leftStartBack + (int) (iDist * tickPerDeg * leftDirAdj);
    rightTargetFront = rightStartFront + (int) (iDist * tickPerDeg * rightDirAdj);
    rightTargetBack = rightStartBack + (int) (iDist * tickPerDeg * rightDirAdj);

    leftAdjTargetFront = leftTargetFront - (int) (tickOverRun * leftDirAdj);
    leftAdjTargetBack = leftTargetBack - (int) (tickOverRun * leftDirAdj);
    rightAdjTargetFront = rightTargetFront - (int) (tickOverRun * rightDirAdj);
    rightAdjTargetBack = rightTargetBack - (int) (tickOverRun * rightDirAdj);

    telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
    telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
    telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
    telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

    telemetry.update();

    fourWheel.rightMotorBack.setPower(iSpeed * rightDirAdj);
    fourWheel.rightMotorFront.setPower(iSpeed * rightDirAdj);
    fourWheel.leftMotorBack.setPower(iSpeed * leftDirAdj);
    fourWheel.leftMotorFront.setPower(iSpeed * leftDirAdj);

    if ((iDir == FourWheelAutoSetup.turnDirections.RIGHT)) {

      while (opModeIsActive() &&
              fourWheel.leftMotorFront.getCurrentPosition() < leftTargetFront &&
              fourWheel.leftMotorBack.getCurrentPosition() < leftTargetBack &&
              fourWheel.rightMotorFront.getCurrentPosition() > rightTargetFront &&
              fourWheel.rightMotorBack.getCurrentPosition() > rightTargetBack) {
      }
    } else {
      while (opModeIsActive() &&
              fourWheel.leftMotorFront.getCurrentPosition() > leftTargetFront &&
              fourWheel.leftMotorBack.getCurrentPosition() > leftTargetBack &&
              fourWheel.rightMotorFront.getCurrentPosition() < rightTargetFront &&
              fourWheel.rightMotorBack.getCurrentPosition() < rightTargetBack) {
      }
    }

    fourWheel.leftMotorBack.setPower(0.0);
    fourWheel.leftMotorFront.setPower(0.0);
    fourWheel.rightMotorBack.setPower(0.0);
    fourWheel.rightMotorFront.setPower(0.0);

    leftFinalFront = fourWheel.leftMotorFront.getCurrentPosition();
    leftFinalBack = fourWheel.leftMotorBack.getCurrentPosition();
    rightFinalFront = fourWheel.rightMotorFront.getCurrentPosition();
    rightFinalBack = fourWheel.rightMotorBack.getCurrentPosition();

    telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
    telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
    telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
    telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

    telemetry.update();

  }
  private void resetAngle() {
    fourWheel.lastAngles = fourWheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

    fourWheel.globalAngle = 0;
  }
  private double getAngle() {
    Orientation angles = fourWheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

    double deltaAngle = angles.firstAngle - fourWheel.lastAngles.firstAngle;

    if (deltaAngle < -180)
      deltaAngle += 360;
    else if (deltaAngle > 180)
      deltaAngle -= 360;

    fourWheel.globalAngle += deltaAngle;

    fourWheel.lastAngles = angles;

    return fourWheel.globalAngle;
  }
  public void gyroTurn (int degrees, double power, turnDirections iDir){
    Orientation lastAngles = new Orientation();
    double  leftPower, rightPower;
    int targetAngle = 0;
    int finalAngle = 0;

    if (iDir == turnDirections.LEFT){
      leftPower = -power;
      rightPower = power;
    }
    else {
      leftPower = power;
      rightPower = -power;
    }

    fourWheel.leftMotorBack.setPower(leftPower);
    fourWheel.leftMotorFront.setPower(leftPower);
    fourWheel.rightMotorBack.setPower(rightPower);
    fourWheel.rightMotorFront.setPower(rightPower);


    if (degrees < 0) {
      // On right turn we have to get off zero first.
      while (opModeIsActive() && getAngle() == 0) {}

      while (opModeIsActive() && getAngle() < degrees) {}
    }
    else    // left turn.
      while (opModeIsActive() && getAngle() > degrees) {}

    fourWheel.leftMotorBack.setPower(0.0);
    fourWheel.leftMotorFront.setPower(0.0);
    fourWheel.rightMotorBack.setPower(0.0);
    fourWheel.rightMotorFront.setPower(0.0);

    telemetry.addData("imu",Integer.toString(targetAngle) + ";" + Integer.toString(finalAngle));
    telemetry.update();


    sleep(1000);

    resetAngle();
  }
  public double checkDirection(){
    // The gain value determines how sensitive the correction is to direction changes.
    double correction, angle, gain = .10;

    angle = getAngle();

    if (angle == 0)
      correction = 0;             // no adjustment.
    else
      correction = -angle;        // reverse sign of angle for correction.

    correction = correction * gain;

    return correction;
  }
  /*   TO DO   */
  public void sampling() {

  }
  /*   TO DO   */
  public void drive (double iSpeed, driveDirections iDir) {
    double dirAdj;

    if (iDir == driveDirections.BACKWARD) {
      dirAdj = -1.0;
    } else {
      dirAdj = 1.0;
    }
    fourWheel.leftMotorBack.setPower(iSpeed * dirAdj);
    fourWheel.leftMotorFront.setPower(iSpeed * dirAdj);
    fourWheel.rightMotorBack.setPower(iSpeed * dirAdj);
    fourWheel.rightMotorFront.setPower(iSpeed * dirAdj);

  }
  public void motorStop () {
    fourWheel.leftMotorBack.setPower(0.0);
    fourWheel.leftMotorFront.setPower(0.0);
    fourWheel.rightMotorBack.setPower(0.0);
    fourWheel.rightMotorFront.setPower(0.0);
  }
  public void driveToColor(String color, double power, driveDirections iDir){

    Color.RGBToHSV((int) (fourWheel.color.red() * fourWheel.SCALE_FACTOR),
            (int) (fourWheel.color.green() * fourWheel.SCALE_FACTOR),
            (int) (fourWheel.color.blue() * fourWheel.SCALE_FACTOR),
            fourWheel.hsvValues);
    double red = 0;
    double blue = 0;
    double black = 51;
    double dirAdj;

    if (iDir == driveDirections.BACKWARD) {
      dirAdj = -1.0;
    } else {
      dirAdj = 1.0;
    }


    if (color.equalsIgnoreCase("Red")){
      while (red < 250 && opModeIsActive()) {
        drive(power, iDir);
        red =  fourWheel.color.red();
      }
      motorStop();
    }
    else if (color.equalsIgnoreCase("Blue")){
      while (blue < 200 && opModeIsActive()) {
        drive(power, iDir);
        blue =  fourWheel.color.blue() ;
      }
      motorStop();
    }
    else if (color.equalsIgnoreCase("black")){
      while (black > 50 && opModeIsActive()) {
        drive(power, iDir);
        black =  fourWheel.color.green();

      }
      motorStop();
    }

  }
  public void driveToColor2(String color, double power, driveDirections iDir){

    Color.RGBToHSV((int) (fourWheel.color2.red() * fourWheel.SCALE_FACTOR),
            (int) (fourWheel.color2.green() * fourWheel.SCALE_FACTOR),
            (int) (fourWheel.color2.blue() * fourWheel.SCALE_FACTOR),
            fourWheel.hsvValues);
    double red = 0;
    double blue = 0;
    double black = 51;
    double dirAdj;

    if (iDir == driveDirections.BACKWARD) {
      dirAdj = -1.0;
    } else {
      dirAdj = 1.0;
    }


    if (color.equalsIgnoreCase("Red")){
      while (red < 250 && opModeIsActive()) {
        drive(power, iDir);
        red =  fourWheel.color2.red();
      }
      motorStop();
    }
    else if (color.equalsIgnoreCase("Blue")){
      while (blue < 200 && opModeIsActive()) {
        drive(power, iDir);
        blue =  fourWheel.color2.blue() ;
      }
      motorStop();
    }
    else if (color.equalsIgnoreCase("black")){
      while (black > 50 && opModeIsActive()) {
        drive(power, iDir);
        black =  fourWheel.color2.green();

      }
      motorStop();
    }

  }
  public void  driveTellDistanceChange(){

  }
}