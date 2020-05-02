package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
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
import org.firstinspires.ftc.teamcode.Fusion4133.skyStoneDetect;

@Autonomous(name="Mechanum Auto Test", group="2019")
public class MechnumAuto extends MechanumTestSetup {
    skyStoneDetect detector;
    MecanumHardwareSetup mecanum;

    @Override
    public void runOpMode() {
        telemetry.log().add("Select Options");
        telemetry.update();

        selectOptions();

        fourwheel.hardwareInit(hardwareMap);
        fourwheel.imuInit(hardwareMap);

        mechanumHardwareTest.setOpMode(this);
        /*if(allianceColor.getValue().equals("blue")){
            skyStoneDetect detector = new skyStoneDetect(false, false);
        }
        else if (allianceColor.getValue().equals("red")){
            skyStoneDetect detector = new skyStoneDetect(false, true);
        }*/

        skyStoneDetect detector = new skyStoneDetect(false, false);
        //skyStoneDetect detector = new skyStoneDetect(false, true);

        waitForStart();
        skyStoneDetect.SkyStoneConfigurations stoneConfigurations = detector.look();

        if (allianceColor.getValue().equals("blue")) {

            if (startPos.getValue().equals("block line")) {
                fourwheel.blockArm2.setPosition(.65);
                fourwheel.grabberSecR.setPosition(.82);
                strafeEnc(.7, 18 ,strafeDirections.Right);
                if (stoneConfigurations == skyStoneDetect.SkyStoneConfigurations.ONE_FOUR){
                    telemetry.addLine("one four");
                    telemetry.update();

                    driveEnc(.7, 8, driveDirection.Backward);
                    fourwheel.grabberSecR.setPosition(.1);//grab
                    sleep(750);

                    fourwheel.blockArm2.setPosition(.3);
                    //strafeEnc(.4 ,3, strafeDirections.Left);
                    sleep(500);
                    //resetAngleTurnRight(.3, 0);
                    //driveEnc(.3, 10, driveDirection.Forward);
                    driveGyro(.5, 0, 20, driveDirection.Forward);
                    driveGyro(.7, 0,30, driveDirection.Forward);
                    driveGyro(.9, 0,90, driveDirection.Forward);
                    driveGyro(.7, 0,30, driveDirection.Forward);
                    driveGyro(.5, 0,20, driveDirection.Forward);
                    fourwheel.motorStop();
                    //sleep(500);
                    ///strafeEnc(.4 ,3, strafeDirections.Right);
                    sleep(250);
                    fourwheel.blockArm2.setPosition(.63);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.2);//close
                    fourwheel.blockArm2.setPosition(.3);
                    strafeEnc(.6, 1 , strafeDirections.Left);
                    sleep(500);
                    driveGyro(.5, 0, 20, driveDirection.Backward);
                    driveGyro(.7, 0,55, driveDirection.Backward);
                    driveGyro(.9, 0,100, driveDirection.Backward);
                    driveGyro(.7, 0,65, driveDirection.Backward);
                    driveGyro(.5, 0,20, driveDirection.Backward);
                    fourwheel.motorStop();
                    fourwheel.grabberSecR.setPosition(.82);
                    fourwheel.blockArm2.setPosition(.65);
                    sleep(500);
                    strafeEnc(.6, 2 , strafeDirections.Right);
                    fourwheel.grabberSecR.setPosition(.1);//grab
                    sleep(750);

                    fourwheel.blockArm2.setPosition(.3);
                    sleep(750);
                    strafeEnc(.6, 2 , strafeDirections.Left);
                    driveGyro(.5, 0, 30, driveDirection.Forward);
                    driveGyro(.7, 0,70, driveDirection.Forward);
                    driveGyro(.9, 0,100, driveDirection.Forward);
                    driveGyro(.7, 0,70, driveDirection.Forward);
                    driveGyro(.5, 0,30, driveDirection.Forward);
                    fourwheel.motorStop();
                    sleep(250);
                    strafeEnc(.6, 3 , strafeDirections.Right);
                    //sleep(250);
                    fourwheel.blockArm2.setPosition(.63);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.2);//close
                    fourwheel.blockArm2.setPosition(.3);
                    strafeEnc(.6, 2 , strafeDirections.Left);
                    sleep(500);
                    driveGyro(.5, 0, 10, driveDirection.Backward);
                    driveGyro(.7, 0,50, driveDirection.Backward);
                    driveGyro(.9, 0,70, driveDirection.Backward);
                    driveGyro(.7, 0,50, driveDirection.Backward);
                    driveGyro(.5, 0,10, driveDirection.Backward);
                    fourwheel.motorStop();

                    fourwheel.grabberSecR.setPosition(.82);
                    fourwheel.blockArm2.setPosition(.65);
                    sleep(500);
                    strafeEnc(.6, 3 , strafeDirections.Right);
                    fourwheel.grabberSecR.setPosition(.1);//grab
                    sleep(750);

                    fourwheel.blockArm2.setPosition(.3);
                    sleep(750);
                    strafeEnc(.6, 3 , strafeDirections.Left);
                    driveGyro(.5, 0, 10, driveDirection.Forward);
                    driveGyro(.7, 0,40, driveDirection.Forward);
                    driveGyro(.9, 0,60, driveDirection.Forward);
                    driveGyro(.7, 0,45, driveDirection.Forward);
                    driveGyro(.5, 0,10, driveDirection.Forward);
                    fourwheel.motorStop();
                    strafeEnc(.6, 8 , strafeDirections.Right);
                    //sleep(250);

                    fourwheel.blockArm2.setPosition(.63);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(250);
                    fourwheel.grabberSecR.setPosition(.2);//close
                    fourwheel.blockArm2.setPosition(.3);
                    fourwheel.colorStick.setPower(-1.0);
                    gyroTurn(65, .8, turnDirections.LEFT);
                    fourwheel.platformHolderR.setPosition(1);
                    fourwheel.platformHolderL.setPosition(0);
                    driveEnc(.5, 30, driveDirection.Backward);
                    fourwheel.platformHolderR.setPosition(.75);
                    fourwheel.platformHolderL.setPosition(.3);
                    sleep(150);
                    fourwheel.colorStick.setPower(0);
                    /*fourwheel.rightBackDrive.setPower(-1);
                    fourwheel.rightFrontDrive.setPower(-1);
                    fourwheel.leftFrontDrive.setPower(-.2);
                    fourwheel.leftBackDrive.setPower(-.2);
                    sleep(2500);*/
                    gyroTurn2(90);
                    fourwheel.motorStop();
                    driveEnc(.8, 50, driveDirection.Backward);
                    fourwheel.platformHolderR.setPosition(1);
                    fourwheel.platformHolderL.setPosition(0);
                    sleep(250);

                    gyroTurn(10, .8, turnDirections.LEFT);
                    //driveToColor("Blue", .6, driveDirection.Forward);










                }
                else if (stoneConfigurations== skyStoneDetect.SkyStoneConfigurations.TWO_FIVE){
                    telemetry.addLine("Two five");
                    telemetry.update();
                    spinEnc(.5, 2, turnDirection.Left);
                    driveGyro(.7, 0,15, driveDirection.Forward);
                    fourwheel.motorStop();

                    fourwheel.grabberSecR.setPosition(.3);//grab
                    sleep(500);

                    fourwheel.blockArm2.setPosition(.25);

                    sleep(250);
                    strafeEnc(.4 ,2, strafeDirections.Left);
                    //resetAngleTurnRight(.3, 0);
                    //driveEnc(.3, 10, driveDirection.Forward);
                    driveGyro(.5, 0, 20, driveDirection.Forward);
                    driveGyro(.7, 0,20, driveDirection.Forward);
                    driveGyro(.9, 0,90, driveDirection.Forward);
                    driveGyro(.7, 0,30, driveDirection.Forward);
                    driveGyro(.5, 0,20, driveDirection.Forward);
                    fourwheel.motorStop();
                    strafeEnc(.6, 3 , strafeDirections.Right);
                    ///strafeEnc(.4 ,3, strafeDirections.Right);
                    //sleep(250);
                    fourwheel.blockArm2.setPosition(.61);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.37);//close
                    fourwheel.blockArm2.setPosition(.25);
                    strafeEnc(.6, 1 , strafeDirections.Left);
                    sleep(250);
                    driveGyro(.5, 0, 20, driveDirection.Backward);
                    driveGyro(.7, 0,70, driveDirection.Backward);
                    driveGyro(.9, 0,100, driveDirection.Backward);
                    driveGyro(.7, 0,60, driveDirection.Backward);
                    driveGyro(.5, 0,20, driveDirection.Backward);
                    fourwheel.motorStop();
                    fourwheel.grabberSecR.setPosition(.82);
                    fourwheel.blockArm2.setPosition(.61);
                    sleep(250);
                    strafeEnc(.6, 4 , strafeDirections.Right);
                    fourwheel.grabberSecR.setPosition(.3);//grab
                    sleep(500);

                    fourwheel.blockArm2.setPosition(.25);
                    sleep(250);
                    strafeEnc(.6, 2 , strafeDirections.Left);
                    driveGyro(.5, 0, 30, driveDirection.Forward);
                    driveGyro(.7, 0,70, driveDirection.Forward);
                    driveGyro(.9, 0,100, driveDirection.Forward);
                    driveGyro(.7, 0,60, driveDirection.Forward);
                    driveGyro(.5, 0,30, driveDirection.Forward);
                    fourwheel.motorStop();
                    //sleep(500);
                    strafeEnc(.6, 4 , strafeDirections.Right);
                    //sleep(250);
                    fourwheel.blockArm2.setPosition(.61);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.37);//close
                    fourwheel.blockArm2.setPosition(.25);
                    strafeEnc(.6, 2 , strafeDirections.Left);
                    sleep(500);
                    driveGyro(.5, 0, 20, driveDirection.Backward);
                    driveGyro(.7, 0,40, driveDirection.Backward);
                    driveGyro(.9, 0,80, driveDirection.Backward);
                    driveGyro(.7, 0,42, driveDirection.Backward);
                    driveGyro(.5, 0,20, driveDirection.Backward);
                    fourwheel.motorStop();

                    fourwheel.grabberSecR.setPosition(.82);
                    fourwheel.blockArm2.setPosition(.61);
                    sleep(500);
                    strafeEnc(.6, 6 , strafeDirections.Right);
                    fourwheel.grabberSecR.setPosition(.3);//grab
                    sleep(500);

                    fourwheel.blockArm2.setPosition(.25);
                    sleep(250);
                    strafeEnc(.6, 4 , strafeDirections.Left);
                    driveGyro(.5, 0, 10, driveDirection.Forward);
                    driveGyro(.7, 0,40, driveDirection.Forward);
                    driveGyro(.9, 0,60, driveDirection.Forward);
                    driveGyro(.7, 0,50, driveDirection.Forward);
                    driveGyro(.5, 0,10, driveDirection.Forward);
                    fourwheel.motorStop();
                    strafeEnc(.6, 8 , strafeDirections.Right);
                    //sleep(500);

                    fourwheel.blockArm2.setPosition(.61);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.37);//close
                    fourwheel.blockArm2.setPosition(.25);


                    gyroTurn(65, .8, turnDirections.LEFT);
                    fourwheel.platformHolderR.setPosition(1);
                    fourwheel.platformHolderL.setPosition(0);
                    driveEnc(.5, 27, driveDirection.Backward);
                    fourwheel.platformHolderR.setPosition(.7);
                    fourwheel.platformHolderL.setPosition(.26);
                    sleep(250);
                    /*fourwheel.rightBackDrive.setPower(-1);
                    fourwheel.rightFrontDrive.setPower(-1);
                    fourwheel.leftFrontDrive.setPower(-.2);
                    fourwheel.leftBackDrive.setPower(-.2);
                    sleep(2500);*/
                    gyroTurn2(90);
                    fourwheel.motorStop();
                    driveEnc(.8, 50, driveDirection.Backward);
                    fourwheel.platformHolderR.setPosition(1);
                    fourwheel.platformHolderL.setPosition(0);
                    sleep(250);
                    gyroTurn(10, .8, turnDirections.LEFT);
                    fourwheel.colorStick.setPower(-1.0);
                    sleep(1000);
                    fourwheel.colorStick.setPower(0);

                }
                else {
                    telemetry.addLine("three six");
                    telemetry.update();
                    spinEnc(.5, 2, turnDirection.Left);
                    driveGyro(.7, 0,30, driveDirection.Forward);
                    fourwheel.motorStop();

                    fourwheel.grabberSecR.setPosition(.3);//grab
                    sleep(500);

                    fourwheel.blockArm2.setPosition(.25);

                    sleep(250);
                    strafeEnc(.4 ,2, strafeDirections.Left);
                    //resetAngleTurnRight(.3, 0);
                    //driveEnc(.3, 10, driveDirection.Forward);
                    driveGyro(.5, 0, 20, driveDirection.Forward);
                    driveGyro(.7, 0,20, driveDirection.Forward);
                    driveGyro(.9, 0,90, driveDirection.Forward);
                    driveGyro(.7, 0,30, driveDirection.Forward);
                    driveGyro(.5, 0,20, driveDirection.Forward);
                    fourwheel.motorStop();
                    strafeEnc(.6, 3 , strafeDirections.Right);
                    ///strafeEnc(.4 ,3, strafeDirections.Right);
                    //sleep(250);
                    fourwheel.blockArm2.setPosition(.61);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.37);//close
                    fourwheel.blockArm2.setPosition(.25);
                    strafeEnc(.6, 1 , strafeDirections.Left);
                    sleep(250);
                    driveGyro(.5, 0, 20, driveDirection.Backward);
                    driveGyro(.7, 0,60, driveDirection.Backward);
                    driveGyro(.9, 0,100, driveDirection.Backward);
                    driveGyro(.7, 0,60, driveDirection.Backward);
                    driveGyro(.5, 0,20, driveDirection.Backward);
                    fourwheel.motorStop();
                    fourwheel.grabberSecR.setPosition(.82);
                    fourwheel.blockArm2.setPosition(.61);
                    sleep(250);
                    strafeEnc(.6, 3 , strafeDirections.Right);
                    fourwheel.grabberSecR.setPosition(.3);//grab
                    sleep(500);

                    fourwheel.blockArm2.setPosition(.25);
                    sleep(250);
                    strafeEnc(.6, 2 , strafeDirections.Left);
                    driveGyro(.5, 0, 30, driveDirection.Forward);
                    driveGyro(.7, 0,50, driveDirection.Forward);
                    driveGyro(.9, 0,70, driveDirection.Forward);
                    driveGyro(.7, 0,50, driveDirection.Forward);
                    driveGyro(.5, 0,30, driveDirection.Forward);
                    fourwheel.motorStop();
                    //sleep(500);
                    strafeEnc(.6, 3 , strafeDirections.Right);
                    //sleep(250);
                    fourwheel.blockArm2.setPosition(.61);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.37);//close
                    fourwheel.blockArm2.setPosition(.25);
                    strafeEnc(.6, 2 , strafeDirections.Left);
                    sleep(500);
                    driveGyro(.5, 0, 20, driveDirection.Backward);
                    driveGyro(.7, 0,40, driveDirection.Backward);
                    driveGyro(.9, 0,60, driveDirection.Backward);
                    driveGyro(.7, 0,55, driveDirection.Backward);
                    driveGyro(.5, 0,20, driveDirection.Backward);
                    fourwheel.motorStop();

                    fourwheel.grabberSecR.setPosition(.82);
                    fourwheel.blockArm2.setPosition(.61);
                    sleep(500);
                    strafeEnc(.6, 4 , strafeDirections.Right);
                    fourwheel.grabberSecR.setPosition(.3);//grab
                    sleep(500);

                    fourwheel.blockArm2.setPosition(.25);
                    sleep(250);
                    strafeEnc(.6, 3 , strafeDirections.Left);
                    driveGyro(.5, 0, 20, driveDirection.Forward);
                    driveGyro(.7, 0,50, driveDirection.Forward);
                    driveGyro(.9, 0,80, driveDirection.Forward);
                    driveGyro(.7, 0,40, driveDirection.Forward);
                    driveGyro(.5, 0,20, driveDirection.Forward);
                    fourwheel.motorStop();
                    strafeEnc(.6, 6 , strafeDirections.Right);
                    //sleep(500);

                    fourwheel.blockArm2.setPosition(.61);
                    fourwheel.grabberSecR.setPosition(.82);
                    sleep(500);
                    fourwheel.grabberSecR.setPosition(.37);//close
                    fourwheel.blockArm2.setPosition(.25);


                    gyroTurn(65, .8, turnDirections.LEFT);
                    fourwheel.platformHolderR.setPosition(1);
                    fourwheel.platformHolderL.setPosition(0);
                    driveEnc(.5, 27, driveDirection.Backward);
                    fourwheel.platformHolderR.setPosition(.7);
                    fourwheel.platformHolderL.setPosition(.26);
                    sleep(250);
                    /*fourwheel.rightBackDrive.setPower(-1);
                    fourwheel.rightFrontDrive.setPower(-1);
                    fourwheel.leftFrontDrive.setPower(-.2);
                    fourwheel.leftBackDrive.setPower(-.2);
                    sleep(2500);*/
                    gyroTurn2(90);
                    fourwheel.motorStop();
                    driveEnc(.8, 50, driveDirection.Backward);
                    fourwheel.platformHolderR.setPosition(1);
                    fourwheel.platformHolderL.setPosition(0);
                    sleep(250);
                    gyroTurn(10, .8, turnDirections.LEFT);
                    fourwheel.colorStick.setPower(-1.0);
                    sleep(1000);
                    fourwheel.colorStick.setPower(0);

                }
            }
            else {
                fourwheel.platformHolderR.setPosition(1);
                fourwheel.platformHolderL.setPosition(0);
                driveGyro(.5, 0,80, driveDirection.Backward);
                fourwheel.motorStop();
                driveGyro(.4, 0,10, driveDirection.Backward);
                fourwheel.motorStop();
                fourwheel.platformHolderR.setPosition(.7);
                fourwheel.platformHolderL.setPosition(.26);
                sleep(250);
                gyroTurn2(90);
                fourwheel.motorStop();
                driveEnc(.8, 50, driveDirection.Backward);
                fourwheel.platformHolderR.setPosition(1);
                fourwheel.platformHolderL.setPosition(0);
                sleep(250);
                gyroTurn(10, .8, turnDirections.LEFT);


            }
        }
        else {
            if (startPos.getValue().equals("block line")) {
                fourwheel.blockArm.setPosition(.62);
                fourwheel.grabberSecL.setPosition(0);
                strafeEnc(.7, 18 ,strafeDirections.Left);
                if (stoneConfigurations == skyStoneDetect.SkyStoneConfigurations.ONE_FOUR){
                    telemetry.addLine("one four");
                    telemetry.update();
                    driveEnc(.7, 5, driveDirection.Backward);

                    fourwheel.grabberSecL.setPosition(.58);//grab
                    sleep(450);

                    fourwheel.blockArm.setPosition(1);
                    strafeEnc(.4 ,2, strafeDirections.Right);
                    sleep(250);
                    //resetAngleTurnRight(.3, 0);
                    //driveEnc(.3, 10, driveDirection.Forward);
                    driveGyro(.5, 0, 20, driveDirection.Forward);
                    driveGyro(.7, 0,30, driveDirection.Forward);
                    driveGyro(.9, 0,90, driveDirection.Forward);
                    driveGyro(.7, 0,30, driveDirection.Forward);
                    driveGyro(.5, 0,20, driveDirection.Forward);
                    fourwheel.motorStop();
                    //sleep(500);
                    strafeEnc(.4 ,7, strafeDirections.Left);
                    sleep(250);
                    fourwheel.blockArm.setPosition(.61);
                    fourwheel.grabberSecL.setPosition(0);
                    sleep(250);
                    fourwheel.grabberSecL.setPosition(.57);//close
                    fourwheel.blockArm.setPosition(1);
                    strafeEnc(.6, 5 , strafeDirections.Right);
                    sleep(250);
                    driveGyro(.5, 0, 20, driveDirection.Backward);
                    driveGyro(.7, 0,55, driveDirection.Backward);
                    driveGyro(.9, 0,100, driveDirection.Backward);
                    driveGyro(.7, 0,65, driveDirection.Backward);
                    driveGyro(.5, 0,20, driveDirection.Backward);
                    fourwheel.motorStop();
                    fourwheel.blockArm.setPosition(.62);
                    fourwheel.grabberSecL.setPosition(0);
                    sleep(500);
                    strafeEnc(.6, 3 , strafeDirections.Left);
                    fourwheel.grabberSecL.setPosition(.58);//grab
                    sleep(450);

                    fourwheel.blockArm.setPosition(1);
                    sleep(250);
                    strafeEnc(.6, 3 , strafeDirections.Right);
                    driveGyro(.5, 0, 30, driveDirection.Forward);
                    driveGyro(.7, 0,70, driveDirection.Forward);
                    driveGyro(.9, 0,100, driveDirection.Forward);
                    driveGyro(.7, 0,70, driveDirection.Forward);
                    driveGyro(.5, 0,30, driveDirection.Forward);
                    fourwheel.motorStop();
                    sleep(250);
                    strafeEnc(.6, 4 , strafeDirections.Left);
                    //sleep(250);
                    fourwheel.blockArm.setPosition(.62);
                    fourwheel.grabberSecL.setPosition(0);
                    sleep(500);
                    fourwheel.grabberSecL.setPosition(.57);//close
                    fourwheel.blockArm.setPosition(1);
                    strafeEnc(.6, 5 , strafeDirections.Right);
                    sleep(500);
                    driveGyro(.5, 0, 10, driveDirection.Backward);
                    driveGyro(.7, 0,45, driveDirection.Backward);
                    driveGyro(.9, 0,70, driveDirection.Backward);
                    driveGyro(.7, 0,50, driveDirection.Backward);
                    driveGyro(.5, 0,10, driveDirection.Backward);
                    fourwheel.motorStop();

                    fourwheel.blockArm.setPosition(.63);
                    fourwheel.grabberSecL.setPosition(0);
                    sleep(500);
                    strafeEnc(.6, 4, strafeDirections.Left);
                    fourwheel.grabberSecL.setPosition(.57);//grab
                    sleep(500);

                    fourwheel.blockArm.setPosition(1);
                    sleep(250);
                    strafeEnc(.6, 3 , strafeDirections.Right);
                    driveGyro(.5, 0, 10, driveDirection.Forward);
                    driveGyro(.7, 0,40, driveDirection.Forward);
                    driveGyro(.9, 0,60, driveDirection.Forward);
                    driveGyro(.7, 0,40, driveDirection.Forward);
                    driveGyro(.5, 0,10, driveDirection.Forward);
                    fourwheel.motorStop();
                    strafeEnc(.6, 8 , strafeDirections.Left);
                    //sleep(250);

                    fourwheel.blockArm.setPosition(.62);
                    fourwheel.grabberSecL.setPosition(0);
                    sleep(250);
                    fourwheel.grabberSecL.setPosition(.57);//close
                    fourwheel.blockArm.setPosition(1);
                    gyroTurn(-65, .8, turnDirections.RIGHT);
                    fourwheel.platformHolderR.setPosition(1);
                    fourwheel.platformHolderL.setPosition(0);
                    driveEnc(.5, 27, driveDirection.Backward);
                    fourwheel.platformHolderR.setPosition(.7);
                    fourwheel.platformHolderL.setPosition(.35);
                    sleep(250);
                    /*fourwheel.rightBackDrive.setPower(-1);
                    fourwheel.rightFrontDrive.setPower(-1);
                    fourwheel.leftFrontDrive.setPower(-.2);
                    fourwheel.leftBackDrive.setPower(-.2);
                    sleep(2500);*/
                    gyroTurn3(-90);
                    fourwheel.motorStop();
                    driveEnc(.8, 50, driveDirection.Backward);
                    fourwheel.platformHolderR.setPosition(1);
                    fourwheel.platformHolderL.setPosition(0);
                    sleep(250);
                    gyroTurn(-10, .8, turnDirections.RIGHT);
                    fourwheel.colorStick.setPower(-1.0);
                    sleep(1000);
                    fourwheel.colorStick.setPower(0);


                }
                else if (stoneConfigurations== skyStoneDetect.SkyStoneConfigurations.TWO_FIVE){
                    telemetry.addLine("Two five");
                    telemetry.update();
                    sleep(5000);

                }
                else {
                    telemetry.addLine("three six");
                    telemetry.update();
                    sleep(5000);

                }

            }
            else {

            }
        }




        }
    }

