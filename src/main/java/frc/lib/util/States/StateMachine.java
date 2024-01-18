package frc.lib.util.States;


import edu.wpi.first.wpilibj2.command.Command;



public class StateMachine 
{
  
    private LinkableState current;

    public StateMachine(LinkableState initial)
    {
       
        current = initial;
       

    }

    

    // returns whether the stateMachine is finished running.
    public boolean execute()
    {
        if(current == null)
        {
            return true;
        }

        current = current.run();
        return current == null;
    }

 

    public LinkableState waitForCommand(Command waitingOn, LinkableState after)
    {

        if(waitingOn.isFinished())
        {
            return after;
        }

        return ()->{return waitForCommand(waitingOn, after);};
    }
    

}
