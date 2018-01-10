import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

class King(team: ETeam): Piece(team) {
    private var hasMoved = false

    override val image: Image?
        get() = ImageIO.read(File("${team.toString().toLowerCase()}/king.png"))

    constructor(other: King) : this(other.team) {
        tile = other.tile
        hasMoved = other.hasMoved
    }

    override fun onMove() {
        hasMoved = true
    }

    override fun getValidMoves(board: BoardState): MutableList<Tile> {
        val tiles = ArrayList<Tile>()

        for (xx in -1..1) {
            (-1..1)
                    .filterNot { xx == -1 && it == -1 }
                    .forEach { checkTile(tile!!.x + xx, tile!!.y + it, board, tiles) }
        }

        //check can castle
        if (!hasMoved) {
            var x = tile!!.x - 1
            while (x > 0) {
                if (board.isOccupied(x, tile!!.y)) {
                    break
                }
                x--
            }
            if (x == 0) {
                val rook = board.get(0, tile!!.y)
                if (rook is Rook && !rook.hasMoved) {
                    tiles.add(Tile(tile!!.x - 2, tile!!.y))
                }
            }

            x = tile!!.x + 1
            while (x < Board.NUM_FILES - 1) {
                if (board.isOccupied(x, tile!!.y)) {
                    break
                }
                x++
            }
            if (x == Board.NUM_FILES - 1) {
                val rook = board.get(Board.NUM_FILES - 1, tile!!.y)
                if (rook is Rook && !rook.hasMoved) {
                    tiles.add(Tile(tile!!.x + 2, tile!!.y))
                }
            }
        }

        return tiles
    }

    private fun checkTile(x: Int, y: Int, board: BoardState, tiles: ArrayList<Tile>) {
        if (0 <= x && x < Board.NUM_FILES && 0 <= y && y < Board.NUM_RANKS) {
            if (!board.isOccupied(x, y) || board.isOccupied(x, y, otherTeam)) {
                tiles.add(Tile(x, y))
            }
        }
    }
}