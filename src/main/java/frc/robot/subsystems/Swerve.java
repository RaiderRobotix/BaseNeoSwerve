package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.SwerveModule.RevSwerveModule;
import frc.robot.SwerveModule.SwerveModule;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Swerve extends SubsystemBase
 {


    public SwerveDriveOdometry swerveOdometry;
    public SwerveModule[] mSwerveMods;
    public Pigeon2 gyro;



    public Swerve() 
    {
        
        gyro = new Pigeon2(Constants.Swerve.pigeonID);
        gyro.configFactoryDefault();
        zeroGyro();

        mSwerveMods = new SwerveModule[] {
           
            new RevSwerveModule(0, Constants.Swerve.Mod0.constants),
           new RevSwerveModule(1, Constants.Swerve.Mod1.constants),
            new RevSwerveModule(2, Constants.Swerve.Mod2.constants),
            new RevSwerveModule(3, Constants.Swerve.Mod3.constants)
        };

        swerveOdometry = new SwerveDriveOdometry(Constants.Swerve.swerveKinematics, getYaw(), getModulePositions());

        ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");


        addDashboardEntries(tab.getLayout("Odo", BuiltInLayouts.kList)
             .withSize(2, 4)
             .withPosition(0, 0), swerveOdometry.getPoseMeters());
             
    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) 
    {
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(
                fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation, 
                                    getYaw()
                                )
                                : new ChassisSpeeds(
                                    translation.getX(), 
                                    translation.getY(), 
                                    rotation)
                                );
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.Swerve.maxSpeed);

        for(SwerveModule mod : mSwerveMods)
        {
            mod.setDesiredState(swerveModuleStates[mod.getModuleNumber()], isOpenLoop);
        }

    }    



    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) 
    {

        System.out.println("setting module states: "+desiredStates[0]);
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(desiredStates[mod.getModuleNumber()], false);
        }
    }    

    public Pose2d getPose() 
    {
        return swerveOdometry.getPoseMeters();
        
    }

    public void resetOdometry(Pose2d pose) 
    {
        swerveOdometry.resetPosition(getYaw(), getModulePositions(), pose);
    }

    public SwerveModuleState[] getModuleStates()
    {
        SwerveModuleState[] states = new SwerveModuleState[4];

        for(SwerveModule mod : mSwerveMods)
        {
            states[mod.getModuleNumber()] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions()
    {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods)
        {
            positions[mod.getModuleNumber()] = mod.getPosition();
        }
        return positions;
    }

    public void zeroGyro()
    {
      
        gyro.setYaw(0);
    }

    public Rotation2d getYaw() 
    {
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(360 - gyro.getYaw()) : Rotation2d.fromDegrees(gyro.getYaw());
    }

    public void lockWheels()
    {
        // 0,3  1,2
        SwerveModuleState sms = new SwerveModuleState(0, Rotation2d.fromDegrees(45));
    

        mSwerveMods[0].setDesiredState(sms, false);
        mSwerveMods[3].setDesiredState(sms, false);


        sms = new SwerveModuleState(0, Rotation2d.fromDegrees(-45));

        mSwerveMods[1].setDesiredState(sms, false);
        mSwerveMods[2].setDesiredState(sms, false);
        
    }

    @Override
    public void periodic()
    {
        
        swerveOdometry.update(getYaw(), getModulePositions());  

        for(SwerveModule mod : mSwerveMods)
        {
            
            SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " Cancoder", mod.getCanCoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.getModuleNumber() + " Velocity", mod.getState().speedMetersPerSecond);    
        }
        Pose2d pose = swerveOdometry.getPoseMeters();
        SmartDashboard.putNumber("Odo Pos X", pose.getX());
        SmartDashboard.putNumber("Odo Pos Y", pose.getY());
        SmartDashboard.putNumber("Odo Angle", pose.getRotation().getDegrees());
    }

    // slight witchcra%ft
    private void addDashboardEntries(ShuffleboardContainer container, Pose2d pose) 
    {
        container.addNumber("Pos X", () -> pose.getX());
        container.addNumber("Pos Y",()-> pose.getY());
        container.addNumber("Angle", ()->pose.getRotation().getDegrees());
    }

}