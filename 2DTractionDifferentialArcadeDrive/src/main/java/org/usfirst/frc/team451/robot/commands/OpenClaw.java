/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team451.robot.commands;

import org.usfirst.frc.team451.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
/**
 * An example command.  You can replace me with your own command.
 */
public class OpenClaw extends Command {
  public OpenClaw() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.Claw);
  }

 //Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.oi.button3.whenPressed(new OpenClaw());
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  //turns pneumatics on 
  protected void execute() {
    Robot.Claw.TurnPneumaticsOn();
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
  };

}