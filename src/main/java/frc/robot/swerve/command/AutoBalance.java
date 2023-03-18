package frc.robot.swerve.command;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.swerve.Swerve;

public class AutoBalance extends CommandBase
{
    
    final private Swerve SWERVE;
    private final double P = -.15;
    private final double I = 0;
    private final double D = 0;
    private PIDController controller;


    public AutoBalance(Swerve swerve)
    {
        SWERVE = swerve;
        controller = new PIDController(P, I, D);
       
        addRequirements(SWERVE);
    }

    @Override
    public void initialize() 
    {
       controller.setSetpoint(0);
       controller.setTolerance(2.25, .05);
    }

    @Override
    public void execute()
    {
       // meters / second 
       double drive = controller.calculate(SWERVE.getTilt());
       System.out.println("BALANCE VAL: "+drive);
       SWERVE.drive(new Translation2d(drive, 0), 0, true, false);


    }

    @Override
    public void end(boolean inturrupted)
    {
        SWERVE.drive(new Translation2d(0,0), 0, true, false);
    }

    @Override
    public boolean isFinished()
    {
       
       return controller.atSetpoint();
    }

    
}
