package frc.robot;


import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.lib.util.States.GamePiece;
import frc.robot.Arm.Arm;
import frc.robot.Arm.NamedPose;
import frc.robot.Arm.command.ArmCommand;
import frc.robot.Shooter.Intake;
import frc.robot.Shooter.IntakeConfig;
import frc.robot.Shooter.Shooter;
import frc.robot.Shooter.Commands.ShootPiece;
import frc.robot.Shooter.Commands.SpinShooter;
import frc.robot.Shooter.Commands.TeleopIntake;
import frc.robot.Shooter.Commands.TeleopOuttake;
import frc.robot.autos.AutoSelector;
import frc.robot.subsystems.Leds;
import frc.robot.subsystems.Thumbwheel;
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
    private final Joystick driveStick;
    private final Joystick rotateStick;
    private final XboxController controller;
    private final GenericHID buttonBoard;
    //private Limelight blindingDevice;

  


    /* Subsystems */
    // the code we were using used the prefix s_ for all of the names of subsystems here.
    // personally, this seems useless, and I would avoid it if I were you
    // I mean it would take like 30 seconds to fix honestly but whatever
    private final Swerve s_Swerve;
    private final Arm s_Arm;
    private final Shooter s_Shooter;
    private final Thumbwheel s_Thumb;
    private final Intake s_Intake;



    private PowerDistribution powerBoard;
    private PieceMode pieceMode; // the piecemode class essentially controls whether the arm is in cone or cube mode. 
                                 //the arm doesn't know about it though


    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() 
    {

        CameraServer.startAutomaticCapture();

        /* Controllers */
        driveStick = new Joystick(0);
        rotateStick = new Joystick(1);
        controller = new XboxController(2);
        buttonBoard = new GenericHID(3);



        /* Subsystems */

        // we use the powerboard and pnumatics board on the robot for various things, so we instantiate them here.
        powerBoard = new PowerDistribution(20, ModuleType.kRev);
        PneumaticHub ph = new PneumaticHub(Constants.REV.PHID);

        // here we instantiate every subsytem on the robot
        // aside from the limelight, mostly because we didn't get the time to set it up
        // and because it probably wouldn't have been be super useful, at least at first.
        pieceMode = new PieceMode();
        s_Swerve = new Swerve();
        s_Thumb = new Thumbwheel();
        s_Arm = new Arm(NamedPose.Home, ph, pieceMode);
        s_Intake = new Intake(ph);
        s_Shooter = new Shooter();
        new Leds(pieceMode);
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

        int translationAxis = Joystick.AxisType.kY.value;
        int strafeAxis = Joystick.AxisType.kX.value;
        int rotationAxis = Joystick.AxisType.kX.value;
        
        Trigger robotCentric = new Trigger(()-> driveStick.getRawButton(2));

        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> Math.pow(driveStick.getRawAxis(translationAxis),3), 
                () -> Math.pow(driveStick.getRawAxis(strafeAxis),3), 
                () -> rotateStick.getRawAxis(rotationAxis)/2, 
                () -> robotCentric.getAsBoolean()
            )
        );

        // drive stick
        Trigger zeroGyro = new Trigger(()->driveStick.getRawButton(1));
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));

        Trigger closeClaw = new Trigger(() -> driveStick.getRawButton(10));
        closeClaw.onTrue(new InstantCommand(()->s_Arm.setClaw(true)));

        Trigger openClaw= new Trigger(() -> driveStick.getRawButton(11));
        openClaw.onTrue(new InstantCommand(()->s_Arm.setClaw(false)));


        // rotate stick
        Trigger lockSwerve = new Trigger(() -> rotateStick.getRawButton(1));
        lockSwerve.onTrue(new LockSwerveCommand(s_Swerve, ()->!lockSwerve.getAsBoolean()));

        Trigger resetOdometrey = new Trigger(()-> rotateStick.getRawButton(2));
        resetOdometrey.onTrue(new InstantCommand(()-> {s_Swerve.resetOdometry(new Pose2d(   /*wow*/));}));//this is normal
        
        Trigger oneEightyGryo = new Trigger(() -> rotateStick.getRawButton(3));
        oneEightyGryo.onTrue(new InstantCommand(()->s_Swerve.zeroGyro(180)));

        Trigger shootCube = new Trigger(()-> rotateStick.getRawButton(7));
        shootCube.whileTrue(new ShootPiece(s_Intake, s_Shooter));
     

        

    }
   
  
    /** Defines controls for the button board and controller */
    private void configureOperatorControls()
     {

        // Button board (this is terrible)
        Trigger BB1 = new Trigger(() -> buttonBoard.getRawButton(1));
        BB1.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.PouncePreScore, s_Arm,powerBoard)));

        Trigger BB2 = new Trigger(() -> buttonBoard.getRawButton(2));
        BB2.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.ScoreL1, s_Arm,powerBoard)));

        Trigger BB3 = new Trigger(() -> buttonBoard.getRawButton(7));
        BB3.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.ScoreL2, s_Arm,powerBoard)));

        Trigger BB4 = new Trigger(() -> buttonBoard.getRawButton(4));
        BB4.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.ScoreL3, s_Arm,powerBoard)));


        Trigger BB5 = new Trigger(() -> buttonBoard.getRawButton(5));
        BB5.onTrue(new InstantCommand(()->{pieceMode.setPiece(GamePiece.cone);}));

        Trigger BB6 = new Trigger(() -> buttonBoard.getRawButton(6));
        BB6.onTrue(new InstantCommand(()->{pieceMode.setPiece(GamePiece.cube);}));


        Trigger BB7 = new Trigger(() -> buttonBoard.getRawButton(3));
        BB7.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.FloorPick, s_Arm,powerBoard)));

        Trigger BB8 = new Trigger(() -> buttonBoard.getRawButton(8));
        BB8.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.PounceDriveUpWindow, s_Arm,powerBoard)));

        Trigger BB9 = new Trigger(() -> buttonBoard.getRawButton(9));
        BB9.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.PickDriveUpWindow, s_Arm,powerBoard)));

        Trigger BB10 = new Trigger(() -> buttonBoard.getRawButton(10));
        BB10.onTrue(new InstantCommand(()->
        {
            ArmCommand.PlotPathAndSchedule( NamedPose.PickFromSubstation, s_Arm);
            powerBoard.setSwitchableChannel(true); 
        }
        ));

        Trigger home = new Trigger(() -> buttonBoard.getRawButton(11));
        home.onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.Home, s_Arm,powerBoard)));

        new Trigger(() -> buttonBoard.getRawButton(12))
            .onTrue(new InstantCommand(()->ArmCommand.PlotPathAndSchedule( NamedPose.Travel, s_Arm,powerBoard)));


        // Xbox controller

        Trigger intakeButton = new Trigger(()-> controller.getRightBumper());
        intakeButton.whileTrue(new TeleopIntake(s_Intake));

        Trigger outtakeButton = new Trigger(()-> controller.getLeftBumper());
        outtakeButton.whileTrue(new TeleopOuttake(s_Intake, s_Shooter));

        Trigger shootL1 = new Trigger(()-> controller.getAButton());
        shootL1.whileTrue(new SpinShooter(s_Shooter, IntakeConfig.level1Speed));

        Trigger shootL2 = new Trigger(()-> controller.getXButton());
        shootL2.whileTrue(new SpinShooter(s_Shooter, IntakeConfig.level2Speed));

        Trigger shootL3 = new Trigger(()-> controller.getYButton());
        shootL3.whileTrue(new SpinShooter(s_Shooter, IntakeConfig.level3Speed));
        
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        AutoSelector auto = new AutoSelector(s_Thumb, s_Arm, s_Swerve, s_Intake, s_Shooter, pieceMode);
        return auto.getAutonomousCommand();
    }
}
