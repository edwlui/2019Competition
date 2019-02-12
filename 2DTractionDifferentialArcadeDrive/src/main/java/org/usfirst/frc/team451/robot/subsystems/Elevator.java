/*******************************************************************************
 * Copyright (c) 2018 Edward Lui. All Rights Reserved.
 *******************************************************************************/
package org.usfirst.frc.team451.robot.subsystems;

import org.usfirst.frc.team451.robot.OI;
//import org.usfirst.frc.team451.robot.commands.Drive;
import org.usfirst.frc.team451.robot.commands.ElevatorMove;

import com.ctre.phoenix.motorcontrol.can.*;
//import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.command.Subsystem;



public class Elevator extends Subsystem {
	public static double elevatorHeightMargin = 0.5;//in inches

	public static double countsPerRevolution = 1024; //counts per motor revolution based on specifications for mag encoder
	public static double gearReduction = 1; //motor revolutions per wheel revolutions
	public static double WheelDiameter = 1.7284; //inches
	public static double inchesPerCount = (Math.PI*WheelDiameter)/(countsPerRevolution*gearReduction); //the amount of inches that are covered in one count

	public static WPI_TalonSRX elevatorMotor = new WPI_TalonSRX(5);

    public Elevator() {
    }
 
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	
	/** Runs the user override commands from the x-box controller. PrintMethodData prints 
	 * information to the console.
	 */
	public static void RunUserOverride(boolean PrintMethodData){
		if (OI.mechBox.getY() > 0.000) {
			elevatorMotor.set(-1);
			if(PrintMethodData) System.out.println("Elevator UP (user)");
		   } else if (OI.mechBox.getY() < 0.000) {
			 elevatorMotor.set(1);
			 if(PrintMethodData) System.out.println("Elevator DOWN (user)");
		   }
	}

	/** Moves elevator based on TargetHeight (inches). PrintMethodData prints 
	 * information to the console.
	 */
	public static void MoveTowards(double TargetHeight, boolean PrintMethodData){
		if(elevatorMotor.getSelectedSensorPosition() > (TargetHeight+elevatorHeightMargin)/inchesPerCount){
			elevatorMotor.set(1);
			if(PrintMethodData) System.out.println("Elevator DOWN (preset)");
		}

		if(elevatorMotor.getSelectedSensorPosition() < (TargetHeight-elevatorHeightMargin)/inchesPerCount){
			elevatorMotor.set(-1);
			if(PrintMethodData) System.out.println("Elevator UP (preset)");
		}
	}


    
	@Override
	protected void initDefaultCommand() {
		
		setDefaultCommand(new ElevatorMove());
		
	}
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//ignore this
//	Spark frontLeftMotor = new Spark(RobotMap.frontLeftMotor);
//	Spark frontRightMotor = new Spark(RobotMap.frontRightMotor);
//	Spark backLeftMotor = new Spark(RobotMap.backLeftMotor);
//	Spark backRightMotor = new Spark(RobotMap.backRightMotor);

//	MecanumDrive Drive = new MecanumDrive(frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor);
//	
//	public void driveCartesian(double y, double x, double z, double gyroAngle) {
//	    Vector2d input = new Vector2d(y, x);
//	    input.rotate(-gyroAngle);
//
//	    double[] wheelSpeeds = new double[4];
//	    wheelSpeeds[MotorType.kFrontLeft.value] = input.x + input.y + z;
//	    wheelSpeeds[MotorType.kFrontRight.value] = input.x - input.y + z;
//	    wheelSpeeds[MotorType.kRearLeft.value] = -input.x + input.y + z;
//	    wheelSpeeds[MotorType.kRearRight.value] = -input.x - input.y + z;
//
//	    normalize(wheelSpeeds);
//
//	    frontLeftMotor.set(wheelSpeeds[MotorType.kFrontLeft.value] * maxOutput);
//	    frontRightMotor.set(wheelSpeeds[MotorType.kFrontRight.value] * maxOutput);
//	    backLeftMotor.set(wheelSpeeds[MotorType.kRearLeft.value] * maxOutput);
//	    backRightMotor.set(wheelSpeeds[MotorType.kRearRight.value] * maxOutput);
//		
//	}
//	
//	protected static void normalize(double[] wheelSpeeds) {
//	    double maxMagnitude = Math.abs(wheelSpeeds[0]);
//	    for (int i = 1; i < wheelSpeeds.length; i++) {
//	      double temp = Math.abs(wheelSpeeds[i]);
//	      if (maxMagnitude < temp) {
//	        maxMagnitude = temp;
//	      }
//	    }
//	    if (maxMagnitude > 1.0) {
//	      for (int i = 0; i < wheelSpeeds.length; i++) {
//	        wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
//	      }
//	    }
//	  }
//	
//    public void initDefaultCommand() {
//        //setDefaultCommand(new MySpecialCommand());
//    	setDefaultCommand(new Drive());
//    }

	    

