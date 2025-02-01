package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class limeLight_subsystem {
    private double tx;
    private double ty;
    private double ta;
    private int tid;
    private NetworkTable table;
    
    public limeLight_subsystem(){

    table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx").getDouble(0.0);
        ty = table.getEntry("ty").getDouble(0.0);
        ta = table.getEntry("ta").getDouble(0.0);

    
//  Below is code that measures the distance of the limelight.
//Tip: We use DEGREES & INCHES

        double TargetOffsetAngle_Vertical = ty;

        double LimelightAngle = 0;

        double LimelightHeight = 19.25;

        //distance of tag to floor
        double TagHeight = 16;

        double AngleToTag = LimelightAngle + TargetOffsetAngle_Vertical;
        double AngleToTagRadians = AngleToTag * (3.14159 / 180);

        //distance of tag to lime
        double LimeToTagDistance = (TagHeight - LimelightHeight) / Math.tan(AngleToTagRadians);


        System.out.println(LimeToTagDistance);

       
    }
}

