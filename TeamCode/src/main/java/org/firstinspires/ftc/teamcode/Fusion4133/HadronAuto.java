package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Light;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Fusion on 10/29/2017.
 */
@Disabled
@Autonomous(name="Hadron: Autonomous", group="Hadron")
public class HadronAuto extends HadronAutoSetup {

  public static final String TAG = "Vuforia VuMark Sample";
  public static final long TIMETOCLOSE = 1500;
  public static final int COLORREDVAL = 20;
  public static final int COLORBLUEVAL = 10;

  int jewelMovement;
  int whatColoumScored;

  OpenGLMatrix lastLocation = null;

  /**
   * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
   * localization engine.
   */
  VuforiaLocalizer vuforia;

  @Override
  public void runOpMode() {

    hadron.hardwareInit(hardwareMap);
    hadron.servoInit(hardwareMap);
    hadron.imuInit(hardwareMap);



    ElapsedTime operationTime = new ElapsedTime();
    ElapsedTime autoRunTime = new ElapsedTime();

    /*
     * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
     * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
     */
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

    parameters.vuforiaLicenseKey = "AYANrAj/////AAAAGYnW9dGVvktdtrC584fnddB5coiWHr7rKj2IfqqfxrEe9x2tl0duihyYtjxdNZCF+fHELOupTUa7Lg9zu7YFb+AioBt+ySeVkIUOrwNVtPzQF0/nFikcg47hLp2E4sWAYB/4y896oKXmUOafIx4Num0+RvzaRkrgZIXv1KWZeWLwWH7/1lEqR5fkr1UQh6A5hyGysbu73GIZII3+X1tudZhTjgF0BMGzFQLLl6jcRp7ZXqHE/xVcvVLcVAt/QivYb+QKLjln9klg8YocFl9OyKp1lkJvvtapBFxEAmz7Wts7vb16GUnpOIQ7jZhiVWBHf1y1//tFbxKMfhJQ3QQWK96zRbdwIKBhfYm+dmVrlAuj";

    /*
     * We also indicate which camera on the RC that we wish to use.
     * Here we chose the back (HiRes) camera (for greater range), but
     * for a competition robot, the front camera might be more convenient.
     */
    parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
    this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

    /**
     * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
     * in this data set: all three of the VuMarks in the game were created from this one template,
     * but differ in their instance id information.
     * @see VuMarkInstanceId
     */
    VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    VuforiaTrackable relicTemplate = relicTrackables.get(0);
    relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

    telemetry.log().add("Select Options");
    telemetry.update();

    selectOptions();

    //because we have one linear slide that switch side the init position will be opposite. we also have to do this so
    //low in the program because we do not know our alliance color until we choose it.
    telemetry.addData("Mode", "calibrating...");
    telemetry.update();

    // make sure the imu gyro is calibrated before continuing.
    while (!isStopRequested()&& !hadron.imu.isGyroCalibrated())
    {
      sleep(50);
      idle();
    }

    telemetry.addData("Mode", "waiting for start");
    telemetry.addData("imu calib status", hadron.imu.getCalibrationStatus().toString());
    telemetry.update();

    telemetry.log().add("Waiting For Start");
    telemetry.update();

    if (hadron.color instanceof SwitchableLight) {
      ((SwitchableLight) hadron.color).enableLight(true);
    }
    CameraDevice.getInstance().setFlashTorchMode(true);
//here
    waitForStart();


    autoRunTime.reset();

    telemetry.log().add("Program Started");
    telemetry.update();

    relicTrackables.activate();



    sleep(TIMETOCLOSE);
    sleep(Math.max(((long) (waitStart.getValue() * 1000) - TIMETOCLOSE), 0));

    operationTime.reset();

    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
    while (vuMark == RelicRecoveryVuMark.UNKNOWN && opModeIsActive() && operationTime.milliseconds() < 5000) {
      vuMark = RelicRecoveryVuMark.from(relicTemplate);
    }

    closeArm(1.0, 1000);
    liftEnc(1.0, 5, driveDirections.FORWARD);

    if (hadron.color.red() > hadron.color.blue() && hadron.color.red() > COLORREDVAL) {
      if (allianceColor.getValue().equals("red")) {

        sleep(700);
        driveEnc(.5, 5, driveDirections.FORWARD);
      } else {

        sleep(700);
        driveEnc(.5, 5, driveDirections.FORWARD);
      }
    } else if (hadron.color.blue() > hadron.color.red() && hadron.color.blue() > COLORBLUEVAL) {
      if (allianceColor.getValue().equals("blue")) {
        driveEnc(.5, 5, driveDirections.FORWARD);
        jewelMovement = 5;
        sleep(700);
      } else {
        driveEnc(.5, 5, driveDirections.FORWARD);
        jewelMovement = 5;
        sleep(700);
      }
    } else {
      jewelMovement = 0;
      sleep(700);
      driveEnc(.5, 5, driveDirections.FORWARD);

    }
    telemetry.update();
    sleep(1000);

    if (startBal.getValue().equals("Corner") && allianceColor.getValue().equals("red")) {
      redCornerMove(vuMark);
    } else if (startBal.getValue().equals("Center") && allianceColor.getValue().equals("red")) {
      redCenterMove(vuMark);
    } else if (startBal.getValue().equals("Corner") && allianceColor.getValue().equals("blue")) {
      blueCornerMove(vuMark);
    } else if (startBal.getValue().equals("Center") && allianceColor.getValue().equals("blue")) {
      blueCenterMove(vuMark);
    }

    if (scoreMultipleGlyphs.getValue() && allianceColor.getValue().equals("red")) {
      scoreTwoGlyphsRed();
    }
    else if (scoreMultipleGlyphs.getValue() && allianceColor.getValue().equals("blue")) {
      scoreTwoGlyphsBlue();
    }
  }

  public void redCornerMove(RelicRecoveryVuMark vuMark) {
    //GOOD ON 2-24-18
    if (vuMark == RelicRecoveryVuMark.CENTER) {

      driveEnc(.5, 13, driveDirections.FORWARD);
      sleep(1000);
      gyroTurn(-35, .5, turnDirections.RIGHT);
      whatColoumScored = 1;
    } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
      //GOOD ON 2-24-18
      driveEnc(.5, 20, driveDirections.FORWARD);
      gyroTurn(-55, .5,turnDirections.RIGHT);
      whatColoumScored = 2;
    } else {
      //GOOD ON 2-24-18
      driveEnc(.5, 20, driveDirections.FORWARD);
      gyroTurn(-29, .5, turnDirections.RIGHT);
      whatColoumScored = 3;
    }

    openArm(.25, 500);
    driveTime(.5, 1000, driveDirections.FORWARD);
    driveEnc(1.0, 15, driveDirections.BACKWARD);

    driveTime(.5, 500, driveDirections.FORWARD);
    driveEnc(1.0, 8, driveDirections.BACKWARD);
  }

  public void redCenterMove(RelicRecoveryVuMark vuMark) {

    driveEnc(.5, 23, driveDirections.FORWARD);
    if (vuMark == RelicRecoveryVuMark.CENTER) {
      strafeEnc(.5, 10, strafeDirections.LEFT);
      whatColoumScored = 1;
    } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
      strafeEnc(.5, 4, strafeDirections.LEFT);
      whatColoumScored = 2;

    } else {
      strafeEnc(.5, 16, strafeDirections.LEFT);
      whatColoumScored = 3;
    }

    openArm(.25, 500);
    driveTime(.5, 1000, driveDirections.FORWARD);
    driveEnc(1.0, 15, driveDirections.BACKWARD);

    driveTime(.5, 500, driveDirections.FORWARD);
    driveEnc(1.0, 8, driveDirections.BACKWARD);
  }

  public void blueCornerMove(RelicRecoveryVuMark vuMark) {

    if (vuMark == RelicRecoveryVuMark.CENTER) {

      driveEnc(.5, 13, driveDirections.FORWARD);
      sleep(1000);
      gyroTurn(35, .5, turnDirections.LEFT);
      whatColoumScored = 1;
    } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
      driveEnc(.5, 20, driveDirections.FORWARD);
      gyroTurn(29, .5, turnDirections.LEFT);
      whatColoumScored = 3;
    } else {
      driveEnc(.5, 20, driveDirections.FORWARD);
      gyroTurn(55, .5,turnDirections.LEFT);
      whatColoumScored = 2;

    }

    openArm(.25, 500);
    driveTime(.5, 1000, driveDirections.FORWARD);
    driveEnc(1.0, 15, driveDirections.BACKWARD);

    driveTime(.5, 500, driveDirections.FORWARD);
    driveEnc(1.0, 8, driveDirections.BACKWARD);
  }

  public void blueCenterMove(RelicRecoveryVuMark vuMark) {
    driveEnc(.5, 23 , driveDirections.FORWARD);
    if (vuMark == RelicRecoveryVuMark.CENTER) {
      strafeEnc(.5, 10, strafeDirections.RIGHT);
      whatColoumScored = 1;
    } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
      strafeEnc(.5, 14, strafeDirections.RIGHT);
      whatColoumScored = 2;

    } else {
      strafeEnc(.5, 3, strafeDirections.RIGHT);
      whatColoumScored = 3;
    }

    openArm(.25, 500);
    driveTime(.5, 1000, driveDirections.FORWARD);
    driveEnc(1.0, 15, driveDirections.BACKWARD);

    driveTime(.5, 500, driveDirections.FORWARD);
    driveEnc(1.0, 8, driveDirections.BACKWARD);
  }

  public void scoreTwoGlyphsRed() {
    if (startBal.getValue().equals("Corner")) {
      if (whatColoumScored == 2) {
        driveEnc(.5, 10, driveDirections.BACKWARD);
        gyroTurn(90, .9, turnDirections.LEFT);
        driveEnc(.5, 15, driveDirections.FORWARD);
        gyroTurn(15, .9, turnDirections.LEFT);
      }
      else if (whatColoumScored == 1) {
        //driveEnc(.5, 10, driveDirections.BACKWARD);
        gyroTurn(95, .9, turnDirections.LEFT);
      }
      else if (whatColoumScored == 3){
        driveEnc(.5, 10, driveDirections.BACKWARD);
        gyroTurn(92, .9, turnDirections.LEFT);
      }

      if (whatColoumScored == 1) {
        driveEnc(1.0, 20, driveDirections.FORWARD);
        sleep(1000);
        closeArm(1.0, 750);
        driveEnc(.5, 10, driveDirections.BACKWARD);
        gyroTurn(140, .9, turnDirections.LEFT);
        openArm(.25, 500);
        driveTime(.5, 2000, driveDirections.FORWARD);
        driveEnc(1.0, 5, driveDirections.BACKWARD);
      } else {
        closeArmV2(.2, 20);
        driveEnc(1.0, 40, driveDirections.FORWARD);
        sleep(1000);
        closeArm(1.0, 750);
        driveEnc(.5, 15, driveDirections.BACKWARD);
        gyroTurn(140, .9, turnDirections.LEFT);
        openArm(.25, 500);
        driveTime(.5, 3000, driveDirections.FORWARD);
        driveEnc(1.0, 5, driveDirections.BACKWARD);
      }
    } else {
      if (whatColoumScored == 2) {
        driveEnc(.9, 10, driveDirections.BACKWARD);
        gyroTurn(185, .7, turnDirections.LEFT);


        sleep(500);
        relicArmOut(.9, 3000);

        sleep(500);
        relicArmIn(.9, 2500);
        //right//

      }
      else if (whatColoumScored == 1) {
        driveEnc(.9, 10, driveDirections.BACKWARD);
        gyroTurn(185, .7, turnDirections.LEFT);
        driveEnc(.5, 2, driveDirections.FORWARD);


        sleep(500);
        relicArmOut(.9, 4000);

        sleep(500);
        relicArmIn(.9, 3500);
        //center//

      }
      else if (whatColoumScored == 3) {
        driveEnc(.9, 10, driveDirections.BACKWARD);
        gyroTurn(180, .7, turnDirections.LEFT);
        driveEnc(.5, 2, driveDirections.FORWARD);


        sleep(500);
        relicArmOut(.9, 5500);

        sleep(500);
        relicArmIn(.9, 5000);

        //left//

      }
      driveEnc(.5, 2, driveDirections.BACKWARD);
    }
  }



  public void scoreTwoGlyphsBlue() {
    if (startBal.getValue().equals("Corner")) {
      if (whatColoumScored == 2) {
        driveEnc(.5, 10, driveDirections.BACKWARD);
        gyroTurn(-90, .9, turnDirections.RIGHT);
        driveEnc(.5, 15, driveDirections.FORWARD);
        gyroTurn(-15, .9, turnDirections.RIGHT);
      }
      else if (whatColoumScored == 1) {
        //driveEnc(.5, 10, driveDirections.BACKWARD);
        gyroTurn(-95, .9, turnDirections.RIGHT);
      }
      else if (whatColoumScored == 3){
        driveEnc(.5, 10, driveDirections.BACKWARD);
        gyroTurn(-92, .9, turnDirections.RIGHT);
      }

      if (whatColoumScored == 1) {
        driveEnc(1.0, 20, driveDirections.FORWARD);
        sleep(1000);
        closeArm(1.0, 750);
        driveEnc(.5, 10, driveDirections.BACKWARD);
        gyroTurn(-140, .9, turnDirections.RIGHT);
        openArm(.25, 500);
        driveTime(.5, 2000, driveDirections.FORWARD);
        driveEnc(1.0, 5, driveDirections.BACKWARD);
      } else {
        closeArmV2(.2, 20);
        driveEnc(1.0, 40, driveDirections.FORWARD);
        sleep(1000);
        closeArm(1.0, 750);
        driveEnc(.5, 15, driveDirections.BACKWARD);
        gyroTurn(-140, .9, turnDirections.LEFT);
        openArm(.25, 500);
        driveTime(.5, 3000, driveDirections.FORWARD);
        driveEnc(1.0, 5, driveDirections.BACKWARD);
      }
    } else {
      if (whatColoumScored == 3) {
        driveEnc(.9, 10, driveDirections.BACKWARD);
        gyroTurn(-30, .7, turnDirections.RIGHT);


        sleep(500);
        relicArmOut(.9, 3000);

        sleep(500);
        relicArmIn(.9, 2500);
        //left//

      }
      else if (whatColoumScored == 1) {
        driveEnc(.9, 10, driveDirections.BACKWARD);
        gyroTurn(-30, .7, turnDirections.RIGHT);
        //driveEnc(.5, 2, driveDirections.FORWARD);


        sleep(500);
        relicArmOut(.9, 4000);

        sleep(500);
        relicArmIn(.9, 3500);
        //center//

      }
      else if (whatColoumScored == 2) {
        driveEnc(.9, 10, driveDirections.BACKWARD);
        gyroTurn(-30, .7, turnDirections.RIGHT);
        //driveEnc(.5, 2, driveDirections.FORWARD);


        sleep(500);
        relicArmOut(.9, 5500);

        sleep(500);
        relicArmIn(.9, 5000);

        //rightt//

      }
      driveEnc(.5, 2, driveDirections.BACKWARD);
    }
  }

}




