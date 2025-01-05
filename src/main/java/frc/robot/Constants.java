package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.lib.util.SwerveModuleConstants;


/**
 * This file comes with command robot projects, and is intended to contain
 * configuration information.
 * I think it would be prudent if this file only contained CanIDs, because it
 * is useful to have all the ids for the whole robot in one place.
 * other configuration goes into subsystem specific configuration files,
 * to make sure this one isn't cluttered.
 */
public final class Constants 
{
    public static final double stickDeadband = 0.0;
    public static final double limelightOffset = 3;
  
    /**
     * REV Robotics specific settings
     */
    public static final class REV
    {
        public static final int pigeonID = 10;

        public static final int PHID = 30;
        
        
    }


     /* Module Specific Constants */
    /* Front Left Module - Module 0 */
    public static final class Mod0 
    { 
        
        public static final int driveMotorID = 10;
        public static final int angleMotorID = 20;
        public static final int canCoderID = 0;
        public static final Rotation2d angleOffset = Rotation2d.fromDegrees(64.16); //Rotation2d.fromDegrees(37.7);
        public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

    /* Front Right Module - Module 1 */
    public static final class Mod1
     { 
        public static final int driveMotorID = 11;
        public static final int angleMotorID = 21;
        public static final int canCoderID = 1;
        public static final Rotation2d angleOffset = Rotation2d.fromDegrees(146.25);
        public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
    }
    
    /* Back Left Module - Module 2 */
    public static final class Mod2
     { 
        public static final int driveMotorID = 12;
        public static final int angleMotorID = 22;
        public static final int canCoderID = 2;
        public static final Rotation2d angleOffset = Rotation2d.fromDegrees(316.49);
        public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

    /* Back Right Module - Module 3 */
    public static final class Mod3 
    { 
        public static final int driveMotorID = 13;
        public static final int angleMotorID = 23;
        public static final int canCoderID = 3;
        public static final Rotation2d angleOffset = Rotation2d.fromDegrees(220.42);
        public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

   

    public static final class Intake
    {
        public static final int intakeMotorID = 50;
        public static final int overIntakeMotorID = 51;

        public static final int intakeArmsUpChannel = 51;
        public static final int intakeArmsDownChannel = 53;


        public static final int sensorChannel = 54;
    }

    public static final class Shooter
    {
        public static final int shooterMotorID = 59;
    }

    public static final class Arm
    {
        public static final int J1MotorID = 68;
        public static final int J1FollowMotorID = 69;
        public static final int J2MotorID = 70;
        public static final int J2FollowMotorID = 78;
        public static final int J3MotorID = 79;

        public static final int forwardExtenderChannel = 40;
        public static final int reverseExtenderChannel = 41;
        public static int forwardClawChannel = 42;
        public static int reverseClawChannel = 43;
    }


    public static final class AutoConstants { //TODO: The below constants are used in the example auto, and must be tuned to specific robot
        public static final double kMaxSpeedMetersPerSecond = 3;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
    
        public static final double kPXController = 1;
        public static final double kPYController = 1;
        public static final double kPThetaController = 1;
    
        /* Constraint for the motion profilied robot angle controller */
        public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
    }

}
