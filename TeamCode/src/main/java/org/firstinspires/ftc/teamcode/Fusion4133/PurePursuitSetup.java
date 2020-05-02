package org.firstinspires.ftc.teamcode.Fusion4133;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptScanServo;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Vector;

public class PurePursuitSetup {

    DcMotor leftDrive;
    DcMotor rightDrive;
    int xLocation;
    int yLocation;


    int spacing = 6;


    int ticksPerInch;
    int wheelDiameter = 4;
    int ticksPerRev;
    BNO055IMU imu;

    double globalAngle, power = .30, correction;
    Orientation lastAngles = new Orientation();

    public enum robotSide{
        LEFT, RIGHT
    }


    public void hardwareInit(HardwareMap hwMap) {
        leftDrive = hwMap.dcMotor.get("ld");
        rightDrive = hwMap.dcMotor.get("rd");

        ticksPerInch = (int) Math.round(ticksPerRev / (wheelDiameter * Math.PI));
    }

    public void imuInit(HardwareMap hwMap) {
        imu = hwMap.get(BNO055IMU.class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu.initialize(parameters);
    }

    private double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

    public int getDist(int lStart, int rStart) {
        int lchange = (leftDrive.getCurrentPosition() - lStart) / ticksPerInch;
        int rchange = (rightDrive.getCurrentPosition() - rStart) / ticksPerInch;

        return (lchange + rchange) / 2;
    }

    public void getLocation(int lStart, int rStart) {
        int distance = getDist(lStart, rStart);
        double currentAngle = Math.toRadians(getAngle());
        yLocation += distance * Math.sin(currentAngle);
        xLocation += distance * Math.cos(currentAngle);
    }

    public double[][] getPath(double startX, double startY, double endX, double endY) {
        double x1 = startX;
        double x2 = endX;
        double y1 = startY;
        double y2 = endY;
        double xChange = x2 - x1;
        double yChange = y2 - y1;
        double dist = Math.hypot(xChange, yChange);
        int numPoints = (int) dist/spacing;

        double angle = Math.atan2(y2,x2);
        int currentPoint = 0;
        double[][] path = new double[numPoints][2];

        path[numPoints-1][0] = x2;
        path[numPoints-1][1] = y2;

        while(currentPoint < numPoints-2){
            path[currentPoint][0] = x1;
            path[currentPoint][1] = y1;
            x1 += x1 + (spacing * Math.cos(angle));
            y1 += y1 + (spacing * Math.sin(angle));
            currentPoint++;

        }

        return path;
    }
    public double[][] multiPointPath(double startX, double startY, double midX, double midY, double endX, double endY){
        double x1 = startX;
        double y1 = startY;
        double x2 = midX;
        double y2 = midY;
        double x3 = endX;
        double y3 = endY;
        int currentPoint = 0;

        double[][] path1 = getPath(x1, y1, x2, y2);
        double[][] path2 = getPath(x2, y2, x3, y3);
        int length = path1.length + path2.length;
        double[][] path = new double[length][2];

        while(currentPoint < path1.length-1){
            path[currentPoint][0] = path1[currentPoint][0];
            path[currentPoint][1] = path1[currentPoint][1];
            currentPoint++;

        }
        while(currentPoint < length-1){
            path[currentPoint][0] = path2[currentPoint][0];
            path[currentPoint][1] = path2[currentPoint][1];
            currentPoint++;

        }

        return path;
    }

    //Smooths the path
    //b = 0.75 to .98; a = 1-b; tolerance = 0.001
    public double[][] smoother(double[][] path, double a, double b, double tolerance){
        //copy array to new array
        double[][] newPath = path;
        double change = tolerance;

        //does some math I didn't feel like looking through
        while(change >= tolerance){
            change = 0.0;
            for(int i=1; i<path.length-1; i++)
                for(int j=0; j<path[i].length; j++)
                {
                    double aux = newPath[i][j];
                    newPath[i][j] += a * (path[i][j] - newPath[i][j]) + b *
                            (newPath[i-1][j] + newPath[i+1][j] - (2.0 * newPath[i][j]));
                    change += Math.abs(aux - newPath[i][j]);
                }
        }

        return newPath;
    }
    public void pathDist(double[][] finalPath){

        double xDistance = 0;
        double yDistance = 0;
        int endPoint = finalPath.length - 1;
        int point = 0;
        double [][]smoothedDist = new double[finalPath.length][2];
        while(point < endPoint){
            smoothedDist[point][0] = finalPath[point+1][0] - finalPath[point][0];
            smoothedDist[point][1]= finalPath[point+1][1] - finalPath[point][1];

            point++;

        }
    }
}
