package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Arm.Arm;
import frc.robot.Arm.NamedPose;
import frc.robot.Arm.command.ArmCommand;
import frc.robot.Arm.command.ArmPose;

public class AutoPoseCommand extends CommandBase
{
    Arm arm;
    ArmPose pose;
    Command poseCommand;
    private static ArmPose last;

    public AutoPoseCommand(NamedPose dest, Arm arm)
    {
        pose = arm.getPoseList().getArmPose(dest);
        this.arm = arm;

        // warning new
        if(last == null)
        {
            last=arm.getCurrentPose();
        }

        // change to null if problem.
        poseCommand = ArmCommand.PlotPath(dest, last, arm);
        last = pose;
    }

    public static void reset()
    {
        last=null;
    }

    @Override
    public void initialize() 
    {
        new ScheduleCommand(((SequentialCommandGroup)poseCommand)).schedule();
       
        
    }
    @Override
    public void execute()
    {
       

    }

    @Override
    public void end(boolean inturrupted)
    {
        
    }

    @Override
    public boolean isFinished()
    {
        return arm.isAtPose(pose);
    }


}


