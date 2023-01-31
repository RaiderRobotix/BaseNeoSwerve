package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.ArmPoses;
import frc.robot.subsystems.Arm;
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

        // TODO remove the false when arm is tested
        if(!inturrupted && false)
        {
            // TODO set correct arm pose
            CommandBase c = new ArmCommand(ARM, ArmPoses.home);
            // 
            new PresentPiece(INTAKE, ARM, c);
        }
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }

}
