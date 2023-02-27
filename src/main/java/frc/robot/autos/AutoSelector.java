package frc.robot.autos;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
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
import frc.robot.swerve.command.LockSwerveCommand;

public class AutoSelector 
{

    private Thumbwheel wheel;
    PieceMode mode;
    private Arm arm;
    private Swerve swerve;

    private Command balanceAuto;
    
    public AutoSelector(Thumbwheel th, Arm arm, Swerve swerve, PieceMode mode)
    {
        this.arm = arm;
        wheel = th;
        this.mode = mode;
        this.swerve = swerve;


 
        TrajectoryConfig config =
        new TrajectoryConfig(
                Constants.AutoConstants.kMaxSpeedMetersPerSecond,
                Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            .setKinematics(SwerveConfig.swerveKinematics);

        

        balanceAuto =  score(arm).andThen(
          
            new SwerveController(swerve,  List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(4.5,0,new Rotation2d(0)),
                new Pose2d(1.5,0,new Rotation2d(0))
            )),
          
            new LockSwerveCommand(swerve, ()->false)
        );
        

           
    }

    private Command basicAuto(Arm arm, Swerve swerve)
    {
      


        return score(arm).andThen(
            new SwerveController(swerve,  List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(3.85,0,new Rotation2d(0))
            ))
        );
            
               
    }

    private Command score(Arm arm)
    {
         return   new AutoPoseCommand( NamedPose.Travel, arm)
        .andThen( 
        new InstantCommand(()->{arm.setClaw(false);}),
        new WaitCommand(.1),
        new AutoPoseCommand( NamedPose.PouncePreScore, arm),
        new AutoPoseCommand(NamedPose.ScoreL3, arm), 
        new WaitCommand(1),
        new InstantCommand(()->{arm.setClaw(true);}),
        new WaitCommand(.1),
        new ScheduleCommand(new AutoPoseCommand(NamedPose.Travel, arm)));
    }


    public Command getAutonomousCommand()
    {


        switch(wheel.getValue())
        {
            case 0: // we reseve zero to doing nothing.
                return new InstantCommand();
            case 1:
                return new InstantCommand(()->mode.setPiece(GamePiece.cone)).andThen(basicAuto(arm, swerve));
            case 2:
                return new InstantCommand(()->mode.setPiece(GamePiece.cone)).andThen(balanceAuto);
            case 3:
                return new InstantCommand(()->mode.setPiece(GamePiece.cone)).andThen(basicAuto(arm, swerve));

            default:
                return new InstantCommand();

        }
    }

}
