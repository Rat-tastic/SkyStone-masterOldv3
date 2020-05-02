package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;
@TeleOp(name="Training: TeleOp", group="training")
public class training extends OpMode {
    TrainingRobot robot = new TrainingRobot();
    ColorSensor sensorColor;
    DistanceSensor sensorDistance;


    @Override
    public void init() {
        robot.robotInit(hardwareMap);
        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");
    }

    @Override
    public void loop() {
        robot.frontLeft.setPower(-gamepad1.left_stick_y);
        robot.backLeft.setPower(-gamepad1.left_stick_y);
        robot.frontRight.setPower(-gamepad1.right_stick_y);
        robot.backRight.setPower(-gamepad1.right_stick_y);
        telemetry.addData("Distance (cm)",
                String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
        telemetry.addData("Alpha", sensorColor.alpha());
        telemetry.addData("Red  ", sensorColor.red());
        telemetry.addData("Green", sensorColor.green());
        telemetry.addData("Blue ", sensorColor.blue());

    }
}
