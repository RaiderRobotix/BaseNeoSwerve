// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 * 
 * ME: you probably don't need to worry about this class when using command robot. most basic robot code ought to be in robot container
 * instead.
 */
public class Robot extends TimedRobot 
{
  
  private boolean m_enabled;
  public void setEnabled(boolean enable){
    m_enabled = enable;
 }
 public void setAutomaticMode(boolean enabled){
    enabled = true;
 }

//  front_sensor.setEnabled();
//  right_sensor.setEnabled();
//  left_sensor.setEnabled();
//  back_sensor.setEnabled();


  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

   private PowerDistribution pd;

/*  THIS CODE IS FOR THE SONIC SENSOR     */

//   static final int SonicPingPort = 1;
//   static final int SonicEchoPort = 2;

//   private final MedianFilter m_filter = new MedianFilter(5);

// private final Ultrasonic sensor1 = new Ultrasonic(SonicPingPort, SonicEchoPort);



  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit()
   {
     pd = new PowerDistribution(20, ModuleType.kRev);
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer(pd);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() 
  {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() 
  {
   

  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() 
  {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    pd.setSwitchableChannel(false);
    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();

    }
      CameraServer.startAutomaticCapture();

    //  front_sensor.setEnabled();
    //  right_sensor.setEnabled();
    //  left_sensor.setEnabled();
    //  back_sensor.setEnabled();

    //  front_sensor.setAutomaticMode(true);


    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

  LimelightHelpers.setLEDMode_PipelineControl("");
  LimelightHelpers.setLEDMode_ForceBlink("");
  LimelightHelpers.setCropWindow("",-1,1,-1,1);
  double tx = LimelightHelpers.getTX("");
  double ty = LimelightHelpers.getTY("");
  double ta = LimelightHelpers.getTA("");
  double[] botpose = LimelightHelpers.getBotPose("");

  System.out.println("horizontal distance: " + tx);
  System.out.println("vertical distance: " + ty);
  System.out.println("target area: " + ta);
  System.out.println(botpose);

  while (1==1){
    tx = LimelightHelpers.getTX("");
    ty = LimelightHelpers.getTY("");
    ta = LimelightHelpers.getTA("");

   botpose = LimelightHelpers.getBotPose("");
    System.out.println(tx);
    System.out.println(ty);
    System.out.println(ta);
    System.out.println(botpose);
  }
    


  //Code below should tell the position of the bot.

  // our defaut limelight name is "limelight". it's automatically added if limelight name is empty.
  
  // front_sensor.setOversampleBits(4);



  }

  @Override
  public void teleopInit() 
  {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) 
    {
      m_autonomousCommand.cancel();
    }

    pd.setSwitchableChannel(false);
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() 
  {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
