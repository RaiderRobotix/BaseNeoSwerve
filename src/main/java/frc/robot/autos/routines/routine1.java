// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autos.routines;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.autos.AutoBuilder;
import frc.robot.subsystems.*;

public class routine1 extends CommandBase {
  /** Creates a new routine1. */
  AutoBuilder auto;

  public routine1(Swerve s_Swerve) {
    auto = new AutoBuilder(s_Swerve);
    auto.loadPath("routine1");
    auto.buildAutos();
    addRequirements(s_Swerve);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize(){
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    auto.getAutoCommand();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}