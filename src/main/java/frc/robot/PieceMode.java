package frc.robot;

import frc.lib.util.States.GamePiece;
import frc.lib.util.States.GamePieceSupplier;

/**
 * PieceMode is a class that is shared between different subsytems and parts of the code, and is a way of reducing coupling.
 * It signfies which mode the arm is in, cone or cube.
 * It's much better, in my mind, for anything that needs to know to have a reference to this class instead of having to directly probe the
 * arm or other part of the code.
 * this way, code that might depend on this information know as little additional information as possible.
 */
public class PieceMode implements GamePieceSupplier
{
    private GamePiece piece;
    public PieceMode()
    {
        piece = GamePiece.cone;
    }


    public void setPiece(GamePiece p)
    {
        piece = p;
    }

    @Override
    public GamePiece getAsGamePiece()
    {
        return piece;
    }
}
