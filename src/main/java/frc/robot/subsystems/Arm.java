package frc.robot.subsystems;

import frc.robot.Constants;
import java.net.CacheRequest;
import java.util.concurrent.CancellationException;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase
{
    //motors
    private CANSparkMax shoulder;
    private CANSparkMax shoulderFollow;
    private CANSparkMax elbow;
    private CANSparkMax elbowFollow;
    private CANSparkMax wrist;
    private DoubleSolenoid extender;
    private DoubleSolenoid claw;

    public Arm()
    {

        // TODO set soft limits for arm motors (I would appreciate the robot not exploding)

        
        
        CANSparkMax[] motors = {shoulder, shoulderFollow, elbow, elbowFollow, wrist};
        shoulder = new CANSparkMax(Constants.Arm.shoulderMotorID, MotorType.kBrushless);
        
        shoulder.getEncoder().setPositionConversionFactor((1.0/144)*360);

        shoulderFollow = new CANSparkMax(Constants.Arm.shoulderFollowMotorID, MotorType.kBrushless);
        elbow = new CANSparkMax(Constants.Arm.elbowMotorID, MotorType.kBrushless);
      
        elbow.getEncoder().setPositionConversionFactor((1.0/100)*360);

        elbowFollow = new CANSparkMax(Constants.Arm.elbowFollowMotorID, MotorType.kBrushless);
        wrist = new CANSparkMax(Constants.Arm.wristMotorID, MotorType.kBrushless);
        shoulder.getEncoder().setPositionConversionFactor((1.0/30)*360);

        extender = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Arm.forwardExtenderChannel, Constants.Arm.reverseExtenderChannel);
        setExtender(false);
        claw = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Arm.forwardClawChannel, Constants.Arm.reverseClawChannel);
        setClaw(false);

        shoulderFollow.follow(shoulder);
        elbowFollow.follow(elbow);

        initializeMotors(motors);



        ShuffleboardTab tab = Shuffleboard.getTab("Arm");


        tab.add("shoulder", shoulder.getEncoder());
        tab.add("elbow", elbow.getEncoder());
        tab.add("wrist", wrist.getEncoder());
        
             
             

    }

    private void initializeMotors(CANSparkMax[] motors)
    {
        for(CANSparkMax motor: motors)
        {
            motor.setIdleMode(IdleMode.kBrake);
            motor.setSmartCurrentLimit(40,15 );

            SparkMaxPIDController controller = motor.getPIDController();
            controller.setP(.05,0);
            controller.setI(.00,0);
            controller.setD(.0,0);
            controller.setFF(.0,0);
            controller.setOutputRange(-.2, .2);
            motor.burnFlash();

            motor.getEncoder().setPosition(0);
        }
    }

    public void setClaw(boolean extended)
    {
        if(extended)
        {
            claw.set(Value.kForward);
            return;
        }
        
        claw.set(Value.kReverse);
    }

    public boolean getClaw()
    {
        return claw.get()==Value.kForward;
    }

    
    public void setExtender(boolean extended)
    {
        if(extended)
        {
            extender.set(Value.kForward);
            return;
        }
        
        extender.set(Value.kReverse);
    }

    public boolean getExtender()
    {
        return extender.get() == Value.kForward;
    }

    // in revolutions
    public void setArmPosition(double shoulderRef, double elbowRef, double wristRef )
    {
         shoulder.getPIDController().setReference(shoulderRef, ControlType.kPosition);
         wrist.getPIDController().setReference(wristRef, ControlType.kPosition);
         elbow.getPIDController().setReference(elbowRef, ControlType.kPosition);
    }


    public void adoptPose(ArmPose pose)
    {
        setArmPosition(pose.getShoulder(),pose.getElbow(), pose.getWrist());
        setExtender(pose.getExtender());
        setClaw(pose.getClaw());
    }

    
}
