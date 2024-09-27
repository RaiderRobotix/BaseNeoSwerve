package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
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


                                                                    // The second part of this line has some work to be dealt with; COTSFalcon
       //public static final COTSFalconSwerveConstants chosenModule = COTSFalconSwerveConstantsNeoTalonFX
        /*DriveStation Constraits */            
        
        //TODO: All must be tuned to specific robot
        public static final double trackWidth = Units.inchesToMeters(18.75);
        public static final double wheelBase = Units.inchesToMeters(20.25);
      /* 
        public static final double wheelDiameter = Units.inchesToMeters(insert value);
        public static final double wheelCircumference = wheelDiameter * Math.PI;
//NEED TO FINE TUNE THE BELOW 4 LINES FOR OUR ROBOT
    public static final double openLoopRamp = 0.25;
    public static final double closedLoopRamp = 0.0;

    public static final double driveGearRatio = (6.75 / 1.0); // 6.75:1
    public static final double angleGearRatio = (12.8 / 1.0); // 12.8:1
     */
            // Chosen
        //   

        /*Swerve Kinematics */

        public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(

        new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
        new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
        new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
        new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));

/*
(WE NEED TO FINE TUNE THIS VALUES FOR OUR ROBOT!) 

 // Angle Motor PID Values 
    public static final double angleKP = 0.01;
    public static final double angleKI = 0.0;
    public static final double angleKD = 0.0;
    public static final double angleKFF = 0.0;

    // Drive Motor PID Values 
    public static final double driveKP = 0.1;
    public static final double driveKI = 0.0;
    public static final double driveKD = 0.0;
    public static final double driveKFF = 0.0;

    // Drive Motor Characterization Values 
    public static final double driveKS = 0.667;
    public static final double driveKV = 2.44;
    public static final double driveKA = 0.27;

    // Drive Motor Conversion Factors 
    public static final double driveConversionPositionFactor =
        (wheelDiameter * Math.PI) / driveGearRatio;
    public static final double driveConversionVelocityFactor = driveConversionPositionFactor / 60.0;
    public static final double angleConversionFactor = 360.0 / angleGearRatio;


//for setting max speed and acceleration
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxAccelerationMetersPerSecondSquared = 1;
*/
        
        
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
        public static final int canCoderID = 4;
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
