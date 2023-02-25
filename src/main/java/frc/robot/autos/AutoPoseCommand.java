package frc.robot.autos;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import frc.robot.Arm.Arm;
import frc.robot.Arm.NamedPose;
import frc.robot.Arm.command.ArmCommand;
import frc.robot.Arm.command.ArmPose;

public class AutoPoseCommand extends CommandBase
{
    Arm arm;
    ArmPose pose;
    Command poseCommand;

    public AutoPoseCommand(NamedPose dest, Arm arm)
    {
        pose = arm.getPoseList().getArmPose(dest);
        this.arm = arm;
        poseCommand = ArmCommand.PlotPath(dest, null, arm);
    }

    @Override
    public void initialize() 
    {
        new ScheduleCommand(poseCommand).schedule();
       
        
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


