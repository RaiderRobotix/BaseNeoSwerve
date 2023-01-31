package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight
{
    // this class exists to give access to the limelight in a human readable way.
    // it does not include all of the limelight's settings and features. if you happen to need one of these, add a method
    private NetworkTable limelight;

    public Limelight()
    {
        limelight =  NetworkTableInstance.getDefault().getTable("limelight");
    }

    // returns whether the limelight has found a target.
    public boolean hasTarget()
    {
        // 1 if limelight has a target, 0 if not.
        // this doesn't appear to behavew like I thought it would.
        double target = limelight.getEntry("tv").getDouble(0);

        return target==1;
    }
    
    // the horizontal offset from the center of the limelight's view, in degrees.
    public double horizontalOffset()
    {
        return limelight.getEntry("tx").getDouble(0);
    }

    // the vertical offset from the center of the limelight's view, in degrees.
    public double verticalOffset()
    {
        return limelight.getEntry("ty").getDouble(0);
    }

    // the size, in percentage of camera space, that the target takes up.
    public double targetSize()
    {
        
        return limelight.getEntry("ta").getDouble(0);
    }

    // the rotation or skew of the target in degrees.
    public double targetRotation()
    {
        return limelight.getEntry("ts").getDouble(0);
    }


}

