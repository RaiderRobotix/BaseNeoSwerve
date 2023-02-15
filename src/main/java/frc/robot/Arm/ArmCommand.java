package frc.robot.Arm;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

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


    public static void PlotPath( NamedPose dest, Arm arm)
    {
        // a decent proxy for whether a gamepiece is held, I guess
        boolean hasPiece = arm.getClaw();


        ArmPose current = arm.getCurrentPose();
        ArmPose to = new PoseList().getArmPose(dest);

        ArrayList<Command> sequence = new ArrayList<Command>();
        if(hasPiece)
        {
            sequence.addAll(AdjustForHeldPiece(to, arm));
        }

        
        //Commands.sequence(null)
    }

    private static ArrayList<Command> AdjustForHeldPiece(ArmPose to, Arm arm)
    {

        // pull the wrist up if J2 is passing 0.
        ArrayList<Command> sequence = new ArrayList<Command>();
        double crossTolerance = 5;

        (Math.signum(J2Ref)== Math.signum(J2.getEncoder().getPosition()-crossTolerance)
         && Math.signum(J2Ref)== Math.signum(J2.getEncoder().getPosition()+crossTolerance)
        {
            crossTolerance*=Math.signum(to.getJ2());
            sequence.add(new ArmCommand(to.getJ1(), crossTolerance, 80));
        }

        return sequence;
    }


}
