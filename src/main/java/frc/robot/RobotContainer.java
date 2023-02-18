package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.lib.util.States.GamePiece;
import frc.robot.Arm.Arm;
import frc.robot.Arm.ArmCommand;
import frc.robot.Arm.NamedPose;
import frc.robot.autos.Auto1;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final Joystick driver;
    private final Joystick rotater;
    private final XboxController operator;
    private final GenericHID buttonBoard;

    /* Drive Controls */
    private final int translationAxis;
    private final int strafeAxis;
    private final int rotationAxis;

    /*
     * private final int translationAxis = XboxController.Axis.kLeftY.value;
     * private final int strafeAxis = XboxController.Axis.kLeftX.value;
     * private final int rotationAxis = XboxController.Axis.kRightX.value;
     */

    /* Driver Buttons */
    private final Trigger zeroGyro;
    private final Trigger robotCentric;


    /* Subsystems */
    private final Swerve s_Swerve;
    private final Arm s_Arm;



    private GamePiece armMode;
    // TODO uncomment once intake exists
    // private final Intake s_Intake;

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() 
    {
       
        PneumaticHub ph = new PneumaticHub(Constants.REV.PHID);

        /* Controllers */
        driver = new Joystick(0);
        rotater = new Joystick(1);

        operator = new XboxController(2);
        buttonBoard = new GenericHID(3);


     

        /* Drive Controls */
        translationAxis = Joystick.AxisType.kY.value;
        strafeAxis = Joystick.AxisType.kX.value;
        rotationAxis = Joystick.AxisType.kX.value;

        /* Driver Buttons */
        zeroGyro = new Trigger(()->driver.getRawButton(1));
        robotCentric = new Trigger(()-> driver.getRawButton(2));

        /* Subsystems */
        s_Swerve = new Swerve();
        // TODO Uncomment once intake exists.
        //s_Intake = new Intake();
        armMode = GamePiece.cone;
        s_Arm = new Arm(NamedPose.Home, ph, ()->armMode);
            
   

        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> Math.pow(driver.getRawAxis(translationAxis),3), 
                () -> Math.pow(driver.getRawAxis(strafeAxis),3), 
                () -> rotater.getRawAxis(rotationAxis)/2, 
                () -> robotCentric.getAsBoolean()
            )
        );

        // Configure the button bindings
        configureButtonBindings();
        
    }


   
    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {

        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));

        Trigger lockSwerve = new Trigger(() -> rotater.getRawButton(1));
        lockSwerve.onTrue(new LockSwerveCommand(s_Swerve, ()->!lockSwerve.getAsBoolean()));

        Trigger closeClaw = new Trigger(() -> rotater.getRawButton(7));
        closeClaw.onTrue(new InstantCommand(()->s_Arm.setClaw(true)));

        Trigger openClaw= new Trigger(() -> rotater.getRawButton(6));
        openClaw.onTrue(new InstantCommand(()->s_Arm.setClaw(false)));

        // TODO uncomment once intake... exists.
        // intakeButton.onTrue(new TeleopIntake(s_Intake, s_Arm));
        // outtakeButton.onTrue(new TeleopOuttake(s_Intake));

        // coneButton.onTrue(new InstantCommand(() -> s_Intake.wantsCone()));
        // cubeButton.onTrue(new InstantCommand(() -> s_Intake.wantsCube()));

        // Button board (this is terrible)
        Trigger BB1 = new Trigger(() -> buttonBoard.getRawButton(1));
        BB1.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.PouncePreScore, s_Arm)));

        Trigger BB2 = new Trigger(() -> buttonBoard.getRawButton(2));
        BB2.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.ScoreL1, s_Arm)));

        Trigger BB3 = new Trigger(() -> buttonBoard.getRawButton(3));
        BB3.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.ScoreL2, s_Arm)));

        Trigger BB4 = new Trigger(() -> buttonBoard.getRawButton(4));
        BB4.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.ScoreL3, s_Arm)));

        Trigger BB5 = new Trigger(() -> buttonBoard.getRawButton(5));
        BB5.onTrue(new InstantCommand(()->{armMode=GamePiece.cone;}));

        
        Trigger BB6 = new Trigger(() -> buttonBoard.getRawButton(6));
        BB6.onTrue(new InstantCommand(()->{armMode=GamePiece.cube;}));

        Trigger BB7 = new Trigger(() -> buttonBoard.getRawButton(7));
        BB7.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.FloorPick, s_Arm)));

        Trigger BB8 = new Trigger(() -> buttonBoard.getRawButton(8));
        BB8.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.PounceDriveUpWindow, s_Arm)));

        Trigger BB9 = new Trigger(() -> buttonBoard.getRawButton(9));
        BB9.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.PickDriveUpWindow, s_Arm)));

        Trigger BB10 = new Trigger(() -> buttonBoard.getRawButton(10));
        BB10.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.PickFromSubstation, s_Arm)));

        Trigger home = new Trigger(() -> buttonBoard.getRawButton(11));
        home.onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.Home, s_Arm)));

        new Trigger(() -> buttonBoard.getRawButton(12))
            .onTrue(new InstantCommand(()->ArmCommand.PlotPath( NamedPose.Travel, s_Arm)));

      


        // Xbox controller



        Trigger resetOdometrey = new Trigger(()-> rotater.getRawButton(2));
        resetOdometrey.onTrue(new InstantCommand(()-> {s_Swerve.resetOdometry(new Pose2d(   /*wow*/));}));//this is normal

       


        Trigger jogJ1Forwards = new Trigger(()-> driver.getRawButton(6));
        jogJ1Forwards.whileTrue(new InstantCommand(()-> s_Arm.jogJoint(1, true)));

        Trigger jogJ1Backwards = new Trigger(()-> driver.getRawButton(7));
        jogJ1Backwards.whileTrue(new InstantCommand(()-> s_Arm.jogJoint(1, false)));

        Trigger jogJ2Forwards = new Trigger(()-> driver.getRawButton(8));
        jogJ2Forwards.whileTrue(new InstantCommand(()-> s_Arm.jogJoint(2, true)));

        Trigger jogJ2Backwards = new Trigger(()-> driver.getRawButton(9));
        jogJ2Backwards.whileTrue(new InstantCommand(()-> s_Arm.jogJoint(2, false)));

        Trigger jogJ3Forwards = new Trigger(()-> driver.getRawButton(10));
        jogJ3Forwards.whileTrue(new InstantCommand(()-> s_Arm.jogJoint(3, true)));

        Trigger jogJ3Backwards = new Trigger(()-> driver.getRawButton(11));
        jogJ3Backwards.whileTrue(new InstantCommand(()-> s_Arm.jogJoint(3, false)));


    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return new Auto1(s_Swerve, s_Arm);
    }
}
