package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.autos.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    private final Joystick driver;
    private final Joystick rotater;
    private final XboxController operator;

    /* Drive Controls */
    private final int translationAxis;
    private final int strafeAxis;
    private final int rotationAxis;

    /*private final int translationAxis = XboxController.Axis.kLeftY.value;
    private final int strafeAxis = XboxController.Axis.kLeftX.value;
    private final int rotationAxis = XboxController.Axis.kRightX.value;*/

    /* Driver Buttons */
    private final Trigger zeroGyro;
    private final Trigger robotCentric;

    private final Trigger intakeButton;
    private final Trigger outtakeButton;
    private final Trigger homeArm;

    private final Trigger coneButton;
    private final Trigger cubeButton;
    
    /*private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    private final JoystickButton robotCentric = new JoystickButton(driver, XboxController.Button.kLeftBumper.value);*/

    /* Subsystems */
    private final Swerve s_Swerve;
    private final Arm s_Arm;
    private final Intake s_Intake;


    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        /* Controllers */
        driver = new Joystick(0);
        rotater = new Joystick(1);
        operator = new XboxController(2);

        /* Drive Controls */
        translationAxis = Joystick.AxisType.kY.value;
        strafeAxis = Joystick.AxisType.kX.value;
        rotationAxis = Joystick.AxisType.kX.value;

        /* Driver Buttons */
        zeroGyro = new Trigger(()->driver.getRawButton(1));
        robotCentric = new Trigger(()-> driver.getRawButton(2));

        intakeButton = new Trigger(()-> driver.getRawButton(4));
        outtakeButton = new Trigger(()-> driver.getRawButton(5));
        homeArm = new Trigger(()-> driver.getRawButton(3));

        coneButton = new Trigger(()-> operator.getRawButton(1));
        cubeButton = new Trigger(()-> operator.getRawButton(2));
        /* Subsystems */
        s_Swerve = new Swerve();
        s_Intake = new Intake();
        s_Arm = new Arm();
            

        s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve, 
                () -> Math.pow(driver.getRawAxis(translationAxis),3), 
                () -> Math.pow(driver.getRawAxis(strafeAxis),3), 
                () -> rotater.getRawAxis(rotationAxis), 
                () -> robotCentric.getAsBoolean()
            )
        );

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
        intakeButton.onTrue(new TeleopIntake(s_Intake));
        outtakeButton.onTrue(new TeleopOuttake(s_Intake));

        coneButton.onTrue(new InstantCommand(() -> s_Intake.wantsCone()));
        cubeButton.onTrue(new InstantCommand(() -> s_Intake.wantsCube()));

        homeArm.onTrue(new ArmCommand(s_Arm, ArmPoses.home) );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return new exampleAuto(s_Swerve);
    }
}
