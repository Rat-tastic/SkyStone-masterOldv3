package org.firstinspires.ftc.teamcode.Fusion4133;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Fusion on 10/23/2018.
 */
@Disabled
@Autonomous(name="Icarus: AutoTest", group="Icarus")
public class FourWheelAutoTest extends FourWheelAutoSetup {

    @Override
    public void runOpMode() {

        fourWheel.hardwareInit(hardwareMap);
        //  fourWheel.servoInit(hardwareMap);
        fourWheel.imuInit(hardwareMap);

        ElapsedTime operationTime = new ElapsedTime();
        ElapsedTime autoRunTime = new ElapsedTime();
        waitForStart();
        //double voltStart = (float) fourWheel.potentiometer.getVoltage();
        //telemetry.addData("startVolt", voltStart);

        //double voltEnd = (float) fourWheel.potentiometer.getMaxVoltage();
        //telemetry.addData("EndVolt", voltEnd);

        //telemetry.addData("differenceVolt", voltEnd - voltStart);
        telemetry.update();
        sleep(2000);
        /* while (!isStopRequested()&& !fourWheel.imu.isGyroCalibrated())*/
        while (opModeIsActive()){

            fourWheel.collectionServoRight.setPower(-.5);
            fusionSleep(1000);
            fourWheel.collectionServoRight.setPower(0.0);
            fourWheel.collectionServoRight.setPower(.5);
            fusionSleep(1000);
            fourWheel.collectionServoRight.setPower(0.0);

            //double voltReading = (float) fourWheel.potentiometer.getVoltage();
            //double percentTurned = ((voltReading-1.1)/2.21) *100;
            //telemetry.addData("rawVal", "voltage: "+ voltReading);
            //telemetry.addData("percentTurned", percentTurned + "%");
        }

    }
}