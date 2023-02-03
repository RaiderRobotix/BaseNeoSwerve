package frc.robot.subsystems;

import frc.robot.Constants;
import java.net.CacheRequest;
import java.util.concurrent.CancellationException;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase
{
    //motors
    private CANSparkMax J1;
    private CANSparkMax J1Follow;
    private CANSparkMax J2;
    private CANSparkMax J2Follow;
    private CANSparkMax J3;
    //private DoubleSolenoid extender;
    //private DoubleSolenoid claw;

    private ArmPose currentPose;

    // pose should be all zeros
    public Arm(ArmPose pose)
    {

        // TODO set soft limits for arm motors (I would appreciate the robot not exploding)

        
        currentPose = pose;
        
        J1 = new CANSparkMax(Constants.Arm.J1MotorID, MotorType.kBrushless);
        J1.getEncoder().setPositionConversionFactor(360.0/(144.0));

        J1.setSoftLimit(SoftLimitDirection.kReverse, -25);
        J1.setSoftLimit(SoftLimitDirection.kForward, 25);

        SparkMaxPIDController controller = J1.getPIDController();
        controller.setP(.025,0);
        controller.setI(.00,0);
        controller.setD(.0,0);
        controller.setFF(.0,0);
        controller.setOutputRange(-.3, .3);
       
        J1Follow = new CANSparkMax(Constants.Arm.J1FollowMotorID, MotorType.kBrushless);
       

        J2 = new CANSparkMax(Constants.Arm.J2MotorID, MotorType.kBrushless);
        J2.getEncoder().setPositionConversionFactor(360.0/100.0);

        J2.setSoftLimit(SoftLimitDirection.kReverse, -100);
        J2.setSoftLimit(SoftLimitDirection.kForward, 100);

        controller = J2.getPIDController();
        controller.setP(.01,0);
        controller.setI(.00,0);
        controller.setD(.0,0);
        controller.setFF(.0,0);
        controller.setOutputRange(-.3, .3);

        J2Follow = new CANSparkMax(Constants.Arm.J2FollowMotorID, MotorType.kBrushless);
        
        
        J3 = new CANSparkMax(Constants.Arm.J3MotorID, MotorType.kBrushless);
        J3.getEncoder().setPositionConversionFactor(360.0/60.0);

        controller = J3.getPIDController();
        controller.setP(.025,0);
        controller.setI(.00,0);
        controller.setD(.0,0);
        controller.setFF(.0,0);
        controller.setOutputRange(-.3, .3);

        //extender = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Arm.forwardExtenderChannel, Constants.Arm.reverseExtenderChannel);
        setExtender(false);
        //claw = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Arm.forwardClawChannel, Constants.Arm.reverseClawChannel);
        setClaw(false);



        J1Follow.follow(J1, true);
        J2Follow.follow(J2, true);



        CANSparkMax[] motors = {J1, J1Follow, J2, J2Follow, J3};
        initializeMotors(motors);


        ShuffleboardTab tab = Shuffleboard.getTab("Arm");
        
             
             

    }

    @Override
    public void periodic()
    {
        // despite the lack of roundness, this is very sensual.
        SmartDashboard.putNumber("J1", J1.getEncoder().getPosition());
        SmartDashboard.putNumber("J2", J2.getEncoder().getPosition());
        SmartDashboard.putNumber("J3", J3.getEncoder().getPosition());
        SmartDashboard.putNumber("J1 Current", J1.getOutputCurrent());
        SmartDashboard.putNumber("J2 Current", J2.getOutputCurrent());
        SmartDashboard.putNumber("J3 Current", J3.getOutputCurrent());
        
        adoptPose(currentPose);
    }

    private void initializeMotors(CANSparkMax[] motors)
    {
        for(CANSparkMax motor: motors)
        {
            motor.setIdleMode(IdleMode.kBrake);
            motor.setSmartCurrentLimit(40,15 );

           
            
            motor.setClosedLoopRampRate(2);
            motor.setOpenLoopRampRate(2);



            
            motor.enableSoftLimit(SoftLimitDirection.kForward, true);
            motor.enableSoftLimit(SoftLimitDirection.kReverse, true);
    
            motor.burnFlash();
            
    
            motor.getEncoder().setPosition(0);
        }
    }

    private void setClaw(boolean extended)
    {
        /*if(extended)
        {
            claw.set(Value.kForward);
            return;
        }
        
        claw.set(Value.kReverse);*/
    }

    //public boolean getClaw()
    {
        /*return claw.get()==Value.kForward;*/
    }

    
    private void setExtender(boolean extended)
    {
       /*/ if(extended)
        {
            extender.set(Value.kForward);
            return;
        }
        
        extender.set(Value.kReverse);*/
    }

    //public boolean getExtender()
    {
       // return extender.get() == Value.kForward;
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
        if(currentPose== null )
        {
            System.out.println("null arm pose");
            return;
        }

        
        if(currentPose!=null && (!pose.isAllowedTransition(currentPose) && currentPose!=pose))
        {
            System.out.println("Invalid pose transition.");
        }
        currentPose = pose;
        
        //System.out.println("working?");
        setArmPosition(pose.getJ1(),pose.getJ2(), pose.getJ3());
        setExtender(pose.getExtender());
        setClaw(pose.getClaw());
    }


    public boolean isAtPose(ArmPose pose)
    {
        double tolerance = .1;
        return isWithin(pose.getJ1(), J1.getEncoder().getPosition(), tolerance)
            && isWithin(pose.getJ2(), J2.getEncoder().getPosition(), tolerance)
            && isWithin(pose.getJ3(), J3.getEncoder().getPosition(), tolerance);
          //  && pose.getClaw() == getClaw();
           // && pose.getExtender() == getExtender();
    }

    private boolean isWithin(double a, double b, double within)
    {
        return Math.abs(a-b)<within;
    }

    
}
