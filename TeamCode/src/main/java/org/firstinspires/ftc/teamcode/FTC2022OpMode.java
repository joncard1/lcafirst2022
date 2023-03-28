package org.firstinspires.ftc.teamcode;
import java.lang.Math;
import org.firstinspires.ftc.*;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp

public class FTC2022OpMode extends LinearOpMode {
    private Blinker expansion_Hub_2;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor armControl;
    private Servo arm1;
    private Servo arm2;
    private Servo claw;

    private boolean xPressed = false;
    private boolean yPressed = false;
    private int currentPos;

    public void runOpMode(){
        //expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        armControl = hardwareMap.get(DcMotor.class, "armControl");
        arm1 = hardwareMap.get(Servo.class,"arm1");
        arm2 = hardwareMap.get(Servo.class,"arm2");
        claw = hardwareMap.get(Servo.class,"claw");

        try {
            Gamepad newGamepad = new Gamepad();
            newGamepad.copy(gamepad1);
            this.gamepad1 = newGamepad;
        } catch (Exception e) {
            telemetry.addData("Status", "Exception");
            telemetry.update();
        }

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        /*telemetry.update();
        if(armControl.getCurrentPosition() < 0){
            currentPos = armControl.getCurrentPosition();
        }
        else{
            currentPos = 10;
        }*/


        telemetry.addData("Status", "arm position: " + currentPos);
        telemetry.update();
        armControl.setTargetPosition(currentPos);

        armControl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armControl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //armControl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


        //armControl.setTargetPosition(armControl.getCurrentPosition());

        currentPos = armControl.getCurrentPosition();
        armControl.setPower(1.0);

        // run until the end of the match (driver presses STOP)
        int controls = 0;

        telemetry.addData("Position", "current position: " + armControl.getCurrentPosition() + ", target pos: " + armControl.getTargetPosition());
        telemetry.update();

        while (opModeIsActive() == true) {
            if (this.gamepad1.b){
                telemetry.addData("Position", "current position: " + armControl.getCurrentPosition() + ", target pos: " + armControl.getTargetPosition() + ", power: " + armControl.getPower());
                telemetry.update();
            }
            if (this.gamepad1.a) {
                if(controls == 0){
                    controls = 1;
                    telemetry.addData("Status","tower control");
                    telemetry.update();
                }
                else{
                    controls = 0;
                    telemetry.addData("Status","driving control");
                    telemetry.update();
                }
            }
            if (controls == 0) { //driving control mode
                //frontLeft.setPower(1);
                //frontRight.setPower(-1);

                if(this.gamepad1.x){
                    if(xPressed == false){
                        raiseArm();
                        xPressed = true;
                    }
                }
                else if(this.gamepad1.x != true){
                    xPressed = false;
                }
                if (this.gamepad1.y){
                    if(yPressed == false){
                        lowerArm();
                        yPressed = true;
                    }
                }
                else{
                    yPressed = false;
                }

                leftWheel(this.gamepad1.left_stick_y/3);
                rightWheel(this.gamepad1.right_stick_y/3);
            }
            if (controls == 1) { //tower control mode
                if (this.gamepad1.left_bumper) {
                    leftWheel(0.2);
                    rightWheel(-0.2);
                } else if (this.gamepad1.right_bumper) {
                    leftWheel(-0.2);
                    rightWheel(0.2);
                } else {
                    leftWheel(0);
                    rightWheel(0);
                }
                moveArm1(this.gamepad1.left_stick_y);
                moveArm2(this.gamepad1.right_stick_y);
                moveClaw(this.gamepad1.left_stick_x);
            }
        }
    }
    public void gamepadChanged(Gamepad gamepad) {

    }
    public void stopMoving() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
    }
    public void leftWheel(double throttle) {
        if(throttle > 0){
            frontRight.setDirection(Direction.FORWARD);
        } else {
            frontRight.setDirection(Direction.REVERSE);
        }
        frontRight.setPower(Math.abs(throttle));


    }
    public void rightWheel(double throttle) {
        if(throttle > 0){
            frontLeft.setDirection(Direction.REVERSE);
        } else {
            frontLeft.setDirection(Direction.FORWARD);
        }
        frontLeft.setPower(Math.abs(throttle));
    }
    //good code below
    //raise and lower the main arm
    public void raiseArm() {
        currentPos += 20;
        armControl.setTargetPosition(currentPos);
    }
    public void lowerArm() {
        currentPos -= 20;
        armControl.setTargetPosition(currentPos);
    }
    //methods for moving the servos on the arm
    public void moveArm1(double throttle) {
        arm1.setPosition(arm1.getPosition() + throttle/5);
    }
    public void moveArm2(double throttle) {
        arm2.setPosition(arm2.getPosition() + throttle/5);
    }
    public void moveClaw(double throttle) {
        claw.setPosition(claw.getPosition() + throttle/5);
    }


}