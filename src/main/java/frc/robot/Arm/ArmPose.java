package frc.robot.Arm;

import java.util.ArrayList;

public class ArmPose
{
    private Double J1Degrees;
    private Double J2Degrees;
    private Double J3Degrees;

    private boolean clawContracted;

    private ArrayList<ArmPose> allowedTransitions;

    public ArmPose(Double J1deg, Double J2deg, Double J3deg,  boolean clawClosed)
    {

        allowedTransitions = new ArrayList<ArmPose>();

        J1Degrees = J1deg;
        J2Degrees = J2deg;
        J3Degrees = J3deg;

        clawContracted = clawClosed;


    }


    public ArmPose(double J1deg, double J2deg, double J3deg, boolean clawClosed)
    {
        this(Double.valueOf(J1deg), Double.valueOf(J2deg), Double.valueOf(J3deg), clawClosed);
    }


    public Double getJ1()
    {
        return J1Degrees;

    }

    public Double getJ2()
    {
        return J2Degrees;
    }

    public Double getJ3()
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
