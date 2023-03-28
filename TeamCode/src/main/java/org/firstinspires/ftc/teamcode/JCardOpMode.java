package org.firstinspires.ftc.teamcode;
import java.lang.Math;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp

public class JCardOpMode extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor armControl;
    private CRServo arm1;
    private Servo arm2;
    private Servo claw;

    private boolean xPressed = false;
    private boolean yPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private int currentPos;

    protected ArmState armState = new ArmStationary();
    protected ElevationState elevationState = new ArmHolding();
    protected DriveState driveState = null;


    public void runOpMode(){
        //expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        armControl = hardwareMap.get(DcMotor.class, "armControl");
        arm1 = hardwareMap.get(CRServo.class,"arm1");
        arm2 = hardwareMap.get(Servo.class,"arm2");
        claw = hardwareMap.get(Servo.class,"claw");

        arm1.setDirection(Direction.FORWARD);
        arm1.setPower(0);

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
                telemetry.addData("Arm Power", arm1.getPower());
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
                armState = armState.executeState(hardwareMap, gamepad1);
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
    /*
    public void moveArm1(double throttle) {
        arm1.setPower(arm1.getPower() + throttle/10);
        telemetry.addData("Extending", arm1.getPower());
        telemetry.update();
    }
     */
    public void moveArm2(double throttle) {
        arm2.setPosition(arm2.getPosition() + 5);
    }
    public void moveClaw(double throttle) {
        claw.setPosition(claw.getPosition() + throttle/5);
    }

    protected interface IControlState<A extends IControlState> {
        public A executeState(HardwareMap map, Gamepad gamepad);
    }

    protected interface ArmState extends IControlState<ArmState> {}
    protected interface ElevationState extends IControlState<ElevationState> {}
    //protected interface GripperState extends IControlState<GripperState> {}
    protected interface DriveState extends IControlState<DriveState> {}

    protected class ArmStationary implements ArmState {

        @Override
        public ArmState executeState(HardwareMap map, Gamepad gamepad) {
            CRServo servo = map.get(CRServo.class, "arm1");
            ArmState returnValue;
            if (gamepad.x) {
                // moveArm1(1) from here
                servo.setDirection(Direction.FORWARD);
                servo.setPower(1);
                returnValue = new ArmExtending();
                JCardOpMode.this.telemetry.addData("State", "Arm Extending");
            } else if (gamepad.y) {
                // moveArm1(-1) from here
                servo.setDirection(Direction.REVERSE);
                servo.setPower(1);
                returnValue = new ArmRetracting();
                JCardOpMode.this.telemetry.addData("State", "Arm Retracting");
            } else {
                returnValue = this;
                JCardOpMode.this.telemetry.addData("State", "Stationary");
            }
            JCardOpMode.this.telemetry.addData("Direction", servo.getDirection());
            JCardOpMode.this.telemetry.addData("Power", servo.getPower());
            JCardOpMode.this.telemetry.update();

            return returnValue;
        }
    }

    protected class ArmExtending implements ArmState {

        @Override
        public ArmState executeState(HardwareMap map, Gamepad gamepad) {
            CRServo servo = map.get(CRServo.class, "arm1");
            // TODO: Also check limits
            if (!gamepad.x) {
                servo.setPower(0);
                return new ArmStationary();
            } else{
                return this;
            }
        }
    }

    protected class ArmRetracting implements ArmState {

        @Override
        public ArmState executeState(HardwareMap map, Gamepad gamepad) {
            CRServo servo = map.get(CRServo.class, "arm1");
            // TODO: Also check limits
            if (!gamepad.y) {
                servo.setPower(0);
                return new ArmStationary();
            } else {
                return this;
            }
        }
    }

    protected class ArmHolding implements ElevationState {

        @Override
        public ElevationState executeState(HardwareMap map, Gamepad gamepad) {
            if (gamepad.x) {
                JCardOpMode.this.raiseArm();
                return new RaisingArm();
            } else if (gamepad.y) {
                return new LoweringArm();
            }else {
                return this;
            }
        }
    }

    protected class RaisingArm implements ElevationState {

        @Override
        public ElevationState executeState(HardwareMap map, Gamepad gamepad) {
            if (!gamepad.x) {
                // TODO: Stop raising arm
                return new ArmHolding();
            } else {
                return this;
            }
        }
    }

    protected class LoweringArm implements ElevationState {

        @Override
        public ElevationState executeState(HardwareMap map, Gamepad gamepad) {
            if (!gamepad.y) {
                // TODO: Stop lowering arm
                return new ArmHolding();
            } else {
                return this;
            }
        }
    }
}
