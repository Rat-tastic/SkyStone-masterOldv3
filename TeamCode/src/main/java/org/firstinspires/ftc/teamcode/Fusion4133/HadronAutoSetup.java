package org.firstinspires.ftc.teamcode.Fusion4133;

import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Fusion on 10/22/2017.
 */

public abstract class HadronAutoSetup extends LinearOpMode{

  //This is where the options values are defined.
  AutonomousTextOption    allianceColor       = new AutonomousTextOption("Alliance Color", "blue", new String[] {"blue", "red"});
  AutonomousTextOption    startBal            = new AutonomousTextOption("Start Balance", "Corner", new String [] {"Corner", "Center"});
  AutonomousTextOption    parkSafeZone        = new AutonomousTextOption("Park Safe Zone", "InFront" , new String [] {"InFront", "ToSide"});
  AutonomousIntOption     waitStart           = new AutonomousIntOption("Wait at Start", 0, 0, 20);
  AutonomousBooleanOption scoreGlyph          = new AutonomousBooleanOption("Score Glyph", true);
  AutonomousBooleanOption scoreMultipleGlyphs = new AutonomousBooleanOption("Multiple Glyphs Or Relic", false);

  //This is the order of our options and setting them all to there preset value.
  AutonomousOption [] autoOptions       = {allianceColor, startBal, waitStart, parkSafeZone, scoreGlyph, scoreMultipleGlyphs};
  int currentOption = 0;

  //this setting the buttons to false to make sure options are not being chosen for us.
  boolean aPressed = false;
  boolean bPressed = false;
  boolean xPressed = false;
  boolean yPressed = false;

  MecanumHardwareSetup hadron = new MecanumHardwareSetup();

  //This defines how many encoder ticks are in a degree or an inch.
  final double ticksPerInch       = (31.3396);  //188;  //tick of the encoder * gear ratio / circumference of the wheel
  final double ticksPerInchStrafe = (960 / 13);
  final  int   tickOverRun        = 0;   //number of tick robot overruns target after stop
  final double inchesPerDeg       = 5;  //wheel base of robot * pi / 360
  final double inchesPerDegArm    = .01;
  final double tickPerDeg         = 16.67 * inchesPerDeg;
  final double tickPerDegArm      = 16.67 *inchesPerDegArm;

  ElapsedTime movementTime   = new ElapsedTime();

  //This is so later we have pre deffined what our directions that are possible in drives and turns.
  public enum driveDirections{
    FORWARD, BACKWARD
  }
  public enum turnDirections{
    LEFT, RIGHT
  }
  public enum strafeDirections{
    LEFT, RIGHT
  }
  public enum armDirection{
    OPEN, CLOSE
  }

  // This is how we get our autonomous options to show up on our phones.
  public void showOptions (){
    int index = 0;
    String str = "";

    while (index < autoOptions.length){
      switch (autoOptions[index].optionType){
        case STRING:
          str = ((AutonomousTextOption)autoOptions[index]).getValue();
          break;
        case INT:
          str = Integer.toString(((AutonomousIntOption)autoOptions[index]).getValue());
          break;
        case BOOLEAN:
          str = String.valueOf(((AutonomousBooleanOption)autoOptions[index]).getValue());
          break;
      }

      if (index == currentOption){
        telemetry.addData(Integer.toString(index) + ") ==> " + autoOptions[index].name,str);
      } else {
        telemetry.addData(Integer.toString(index) + ")     " + autoOptions[index].name, str);
      }

      index = index + 1;
    }
    telemetry.update();
  }
  // This is how we select our auto options
  public void selectOptions () {

    while (currentOption< autoOptions.length && !opModeIsActive()){
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

    telemetry.addData("Robot","READY!!");
    telemetry.update();
  }

  //This is our most basic drive that uses time to determine the distance it goes.
  public void driveTime (double iSpeed, int iTime, driveDirections iDir ) {
//        hadron.init(hardwareMap);

    double dirAdj;

    if(iDir == driveDirections.BACKWARD){
      dirAdj  = -1.0;
    }
    else {
      dirAdj  = 1.0;
    }

    hadron.rightFrontDrive.setPower(iSpeed*dirAdj);
    hadron.rightBackDrive.setPower(iSpeed*dirAdj);
    hadron.leftFrontDrive.setPower(iSpeed*dirAdj);
    hadron.leftBackDrive.setPower(iSpeed*dirAdj);

    movementTime.reset();

    while (opModeIsActive() && movementTime.milliseconds() < iTime) {
    }

    hadron.rightFrontDrive.setPower(0.0);
    hadron.rightBackDrive.setPower(0.0);
    hadron.leftFrontDrive.setPower(0.0);
    hadron.leftBackDrive.setPower(0.0);

  }
  //time based movement that closes the collection arms
  public void closeArm (double iSpeed, int iTime){

    double vSpeed = iSpeed;

    double leftDirAdj;
    double rightDirAdj;


    hadron.collectionMotorRight.setPower(-iSpeed);
    hadron.collectionMotorLeft.setPower(-iSpeed);

    movementTime.reset();

    while (opModeIsActive() && movementTime.milliseconds() < iTime) {
    }

    hadron.collectionMotorRight.setPower(-0.2);
    hadron.collectionMotorLeft.setPower(-0.2);


  }
  public void closeArmV2 (double iSpeed, int iTime){

    double vSpeed = iSpeed;

    double leftDirAdj;
    double rightDirAdj;


    hadron.collectionMotorRight.setPower(-iSpeed);
    hadron.collectionMotorLeft.setPower(-iSpeed);

    movementTime.reset();

    while (opModeIsActive() && movementTime.milliseconds() < iTime) {
    }

    hadron.collectionMotorRight.setPower(0.0);
    hadron.collectionMotorLeft.setPower(0.0);


  }
  //time based movement that closes the collection arms is differnt because it does not the the motor speed to 0.0 to stop the motors.
  public void openArm (double iSpeed, int iTime){

    double vSpeed = iSpeed;

    double leftDirAdj;
    double rightDirAdj;


    hadron.collectionMotorRight.setPower(iSpeed);
    hadron.collectionMotorLeft.setPower(iSpeed);

    movementTime.reset();

    while (opModeIsActive() && movementTime.milliseconds() < iTime) {
    }

    hadron.collectionMotorRight.setPower(0.3);
    hadron.collectionMotorLeft.setPower(0.3);


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
      vSpeed = vSpeed * -1;
    }

    leftStartFront = hadron.leftFrontDrive.getCurrentPosition();
    leftStartBack = hadron.leftBackDrive.getCurrentPosition();
    rightStartFront = hadron.rightFrontDrive.getCurrentPosition();
    rightStartBack = hadron.rightBackDrive.getCurrentPosition();

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
    hadron.rightBackDrive.setPower(vSpeed);
    hadron.rightFrontDrive.setPower(vSpeed);
    hadron.leftFrontDrive.setPower(vSpeed );
    hadron.leftBackDrive.setPower(vSpeed);

    if (iDir == driveDirections.FORWARD) {

      //while loop set motor at vSpeed until these conditions are not ture any longer.
      while (opModeIsActive() &&
              hadron.leftBackDrive.getCurrentPosition() < leftAdjTargetBack &&
              hadron.leftFrontDrive.getCurrentPosition() < leftAdjTargetFront &&
              hadron.rightFrontDrive.getCurrentPosition() < rightAdjTargetFront &&
              hadron.rightBackDrive.getCurrentPosition() < rightAdjTargetBack ) {
      }
    }
    else {
      while (opModeIsActive() &&
              hadron.leftFrontDrive.getCurrentPosition() > leftAdjTargetFront &&
              hadron.leftBackDrive.getCurrentPosition() > leftAdjTargetBack &&
              hadron.rightFrontDrive.getCurrentPosition() > rightAdjTargetFront &&
              hadron.rightBackDrive.getCurrentPosition() > rightAdjTargetBack ) {
      }
    }

    hadron.rightBackDrive.setPower(0.0);
    hadron.rightFrontDrive.setPower(0.0);
    hadron.leftBackDrive.setPower(0.0);
    hadron.leftFrontDrive.setPower(0.0);

    leftFinalFront = hadron.leftFrontDrive.getCurrentPosition();
    leftFinalBack = hadron.leftBackDrive.getCurrentPosition();
    rightFinalFront = hadron.rightFrontDrive.getCurrentPosition();
    rightFinalBack = hadron.rightBackDrive.getCurrentPosition();

    telemetry.addData("leftFront",Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront)+ "; " + Integer.toString(leftFinalFront)+ "; " + Integer.toString(leftAdjTargetFront));
    telemetry.addData("leftBack",Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack)+ "; " + Integer.toString(leftFinalBack)+ "; " + Integer.toString(leftAdjTargetBack));
    telemetry.addData("rightFront",Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront)+ "; " + Integer.toString(rightFinalFront) + "; " + Integer.toString(rightAdjTargetFront));
    telemetry.addData("rightBack",Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack)+ "; " + Integer.toString(rightFinalBack) + "; " + Integer.toString(rightAdjTargetBack));

    telemetry.update();
  }
  //This is the how we get that robot to move side to side this one is a little more complicated.
  public void strafeEnc (double iSpeed, int iDist, strafeDirections iDir) {

    double vSpeed = iSpeed;
    //double correction;
    //correction = checkDirection();

    double frontLeftBottomRightAdj;
    double bottomLeftFrontRightAdj;

    //next lines are just undefined integer values that make writing the rest of the code allot easier
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

    //these are adjustment values allow us to write one void for both left and right movements.
    if (iDir == strafeDirections.RIGHT) {
      frontLeftBottomRightAdj = 1.0;
      bottomLeftFrontRightAdj = -1.0;
    }
    else {
      frontLeftBottomRightAdj = -1.0;
      bottomLeftFrontRightAdj = 1.0;
    }

    //checks value encoder is at then sets value as start integers.
    leftStartFront = hadron.leftFrontDrive.getCurrentPosition();
    leftStartBack = hadron.leftBackDrive.getCurrentPosition();
    rightStartFront = hadron.rightFrontDrive.getCurrentPosition();
    rightStartBack = hadron.rightBackDrive.getCurrentPosition();

    //takes value the we put into iDist above and translates that inch value into tick values that the code can understand.
    leftTargetFront = leftStartFront + (int) (iDist * ticksPerInchStrafe * frontLeftBottomRightAdj);
    leftTargetBack = leftStartBack + (int) (iDist * ticksPerInchStrafe * bottomLeftFrontRightAdj);
    rightTargetFront = rightStartFront + (int) (iDist * ticksPerInchStrafe * bottomLeftFrontRightAdj);
    rightTargetBack = rightStartBack + (int) (iDist * ticksPerInchStrafe * frontLeftBottomRightAdj);

    //makes an adjusted value that takes over run into acount so we end up close to desired value
    leftAdjTargetFront = leftTargetFront - (int) (tickOverRun * frontLeftBottomRightAdj);
    leftAdjTargetBack = leftTargetBack - (int) (tickOverRun * bottomLeftFrontRightAdj);
    rightAdjTargetFront = rightTargetFront - (int) (tickOverRun * bottomLeftFrontRightAdj);
    rightAdjTargetBack = rightTargetBack - (int) (tickOverRun * frontLeftBottomRightAdj);

    telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
    telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
    telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
    telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

    telemetry.update();

    //sets the power of the motors based on the direction we are strafing in.
    if (iDir == strafeDirections.RIGHT){
      hadron.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
      hadron.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj );
      hadron.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj );
      hadron.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
    }
    else {
      hadron.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
      hadron.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
      hadron.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
      hadron.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
    }



    if ((iDir == strafeDirections.RIGHT)) {

      //until the values set here are false it will hold the speed of the motors.
      while (opModeIsActive() &&
              hadron.leftFrontDrive.getCurrentPosition() < leftAdjTargetFront &&
              hadron.leftBackDrive.getCurrentPosition() > leftAdjTargetBack &&
              hadron.rightFrontDrive.getCurrentPosition() > rightAdjTargetFront &&
              hadron.rightBackDrive.getCurrentPosition() < rightAdjTargetBack) {
      }
    } else {
      while (opModeIsActive() &&
              hadron.leftFrontDrive.getCurrentPosition() > leftAdjTargetFront &&
              hadron.leftBackDrive.getCurrentPosition() < leftAdjTargetBack &&
              hadron.rightFrontDrive.getCurrentPosition() < rightAdjTargetFront &&
              hadron.rightBackDrive.getCurrentPosition() > rightAdjTargetBack) {
      }
    }

    hadron.rightBackDrive.setPower(0.0);
    hadron.rightFrontDrive.setPower(0.0);
    hadron.leftBackDrive.setPower(0.0);
    hadron.leftFrontDrive.setPower(0.0);

    //checks values of encoders at the end of the program and then reads them to us so we know if the we are getting to desired values.
    leftFinalFront = hadron.leftFrontDrive.getCurrentPosition();
    leftFinalBack = hadron.leftBackDrive.getCurrentPosition();
    rightFinalFront = hadron.rightFrontDrive.getCurrentPosition();
    rightFinalBack = hadron.rightBackDrive.getCurrentPosition();

    telemetry.addData("Done leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
    telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
    telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
    telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

    telemetry.update();

  }
  //This is a basic sin turn that reverses one side to make a turn.
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

    if (iDir == turnDirections.RIGHT) {
      leftDirAdj = 1.0;
      rightDirAdj = -1.0;
    } else {
      leftDirAdj = -1.0;
      rightDirAdj = 1.0;
    }

    leftStartFront = hadron.leftFrontDrive.getCurrentPosition();
    leftStartBack = hadron.leftBackDrive.getCurrentPosition();
    rightStartFront = hadron.rightFrontDrive.getCurrentPosition();
    rightStartBack = hadron.rightBackDrive.getCurrentPosition();

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

    if (iDir == turnDirections.RIGHT){
      hadron.rightFrontDrive.setPower(iSpeed * rightDirAdj);
      hadron.rightBackDrive.setPower(iSpeed * rightDirAdj);
      hadron.leftBackDrive.setPower(iSpeed * leftDirAdj);
      hadron.leftFrontDrive.setPower(iSpeed * leftDirAdj);
    }
    else {
      hadron.rightFrontDrive.setPower(iSpeed * rightDirAdj);
      hadron.rightBackDrive.setPower(iSpeed * rightDirAdj);
      hadron.leftBackDrive.setPower(iSpeed * leftDirAdj);
      hadron.leftFrontDrive.setPower(iSpeed * leftDirAdj);
    }

    if ((iDir == turnDirections.LEFT)) {

      while (opModeIsActive() &&
              hadron.leftFrontDrive.getCurrentPosition() > leftTargetFront &&
              hadron.leftBackDrive.getCurrentPosition() > leftTargetBack &&
              hadron.rightFrontDrive.getCurrentPosition() < rightTargetFront &&
              hadron.rightBackDrive.getCurrentPosition() < rightTargetBack) {
      }
    } else {
      while (opModeIsActive() &&
              hadron.leftFrontDrive.getCurrentPosition() < leftTargetFront &&
              hadron.leftBackDrive.getCurrentPosition() < leftTargetBack &&
              hadron.rightFrontDrive.getCurrentPosition() > rightTargetFront &&
              hadron.rightBackDrive.getCurrentPosition() > rightTargetBack) {
      }
    }

    hadron.rightBackDrive.setPower(0.0);
    hadron.rightFrontDrive.setPower(0.0);
    hadron.leftBackDrive.setPower(0.0);
    hadron.leftFrontDrive.setPower(0.0);

    leftFinalFront = hadron.leftFrontDrive.getCurrentPosition();
    leftFinalBack = hadron.leftBackDrive.getCurrentPosition();
    rightFinalFront = hadron.rightFrontDrive.getCurrentPosition();
    rightFinalBack = hadron.rightBackDrive.getCurrentPosition();

    telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
    telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
    telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
    telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

    telemetry.update();

  }
  private void resetAngle()
  {
    hadron.lastAngles = hadron.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

    hadron.globalAngle = 0;
  }
  private double getAngle()
  {
    Orientation angles = hadron.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

    double deltaAngle = angles.firstAngle - hadron.lastAngles.firstAngle;

    if (deltaAngle < -180)
      deltaAngle += 360;
    else if (deltaAngle > 180)
      deltaAngle -= 360;

    hadron.globalAngle += deltaAngle;

    hadron.lastAngles = angles;

    return hadron.globalAngle;
  }
  public void gyroTurn (int degrees, double power, turnDirections iDir){
    Orientation lastAngles = new Orientation();
    double  leftPower, rightPower;

    if (iDir == turnDirections.LEFT){
      leftPower = -power;
      rightPower = power;
    }
    else {
      leftPower = power;
      rightPower = -power;
    }

    hadron.rightBackDrive.setPower(rightPower);
    hadron.rightFrontDrive.setPower(rightPower);
    hadron.leftBackDrive.setPower(leftPower);
    hadron.leftFrontDrive.setPower(leftPower);

    if (degrees < 0) {
      // On right turn we have to get off zero first.
      while (opModeIsActive() && getAngle() == 0) {}

      while (opModeIsActive() && getAngle() > degrees) {}
    }
    else {   // left turn.
      while (opModeIsActive() && getAngle() < degrees) {}
    }

    hadron.rightBackDrive.setPower(0.0);
    hadron.rightFrontDrive.setPower(0.0);
    hadron.leftBackDrive.setPower(0.0);
    hadron.leftFrontDrive.setPower(0.0);

    sleep(1000);

    resetAngle();
  }
  public double checkDirection()
  {
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

  public void relicArmOut (double iSpeed, int iTime){

    hadron.relicArm.setPower(-iSpeed);

    movementTime.reset();

    while (opModeIsActive() && movementTime.milliseconds() < iTime) {
    }

    hadron.relicArm.setPower(0.0);
  }

  public void relicArmIn (double iSpeed, int iTime){
    hadron.relicArm.setPower(iSpeed);

    movementTime.reset();

    while (opModeIsActive() && movementTime.milliseconds() < iTime) {
    }

    hadron.relicArm.setPower(0.0);
  }


  public void liftEnc (double iSpeed, int iDist, driveDirections iDir) {
    double vSpeed = iSpeed;

    double dirAdj;

    int liftTarget;
    int liftStart = 0;
    int liftFinal = 0;
    int liftAdjTarget;

    if (iDir == driveDirections.FORWARD) {
      dirAdj = 1.0;
    } else {
      dirAdj = -1.0;
      vSpeed = vSpeed * -1;
    }

    liftStart = hadron.liftDrive.getCurrentPosition();
    liftTarget = liftStart + (int) (iDist * tickPerDegArm * dirAdj);
    liftAdjTarget = liftTarget - (int) (tickOverRun * dirAdj);
    telemetry.addData("lift", Integer.toString(liftStart) + "; " + Integer.toString(liftTarget) + "; " + Integer.toString(liftFinal));
    telemetry.update();

    hadron.liftDrive.setPower(vSpeed * dirAdj);

    if (iDir == driveDirections.FORWARD) {

      while (opModeIsActive() &&
              hadron.liftDrive.getCurrentPosition() < liftAdjTarget) {
      }
    } else {
      while (opModeIsActive() &&
              hadron.liftDrive.getCurrentPosition() > liftAdjTarget) {
      }
    }


    hadron.liftDrive.setPower(0.2);

    liftFinal = hadron.liftDrive.getCurrentPosition();
    telemetry.addData("Done lift", Integer.toString(liftStart) + "; " + Integer.toString(liftTarget) + "; " + Integer.toString(liftFinal));

  }
}






