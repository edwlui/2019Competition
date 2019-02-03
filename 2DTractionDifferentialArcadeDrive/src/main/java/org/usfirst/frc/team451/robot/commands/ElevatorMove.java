/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team451.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
//import com.sun.java.swing.plaf.windows.TMSchema.Control;

import org.usfirst.frc.team451.robot.OI;
import org.usfirst.frc.team451.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorMove extends Command {
  private static final String Elevator = null;

public ElevatorMove() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.Elevator);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (OI.mechBox.getY() > 0.000) {
     Robot.Elevator.elevatorMotor.set(-1);
     System.out.println("Elevator UP");
    } else if (OI.mechBox.getY() < 0.000) {
      Robot.Elevator.elevatorMotor.set(1);
      System.out.println("Elevator DOWN");
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}