package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Swerve;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  public final Joystick driver;

  public final Swerve swerve;

  private final int translationAxis = XboxController.Axis.kLeftY.value;
  private final int strafeAxis = XboxController.Axis.kLeftX.value;
  private final int rotationAxis = XboxController.Axis.kRightX.value;

  public final AutoCommands auto;

  public RobotContainer() {
    driver = new Joystick(0);

    swerve = new Swerve();

    auto = new AutoCommands(swerve);

    // Configure button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    swerve.setDefaultCommand(swerve.drive(
      () -> -Constants.kControls.X_DRIVE_LIMITER.calculate(driver.getRawAxis(translationAxis)),
      () -> -Constants.kControls.Y_DRIVE_LIMITER.calculate(driver.getRawAxis(strafeAxis)), 
      () -> -Constants.kControls.THETA_DRIVE_LIMITER.calculate(driver.getRawAxis(rotationAxis)),
      true,
      false
    ));

    new JoystickButton(driver, XboxController.Button.kY.value)
      .onTrue(swerve.zeroGyroCommand());
  }

    /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return auto.getSelectedCommand();
  }
}
