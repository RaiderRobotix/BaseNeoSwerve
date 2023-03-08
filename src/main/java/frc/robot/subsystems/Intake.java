package frc.robot.subsystems;

import frc.robot.Constants;

import java.util.ArrayList;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase
{
    private final double SPEED = 0.5;
    //motors
    private CANSparkMax intakeMotor;

    ArrayList<CANSparkMax> motors;
    private DoubleSolenoid intakeArms;

    private DigitalInput sensor;

    public Intake(PneumaticHub ph)
    {
        // initialize intake motors
        intakeMotor = new CANSparkMax(Constants.Intake.intakeMotorID, MotorType.kBrushless);


        intakeMotor.setIdleMode(IdleMode.kCoast);
        intakeMotor.setSmartCurrentLimit(40, 15);
        


        // initialize solinoids.
        intakeArms = ph.makeDoubleSolenoid(Constants.Intake.intakeArmsDownChannel, Constants.Intake.intakeArmsUpChannel);

        sensor = new DigitalInput(Constants.Intake.sensorChannel);
    }

    public void startIntake()
    {
        intakeMotor.set(SPEED);
        intakeArms.set(Value.kForward);
    }

    public void stopIntake()
    {
        intakeMotor.set(0);
        intakeArms.set(Value.kReverse);
    }

    public boolean hasPiece()
    {
        return !sensor.get();
    }

    public void outtake()
    {
        intakeMotor.set(-SPEED);
    }
    
    public void shoot()
    {
        intakeMotor.set(SPEED);
    }
}
