package frc.robot.subsystems;

import frc.robot.Constants;
import java.net.CacheRequest;
import java.util.concurrent.CancellationException;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase
{
    //motors
    private CANSparkMax shoulder;
    private CANSparkMax shoulderFollow;
    private CANSparkMax elbow;
    private CANSparkMax elbowFollow;
    private CANSparkMax wrist;
    private Solenoid extender;

    public Arm()
    {
        shoulder = new CANSparkMax(Constants.Arm.shoulderMotorID, MotorType.kBrushless);
        shoulderFollow = new CANSparkMax(Constants.Arm.shoulderFollowMotorID, MotorType.kBrushless);
        elbow = new CANSparkMax(Constants.Arm.elbowMotorID, MotorType.kBrushless);
        elbowFollow = new CANSparkMax(Constants.Arm.elbowFollowMotorID, MotorType.kBrushless);
        wrist = new CANSparkMax(Constants.Arm.wristMotorID, MotorType.kBrushless);

        extender = new Solenoid(PneumaticsModuleType.REVPH, Constants.Arm.extenderChannel);

        shoulderFollow.follow(shoulder);
        elbowFollow.follow(elbow);
    }

    public void setArmPosition(/*3 coordinate angle*/ boolean extended)
    {
        extender.set(extended);//TODO need to unextend first, but extend last.
    }
}
