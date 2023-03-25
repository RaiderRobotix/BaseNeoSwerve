package frc.robot.autos;

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.swerve.Swerve;

public class SwerveController extends CommandBase
{
 
    private final double p = -13;
    private final double i = 0.00;
    private final double d = 0.00;

    private final double pRot = -.4;
    private final double iRot = 0.00;
    private final double dRot = 0.00;
    

    private PIDController pidX;
    private PIDController pidY;
    private PIDController pidRot;

    private Swerve driveBase;
    private List<Pose2d> poses;

    int progress;
    private double power;

    public SwerveController(Swerve swerve, List<Pose2d> poses)
    {
        this.poses = poses;
        driveBase = swerve;
        pidX = new PIDController(p, i, d);
        pidY = new PIDController(p, i, d);
        pidRot = new PIDController(pRot, iRot, dRot);

        pidX.setTolerance(.05);
        pidY.setTolerance(.05);
        pidRot.setTolerance(1);
        power = 1;
        //pidX.setIntegratorRange(-.8, .8);
        //pidY.setIntegratorRange(-.8, .8);
        //pidRot.setIntegratorRange(-.3, .3);
        pidRot.enableContinuousInput(-180, 180);

        if(poses.size()<2)
        {
            throw new  IllegalArgumentException("Pose list too short.");
        }

       swerve.resetOdometry(poses.get(0));
       progress = 1;
       
       addRequirements(swerve);
    }


    public SwerveController(Swerve swerve, double power, List<Pose2d> poses)
    {
        
        this(swerve, poses);
        this.power = power;
        
    }


    public boolean isFinished()
    {
        return progress>=poses.size();    
    }

    private boolean atCurrentPose()
    {
       
        if(Math.abs(driveBase.getPose().getX()-poses.get(progress).getX())>pidX.getPositionTolerance() )
        {
            //System.out.println("X NO GOOD");
            return false;
        }

        if(Math.abs(driveBase.getPose().getY()-poses.get(progress).getY())>pidY.getPositionTolerance() )
        {
            //System.out.println("Y NO GOOD");
            return false;
        }

        /*Math.abs
            (driveBase.getPose().getRotation().getDegrees()-poses.get(progress).getRotation().getDegrees())
            >pidRot.getPositionTolerance() 
            &&
            Math.abs(Math.abs(poses.get(progress).getRotation().getDegrees())-180)
            ) */
        if(!isAtRotationSetpoint(driveBase.getPose().getRotation().getDegrees(), poses.get(progress).getRotation().getDegrees())) 
        {

            System.out.println("ROT NO GOOD");
            return false;
        }
        return true;
    }

    private boolean isAtRotationSetpoint(double current, double setpoint)
    {
        double tol = pidRot.getPositionTolerance();
        if(Math.abs(current-setpoint)<tol)
        {
            return true;
        }

        double curdistfrom180 = Math.abs(Math.abs(current)-180);
        double setdistfrom180 = Math.abs(Math.abs(setpoint)-180);

        System.out.println("DIST"+curdistfrom180+setdistfrom180);
        return curdistfrom180+setdistfrom180<tol;

    }

    /**
     * should run every 20 ms.
     */
    @Override
    public void execute()
    {
        

        if(atCurrentPose())
        {
            System.out.println("PROGRESS!!!");
            progress++;
        }

        if(isFinished())
        {
            System.out.println("FINISHED!!!!");
            return;
        }

        double x = power*pidX.calculate(driveBase.getPose().getX() , poses.get(progress).getX() );
      
        double y = power*pidX.calculate(driveBase.getPose().getY() , poses.get(progress).getY() );
        pidRot.setSetpoint(poses.get(progress).getRotation().getDegrees());
        double rot = pidRot.calculate(driveBase.getPose().getRotation().getDegrees());

        SmartDashboard.putNumber("x", x);
        SmartDashboard.putNumber("y", y);
        SmartDashboard.putNumber("Rotation", rot);
        SmartDashboard.putNumber("Rot error", pidRot.getPositionError());
        SmartDashboard.putNumber("Rot setpoint", pidRot.getSetpoint());
        driveBase.drive(new Translation2d(x,y), rot, true, false);

    }


    @Override
    public void end(boolean interrupted) {
        driveBase.drive(new Translation2d(0,0), 0, true, false);
    }
}
