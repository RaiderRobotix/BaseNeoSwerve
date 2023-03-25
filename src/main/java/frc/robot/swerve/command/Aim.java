package frc.robot.swerve.command;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Limelight;
import frc.robot.swerve.Swerve;

public class Aim extends CommandBase
{
    private final Swerve swerve;
    private final Limelight limelight;
    private boolean finished;

    public Aim(Swerve s, Limelight l)
    {
        swerve = s;
        limelight = l;
        finished = false;
    }

    @Override
    public void initialize()
    {
        limelight.setLight(true);
    }

    @Override
    public void execute()
    {
        double offset = limelight.horizontalOffset();
        offset += Constants.limelightOffset;

        if(Math.abs(offset)-0.1 < 0)//if the offset is very close to 0
        {
            finished = true;
            return;
        }
        if(offset > 0)
        {
            swerve.drive(new Translation2d(-0.15, 0), 0, true, false);
            return;
        }
        if(offset < 0)
        {
            swerve.drive(new Translation2d(0.15, 0), 0, true, false);
            return;
        }
    }

    @Override
    public void end(boolean inturrupted)
    {
        limelight.setLight(false);
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }

}
