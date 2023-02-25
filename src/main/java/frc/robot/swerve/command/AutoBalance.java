package frc.robot.swerve.command;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.swerve.Swerve;

public class AutoBalance extends CommandBase
{
    
    final private Swerve SWERVE;
    private boolean posX; // whether we are coming from the positive x direction

    public AutoBalance(Swerve swerve, boolean PosX )
    {
        SWERVE = swerve;
        this.posX = PosX;
       
        addRequirements(SWERVE);
    }

    @Override
    public void initialize() 
    {
       
    }
    @Override
    public void execute()
    {
       // meters / second 
       double speed = 1*(posX?1:-1);
       SWERVE.drive(new Translation2d(speed, 0), 0, true, false);


    }

    @Override
    public void end(boolean inturrupted)
    {
        SWERVE.drive(new Translation2d(0,0), 0, true, false);
    }

    @Override
    public boolean isFinished()
    {
       double tolerance = 3;
       return SWERVE.getYawDegrees()<tolerance 
              && SWERVE.getPitchDegrees()<tolerance;
    }

    
}
