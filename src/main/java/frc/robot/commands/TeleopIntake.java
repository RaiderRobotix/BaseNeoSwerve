package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class TeleopIntake extends CommandBase
{
    final private Intake INTAKE;
    private boolean finished = false;

    public TeleopIntake(Intake subsystem)
    {
        INTAKE = subsystem;
        addRequirements(INTAKE);
    }

    @Override
    public void initialize() 
    {
        INTAKE.startIntake();
    }

    @Override
    public void execute()
    {
        INTAKE.runIntake();
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
