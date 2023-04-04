package frc.robot.Shooter.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Shooter.Intake;

public class RunIntake extends CommandBase
{

    final private Intake INTAKE;
    private boolean finished = false;
    private boolean useArms;

    public RunIntake(Intake intake, boolean useArms)
    {
        INTAKE = intake;
        finished = false;
        this.useArms = useArms;
        //addRequirements(INTAKE);
    }

    @Override
    public void initialize() 
    {
        if(useArms)
        {
            INTAKE.setArms(false);
        }
    }
    @Override
    public void execute()
    {
        if(!INTAKE.hasPiece())
        {
            INTAKE.startIntake();//only run if needs a piece

            if(useArms)
            {
                INTAKE.startArms();
            }
            finished = false;
            return;
        }
       
        INTAKE.stopIntake(); 
        finished = true;  
        
    }

    @Override
    public void end(boolean inturrupted)
    {
        INTAKE.stopIntake();
        INTAKE.stopArms();
        INTAKE.setArms(true);

    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }

}
