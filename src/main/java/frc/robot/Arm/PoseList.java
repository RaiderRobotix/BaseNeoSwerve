
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
                new BasicPose(0,39, 69, false),
                new BasicPose(-1, 53, 61,  false),
                mode
            );
      
        poseList[NamedPose.PickDriveUpWindow.ordinal()] = 
            new BasicPose(-3, 92, 71,  false);

        poseList[NamedPose.PounceDriveUpWindow.ordinal()] = 
            new DoublePose(
                new BasicPose(-0, 79, -26, false),
                new BasicPose(-56, 92, -29, false),
                mode
            );


        // Cone
        poseList[NamedPose.FloorPick.ordinal()] = 
        new DoublePose(
            new BasicPose(-54, 39, -24, false),
            new BasicPose(-61, 40, 48,   false),
            mode
        );

        
        poseList[NamedPose.ScoreL1.ordinal()] = 
            new BasicPose(-25, 11, 80,  false);
      
        poseList[NamedPose.ScoreL2.ordinal()] = 
        new DoublePose(
            new BasicPose(-23, 68, 10, false),
            new BasicPose(-28, 108, 0,   false),
            mode
        );
            
        
        poseList[NamedPose.ScoreL3.ordinal()] = 
        new DoublePose(
            new BasicPose(-39, 111, -7, false),
            new BasicPose(-47, 148, -27,   true),
            mode
        );

       
          
        
    }


    public ArmPose getArmPose(NamedPose p)
    {
        return poseList[p.ordinal()];
    }
   

}
