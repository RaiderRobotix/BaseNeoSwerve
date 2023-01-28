package frc.robot.subsystems;

import frc.robot.Constants;
import java.util.concurrent.CancellationException;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase
{
    private final double SPEED = 0.5;
    //motors
    private CANSparkMax intakeMotor;
    private CANSparkMax undertakeMotor;
    private CANSparkMax undertakeFollowMotor;

    CANSparkMax[] motors = {intakeMotor, undertakeMotor, undertakeFollowMotor};

    private DoubleSolenoid intakeArms;
    private DoubleSolenoid presenter;
    private DoubleSolenoid vaccum;

    public Intake()
    {
        intakeMotor = new CANSparkMax(Constants.Intake.intakeMotorID, MotorType.kBrushless);
        undertakeMotor = new CANSparkMax(Constants.Intake.undertakeMotorID, MotorType.kBrushless);
        undertakeFollowMotor = new CANSparkMax(Constants.Intake.undertakeFollowMotorID, MotorType.kBrushless);

        for(CANSparkMax i : motors)
        {
            i.setIdleMode(IdleMode.kCoast);
            i.setSmartCurrentLimit(40, 15);
        }

        undertakeFollowMotor.follow(undertakeMotor);

        intakeArms = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Intake.intakeArmsDownChannel, Constants.Intake.intakeArmsUpChannel);
        presenter = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Intake.presenterUpChannel, Constants.Intake.presenterDownChannel);
        vaccum = new DoubleSolenoid(PneumaticsModuleType.REVPH, Constants.Intake.vaccumForwardChannel, Constants.Intake.vaccumReverseChannel);
    }

    @Deprecated
    public void setIntakeMotor(double motorSpeed)
    {
        intakeMotor.set(motorSpeed);
    }

    public void startIntake()
    {
        intakeMotor.set(SPEED);
        undertakeMotor.set(SPEED);
        intakeArms.set(Value.kForward);
    }

    //useless as of now
    public void turnArmsOff()
    {
        intakeArms.set(Value.kOff);
    }

    public void stopIntake()
    {
        intakeMotor.set(0);
        undertakeMotor.set(0);
        intakeArms.set(Value.kReverse);
    }

    public void setPresenter(boolean extended)
    {
        if(extended)
        {
            presenter.set(Value.kForward);
        }
        else
        {
            presenter.set(Value.kReverse);
        }
    }

    public void setVaccum(Value position)
    {
        vaccum.set(position);
    }
    
}
