import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

class Pawn(team: ETeam): Piece(team) {
    private var hasMoved = false
    private val dir = when (team == ETeam.White) {
        true -> 1
        false -> -1
    }

    constructor(other: Pawn) : this(other.team) {
        tile = other.tile
        hasMoved = other.hasMoved
    }

    override val image: Image?
        get() = ImageIO.read(File("${team.toString().toLowerCase()}/pawn.png"))

    override fun onMove() {
        hasMoved = true
    }

    override fun getValidMoves(board: BoardState): MutableList<Tile> {
        val tiles = ArrayList<Tile>()

        if (!board.isOccupied(tile!!.x, tile!!.y + dir)) {
            tiles.add(Tile(tile!!.x, tile!!.y + dir))

            if (!hasMoved && !board.isOccupied(tile!!.x, tile!!.y + dir * 2))
                tiles.add(Tile(tile!!.x, tile!!.y + dir * 2))
        }

        //attacks
        if (tile!!.x - 1 >= 0 && board.isOccupied(tile!!.x - 1, tile!!.y + dir, otherTeam))
            tiles.add(Tile(tile!!.x - 1, tile!!.y + dir))
        if (tile!!.x + 1 < Board.NUM_FILES && board.isOccupied(tile!!.x + 1, tile!!.y + dir, otherTeam))
            tiles.add(Tile(tile!!.x + 1, tile!!.y + dir))

        return tiles
    }
}