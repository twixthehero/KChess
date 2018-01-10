import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

class Rook(team: ETeam): Piece(team) {
    var hasMoved = false

    override val image: Image?
        get() = ImageIO.read(File("${team.toString().toLowerCase()}/rook.png"))

    constructor(other: Rook) : this(other.team) {
        tile = other.tile
        hasMoved = other.hasMoved
    }

    override fun onMove() {
        hasMoved = true
    }

    override fun getValidMoves(board: BoardState): MutableList<Tile> {
        val tiles = ArrayList<Tile>()

        var i = tile!!.x - 1
        while (i > -1) {
            if (!board.isOccupied(i, tile!!.y)) {
                tiles.add(Tile(i, tile!!.y))
            }
            else {
                if (board.isOccupied(i, tile!!.y, otherTeam)) {
                    tiles.add(Tile(i, tile!!.y))
                }
                break
            }
            i--
        }

        i = tile!!.x + 1
        while (i < Board.NUM_FILES) {
            if (!board.isOccupied(i, tile!!.y)) {
                tiles.add(Tile(i, tile!!.y))
            }
            else {
                if (board.isOccupied(i, tile!!.y, otherTeam)) {
                    tiles.add(Tile(i, tile!!.y))
                }
                break
            }
            i++
        }

        i = tile!!.y - 1
        while (i > -1) {
            if (!board.isOccupied(tile!!.x, i)) {
                tiles.add(Tile(tile!!.x, i))
            }
            else {
                if (board.isOccupied(tile!!.x, i, otherTeam)) {
                    tiles.add(Tile(tile!!.x, i))
                }
                break
            }
            i--
        }

        i = tile!!.y + 1
        while (i < Board.NUM_RANKS) {
            if (!board.isOccupied(tile!!.x, i)) {
                tiles.add(Tile(tile!!.x, i))
            }
            else {
                if (board.isOccupied(tile!!.x, i, otherTeam)) {
                    tiles.add(Tile(tile!!.x, i))
                }
                break
            }
            i++
        }

        return tiles
    }
}