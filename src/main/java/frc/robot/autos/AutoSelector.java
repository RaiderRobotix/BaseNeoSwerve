package frc.robot.autos;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.lib.util.States.GamePiece;
import frc.robot.Constants;
import frc.robot.PieceMode;
import frc.robot.Arm.Arm;
import frc.robot.Arm.NamedPose;
import frc.robot.subsystems.Thumbwheel;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.SwerveConfig;
import frc.robot.swerve.command.AutoBalance;
import frc.robot.swerve.command.LockSwerveCommand;

public class AutoSelector 
{

    private Thumbwheel wheel;
    PieceMode mode;

    private Command basicAuto;
    private Command balanceAuto;
    
    public AutoSelector(Thumbwheel th, Arm arm, Swerve swerve, PieceMode mode)
    {
        wheel = th;
        this.mode = mode;

        TrajectoryConfig config =
        new TrajectoryConfig(
                Constants.AutoConstants.kMaxSpeedMetersPerSecond,
                Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            .setKinematics(SwerveConfig.swerveKinematics);

        Command score =   new AutoPoseCommand( NamedPose.Travel, arm)
            .andThen( new InstantCommand(()->{arm.setClaw(false);}),
            new WaitCommand(.1),
            new AutoPoseCommand( NamedPose.PouncePreScore, arm),
            new AutoPoseCommand(NamedPose.ScoreL3, arm), 
            new WaitCommand(1),
            new WaitCommand(.1),
            new ScheduleCommand(new AutoPoseCommand(NamedPose.Travel, arm)));

        basicAuto =  score.andThen(
            new AutoTrajectory(swerve, 
            TrajectoryGenerator.generateTrajectory(
            // Start at the origin facing the +X direction
            new Pose2d(0, 0, new Rotation2d(0)),
            // Pass through these interior waypoints.
            //List.of(new Translation2d(6,-.5)),
            
            new ArrayList<>(),
        
            // End 3 meters straight ahead of where we started, facing forward
            new Pose2d(7, 0, new Rotation2d(0)),
            config)
            )
        );           

        balanceAuto =  score.andThen(
          
            new AutoTrajectory(swerve, 
                TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these interior waypoints.
                List.of(new Translation2d(6,-.5)),
                
                //new ArrayList<>(),
            
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(-2, 0, new Rotation2d(0)),
                config)
            ),
            new AutoBalance(swerve, false),
            new LockSwerveCommand(swerve, ()->false)
        );
        

           
    }


    public Command getAutonomousCommand()
    {


        switch(wheel.getValue())
        {
            case 0: // we reseve zero to doing nothing.
                return new InstantCommand();
            case 1:
                return new InstantCommand(()->mode.setPiece(GamePiece.cone)).andThen(basicAuto);
            case 2:
                return new InstantCommand(()->mode.setPiece(GamePiece.cone)).andThen(balanceAuto);
            case 3:
                return new InstantCommand(()->mode.setPiece(GamePiece.cone)).andThen(basicAuto);

            default:
                return new InstantCommand();

        }
    }

}
