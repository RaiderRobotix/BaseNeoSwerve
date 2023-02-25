package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Leds;

public class ledControlCommand extends CommandBase
{

    // temp garbage

    private Leds led;
    private Joystick J;
    public ledControlCommand(Leds led, Joystick s)
    {
        J = s;
        this.led = led;
        addRequirements(led);   
    }
    @Override
    public void execute()
   {
        double value = (((J.getRawAxis(1)+1)/2.0)*.43)+.57;
        led.set(
            value
        );
        System.out.println(value);
        super.execute();
    }

@Override
public boolean isFinished() {
    // TODO Auto-generated method stub
    return false;
}
    
}
