import java.awt.Image
import java.io.File
import javax.imageio.ImageIO

class Knight(team: ETeam): Piece(team) {
    override val image: Image?
        get() = ImageIO.read(File("${team.toString().toLowerCase()}/knight.png"))

    constructor(other: Piece) : this(other.team) {
        tile = other.tile
    }

    override fun getValidMoves(board: BoardState): MutableList<Tile> {
        val tiles = ArrayList<Tile>()

        var x = tile!!.x + 2
        var y = tile!!.y + 1
        checkTile(x, y, board, tiles)

        x = tile!!.x + 2
        y = tile!!.y - 1
        checkTile(x, y, board, tiles)

        x = tile!!.x - 2
        y = tile!!.y + 1
        checkTile(x, y, board, tiles)

        x = tile!!.x - 2
        y = tile!!.y - 1
        checkTile(x, y, board, tiles)

        x = tile!!.x + 1
        y = tile!!.y - 2
        checkTile(x, y, board, tiles)

        x = tile!!.x - 1
        y = tile!!.y - 2
        checkTile(x, y, board, tiles)

        x = tile!!.x + 1
        y = tile!!.y + 2
        checkTile(x, y, board, tiles)

        x = tile!!.x - 1
        y = tile!!.y + 2
        checkTile(x, y, board, tiles)

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