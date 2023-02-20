package frc.robot.Arm;

import com.revrobotics.CANSparkMax.IdleMode;

public final class ArmConfig 
{
    public static final double rampRate = 2;

    public static final class Joint1
    {
        public static final int gearRatio = 144;

        public static final IdleMode idleMode = IdleMode.kBrake;

        public static final float upperLimit = 25;
        public static final float lowerLimit = -25;

        //J1 PID
        public static final double pValue = 0.025;
        public static final double iValue = 0;
        public static final double dValue = 0;
        public static final double ffValue = 0;
        
        public static final double maxPower = 0.3;
    }

    public static final class Joint2
    {
        public static final int gearRatio = 100;

        public static final IdleMode idleMode = IdleMode.kCoast;

        public static final float upperLimit = 130;
        public static final float lowerLimit = -130;

        //J2 PID
        public static final double pValue = 0.01;
        public static final double iValue = 0;
        public static final double dValue = 0;
        public static final double ffValue = 0;
        
        public static final double maxPower = 0.9;
    }

    public static final class Joint3
    {
        public static final int gearRatio = 100;

        public static final IdleMode idleMode = IdleMode.kBrake;

        public static final float upperLimit = 95;
        public static final float lowerLimit = 0;

        //J3 PID
        public static final double pValue = 0.01;
        public static final double iValue = 0;
        public static final double dValue = 0;
        public static final double ffValue = 0;
        
        public static final double maxPower = 1;

           }
}
