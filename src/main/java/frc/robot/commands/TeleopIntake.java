package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeState;

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
        if(INTAKE.getState() == IntakeState.wantsCone || INTAKE.getState() == IntakeState.wantsCube) 
        {
            INTAKE.startIntake();//only run if needs a piece
        }
        else
        {
            finished = true;
        }
    }
    @Override
    public void execute()
    {
        if(INTAKE.getSensorValue())
        {
            INTAKE.collectPiece();
            finished = true;
        }
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
