package frc.robot.Arm;

public class BasicPose implements ArmPose
{
    private Double J1Degrees;
    private Double J2Degrees;
    private Double J3Degrees;

    private boolean ExtenderExtended;

   
    public BasicPose(Double J1deg, Double J2deg, Double J3deg,  boolean extended)
    {

       

        J1Degrees = J1deg;
        J2Degrees = J2deg;
        J3Degrees = J3deg;

        ExtenderExtended = extended;


    }
    
    public BasicPose(double J1deg, double J2deg, double J3deg, boolean extended)
    {
        this(Double.valueOf(J1deg), Double.valueOf(J2deg), Double.valueOf(J3deg), extended);
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

    public boolean getExtender()
    {
        return ExtenderExtended;
    }

    public String toString()
    {
        return "ArmPose: { J1: "+getJ1()+" J2: "+getJ2()+" J3: "+getJ3()+" Extended: "+getExtender()+" }";
    }
    


    



}
