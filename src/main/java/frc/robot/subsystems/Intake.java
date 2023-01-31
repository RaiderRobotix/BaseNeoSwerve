package frc.robot.subsystems;

import frc.robot.Constants;
import java.util.concurrent.CancellationException;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase
{
    private IntakeState intakeState;
    private final double SPEED = 0.5;
    //motors
    private CANSparkMax intakeMotor;
    private CANSparkMax undertakeMotor;
    private CANSparkMax undertakeFollowMotor;

    CANSparkMax[] motors = {intakeMotor, undertakeMotor, undertakeFollowMotor};

    private DoubleSolenoid intakeArms;
    private DoubleSolenoid presenter;
    private DoubleSolenoid vaccum;

    private DigitalInput sensor;

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

        sensor = new DigitalInput(Constants.Intake.sensorChannel);
    }

    public void startIntake()
    {
        intakeMotor.set(SPEED);
        undertakeMotor.set(SPEED);
        intakeArms.set(Value.kForward);
        presenter.set(Value.kForward);
    }

    public void stopIntake()
    {
        intakeMotor.set(0);
        undertakeMotor.set(0);
        intakeArms.set(Value.kReverse);
        presenter.set(Value.kReverse);
    }

    public void setPresenter(boolean extended)
    {
        if(extended)
        {
            presenter.set(Value.kForward);
            return;
        }
        presenter.set(Value.kReverse);
    }

    public void setVaccum(boolean on)
    {
        if(on)
        {
            vaccum.set(Value.kForward);
            return;
        }
        vaccum.set(Value.kReverse);
    }

    public boolean getSensorValue()
    {
        return !sensor.get();
    }

    public void outtake()
    {
        intakeMotor.set(-SPEED);
        undertakeMotor.set(-SPEED);
        setPresenter(true);
    }
    
    public IntakeState getState()
    {
        return intakeState;
    }

    public void collectPiece()
    {
        if(intakeState == IntakeState.wantsCone)
        {
            intakeState = IntakeState.getCone;
            return;
        }
        if(intakeState == IntakeState.wantsCube)
        {
            intakeState = IntakeState.getCube;
            return;
        }
    }

    public void wantsCone()
    {
        if(canWant()) intakeState = IntakeState.wantsCone;
    }

    public void wantsCube()
    {
        if(canWant()) intakeState = IntakeState.wantsCube;
    }

    public boolean canWant()//you must ask permission to have free will
    {
        return !(intakeState == IntakeState.getCone || intakeState == IntakeState.getCube);
    }

}
