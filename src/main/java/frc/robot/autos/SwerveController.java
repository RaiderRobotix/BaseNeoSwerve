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
 
    private final double p = -8;
    private final double i = 0.00;
    private final double d = 0.00;

    private final double pRot = -.15;
    private final double iRot = 0.00;
    private final double dRot = 0.00;
    

    private PIDController pidX;
    private PIDController pidY;
    private PIDController pidRot;

    private Swerve driveBase;
    private List<Pose2d> poses;

    int progress;

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

        pidX.setIntegratorRange(-.8, .8);
        pidY.setIntegratorRange(-.8, .8);
        pidRot.setIntegratorRange(-.3, .3);
        //pidRot.enableContinuousInput(0, 360);

        if(poses.size()<2)
        {
            throw new  IllegalArgumentException("Pose list too short.");
        }

       swerve.resetOdometry(poses.get(0));
       progress = 1;
       
       addRequirements(swerve);
    }


    public boolean isFinished()
    {
        return progress>=poses.size();    
    }

    private boolean atCurrentPose()
    {
        //SmartDashboard.putNumber("Rotation Error:", pidRot.getPositionError());
        if(Math.abs(driveBase.getPose().getX()-poses.get(progress).getX())>pidX.getPositionTolerance() )
        {
            return false;
        }

        if(Math.abs(driveBase.getPose().getY()-poses.get(progress).getY())>pidY.getPositionTolerance() )
        {
            return false;
        }

        /*if(Math.abs
            (driveBase.getPose().getRotation().getDegrees()-poses.get(progress).getRotation().getDegrees())
            >pidRot.getPositionTolerance() )
        {
            //return false;
        }*/
        return true;
    }

    /**
     * should run every 20 ms.
     */
    @Override
    public void execute()
    {
        

        //System.out.println("running ppidpidpdip errr: "+pidX.atSetpoint()+"   "+poses.get(progress).getX());
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

        double x = pidX.calculate(driveBase.getPose().getX() , poses.get(progress).getX() );
      
        double y = pidX.calculate(driveBase.getPose().getY() , poses.get(progress).getY() );
        double rot = pidRot.calculate(driveBase.getPose().getRotation().getDegrees() , poses.get(progress).getRotation().getDegrees());

        driveBase.drive(new Translation2d(x,y), rot, false, false);

    }


    @Override
    public void end(boolean interrupted) {
        driveBase.drive(new Translation2d(0,0), 0, true, false);
    }
}
