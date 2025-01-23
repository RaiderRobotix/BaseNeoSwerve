package frc.lib.util.States;

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
