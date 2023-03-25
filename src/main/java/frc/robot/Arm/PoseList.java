
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
                new BasicPose(-3, 45, 67,  false),
                mode
            );
      
        poseList[NamedPose.PickDriveUpWindow.ordinal()] = 
        new DoublePose(
                new BasicPose(-3, 74, 0, false),
                new BasicPose(-5, 82, -8, false),
                mode
            );
           

        poseList[NamedPose.PounceDriveUpWindow.ordinal()] = 
        new BasicPose(-5, 82, 84,  false);


      
        poseList[NamedPose.FloorPick.ordinal()] = 
       
            new BasicPose(-65, 37, 90, false);
        

        
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
