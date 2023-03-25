package frc.robot.Shooter.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Shooter.Intake;
import frc.robot.Shooter.Shooter;

public class ShootPiece extends CommandBase
{
    final private Intake INTAKE;
    final private Shooter SHOOTER;
    private boolean finished = false;

    public ShootPiece(Intake intake, Shooter shooter)
    {
        INTAKE = intake;
        SHOOTER = shooter;
        //addRequirements(INTAKE);
    }

    @Override
    public void initialize() 
    {
        

    }

    @Override
    public void execute()
    {
        //if(SHOOTER.isUpToSpeed())
        {
            INTAKE.shoot();
        }
       /*else
        {
            INTAKE.stopIntake();
        }*/
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