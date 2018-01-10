import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

class Queen(team: ETeam): Piece(team) {
    override val image: Image?
        get() = ImageIO.read(File("${team.toString().toLowerCase()}/queen.png"))

    constructor(other: Piece) : this(other.team) {
        tile = other.tile
    }

    override fun getValidMoves(board: BoardState): MutableList<Tile> {
        val tiles = ArrayList<Tile>()

        //horizontal
        var x = tile!!.x - 1
        while (x > -1) {
            if (!board.isOccupied(x, tile!!.y)) {
                tiles.add(Tile(x, tile!!.y))
            }
            else {
                if (board.isOccupied(x, tile!!.y, otherTeam)) {
                    tiles.add(Tile(x, tile!!.y))
                }
                break
            }
            x--
        }

        x = tile!!.x + 1
        while (x < Board.NUM_FILES) {
            if (!board.isOccupied(x, tile!!.y)) {
                tiles.add(Tile(x, tile!!.y))
            }
            else {
                if (board.isOccupied(x, tile!!.y, otherTeam)) {
                    tiles.add(Tile(x, tile!!.y))
                }
                break
            }
            x++
        }

        x = tile!!.y - 1
        while (x > -1) {
            if (!board.isOccupied(tile!!.x, x)) {
                tiles.add(Tile(tile!!.x, x))
            }
            else {
                if (board.isOccupied(tile!!.x, x, otherTeam)) {
                    tiles.add(Tile(tile!!.x, x))
                }
                break
            }
            x--
        }

        x = tile!!.y + 1
        while (x < Board.NUM_RANKS) {
            if (!board.isOccupied(tile!!.x, x)) {
                tiles.add(Tile(tile!!.x, x))
            }
            else {
                if (board.isOccupied(tile!!.x, x, otherTeam)) {
                    tiles.add(Tile(tile!!.x, x))
                }
                break
            }
            x++
        }

        //diagonal
        x = tile!!.x - 1
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