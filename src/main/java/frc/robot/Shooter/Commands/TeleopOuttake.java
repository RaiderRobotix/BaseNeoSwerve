package frc.robot.Shooter.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Shooter.Intake;


public class TeleopOuttake extends CommandBase
{
    final private Intake INTAKE;
    private boolean finished = false;

    public TeleopOuttake(Intake subsystem)
    {
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
    }

    @Override
    public void end(boolean inturrupted)
    {
        INTAKE.stopIntake();
        
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }
}
