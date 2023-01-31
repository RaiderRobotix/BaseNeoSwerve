package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

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
        INTAKE.outtake();
    }

    @Override
    public void execute()
    {
        
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
