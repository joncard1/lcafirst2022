package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp

public class FTC2022OpMode extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor armControl;
    private CRServo arm1;
    private Servo claw;
    private String message;

    boolean movingArm = false;
    boolean unmovingArm = false;

    public void runOpMode(){
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        armControl = hardwareMap.get(DcMotor.class, "armControl");
        arm1 = hardwareMap.get(CRServo.class,"arm1");
        claw = hardwareMap.get(Servo.class,"claw");

        try {
            Gamepad newGamepad = new Gamepad();
            newGamepad.copy(gamepad1);
            this.gamepad1 = newGamepad;
            Gamepad newGamepad2 = new Gamepad();
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

        // run until the end of the match (driver presses STOP)

        while (opModeIsActive() == true) {

            leftWheel(this.gamepad1.left_stick_y/3);
            rightWheel(this.gamepad1.right_stick_y/3);

            if(this.gamepad2.x){
                moveArm1();
                movingArm = true;
            }
            else if (!this.gamepad2.x){
                stopMoveArm();
                movingArm = false;
            }

            if (this.gamepad2.y){
                unmoveArm1();
                unmovingArm = true;
            }
            else if (!this.gamepad2.y){
                stopUnmoveArm();
                unmovingArm = false;
            }

            if (this.gamepad2.right_bumper){
                moveClaw();
                telemetry.addData("Status",claw.getPosition());
                telemetry.update();
                //claw.setPosition();
            }
            if (this.gamepad2.left_bumper){
                unmoveClaw();
                telemetry.addData("Status",claw.getPosition());
                telemetry.update();
                //claw.setPosition();
            }

            if (this.gamepad2.a){
                raiseArm();
            }
            else if (!this.gamepad2.a){
                stopArm();
            }

            if (this.gamepad2.b){
                lowerArm();
            }
            else if (!this.gamepad2.b){
                stopArm();
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
        armControl.setPower(0.55);
    }
    public void stopMoveArm(){
        if (movingArm == true)
        {
            arm1.setPower(0);
        }
    }
    public void stopUnmoveArm(){
        if (unmovingArm == true)
        {
            arm1.setPower(0);
        }
    }
    public void moveArm1() {
        if (movingArm == false)
        {
            arm1.setPower(0.7);
        }
    }
    public void moveClaw() {
        claw.setPosition(claw.getPosition() + .5);
    }
    public void lowerArm() {
        armControl.setPower(-0.25);
    }
    public void unmoveArm1() {
        if (unmovingArm == false)
        {
            arm1.setPower(-0.7);
        }
    }
    public void unmoveClaw() {
        claw.setPosition(claw.getPosition() - .5);
    }

    public void fastArm(){


    }

}
