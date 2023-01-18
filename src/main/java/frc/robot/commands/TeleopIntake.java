package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class TeleopIntake extends CommandBase
{
    final private Intake INTAKE;

    public TeleopIntake(Intake subsystem)
    {
        INTAKE = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute()
    {
        INTAKE.setIntakeMotor(1);
    }

    @Override
    public void end(boolean inturrupted)
    {
        INTAKE.setIntakeMotor(0);
    }

}
