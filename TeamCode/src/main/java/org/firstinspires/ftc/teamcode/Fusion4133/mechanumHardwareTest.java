package org.firstinspires.ftc.teamcode.Fusion4133;

import android.graphics.Bitmap;
import android.os.Environment;

import com.qualcomm.ftccommon.CommandList;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Warning;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

public class mechanumHardwareTest {


    // Constants for perimeter targets


    // Class Members

    DcMotor rightFrontDrive;
    DcMotor rightBackDrive;
    DcMotor leftFrontDrive;
    DcMotor leftBackDrive;
    DcMotor leftCollection;
    DcMotor rightCollection;
    DcMotor liftMotor;
    double globalAngle, power = .30, correction;
    Orientation lastAngles = new Orientation();

    Servo platformHolderR;
    Servo platformHolderL;
    Servo graber;
    Servo blockArm;
    Servo blockArm2;
    Servo grabberSecR;
    Servo grabberSecL;
    CRServo graberF;
    CRServo graberB;
    CRServo extendorS;
    CRServo topSlide;
    CRServo leftSweeper;
    CRServo rightSweeper;
    CRServo colorStick;

    BNO055IMU   imu;
    RevColorSensorV3 color;
    RevColorSensorV3 color2;
    private static boolean testMode = false;
    private static OpMode opMode;
    private static HardwareMap hardwareMap;
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;



    public void hardwareInit(HardwareMap hwMap) {

        rightFrontDrive = hwMap.dcMotor.get("rfd");
        rightBackDrive = hwMap.dcMotor.get("rbd");
        leftFrontDrive = hwMap.dcMotor.get("lfd");
        leftBackDrive = hwMap.dcMotor.get("lbd");
        leftCollection = hwMap.dcMotor.get("lc");
        rightCollection = hwMap.dcMotor.get("rc");
        liftMotor       = hwMap.dcMotor.get("lm");

        platformHolderR = hwMap.servo.get("pr");
        platformHolderL = hwMap.servo.get("pl");
        blockArm       = hwMap.servo.get("ba");
        blockArm2      = hwMap.servo.get("ba2");
        graber         = hwMap.servo.get("gr");
        //graberB        = hwMap.crservo.get("gb");
        //graberF        = hwMap.crservo.get("gf");
        extendorS      = hwMap.crservo.get("es");
        //topSlide       = hwMap.crservo.get("ts");
        leftSweeper    = hwMap.crservo.get("ls");
        rightSweeper   = hwMap.crservo.get("rs");
        grabberSecR    = hwMap.servo.get("sr");
        grabberSecL    = hwMap.servo.get("sl");
        colorStick     = hwMap.crservo.get("cs");


        color = hwMap.get( RevColorSensorV3.class, "clr");

        color2 = hwMap.get( RevColorSensorV3.class, "clr2");

        blockArm.setPosition(1);

        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightCollection.setDirection(DcMotor.Direction.REVERSE);
        rightSweeper.setDirection(CRServo.Direction.REVERSE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        blockArm2.setPosition(.3);
        blockArm.setPosition(1);
        grabberSecR.setPosition(.3);
        grabberSecL.setPosition(.5);
        platformHolderR.setPosition(.47);
        platformHolderL.setPosition(.52);
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

    public void motorStrafeLeft(double iSpeed){
        rightFrontDrive.setPower(-iSpeed);
        rightBackDrive.setPower(iSpeed);
        leftFrontDrive.setPower(iSpeed);
        leftBackDrive.setPower(-iSpeed);
    }

    public void motorStrafeRight(double iSpeed){
        rightFrontDrive.setPower(iSpeed);
        rightBackDrive.setPower(-iSpeed);
        leftFrontDrive.setPower(-iSpeed);
        leftBackDrive.setPower(iSpeed);
    }

    public void motorForward(double iSpeed){
        rightFrontDrive.setPower(-iSpeed);
        rightBackDrive.setPower(-iSpeed);
        leftFrontDrive.setPower(-iSpeed);
        leftBackDrive.setPower(-iSpeed);
    }

    public void motorBackward(double iSpeed){
        rightFrontDrive.setPower(iSpeed);
        rightBackDrive.setPower(iSpeed);
        leftFrontDrive.setPower(iSpeed);
        leftBackDrive.setPower(iSpeed);
    }
    public void motorSpinLeft(double iSpeed){
        rightFrontDrive.setPower(-iSpeed);
        rightBackDrive.setPower(-iSpeed);
        leftFrontDrive.setPower(iSpeed);
        leftBackDrive.setPower(iSpeed);
    }
    public void motorSpinRight(double iSpeed){
        rightFrontDrive.setPower(iSpeed);
        rightBackDrive.setPower(iSpeed);
        leftFrontDrive.setPower(-iSpeed);
        leftBackDrive.setPower(-iSpeed);
    }

    public void motorStop(){
        rightFrontDrive.setPower(0.0);
        rightBackDrive.setPower(0.0);
        leftFrontDrive.setPower(0.0);
        leftBackDrive.setPower(0.0);
    }
    public static void saveImage(Bitmap bitmap){
        Calendar now = Calendar.getInstance();
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String fileName = "BotImg_"+ now.get(Calendar.DAY_OF_MONTH) + "_" + now.get(Calendar.HOUR_OF_DAY) + "_" + now.get(Calendar.MINUTE) + "_" + now.get(Calendar.SECOND) + now.get(Calendar.MILLISECOND) +".jpg";
        File img = new File(filePath, fileName);
        if (img.exists())
            img.delete();
        try {
            FileOutputStream out = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();


        } catch (Exception e) {
            throw new Warning(e.getMessage());
        }
    }
    public static void addData(String caption, Object object){

        if(!testMode) {
            opMode.telemetry.addData(caption, object);
        } else {
            System.out.println(caption + ": " + object);
        }
    }
    public static void updateOpLogger(){
        if(!testMode){
            opMode.telemetry.update();
        }
    }
    public static HardwareMap getHardwareMap() {
        return mechanumHardwareTest.hardwareMap;
    }
    public static OpMode getOpMode(){
        return mechanumHardwareTest.opMode;
    }
    public static void setOpMode(OpMode opMode){
        mechanumHardwareTest.opMode = opMode;
        mechanumHardwareTest.hardwareMap = opMode.hardwareMap;
    }



}
