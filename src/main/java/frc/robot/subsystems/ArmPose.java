package frc.robot.subsystems;

import java.util.ArrayList;

public class ArmPose
{
    private double shoulderDegrees;
    private double elbowDegrees;
    private double wristDegrees;

    private boolean armExtended;
    private boolean clawContracted;

    private ArrayList<ArmPose> allowedTransitions;

    public ArmPose(double shoulderdeg, double elbowdeg, double wristdeg, boolean armOut, boolean clawClosed)
    {

        allowedTransitions = new ArrayList<ArmPose>();

        shoulderDegrees = shoulderdeg;
        elbowDegrees = elbowdeg;
        wristDegrees = wristdeg;

        armExtended = armOut;
        clawContracted = clawClosed;


    }


    public double getShoulder()
    {
        return shoulderDegrees;

    }

    public double getElbow()
    {
        return elbowDegrees;
    }

    public double getWrist()
    {
        return wristDegrees;
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
