package frc.robot.Shooter.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Shooter.Intake;
import frc.robot.Shooter.Shooter;


public class TeleopOuttake extends CommandBase
{
    final private Intake INTAKE;
    final private Shooter shooter;
    
    private boolean finished = false;

    public TeleopOuttake(Intake subsystem, Shooter shooter)
    {
        this.shooter = shooter;
        INTAKE = subsystem;
        addRequirements(INTAKE);
    }

    @Override
    public void initialize() 
    {
        
    }

    @Override
    public void execute()
    {
        INTAKE.outtake();  
        shooter.setSpeed(-.5);
    }

    @Override
    public void end(boolean inturrupted)
    {
        INTAKE.stopIntake();
        shooter.setSpeed(0);
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }
}
