package org.firstinspires.ftc.teamcode;
import java.lang.Math;
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

public class FTC2022OpMode extends LinearOpMode implements Gamepad.GamepadCallback {
    private Blinker expansion_Hub_2;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor armControl;
    private Servo arm1;
    private Servo arm2;
    private Servo claw;
    private String message;
    
    public void runOpMode(){
        expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        armControl = hardwareMap.get(DcMotor.class, "armControl");
        arm1 = hardwareMap.get(Servo.class,"arm1");
        arm2 = hardwareMap.get(Servo.class,"arm2");    
        claw = hardwareMap.get(Servo.class,"claw");
    
        try {
                Gamepad newGamepad = new Gamepad(this);
                newGamepad.copy(gamepad1);
                this.gamepad1 = newGamepad;
                Gamepad newGamepad2 = new Gamepad(this);
                newGamepad2.copy(gamepad2);
                this.gamepad2 = newGamepad2;
            } catch (Exception e) {
                telemetry.addData("Status", "Exception");
                telemetry.update();
            }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        
        this.message = "Running";

        // run until the end of the match (driver presses STOP)
        int controls = 0;
        while (opModeIsActive() == true) {
            telemetry.addData("Status", message);
            telemetry.update();
            if (this.gamepad1.b) {
                controls = 0;
                this.message = "driving control";
                telemetry.update();
            }
            
              //driving control mode
                //frontLeft.setPower(1);
                //frontRight.setPower(-1);
                leftWheel(this.gamepad1.left_stick_y/3);
                rightWheel(this.gamepad1.right_stick_y/3);
            
              
               
            
            if(this.gamepad2.x){
                raiseArm();
                moveArm1();
                moveArm2();
                telemetry.adddData("Status","tower control");
                telemetry.update();
            }
            else if (this.gamepad2.y){
                lowerArm();
                unmoveArm1();
                unmoveArm2();
                telemetry.addData("Status","tower control");
                telemetry.update();                  
            }
            else if (this.gamepad2.right_bumper){
                stopArm();
                telemetry.addData("Status","tower control");
                telemetry.update();
            }
        
        }
    }
    public void gamepadChanged(Gamepad gamepad) {
        message = "In changed";
    
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
    public void stopArm() {
        armControl.setPower(0.1);
    }
    public void raiseArm() {
        armControl.setPower(0.65);
    }
    public void moveArm1() {
        arm1.setPosition(arm1.getPosition() + .5);
    }
    public void moveArm2() {
        arm2.setPosition(arm2.getPosition() + .5);
    }
    public void moveClaw() {
        claw.setPosition(claw.getPosition() + .5);
    }
    public void lowerArm() {
        armControl.setPower(-0.25);
    }
    public void unmoveArm1() {
        arm1.setPosition(arm1.getPosition() - .5);
    }
    public void unmoveArm2() {
        arm2.setPosition(arm2.getPosition() - .5);
    }
    public void unmoveClaw() {
        claw.setPosition(claw.getPosition() - .5);
    }
    
    public void fastArm(){
        
        
    }

}
