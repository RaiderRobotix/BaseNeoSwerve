package frc.robot.subsystems;

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
        intakeMotor = new CANSparkMax(0, MotorType.kBrushless);
    }

    public void setIntakeMotor(double motorSpeed)
    {
        intakeMotor.set(motorSpeed);
    }
    
}
