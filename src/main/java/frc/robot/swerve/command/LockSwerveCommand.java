
package frc.robot.swerve.command;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.swerve.Swerve;

public class LockSwerveCommand extends Command
{

    final private Swerve SWERVE;
    
    private BooleanSupplier isDone;


    public LockSwerveCommand(Swerve swerve, BooleanSupplier isFinished)
    {
        SWERVE = swerve;
        this.isDone = isFinished;
        addRequirements(SWERVE);
    }

    @Override
    public void initialize() 
    {
        System.out.println("llocckking?");
    }
    @Override
    public void execute()
    {
        SWERVE.lockWheels();

    }

    @Override
    public void end(boolean inturrupted)
    {
        
    }

    @Override
    public boolean isFinished()
    {
        return isDone.getAsBoolean();
    }


}
