
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
            new BasicPose(0, 88, 0,  false);

        poseList[NamedPose.Travel.ordinal()] = 
            new BasicPose(0, 0, 50,  false);

        poseList[NamedPose.PickFromSubstation.ordinal()] = 
            new DoublePose(
                new BasicPose(0,41, 75, false),
                new BasicPose(-1, 58, 61,  false),
                mode
            );
      
        poseList[NamedPose.PickDriveUpWindow.ordinal()] = 
        new DoublePose(
                new BasicPose(0, 79, -13, false),
                new BasicPose(-9, 93, -18, false),
                mode
            );
           

        poseList[NamedPose.PounceDriveUpWindow.ordinal()] = 
        new BasicPose(0, 87, 84,  false);


      
        poseList[NamedPose.FloorPick.ordinal()] = 
        new DoublePose(
            new BasicPose(-53, 30, 3, false),
            new BasicPose(-51, 27, 50,   false),
            mode
        );

        
        poseList[NamedPose.ScoreL1.ordinal()] = 
            new BasicPose(-25, 11, 80,  false);
      
        poseList[NamedPose.ScoreL2.ordinal()] = 
        new DoublePose(
            new BasicPose(-23, 72, 10, false),
            new BasicPose(-27, 108, 0,   false),
            mode
        );
            
        
        poseList[NamedPose.ScoreL3.ordinal()] = 
        new DoublePose(
            new BasicPose(-39, 116, 0, false),
            new BasicPose(-42, 151, -26,   true),
            mode
        );

       
          
        
    }


    public ArmPose getArmPose(NamedPose p)
    {
        return poseList[p.ordinal()];
    }
   

}
