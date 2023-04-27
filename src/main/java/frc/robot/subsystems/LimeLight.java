// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LimeLight extends SubsystemBase {
  /** Creates a new LimeLight. */

  public double tid;
  public Transform3d botpose;
  public Transform3d targetpose;
  public boolean hasTarget;

  public LimeLight() {
    updateValues();
  }

  private void updateValues() {
    this.tid = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getDouble(0);
    this.hasTarget = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tid").getDouble(0) > 0;
    double[] botPoseDump = NetworkTableInstance.getDefault().getTable("limelight").getEntry("botpose").getDoubleArray(new double[6]);
    double[] targetposeDump = NetworkTableInstance.getDefault().getTable("limelight").getEntry("targetpose").getDoubleArray(new double[6]);
    
    botpose = new Transform3d(new Translation3d(botPoseDump[0], botPoseDump[1], botPoseDump[2]), new Rotation3d(botPoseDump[3], botPoseDump[4], botPoseDump[5]));
    targetpose = new Transform3d(new Translation3d(targetposeDump[0], targetposeDump[1], targetposeDump[2]), new Rotation3d(targetposeDump[3], targetposeDump[4], targetposeDump[5]));
  }


  @Override
  public void periodic() {
    updateValues();
    // This method will be called once per scheduler run
  }
}
