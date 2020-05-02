package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Fusion on 11/8/2017.
 */
@Disabled
@TeleOp (name = "Hadron : Displsy", group = "Hadron")
public class HadronTeleOpTestENC extends OpMode {
  MecanumHardwareSetup hadron = new MecanumHardwareSetup();

  @Override
  public void init() {
    hadron.hardwareInit(hardwareMap);
  }

  @Override
  public void stop() {
    super.stop();
  }

  @Override
  public void loop() {
    double xVal;
    double yVal;
    double zVal;
    double csInc = .001;

    xVal = gamepad1.left_stick_x;
    yVal = -gamepad1.left_stick_y;
    zVal = gamepad1.right_stick_x;

    hadron.leftFrontDrive.setPower(yVal + xVal + zVal);
    hadron.leftBackDrive.setPower(yVal - xVal + zVal);
    hadron.rightFrontDrive.setPower(yVal - xVal - zVal);
    hadron.rightBackDrive.setPower(yVal + xVal - zVal);
    hadron.liftDrive.setPower(gamepad2.left_stick_y);

    hadron.collectionMotorLeft.setPower(gamepad1.right_trigger);    //Next four lines for opening and closing the collection motors.
    hadron.collectionMotorRight.setPower(gamepad1.right_trigger);
    hadron.collectionMotorLeft.setPower(-gamepad1.left_trigger);
    hadron.collectionMotorRight.setPower(-gamepad1.left_trigger);

    if (gamepad1.right_bumper){
      hadron.liftDrive.setPower(1.0);
    }
    else if (gamepad1.left_bumper){
      hadron.liftDrive.setPower(-1.0);
    }
    else {
      hadron.liftDrive.setPower(0.0);
    }

    telemetry.addData("liftENC",  "Value: %7d", hadron.liftDrive.getCurrentPosition());
    // telemetry.addData("gyro",hadron.imu.getAngularOrientation());


    telemetry.addData("FrontLeftENC",  "Value: %7d", hadron.leftFrontDrive.getCurrentPosition());
    telemetry.addData("BackLeftENC",  "Value: %7d", hadron.leftBackDrive.getCurrentPosition());
    telemetry.addData("FrontRightENC",  "Value: %7d", hadron.rightFrontDrive.getCurrentPosition());
    telemetry.addData("Back" + "RightENC",  "Value: %7d", hadron.rightBackDrive.getCurrentPosition());
  }
}
