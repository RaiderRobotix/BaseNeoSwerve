package frc.robot;


import javax.sound.sampled.AudioFileFormat.Type;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.lib.util.States.GamePiece;
import frc.robot.autos.exampleAuto;
import frc.robot.subsystems.LimeLight;
import frc.robot.subsystems.rollerTest;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.command.LockSwerveCommand;
import frc.robot.swerve.command.TeleopSwerve;

/**
 * WPILIB:
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 * 
 * ME:
 * For all intents and purposes, this is the primary file in the robot code.
 * I'll say outright that this code is not *amazingly* structured, but it gets the job done and isn't a huge mess.
 * Think you can do better? Good! Make it happen. You've got something to work off of! I didn't really have that.
 * Actual relevant advice though:
 * It's probably not wise to include a huge amount of logic here either.
 * This class currently does about three things:
 *      Creates/initializes all subsytems
 *      Sets up commands to run in teleop when certain buttons are pressed
 *      creates an instance of AutoSelector, which creates the command used for auto.
 * and schedules an auto accordingly.
 */
public class RobotContainer 
{
    /* Controllers */
    //private final Joystick driveStick;
    // private final Joystick rotateStick;
    private final XboxController controller;
    private final Joystick operator;
    // private final GenericHID buttonBoard;

    // Limelight IS NOT A TYPE

    // LIMELIGHT
    // private NetworkTable Limelight;


    /*  THIS CODE IS FOR THE SONIC SENSOR     */
    //Ultrasonic sensor below
    //  public final AnalogInput front_sensor;
    //  public final AnalogInput left_sensor;
    //  public final AnalogInput right_sensor;
    //  public final AnalogInput back_sensor;  

    // Made all above public for testing reasons


    private Ultrasonic test;

    /* Subsystems */
    // the code we were using used the prefix s_ for all of the names of subsystems here.
    // personally, this seems useless, and I would avoid it if I were you
    // I mean it would take like 30 seconds to fix honestly but whatever
    private final Swerve s_Swerve;
    private final rollerTest s_roller;



    private PowerDistribution powerBoard;
    private PieceMode pieceMode; // the piecemode class essentially controls whether the arm is in cone or cube mode. 
                                 //the arm doesn't know about it though


    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer(PowerDistribution pd) 
    {
        powerBoard = pd;
     //   CameraServer.startAutomaticCapture();

        /* Controllers */
        //driveStick = new Joystick(2);
        // rotateStick = new Joystick(1);
        controller = new XboxController(0);
        operator = new Joystick(1);
        
        /*  THIS CODE IS FOR THE SONIC SENSOR     */
        

            
        AnalogInput front_sensor = new AnalogInput(1);
            /*TEST WITH SENSOR METHOD */
         //    front_sensor.setEnabled();
         // A bunch of samples get thrown, and a sort of mean/median is returned
         front_sensor.setOversampleBits(4);
         
        // front_sensor.initSendable("front_sensor");
        
         // This DOES NOT work over at robot.java, so maybe try adding all of this there, too.

        AnalogInput left_sensor = new AnalogInput(2);
        AnalogInput right_sensor = new AnalogInput(3);
        AnalogInput back_sensor = new AnalogInput(4);

        //                      LIMELIGHT TERRITORY             LIMELIGHT TERRITORY                 LIMELIGHT TERRITORY

        /* LIMELIGHT DOWN HERE*/ 

        //private Limelight blindingDevice = new LimeLight();        

        
        
        
        /*  OTHER Test LIMELIGHT               INSTANCES AT ROBOT.JAVA      */
        
       // Limelight = new NetworkTable( "raiders", "test path");
        NetworkTable RaidersLimeLight = NetworkTableInstance.getDefault().getTable("raiders");
        NetworkTableEntry tx = RaidersLimeLight.getEntry("tx");
        NetworkTableEntry ty = RaidersLimeLight.getEntry("ty");
        NetworkTableEntry ta = RaidersLimeLight.getEntry("ta");

        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);

        //post to smart dashboard periodically
        SmartDashboard.putNumber("LimelightX", x);
        SmartDashboard.putNumber("LimelightY", y);
        SmartDashboard.putNumber("LimelightArea", area);


        System.out.println(tx);
        System.out.println(ty);
        System.out.println(ta);




        //COde from limelight lib

        






        //                      END                 END             END                     END

        // buttonBoard = new GenericHID(3);


        /* Subsystems */

        // we use the powerboard and pnumatics board on the robot for various things, so we instantiate them here.
       
       // PneumaticHub ph = new PneumaticHub(Constants.REV.PHID);

        // here we instantiate every subsytem on the robot
        // aside from the limelight, mostly because we didn't get the time to set it up
        // and because it probably wouldn't have been be super useful, at least at first.
        pieceMode = new PieceMode();
        s_Swerve = new Swerve();
        s_roller = new rollerTest();
    
        //blindingDevice = new Limelight();

        // Configure the button bindings ( what a useful comment)
        configureDriverControls();
        configureOperatorControls();

        // turn off the light, because it starts on
        //powerBoard.setSwitchableChannel(false); 
        
    }

    /** defines teleop driving and controls on the joysticks. */
    private void configureDriverControls()
    {

         /* Drive Controls */
         // this code sets up driving in teleop.

        int translationAxis = XboxController.Axis.kLeftY.value;
        int strafeAxis = XboxController.Axis.kLeftX.value;
        int rotationAxis = XboxController.Axis.kRightX.value;

        JoystickButton zeroGyro = new JoystickButton(controller, XboxController.Button.kY.value);
        JoystickButton roller = new JoystickButton(operator, 3);
        JoystickButton stopRoller = new JoystickButton(operator, 4);

        
        //Trigger robotCentric = new Trigger(()-> controller.getRawButton(2));

        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> Math.pow(controller.getRawAxis(translationAxis),3), 
                () -> Math.pow(controller.getRawAxis(strafeAxis),3), 
                () -> Math.pow(-controller.getRawAxis(rotationAxis), 3), 
                () -> false
            )
        );

        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
        roller.whileTrue(new InstantCommand(() -> s_roller.setSpeed(1.0)));
        stopRoller.whileTrue(new InstantCommand(() -> s_roller.setSpeed(0)));


        // drive sticksee
        // Trigger zeroGyro = new Trigger(()->controller.getYButtonPressed());
        // zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));

        // Trigger closeClaw = new Trigger(() -> driveStick.getRawButton(10));
        // closeClaw.onTrue(new InstantCommand(()->s_Arm.setClaw(true)));

        // Trigger openClaw= new Trigger(() -> driveStick.getRawButton(11));
        // openClaw.onTrue(new InstantCommand(()->s_Arm.setClaw(false)));


        // // rotate stick
        // Trigger lockSwerve = new Trigger(() -> rotateStick.getRawButton(1));
        // lockSwerve.onTrue(new LockSwerveCommand(s_Swerve, ()->!lockSwerve.getAsBoolean()));

        // Trigger resetOdometrey = new Trigger(()-> rotateStick.getRawButton(2));
        // resetOdometrey.onTrue(new InstantCommand(()-> {s_Swerve.resetOdometry(new Pose2d(   /*wow*/));}));//this is normal
        
        // Trigger oneEightyGryo = new Trigger(() -> rotateStick.getRawButton(3));
        // oneEightyGryo.onTrue(new InstantCommand(()->s_Swerve.zeroGyro(180)));

        // Trigger shootCube = new Trigger(()-> rotateStick.getRawButton(7));
        // shootCube.whileTrue(new ShootPiece(s_Intake, s_Shooter));
     

        

    }
   
  
    /** Defines controls for the button board and controller */
    private void configureOperatorControls()
     {

        // Button board (this is terrible)
        // Trigger BB1 = new Trigger(() -> buttonBoard.getRawButton(1));
        // BB1.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.PouncePreScore, s_Arm,powerBoard)));

        // Trigger BB2 = new Trigger(() -> buttonBoard.getRawButton(2));
        // BB2.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.ScoreL1, s_Arm,powerBoard)));

        // Trigger BB3 = new Trigger(() -> buttonBoard.getRawButton(7));
        // BB3.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.ScoreL2, s_Arm,powerBoard)));

        // Trigger BB4 = new Trigger(() -> buttonBoard.getRawButton(4));
        // BB4.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.ScoreL3, s_Arm,powerBoard)));


        // Trigger BB5 = new Trigger(() -> buttonBoard.getRawButton(5));
        // BB5.onTrue(new InstantCommand(()->{pieceMode.setPiece(GamePiece.cone);}));

        // Trigger BB6 = new Trigger(() -> buttonBoard.getRawButton(6));
        // BB6.onTrue(new InstantCommand(()->{pieceMode.setPiece(GamePiece.cube);}));


        // Trigger BB7 = new Trigger(() -> buttonBoard.getRawButton(3));
        // BB7.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.FloorPick, s_Arm,powerBoard)));

        // Trigger BB8 = new Trigger(() -> buttonBoard.getRawButton(8));
        // BB8.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.PounceDriveUpWindow, s_Arm,powerBoard)));

        // Trigger BB9 = new Trigger(() -> buttonBoard.getRawButton(9));
        // BB9.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.PickDriveUpWindow, s_Arm,powerBoard)));

        // Trigger BB10 = new Trigger(() -> buttonBoard.getRawButton(10));
        // BB10.onTrue(new InstantCommand(()->
        // {
        //     ArmCommand.PlotPathAndSchedule( NamedPose.PickFromSubstation, s_Arm);
        //     powerBoard.setSwitchableChannel(true); 
        // }
        // ));

        // Trigger home = new Trigger(() -> buttonBoard.getRawButton(11));
        // home.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.Home, s_Arm,powerBoard)));

        // new Trigger(() -> buttonBoard.getRawButton(12))
        //     .onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.Travel, s_Arm,powerBoard)));



        // // Xbox controller

        // Trigger intakeButton = new Trigger(()-> controller.getRightBumper());
        // intakeButton.whileTrue(new RunIntake(s_Intake, true));

        // Trigger outtakeButton = new Trigger(()-> controller.getLeftBumper());
        // outtakeButton.whileTrue(new TeleopOuttake(s_Intake, s_Shooter));

        // Trigger shootL1 = new Trigger(()-> controller.getAButton());
        // shootL1.whileTrue(new SpinShooter(s_Shooter, s_Intake, IntakeConfig.slowSpeed));

        // Trigger shootL2 = new Trigger(()-> controller.getXButton());
        // shootL2.whileTrue(new SpinShooter(s_Shooter, s_Intake, IntakeConfig.MediumSpeed));

        // Trigger shootL3 = new Trigger(()-> controller.getYButton());
        // shootL3.whileTrue(new SpinShooter(s_Shooter, s_Intake, IntakeConfig.FastSpeed));

        // Trigger shootFAST= new Trigger(()-> controller.getBButton());
        // shootFAST.whileTrue(new SpinShooter(s_Shooter, s_Intake, 1)
        // .alongWith(ArmCommand.PlotPathAndSchedule(NamedPose.HelpShoot, s_Arm)));
        // shootFAST.onFalse(ArmCommand.PlotPathAndSchedule(NamedPose.Travel, s_Arm));

        // Trigger intakeNoArms = new Trigger(()-> controller.getStartButton());
        // intakeNoArms.whileTrue(new RunIntake(s_Intake, false));      
    }
//todd was here

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
       
        // this is disgusting.
        return new exampleAuto(s_Swerve);

        // I can change whate
    }
}


