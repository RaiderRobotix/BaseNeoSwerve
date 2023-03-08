package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase
{
    private CANSparkMax shooterMotor;

    

    public Shooter()
    {
        shooterMotor.setIdleMode(IdleMode.kCoast);
    }
}
