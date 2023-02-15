package frc.robot.Arm;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class ArmCommand extends CommandBase
{
    private Arm arm;
    private ArmPose pose;

    private ArmCommand(Arm s_Arm, ArmPose pose)
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


    public static Command PlotPath( NamedPose dest, Arm arm)
    {
        // a decent proxy for whether a gamepiece is held, I guess
        boolean hasPiece = arm.getClaw();


        ArmPose current = arm.getCurrentPose();
        ArmPose to = new PoseList().getArmPose(dest);

        ArrayList<Command> sequence = new ArrayList<Command>();
       
        if(arm.getExtender() && !to.getExtender())
        {
            sequence.add(new InstantCommand(()->arm.setExtender(false)));
            sequence.add(new WaitCommand(.5));
        }


        if(hasPiece)
        {
            sequence.addAll(AdjustForHeldPiece(to, arm));
        }

        sequence.add(new ArmCommand(arm,to));

        if(to.getExtender())
        {
            sequence.add(new InstantCommand(()->arm.setExtender(true)));
        }


        return Commands.sequence((Command[])sequence.toArray());
    }

    private static ArrayList<Command> AdjustForHeldPiece(ArmPose to, Arm arm)
    {

        // pull the wrist up if J2 is passing 0.
        ArrayList<Command> sequence = new ArrayList<Command>();
        double crossTolerance = 5;
        ArmPose current = arm.getCurrentPose();

        if(Math.signum(to.getJ2())== Math.signum(current.getJ2()-crossTolerance)
         && Math.signum(to.getJ2())== Math.signum(current.getJ2()+crossTolerance))
        {
            crossTolerance*=Math.signum(to.getJ2());
            ArmPose p = new ArmPose((double)to.getJ1(), crossTolerance, 80.0, false);
            sequence.add(new ArmCommand(arm, p));
        }

        return sequence;
    }


}
