package frc.robot.autos;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.lib.util.States.GamePiece;
import frc.robot.PieceMode;
import frc.robot.Arm.Arm;
import frc.robot.Arm.NamedPose;
import frc.robot.Shooter.Intake;
import frc.robot.Shooter.IntakeConfig;
import frc.robot.Shooter.Shooter;
import frc.robot.Shooter.Commands.ShootPiece;
import frc.robot.Shooter.Commands.SpinShooter;
import frc.robot.Shooter.Commands.TeleopIntake;
import frc.robot.subsystems.Thumbwheel;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.command.AutoBalance;
import frc.robot.swerve.command.LockSwerveCommand;

public class AutoSelector 
{

    private Thumbwheel wheel;
    PieceMode mode;
    private Arm arm;
    private Swerve swerve;
    private Intake intake;
    private Shooter shooter;
    private PowerDistribution powerBoard;

    
    public AutoSelector(Thumbwheel th, Arm arm, Swerve swerve, Intake intake, Shooter shooter, PowerDistribution pd, PieceMode mode)
    {
        this.arm = arm;
        wheel = th;
        this.mode = mode;
        this.swerve = swerve;
        this.intake = intake;
        this.shooter = shooter;
        powerBoard = pd;
        
        
           
    }

    // usually point 8
    private Command basicAuto(boolean invertY, double dist)
    {
      
        // test
        double speed = .2;
        return score().andThen(
            new SwerveController(swerve,speed, List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(6.2,dist*(invertY?-1:1),new Rotation2d(0))
            )).alongWith(new TeleopIntake(intake))
        );
            
               
    }


  

    private Command doubleScore(boolean invertY, double dist, double rampAvoid, double target)
    {

        //.32, .7
        double speed = .2;
        return basicAuto(invertY, dist).andThen(
            new InstantCommand(()->{CommandScheduler.getInstance()
            .schedule(new SpinShooter(shooter, IntakeConfig.FastSpeed)); System.out.println("heyo");}),

            new SwerveController(swerve, speed, List.of(
            new Pose2d(6.2,dist*(invertY?-1:1),new Rotation2d(0)),
            new Pose2d(3,rampAvoid*(invertY?-1:1),Rotation2d.fromDegrees(180)),
            new Pose2d(1,target*(invertY?-1:1),Rotation2d.fromDegrees(180)))
            ),
            new WaitCommand(1),
            new ShootPiece(intake, shooter).raceWith(new WaitCommand(1.5)),
            // finish up
            new SwerveController(swerve, speed, List.of(
                new Pose2d(1,target*(invertY?-1:1),Rotation2d.fromDegrees(180)),
                new Pose2d(5.5,rampAvoid*(invertY?-1:1),Rotation2d.fromDegrees(180))
            ))
            //new SpinShooter(shooter, 0)
        );


       
    }
    

    private Command balanceAuto()
    {
    
        return score().andThen(
          
            new SwerveController(swerve,  List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(4,0,new Rotation2d(0))
               
            )),
            new SwerveController(swerve,  List.of(
                    new Pose2d(4,0,new Rotation2d(0)),
                    new Pose2d(5.5,0,new Rotation2d(0))
                  
            )).alongWith( new WaitCommand(2).raceWith(new TeleopIntake(intake))),
            new SwerveController(swerve, List.of(
                new Pose2d(5.4,0,new Rotation2d(0)),
                new Pose2d(2.4,0,new Rotation2d(0))
            )),
            new AutoBalance(swerve),
            new SwerveController(swerve,  List.of(
                new Pose2d(2,0,new Rotation2d(0)),
                new Pose2d(2.25,0,new Rotation2d(0))
            )),
            new LockSwerveCommand(swerve, ()->false)
        );
    }


    private Command TestAuto()
    {
      
//

        return  
        new SwerveController(swerve,  List.of(
            new Pose2d(0,0,new Rotation2d(0)),
            new Pose2d(0,0,Rotation2d.fromDegrees(180))//,
            //new Pose2d(3,0,new Rotation2d(0))
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
        new WaitCommand(.9),
        new InstantCommand(()->{arm.setClaw(true);}),
        new WaitCommand(.1),
        new ScheduleCommand(new AutoPoseCommand(NamedPose.Travel, arm)));
    }

    private Command init(GamePiece p)
    {
        return new InstantCommand(()->  
        {  
            //swerve.zeroGyro();
            swerve.resetOdometry(new Pose2d());
           // swerve.zeroGyro();
            mode.setPiece(p);
            
            AutoPoseCommand.reset();
            powerBoard.setSwitchableChannel(false);
           
        });
     
       
    }

    public Command getAutonomousCommand()
    {

       // test
        switch(wheel.getValue())
        {
            case 0: // we reseve zero to doing nothing.
                return new InstantCommand();

            // TODO init with cube DOES NOT WORK! this is likely due to a oversight in arm scheduling code. fix?
           
            // basic set (blue) 
            case 1: 
                return init(GamePiece.cone).andThen(basicAuto(false, .4));
            case 2:
                return init(GamePiece.cone).andThen(balanceAuto());
            case 3:
                return init(GamePiece.cone).andThen(basicAuto(true, .8));

            // blue autos
            case 4:
                return init(GamePiece.cone).andThen(doubleScore(false , .31, .3, .6));
            case 5:
                return init(GamePiece.cone).andThen(balanceAuto());
            case 6:
                return init(GamePiece.cone).andThen(doubleScore(true, .76, .5, .8));


            // red autos
            case 7:
                return init(GamePiece.cone).andThen(doubleScore(false, .76, .5, .8));
            case 8:
                return init(GamePiece.cone).andThen(balanceAuto());
            case 9:
                return init(GamePiece.cone).andThen(doubleScore(true, .31, .3, .6));


            // test autos
            case 10:
                return init(GamePiece.cone);

            case 13:
                return init(GamePiece.cone).andThen(score());
            
            case 15:
                return init(GamePiece.cone) .andThen(TestAuto());
           
            default:
                return new InstantCommand();

        }
    }

}
