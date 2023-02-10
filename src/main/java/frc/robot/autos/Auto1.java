package frc.robot.autos;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Swerve;
import frc.robot.ArmPoses;
import frc.robot.Constants;
import frc.robot.ArmPoses.Poses;
import frc.robot.commands.ArmCommand;
import frc.robot.commands.LockSwerveCommand;
import frc.robot.subsystems.Arm;

public class Auto1 extends SequentialCommandGroup
{
    public Auto1(Swerve swerve, Arm arm)
    {
        TrajectoryConfig config =
        new TrajectoryConfig(
                Constants.AutoConstants.kMaxSpeedMetersPerSecond,
                Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            .setKinematics(Constants.Swerve.swerveKinematics);

            
        addCommands(
            new ArmCommand(arm, arm.getPose(Poses.PouncePreScore)),
            new ArmCommand(arm, arm.getPose(Poses.ConeScoreL3)),
            new ArmCommand(arm, arm.getPose(Poses.PouncePreScore)),
            new AutoTrajectory(swerve, 
            TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these meny interior waypoints, making an too sirkle curve path
                List.of(new Translation2d(1,0),
                    new Translation2d(1, 1), 
                    new Translation2d(0, 1)),
                    
                 //new ArrayList<Translation2d>(),
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(0, 0, new Rotation2d(0)),
                config)),
            
                new ArmCommand(arm, arm.getPose(Poses.Home)),
                new LockSwerveCommand(swerve, ()->false)
                
        );

            
        
    }
}
