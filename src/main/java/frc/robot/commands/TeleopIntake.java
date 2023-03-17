package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Arm.Arm;
import frc.robot.subsystems.Intake;

public class TeleopIntake extends CommandBase
{

    final private Intake INTAKE;
    private boolean finished = false;

    public TeleopIntake(Intake intake)
    {
        INTAKE = intake;
        
        addRequirements(INTAKE);
    }

    @Override
    public void initialize() 
    {
        if(!INTAKE.hasPiece()) 
        {
            INTAKE.startIntake();//only run if needs a piece
            System.out.println("starting intake");
        }
        else
        {
            finished = true;
            end(true);
        }
    }
    @Override
    public void execute()
    {
        if(INTAKE.hasPiece())
        {
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
