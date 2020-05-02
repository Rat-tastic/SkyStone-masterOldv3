package org.firstinspires.ftc.teamcode.Fusion4133;

import android.hardware.Sensor;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Fusion on 10/21/2018.
 */
//Hardware setup class defines motors, servos, and ect.
public class FourWheelHardwareSetup {
    //defines and sets motorso null val
    public DcMotor leftMotorBack   = null;
    public DcMotor leftMotorFront  = null;
    public DcMotor rightMotorBack  = null;
    public DcMotor rightMotorFront = null;
    public DcMotor rightLiftMotor  = null;
    public DcMotor leftliftMotor   = null;
    //public DcMotor collectionMotor = null;
    public DcMotor leadScrew       = null;
    public DcMotor leadScrewLander = null;





    public CRServo flapServo;
    //public Servo collectionServoRight;
    public CRServo collectionServoRight;
    public Servo phoneServo;
    public Servo blockStopperServo;
    public Servo markerServo;
    public Servo fingerServo;

    public ColorSensor color;
    public ColorSensor color2;
    //public TouchSensor liftButton;
    public AnalogInput potentiometer;

    //sets up double vals for later in teleopp
    public final double blockStopperClosed = 0.0;
    public final double blockStopperOpen = 1.0;
    double leftPower;
    double rightPower;
    double liftPower;
    double linearSlideleft  = .52;
    double linearSlideRight = .95;
    double globalAngle, power = .30, correction;
    Orientation lastAngles = new Orientation();
    double dropMarker = .38;
    double holdMaker = .78;

    public final double phonesPosUp   = 0.0;
    public final double phonesPosDown = .23;

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;

    //sensor setup
    BNO055IMU imu;

    //makes calleble void for teleop
    public void hardwareInit(HardwareMap hwMap) {

        leftMotorFront  = hwMap.dcMotor.get("lmf");
        leftMotorBack   = hwMap.dcMotor.get("lmb");
        rightMotorFront = hwMap.dcMotor.get("rmf");
        rightMotorBack  = hwMap.dcMotor.get("rmb");
        leftliftMotor   = hwMap .dcMotor.get("llm");
        rightLiftMotor  = hwMap.dcMotor.get("rlm");
        //collectionMotor = hwMap.dcMotor.get("cm");
        leadScrew       = hwMap.dcMotor.get("ls");

        //collectionServoRight = hwMap.servo.get("csr");
        collectionServoRight = hwMap.crservo.get("csr");
        leadScrewLander   = hwMap.dcMotor.get("lsl");
        flapServo         = hwMap.crservo.get("fs");
        phoneServo        = hwMap.servo.get("ps");
        blockStopperServo = hwMap.servo.get("bss");
        markerServo       = hwMap.servo.get("ms");
        fingerServo       = hwMap.servo.get("fis");

        color = hwMap.colorSensor.get("clr");
        color2 = hwMap.colorSensor.get("clr2");
        potentiometer = hwMap.analogInput.get("pot");
        //liftButton = hwMap.touchSensor.get("lb");



        //sets motor directions(one side reversed so our robot dosnt just spin)
        leftMotorFront.setDirection(DcMotor.Direction.REVERSE); //we have to set two motors on the same side in revers so that the robot goes forward on both sides and dose not spin
        leftMotorBack.setDirection(DcMotor.Direction.REVERSE);
        rightMotorFront.setDirection(DcMotor.Direction.FORWARD);
        rightMotorBack.setDirection(DcMotor.Direction.FORWARD);
        leftliftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        leadScrew.setDirection(DcMotor.Direction.REVERSE);

        //sets motor vals to 0.0 so robot does not move in init
        leftMotorFront.setPower(0.0);
        leftMotorBack.setPower(0.0);
        rightMotorFront.setPower(0.0);
        rightMotorBack.setPower(0.0);
//        leftliftMotor.setPower(0.0);
//        rightLiftMotor.setPower(0.0);
        //collectionMotor.setPower(0.0);
        leadScrew.setPower(0.0);
        leadScrewLander.setPower(0.0);

        //set default mode to no encoders.
        leftMotorBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotorFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotorBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotorFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftliftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //collectionMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leadScrew.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leadScrewLander.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leadScrewLander.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){}


        rightLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftliftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        leadScrewLander.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
    public void autoInit(HardwareMap hwMap){

    }
    public  void servoInit(HardwareMap hwMap){
        phoneServo.setPosition(phonesPosUp);
        collectionServoRight.setPower(.0);
        //collectionServoRight.setPosition(.24);
        flapServo.setPower(.0);
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
   public void servoInitTele (HardwareMap hwMap){
        collectionServoRight.setPower(0.0);
        flapServo.setPower(.0);
        blockStopperServo.setPosition(.77);
        fingerServo.setPosition(.73);
   }
  /* public void cameraInit(HardwareMap hwMap){
       // Set up detector
       detector = new GoldAlignDetector(); // Create detector
       detector.init(hwMap.appContext, CameraViewDisplay.getInstance(), 1, false); // Initialize it with the app context and camera
       detector.useDefaults(); // Set detector to use default settings

       // Optional tuning
       detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
       detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
       detector.downscale = 0.4; // How much to downscale the input frames

       detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
       //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
       detector.maxAreaScorer.weight = 0.005; //

       detector.ratioScorer.weight = 5; //
       detector.ratioScorer.perfectRatio = 1.0; // Ratio adjustment

       detector.enable(); // Start the detector!
   }*/
}
