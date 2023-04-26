package frc.robot.autos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

public class AutoBuilder {
    private List<PathPlannerTrajectory> pathGroup;
    private HashMap<String, Command> eventMap;
    private SwerveAutoBuilder autoBuilder;
    private Swerve driveSubsystem;

    public AutoBuilder(Swerve driveSubsystem){
        this.driveSubsystem = driveSubsystem;
    }

    public void loadPath(String pathName){
        pathGroup = PathPlanner.loadPathGroup(pathName, 
            new PathConstraints(Constants.AutoConstants.kMaxSpeedMetersPerSecond, 
                Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared));
        
        eventMap = new HashMap<String, Command>();
    }

    public void addEvent(String markerName, Command command){
        eventMap.put(markerName, command);
    }

    public void buildAutos(){
        this.autoBuilder = new SwerveAutoBuilder(
            driveSubsystem::getPose,
            driveSubsystem::resetOdometry,
            Constants.Swerve.swerveKinematics,
            new PIDConstants(0, 0, 0),
            new PIDConstants(0, 0, 0),
            driveSubsystem::setModuleStates,
            eventMap,
            true,
            driveSubsystem
        );
    }

    public Command getAutoCommand(){
        return autoBuilder.fullAuto(pathGroup);
    }
    
}
