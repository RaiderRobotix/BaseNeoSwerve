package frc.robot.Arm;


import frc.lib.util.States.GamePieceSupplier;
import frc.robot.Constants;
import frc.robot.Arm.command.ArmPose;
import frc.robot.Arm.command.BasicPose;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
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
    private DoubleSolenoid extender;
    private DoubleSolenoid claw;

    private ArmPose currentPose;

    private PoseList poses;
    // pose should be all zeros
    public Arm(NamedPose pose, PneumaticHub ph, GamePieceSupplier mode)
    {

        poses = new PoseList(mode);
        currentPose = poses.getArmPose(pose);
        
        J1 = new CANSparkMax(Constants.Arm.J1MotorID, MotorType.kBrushless);
        J1.getEncoder().setPositionConversionFactor(360.0/(ArmConfig.Joint1.gearRatio));

        J1.setIdleMode(IdleMode.kBrake);

        J1.setSoftLimit(SoftLimitDirection.kReverse, ArmConfig.Joint1.lowerLimit);
        J1.setSoftLimit(SoftLimitDirection.kForward, ArmConfig.Joint1.upperLimit);

        SparkMaxPIDController controller = J1.getPIDController();
        controller.setP(ArmConfig.Joint1.pValue,0);
        controller.setI(ArmConfig.Joint1.iValue,0);
        controller.setD(ArmConfig.Joint1.dValue,0);
        controller.setFF(ArmConfig.Joint1.ffValue,0);
        controller.setOutputRange(-ArmConfig.Joint1.maxPower, ArmConfig.Joint1.maxPower);
       
        J1Follow = new CANSparkMax(Constants.Arm.J1FollowMotorID, MotorType.kBrushless);
       

        J2 = new CANSparkMax(Constants.Arm.J2MotorID, MotorType.kBrushless);
        J2.getEncoder().setPositionConversionFactor(360.0/(ArmConfig.Joint2.gearRatio));

        J2.setIdleMode(IdleMode.kCoast);
      

        J2.setSoftLimit(SoftLimitDirection.kReverse, ArmConfig.Joint2.lowerLimit);
        J2.setSoftLimit(SoftLimitDirection.kForward, ArmConfig.Joint2.upperLimit);

        controller = J2.getPIDController();
        controller.setP(ArmConfig.Joint2.pValue,0);
        controller.setI(ArmConfig.Joint2.iValue,0);
        controller.setD(ArmConfig.Joint2.dValue,0);
        controller.setFF(ArmConfig.Joint2.ffValue,0);
        controller.setOutputRange(-ArmConfig.Joint2.maxPower, ArmConfig.Joint2.maxPower);

        J2Follow = new CANSparkMax(Constants.Arm.J2FollowMotorID, MotorType.kBrushless);
        J2Follow.setIdleMode(IdleMode.kBrake);
        
        J3 = new CANSparkMax(Constants.Arm.J3MotorID, MotorType.kBrushless);
        J3.getEncoder().setPositionConversionFactor(360.0/ArmConfig.Joint3.gearRatio);

        J3.setIdleMode(IdleMode.kCoast);

        J3.setSoftLimit(SoftLimitDirection.kReverse, ArmConfig.Joint3.lowerLimit);
        J3.setSoftLimit(SoftLimitDirection.kForward, ArmConfig.Joint3.upperLimit);

        controller = J3.getPIDController();
        controller.setP(ArmConfig.Joint3.pValue,0);
        controller.setI(ArmConfig.Joint3.iValue,0);
        controller.setD(ArmConfig.Joint3.dValue,0);
        controller.setFF(ArmConfig.Joint3.ffValue,0);
        controller.setOutputRange(-ArmConfig.Joint3.maxPower, ArmConfig.Joint3.maxPower);

        extender =  ph.makeDoubleSolenoid(Constants.Arm.forwardExtenderChannel, Constants.Arm.reverseExtenderChannel);
        setExtender(false);
        claw = ph.makeDoubleSolenoid( Constants.Arm.forwardClawChannel, Constants.Arm.reverseClawChannel);
        setClaw(false);


        
        J1Follow.follow(J1, true);
        J2Follow.follow(J2, true);



        CANSparkMax[] motors = {J1, J1Follow, J2, J2Follow, J3};
        initializeMotors(motors);


       
        
             
             

    }

    public PoseList getPoseList()
    {
        return poses;
    }

    /**
     * get the current state of the arm as an armpose
     * @return
     */
    public ArmPose getCurrentPose()
    {
        return new BasicPose(J1.getEncoder().getPosition(), J2.getEncoder().getPosition(), J3.getEncoder().getPosition(), getClaw());
    }


    public ArmPose getSetPose()
    {
        return currentPose;
    }

    @Override
    public void periodic()
    {
        // despite the lack of roundness, this is very sensual. Yes more!;
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
            motor.setSmartCurrentLimit(40,15 );

            motor.setClosedLoopRampRate(ArmConfig.rampRate);
            motor.setOpenLoopRampRate(ArmConfig.rampRate);

            motor.enableSoftLimit(SoftLimitDirection.kForward, true);
            motor.enableSoftLimit(SoftLimitDirection.kReverse, true);
            
            motor.getEncoder().setPosition(0);
        }
        J3.setSmartCurrentLimit(20, 15);
        for(CANSparkMax motor : motors)
        {
            motor.burnFlash();
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
        if(!extended)
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
    private void setArmPosition(Double J1Ref, Double J2Ref, Double J3Ref )
    {
        if(J1Ref == null)
        {
            J1Ref = J1.getEncoder().getPosition();
        }
        if(J2Ref == null)
        {
            J2Ref = J2.getEncoder().getPosition();
        }
        if(J3Ref == null)
        {
            J3Ref = J3.getEncoder().getPosition();
        }

        J1.getPIDController().setReference(J1Ref, ControlType.kPosition);

        J2.getPIDController().setReference(J2Ref, ControlType.kPosition);

        J3.getPIDController().setReference(J3Ref, ControlType.kPosition);
         
    }


    public void adoptPose(ArmPose pose)
    {
        if(currentPose== null )
        {
            throw new NullPointerException("Arm pose may not be null. You ****ed up");
        }

        
        currentPose = pose;
        
        //System.out.println("working?");
        setArmPosition(pose.getJ1(),pose.getJ2(), pose.getJ3());
       
        
    }


    public boolean isAtPose(ArmPose pose)
    {

        double tolerance = 2;
        return isWithin(pose.getJ1(), J1.getEncoder().getPosition(), tolerance)
            && isWithin(pose.getJ2(), J2.getEncoder().getPosition(), tolerance)
            && isWithin(pose.getJ3(), J3.getEncoder().getPosition(), tolerance);
          
         
    }

    private boolean isWithin(double a, double b, double within)
    {
       
    
        return Math.abs(a-b)<within;
    }

    public void jogJoint(int joint, boolean forward)
    {
        final double JOG_BY = 0.25;
        double jog = (forward)? JOG_BY : -JOG_BY;

        System.out.println("jogging motor by " + jog + " degrees");

        Double pos1 = currentPose.getJ1();
        Double pos2 = currentPose.getJ2();
        Double pos3 = currentPose.getJ3();

        switch (joint)
        {
            case 1:
                pos1 += jog;
                break;
            case 2:
                pos2 += jog;
                break;
            case 3:
                pos3 += jog;
                break;

        }

        adoptPose(new BasicPose(pos1, pos2, pos3, getExtender()));
    }

    
}
