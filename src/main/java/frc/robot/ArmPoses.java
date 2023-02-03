package frc.robot;

import frc.robot.subsystems.ArmPose;

public class ArmPoses 
{
    public static ArmPose home = new ArmPose(0, 0, 0, false, false);
    public static ArmPose test = new ArmPose(10, 90, 0, false, false);

    public static ArmPose test2 = new ArmPose(20, -19, 0, false, false);


    public static void setup()
    {
        home.AddAllowedTransition(test, test2);
        test.AddAllowedTransition(home);
        test2.AddAllowedTransition(home);
    }
   

}
