
package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Swerve;

public class LockSwerveCommand extends CommandBase
{

    final private Swerve SWERVE;
    
    private BooleanSupplier isDone;
    private boolean finished = false;

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

       SWERVE.lockWheels();
    }
    @Override
    public void execute()
    {
      
    }

    @Override
    public void end(boolean inturrupted)
    {
        

     
        if(!inturrupted)
        {
           
        }
    }

    @Override
    public boolean isFinished()
    {
        return isDone.getAsBoolean();
    }


}
