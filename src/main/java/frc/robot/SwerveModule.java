package frc.robot;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import frc.lib.math.Conversions;
import frc.lib.util.CTREModuleState;
import frc.lib.util.SwerveModuleConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class SwerveModule {
    public int moduleNumber;
    private Rotation2d angleOffset;
    private Rotation2d lastAngle;

    private CANSparkMax mAngleMotor;
    private CANSparkMax mDriveMotor;

    private final RelativeEncoder angleEncoder;
    private final RelativeEncoder driveEncoder;

    private final SparkMaxPIDController drivePIDController;
    private final SparkMaxPIDController anglePIDController;

    private CANCoder absoluteEncoder;

    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.Swerve.driveKS, Constants.Swerve.driveKV, Constants.Swerve.driveKA);

    public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants){
        this.moduleNumber = moduleNumber;
        this.angleOffset = moduleConstants.angleOffset;
        
        /* Angle Encoder Config */
        absoluteEncoder = new CANCoder(moduleConstants.cancoderID);
        configAngleEncoder();

        /* Angle Motor Config */
        mAngleMotor = new CANSparkMax(moduleConstants.angleMotorID, MotorType.kBrushless);
        angleEncoder = mAngleMotor.getEncoder();
        anglePIDController = mAngleMotor.getPIDController();
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new CANSparkMax(moduleConstants.driveMotorID, MotorType.kBrushless);
        driveEncoder = mDriveMotor.getEncoder();
        drivePIDController = mDriveMotor.getPIDController();
        configDriveMotor();

        lastAngle = getState().angle;
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop){
        /* This is a custom optimize function, since default WPILib optimize assumes continuous controller which CTRE and Rev onboard is not */
        desiredState = CTREModuleState.optimize(desiredState, getState().angle); 
        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop){
        if(isOpenLoop){
            double percentOutput = desiredState.speedMetersPerSecond / Constants.Swerve.maxSpeed;
            mDriveMotor.set(percentOutput);
        }
        else {
            mDriveMotor
                    .getPIDController()
                    .setReference(
                        desiredState.speedMetersPerSecond,
                        ControlType.kVelocity,
                        0,
                        feedforward.calculate(desiredState.speedMetersPerSecond),
                        SparkMaxPIDController.ArbFFUnits.kVoltage);
        }
    }

    private void setAngle(SwerveModuleState desiredState){
        Rotation2d angle = (Math.abs(desiredState.speedMetersPerSecond) <= (Constants.Swerve.maxSpeed * 0.01)) ? lastAngle : desiredState.angle; //Prevent rotating module if speed is less then 1%. Prevents Jittering.
        
        mAngleMotor.getPIDController().setReference(angle.getDegrees(), ControlType.kPosition);
        lastAngle = angle;

    }

    private Rotation2d getAngle(){
        return Rotation2d.fromDegrees(angleEncoder.getPosition());
    }

    public Rotation2d getCanCoder(){
        return Rotation2d.fromDegrees(absoluteEncoder.getAbsolutePosition());
    }

    public void resetToAbsolute(){
        angleEncoder.setPosition(getCanCoder().getDegrees() - angleOffset.getDegrees());
    }

    private void configAngleEncoder(){        
        absoluteEncoder.configFactoryDefault();
        absoluteEncoder.configAllSettings(Robot.ctreConfigs.swerveCanCoderConfig);
        // absoluteEncoder.configMagnetOffset(angleOffset.getDegrees());
    }

    private void configAngleMotor(){
        mAngleMotor.restoreFactoryDefaults();
        mAngleMotor.setSmartCurrentLimit(Constants.Swerve.angleContinuousCurrentLimit);
        mAngleMotor.setSecondaryCurrentLimit(Constants.Swerve.anglePeakCurrentDuration);
        mAngleMotor.setInverted(Constants.Swerve.angleMotorInvert);
        mAngleMotor.setIdleMode(Constants.Swerve.angleNeutralMode);
        
        angleEncoder.setPositionConversionFactor((1/Constants.Swerve.angleGearRatio) * 360);

        resetToAbsolute();

        anglePIDController.setP(Constants.Swerve.angleKP);
        anglePIDController.setI(Constants.Swerve.angleKI);
        anglePIDController.setD(Constants.Swerve.angleKD);
        anglePIDController.setFF(Constants.Swerve.angleKF);

        // mAngleMotor.getPIDController().setOutputRange(-0.25, 0.25);
    }

    private void configDriveMotor(){        
        mDriveMotor.restoreFactoryDefaults();
        mDriveMotor.setSmartCurrentLimit(Constants.Swerve.driveContinuousCurrentLimit);
        mDriveMotor.setSecondaryCurrentLimit(Constants.Swerve.drivePeakCurrentDuration);
        mDriveMotor.setInverted(Constants.Swerve.driveMotorInvert);
        mDriveMotor.setIdleMode(Constants.Swerve.driveNeutralMode);
        mDriveMotor.setOpenLoopRampRate(Constants.Swerve.openLoopRamp);
        mDriveMotor.setClosedLoopRampRate(Constants.Swerve.closedLoopRamp);

        driveEncoder.setVelocityConversionFactor(1/Constants.Swerve.driveGearRatio * Constants.Swerve.wheelCircumference / 60);
        //TODO Make sure conversion factor is correct for position
        driveEncoder.setPositionConversionFactor(1/Constants.Swerve.driveGearRatio * Constants.Swerve.wheelCircumference);
        driveEncoder.setPosition(0);

        drivePIDController.setP(Constants.Swerve.driveKP);
        drivePIDController.setI(Constants.Swerve.driveKI);
        drivePIDController.setD(Constants.Swerve.driveKD);
        drivePIDController.setFF(Constants.Swerve.driveKF);

        drivePIDController.setOutputRange(-0.5, 0.5);
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(
            driveEncoder.getVelocity(),
            getAngle()
        ); 
    }

    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(
            driveEncoder.getPosition(), 
            getAngle()
        );
    }
}