package frc.robot.subsystems;

import java.util.ArrayList;

public class ArmPose
{
    private double J1Degrees;
    private double J2Degrees;
    private double J3Degrees;

    private boolean clawContracted;

    private ArrayList<ArmPose> allowedTransitions;

    public ArmPose(double J1deg, double J2deg, double J3deg,  boolean clawClosed)
    {

        allowedTransitions = new ArrayList<ArmPose>();

        J1Degrees = J1deg;
        J2Degrees = J2deg;
        J3Degrees = J3deg;

        clawContracted = clawClosed;


    }


    public double getJ1()
    {
        return J1Degrees;

    }

    public double getJ2()
    {
        return J2Degrees;
    }

    public double getJ3()
    {
        return J3Degrees;
    }

    public boolean getClaw()
    {
        return clawContracted;
    }


    public void AddAllowedTransition(ArmPose...armPoses)
    {
        for(ArmPose pose: armPoses)
        {
            if(pose== null || pose == this)
            {
                throw new IllegalArgumentException("valid pose cannot be self or null.");
            }
            allowedTransitions.add(pose);
        }
    }

    public boolean isAllowedTransition(ArmPose pose)
    {
        if(allowedTransitions.size()==0)
        {
            return true;
        }

        return allowedTransitions.contains(pose);
    }





}
