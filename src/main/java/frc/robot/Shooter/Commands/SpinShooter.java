package frc.robot.Shooter.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Shooter.Intake;
import frc.robot.Shooter.Shooter;

public class SpinShooter extends CommandBase
{
    private final Shooter SHOOTER;
    private final Intake intake;
    private double speed;
    private boolean finished;

    public SpinShooter(Shooter shooter, Intake intake, double speed)
    {
        SHOOTER = shooter;
        this.speed = speed;
        this.intake = intake;
        addRequirements(shooter);
    }

    @Override
    public void initialize() 
    {
        intake.setArms(false);
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
        intake.setArms(true);
        
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }
}
