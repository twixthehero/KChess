import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

class Bishop(team: ETeam): Piece(team) {
    override val image: Image?
        get() = ImageIO.read(File("${team.toString().toLowerCase()}/bishop.png"))

    constructor(other: Piece) : this(other.team) {
        tile = other.tile
    }

    override fun getValidMoves(board: BoardState): MutableList<Tile> {
        val tiles = ArrayList<Tile>()

        var x = tile!!.x - 1
        var y = tile!!.y - 1
        while (x > -1 && y > -1) {
            if (!board.isOccupied(x, y)) {
                tiles.add(Tile(x, y))
            } else {
                if (board.isOccupied(x, y, otherTeam)) {
                    tiles.add(Tile(x, y))
                }
                break
            }

            x--
            y--
        }

        x = tile!!.x - 1
        y = tile!!.y + 1
        while (x > -1 && y < Board.NUM_RANKS) {
            if (!board.isOccupied(x, y)) {
                tiles.add(Tile(x, y))
            } else {
                if (board.isOccupied(x, y, otherTeam)) {
                    tiles.add(Tile(x, y))
                }
                break
            }

            x--
            y++
        }

        x = tile!!.x + 1
        y = tile!!.y - 1
        while (x < Board.NUM_FILES && y > -1) {
            if (!board.isOccupied(x, y)) {
                tiles.add(Tile(x, y))
            } else {
                if (board.isOccupied(x, y, otherTeam)) {
                    tiles.add(Tile(x, y))
                }
                break
            }

            x++
            y--
        }

        x = tile!!.x + 1
        y = tile!!.y + 1
        while (x < Board.NUM_FILES && y < Board.NUM_RANKS) {
            if (!board.isOccupied(x, y)) {
                tiles.add(Tile(x, y))
            } else {
                if (board.isOccupied(x, y, otherTeam)) {
                    tiles.add(Tile(x, y))
                }
                break
            }

            x++
            y++
        }

        return tiles
    }
}