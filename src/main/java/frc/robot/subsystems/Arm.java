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
    private CANSparkMax J1;
    private CANSparkMax J1Follow;
    private CANSparkMax J2;
    private CANSparkMax J2Follow;
    private CANSparkMax J3;
    private DoubleSolenoid extender;
    private DoubleSolenoid claw;

    private ArmPose currentPose;

    public Arm()
    {

        // TODO set soft limits for arm motors (I would appreciate the robot not exploding)

        
        
        CANSparkMax[] motors = {J1, J1Follow, J2, J2Follow, J3};
        J1 = new CANSparkMax(Constants.Arm.J1MotorID, MotorType.kBrushless);
        
        J1.getEncoder().setPositionConversionFactor((1.0/144)*360);

        J1Follow = new CANSparkMax(Constants.Arm.J1FollowMotorID, MotorType.kBrushless);
        J2 = new CANSparkMax(Constants.Arm.J2MotorID, MotorType.kBrushless);
      
        J2.getEncoder().setPositionConversionFactor((1.0/100)*360);

        J2Follow = new CANSparkMax(Constants.Arm.J2FollowMotorID, MotorType.kBrushless);
        J3 = new CANSparkMax(Constants.Arm.J3MotorID, MotorType.kBrushless);
        J1.getEncoder().setPositionConversionFactor((1.0/30)*360);

        extender = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Arm.forwardExtenderChannel, Constants.Arm.reverseExtenderChannel);
        setExtender(false);
        claw = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Arm.forwardClawChannel, Constants.Arm.reverseClawChannel);
        setClaw(false);

        J1Follow.follow(J1);
        J2Follow.follow(J2);

        initializeMotors(motors);



        ShuffleboardTab tab = Shuffleboard.getTab("Arm");


        tab.add("J1", J1.getEncoder());
        tab.add("J2", J2.getEncoder());
        tab.add("J3", J3.getEncoder());

        currentPose = null;
        
             
             

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

    private void setClaw(boolean extended)
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

    
    private void setExtender(boolean extended)
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
    private void setArmPosition(double J1Ref, double J2Ref, double J3Ref )
    {
         J1.getPIDController().setReference(J1Ref, ControlType.kPosition);
         J3.getPIDController().setReference(J3Ref, ControlType.kPosition);
         J2.getPIDController().setReference(J2Ref, ControlType.kPosition);
    }


    public void adoptPose(ArmPose pose)
    {
        if(currentPose!=null && !pose.getAllowedTransitions().contains(currentPose) )
        {
            System.out.println("Invalid pose occured.");
        }
        setArmPosition(pose.getJ1(),pose.getJ2(), pose.getJ3());
        setExtender(pose.getExtender());
        setClaw(pose.getClaw());
    }

    
}
