package org.firstinspires.ftc.teamcode.Fusion4133;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Fusion4133.mechanumHardwareTest;

public class skyStoneDetect  {
    private Vuforia2020 vuforia;

    private boolean isImageSavingEnabled;


    //private final int IMAGE_X = 0;
    //private final int IMAGE_Y = 500;
    //private final int IMAGE_WIDTH = 1000;
    //private final int IMAGE_HEIGHT = 200;

    private static final int Y_COORD = 380;

    private int stone4X, stone6X, stone5X;

    private static final int STONE_WIDTH = 100, STONE_HEIGHT = 25;

    private static final ColorPreset ACTIVE_YELLOW = ColorPreset.PURE_YELLOW; //Change these bad boys to calibrate
    private static final ColorPreset ACTIVE_BLACK = ColorPreset.PURE_BLACK;

    /**
     * Represents each of the three field conditions based on the dice roll.
     */
    public enum SkyStoneConfigurations {
        ONE_FOUR,
        TWO_FIVE,
        THREE_SIX,
    }

    private enum ColorPreset {
        PURE_YELLOW(255,255,0),
        PURE_BLACK(0,0,0),

        BASEMENT_YELLOW(188,112,0),
        BASEMENT_BLACK(5,4,3),

        SEYBOLDS_YELLOW(0,0,0),
        SEYBOLDS_BLACK(0,0,0);

        int r,g,b;

        ColorPreset(int r, int g, int b){
            this.r = r;
            this.g = g;
            this.b = b;
        }

    }


    public skyStoneDetect(boolean isImageSavingEnabled,  boolean redSide) {
        vuforia = new Vuforia2020();
        this.isImageSavingEnabled = isImageSavingEnabled;
        if(redSide){
            stone4X = 850;
            stone5X = 650;
            stone6X = 350;
        } else {
            stone4X = 850;
            stone5X = 550;
            stone6X = 250;
        }
    }

    /**
     * Runs the image processing code.
     * @return The location of the skystone in SkyStoneConfigurations form
     */
    public SkyStoneConfigurations look(){
        Bitmap vuBitmap = vuforia.getBitmap();
        //Bitmap croppedBitmap = Bitmap.createBitmap(vuBitmap, IMAGE_X, IMAGE_Y, IMAGE_WIDTH, IMAGE_HEIGHT);

        Bitmap stone4 = Bitmap.createBitmap(vuBitmap, stone4X, Y_COORD, STONE_WIDTH, STONE_HEIGHT);
        Bitmap stone5 = Bitmap.createBitmap(vuBitmap, stone5X, Y_COORD, STONE_WIDTH, STONE_HEIGHT);
        Bitmap stone6 = Bitmap.createBitmap(vuBitmap, stone6X, Y_COORD, STONE_WIDTH, STONE_HEIGHT);

        if (isImageSavingEnabled) {
            mechanumHardwareTest.saveImage(vuBitmap);
            mechanumHardwareTest.saveImage(stone4);
            mechanumHardwareTest.saveImage(stone5);
            mechanumHardwareTest.saveImage(stone6);


        }
        //Ratio is measured blackness to yellowness. higher ratio is more likeliness to be a skystone.

        double ratio4 = getColorness(stone4, ACTIVE_BLACK) / getColorness(stone4, ACTIVE_YELLOW);
        double ratio5 = getColorness(stone5, ACTIVE_BLACK) / getColorness(stone5, ACTIVE_YELLOW);
        double ratio6 = getColorness(stone6, ACTIVE_BLACK) / getColorness(stone6, ACTIVE_YELLOW);

        mechanumHardwareTest.addData("Skystone 4 Ratio", ratio4);
        mechanumHardwareTest.addData("Skystone 5 Ratio", ratio5);
        mechanumHardwareTest.addData("Skystone 6 Ratio", ratio6);


        mechanumHardwareTest.updateOpLogger();

        if (ratio4 > ratio5 && ratio4 > ratio6) {
            return SkyStoneConfigurations.ONE_FOUR;

        } else if (ratio5 > ratio4 && ratio5 > ratio6) {
            return SkyStoneConfigurations.TWO_FIVE;
        } else {
            return SkyStoneConfigurations.THREE_SIX;
        }

    }


    private double getColorness(Bitmap bitmap, ColorPreset colorPreset){//finds the closeness of a region to a color
        int color;

        int r, g, b;

        double distanceSum = 0;
        int pixels = bitmap.getWidth()*bitmap.getHeight();

        for(int i = 0; i < bitmap.getWidth(); i++){
            for(int j = 0; j < bitmap.getHeight(); j++) {
                color = bitmap.getPixel(i, j);
                r = Color.red(color);
                g = Color.green(color);
                b = Color.blue(color);
                distanceSum += getColorDistance(r,g,b,colorPreset.r, colorPreset.g, colorPreset.b);//todo refactor
            }
        }

        double averageDistance = distanceSum/pixels;

        if(averageDistance != 0){
            return 1/averageDistance;
        } else {
            return Double.POSITIVE_INFINITY;
        }

        //return 1/averageDistance; //average
    }

    /**
     * Gets the distance between two colors via 3 dimensional distance formula.
     * @param r Actual red value
     * @param g Actual green value
     * @param b Actual blue value
     * @param targetR Target red value
     * @param targetG Target green value
     * @param targetB Target blue value
     * @return Distance between actual color and target color
     */
    private double getColorDistance(int r, int g, int b, int targetR, int targetG, int targetB) {//does the actual mAthS
        int rDifference = r - targetR;
        int gDifference = g - targetG;
        int bDifference = b - targetB;

        int rDifferenceSquared = (int) Math.pow(rDifference, 2);
        int gDifferenceSquared = (int) Math.pow(gDifference, 2);
        int bDifferenceSquared = (int) Math.pow(bDifference, 2);

        int sum = rDifferenceSquared + gDifferenceSquared + bDifferenceSquared;

        double distance = Math.sqrt(sum);

        return distance;
    }

}
