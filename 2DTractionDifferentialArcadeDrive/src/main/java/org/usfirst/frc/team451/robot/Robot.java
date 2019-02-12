/*******************************************************************************
 * Copyright (c) 2018 Edward Lui. All Rights Reserved.
 *******************************************************************************/
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team451.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team451.robot.commands.ClimberMove;
import org.usfirst.frc.team451.robot.subsystems.Climber;
import org.usfirst.frc.team451.robot.subsystems.CameraServo;
import org.usfirst.frc.team451.robot.subsystems.Claw;
import org.usfirst.frc.team451.robot.subsystems.DriveTrain;
import org.usfirst.frc.team451.robot.subsystems.Elevator;
import org.usfirst.frc.team451.robot.subsystems.LineTracker;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	//Mechanisms and the like
	public static DriveTrain DriveTrain = new DriveTrain();
	public static Claw Claw = new Claw();
	public static LineTracker LineTracker = new LineTracker();
	public static CameraServo CameraServo = new CameraServo();
	public static Elevator Elevator = new Elevator();
	public static OI oi;
	public static ADXRS450_Gyro gyro;
	public static Climber Climber = new Climber();
	Thread m_visionThread;

	//SmartDashboard Editable variables
	Preferences prefs;
	public static double UserAssistCorrectionSpeed;
	public static double ElevatorUserOverrideDeadzone;

	//Autonomous
	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		//Initialize gyro and OI
		gyro = new ADXRS450_Gyro();
		oi = new OI();
		OI.init();

		//SmartDashboard editable variables
		prefs = Preferences.getInstance();
		UserAssistCorrectionSpeed = prefs.getDouble("UserAssistCorrectionSpeed (%)", 3);
		ElevatorUserOverrideDeadzone = prefs.getDouble("Deadzone for X-Box Elevator User Override (%)", 3);
		
		//m_chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", m_chooser);
		new Thread(() -> {
			UsbCamera USBcamera = CameraServer.getInstance().startAutomaticCapture();
			USBcamera.setResolution(426, 240);
			//AxisCamera axisCamera = CameraServer.getInstance().addAxisCamera("axis-camera.local");
			// Set the resolution
			//axisCamera.setResolution(640, 480);
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 426,
			240);
			Mat source = new Mat();
			Mat output = new Mat();
			while(true) {
			cvSink.grabFrame(source);
			Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
			outputStream.putFrame(output);
			}
			}).start();
		// m_visionThread = new Thread(() -> {
		// 	// Get the Axis camera from CameraServer
		// 	
	  
		// 	// Get a CvSink. This will capture Mats from the camera
		// 	CvSink cvSink = CameraServer.getInstance().getVideo();
		// 	// Setup a CvSource. This will send images back to the Dashboard
		// 	CvSource outputStream
		// 		= CameraServer.getInstance().putVideo("Rectangle", 640, 480);
	  
		// 	// Mats are very memory expensive. Lets reuse this Mat.
		// 	Mat mat = new Mat();
	  
		// 	// This cannot be 'true'. The program will never exit if it is. This
		// 	// lets the robot stop this thread when restarting robot code or
		// 	// deploying.
		// 	while (!Thread.interrupted()) {
		// 	  // Tell the CvSink to grab a frame from the camera and put it
		// 	  // in the source mat.  If there is an error notify the output.
		// 	  if (cvSink.grabFrame(mat) == 0) {
		// 		// Send the output the error.
		// 		outputStream.notifyError(cvSink.getError());
		// 		// skip the rest of the current iteration
		// 		continue;
		// 	  }
		// 	  // Put a rectangle on the image
		// 	  Imgproc.rectangle(mat, new Point(100, 100), new Point(400, 400),
		// 		  new Scalar(255, 255, 255), 5);
		// 	  // Give the output stream a new image to display
		// 	  outputStream.putFrame(mat);
		// 	}
		//   });
		}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		m_autonomousCommand = m_chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
