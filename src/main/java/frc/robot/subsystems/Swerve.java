package frc.robot.subsystems;

import frc.robot.SwerveModule;
import frc.robot.Constants;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import com.ctre.phoenix.sensors.Pigeon2;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Swerve extends SubsystemBase {
    public SwerveDrivePoseEstimator swerveOdometry;
    public SwerveModule[] mSwerveMods;
    public AHRS gyro;

    private final ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("driveSubsystem");
    private final Field2d field = new Field2d();

    public Swerve() {
        gyro = new AHRS(Port.kUSB);
        gyro.calibrate();
        zeroGyro();

        mSwerveMods = new SwerveModule[] {
            new SwerveModule(0, Constants.Swerve.Mod0.constants),
            new SwerveModule(1, Constants.Swerve.Mod1.constants),
            new SwerveModule(2, Constants.Swerve.Mod2.constants),
            new SwerveModule(3, Constants.Swerve.Mod3.constants)
        };

        /* By pausing init for a second before setting module offsets, we avoid a bug with inverting motors.
         * See https://github.com/Team364/BaseFalconSwerve/issues/8 for more info.
         */
        Timer.delay(1.0);
        resetModulesToAbsolute();

        swerveOdometry = new SwerveDrivePoseEstimator(Constants.Swerve.swerveKinematics, getYaw(), getModulePositions(), new Pose2d(0, 0, Rotation2d.fromDegrees(0)));
    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation, 
                                    getRotation2dIEEE()
                                )
                                : new ChassisSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation)
                                );
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    }    

    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }    

    public void updatePose(){
        swerveOdometry.update(getRotation2dIEEE(), getModulePositions());
    }

    public Pose2d getPose() {
        return swerveOdometry.getEstimatedPosition();
    }

    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(getYaw(), getModulePositions(), pose);
    }

    public SwerveDriveKinematics getSwerveKinematics(){
        return Constants.Swerve.swerveKinematics;
    }

    public SwerveModuleState[] getModuleStates(){
        SwerveModuleState[] states = new SwerveModuleState[4];
        for(SwerveModule mod : mSwerveMods){
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions(){
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods){
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    public void zeroGyro(){
        gyro.zeroYaw();
    }

    public Rotation2d getYaw() {
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - gyro.getYaw()) : Rotation2d.fromDegrees(gyro.getYaw());
    }

    public double getYawIEEE(){
        return Math.IEEEremainder(getYaw().getDegrees(), 360);
    }

    public Rotation2d getRotation2dIEEE(){
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(-getYawIEEE()) : Rotation2d.fromDegrees(getYawIEEE());
    }

    public void resetModulesToAbsolute(){
        for(SwerveModule mod : mSwerveMods){
            mod.resetToAbsolute();
        }
    }

    private void drawRobotOnField(){
        field.setRobotPose(getPose());

        field.getObject("frontLeft")
            .setPose(getPose()
                .transformBy(
                    new Transform2d(
                        Constants.Swerve.Mod0.translation,
                        getModuleStates()[0].angle)));
        field.getObject("frontRight")
            .setPose(getPose()
                .transformBy(
                    new Transform2d(
                        Constants.Swerve.Mod1.translation,
                        getModuleStates()[1].angle)));
        field.getObject("backLeft")
            .setPose(getPose()
                .transformBy(
                    new Transform2d(
                        Constants.Swerve.Mod2.translation,
                        getModuleStates()[2].angle)));
        field.getObject("backRight")
            .setPose(getPose()
                .transformBy(
                    new Transform2d(
                        Constants.Swerve.Mod3.translation,
                        getModuleStates()[3].angle)));
    }

    @Override
    public void periodic(){
        shuffleboardTab.addString("Gyro", () -> getYaw().toString());
        
        updatePose();
        drawRobotOnField();
        
        for(SwerveModule mod : mSwerveMods){
            shuffleboardTab.addNumber("Mod " + mod.moduleNumber + " Cancoder", () -> mod.getCanCoder().getDegrees());
            shuffleboardTab.addNumber("Mod " + mod.moduleNumber + " Integrated", () -> mod.getPosition().angle.getDegrees());
            shuffleboardTab.addNumber("Mod " + mod.moduleNumber + " Velocity", () -> mod.getState().speedMetersPerSecond);    
        }
    }
}