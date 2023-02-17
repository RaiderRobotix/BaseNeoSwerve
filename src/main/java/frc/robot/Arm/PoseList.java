package frc.robot.Arm;

import frc.lib.util.States.GamePieceSupplier;

public class PoseList
{
    

    
    private ArmPose[] poseList;

    public PoseList(GamePieceSupplier mode)
    {
        poseList = new ArmPose[NamedPose.values().length];
       

        // General
        poseList[NamedPose.Home.ordinal()] = 
            new BasicPose(0, 0, 0,  false);
      
        poseList[NamedPose.PouncePreScore.ordinal()] = 
            new BasicPose(5, 115, 0,  false);

        poseList[NamedPose.Travel.ordinal()] = 
            new BasicPose(-7, -24, 69,  false);

        poseList[NamedPose.PickFromSubstation.ordinal()] = 
            new BasicPose(-6, -75, 35,  false);
      
        poseList[NamedPose.PickDriveUpWindow.ordinal()] = 
            new BasicPose(0, -90, 0,  false);

        poseList[NamedPose.PounceDriveUpWindow.ordinal()] = 
            new BasicPose(0, 90, 0,  false);


        // Cone
        poseList[NamedPose.FloorPick.ordinal()] = 
            new BasicPose(7.5, 16.5, 0,  false);

        
        poseList[NamedPose.ScoreL1.ordinal()] = 
            new BasicPose(-24, 24, 0,  false);
      
        poseList[NamedPose.ScoreL2.ordinal()] = 
            new BasicPose(-5, 100, -0,  false);

        poseList[NamedPose.ScoreL3.ordinal()] = 
            new BasicPose( -13, 115, 0,  false);



       
          
        
    }


    public ArmPose getArmPose(NamedPose p)
    {
        return poseList[p.ordinal()];
    }
   

}
