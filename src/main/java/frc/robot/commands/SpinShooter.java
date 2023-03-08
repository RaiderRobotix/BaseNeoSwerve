package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class SpinShooter extends CommandBase
{
    private final Shooter SHOOTER;
    private double speed;
    private boolean finished;

    public SpinShooter(Shooter shooter, double speed)
    {
        SHOOTER = shooter;
        this.speed = speed;
    }

    @Override
    public void initialize() 
    {
        SHOOTER.setSpeed(speed);
    }

    @Override
    public void execute()
    {
        
    }

    @Override
    public void end(boolean inturrupted)
    {
        SHOOTER.stop();
        
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }
}
