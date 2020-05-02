package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="camera : CameraTest", group="Camera")
public class TestCamera extends OpMode {
    //hwMap Pull from fourwheel Hardware setup.
    FourWheelHardwareSetup fourWheel = new FourWheelHardwareSetup();

    @Override
    public void init () {
        // Save reference to Hardware map

    }
    @Override
    //this is just so when we hit the stop button at any point in the program the robot will stop
    public void stop() {
        super.stop();
    }

    @Override
    //runnable code
    public void loop () {

    }
}
