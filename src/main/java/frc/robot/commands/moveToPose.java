// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

public class moveToPose extends CommandBase {
  /** Creates a new moveToPose. */
  private Swerve s_Swerve;
  private Pose2d desiredPose;
  private boolean isFieldCentric;
  private Trajectory desiredTrajectory;
  private SwerveControllerCommand desiredCommand;

  public moveToPose(Swerve s_Swerve, double x, double y, Rotation2d angle, boolean isFieldCentric ) {
    desiredPose = (isFieldCentric) ? new Pose2d(x, y, angle) : new Pose2d(x + s_Swerve.getPose().getX(), y + s_Swerve.getPose().getY(), angle);
    this.s_Swerve = s_Swerve;
    addRequirements(s_Swerve);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  public moveToPose(Swerve s_Swerve, double x, double y, double angle, boolean isFieldCentric) {
    desiredPose = (isFieldCentric) ? new Pose2d(x, y, Rotation2d.fromDegrees(angle)) : new Pose2d(x + s_Swerve.getPose().getX(), y + s_Swerve.getPose().getY(), Rotation2d.fromDegrees(angle));
    this.s_Swerve = s_Swerve;
    addRequirements(s_Swerve);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  public moveToPose(Swerve s_Swerve, double x, double y, boolean isFieldCentric) {
    desiredPose = (isFieldCentric) ? new Pose2d(x, y, Rotation2d.fromDegrees(s_Swerve.getYawIEEE())) : new Pose2d(x + s_Swerve.getPose().getX(), y + s_Swerve.getPose().getY(), Rotation2d.fromDegrees(s_Swerve.getYawIEEE()));
    this.s_Swerve = s_Swerve;
    addRequirements(s_Swerve);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  public moveToPose(Swerve s_Swerve, Pose2d pose) {
    desiredPose = pose;
    this.s_Swerve = s_Swerve;
    addRequirements(s_Swerve);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    desiredTrajectory = TrajectoryGenerator.generateTrajectory(
        s_Swerve.getPose(),
        new ArrayList<Translation2d>(Arrays.asList(new Translation2d(desiredPose.getX() / 2, desiredPose.getY() / 2))),
        desiredPose,
        new TrajectoryConfig(
          Constants.AutoConstants.kMaxSpeedMetersPerSecond,
          Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared));
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    desiredCommand = new SwerveControllerCommand(
      desiredTrajectory,
      s_Swerve::getPose, 
      Constants.Swerve.swerveKinematics, 
      new PIDController(0, 0, 0),
      new PIDController(0, 0, 0),
      new ProfiledPIDController(0, 0, 0,
        new Constraints(
          Constants.AutoConstants.kMaxSpeedMetersPerSecond,
          Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)),
      s_Swerve::setModuleStates,
      s_Swerve);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(s_Swerve.getPose().getX() - desiredPose.getX()) <= 0.5
        && Math.abs(s_Swerve.getPose().getY() - desiredPose.getY()) <= 0.5;
  }
}
