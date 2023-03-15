package frc.robot.subsystems;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Shooter extends SubsystemBase
{
    private CANSparkMax shooterMotor;

    
    private double targetSpeed;

    public Shooter()
    {
       shooterMotor = new CANSparkMax(Constants.Shooter.shooterMotorID, MotorType.kBrushless);

        shooterMotor.setIdleMode(IdleMode.kCoast);
        shooterMotor.setSmartCurrentLimit(40, 15);
        shooterMotor.burnFlash();

    }

    public void setSpeed(double speed)
    {
        targetSpeed = speed;
        shooterMotor.set(speed);
    }

    public boolean isUpToSpeed()
    {
        return shooterMotor.getEncoder().getVelocity() >= targetSpeed;
    }

    public void stop()
    {
        setSpeed(0);
    }   
}
