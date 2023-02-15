package frc.robot.Arm;

import java.util.ArrayList;




public class PoseList
{
    

    private ArmPose[] poseList;

    public PoseList()
    {
        poseList = new ArmPose[NamedPose.values().length];
       

        // General
        poseList[NamedPose.Home.ordinal()] = 
            new ArmPose(0, 0, 0,  false);
      
        poseList[NamedPose.PouncePreScore.ordinal()] = 
            new ArmPose(5, 130, 0,  false);

        poseList[NamedPose.Travel.ordinal()] = 
            new ArmPose(0, 3, 80,  false);

        poseList[NamedPose.PickFromSubstation.ordinal()] = 
            new ArmPose(0, -60, 68,  false);
      
        poseList[NamedPose.PickDriveUpWindow.ordinal()] = 
            new ArmPose(0, -90, 0,  false);

        poseList[NamedPose.PounceDriveUpWindow.ordinal()] = 
            new ArmPose(0, 90, 0,  false);


        // Cone
        poseList[NamedPose.FloorPickCone.ordinal()] = 
            new ArmPose(7.5, 16.5, 0,  false);

        
        poseList[NamedPose.ConeScoreL1.ordinal()] = 
            new ArmPose(-24, 24, 0,  false);
      
        poseList[NamedPose.ConeScoreL2.ordinal()] = 
            new ArmPose(-5, 100, -0,  false);

        poseList[NamedPose.ConeScoreL3.ordinal()] = 
            new ArmPose( -13, 130, 0,  false);



        // Cube
        poseList[NamedPose.FloorPickCube.ordinal()] = 
            new ArmPose(7.5, 16.5, 0,  false);
        

        poseList[NamedPose.CubeScoreL1.ordinal()] = 
            new ArmPose(-24, 24, 0,  false);
      
        poseList[NamedPose.CubeScoreL2.ordinal()] = 
            new ArmPose(-5, 100,0,  false);

        poseList[NamedPose.CubeScoreL3.ordinal()] = 
            new ArmPose(-13, 130, 0,  false);
          
        
    }


    public ArmPose getArmPose(NamedPose p)
    {
        return poseList[p.ordinal()];
    }
   

}
