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
import frc.robot.Shooter.Commands.RunIntake;
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
            )).alongWith(new RunIntake(intake, true))
        );
            
               
    }


  
    // this is terrible

    private Command doubleScore(boolean invertY, double dist, double rampAvoid, double target, double endPoint)
    {   
    

        //.32, .7
        double speed = .2;
        return basicAuto(invertY, dist).andThen(
            new InstantCommand(()->{CommandScheduler.getInstance()
            .schedule(new SpinShooter(shooter, intake, IntakeConfig.FastSpeed)); System.out.println("heyo");}),

            new SwerveController(swerve, speed, List.of(
            new Pose2d(6.2,dist*(invertY?-1:1),new Rotation2d(0)),
            new Pose2d(3,rampAvoid*(invertY?-1:1),Rotation2d.fromDegrees(180)),
            new Pose2d(.9,target*(invertY?-1:1),Rotation2d.fromDegrees(180)))
            ),
            new WaitCommand(1),
            new ShootPiece(intake, shooter).raceWith(new WaitCommand(1.5)),
            // finish up
            new SwerveController(swerve, speed, List.of(
                new Pose2d(1,target*(invertY?-1:1),Rotation2d.fromDegrees(180)),
                new Pose2d(1.3, .2*(invertY?-1:1),Rotation2d.fromDegrees(180)),
                new Pose2d(6.2,endPoint*(invertY?-1:1),Rotation2d.fromDegrees(180))
            ))
            /*new InstantCommand(()->{CommandScheduler.getInstance()
            .schedule(new SpinShooter(shooter, intake, 0));}),
              new SwerveController(swerve, speed, List.of(
                new Pose2d(3.3,rampAvoid*(invertY?-1:1),Rotation2d.fromDegrees(180)),
                new Pose2d(6.2,1.8*(invertY?-1:1),Rotation2d.fromDegrees(270))
            )).alongWith(new RunIntake(intake, true))*/

        );


       
    }
    

    private Command balanceAuto()
    {
    
        
        return score().andThen(
          
         
            new SwerveController(swerve,  List.of(
                new Pose2d(0,0,new Rotation2d(0)),
                new Pose2d(4.5,0,new Rotation2d(0))
               
            )),
            new SwerveController(swerve,  List.of(
                    new Pose2d(4.5,0,new Rotation2d(0)),
                    new Pose2d(6.2,0,new Rotation2d(0))
                  
            )).alongWith( new WaitCommand(2).raceWith(new RunIntake(intake, true))),
            new SwerveController(swerve, List.of(
                new Pose2d(6.2,0,new Rotation2d(0)),
                new Pose2d(2.4,0,new Rotation2d(0))
            )),
            new AutoBalance(swerve),
            new SwerveController(swerve, .05f,  List.of(
                new Pose2d(2,0,new Rotation2d(0)),
                new Pose2d(2.05,0,new Rotation2d(0))
            )),
            new LockSwerveCommand(swerve, ()->false)
        );
        // 
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
        new WaitCommand(.5),
        new AutoPoseCommand( NamedPose.PouncePreScore, arm),
        new AutoPoseCommand(NamedPose.ScoreL3Auto, arm), 
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

       
        switch(wheel.getValue())
        {
            case 0: // we reseve zero to doing nothing.
                return new InstantCommand();

            // TODO init with cube DOES NOT WORK! this is likely due to a oversight in arm scheduling code. fix?
           
            // basic set (blue) 
            case 1: // dunno
                return init(GamePiece.cone).andThen(basicAuto(false, .31));
            case 2:
                return init(GamePiece.cone).andThen(balanceAuto());
            case 3: // dunno
                return init(GamePiece.cone).andThen(basicAuto(true, .6));

            // blue autos
            case 4: // dunno
                return init(GamePiece.cone).andThen(doubleScore(false , .31, .3, .7, .5));
            case 5: // dunno
                return init(GamePiece.cone).andThen(balanceAuto());
            case 6: // dunno prev .76 dist
                return init(GamePiece.cone).andThen(doubleScore(true, .45, -.15, .7, -.5));


            // red autos
            case 7: // dunno
                return init(GamePiece.cone).andThen(doubleScore(false, .4, -.15, .6, -.5));
            case 8: 
                return init(GamePiece.cone).andThen(balanceAuto());
            case 9: // dunno
                return init(GamePiece.cone).andThen(doubleScore(true, .31, .3, .7, .5));


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
