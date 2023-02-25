package frc.robot.autos;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.lib.util.States.GamePiece;
import frc.robot.Constants;
import frc.robot.PieceMode;
import frc.robot.Arm.Arm;
import frc.robot.Arm.NamedPose;
import frc.robot.Arm.PoseList;
import frc.robot.Arm.command.ArmCommand;
import frc.robot.Arm.command.BasicPose;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.SwerveConfig;
import frc.robot.swerve.command.LockSwerveCommand;

public class Auto1 extends SequentialCommandGroup
{
    public Auto1(Swerve swerve, Arm arm, PieceMode mode)
    {
      

        PoseList p = arm.getPoseList();
        //arm.adopt Pose(new BasicPose
        //(null, null, null, true));
        mode.setPiece(GamePiece.cone);
        addCommands(
            new AutoPoseCommand( NamedPose.Travel, arm),
           
            new InstantCommand(()->{arm.setClaw(false);}),
            new WaitCommand(.1),
            new AutoPoseCommand( NamedPose.PouncePreScore, arm),
            new AutoPoseCommand(NamedPose.ScoreL3, arm),
            new WaitCommand(1),
            new InstantCommand(()->{arm.setClaw(true);}),
            new WaitCommand(.1),
            new AutoPoseCommand(NamedPose.Travel, arm),
            new AutoTrajectory(swerve, 
            TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these meny interior waypoints, making an too sirkle curve path
               List.of(new Translation2d(6,-.5)),
                   
                    //new ArrayList<>(),
              
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(-2, 0, new Rotation2d(0)),
                config)),
           
            
                
                new LockSwerveCommand(swerve, ()->false)
                
        );

            
        
    }
}
