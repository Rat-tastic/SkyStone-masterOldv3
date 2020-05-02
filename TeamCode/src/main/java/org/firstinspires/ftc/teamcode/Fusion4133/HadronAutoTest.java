package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Fusion on 11/8/2017.
 */
@Disabled
@Autonomous(name="HadronAutonomousTest", group="Hadron")

public class HadronAutoTest extends HadronAutoSetup {


  public static final String TAG = "Vuforia VuMark Sample";
  public static final long TIMETOCLOSE = 1500;
  public static final int COLORREDVAL = 20;
  public static final int COLORBLUEVAL = 10;
  int jewelMovement;

  public void runOpMode() {


    hadron.hardwareInit(hardwareMap);
    hadron.servoInit(hardwareMap);
    waitForStart();

    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);
    driveEnc(.5, 185, driveDirections.FORWARD);
    spinEnc(.5, 120, turnDirections.LEFT);


  }

}
