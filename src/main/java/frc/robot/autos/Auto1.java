package frc.robot.autos;

import java.util.ArrayList;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Arm.Arm;
import frc.robot.Arm.NamedPose;
import frc.robot.Arm.command.ArmCommand;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.SwerveConfig;
import frc.robot.swerve.command.LockSwerveCommand;

public class Auto1 extends SequentialCommandGroup
{
    public Auto1(Swerve swerve, Arm arm)
    {
        TrajectoryConfig config =
        new TrajectoryConfig(
                Constants.AutoConstants.kMaxSpeedMetersPerSecond,
                Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            .setKinematics(SwerveConfig.swerveKinematics);

            
        addCommands(
            ArmCommand.PlotPath( NamedPose.PouncePreScore, arm),
            ArmCommand.PlotPath(NamedPose.ScoreL3,arm),
            new WaitCommand(1),
            ArmCommand.PlotPath(NamedPose.PouncePreScore,arm),
            ArmCommand.PlotPath(NamedPose.Home,arm),
            new AutoTrajectory(swerve, 
            TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these meny interior waypoints, making an too sirkle curve path
               /*  List.of(new Translation2d(1,0),
                    new Translation2d(1, 1), 
                    new Translation2d(0, 1)),*/
                    
                 new ArrayList<Translation2d>(),
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(4.5, 0, new Rotation2d(0)),
                config)),
            
                
                new LockSwerveCommand(swerve, ()->false)
                
        );

            
        
    }
}
