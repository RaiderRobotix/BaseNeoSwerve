// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;


import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class rollerTest extends SubsystemBase {
  /** Creates a new rollerTest. */

  private TalonFX Lroller;
  private TalonFX Rroller;

  public rollerTest() {
    this.Lroller = new TalonFX(9);
    this.Rroller = new TalonFX(0);
    
  }

  public void setSpeed(double speed){
    Lroller.set(speed);
    Rroller.set(-speed);
  }


  public void stop(){
    Lroller.set(0);
    Rroller.set(0);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
