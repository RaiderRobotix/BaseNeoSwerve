package frc.robot.commands;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.util.States.LinkableState;
import frc.lib.util.States.StateMachine;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

public class AutoBalance extends CommandBase
{

    final private Swerve SWERVE;
    private StateMachine stateMachine;
    
    

    public AutoBalance(Swerve swerve)
    {
        System.out.println("conrt");
        SWERVE = swerve;
        stateMachine = new StateMachine(this::balance);
   
    }

    @Override
    public void initialize() 
    {
        
    }
    @Override
    public void execute()
    {
        System.out.println("Running?");
        stateMachine.execute();
    }

    @Override
    public void end(boolean inturrupted)
    {
        
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }

    public LinkableState startLock()
    {
        
        CommandScheduler.getInstance().schedule( new LockSwerveCommand(SWERVE, ()->!shouldBeLocked()));
        return this::stayLocked;
    }

    public LinkableState stayLocked()
    {
      
        if(shouldBeLocked())
        {
            return this::stayLocked;
        }

        return this::balance;
    }

    public LinkableState balance()
    {
        System.out.println("BALANCE");
        double speed= .01;
        
        speed*=SWERVE.getPitchDegrees();
        
        if(shouldBeLocked())
        {
            return this::startLock;
        }

        SWERVE.drive(
             new Translation2d(speed, 0).times(Constants.Swerve.maxSpeed),
             0,
             false,
             true
             );

       
        return this::balance;
    }

    private boolean shouldBeLocked()
    {
        
        
        return Math.abs(SWERVE.getPitchDegrees())<5;
    }


}
