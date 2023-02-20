package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.lib.util.COTSFalconSwerveConstants;
import frc.lib.util.SwerveModuleConstants;

public final class Constants 
{
    public static final double stickDeadband = 0.1;

  
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
        public static final int intakeMotorID = 9;
        public static final int undertakeMotorID = 21;
        public static final int undertakeFollowMotorID = 22;

        public static final int intakeArmsUpChannel = 4;
        public static final int intakeArmsDownChannel = 5;

        public static final int presenterUpChannel = 6;
        public static final int presenterDownChannel = 7;

        public static final int vaccumForwardChannel = 8;
        public static final int vaccumReverseChannel = 9;

        public static final int sensorChannel = 0;
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

    public static final class AutoConstants { 
        public static final double kMaxSpeedMetersPerSecond = 4.5;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;
    
        public static final double kPXController = -6;
        public static final double kPYController = -6;
        public static final double kPThetaController = 1;
    
        /* Constraint for the motion profilied robot angle controller */
        public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
    }
}
