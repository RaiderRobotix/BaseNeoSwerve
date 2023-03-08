package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class ShootPiece extends CommandBase
{
    final private Intake INTAKE;
    final private Shooter SHOOTER;
    private boolean finished = false;

    public ShootPiece(Intake intake, Shooter shooter)
    {
        INTAKE = intake;
        SHOOTER = shooter;
        addRequirements(INTAKE);
    }

    @Override
    public void initialize() 
    {
        if(SHOOTER.isUpToSpeed())
        {
            INTAKE.shoot();
        }

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