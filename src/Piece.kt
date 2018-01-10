import java.awt.Graphics
import java.awt.Image

abstract class Piece(val team: ETeam) {
    val otherTeam = when (team) {
        ETeam.White -> ETeam.Black
        ETeam.Black -> ETeam.White
    }
    var tile: Tile? = null
    abstract val image: Image?

    abstract fun getValidMoves(board: BoardState) : MutableList<Tile>

    fun draw(g: Graphics?, x: Int, y: Int) {
        g?.drawImage(image, x, y, 64, 64, null)
    }

    open fun onMove() {

    }

    override fun toString(): String {
        return "$team${javaClass.name}[${tile?.x},${tile?.y}]"
    }
}