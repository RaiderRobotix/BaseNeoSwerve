package frc.robot.Arm;

import frc.lib.util.States.GamePiece;

public class DoublePose implements ArmPose
{

    private GamePiece mode;
    private BasicPose cube;
    private BasicPose cone;

    public DoublePose(BasicPose cube, BasicPose cone)
    {
        mode = GamePiece.cone;

        this.cube = cube;
        this.cone = cone;

    }

    public void update(GamePiece p)
    {
        mode = p;
    }

    private BasicPose getRealPose()
    {
        if(mode == GamePiece.cone)
        {
            return cone;
        }

        return cube;
    }

    public Double getJ1()
    {
        return getRealPose().getJ1();
    }

    public Double getJ2()
    {
        return getRealPose().getJ2();
    }

    public Double getJ3()
    {
        return getRealPose().getJ3();
    }

    public boolean getExtender()
    {
        return getRealPose().getExtender();
    }

    public String toString()
    {
        return getRealPose().toString();
    }
}
