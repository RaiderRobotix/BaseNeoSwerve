package frc.robot.Shooter;

import frc.robot.Constants;

import java.util.ArrayList;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase
{
   
    //motors
    private CANSparkMax intakeMotor;
    private CANSparkMax overIntakeMotor;

    //private DoubleSolenoid intakeArms;

    private DigitalInput sensor;

    public Intake(PneumaticHub ph)
    {
        // initialize intake motors
        intakeMotor = new CANSparkMax(Constants.Intake.intakeMotorID, MotorType.kBrushless);
        overIntakeMotor = new CANSparkMax(Constants.Intake.overIntakeMotorID, MotorType.kBrushless);

        intakeMotor.setIdleMode(IdleMode.kCoast);
        intakeMotor.setSmartCurrentLimit(40, 15);

        overIntakeMotor.setIdleMode(IdleMode.kCoast);
        overIntakeMotor.setSmartCurrentLimit(40, 15);
        
        intakeMotor.burnFlash();
        overIntakeMotor.burnFlash();

        //initialize solinoids.
        //intakeArms = ph.makeDoubleSolenoid(Constants.Intake.intakeArmsDownChannel, Constants.Intake.intakeArmsUpChannel);

        sensor = new DigitalInput(Constants.Intake.sensorChannel);
    }


    public void periodic()
    {
        SmartDashboard.putBoolean("Has Piece", hasPiece());
    }

    public void startIntake()
    {
        intakeMotor.set(IntakeConfig.intakeSpeed);
        //intakeArms.set(Value.kForward);
        //overIntakeMotor.set(SPEED);
    }

    public void stopIntake()
    {
        intakeMotor.set(0);
        //intakeArms.set(Value.kReverse);
        //overIntakeMotor.set(0);
    }

    public boolean hasPiece()
    {
        return !sensor.get();
    }

    public void outtake()
    {
        intakeMotor.set(IntakeConfig.outtakeSpeed);
    }
    
    public void shoot()
    {
        intakeMotor.set(IntakeConfig.shootSpeed);
    }
}
