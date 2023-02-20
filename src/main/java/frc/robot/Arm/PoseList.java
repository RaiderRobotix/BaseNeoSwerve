
package frc.robot.Arm;

import frc.lib.util.States.GamePieceSupplier;
import frc.robot.Arm.command.ArmPose;
import frc.robot.Arm.command.BasicPose;
import frc.robot.Arm.command.DoublePose;

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
            new BasicPose(0, 125, 0,  false);

        poseList[NamedPose.Travel.ordinal()] = 
            new BasicPose(-3, -10, 90,  false);

        poseList[NamedPose.PickFromSubstation.ordinal()] = 
            new DoublePose(
                new BasicPose(3, -67, 50, false),
                new BasicPose(-2, -66, 75,  false),
                mode
            );
      
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
        new DoublePose(
            new BasicPose(0, 75, 0, false),
            new BasicPose(-5, 115, 0,   false),
            mode
        );
            
        
        poseList[NamedPose.ScoreL3.ordinal()] = 
        new DoublePose(
            new BasicPose(-17, 130, 0, false),
            new BasicPose(-8, 130, 0,   true),
            mode
        );

       
          
        
    }


    public ArmPose getArmPose(NamedPose p)
    {
        return poseList[p.ordinal()];
    }
   

}
