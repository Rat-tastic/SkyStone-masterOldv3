package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaSkyStone;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

@Autonomous(name="Mechanum Auto ", group="2019")
public class MechanumAutoTest extends MechanumTestSetup {
    public static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    public static final boolean PHONE_IS_PORTRAIT = true;

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AQ/M/I7/////AAABmcevgbHYf0gDrLkzmSUmljkE1I2luJNw25Tm4u/RavRG11K3hnzt97fv5BxNQxAXntQA2XtVrgue1Er8mdGkuSxosGX/JzLFO4vZdOBUqXbijZ+elcbAi2Ap/0ZgysJ6spVaxeVw247cJOQcfIB7YiHRv6VmwINoX2b1nFo9jQRKkNK+Ufn4lDi9s6pq4B8V7AJKgVQH49Ne2MifgbZEseEnBdoEKbQHJGMKa/Jyx2E7BS8Zqx745Yypi0Z6MA4QFkjKWTz55gU6gLBnupeDMlRiQRd+wRUckImOVBMAzEcs55WrE++gZTZek9zkKjqDZvbSkWIicKlZ0yGyTtFNp9H6HdZ3U2cooEZh+X89Jt1C ";

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    public static final float mmPerInch = 25.4f;
    public static final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target image above the floor

    // Constant for Stone Target
    public static final float stoneZ = 2.00f * mmPerInch;

    // Constants for the center support targets
    public static final float bridgeZ = 6.42f * mmPerInch;
    public static final float bridgeY = 23 * mmPerInch;
    public static final float bridgeX = 5.18f * mmPerInch;
    public static final float bridgeRotY = 59;                                 // Units are degrees
    public static final float bridgeRotZ = 180;

    // Constants for perimeter targets
    public static final float halfField = 72 * mmPerInch;
    public static final float quadField = 36 * mmPerInch;

    // Class Members
    public OpenGLMatrix lastLocation = null;
    public VuforiaLocalizer vuforia = null;
    public boolean targetVisible = false;
    public float phoneXRotate = 0;
    public float phoneYRotate = 0;
    public float phoneZRotate = 0;


    @Override
    public void runOpMode() {

        telemetry.log().add("Select Options");
        telemetry.update();

        selectOptions();

        fourwheel.hardwareInit(hardwareMap);
        fourwheel.imuInit(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);

        stoneTarget.setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //Set the position of the bridge support targets with relation to origin (center of field)
        blueFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

        blueRearBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

        redFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

        redRearBridge.setLocation(OpenGLMatrix
                .translation(bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

        //Set the position of the perimeter targets with relation to origin (center of field)
        red1.setLocation(OpenGLMatrix
                .translation(quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        red2.setLocation(OpenGLMatrix
                .translation(-quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        front1.setLocation(OpenGLMatrix
                .translation(-halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        front2.setLocation(OpenGLMatrix
                .translation(-halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        blue1.setLocation(OpenGLMatrix
                .translation(-quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        blue2.setLocation(OpenGLMatrix
                .translation(quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        rear1.setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        rear2.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //
        // Create a transformation matrix describing where the phone is on the robot.
        //
        // NOTE !!!!  It's very important that you turn OFF your phone's Auto-Screen-Rotation option.
        // Lock it into Portrait for these numbers to work.
        //
        // Info:  The coordinate frame for the robot looks the same as the field.
        // The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
        // Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
        //
        // The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
        // pointing to the LEFT side of the Robot.
        // The two examples below assume that the camera is facing forward out the front of the robot.

        // We need to rotate the camera around it's long axis to bring the correct camera forward.
        if (CAMERA_CHOICE == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }

        // Rotate the phone vertical about the X axis if it's in portrait mode
        if (PHONE_IS_PORTRAIT) {
            phoneXRotate = 90;
        }

        // Next, translate the camera lens to where it is on the robot.
        // In this example, it is centered (left to right), but forward of the middle of the robot, and above ground level.
        final float CAMERA_FORWARD_DISPLACEMENT = 4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot center
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
        final float CAMERA_LEFT_DISPLACEMENT = 0;     // eg: Camera is ON the robot's center line

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }
        int blockPos = 1;




        waitForStart();
        while (!isStarted() && !isStopRequested()){
            telemetry.addData("Status", "Waiting in init");
            telemetry.update();
        }



        if (allianceColor.getValue().equals("blue")){
            if (startPos.getValue().equals("block line")){
                if (skyBlock.getValue() >= 1)
                driveEnc(.7, 21, driveDirection.Forward);
                sleep(1000);
                gyroTurn(65, .6, turnDirections.LEFT);
                driveEnc(.5, 40, driveDirection.Backward);
                fourwheel.blockArm2.setPosition(.45);
                fourwheel.grabberSecR.setPosition(1);
                sleep(500);
                double frontLeftBottomRightAdj;
                double bottomLeftFrontRightAdj;

                double target = 980;
                double iSpeed = .5;

                boolean flag = false;
                float start = 0;

                frontLeftBottomRightAdj = -1.0;
                bottomLeftFrontRightAdj = 1.0;
                targetsSkyStone.activate();
                double dist;
                double target1= 0;
                targetVisible = false;

                //adjustToTarget(blue1, target, iSpeed);
               while (opModeIsActive() && flag == false) {
                    for (VuforiaTrackable trackable : allTrackables) {
                        if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                            telemetry.addData("Visible Target", trackable.getName());
                            targetVisible = true;

                            // getUpdatedRobotLocation() will return null if no new information is available since
                            // the last time that call was made, or if the trackable is not currently visible.
                            OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                            if (robotLocationTransform != null) {
                                lastLocation = robotLocationTransform;
                            }
                            VectorF translation = lastLocation.getTranslation();
                            telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                            if (lastLocation.getTranslation().get(1)  > target + 10) {
                                fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                            } else if (lastLocation.getTranslation().get(1) < target - 10) {
                                fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                            }else {
                                fourwheel.rightBackDrive.setPower(0.0);
                                fourwheel.rightFrontDrive.setPower(0.0);
                                fourwheel.leftBackDrive.setPower(0.0);
                                fourwheel.leftFrontDrive.setPower(0.0);
                                flag = true;
                                break;
                            }



                        }
                    }





                }



                // Disable Tracking when we are done;


                //gyroTurn(2, .4, turnDirections.LEFT);
                sleep(500);
                if(fourwheel.color2.alpha() > 25) {
                    driveEnc(.3, 10, driveDirection.Forward);
                    sleep(500);
                    blockPos = 2;

                    if(fourwheel.color2.alpha() > 25){
                        driveEnc(.5, 10, driveDirection.Forward);
                        strafeEnc(.5, 5, strafeDirections.Right);
                        blockPos = 3;


                        fourwheel.grabberSecR.setPosition(.5);
                        sleep(750);
                        fourwheel.blockArm2.setPosition(.5);

                        sleep(250);
                        fourwheel.blockArm2.setPosition(0);
                        strafeEnc(.5, 10, strafeDirections.Left);
                        //driveEnc(.3, 15, driveDirection.Backward);


                         /*target = 1120;
                         iSpeed = .5;

                         flag = false;


                        frontLeftBottomRightAdj = -1.0;
                        bottomLeftFrontRightAdj = 1.0;
                        targetsSkyStone.activate();

                        targetVisible = false;
                        while (opModeIsActive() && flag == false) {
                            for (VuforiaTrackable trackable : allTrackables) {
                                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                    telemetry.addData("Visible Target", trackable.getName());
                                    targetVisible = true;

                                    // getUpdatedRobotLocation() will return null if no new information is available since
                                    // the last time that call was made, or if the trackable is not currently visible.
                                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                    if (robotLocationTransform != null) {
                                        lastLocation = robotLocationTransform;
                                    }
                                    VectorF translation = lastLocation.getTranslation();
                                    telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                    if (lastLocation.getTranslation().get(1)  < target - 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                        fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    } else if (lastLocation.getTranslation().get(1) > target + 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                        fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    }else {
                                        fourwheel.rightBackDrive.setPower(0.0);
                                        fourwheel.rightFrontDrive.setPower(0.0);
                                        fourwheel.leftBackDrive.setPower(0.0);
                                        fourwheel.leftFrontDrive.setPower(0.0);
                                        flag = true;
                                        break;
                                    }



                                }
                            }





                        }



                        // Disable Tracking when we are done;*/

                        //gyroTurn(2, .4, turnDirections.LEFT);
                        driveEnc(1, 80, driveDirection.Forward);
                        driveEnc(1, 30, driveDirection.Forward);
                        driveEnc(1, 15, driveDirection.Forward);
                        driveEnc(.7, 15, driveDirection.Forward);
                        sleep(500);

                        strafeEnc(.6, 15 , strafeDirections.Right);
                        sleep(250);
                        fourwheel.blockArm2.setPosition(.5);
                        fourwheel.grabberSecR.setPosition(1);
                        sleep(500);
                        fourwheel.grabberSecR.setPosition(.5);
                        fourwheel.blockArm2.setPosition(0);
                        strafeEnc(.6, 12 , strafeDirections.Left);
                        if (skyBlock.getValue() == 2 && platformGrab.getValue().equals("no")){
                            gyroTurn(1, .4, turnDirections.LEFT);
                            driveEnc(1, 65, driveDirection.Backward);
                            driveEnc(1, 50, driveDirection.Backward);
                            driveEnc(1, 50, driveDirection.Backward);
                            driveEnc(1, 44, driveDirection.Backward);
                            //driveEnc(.5, 10, driveDirection.Forward);
                            driveEnc(.7, 40, driveDirection.Backward);

                            fourwheel.blockArm2.setPosition(.5);
                            fourwheel.grabberSecR.setPosition(1);
                            strafeEnc(.5, 15, strafeDirections.Right);
                            fourwheel.grabberSecR.setPosition(.5);
                            sleep(750);
                            //fourwheel.blockArm2.setPosition(.5);

                            //sleep(250);
                            fourwheel.blockArm2.setPosition(0);
                            strafeEnc(.5, 10, strafeDirections.Left);
                            gyroTurn(-2, .4, turnDirections.RIGHT);

                            driveEnc(1, 40, driveDirection.Forward);
                            //gyroTurn(2, .4, turnDirections.LEFT);

                           /* target = 1120;
                            iSpeed = .5;
                            flag = false;

                            frontLeftBottomRightAdj = -1.0;
                            bottomLeftFrontRightAdj = 1.0;
                            targetsSkyStone.activate();
                            targetVisible = false;
                            while (opModeIsActive() && flag == false) {
                                for (VuforiaTrackable trackable : allTrackables) {
                                    if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                        telemetry.addData("Visible Target", trackable.getName());
                                        targetVisible = true;

                                        // getUpdatedRobotLocation() will return null if no new information is available since
                                        // the last time that call was made, or if the trackable is not currently visible.
                                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                        if (robotLocationTransform != null) {
                                            lastLocation = robotLocationTransform;
                                        }
                                        VectorF translation = lastLocation.getTranslation();
                                        telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                        if (lastLocation.getTranslation().get(1)  < target - 10) {
                                            fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                            fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                            fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                            fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        } else if (lastLocation.getTranslation().get(1) > target + 10) {
                                            fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                            fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                            fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                            fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        }else {
                                            fourwheel.rightBackDrive.setPower(0.0);
                                            fourwheel.rightFrontDrive.setPower(0.0);
                                            fourwheel.leftBackDrive.setPower(0.0);
                                            fourwheel.leftFrontDrive.setPower(0.0);
                                            flag = true;
                                            break;
                                        }



                                    }
                                }





                            }



                            // Disable Tracking when we are done;
                            targetsSkyStone.deactivate();*/
                            driveEnc(1, 50, driveDirection.Forward);
                            driveEnc(1, 50, driveDirection.Forward);
                            driveEnc(.7, 56, driveDirection.Forward);
                            sleep(500);
                            strafeEnc(.5, 15, strafeDirections.Right);
                            //driveEnc(.5, 5, driveDirection.Backward);
                            //driveEnc(.5, 75, driveDirection.Backward);
                            fourwheel.blockArm2.setPosition(.5);
                            fourwheel.grabberSecR.setPosition(1);
                            sleep(500);
                            fourwheel.grabberSecR.setPosition(.5);
                            fourwheel.blockArm2.setPosition(0);
                            strafeEnc(.9, 12, strafeDirections.Left);
                            driveEnc(.9, 6 , driveDirection.Backward);

                        }
                        else if (skyBlock.getValue() == 1 && platformGrab.getValue().equals("yes")){
                            gyroTurn(-170, .5, turnDirections.RIGHT);
                            driveEnc(.3, 70, driveDirection.Forward);
                            strafeEnc(.5, 10, strafeDirections.Right);
                            //fourwheel.platformHolder.setPosition(.17);//.47 is up
                            sleep(500);
                            strafeEnc(1, 50, strafeDirections.Left);
                            //fourwheel.platformHolder.setPosition(.47);//.47 is up
                            sleep(500);
                            driveEnc(1, 40, driveDirection.Backward);
                        }
                    }//pos2 start
                    else {
                        strafeEnc(1, 3, strafeDirections.Right);
                        fourwheel.grabberSecR.setPosition(.5);
                        sleep(750);
                        fourwheel.blockArm2.setPosition(.5);

                        sleep(250);
                        fourwheel.blockArm2.setPosition(0);

                        strafeEnc(.6, 9, strafeDirections.Left);

                        /*target = 1120;
                        iSpeed = .5;

                        flag = false;
                        start = 0;

                        frontLeftBottomRightAdj = -1.0;
                        bottomLeftFrontRightAdj = 1.0;
                        targetsSkyStone.activate();

                        targetVisible = false;
                        while (opModeIsActive() && flag == false) {
                            for (VuforiaTrackable trackable : allTrackables) {
                                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                    telemetry.addData("Visible Target", trackable.getName());
                                    targetVisible = true;

                                    // getUpdatedRobotLocation() will return null if no new information is available since
                                    // the last time that call was made, or if the trackable is not currently visible.
                                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                    if (robotLocationTransform != null) {
                                        lastLocation = robotLocationTransform;
                                    }
                                    VectorF translation = lastLocation.getTranslation();
                                    telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                    if (lastLocation.getTranslation().get(1)  < target - 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                        fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    } else if (lastLocation.getTranslation().get(1) > target + 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                        fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    }else {
                                        fourwheel.rightBackDrive.setPower(0.0);
                                        fourwheel.rightFrontDrive.setPower(0.0);
                                        fourwheel.leftBackDrive.setPower(0.0);
                                        fourwheel.leftFrontDrive.setPower(0.0);
                                        flag = true;
                                        break;
                                    }



                                }
                            }





                        }



                        // Disable Tracking when we are done;
                        targetsSkyStone.deactivate();*/
                        gyroTurn(-2, .4, turnDirections.RIGHT);
                        driveEnc(1, 80, driveDirection.Forward);
                        driveEnc(1, 50, driveDirection.Forward);
                       // driveEnc(1, 20 , driveDirection.Forward);
                        sleep(500);
                        strafeEnc(.6, 12 , strafeDirections.Right);
                        sleep(250);
                        fourwheel.blockArm2.setPosition(.5);
                        fourwheel.grabberSecR.setPosition(1);
                        sleep(500);
                        fourwheel.grabberSecR.setPosition(.5);
                        fourwheel.blockArm2.setPosition(0);
                        strafeEnc(.6, 9 , strafeDirections.Left);
                        gyroTurn(2, .6, turnDirections.LEFT);

                        if (skyBlock.getValue() == 2 || platformGrab.getValue().equals("no")){
                            driveEnc(.5, 30, driveDirection.Backward);
                            driveEnc(1 ,80, driveDirection.Backward);

                            //driveEnc(.5, 20, driveDirection.Forward);
                            driveEnc(1, 32, driveDirection.Backward);
                            driveEnc(1, 32, driveDirection.Backward);
                            driveEnc(1, 42 , driveDirection.Backward);
                            driveEnc(.4, 12 , driveDirection.Backward);
                            fourwheel.blockArm2.setPosition(.5);
                            fourwheel.grabberSecR.setPosition(1);
                            strafeEnc(.5, 15, strafeDirections.Right);
                            fourwheel.grabberSecR.setPosition(.5);
                            sleep(750);
                            //fourwheel.blockArm2.setPosition(.5);

                            //sleep(250);
                            fourwheel.blockArm2.setPosition(0);
                            strafeEnc(.5, 10, strafeDirections.Left);
                            gyroTurn(-3, .4, turnDirections.RIGHT);
                            driveEnc(1, 40, driveDirection.Forward);
                            driveEnc(1, 10, driveDirection.Forward);
                            driveEnc(1, 10, driveDirection.Forward);

                            //gyroTurn(1, .4, turnDirections.LEFT);
                            /*target = 1120;
                            iSpeed = .5;
                            flag = false;

                            frontLeftBottomRightAdj = -1.0;
                            bottomLeftFrontRightAdj = 1.0;
                            targetsSkyStone.activate();
                            targetVisible = false;
                            while (opModeIsActive() && flag == false) {
                                for (VuforiaTrackable trackable : allTrackables) {
                                    if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                        telemetry.addData("Visible Target", trackable.getName());
                                        targetVisible = true;

                                        // getUpdatedRobotLocation() will return null if no new information is available since
                                        // the last time that call was made, or if the trackable is not currently visible.
                                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                        if (robotLocationTransform != null) {
                                            lastLocation = robotLocationTransform;
                                        }
                                        VectorF translation = lastLocation.getTranslation();
                                        telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                        if (lastLocation.getTranslation().get(1)  < target - 10) {
                                            fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                            fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                            fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                            fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        } else if (lastLocation.getTranslation().get(1) > target + 10) {
                                            fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                            fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                            fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                            fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        }else {
                                            fourwheel.rightBackDrive.setPower(0.0);
                                            fourwheel.rightFrontDrive.setPower(0.0);
                                            fourwheel.leftBackDrive.setPower(0.0);
                                            fourwheel.leftFrontDrive.setPower(0.0);
                                            flag = true;
                                            break;
                                        }



                                    }
                                }





                            }



                            // Disable Tracking when we are done;
                            targetsSkyStone.deactivate();*/
                            //gyroTurn(2, .4, turnDirections.LEFT);
                            //sleep(250);
                            driveEnc(1, 50, driveDirection.Forward);
                            driveEnc(1, 30, driveDirection.Forward);
                            driveEnc(1, 30, driveDirection.Forward);
                            driveEnc(.5, 20, driveDirection.Forward);
                            sleep(500);
                            strafeEnc(.5, 15, strafeDirections.Right);
                            //driveEnc(.5, 5, driveDirection.Backward);
                            //driveEnc(.5, 75, driveDirection.Backward);
                            fourwheel.blockArm2.setPosition(.5);
                            fourwheel.grabberSecR.setPosition(1);
                            sleep(500);
                            fourwheel.grabberSecR.setPosition(.5);
                            fourwheel.blockArm2.setPosition(0);

                            //strafeEnc(.9, 12, strafeDirections.Left);
                            driveEnc(.9, 60, driveDirection.Backward);



                        }
                       else if (platformGrab.getValue().equals("yes") && skyBlock.getValue() == 1) {
                            gyroTurn(-170, .5, turnDirections.RIGHT);
                            driveEnc(.3, 75, driveDirection.Forward);
                            strafeEnc(.5, 13, strafeDirections.Right);
                            // fourwheel.platformHolder.setPosition(.17);//.47 is up
                            sleep(500);
                            strafeEnc(1, 50, strafeDirections.Left);
                            //fourwheel.platformHolder.setPosition(.47);//.47 is up
                            sleep(500);
                            driveEnc(1, 40, driveDirection.Backward);
                        }
                        else {
                            driveEnc(.5, 15, driveDirection.Forward);
                        }
                    }


                }//pos1start

                else {
                    strafeEnc(.5, 5, strafeDirections.Right);
                    fourwheel.grabberSecR.setPosition(.5);
                    sleep(750);
                    fourwheel.blockArm2.setPosition(.5);

                    sleep(250);
                    fourwheel.blockArm2.setPosition(0);
                    strafeEnc(.5, 13, strafeDirections.Left);
                    //driveEnc(.4, 5, driveDirection.Forward);


                    /*target = 1120;
                    iSpeed = .5;

                    flag = false;
                    start = 0;

                    frontLeftBottomRightAdj = -1.0;
                    bottomLeftFrontRightAdj = 1.0;
                    targetsSkyStone.activate();

                    targetVisible = false;
                    targetsSkyStone.activate();
                    targetVisible = false;
                    while (opModeIsActive() && flag == false) {
                        for (VuforiaTrackable trackable : allTrackables) {
                            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                telemetry.addData("Visible Target", trackable.getName());
                                targetVisible = true;

                                // getUpdatedRobotLocation() will return null if no new information is available since
                                // the last time that call was made, or if the trackable is not currently visible.
                                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                if (robotLocationTransform != null) {
                                    lastLocation = robotLocationTransform;
                                }
                                VectorF translation = lastLocation.getTranslation();
                                telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                if (lastLocation.getTranslation().get(1)  < target - 10) {
                                    fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                    fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                } else if (lastLocation.getTranslation().get(1) > target + 10) {
                                    fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                    fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                }else {
                                    fourwheel.rightBackDrive.setPower(0.0);
                                    fourwheel.rightFrontDrive.setPower(0.0);
                                    fourwheel.leftBackDrive.setPower(0.0);
                                    fourwheel.leftFrontDrive.setPower(0.0);
                                    flag = true;
                                    break;
                                }



                            }
                        }





                    }




                    // Disable Tracking when we are done;*/
                    gyroTurn(-1, .4, turnDirections.RIGHT);

                    driveEnc(1, 80, driveDirection.Forward);
                    driveEnc(1, 40, driveDirection.Forward);
                    driveEnc(1, 40, driveDirection.Forward);
                    //driveEnc(1, 10, driveDirection.Forward);
                    sleep(500);
                    strafeEnc(.8, 12, strafeDirections.Right);
                    sleep(250);
                    fourwheel.blockArm2.setPosition(.5);
                    fourwheel.grabberSecR.setPosition(1);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.5);
                    fourwheel.blockArm2.setPosition(0);
                    strafeEnc(.8, 12, strafeDirections.Left);

                    if (skyBlock.getValue() == 2 && platformGrab.getValue().equals("no")){
                        gyroTurn(2, .5, turnDirections.LEFT);
                        driveEnc(1, 75, driveDirection.Backward);
                        driveEnc(1, 60, driveDirection.Backward);
                        driveEnc(1, 70, driveDirection.Backward);
                        driveEnc(.6, 90, driveDirection.Backward);
                        driveEnc(.3,5,driveDirection.Backward);
                        fourwheel.blockArm2.setPosition(.5);
                        fourwheel.grabberSecR.setPosition(1);
                        sleep(250);

                        strafeEnc(.7, 15, strafeDirections.Right);
                        fourwheel.grabberSecR.setPosition(.5);
                        //strafeEnc(.9, 14, strafeDirections.Left);
                        sleep(750);
                        fourwheel.blockArm2.setPosition(0);
                        strafeEnc(.7, 9, strafeDirections.Left);
                        //gyroTurn(2, .5, turnDirections.RIGHT);
                        driveEnc(.9, 80, driveDirection.Forward);
                        driveEnc(.9, 50, driveDirection.Forward);
                        driveEnc(.9, 60, driveDirection.Forward);
                        driveEnc(.9, 50, driveDirection.Forward);
                        sleep(500);
                        strafeEnc(.6, 15, strafeDirections.Right);
                        fourwheel.blockArm2.setPosition(.5);
                        fourwheel.grabberSecR.setPosition(1);
                        sleep(500);
                        fourwheel.blockArm2.setPosition(0);
                        fourwheel.grabberSecR.setPosition(.5);
                        strafeEnc(.6, 12, strafeDirections.Left);
                        driveEnc(.9, 60, driveDirection.Backward);
                        /*gyroTurn(90, .8, turnDirections.LEFT);
                        driveEnc(.5, 10, driveDirection.Backward);*/
                        //driveEnc(.5, 9, driveDirection.Forward);


                        //driveEnc(.5, 25, driveDirection.Backward);
                        //sleep(750);
                       /*target = 1120;
                       iSpeed = .5;

                       flag = false;
                       start = 0;

                        frontLeftBottomRightAdj = -1.0;
                        bottomLeftFrontRightAdj = 1.0;

                        targetVisible = false;
                        while (opModeIsActive() && flag == false) {
                            for (VuforiaTrackable trackable : allTrackables) {
                                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                    telemetry.addData("Visible Target", trackable.getName());
                                    targetVisible = true;

                                    // getUpdatedRobotLocation() will return null if no new information is available since
                                    // the last time that call was made, or if the trackable is not currently visible.
                                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                    if (robotLocationTransform != null) {
                                        lastLocation = robotLocationTransform;
                                    }
                                    VectorF translation = lastLocation.getTranslation();
                                    telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                    if (lastLocation.getTranslation().get(1)  < target - 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                        fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    } else if (lastLocation.getTranslation().get(1) > target + 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                        fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    }else {
                                        fourwheel.rightBackDrive.setPower(0.0);
                                        fourwheel.rightFrontDrive.setPower(0.0);
                                        fourwheel.leftBackDrive.setPower(0.0);
                                        fourwheel.leftFrontDrive.setPower(0.0);
                                        flag = true;
                                        break;
                                    }



                                }
                            }





                        }



                        // Disable Tracking when we are done;
                        targetsSkyStone.deactivate();*/









                    }
                    else if (platformGrab.getValue().equals("yes") && skyBlock.getValue() == 1) {
                        gyroTurn(-100, .5, turnDirections.RIGHT);
                        driveEnc(.5, 20, driveDirection.Backward);
                        driveEnc(.3, 70, driveDirection.Backward);
                        //fourwheel.platformHolder.setPosition(.17);//.47 is up
                        fourwheel.platformHolderR.setPosition(.57);
                        fourwheel.platformHolderL.setPosition(.059);
                        sleep(500);
                        driveEnc(.5, 40, driveDirection.Forward);
                        //  fourwheel.platformHolder.setPosition(.47);//.47 is up
                        fourwheel.platformHolderR.setPosition(0);
                        fourwheel.platformHolderL.setPosition(1);
                        sleep(500);
                        strafeEnc(.6, 40, strafeDirections.Left);
                    }
                }


            }
            else {
                if (waitStart.getValue() > 2){
                    driveEnc(.9, 20, driveDirection.Forward);
                    strafeEnc(.9, 15, strafeDirections.Right);
                }
                else {
                    strafeEnc(.9, 15, strafeDirections.Right);
                }

            }
        }
        else {
            if (startPos.getValue().equals("block line")) {
                if (skyBlock.getValue() >= 1)
                driveEnc(.5, 21, driveDirection.Forward);
                sleep(1000);
                gyroTurn(-75, .4, turnDirections.RIGHT);
                driveEnc(.5, 39, driveDirection.Backward);
                fourwheel.blockArm.setPosition(.64);
                fourwheel.grabberSecL.setPosition(0);
                sleep(500);
                double frontLeftBottomRightAdj;
                double bottomLeftFrontRightAdj;

                double target = -980;
                double iSpeed = .5;

                boolean flag = false;
                float start = 0;

                frontLeftBottomRightAdj = -1.0;
                bottomLeftFrontRightAdj = 1.0;
                targetsSkyStone.activate();
                double dist;
                double target1= 0;
                targetVisible = false;
                while (opModeIsActive() && flag == false) {
                    for (VuforiaTrackable trackable : allTrackables) {
                        if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                            telemetry.addData("Visible Target", trackable.getName());
                            targetVisible = true;

                            // getUpdatedRobotLocation() will return null if no new information is available since
                            // the last time that call was made, or if the trackable is not currently visible.
                            OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                            if (robotLocationTransform != null) {
                                lastLocation = robotLocationTransform;
                            }
                            VectorF translation = lastLocation.getTranslation();
                            telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                            if (lastLocation.getTranslation().get(1)  > target + 10) {
                                fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                            } else if (lastLocation.getTranslation().get(1) < target - 10) {
                                fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                            }else {
                                fourwheel.rightBackDrive.setPower(0.0);
                                fourwheel.rightFrontDrive.setPower(0.0);
                                fourwheel.leftBackDrive.setPower(0.0);
                                fourwheel.leftFrontDrive.setPower(0.0);
                                flag = true;
                                break;
                            }



                        }
                    }





                }



                // Disable Tracking when we are done;
                targetsSkyStone.deactivate();
                //gyroTurn(2, .4, turnDirections.LEFT);
                sleep(500);
                if (fourwheel.color.alpha() > 25) {
                    gyroTurn(3, .3, turnDirections.LEFT);
                    driveEnc(.3, 14, driveDirection.Forward);
                    sleep(500);
                    blockPos = 2;

                    if (fourwheel.color.alpha() > 25) {
                        driveEnc(.3, 16, driveDirection.Forward);
                        blockPos = 3;

                        strafeEnc(.5, 4, strafeDirections.Left);


                        fourwheel.grabberSecL.setPosition(.5);
                        sleep(750);
                        fourwheel.blockArm.setPosition(1);
                        sleep(250);

                        strafeEnc(.6, 10, strafeDirections.Right);
                        /*targetsSkyStone.activate();
                        target = -1120;
                        iSpeed = .5;

                        flag = false;
                        start = 0;

                        frontLeftBottomRightAdj = -1.0;
                        bottomLeftFrontRightAdj = 1.0;

                        targetVisible = false;
                        while (opModeIsActive() && flag == false) {
                            for (VuforiaTrackable trackable : allTrackables) {
                                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                    telemetry.addData("Visible Target", trackable.getName());
                                    targetVisible = true;

                                    // getUpdatedRobotLocation() will return null if no new information is available since
                                    // the last time that call was made, or if the trackable is not currently visible.
                                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                    if (robotLocationTransform != null) {
                                        lastLocation = robotLocationTransform;
                                    }
                                    VectorF translation = lastLocation.getTranslation();
                                    telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                    if (lastLocation.getTranslation().get(1)  > target + 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                        fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    } else if (lastLocation.getTranslation().get(1) < target - 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                        fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    }else {
                                        fourwheel.rightBackDrive.setPower(0.0);
                                        fourwheel.rightFrontDrive.setPower(0.0);
                                        fourwheel.leftBackDrive.setPower(0.0);
                                        fourwheel.leftFrontDrive.setPower(0.0);
                                        flag = true;
                                        break;
                                    }



                                }
                            }





                        }



                        // Disable Tracking when we are done;
                        targetsSkyStone.deactivate();*/
                        //gyroTurn(-2, .3, turnDirections.RIGHT);
                        driveEnc(1, 80, driveDirection.Forward);
                        driveEnc(1, 10, driveDirection.Forward);
                        //driveEnc(1, 20, driveDirection.Forward);
                        sleep(750);
                        strafeEnc(.6, 15, strafeDirections.Left);
                        fourwheel.blockArm.setPosition(.6);
                        fourwheel.grabberSecL.setPosition(0);

                        sleep(500);
                        fourwheel.blockArm.setPosition(1);
                        fourwheel.grabberSecL.setPosition(.5);
                        strafeEnc(.9, 12, strafeDirections.Right);
                        gyroTurn(2, .4, turnDirections.LEFT);
                        //spinEnc(.4, 3, turnDirection.Right);

                        if (skyBlock.getValue() == 2 && platformGrab.getValue().equals("no")) {
                            driveEnc(1, 80, driveDirection.Backward);
                            driveEnc(1, 24, driveDirection.Backward);
                            driveEnc(1, 35, driveDirection.Backward);
                            driveEnc(1, 36, driveDirection.Backward);
                            driveEnc(.5, 26, driveDirection.Backward);
                            //strafeEnc(.5, 16, strafeDirections.Left);
                            fourwheel.blockArm.setPosition(.6);
                            fourwheel.grabberSecL.setPosition(0);
                            strafeEnc(.6, 18, strafeDirections.Left);
                            fourwheel.blockArm.setPosition(.5);
                            fourwheel.grabberSecL.setPosition(.57);

                            sleep(500);
                            fourwheel.blockArm.setPosition(1);
                            strafeEnc(.6, 11, strafeDirections.Right);
                            /*target = -1120;
                            iSpeed = .5;

                            flag = false;
                            start = 0;

                            frontLeftBottomRightAdj = -1.0;
                            bottomLeftFrontRightAdj = 1.0;
                            targetsSkyStone.activate();

                            targetVisible = false;
                            while (opModeIsActive() && flag == false) {
                                for (VuforiaTrackable trackable : allTrackables) {
                                    if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                        telemetry.addData("Visible Target", trackable.getName());
                                        targetVisible = true;

                                        // getUpdatedRobotLocation() will return null if no new information is available since
                                        // the last time that call was made, or if the trackable is not currently visible.
                                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                        if (robotLocationTransform != null) {
                                            lastLocation = robotLocationTransform;
                                        }
                                        VectorF translation = lastLocation.getTranslation();
                                        telemetry.addData("Pos (in)", translation.get(1) / mmPerInch);


                                        if (lastLocation.getTranslation().get(1) > target + 10) {
                                            fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                            fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                            fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                            fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        } else if (lastLocation.getTranslation().get(1) < target - 10) {
                                            fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                            fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                            fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                            fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        } else {
                                            fourwheel.rightBackDrive.setPower(0.0);
                                            fourwheel.rightFrontDrive.setPower(0.0);
                                            fourwheel.leftBackDrive.setPower(0.0);
                                            fourwheel.leftFrontDrive.setPower(0.0);
                                            flag = true;
                                            break;
                                        }


                                    }
                                }


                            }


                            // Disable Tracking when we are done;
                            targetsSkyStone.deactivate();*/
                            //gyroTurn(2, .3, turnDirections.LEFT);
                            driveEnc(1, 95, driveDirection.Forward);
                            driveEnc(.8, 30, driveDirection.Forward);
                            driveEnc(.5, 80 ,driveDirection.Forward);
                            //driveEnc(.5, 20, driveDirection.Forward);
                            //driveEnc(.5, 56, driveDirection.Forward);
                            sleep(750);
                            strafeEnc(.8, 14, strafeDirections.Left);
                            fourwheel.blockArm.setPosition(.5);
                            fourwheel.grabberSecL.setPosition(0);

                            sleep(250);


                            fourwheel.grabberSecL.setPosition(.5);

                            fourwheel.blockArm.setPosition(1);
                            sleep(750);
                            /*gyroTurn(-60, .8 ,turnDirections.RIGHT);
                            //strafeEnc(.9, , strafeDirections.Left);
                            driveEnc(.6, 10, driveDirection.Backward);
                            fourwheel.platformHolderL.setPosition(.39);
                            fourwheel.platformHolderR.setPosition(.54);
                            sleep(750);
                            driveEnc(.4, 10, driveDirection.Forward);
                            driveEnc(1, 90, driveDirection.Forward);
                            gyroTurn(-80, .8, turnDirections.RIGHT);
                            fourwheel.platformHolderL.setPosition(1);
                            fourwheel.platformHolderR.setPosition(0);
                            driveEnc(.8, 50, driveDirection.Forward);*/
                            strafeEnc(.9, 12, strafeDirections.Right);
                            driveEnc(.9, 40, driveDirection.Backward);
                        }
                        else if (skyBlock.getValue() == 1 && platformGrab.getValue().equals("yes")){
                            gyroTurn(170, .5, turnDirections.LEFT);
                            driveEnc(.3, 75, driveDirection.Backward);
                            strafeEnc(.5, 13, strafeDirections.Right);
                            //fourwheel.platformHolder.setPosition(.17);//.47 is up
                            sleep(500);
                            strafeEnc(1, 40, strafeDirections.Left);
                            //fourwheel.platformHolder.setPosition(.47);//.47 is up
                            sleep(500);
                            driveEnc(1, 40, driveDirection.Forward);
                        }

                    } else {        //pos2
                        strafeEnc(.5, 4, strafeDirections.Left);
                        fourwheel.blockArm.setPosition(.5);
                        fourwheel.grabberSecL.setPosition(.57);

                        sleep(500);
                        fourwheel.blockArm.setPosition(1);
                        strafeEnc(.7, 12, strafeDirections.Right);
                        gyroTurn(1, .5, turnDirections.LEFT);
                        /*sleep(500);
                        target = -1120;
                        iSpeed = .5;

                        flag = false;
                        start = 0;

                        frontLeftBottomRightAdj = -1.0;
                        bottomLeftFrontRightAdj = 1.0;
                        targetsSkyStone.activate();

                        targetVisible = false;
                        targetsSkyStone.activate();
                        targetVisible = false;
                        while (opModeIsActive() && flag == false) {
                            for (VuforiaTrackable trackable : allTrackables) {
                                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                    telemetry.addData("Visible Target", trackable.getName());
                                    targetVisible = true;

                                    // getUpdatedRobotLocation() will return null if no new information is available since
                                    // the last time that call was made, or if the trackable is not currently visible.
                                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                    if (robotLocationTransform != null) {
                                        lastLocation = robotLocationTransform;
                                    }
                                    VectorF translation = lastLocation.getTranslation();
                                    telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                    if (lastLocation.getTranslation().get(1)  > target + 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                        fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    } else if (lastLocation.getTranslation().get(1) < target - 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                        fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    }else {
                                        fourwheel.rightBackDrive.setPower(0.0);
                                        fourwheel.rightFrontDrive.setPower(0.0);
                                        fourwheel.leftBackDrive.setPower(0.0);
                                        fourwheel.leftFrontDrive.setPower(0.0);
                                        flag = true;
                                        break;
                                    }



                                }
                            }





                        }*/
                        //gyroTurn(-2, .3, turnDirections.RIGHT);
                        driveEnc(1, 80, driveDirection.Forward);
                        driveEnc(1, 30, driveDirection.Forward);

                        sleep(750);
                        strafeEnc(.9, 15, strafeDirections.Left);
                        fourwheel.blockArm.setPosition(.6);
                        fourwheel.grabberSecL.setPosition(0);

                        sleep(500);
                        fourwheel.blockArm.setPosition(1);
                        fourwheel.grabberSecL.setPosition(.5);
                        strafeEnc(.6, 14, strafeDirections.Right);
                        //gyroTurn(-2, .4, turnDirections.RIGHT);
                        spinEnc(.4, 3, turnDirection.Right);
                        if (skyBlock.getValue() == 2 && platformGrab.getValue().equals("no")) {
                            driveEnc(1, 80, driveDirection.Backward);
                            driveEnc(1, 60, driveDirection.Backward);
                            driveEnc(1, 43, driveDirection.Backward);
                            driveEnc(1, 20, driveDirection.Backward);
                            //driveEnc(.3, 36, driveDirection.Backward);
                            driveEnc(.3, 20, driveDirection.Backward);
                            fourwheel.blockArm.setPosition(.6);
                            fourwheel.grabberSecL.setPosition(0);
                            strafeEnc(.6, 14, strafeDirections.Left);
                            fourwheel.blockArm.setPosition(.5);
                            fourwheel.grabberSecL.setPosition(.57);

                            sleep(500);
                            fourwheel.blockArm.setPosition(1);
                            strafeEnc(.6, 12, strafeDirections.Right);
                            spinEnc(.4, 2, turnDirection.Left);
                            driveEnc(1, 30, driveDirection.Forward);
                           /* target = -1130;
                            iSpeed = .5;

                            flag = false;
                            start = 0;

                            frontLeftBottomRightAdj = -1.0;
                            bottomLeftFrontRightAdj = 1.0;

                            targetVisible = false;
                            while (opModeIsActive() && flag == false) {
                                for (VuforiaTrackable trackable : allTrackables) {
                                    if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                        telemetry.addData("Visible Target", trackable.getName());
                                        targetVisible = true;

                                        // getUpdatedRobotLocation() will return null if no new information is available since
                                        // the last time that call was made, or if the trackable is not currently visible.
                                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                        if (robotLocationTransform != null) {
                                            lastLocation = robotLocationTransform;
                                        }
                                        VectorF translation = lastLocation.getTranslation();
                                        telemetry.addData("Pos (in)", translation.get(1) / mmPerInch);


                                        if (lastLocation.getTranslation().get(1) > target + 10) {
                                            fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                            fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                            fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                            fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        } else if (lastLocation.getTranslation().get(1) < target - 10) {
                                            fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                            fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                            fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                            fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        } else {
                                            fourwheel.rightBackDrive.setPower(0.0);
                                            fourwheel.rightFrontDrive.setPower(0.0);
                                            fourwheel.leftBackDrive.setPower(0.0);
                                            fourwheel.leftFrontDrive.setPower(0.0);
                                            flag = true;
                                            break;
                                        }


                                    }
                                }


                            }


                            // Disable Tracking when we are done;
                            targetsSkyStone.deactivate();*/
                            driveEnc(1, 80, driveDirection.Forward);
                            driveEnc(1, 60, driveDirection.Forward);
                            sleep(750);
                            strafeEnc(.8, 17, strafeDirections.Left);
                            fourwheel.blockArm.setPosition(.5);
                            fourwheel.grabberSecL.setPosition(0);

                            sleep(250);


                            fourwheel.grabberSecL.setPosition(.5);

                            fourwheel.blockArm.setPosition(1);
                            sleep(750);
                            /*gyroTurn(-60, .8 ,turnDirections.RIGHT);
                            //strafeEnc(.9, , strafeDirections.Left);
                            driveEnc(.6, 10, driveDirection.Backward);
                            fourwheel.platformHolderL.setPosition(.39);
                            fourwheel.platformHolderR.setPosition(.54);
                            sleep(750);
                            driveEnc(1, 90, driveDirection.Forward);
                            gyroTurn(-60, .8, turnDirections.RIGHT);
                            fourwheel.platformHolderL.setPosition(1);
                            fourwheel.platformHolderR.setPosition(0);
                            driveEnc(.8, 50, driveDirection.Forward);
                            //strafeEnc(.9, 50, strafeDirections.Right);*/
                            strafeEnc(.9, 12, strafeDirections.Right);
                            driveEnc(.9, 40, driveDirection.Backward);

                        }
                        else if (skyBlock.getValue() == 1 && platformGrab.getValue().equals("yes")){
                            gyroTurn(170, .5, turnDirections.LEFT);
                            driveEnc(.3, 75, driveDirection.Backward);
                            strafeEnc(.5, 13, strafeDirections.Right);
                            //fourwheel.platformHolder.setPosition(.17);//.47 is up
                            sleep(500);
                            strafeEnc(1, 40, strafeDirections.Left);
                            //fourwheel.platformHolder.setPosition(.47);//.47 is up
                            sleep(500);
                            driveEnc(1, 40, driveDirection.Forward);
                        }


                    }


                }//pos3
                else {
                    strafeEnc(.5, 5, strafeDirections.Left);
                    fourwheel.blockArm.setPosition(.5);
                    fourwheel.grabberSecL.setPosition(.57);

                    sleep(500);
                    fourwheel.blockArm.setPosition(1);
                    strafeEnc(.7, 11, strafeDirections.Right);
                    //gyroTurn(1, .5, turnDirections.LEFT);
                   // driveEnc(.4, 5, driveDirection.Backward);


                    /*target = -1140;
                    iSpeed = .5;

                    flag = false;
                    start = 0;

                    frontLeftBottomRightAdj = -1.0;
                    bottomLeftFrontRightAdj = 1.0;
                    targetsSkyStone.activate();

                    targetVisible = false;
                    targetsSkyStone.activate();
                    targetVisible = false;
                    while (opModeIsActive() && flag == false) {
                        for (VuforiaTrackable trackable : allTrackables) {
                            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                telemetry.addData("Visible Target", trackable.getName());
                                targetVisible = true;

                                // getUpdatedRobotLocation() will return null if no new information is available since
                                // the last time that call was made, or if the trackable is not currently visible.
                                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                if (robotLocationTransform != null) {
                                    lastLocation = robotLocationTransform;
                                }
                                VectorF translation = lastLocation.getTranslation();
                                telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                if (lastLocation.getTranslation().get(1)  > target + 10) {
                                    fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                    fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                } else if (lastLocation.getTranslation().get(1) < target - 10) {
                                    fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                    fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                }else {
                                    fourwheel.rightBackDrive.setPower(0.0);
                                    fourwheel.rightFrontDrive.setPower(0.0);
                                    fourwheel.leftBackDrive.setPower(0.0);
                                    fourwheel.leftFrontDrive.setPower(0.0);
                                    flag = true;
                                    break;
                                }



                            }
                        }





                    }




                    // Disable Tracking when we are done;*/
                    //gyroTurn(-3, .4, turnDirections.RIGHT);

                    driveEnc(1, 80, driveDirection.Forward);
                    driveEnc(1, 40, driveDirection.Forward);
                    driveEnc(1, 20, driveDirection.Forward);
                    //driveEnc(.9, 30, driveDirection.Forward);
                    sleep(750);
                    strafeEnc(.6, 15, strafeDirections.Left);
                    fourwheel.blockArm.setPosition(.6);
                    fourwheel.grabberSecL.setPosition(0);

                    sleep(500);
                    fourwheel.blockArm.setPosition(1);
                    strafeEnc(.6, 11, strafeDirections.Right);
                    spinEnc(.4, 1, turnDirection.Right);
                    //gyroTurn(-1, .3, turnDirections.RIGHT);
                    if (skyBlock.getValue() == 2 && platformGrab.getValue().equals("no")){
                        driveEnc(.9, 80, driveDirection.Backward);
                        driveEnc(.9, 30, driveDirection.Backward);
                        driveEnc(.9, 25, driveDirection.Backward);
                        driveEnc(.9, 36, driveDirection.Backward);
                        driveEnc(.9, 45, driveDirection.Backward);
                        driveEnc(.7, 30, driveDirection.Backward);
                        driveEnc(.3, 10, driveDirection.Backward);
                        gyroTurn(6, .4, turnDirections.LEFT);

                        fourwheel.blockArm.setPosition(.5);
                        fourwheel.grabberSecL.setPosition(0);

                        sleep(250);
                        strafeEnc(.8, 12, strafeDirections.Left);

                        fourwheel.grabberSecL.setPosition(.5);
                        sleep(750);
                        fourwheel.blockArm.setPosition(1);





                        /*target = -1090;
                        iSpeed = .5;

                        flag = false;
                        start = 0;

                        frontLeftBottomRightAdj = -1.0;
                        bottomLeftFrontRightAdj = 1.0;

                        targetVisible = false;
                        while (opModeIsActive() && flag == false) {
                            for (VuforiaTrackable trackable : allTrackables) {
                                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                                    telemetry.addData("Visible Target", trackable.getName());
                                    targetVisible = true;

                                    // getUpdatedRobotLocation() will return null if no new information is available since
                                    // the last time that call was made, or if the trackable is not currently visible.
                                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                                    if (robotLocationTransform != null) {
                                        lastLocation = robotLocationTransform;
                                    }
                                    VectorF translation = lastLocation.getTranslation();
                                    telemetry.addData("Pos (in)", translation.get(1)/mmPerInch);





                                    if (lastLocation.getTranslation().get(1)  > target + 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                                        fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                    } else if (lastLocation.getTranslation().get(1) < target - 10) {
                                        fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                                        fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                                        fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                        fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                                    }else {
                                        fourwheel.rightBackDrive.setPower(0.0);
                                        fourwheel.rightFrontDrive.setPower(0.0);
                                        fourwheel.leftBackDrive.setPower(0.0);
                                        fourwheel.leftFrontDrive.setPower(0.0);
                                        flag = true;
                                        break;
                                    }



                                }
                            }





                        }



                        // Disable Tracking when we are done;
                        targetsSkyStone.deactivate();*/
                        strafeEnc(.9, 12, strafeDirections.Right);
                        //gyroTurn(-1, .3, turnDirections.RIGHT);
                        driveEnc(1, 45, driveDirection.Forward);
                        driveEnc(1, 45, driveDirection.Forward);
                        driveEnc(1, 45, driveDirection.Forward);
                        driveEnc(1, 45, driveDirection.Forward);
                        driveEnc(1, 40, driveDirection.Forward);
                        //driveEnc(.5, 20, driveDirection.Forward);
                        //driveEnc(.5, 30, driveDirection.Forward);
                        sleep(750);
                        //gyroTurn(80, .5, turnDirections.LEFT);
                        strafeEnc(.9, 14, strafeDirections.Left);
                        fourwheel.blockArm.setPosition(.6);
                        fourwheel.grabberSecL.setPosition(0);

                        sleep(500);
                        fourwheel.blockArm.setPosition(1);
                        /*gyroTurn(-60, .8 ,turnDirections.RIGHT);
                        driveEnc(.6, 10, driveDirection.Backward);
                        fourwheel.platformHolderL.setPosition(.39);
                        fourwheel.platformHolderR.setPosition(.54);
                        sleep(750);
                        driveEnc(1, 90, driveDirection.Forward);
                        gyroTurn(-90, .8, turnDirections.RIGHT);
                        fourwheel.platformHolderL.setPosition(1);
                        fourwheel.platformHolderR.setPosition(0);
                        driveEnc(1, 60, driveDirection.Forward);
                        //strafeEnc(.9, 50, strafeDirections.Right);*/
                        strafeEnc(.9, 12, strafeDirections.Right);
                        driveEnc(.9, 40, driveDirection.Backward);





                    }
                    else if (platformGrab.getValue().equals("yes") && skyBlock.getValue() == 1) {
                        gyroTurn(100, .5, turnDirections.LEFT);
                        driveEnc(.5, 20, driveDirection.Forward);
                        driveEnc(.3, 70, driveDirection.Forward);
                        //fourwheel.platformHolder.setPosition(.17);//.47 is up
                        fourwheel.platformHolderR.setPosition(.57);
                        fourwheel.platformHolderL.setPosition(.059);
                        sleep(500);
                        driveEnc(.5, 40, driveDirection.Backward);
                        //  fourwheel.platformHolder.setPosition(.47);//.47 is up
                        fourwheel.platformHolderR.setPosition(0);
                        fourwheel.platformHolderL.setPosition(1);
                        sleep(500);
                        strafeEnc(.6, 40, strafeDirections.Right);




                    }

                }
            }
            else {
                if (waitStart.getValue() > 2){
                    driveEnc(.9, 20, driveDirection.Forward);
                    strafeEnc(.9, 15, strafeDirections.Left);
                }
                else {
                    strafeEnc(.9, 15, strafeDirections.Left);
                }

            }

        }



    }
}
