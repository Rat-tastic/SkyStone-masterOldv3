package org.firstinspires.ftc.teamcode.Fusion4133;

import android.graphics.Color;
import android.hardware.Sensor;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Fusion on 10/22/2017.
 */

public class MecanumHardwareSetup {

  //this is our set servo positions that will be used in teleOp and Auto.

  public static double colorStickUp   = .0;
  public static double colorStickDown     = 1.0;
  public static double linearSlideleft  = .63;
  public static double linearSlideRight = .95;
  public static double graberClosed     = .55;
  public static double graberOpen     = 1.0;
  public static double fliperUp     = .24;
  public static double fliperGrab     = 1.0;
  public static double fliperInit     = .61;


    /*public static double bgtOpen   = .216;
    public static double bgtClosed = .052;
    public static double bgtInit   = .383;
    public static double bgbOpen   = .379;
    public static double bgbClosed = .150;
    public static double bgbInit   = .578;

    //This is where we define our motors as Dcmotors meaning that this is when we tell the code what the motors are as something
    // it already understands.*/

  DcMotor rightFrontDrive;
  DcMotor rightBackDrive;
  DcMotor leftFrontDrive;
  DcMotor leftBackDrive;
  DcMotor liftDrive;
  DcMotor relicArm;
  DcMotor collectionMotorLeft;
  DcMotor collectionMotorRight;
  double globalAngle, power = .30, correction;
  Orientation lastAngles = new Orientation();


  //This is where we do the same thing as above just for servos.



  //This is where we define the sensors.

  ColorSensor color;
  BNO055IMU   imu;

  public void hardwareInit(HardwareMap hwMap){
    rightFrontDrive      = hwMap.dcMotor.get("rfd");
    rightBackDrive       = hwMap.dcMotor.get("rbd");
    leftFrontDrive       = hwMap.dcMotor.get("lfd");
    leftBackDrive        = hwMap.dcMotor.get("lbd");
 /*   relicArm             = hwMap.dcMotor.get("ra");
    liftDrive            = hwMap.dcMotor.get("ld");
    collectionMotorLeft  = hwMap.dcMotor.get("cml");
    collectionMotorRight = hwMap.dcMotor.get("cmr"); */

    rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
    rightBackDrive.setDirection(DcMotor.Direction.REVERSE);
    //liftDrive.setDirection(DcMotor.Direction.REVERSE);
    collectionMotorRight.setDirection(DcMotor.Direction.REVERSE);

    leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);






  }

  //This is where we make a transferable Hardware map Or the init phase is, that can be put in any of our programs.

  public void servoInit(HardwareMap hwMap) {





    //blockGrabberBottom.setPosition(bgbInit);
    //blockGrabberTop.setPosition(bgtInit);
  }
  public void imuInit(HardwareMap hwMap){
    imu                  = hwMap.get(BNO055IMU.class, "imu");

    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    parameters.mode                = BNO055IMU.SensorMode.IMU;
    parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
    parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    parameters.loggingEnabled      = false;

    imu.initialize(parameters);
  }


}
