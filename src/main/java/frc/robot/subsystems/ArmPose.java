package frc.robot.subsystems;

import java.util.ArrayList;

public class ArmPose
{
    private double J1Degrees;
    private double J2Degrees;
    private double J3Degrees;

    private boolean armExtended;
    private boolean clawContracted;

    private ArrayList<ArmPose> allowedTransitions;

    public ArmPose(double J1deg, double J2deg, double J3deg, boolean armOut, boolean clawClosed)
    {

        allowedTransitions = new ArrayList<ArmPose>();

        J1Degrees = J1deg;
        J2Degrees = J2deg;
        J3Degrees = J3deg;

        armExtended = armOut;
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

    public boolean getExtender()
    {
        return armExtended;
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

    public ArrayList<ArmPose> getAllowedTransitions()
    {
        return new ArrayList<ArmPose>(allowedTransitions);
    }





}
