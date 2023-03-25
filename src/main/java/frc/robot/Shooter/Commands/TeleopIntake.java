package frc.robot.Shooter.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Shooter.Intake;

public class TeleopIntake extends CommandBase
{

    final private Intake INTAKE;
    private boolean finished = false;

    public TeleopIntake(Intake intake)
    {
        INTAKE = intake;
        finished = false;
        //addRequirements(INTAKE);
    }

    @Override
    public void initialize() 
    {
      
    }
    @Override
    public void execute()
    {
        if(!INTAKE.hasPiece())
        {
            INTAKE.startIntake();//only run if needs a piece
            //System.out.println("intake!!!!");
            finished = false;
            return;
        }
       
        //System.out.println("NO!");
        INTAKE.stopIntake(); 
        finished = true;  
        
    }

    @Override
    public void end(boolean inturrupted)
    {
        //System.out.println("Dead..."+inturrupted);
        //Thread.dumpStack();
        INTAKE.stopIntake();

    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }

}
