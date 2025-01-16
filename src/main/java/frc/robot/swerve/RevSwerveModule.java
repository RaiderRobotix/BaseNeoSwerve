
package frc.robot.swerve;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import frc.lib.util.CTREModuleState;
import frc.lib.util.SwerveModuleConstants;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.RelativeEncoder;
//import com.revrobotics.SparkMaxPIDControlle
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkClosedLoopController;
//import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.spark.SparkBase.Faults;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfigAccessor;
import com.revrobotics.spark.config.ClosedLoopConfig;
/**
 * a Swerve Modules using REV Robotics motor controllers and CTRE CANcoder absolute encoders.
 */
public class RevSwerveModule implements SwerveModule
{
    public int moduleNumber;
    private Rotation2d angleOffset;
   // private Rotation2d lastAngle;

    private SparkMax mAngleMotor;
    private SparkMax mDriveMotor;

    SparkMaxConfig driveconfig = new SparkMaxConfig();
    SparkMaxConfig angleconfig = new SparkMaxConfig();

    private CANCoder angleEncoder;
    private RelativeEncoder relAngleEncoder;
    private RelativeEncoder relDriveEncoder;

   
    //SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.Swerve.driveKS, Constants.Swerve.driveKV, Constants.Swerve.driveKA);

    public RevSwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants)
    {
        this.moduleNumber = moduleNumber;
        this.angleOffset = moduleConstants.angleOffset;
        
       
        /* Angle Motor Config */
        mAngleMotor = new SparkMax(moduleConstants.angleMotorID, MotorType.kBrushless);
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new SparkMax(moduleConstants.driveMotorID,  MotorType.kBrushless);
        configDriveMotor();

         /* Angle Encoder Config */
    
        angleEncoder = new CANCoder(moduleConstants.cancoderID);
        configEncoders();


       // lastAngle = getState().angle;
    }


    private void configEncoders()
    {     
        // absolute encoder   
      
        angleEncoder.configFactoryDefault();
        angleEncoder.configAllSettings(new SwerveConfig().canCoderConfig);
       
        relDriveEncoder = mDriveMotor.getEncoder();
        relDriveEncoder.setPosition(0);
     
        driveconfig.encoder
        .positionConversionFactor(SwerveConfig.driveRevToMeters)
        .velocityConversionFactor(SwerveConfig.driveRpmToMetersPerSecond);
      //  relDriveEncoder.setPositionConversionFactor(SwerveConfig.driveRevToMeters);
 
     //   relDriveEncoder.setVelocityConversionFactor(SwerveConfig.driveRpmToMetersPerSecond);


        relAngleEncoder = mAngleMotor.getEncoder();
        driveconfig.encoder
        .positionConversionFactor(SwerveConfig.DegreesPerTurnRotation)
        .velocityConversionFactor(SwerveConfig.DegreesPerTurnRotation / 60);
        //relAngleEncoder.setPositionConversionFactor(SwerveConfig.DegreesPerTurnRotation);

        // in degrees/sec
        //relAngleEncoder.setVelocityConversionFactor(SwerveConfig.DegreesPerTurnRotation / 60);
     
    

        resetToAbsolute();
        mDriveMotor.configure(driveconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        mAngleMotor.configure(angleconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    private void configAngleMotor()
    {
        mAngleMotor.configure(angleconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        //SparkClosedLoopController anglecontroller = mAngleMotor.getClosedLoopController();
        angleconfig.closedLoop
        .pidf(SwerveConfig.angleKP, SwerveConfig.angleKI, SwerveConfig.angleKD,SwerveConfig.angleKF);
       /* 
        controller.setP(SwerveConfig.angleKP, 0);
        controller.setI(SwerveConfig.angleKI,0);
        controller.setD(SwerveConfig.angleKD,0);
        controller.setFF(SwerveConfig.angleKF,0);
*/
        angleconfig
        .smartCurrentLimit(SwerveConfig.angleContinuousCurrentLimit);
        angleconfig.closedLoop
        .outputRange(-SwerveConfig.anglePower, SwerveConfig.anglePower);
       // controller.setOutputRange(-SwerveConfig.anglePower, SwerveConfig.anglePower);
       // mAngleMotor.setSmartCurrentLimit(SwerveConfig.angleContinuousCurrentLimit);
       angleconfig
    .inverted(SwerveConfig.angleMotorInvert)
    .idleMode(SwerveConfig.angleIdleMode);
/* 
        mAngleMotor.setInverted(SwerveConfig.angleMotorInvert);
        mAngleMotor.setIdleMode(SwerveConfig.angleIdleMode);
*/
        
       
    }

    private void configDriveMotor()
    {        
        mDriveMotor.configure(driveconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
       // SparkClosedLoopController  drivecontroller = mDriveMotor.getClosedLoopController();
        driveconfig.closedLoop
        .pidf(SwerveConfig.driveKP, SwerveConfig.driveKI, SwerveConfig.driveKD,SwerveConfig.driveKF);
/* 
        controller.setP(SwerveConfig.driveKP,0);
        controller.setI(SwerveConfig.driveKI,0);
        controller.setD(SwerveConfig.driveKD,0);
        
        controller.setFF(SwerveConfig.driveKF,0);
        */
       // controller.setOutputRange(-SwerveConfig.drivePower, SwerveConfig.drivePower);
        driveconfig.closedLoop
        .outputRange(-SwerveConfig.drivePower, SwerveConfig.drivePower);

        driveconfig
        .smartCurrentLimit(SwerveConfig.driveContinuousCurrentLimit);

       //mDriveMotor.setSmartCurrentLimit(SwerveConfig.driveContinuousCurrentLimit);
        driveconfig
        .inverted(SwerveConfig.driveMotorInvert)
        .idleMode(SwerveConfig.driveIdleMode);
        /* 
        mDriveMotor.setInverted(SwerveConfig.driveMotorInvert);
        mDriveMotor.setIdleMode(SwerveConfig.driveIdleMode); 
    
       */
       
       
    }



    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop)
    {
        
        
        /* This is a custom optimize function, since default WPILib optimize assumes continuous controller which CTRE and Rev onboard is not */
        // CTREModuleState actually works for any type of motor.
        desiredState = CTREModuleState.optimize(desiredState, getState().angle); 
        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);

     //   if(mDriveMotor.getFault(FaultID.kSensorFault))
        {
            DriverStation.reportWarning("Sensor Fault on Drive Motor ID:"+mDriveMotor.getDeviceId(), false);
        }

     //   if(mAngleMotor.getFault(FaultID.kSensorFault))
        {
            DriverStation.reportWarning("Sensor Fault on Angle Motor ID:"+mAngleMotor.getDeviceId(), true);
        }
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop)
    {
       
        if(isOpenLoop)
        {
            double percentOutput = desiredState.speedMetersPerSecond / SwerveConfig.maxSpeed;
            mDriveMotor.set(percentOutput);
            return;
        }
 
        double velocity = desiredState.speedMetersPerSecond;
        
       // SparkClosedLoopController   controller = mDriveMotor.getClosedLoopController();
        //controller.setReference(velocity, ControlType.kVelocity, 0);
        /* 
        SparkMax m_motor = new SparkMax(moduleConstants.driveMotorID, MotorType.kBrushless);
        SparkPIDController m_pidController = m_motor.getPIDController();
        m_pidController.setReference(setPoint, SparkBase.ControlType.kPosition);
    
*/
    }
    private void setAngle(SwerveModuleState desiredState)
    {
        if(Math.abs(desiredState.speedMetersPerSecond) <= (SwerveConfig.maxSpeed * 0.01)) 
        {
            mAngleMotor.stopMotor();
            return;

        }
        Rotation2d angle = desiredState.angle; 
        //Prevent rotating module if speed is less then 1%. Prevents Jittering.
        
        SparkClosedLoopController   controller = mAngleMotor.getClosedLoopController();
        
        double degReference = angle.getDegrees();
     
      
        
    //    angleconfig.setReference(degReference,ControlType.kPosition,0);
        
    }

   

    private Rotation2d getAngle()
    {
        return Rotation2d.fromDegrees(relAngleEncoder.getPosition());
    }

    public Rotation2d getCanCoder()
    {
        
        return Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
        //return getAngle();
    }

    public int getModuleNumber() 
    {
        return moduleNumber;
    }

    public void setModuleNumber(int moduleNumber) 
    {
        this.moduleNumber = moduleNumber;
    }

    private void resetToAbsolute()
    {
    
        double absolutePosition =getCanCoder().getDegrees() - angleOffset.getDegrees();
        relAngleEncoder.setPosition(absolutePosition);
    }

  

    public SwerveModuleState getState()
    {
        return new SwerveModuleState(
            relDriveEncoder.getVelocity(),
            getAngle()
        ); 
    }

    public SwerveModulePosition getPosition()
    {
        return new SwerveModulePosition(
            relDriveEncoder.getPosition(), 
            getAngle()
        );
    }
}