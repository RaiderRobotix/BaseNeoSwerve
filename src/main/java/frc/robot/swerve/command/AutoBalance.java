package frc.robot.swerve.command;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.swerve.Swerve;

public class AutoBalance extends Command
{
    
    final private Swerve SWERVE;
    private double tol = 2;
    private double oldTilt;
    private boolean finished;


    public AutoBalance(Swerve swerve)
    {
        SWERVE = swerve;
        finished = false;
       
        addRequirements(SWERVE);
    }

    @Override
    public void initialize() 
    {
       oldTilt = SWERVE.getTilt();
       finished = false;
       System.out.println("Balance Begin");
    }

    @Override
    public void execute()
    {
       // meters / second 
       //System.out.println("GOING  old-2: " + (oldTilt-tol)+" current:"+SWERVE.getTilt());
       if(oldTilt-tol<SWERVE.getTilt())
       {
            //System.out.println("Actually going!");
            SWERVE.drive(new Translation2d(.6, 0), 0, true, true);
            return;
       }
       //System.out.println("NO GO");
       finished = true;

    }

    @Override
    public void end(boolean inturrupted)
    {
        System.out.println("Balance End");
        SWERVE.drive(new Translation2d(0,0), 0, true, false);
    }

    @Override
    public boolean isFinished()
    {
       
       return finished;
    }

    
}
