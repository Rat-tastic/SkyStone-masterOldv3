package org.firstinspires.ftc.teamcode.Fusion4133;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaSkyStone;
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

public abstract class MechanumTestSetup extends LinearOpMode{
    mechanumHardwareTest fourwheel = new mechanumHardwareTest();


    AutonomousTextOption    allianceColor       = new AutonomousTextOption("Alliance Color", "blue", new String[] {"blue", "red"});
    AutonomousIntOption     waitStart           = new AutonomousIntOption("Wait at Start", 0, 0, 20);
    AutonomousIntOption     skyBlock           = new AutonomousIntOption("How Many SkyBlocks", 0, 0, 2);
    AutonomousTextOption    platformGrab       = new AutonomousTextOption("grab platform?", "yes", new String[] {"yes", "no"});
    AutonomousTextOption    startPos       = new AutonomousTextOption("Start Position", "tower", new String[] {"tower", "block line"});


    //This is the order of our options and setting them all to there preset value.
    AutonomousOption [] autoOptions       = {allianceColor, waitStart, startPos, skyBlock,platformGrab};
    int currentOption = 0;


    //this setting the buttons to false to make sure options are not being chosen for us.
    boolean aPressed = false;
    boolean bPressed = false;
    boolean xPressed = false;
    boolean yPressed = false;


        ElapsedTime movementTime   = new ElapsedTime();
    final double ticksPerInch       = (15);  //188;  //tick of the encoder * gear ratio / circumference of the wheel
    final double ticksPerInchStrafe = (960 / 13);
    final  int   tickOverRun        = 0;   //number of tick robot overruns target after stop
    final double inchesPerDeg       = .5;  //wheel base of robot * pi / 360
    final double tickPerDeg         = 16.67 * inchesPerDeg;
    public int swag;

    public enum turnDirections{
        LEFT, RIGHT
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




    public enum driveDirection{
        Forward,Backward
    }
    public enum turnDirection{
        Left, Right
    }
    public enum strafeDirections{
        Left, Right
    }
    public enum allianceColor{
        Red, Blue
    }
    public void driveTime (double iSpeed, int iTime, driveDirection iDir ) {

        double dirAdj;

        if(iDir == driveDirection.Backward){
            fourwheel.motorBackward(iSpeed);
        }
        else {
            fourwheel.motorForward(iSpeed);
        }


        movementTime.reset();

        while (opModeIsActive() && movementTime.milliseconds() < iTime) {
        }

        fourwheel.motorStop();

    }
    public void driveEnc (double iSpeed, double iDist, driveDirection iDir) {
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

        if (iDir == driveDirection.Forward) {
            dirAdj = -1.0;
            vSpeed = vSpeed * -1;
        } else {
            dirAdj = 1.0;

        }

        leftStartFront = fourwheel.leftFrontDrive.getCurrentPosition();
        leftStartBack = fourwheel.leftBackDrive.getCurrentPosition();
        rightStartFront = fourwheel.rightFrontDrive.getCurrentPosition();
        rightStartBack = fourwheel.rightBackDrive.getCurrentPosition();

        leftTargetFront = leftStartFront + (int) (iDist * ticksPerInch * dirAdj);
        leftTargetBack = leftStartBack + (int) (iDist * ticksPerInch * dirAdj);
        rightTargetFront = rightStartFront + (int) (iDist * ticksPerInch * dirAdj);
        rightTargetBack = rightStartBack + (int) (iDist * ticksPerInch * dirAdj);


        //telemetry that allows us see if our target is correct.
        telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
        telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
        telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
        telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

        telemetry.update();

        //vSpeed is a variable that is bases on what we set iSpeed to when putting this void into the program.
        fourwheel.rightBackDrive.setPower(vSpeed);
        fourwheel.rightFrontDrive.setPower(vSpeed);
        fourwheel.leftFrontDrive.setPower(vSpeed );
        fourwheel.leftBackDrive.setPower(vSpeed);

        if (iDir == driveDirection.Forward) {

            //while loop set motor at vSpeed until these conditions are not ture any longer.
            while (opModeIsActive() &&
                    fourwheel.leftBackDrive.getCurrentPosition() > leftTargetBack &&
                    fourwheel.leftFrontDrive.getCurrentPosition() > leftTargetFront &&
                    fourwheel.rightFrontDrive.getCurrentPosition() > rightTargetFront &&
                    fourwheel.rightBackDrive.getCurrentPosition() > rightTargetBack ) {
            }
        }
        else {
            while (opModeIsActive() &&
                    fourwheel.leftFrontDrive.getCurrentPosition() < leftTargetFront &&
                    fourwheel.leftBackDrive.getCurrentPosition() < leftTargetBack &&
                    fourwheel.rightFrontDrive.getCurrentPosition() < rightTargetFront &&
                    fourwheel.rightBackDrive.getCurrentPosition() < rightTargetBack ) {
            }
        }

        fourwheel.motorStop();

        leftFinalFront = fourwheel.leftFrontDrive.getCurrentPosition();
        leftFinalBack = fourwheel.leftBackDrive.getCurrentPosition();
        rightFinalFront = fourwheel.rightFrontDrive.getCurrentPosition();
        rightFinalBack = fourwheel.rightBackDrive.getCurrentPosition();

        telemetry.addData("leftFront",Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront)+ "; " + Integer.toString(leftFinalFront)+ "; " + Integer.toString(leftTargetFront));
        telemetry.addData("leftBack",Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack)+ "; " + Integer.toString(leftFinalBack)+ "; " + Integer.toString(leftTargetBack));
        telemetry.addData("rightFront",Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront)+ "; " + Integer.toString(rightFinalFront) + "; " + Integer.toString(rightTargetFront));
        telemetry.addData("rightBack",Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack)+ "; " + Integer.toString(rightFinalBack) + "; " + Integer.toString(rightTargetBack));

        telemetry.update();
    }
    private double getAngle2()
    {
        Orientation angles = fourwheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

       return deltaAngle;
    }
    public void resetAngleTurnRight(double iSpeed, int angle){
        Orientation angles = fourwheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double angleNow = angles.firstAngle;
        telemetry.addData("angle now:", angleNow);
        fourwheel.leftBackDrive.setPower(-iSpeed);
        fourwheel.leftFrontDrive.setPower(-iSpeed);
        fourwheel.rightBackDrive.setPower(iSpeed);
        fourwheel.rightFrontDrive.setPower(iSpeed);
        while (angle + 1 < getAngle2()){
        }

        fourwheel.leftBackDrive.setPower(0);
        fourwheel.leftFrontDrive.setPower(0);
        fourwheel.rightBackDrive.setPower(0);
        fourwheel.rightFrontDrive.setPower(0);
    }
    public void driveGyro(double iSpeed, int angle,int iDist,  driveDirection iDir){
        Orientation angles = fourwheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double angleNow = angles.firstAngle;
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

        if (iDir == driveDirection.Forward) {
            dirAdj = -1.0;
            vSpeed = vSpeed * -1;
        } else {
            dirAdj = 1.0;

        }

        leftStartFront = fourwheel.leftFrontDrive.getCurrentPosition();
        leftStartBack = fourwheel.leftBackDrive.getCurrentPosition();
        rightStartFront = fourwheel.rightFrontDrive.getCurrentPosition();
        rightStartBack = fourwheel.rightBackDrive.getCurrentPosition();

        leftTargetFront = leftStartFront + (int) (iDist * ticksPerInch * dirAdj);
        leftTargetBack = leftStartBack + (int) (iDist * ticksPerInch * dirAdj);
        rightTargetFront = rightStartFront + (int) (iDist * ticksPerInch * dirAdj);
        rightTargetBack = rightStartBack + (int) (iDist * ticksPerInch * dirAdj);


        //telemetry that allows us see if our target is correct.
        telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
        telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
        telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
        telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

        telemetry.update();

        //vSpeed is a variable that is bases on what we set iSpeed to when putting this void into the program.
        fourwheel.rightBackDrive.setPower(vSpeed);
        fourwheel.rightFrontDrive.setPower(vSpeed);
        fourwheel.leftFrontDrive.setPower(vSpeed );
        fourwheel.leftBackDrive.setPower(vSpeed);

        if (iDir == driveDirection.Forward) {

            //while loop set motor at vSpeed until these conditions are not ture any longer.
            while (opModeIsActive() &&
                    fourwheel.leftBackDrive.getCurrentPosition() > leftTargetBack &&
                    fourwheel.leftFrontDrive.getCurrentPosition() > leftTargetFront &&
                    fourwheel.rightFrontDrive.getCurrentPosition() > rightTargetFront &&
                    fourwheel.rightBackDrive.getCurrentPosition() > rightTargetBack ) {
                if (angle > getAngle2()){
                    fourwheel.rightBackDrive.setPower(vSpeed - (getAngle2() * -.02));
                    fourwheel.rightFrontDrive.setPower(vSpeed - (getAngle2() * -.02));
                    fourwheel.leftFrontDrive.setPower(vSpeed );
                    fourwheel.leftBackDrive.setPower(vSpeed);
                }
                else if (angle < getAngle2()){
                    fourwheel.rightBackDrive.setPower(vSpeed);
                    fourwheel.rightFrontDrive.setPower(vSpeed);
                    fourwheel.leftFrontDrive.setPower(vSpeed - (getAngle2() * .02) );
                    fourwheel.leftBackDrive.setPower(vSpeed - (getAngle2() * .02));
                }
                else {
                    fourwheel.rightBackDrive.setPower(vSpeed);
                    fourwheel.rightFrontDrive.setPower(vSpeed);
                    fourwheel.leftFrontDrive.setPower(vSpeed );
                    fourwheel.leftBackDrive.setPower(vSpeed);
                }
            }
        }
        else {
            while (opModeIsActive() &&
                    fourwheel.leftFrontDrive.getCurrentPosition() < leftTargetFront &&
                    fourwheel.leftBackDrive.getCurrentPosition() < leftTargetBack &&
                    fourwheel.rightFrontDrive.getCurrentPosition() < rightTargetFront &&
                    fourwheel.rightBackDrive.getCurrentPosition() < rightTargetBack ) {
                if (angle < getAngle2()){
                    fourwheel.rightBackDrive.setPower(vSpeed + (getAngle2() * .02));
                    fourwheel.rightFrontDrive.setPower(vSpeed + (getAngle2() * .02));
                    fourwheel.leftFrontDrive.setPower(vSpeed );
                    fourwheel.leftBackDrive.setPower(vSpeed);
                }
                else if (angle > getAngle2()){
                    fourwheel.rightBackDrive.setPower(vSpeed);
                    fourwheel.rightFrontDrive.setPower(vSpeed);
                    fourwheel.leftFrontDrive.setPower(vSpeed + (getAngle2() * -.02) );
                    fourwheel.leftBackDrive.setPower(vSpeed + (getAngle2() * -.02));
                }
                else {
                    fourwheel.rightBackDrive.setPower(vSpeed);
                    fourwheel.rightFrontDrive.setPower(vSpeed);
                    fourwheel.leftFrontDrive.setPower(vSpeed );
                    fourwheel.leftBackDrive.setPower(vSpeed);
                }

            }
        }



        leftFinalFront = fourwheel.leftFrontDrive.getCurrentPosition();
        leftFinalBack = fourwheel.leftBackDrive.getCurrentPosition();
        rightFinalFront = fourwheel.rightFrontDrive.getCurrentPosition();
        rightFinalBack = fourwheel.rightBackDrive.getCurrentPosition();

        telemetry.addData("leftFront",Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront)+ "; " + Integer.toString(leftFinalFront)+ "; " + Integer.toString(leftTargetFront));
        telemetry.addData("leftBack",Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack)+ "; " + Integer.toString(leftFinalBack)+ "; " + Integer.toString(leftTargetBack));
        telemetry.addData("rightFront",Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront)+ "; " + Integer.toString(rightFinalFront) + "; " + Integer.toString(rightTargetFront));
        telemetry.addData("rightBack",Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack)+ "; " + Integer.toString(rightFinalBack) + "; " + Integer.toString(rightTargetBack));

        telemetry.update();

    }

    public void spinEnc (double iSpeed, int iDist, turnDirection iDir) {

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

        if (iDir == turnDirection.Right) {
            leftDirAdj = -1.0;
            rightDirAdj = 1.0;

        } else {
            leftDirAdj = 1.0;
            rightDirAdj = -1.0;
        }

        leftStartFront = fourwheel.leftFrontDrive.getCurrentPosition();
        leftStartBack = fourwheel.leftBackDrive.getCurrentPosition();
        rightStartFront = fourwheel.rightFrontDrive.getCurrentPosition();
        rightStartBack = fourwheel.rightBackDrive.getCurrentPosition();

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

        if (iDir == turnDirection.Right){
            fourwheel.rightFrontDrive.setPower(iSpeed * rightDirAdj);
            fourwheel.rightBackDrive.setPower(iSpeed * rightDirAdj);
            fourwheel.leftBackDrive.setPower(iSpeed * leftDirAdj);
            fourwheel.leftFrontDrive.setPower(iSpeed * leftDirAdj);
        }
        else {
            fourwheel.rightFrontDrive.setPower(iSpeed * rightDirAdj);
            fourwheel.rightBackDrive.setPower(iSpeed * rightDirAdj);
            fourwheel.leftBackDrive.setPower(iSpeed * leftDirAdj);
            fourwheel.leftFrontDrive.setPower(iSpeed * leftDirAdj);
        }

        if ((iDir == turnDirection.Right)) {

            while (opModeIsActive() &&
                    fourwheel.leftFrontDrive.getCurrentPosition() > leftTargetFront &&
                    fourwheel.leftBackDrive.getCurrentPosition() > leftTargetBack &&
                    fourwheel.rightFrontDrive.getCurrentPosition() < rightTargetFront &&
                    fourwheel.rightBackDrive.getCurrentPosition() < rightTargetBack) {
            }
        } else {
            while (opModeIsActive() &&
                    fourwheel.leftFrontDrive.getCurrentPosition() < leftTargetFront &&
                    fourwheel.leftBackDrive.getCurrentPosition() < leftTargetBack &&
                    fourwheel.rightFrontDrive.getCurrentPosition() > rightTargetFront &&
                    fourwheel.rightBackDrive.getCurrentPosition() > rightTargetBack) {
            }
        }

        fourwheel.rightBackDrive.setPower(0.0);
        fourwheel.rightFrontDrive.setPower(0.0);
        fourwheel.leftBackDrive.setPower(0.0);
        fourwheel.leftFrontDrive.setPower(0.0);

        leftFinalFront = fourwheel.leftFrontDrive.getCurrentPosition();
        leftFinalBack = fourwheel.leftBackDrive.getCurrentPosition();
        rightFinalFront = fourwheel.rightFrontDrive.getCurrentPosition();
        rightFinalBack = fourwheel.rightBackDrive.getCurrentPosition();

        telemetry.addData("leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
        telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
        telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
        telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

        telemetry.update();

    }
    public void strafeEnc (double iSpeed, double iDist, strafeDirections iDir) {

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
        if (iDir == strafeDirections.Right) {
            frontLeftBottomRightAdj = -1.0;
            bottomLeftFrontRightAdj = 1.0;
        }
        else {
            frontLeftBottomRightAdj = 1.0;
            bottomLeftFrontRightAdj = -1.0;
        }

        //checks value encoder is at then sets value as start integers.
        leftStartFront = fourwheel.leftFrontDrive.getCurrentPosition();
        leftStartBack = fourwheel.leftBackDrive.getCurrentPosition();
        rightStartFront = fourwheel.rightFrontDrive.getCurrentPosition();
        rightStartBack = fourwheel.rightBackDrive.getCurrentPosition();

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
        if (iDir == strafeDirections.Right){
            fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
            fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj );
            fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj );
            fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
        }
        else {
            fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
            fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
            fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
            fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
        }



        if ((iDir == strafeDirections.Right)) {

            //until the values set here are false it will hold the speed of the motors.
            while (opModeIsActive() &&
                    fourwheel.leftFrontDrive.getCurrentPosition() > leftAdjTargetFront &&
                    fourwheel.leftBackDrive.getCurrentPosition() < leftAdjTargetBack &&
                    fourwheel.rightFrontDrive.getCurrentPosition() < rightAdjTargetFront &&
                    fourwheel.rightBackDrive.getCurrentPosition() > rightAdjTargetBack) {
                telemetry.addData("intresnsic", fourwheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));
                telemetry.update();
            }
        } else {
            while (opModeIsActive() &&
                    fourwheel.leftFrontDrive.getCurrentPosition() < leftAdjTargetFront &&
                    fourwheel.leftBackDrive.getCurrentPosition() > leftAdjTargetBack &&
                    fourwheel.rightFrontDrive.getCurrentPosition() > rightAdjTargetFront &&
                    fourwheel.rightBackDrive.getCurrentPosition() < rightAdjTargetBack) {
                telemetry.addData("intresnsic", fourwheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));
                telemetry.update();
            }
        }

        fourwheel.rightBackDrive.setPower(0.0);
        fourwheel.rightFrontDrive.setPower(0.0);
        fourwheel.leftBackDrive.setPower(0.0);
        fourwheel.leftFrontDrive.setPower(0.0);

        //checks values of encoders at the end of the program and then reads them to us so we know if the we are getting to desired values.
        leftFinalFront = fourwheel.leftFrontDrive.getCurrentPosition();
        leftFinalBack = fourwheel.leftBackDrive.getCurrentPosition();
        rightFinalFront = fourwheel.rightFrontDrive.getCurrentPosition();
        rightFinalBack = fourwheel.rightBackDrive.getCurrentPosition();

        telemetry.addData("Done leftFront", Integer.toString(leftStartFront) + "; " + Integer.toString(leftTargetFront) + "; " + Integer.toString(leftFinalFront));
        telemetry.addData("leftBack", Integer.toString(leftStartBack) + "; " + Integer.toString(leftTargetBack) + "; " + Integer.toString(leftFinalBack));
        telemetry.addData("rightFront", Integer.toString(rightStartFront) + "; " + Integer.toString(rightTargetFront) + "; " + Integer.toString(rightFinalFront));
        telemetry.addData("rightBack", Integer.toString(rightStartBack) + "; " + Integer.toString(rightTargetBack) + "; " + Integer.toString(rightFinalBack));

        telemetry.update();

    }
    private void resetAngle()
    {
        fourwheel.lastAngles = fourwheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        fourwheel.globalAngle = 0;
    }
    private double getAngle()
    {
        Orientation angles = fourwheel.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - fourwheel.lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        fourwheel.globalAngle += deltaAngle;

        fourwheel.lastAngles = angles;

        return fourwheel.globalAngle;
    }
    public void drive (double iSpeed, driveDirection iDir) {
        double dirAdj;

        if (iDir == driveDirection.Forward) {
            dirAdj = -1.0;
        } else {
            dirAdj = 1.0;
        }
        fourwheel.leftBackDrive.setPower(iSpeed * dirAdj);
        fourwheel.leftFrontDrive.setPower(iSpeed * dirAdj);
        fourwheel.rightBackDrive.setPower(iSpeed * dirAdj);
        fourwheel.rightFrontDrive.setPower(iSpeed * dirAdj);

    }

    public void driveToColor(String color, double power, driveDirection iDir){

        Color.RGBToHSV((int) (fourwheel.color.red() * fourwheel.SCALE_FACTOR),
                (int) (fourwheel.color.green() * fourwheel.SCALE_FACTOR),
                (int) (fourwheel.color.blue() * fourwheel.SCALE_FACTOR),
                fourwheel.hsvValues);
        double red = 0;
        double blue = 0;
        double black = 51;
        double dirAdj;

        if (iDir == driveDirection.Forward) {
            dirAdj = -1.0;
        } else {
            dirAdj = 1.0;
        }


        if (color.equalsIgnoreCase("Red")){
            while (red < 250 && opModeIsActive()) {
                drive((power), iDir);
                red =  fourwheel.color.red();
            }
            fourwheel.motorStop();
        }
        else if (color.equalsIgnoreCase("Blue")){
            while (blue < 200 && opModeIsActive()) {
                drive((power), iDir);
                blue =  fourwheel.color.blue() ;
            }
            fourwheel.motorStop();
        }
        else if (color.equalsIgnoreCase("black")){
            while (black > 50 && opModeIsActive()) {
                drive((power), iDir);
                black =  fourwheel.color.green();

            }
            fourwheel.motorStop();
        }

    }
    public void gyroTurn (int degrees, double power, turnDirections iDir){
        Orientation lastAngles = new Orientation();
        double  leftPower, rightPower;

        if (iDir == turnDirections.LEFT){
            leftPower = power;
            rightPower = -power;
        }
        else {
            leftPower = -power;
            rightPower = power;
        }

        fourwheel.rightBackDrive.setPower(rightPower);
        fourwheel.rightFrontDrive.setPower(rightPower);
        fourwheel.leftBackDrive.setPower(leftPower);
        fourwheel.leftFrontDrive.setPower(leftPower);

        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {}

            while (opModeIsActive() && getAngle() > degrees) {}
        }
        else {   // left turn.
            while (opModeIsActive() && getAngle() < degrees-5) {}
        }

        fourwheel.rightBackDrive.setPower(0.0);
        fourwheel.rightFrontDrive.setPower(0.0);
        fourwheel.leftBackDrive.setPower(0.0);
        fourwheel.leftFrontDrive.setPower(0.0);

        sleep(1000);

        resetAngle();
    }
    public void gyroTurn2 (int degrees){
        Orientation lastAngles = new Orientation();
        double  leftPower, rightPower;



        fourwheel.rightBackDrive.setPower(-1);
        fourwheel.rightFrontDrive.setPower(-1);
        fourwheel.leftFrontDrive.setPower(-.2);
        fourwheel.leftBackDrive.setPower(-.2);

        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {}

            while (opModeIsActive() && getAngle() > degrees) {}
        }
        else {   // left turn.
            while (opModeIsActive() && getAngle() < degrees-5) {}
        }

        fourwheel.rightBackDrive.setPower(0.0);
        fourwheel.rightFrontDrive.setPower(0.0);
        fourwheel.leftBackDrive.setPower(0.0);
        fourwheel.leftFrontDrive.setPower(0.0);

        sleep(1000);

        resetAngle();
    }
    public void gyroTurn3 (int degrees){
        Orientation lastAngles = new Orientation();
        double  leftPower, rightPower;



        fourwheel.rightBackDrive.setPower(-.2);
        fourwheel.rightFrontDrive.setPower(-.2);
        fourwheel.leftFrontDrive.setPower(-1);
        fourwheel.leftBackDrive.setPower(-1);

        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0) {}

            while (opModeIsActive() && getAngle() > degrees) {}
        }
        else {   // left turn.
            while (opModeIsActive() && getAngle() < degrees-5) {}
        }

        fourwheel.rightBackDrive.setPower(0.0);
        fourwheel.rightFrontDrive.setPower(0.0);
        fourwheel.leftBackDrive.setPower(0.0);
        fourwheel.leftFrontDrive.setPower(0.0);

        sleep(1000);

        resetAngle();
    }
    public void skyBlockIdentification (){
        int leftFront;
        int leftBack;
        int rightFront;
        int rightBack;

        leftBack = fourwheel.leftBackDrive.getCurrentPosition();
        if(allianceColor.getValue().equals("blue")){
            leftBack = fourwheel.leftBackDrive.getCurrentPosition();
            while(fourwheel.color.alpha() > 6 && fourwheel.leftFrontDrive.getCurrentPosition() - leftBack < 100){
                fourwheel.rightBackDrive.setPower(.3);
                fourwheel.rightFrontDrive.setPower(.3);
                fourwheel.leftBackDrive.setPower(.3);
                fourwheel.leftFrontDrive.setPower(.3);
            }

            fourwheel.rightBackDrive.setPower(.0);
            fourwheel.rightFrontDrive.setPower(.0);
            fourwheel.leftBackDrive.setPower(.0);
            fourwheel.leftFrontDrive.setPower(.0);

            if (leftBack < 10){
                telemetry.log().add("block in pos 1");
                swag = 1;
            }
            else if (leftBack < 100 && leftBack > 20) {
                telemetry.log().add("block in pos 2");
                swag = 2;
            }
            else {
                telemetry.log().add("block in pos 3");
                swag = 3;
            }

        }
        else if(allianceColor.getValue().equals("red")){
            leftBack = fourwheel.leftBackDrive.getCurrentPosition();

            while(fourwheel.color.alpha() > 6 && leftBack - fourwheel.leftFrontDrive.getCurrentPosition() > -100){
                fourwheel.rightBackDrive.setPower(-.3);
                fourwheel.rightFrontDrive.setPower(-.3);
                fourwheel.leftBackDrive.setPower(-.3);
                fourwheel.leftFrontDrive.setPower(-.3);
            }

            fourwheel.rightBackDrive.setPower(.0);
            fourwheel.rightFrontDrive.setPower(.0);
            fourwheel.leftBackDrive.setPower(.0);
            fourwheel.leftFrontDrive.setPower(.0);

            if (leftBack < -.1){
                telemetry.log().add("block in pos 1");
                swag = 1;
            }
            else if (leftBack > -100 && leftBack < .1) {
                telemetry.log().add("block in pos 2");
                swag = 2;
            }
            else {
                telemetry.log().add("block in pos 3");
                swag = 3;
            }
        }
    }

    public void adjustToTarget(VuforiaTrackable iTarget, double iDist, double iSpeed) {
        boolean flag = false;
        boolean targetVisible = false;
        OpenGLMatrix lastLocation = null;
        final float mmPInch = 25.4f;
        double frontLeftBottomRightAdj = -1.0;
        double bottomLeftFrontRightAdj = 1.0;

        while (opModeIsActive() && flag == false) {
                if (((VuforiaTrackableDefaultListener) iTarget.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", iTarget.getName());
                    targetVisible = true;

                    // getUpdatedRobotLocation() will return null if no new information is available since
                    // the last time that call was made, or if the trackable is not currently visible.
                    OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) iTarget.getListener()).getUpdatedRobotLocation();
                    if (robotLocationTransform != null) {
                        lastLocation = robotLocationTransform;
                    }
                    VectorF translation = lastLocation.getTranslation();
                    telemetry.addData("Pos (in)", translation.get(1)/mmPInch);

                    if (lastLocation.getTranslation().get(1)  < iDist - 10.0 && allianceColor.getValue().equals("blue")) {
                        fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                        fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                        fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                        fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                    } else if (lastLocation.getTranslation().get(1) > iDist + 10.0 && allianceColor.getValue().equals("red")) {
                        fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                        fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                        fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                        fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                    }else  if (lastLocation.getTranslation().get(1)  > iDist + 10 && allianceColor.getValue().equals("red")) {
                        fourwheel.rightBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);//right
                        fourwheel.leftFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                        fourwheel.rightFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                        fourwheel.leftBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                    } else if (lastLocation.getTranslation().get(1) < iDist - 10 && allianceColor.getValue().equals("red")) {
                        fourwheel.rightBackDrive.setPower(iSpeed * bottomLeftFrontRightAdj);//left
                        fourwheel.leftFrontDrive.setPower(iSpeed * bottomLeftFrontRightAdj);
                        fourwheel.rightFrontDrive.setPower(iSpeed * frontLeftBottomRightAdj);
                        fourwheel.leftBackDrive.setPower(iSpeed * frontLeftBottomRightAdj);

                    }

                    else {
                        fourwheel.motorStop();;
                        flag = true;
                    }
                }
        }
    }


}
