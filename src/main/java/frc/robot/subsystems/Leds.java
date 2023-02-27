package frc.robot.subsystems;

import java.util.Random;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.States.GamePieceSupplier;

public class Leds extends SubsystemBase
{
    
    private Spark controller;
    private GamePieceSupplier gsp;

    public Leds(GamePieceSupplier gsp)
    {
        controller = new Spark(9);
        this.gsp = gsp;

    }

    public void periodic()
    {
       controller.set(getColor());
    }


    public boolean set(double color)
    {
        controller.set(color);
        return false;
    }


    private double getColor()
    {
        
        switch(gsp.getAsGamePiece())
        {
            case cone:
                return .65; // 'gold'
            case cube:
                return .91; // 'violet'
           
            
        }
        return -.99;
        //return getRandomPattern();
    }

    private double getRandomPattern()
    {
        Random rand = new Random();
        double[] patterns = 
        {-.99, -.97, -.79, -.89, -.59};

        int n = rand.nextInt(patterns.length);

        return patterns[n];
    }
}
