// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.autos;

// import com.pathplanner.lib.auto.PIDConstants;
// import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.autos.routines.*;
// import frc.robot.autos.Routines.bottom;
// import frc.robot.autos.Routines.middle;

/** Add your docs here. */
public class AutonChooser {
    private SendableChooser<AutonomousMode> autonomousModeChooser;
    Swerve s_Swerve;

    public AutonChooser(Swerve s_Swerve){
        this.s_Swerve = s_Swerve;
        ShuffleboardTab autoTab = Shuffleboard.getTab("default");

        autonomousModeChooser = new SendableChooser<>();
        autonomousModeChooser.setDefaultOption("Do Nothing", AutonomousMode.doNothing);
        autonomousModeChooser.addOption("Test Routine 1", AutonomousMode.routine1);  
        autoTab.add("autoMode", autonomousModeChooser).withSize(5, 2);
    }
    

    public Command getCommand(){
        AutonomousMode mode = autonomousModeChooser.getSelected();

        switch (mode) {
            case doNothing:
                return new InstantCommand();
            case routine1:
                return new routine1(s_Swerve);
            default:
                System.out.println("ERROR: unexpected auto mode: " + mode);
                break; 
        }
        return null;
    }

    private enum AutonomousMode {
        doNothing,
        routine1
    }
}
