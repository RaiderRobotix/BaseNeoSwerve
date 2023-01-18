package frc.robot.subsystems;

import frc.robot.Constants;
import java.util.concurrent.CancellationException;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase
{
    //motors
    private CANSparkMax intakeMotor;

    public Intake()
    {
        intakeMotor = new CANSparkMax(Constants.Intake.intakeMotorID, MotorType.kBrushless);
    }

    public void setIntakeMotor(double motorSpeed)
    {
        intakeMotor.set(motorSpeed);
    }
    
}
