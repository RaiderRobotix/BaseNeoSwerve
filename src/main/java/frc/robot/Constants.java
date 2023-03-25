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
    public static final double stickDeadband = 0.1;
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
        
        public static final int driveMotorID = 7;
        public static final int angleMotorID = 8;
        public static final int canCoderID = 11;
        public static final Rotation2d angleOffset = Rotation2d.fromDegrees(293); //Rotation2d.fromDegrees(37.7);
        public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

    /* Front Right Module - Module 1 */
    public static final class Mod1
     { 
        public static final int driveMotorID = 5;
        public static final int angleMotorID = 6;
        public static final int canCoderID = 12;
        public static final Rotation2d angleOffset = Rotation2d.fromDegrees(359);
        public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
    }
    
    /* Back Left Module - Module 2 */
    public static final class Mod2
     { 
        public static final int driveMotorID = 3;
        public static final int angleMotorID = 4;
        public static final int canCoderID = 13;
        public static final Rotation2d angleOffset = Rotation2d.fromDegrees(237);
        public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

    /* Back Right Module - Module 3 */
    public static final class Mod3 
    { 
        public static final int driveMotorID = 1;
        public static final int angleMotorID = 2;
        public static final int canCoderID = 14;
        public static final Rotation2d angleOffset = Rotation2d.fromDegrees(221);
        public static final SwerveModuleConstants constants = 
            new SwerveModuleConstants(driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

   

    public static final class Intake
    {
        public static final int intakeMotorID = 21;
        public static final int overIntakeMotorID = 22;

        public static final int intakeArmsUpChannel = 4;
        public static final int intakeArmsDownChannel = 5;


        public static final int sensorChannel = 5;
    }

    public static final class Shooter
    {
        public static final int shooterMotorID = 9;
    }

    public static final class Arm
    {
        public static final int J1MotorID = 15;
        public static final int J1FollowMotorID = 16;
        public static final int J2MotorID = 17;
        public static final int J2FollowMotorID = 18;
        public static final int J3MotorID = 19;

        public static final int forwardExtenderChannel = 0;
        public static final int reverseExtenderChannel = 1;
        public static int forwardClawChannel = 2;
        public static int reverseClawChannel = 3;
    }

}
