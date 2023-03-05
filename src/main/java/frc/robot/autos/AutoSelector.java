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

    
    public AutoSelector(Thumbwheel th, Arm arm, Swerve swerve, PieceMode mode)
    {
        this.arm = arm;
        wheel = th;
        this.mode = mode;
        this.swerve = swerve;
        

           
    }

    private Command basicAuto()
    {
      


        return score().andThen(
            new SwerveController(swerve,  List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(4,0,new Rotation2d(0))
            ))
        );
            
               
    }

    private Command doubleScore()
    {

        double dist = 3.5;
        return score().andThen(
            new SwerveController(swerve,  List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(dist-.1,0,new Rotation2d(0)),
                new Pose2d(dist,0,new Rotation2d(180))
            )).alongWith(
                new WaitCommand(.3).andThen
                (
                    new AutoPoseCommand(NamedPose.FloorPick, arm),
                new WaitCommand(1.5)
                )),
            
            new AutoPoseCommand(NamedPose.Travel, arm),
            new WaitCommand(.2),
            new SwerveController(swerve,  List.of(
                new Pose2d(dist,0,new Rotation2d(180)),
                new Pose2d(dist-.1,0,new Rotation2d(0)),
                new Pose2d(0,0,new Rotation2d(0))
            )),
            new AutoPoseCommand( NamedPose.PouncePreScore, arm),
            new AutoPoseCommand(NamedPose.ScoreL2, arm), 
            new WaitCommand(1.5),
            new InstantCommand(()->{arm.setClaw(true);}),
            new WaitCommand(.1),
            new ScheduleCommand(new AutoPoseCommand(NamedPose.Travel, arm))
        );
    }
    

    private Command balanceAuto()
    {
        return score().andThen(
          
            new SwerveController(swerve,  List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(4.5,0,new Rotation2d(0)),
                new Pose2d(2.2,0,new Rotation2d(0))
            )),
          
            new LockSwerveCommand(swerve, ()->false)
        );
    }


    private Command TestAuto()
    {
      
//

        return new SwerveController(swerve,  List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(.1,0,Rotation2d.fromDegrees(90))
            )
        );
            
               
    }


    private Command score()
    {
         return   new AutoPoseCommand( NamedPose.Travel, arm)
        .andThen( 
        new InstantCommand(()->{arm.setClaw(false);}),
        new WaitCommand(.1),
        new AutoPoseCommand( NamedPose.PouncePreScore, arm),
        new AutoPoseCommand(NamedPose.ScoreL3, arm), 
        new WaitCommand(1.5),
        new InstantCommand(()->{arm.setClaw(true);}),
        new WaitCommand(.1),
        new ScheduleCommand(new AutoPoseCommand(NamedPose.Travel, arm)));
    }

    private Command init(GamePiece p)
    {
        return new InstantCommand(()->  
        {  
        swerve.zeroGyro();
        swerve.resetOdometry(new Pose2d(0,0,new Rotation2d()));
        mode.setPiece(p);
        AutoPoseCommand.reset();
        });
     
       
    }

    public Command getAutonomousCommand()
    {

       
        switch(wheel.getValue())
        {
            case 0: // we reseve zero to doing nothing.
                return new InstantCommand();
            case 1:
                return init(GamePiece.cone).andThen(basicAuto());
            case 2:
                return init(GamePiece.cone) .andThen(balanceAuto());
            case 3:
                return init(GamePiece.cone).andThen(score());
            case 4:
                return init(GamePiece.cube).andThen(score());
            case 5:
                return init(GamePiece.cube).andThen(basicAuto());
            case 6:
                return init(GamePiece.cube) .andThen(balanceAuto());
            case 14:
                return init(GamePiece.cube) .andThen(doubleScore());
            case 15:
                return init(GamePiece.cube) .andThen(TestAuto());
           
            default:
                return new InstantCommand();

        }
    }

}
