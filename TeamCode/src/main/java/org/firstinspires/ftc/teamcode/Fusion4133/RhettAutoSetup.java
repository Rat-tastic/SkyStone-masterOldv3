package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;


public abstract class RhettAutoSetup extends LinearOpMode {

    RhettHardwareSetup robot = new RhettHardwareSetup();
    ElapsedTime moveTime = new ElapsedTime();

    public void setZoom(double power){
        robot.LeftDrive.setPower(power);
        robot.RightDrive.setPower(power);
    }
    public void turnZoom(double power){
        robot.LeftDrive.setPower(-power);
        robot.RightDrive.setPower(power);
    }

    public enum driveDirection{
        FORWARD,BACK
    }
    public enum turnDirection{
        LEFT,RIGHT
    }
    public void goZoom(driveDirection iDir,double zoom, long time){
        moveTime.reset();
        while(iDir == driveDirection.FORWARD && moveTime.milliseconds() <= time*1000){
            setZoom(zoom);
        }
        while(iDir == driveDirection.BACK && moveTime.milliseconds() <= time*1000){
            setZoom(-zoom);
        }

        setZoom(0.0);
    }
    public void angledZoom(turnDirection iDir,double zoom, long time){
        moveTime.reset();
        while(iDir == turnDirection.LEFT && moveTime.milliseconds() <= time*1000){
            turnZoom(zoom);
        }
        while(iDir == turnDirection.RIGHT && moveTime.milliseconds() <= time*1000){
            turnZoom(-zoom);
        }

        setZoom(0.0);
    }
    public void zoomEnc(driveDirection iDir, double zoom, long encValue){
        long leftZoomDistance = robot.LeftDrive.getCurrentPosition();
        long leftZoomForward = leftZoomDistance - encValue;
        long rightZoomDistance = robot.RightDrive.getCurrentPosition();
        long rightZoomForward = rightZoomDistance - encValue;
        long leftZoomBack = leftZoomDistance + encValue;
        long rightZoomBack = rightZoomDistance + encValue;

        if(iDir == driveDirection.FORWARD) {
            setZoom(zoom);
            while(opModeIsActive() &&
                    robot.LeftDrive.getCurrentPosition() > leftZoomForward &&
                    robot.RightDrive.getCurrentPosition() > rightZoomForward){
            }
        }
        else if(iDir == driveDirection.BACK){
            setZoom(-zoom);
            while(opModeIsActive() &&
                    robot.LeftDrive.getCurrentPosition() < leftZoomBack &&
                    robot.RightDrive.getCurrentPosition() < rightZoomBack) {
            }
        }

        setZoom(0.0);
    }
}
