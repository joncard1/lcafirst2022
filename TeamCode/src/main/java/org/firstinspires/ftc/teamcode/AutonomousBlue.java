package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous

public class AutonomousBlue extends LinearOpMode
{
    private ColorSensor color;
    private DcMotor frontLeft;
    private DcMotor frontRight;

    public void runOpMode()
    {
        waitForStart();

        // initialize sensor
        color = hardwareMap.get(ColorSensor.class,"color");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");


        frontRight.setTargetPosition(0);
        frontLeft.setTargetPosition(0);
        boolean isNotTurned = true;


        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRight.setTargetPosition(0);
        frontLeft.setTargetPosition(0);

        int currentPosRight = frontRight.getCurrentPosition();
        int currentPosLeft = frontLeft.getCurrentPosition();

        frontLeft.setPower(1);
        frontRight.setPower(1);


        while(opModeIsActive())
        {
            telemetry.addData("Red Value: ", color.red());
            telemetry.addData("Green Value: ", color.green());
            telemetry.addData("Blue Value: ", color.blue());
            telemetry.addData("Current Position Right: ", frontRight.getCurrentPosition());
            telemetry.addData("Current Position Left: ", frontLeft.getCurrentPosition());
            telemetry.addData("isBusy?: ", frontRight.isBusy());
            telemetry.update();


            //frontLeft.setTargetPosition(430);
            if(isNotTurned == true){
                frontRight.setTargetPosition(currentPosRight);
                frontLeft.setTargetPosition(currentPosLeft);
                currentPosRight += 15;
                currentPosLeft -= 15;

                if(color.blue() > color.red() + 40 && color.blue() > color.green() + 40){
                    isNotTurned = false;
                }
            }
            else{
                telemetry.addData("Turn complete","Move to next");
                telemetry.update();
                frontRight.setTargetPosition(currentPosRight - 23);
                frontLeft.setTargetPosition(currentPosLeft - 23);
                try{
                    Thread.sleep(2000);
                }
                catch(Exception e){

                }
                frontRight.setTargetPosition(currentPosRight - 15);
                frontLeft.setTargetPosition(currentPosLeft + 15);
            }

            /*
            if(color.blue() > color.red() + 100 && color.blue() > color.green() + 100){
                frontRight.setTargetPosition(20);
                frontLeft.setTargetPosition(20);
            }*/
        }


    }

}
