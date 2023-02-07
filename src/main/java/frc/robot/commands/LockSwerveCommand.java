
    package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.ArmPoses;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeState;
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
        System.out.println("llocckking?");
        addRequirements(SWERVE);
    }

    @Override
    public void initialize() 
    {
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
            //TODO uncomment once arm poses are correct and junk
           // new PresentPiece(INTAKE, ARM);
        }
    }

    @Override
    public boolean isFinished()
    {
        return isDone.getAsBoolean();
    }


}
