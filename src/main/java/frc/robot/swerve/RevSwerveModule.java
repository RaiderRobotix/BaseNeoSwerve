
package frc.robot.swerve;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import frc.lib.util.CTREModuleState;
import frc.lib.util.SwerveModuleConstants;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.spark.SparkClosedLoopController;
//import com.revrobotics.CANSparkMax;(removed)
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.RelativeEncoder;
//import com.revrobotics.SparkMaxPIDController;(removed)
//import com.revrobotics.CANSparkBase.ControlType;(removed)
import com.revrobotics.spark.SparkBase.ControlType;
//import com.revrobotics.CANSparkBase.FaultID;(removed)
import com.revrobotics.spark.SparkBase.Faults;
//import com.revrobotics.CANSparkMaxLowLevel.MotorType;(removed)
import com.revrobotics.spark.SparkLowLevel.MotorType;
//new stuff
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;






/**
 * a Swerve Modules using REV Robotics motor controllers and CTRE CANcoder absolute encoders.
 */
public class RevSwerveModule implements SwerveModule
{
    public int moduleNumber;
    private Rotation2d angleOffset;
   // private Rotation2d lastAngle;

    //private CANSparkMax mAngleMotor;(removed)
    //private CANSparkMax mDriveMotor;(removed)
    private SparkMax mAngleMotor;
    private SparkMax mDriveMotor;

    //new methods
    public static final SparkMaxConfig driveconfig = new SparkMaxConfig();
    public static final SparkMaxConfig angleconfig = new SparkMaxConfig();
    //end of new methods


    private CANCoder angleEncoder;
    private RelativeEncoder relAngleEncoder;
    private RelativeEncoder relDriveEncoder;


    //SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.Swerve.driveKS, Constants.Swerve.driveKV, Constants.Swerve.driveKA);

    public RevSwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants)
    {
        this.moduleNumber = moduleNumber;
        this.angleOffset = moduleConstants.angleOffset;
        
       
        /* Angle Motor Config */
        //mAngleMotor = new CANSparkMax(moduleConstants.angleMotorID, MotorType.kBrushless);(removed)
        mAngleMotor = new SparkMax(moduleConstants.angleMotorID, MotorType.kBrushless);
        configAngleMotor();

        /* Drive Motor Config */
        //mDriveMotor = new CANSparkMax(moduleConstants.driveMotorID,  MotorType.kBrushless);(removed)
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

         
        //relDriveEncoder.setPositionConversionFactor(SwerveConfig.driveRevToMeters);(removed)
        //relDriveEncoder.setVelocityConversionFactor(SwerveConfig.driveRpmToMetersPerSecond);(removed)
        driveconfig.encoder
            .positionConversionFactor(SwerveConfig.driveRevToMeters)
            .velocityConversionFactor(SwerveConfig.driveRpmToMetersPerSecond);
            
        relAngleEncoder = mAngleMotor.getEncoder();
        //relAngleEncoder.setPositionConversionFactor(SwerveConfig.DegreesPerTurnRotation);(removed)
        // in degrees/sec
        //relAngleEncoder.setVelocityConversionFactor(SwerveConfig.DegreesPerTurnRotation / 60);(removed)
        angleconfig.encoder
            .positionConversionFactor(SwerveConfig.DegreesPerTurnRotation)
            .velocityConversionFactor(SwerveConfig.DegreesPerTurnRotation / 60);


        resetToAbsolute();
        //mDriveMotor.burnFlash();(removed)
        //mAngleMotor.burnFlash();(removed)
        mDriveMotor.configure(angleconfig,  ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
        mAngleMotor.configure(angleconfig,  ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    }

    private void configAngleMotor()
    {
        //mAngleMotor.restoreFactoryDefaults();(removed)
        //SparkMaxPIDController controller = mAngleMotor.getPIDController();
        //SparkClosedLoopController controller = mAngleMotor.getClosedLoopController();
        angleconfig.closedLoop
            .pidf(SwerveConfig.angleKP, SwerveConfig.angleKI, 
            SwerveConfig.angleKD, SwerveConfig.angleKF)
        /* 
        controller.setP(SwerveConfig.angleKP, 0);
        controller.setI(SwerveConfig.angleKI,0);
        controller.setD(SwerveConfig.angleKD,0);
        controller.setFF(SwerveConfig.angleKF,0);
        (removed)
        */
        //controller.setOutputRange(-SwerveConfig.anglePower, SwerveConfig.anglePower);(removed)
            .outputRange(-SwerveConfig.anglePower, SwerveConfig.anglePower);
       // mAngleMotor.setSmartCurrentLimit(SwerveConfig.angleContinuousCurrentLimit);(removed)
        //mAngleMotor.setInverted(SwerveConfig.angleMotorInvert);(removed)
        //mAngleMotor.setIdleMode(SwerveConfig.angleIdleMode);(removed)
        angleconfig
            .idleMode(SwerveConfig.angleIdleMode)
            .smartCurrentLimit(SwerveConfig.angleContinuousCurrentLimit)
            .inverted(SwerveConfig.angleMotorInvert);
        
            //new
        mAngleMotor.configure(angleconfig,  ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
       
    }

    private void configDriveMotor()
    {        
        //mDriveMotor.restoreFactoryDefaults();(removed)
        //SparkMaxPIDController controller = mDriveMotor.getPIDController();(removed)
        //SparkClosedLoopController controller = mDriveMotor.getClosedLoopController();
        driveconfig.closedLoop
            .pidf(SwerveConfig.driveKP,SwerveConfig.driveKI,
            SwerveConfig.driveKD,SwerveConfig.driveKF)
        /* 
        controller.setP(SwerveConfig.driveKP,0);
        controller.setI(SwerveConfig.driveKI,0);
        controller.setD(SwerveConfig.driveKD,0);
        controller.setFF(SwerveConfig.driveKF,0);
        */
        .outputRange(-SwerveConfig.drivePower, SwerveConfig.drivePower);
       // controller.setOutputRange(-SwerveConfig.drivePower, SwerveConfig.drivePower);(removed)
        //mDriveMotor.setSmartCurrentLimit(SwerveConfig.driveContinuousCurrentLimit);(removed)
        //mDriveMotor.setInverted(SwerveConfig.driveMotorInvert);(removed)
        //mDriveMotor.setIdleMode(SwerveConfig.driveIdleMode);(removed) 
        driveconfig
        .idleMode(SwerveConfig.driveIdleMode)
        .smartCurrentLimit(SwerveConfig.driveContinuousCurrentLimit)
        .inverted(SwerveConfig.driveMotorInvert);
       
       //new
       mDriveMotor.configure(driveconfig,  ResetMode.kResetSafeParameters,
       PersistMode.kPersistParameters);
       
    }



    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop)
    {
        
        
        /* This is a custom optimize function, since default WPILib optimize assumes continuous controller which CTRE and Rev onboard is not */
        // CTREModuleState actually works for any type of motor.
        desiredState = CTREModuleState.optimize(desiredState, getState().angle); 
        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);
/* These are (removed) but idk how to fix them rn+--
        if(mDriveMotor.getFault(Faults.kSensorFault))
        {
            DriverStation.reportWarning("Sensor Fault on Drive Motor ID:"+mDriveMotor.getDeviceId(), false);
        }

        if(mAngleMotor.getFault(FaultID.kSensorFault))
        {
            DriverStation.reportWarning("Sensor Fault on Angle Motor ID:"+mAngleMotor.getDeviceId(), true);
        }
         */
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
        
        //SparkMaxPIDController controller = mDriveMotor.getPIDController();(removed)
        SparkClosedLoopController controller = mDriveMotor.getClosedLoopController();
        controller.setReference(velocity, ControlType.kVelocity);
        
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
        
        //SparkMaxPIDController controller = mAngleMotor.getPIDController();(removed)
        SparkClosedLoopController controller = mAngleMotor.getClosedLoopController();
        double degReference = angle.getDegrees();
     
       
        
        controller.setReference(degReference, ControlType.kPosition);
        
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