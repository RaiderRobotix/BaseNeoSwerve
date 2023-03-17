package frc.robot.Shooter.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Shooter.Shooter;

public class SpinShooter extends CommandBase
{
    private final Shooter SHOOTER;
    private double speed;
    private boolean finished;

    public SpinShooter(Shooter shooter, double speed)
    {
        SHOOTER = shooter;
        this.speed = speed;
        addRequirements(shooter);
    }

    @Override
    public void initialize() 
    {
        
    }

    @Override
    public void execute()
    {
        SHOOTER.setSpeed(speed);
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
