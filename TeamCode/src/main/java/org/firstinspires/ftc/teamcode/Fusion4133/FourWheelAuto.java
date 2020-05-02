package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Fusion on 10/23/2018.
 */
@Autonomous(name="Icarus: Auto", group="Icarus")
public class FourWheelAuto extends FourWheelAutoSetup {

    @Override
    public void runOpMode() {

        fourWheel.hardwareInit(hardwareMap);
        fourWheel.servoInit(hardwareMap);
        fourWheel.imuInit(hardwareMap);
        fourWheel.autoInit(hardwareMap);
        int goldPos = 0;


        ElapsedTime operationTime = new ElapsedTime();
        ElapsedTime autoRunTime = new ElapsedTime();

        while (!isStopRequested() && !fourWheel.imu.isGyroCalibrated()) {
            fusionSleep(50);
            idle();
        }
        telemetry.log().add("Select Options");
        telemetry.update();

        selectOptions();

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", fourWheel.imu.getCalibrationStatus().toString());
        telemetry.update();

        telemetry.log().add("Waiting For Start");
        telemetry.update();
        boolean flag = true;

        waitForStart();
        sleep(waitStart.getValue() * 1000);
        while (opModeIsActive() && flag == true && !isStopRequested()) {

            autoRunTime.reset();


            telemetry.log().add("Program Started");
            if (startHanging.getValue().equals("hanging")) {
                while (fourWheel.leadScrewLander.getCurrentPosition() >= -8854 && opModeIsActive()) {
                    fourWheel.leadScrewLander.setPower(-1.0);
                }
                fourWheel.leadScrewLander.setPower(0.0);
                fusionSleep(500);

                fourWheel.leadScrewLander.setPower(0.8);


                
                /*fourWheel.leadScrewLander.setPower(-0.8);
                fusionSleep(4000);
                fourWheel.leadScrewLander.setPower(0.0);
                fusionSleep(500);
                fourWheel.leadScrewLander.setPower(0.8);
                fusionSleep(3900);
                fourWheel.leadScrewLander.setPower(0.0);*/
            }//hang

            if (sample.getValue() == true) {
                fourWheel.phoneServo.setPosition(fourWheel.phonesPosDown);
                fourWheel.leadScrew.setPower(.8);
                fusionSleep(1500);
                fourWheel.leadScrew.setPower(0.0);



                fusionSleep(2000);
                while (fourWheel.leadScrewLander.getCurrentPosition() <= -100 && opModeIsActive()) {

                }
                /*
                /*fourWheel.leadScrewLander.setPower(0.0);
                /*double xPos = fourWheel.detector.getXPosition();
                if (xPos == 0) {
                    driveEnc(.8, 4, driveDirections.FORWARD);
                    //fourWheel.collectionServoRight.setPosition(.82);
                    fourWheel.collectionServoRight.setPower(-.75);
                    fusionSleep(500);
                    fourWheel.collectionServoRight.setPower(0.0);
                    spinEnc(.5, 25, turnDirections.RIGHT);
                    collectDriveEnc(.4, 34, driveDirections.FORWARD);
                    goldPos = 1;
                    /*fourWheel.collectionServoRight.setPower(.5);
                    fusionSleep(1500);
                    fourWheel.collectionServoRight.setPower(0.0);*/
                /*
                } else if (xPos > 0 && xPos < 200) {
                    //fourWheel.collectionServoRight.setPosition(.82);
                    fourWheel.collectionServoRight.setPower(-.75);
                    fusionSleep(500);
                    fourWheel.collectionServoRight.setPower(0.0);
                    fusionSleep(500);
                    collectDriveEnc(.5, 38, driveDirections.FORWARD);
                    goldPos = 2;

                } else if (xPos > 200) {
                    driveEnc(.8, 4, driveDirections.FORWARD);
                    //fourWheel.collectionServoRight.setPosition(.82);
                    fourWheel.collectionServoRight.setPower(-.75);
                    fusionSleep(500);
                    fourWheel.collectionServoRight.setPower(0.0);
                    spinEnc(.6, 40, turnDirections.LEFT);
                    collectDriveEnc(.4, 37, driveDirections.FORWARD);
                    goldPos = 3;
                    /*fourWheel.collectionServoRight.setPower(.5);
                    fusionSleep(1500);
                    fourWheel.collectionServoRight.setPower(0.0);*/


            /*
                fourWheel.phoneServo.setPosition(fourWheel.phonesPosUp);
                //fourWheel.collectionServoRight.setPosition(.24);
                fourWheel.collectionServoRight.setPower(.75);
                fusionSleep(1000);
                fourWheel.collectionServoRight.setPower(0.0);
                //fusionSleep(1000);
                flag = false;
                fourWheel.detector.disable();
            }//sampiling
        */
                if (scoreTeamMarker.getValue() == true) {
                    if (startHook.getValue().equals("ballSide")) {
                        if (goldPos == 1) {

                            driveEnc(.5, 10, driveDirections.BACKWARD);
                            spinEnc(.5, 130, turnDirections.LEFT);
                            driveEnc(.7, 100, driveDirections.FORWARD);
                            spinEnc(.5, 60, turnDirections.LEFT);
                            driveEnc(.7, 50, driveDirections.FORWARD);
                            if (allianceColor.getValue().equals("red")) {
                                driveToColor("Red", .3, driveDirections.FORWARD);

                            } else if (allianceColor.getValue().equals("blue")) {
                                driveToColor("Blue", .3, driveDirections.FORWARD);
                            }
                            driveEnc(.5, 10, driveDirections.FORWARD);
                            spinEnc(.5, 10, turnDirections.LEFT);
                            //fourWheel.collectionServoRight.setPosition(.85);
                            fourWheel.markerServo.setPosition(fourWheel.dropMarker);
                            sleep(500);
                            driveEnc(.5, 10, driveDirections.BACKWARD);
                            fourWheel.fingerServo.setPosition(.0);

                            if (doubleSample.getValue() == true) {
                                spinEnc(.5, 195, turnDirections.LEFT);
                                driveEnc(.5, 20, driveDirections.FORWARD);


                            }
                            if ((allianceColor.getValue().equals("blue") && parkCrater.getValue().equals("RedCrater")) ||
                                    (allianceColor.getValue().equals("red") && parkCrater.getValue().equals("BlueCrater"))) {

                            } else {

                                driveToColor2("Black", .8, driveDirections.BACKWARD);
                                fourWheel.fingerServo.setPosition(.0);
                            }


                        } else if (goldPos == 2) {

                            driveEnc(.5, 10, driveDirections.BACKWARD);
                            spinEnc(.5, 110, turnDirections.LEFT);
                            driveEnc(.7, 90, driveDirections.FORWARD);
                            spinEnc(.5, 55, turnDirections.LEFT);
                            driveEnc(.7, 25, driveDirections.FORWARD);
                            if (allianceColor.getValue().equals("red")) {
                                driveToColor("Red", .3, driveDirections.FORWARD);
                            } else if (allianceColor.getValue().equals("blue")) {
                                driveToColor("Blue", .3, driveDirections.FORWARD);
                            }
                            spinEnc(.5, 15, turnDirections.LEFT);
                            driveEnc(.5, 10, driveDirections.FORWARD);
                            //fourWheel.collectionServoRight.setPosition(.85);

                            fourWheel.markerServo.setPosition(fourWheel.dropMarker);
                            sleep(500);
                            driveEnc(.5, 10, driveDirections.BACKWARD);
                            fourWheel.fingerServo.setPosition(.0);
                            if (doubleSample.getValue() == true) {
                                spinEnc(.5, 145, turnDirections.LEFT);
                                driveEnc(.5, 20, driveDirections.FORWARD);

                            }
                            if ((allianceColor.getValue().equals("blue") && parkCrater.getValue().equals("RedCrater")) ||
                                    (allianceColor.getValue().equals("red") && parkCrater.getValue().equals("BlueCrater"))) {

                            } else {
                                driveToColor2("Black", .8, driveDirections.BACKWARD);
                                fourWheel.fingerServo.setPosition(.0);
                            }


                        } else if (goldPos == 3) {
                            spinEnc(.5, 85, turnDirections.LEFT);
                            driveEnc(.7, 65, driveDirections.FORWARD);
                            spinEnc(.5, 60, turnDirections.LEFT);
                            driveEnc(.7, 30, driveDirections.FORWARD);
                            if (allianceColor.getValue().equals("red")) {
                                driveToColor("Red", .3, driveDirections.FORWARD);
                            } else if (allianceColor.getValue().equals("blue")) {
                                driveToColor("Blue", .3, driveDirections.FORWARD);
                            }
                            driveEnc(.5, 10, driveDirections.FORWARD);
                            fourWheel.markerServo.setPosition(fourWheel.dropMarker);
                            sleep(500);
                            driveEnc(.5, 10, driveDirections.BACKWARD);
                            fourWheel.fingerServo.setPosition(.0);

                            if (doubleSample.getValue() == true) {
                                spinEnc(.5, 110, turnDirections.LEFT);
                                driveEnc(.5, 30, driveDirections.FORWARD);

                            }
                            if ((allianceColor.getValue().equals("blue") && parkCrater.getValue().equals("RedCrater")) ||
                                    (allianceColor.getValue().equals("red") && parkCrater.getValue().equals("BlueCrater"))) {

                            } else {
                                driveToColor2("Black", .8, driveDirections.BACKWARD);
                                fourWheel.fingerServo.setPosition(.0);
                            }


                        }
                    } else if (startHook.getValue().equals("blockSide")) {
                        if (goldPos == 1) {
                            driveEnc(0.5, 20, driveDirections.FORWARD);
                            spinEnc(.5, 50, turnDirections.LEFT);
                            if (allianceColor.getValue().equals("red")) {
                                driveToColor("Red", .3, driveDirections.FORWARD);
                            } else if (allianceColor.getValue().equals("blue")) {
                                driveToColor("Blue", .3, driveDirections.FORWARD);
                            }
                            driveEnc(.7, 30, driveDirections.FORWARD);

                            //fourWheel.collectionServoRight.setPosition(.85);
                            fourWheel.markerServo.setPosition(fourWheel.dropMarker);
                            driveEnc(.7, 30, driveDirections.BACKWARD);
                            sleep(500);

                            if ((allianceColor.getValue().equals("blue") && parkCrater.getValue().equals("RedCrater"))) {
                                spinEnc(.7, 15, turnDirections.LEFT);
                                driveEnc(.7, 25, driveDirections.FORWARD);
                                spinEnc(.7, 90, turnDirections.LEFT);
                                driveEnc(.7, 45, driveDirections.FORWARD);
                                spinEnc(.7, 40, turnDirections.LEFT);
                                driveToColor("Black", .8, driveDirections.FORWARD);
                            } else if ((allianceColor.getValue().equals("red") && parkCrater.getValue().equals("BlueCrater"))) {

                            }


                        } else if (goldPos == 2) {

                            if (allianceColor.getValue().equals("red")) {
                                driveToColor("Red", .4, driveDirections.FORWARD);
                            } else if (allianceColor.getValue().equals("blue")) {
                                driveToColor("Blue", .4, driveDirections.FORWARD);
                            }
                            driveEnc(.5, 10, driveDirections.FORWARD);
                            fourWheel.markerServo.setPosition(fourWheel.dropMarker);

                            sleep(500);
                            driveEnc(.5, 10, driveDirections.BACKWARD);

                            if (doubleSample.getValue() == true) {
                                spinEnc(.5, 89, turnDirections.RIGHT);
                                driveEnc(.6, 28, driveDirections.FORWARD);
                                spinEnc(.5, 60, turnDirections.RIGHT);
                                driveEnc(.7, 40, driveDirections.FORWARD);
                                spinEnc(.5, 65, turnDirections.RIGHT);
                                //fourWheel.collectionServoRight.setPosition(.85);
                                fourWheel.collectionServoRight.setPower(-.75);
                                fusionSleep(1000);
                                fourWheel.collectionServoRight.setPower(0.0);
                                driveEnc(.7, 85, driveDirections.FORWARD);
                                spinEnc(.5, 90, turnDirections.LEFT);
                                //fourWheel.collectionServoRight.setPosition(.24);
                                driveToColor("black", .3, driveDirections.FORWARD);
                                fourWheel.collectionServoRight.setPower(.75);
                                fusionSleep(1500);
                                fourWheel.collectionServoRight.setPower(0.0);
                                fourWheel.flapServo.setPower(0.0);

                            }
                            if ((allianceColor.getValue().equals("blue") && parkCrater.getValue().equals("RedCrater"))) {
                                driveEnc(.8, 57, driveDirections.BACKWARD);
                                spinEnc(.5, 115, turnDirections.LEFT);
                                driveEnc(.8, 48, driveDirections.FORWARD);
                                spinEnc(.5, 45, turnDirections.LEFT);
                                driveToColor("Black", .8, driveDirections.FORWARD);

                            } else if ((allianceColor.getValue().equals("red") && parkCrater.getValue().equals("BlueCrater"))) {
                                driveEnc(.8, 48, driveDirections.BACKWARD);
                                spinEnc(.5, 115, turnDirections.LEFT);
                                driveEnc(.8, 48, driveDirections.FORWARD);
                                spinEnc(.5, 35, turnDirections.LEFT);
                                driveToColor("Black", .8, driveDirections.FORWARD);
                            }

                        } else if (goldPos == 3) {
                            driveEnc(0.5, 30, driveDirections.FORWARD);
                            spinEnc(.5, 60, turnDirections.RIGHT);
                            if (allianceColor.getValue().equals("red")) {
                                driveToColor("Red", .3, driveDirections.FORWARD);
                            } else if (allianceColor.getValue().equals("blue")) {
                                driveToColor("Blue", .3, driveDirections.FORWARD);
                            }
                            driveEnc(.5, 10, driveDirections.FORWARD);
                            fourWheel.markerServo.setPosition(fourWheel.dropMarker);

                            sleep(500);
                            driveEnc(.5, 10, driveDirections.BACKWARD);
                            if (doubleSample.getValue() == true) {

                            }
                            if ((allianceColor.getValue().equals("blue") && parkCrater.getValue().equals("RedCrater"))) {
                                spinEnc(.7, 190, turnDirections.LEFT);
                                driveEnc(.9, 80, driveDirections.FORWARD);
                                driveToColor("Black", .5, driveDirections.FORWARD);

                            } else if ((allianceColor.getValue().equals("red") && parkCrater.getValue().equals("BlueCrater"))) {
                                spinEnc(.7, 190, turnDirections.LEFT);
                                driveEnc(.9, 80, driveDirections.FORWARD);
                                driveToColor("Black", .5, driveDirections.FORWARD);
                            }

                        }

                    } //marker + depot

                }
            }
        }
    }}

