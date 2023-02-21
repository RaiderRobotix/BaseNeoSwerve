package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Arm.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeState;

public class TeleopIntake extends CommandBase
{

    final private Intake INTAKE;
    private final Arm ARM;
    private boolean finished = false;

    public TeleopIntake(Intake intake, Arm arm)
    {
        INTAKE = intake;
        ARM = arm;
        
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
            end(true);
        }
    }
    @Override
    public void execute()
    {
        if(INTAKE.getSensorValue())
        {
            INTAKE.notifyPieceObtained();
            finished = true;
        }
    }

    @Override
    public void end(boolean inturrupted)
    {
        
        INTAKE.stopIntake();

     
        if(!inturrupted)
        {
            //TODO uncomment once arm poses are correct and junk
            //new PresentPiece(INTAKE, ARM);
        }
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }

}
