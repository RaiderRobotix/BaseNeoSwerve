package frc.lib.util.States;

import java.util.ArrayList;



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

}
