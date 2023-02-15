package frc.robot.Arm;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ArmCommand extends CommandBase
{
    private Arm arm;
    private ArmPose pose;

    public ArmCommand(Arm s_Arm, ArmPose pose)
    {
       
        this.arm = s_Arm;
        this.pose = pose;
        addRequirements(s_Arm);
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
        if(! inturrupted)
        {
        System.out.println("posed "+inturrupted);
        }
    }

    @Override
    public boolean isFinished()
    {
        
        return arm.isAtPose(pose);
    }


    public static void ScheduleArmCommandToPose()
    {
        
    }
}
