package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.ArmPose;

public class ArmCommand extends CommandBase
{
    private Arm arm;
    private ArmPose pose;
    private boolean finished = false;

    public ArmCommand(Arm arm, ArmPose pose)
    {
       
        this.arm = arm;
        this.pose = pose;
        addRequirements(arm);
    }

    @Override
    public void initialize()    
    {
       arm.adoptPose(pose);
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
