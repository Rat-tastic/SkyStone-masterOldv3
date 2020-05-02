package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Autonomous(name="Rhett: Practice", group="2019")
public class RhettAutoTest extends RhettAutoSetup {
    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);
        waitForStart();

        zoomEnc(driveDirection.FORWARD, 1, 1120);

    }

}

