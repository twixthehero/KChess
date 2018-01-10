import java.awt.Color
import java.awt.Graphics
import javax.swing.JOptionPane

class Board {
    companion object {
        const val NUM_FILES = 8
        const val NUM_RANKS = 8
    }

    private val states = ArrayList<BoardState>()
    val board = Array(8, { arrayOfNulls<Piece>(8) })
    private var turn = ETeam.White
    private var gameover = false

    private var viewFromWhite = true
    private var sx: Int = -1
    private var sy: Int = -1
    private var valid: MutableList<Tile>? = null

    private val kingWhite = King(ETeam.White)
    private val kingBlack = King(ETeam.Black)

    // list of capped pieces from X team
    private val capWhite = ArrayList<Piece>()
    private val capBlack = ArrayList<Piece>()

    fun init() {
        turn = ETeam.White

        states.clear()
        capWhite.clear()
        capBlack.clear()

        sx = -1
        sy = -1
        valid = null

        for (file in 0 until NUM_FILES) {
            for (rank in 0 until NUM_RANKS)
                board[file][rank] = null
        }

        for (i in 0 until NUM_FILES) {
            place(Pawn(ETeam.White), i, 1)
            place(Pawn(ETeam.Black), i, 6)
        }

        place(Rook(ETeam.White), 0, 0)
        place(Rook(ETeam.White), 7, 0)
        place(Rook(ETeam.Black), 0, 7)
        place(Rook(ETeam.Black), 7, 7)

        place(Knight(ETeam.White), 1, 0)
        place(Knight(ETeam.White), 6, 0)
        place(Knight(ETeam.Black), 1, 7)
        place(Knight(ETeam.Black), 6, 7)

        place(Bishop(ETeam.White), 2, 0)
        place(Bishop(ETeam.White), 5, 0)
        place(Bishop(ETeam.Black), 2, 7)
        place(Bishop(ETeam.Black), 5, 7)

        place(kingWhite, 4, 0)
        place(Queen(ETeam.White), 3, 0)
        place(kingBlack, 4, 7)
        place(Queen(ETeam.Black), 3, 7)

        states.add(BoardState(board, turn, capWhite, capBlack))
    }

    private fun place(piece: Piece, x: Int, y: Int) {
        piece.tile = Tile(x, y)
        board[x][y] = piece
    }

    fun switchSides() {
        viewFromWhite = !viewFromWhite
    }

    fun select(xx: Int, yy: Int) {
        if (gameover)
            return

        var x = xx
        var y = yy
        if (!viewFromWhite) {
            x = Board.NUM_FILES - x - 1
            y = Board.NUM_RANKS - y - 1
        }

        if (x < 0 || x > NUM_FILES - 1 ||
                y < 0 || y > NUM_RANKS - 1)
            return

        val curState = states[states.size - 1]
        if (selectedMove(x, y)) {
            val nextState = BoardState(curState.board, turn, capWhite, capBlack)
            val cap = nextState.playMove(sx, sy, x, y)
            when (cap?.team) {
                ETeam.White -> {
                    capWhite.add(cap)
                }
                ETeam.Black -> {
                    capBlack.add(cap)
                }
            }
            states.add(nextState)
            sx = -1
            sy = -1
            valid = null
            val lastTurn = turn
            nextTurn()

            var isMate = true
            for (piece in when (turn) {
                ETeam.White -> nextState.white
                ETeam.Black -> nextState.black
            }) {
                val moves = piece.getValidMoves(nextState)
                val toRemove = ArrayList<Int>()
                for (i in 0 until moves.size) {
                    val tile = piece.tile
                    if (nextState.simMove(tile!!.file, tile.rank, moves[i].file, moves[i].rank).isInCheck())
                        toRemove.add(i)
                }
                while (toRemove.size > 0) {
                    moves.removeAt(toRemove[toRemove.size - 1])
                    toRemove.removeAt(toRemove.size - 1)
                }

                if (moves.size > 0)
                    isMate = false
            }
            //check for checkmate

            if (isMate) {
                gameover = true
                JOptionPane.showMessageDialog(App.instance, "$lastTurn checkmated $turn", "Gameover", JOptionPane.PLAIN_MESSAGE)
            }
        } else if (curState.get(x, y) == null || curState.get(x, y)?.team != turn) {
            //clear selection
            sx = -1
            sy = -1
            valid = null
        } else {
            //select piece
            sx = x
            sy = y

            valid = curState.get(sx, sy)?.getValidMoves(curState)
            //remove invalid moves because of check
            val removeMoves = ArrayList<Int>()

            //simulate each valid move and see if check happens after
            for (i in 0 until valid!!.size) {
                val dest = valid!![i]
                if (curState.simMove(sx, sy, dest.file, dest.rank).isInCheck())
                    removeMoves.add(i)
            }

            while (removeMoves.size > 0) {
                valid?.removeAt(removeMoves[removeMoves.size - 1])
                removeMoves.removeAt(removeMoves.size - 1)
            }
        }
    }

    private fun nextTurn() {
        turn = when (turn) {
            ETeam.White -> ETeam.Black
            ETeam.Black -> ETeam.White
        }
    }

    private fun selectedMove(x: Int, y: Int) : Boolean {
        if (valid == null)
            return false

        return (0 until valid!!.size)
                .map { (valid as List<Tile>)[it] }
                .any { it.x == x && it.y == y }
    }

    fun draw(g: Graphics?) {
        drawBackground(g)

        states[states.size - 1].draw(g, viewFromWhite)

        if (sx != -1 && sy != -1) {
            g?.color = Color.GREEN

            if (viewFromWhite)
                g?.drawRect(sx * 64, (7 - sy) * 64, 64, 64)
            else
                g?.drawRect((7 - sx) * 64, sy * 64, 64, 64)

            if (valid != null) {
                g?.color = Color.ORANGE
                for (tile in valid as List<Tile>) {
                    if (viewFromWhite)
                        g?.drawRect(tile.x * 64, (7 - tile.y) * 64, 64, 64)
                    else
                        g?.drawRect((7 - tile.x) * 64, tile.y * 64, 64, 64)
                }
            }
        }
    }

    private fun drawBackground(g: Graphics?) {
        for (file in board.indices) {
            for (rank in board[file].indices) {
                g?.color = when (file % 2) {
                    0 -> when (rank % 2) {
                        0 -> Color.WHITE
                        1 -> Color.BLACK
                        else -> Color.RED
                    }
                    1 -> when (rank % 2) {
                        0 -> Color.BLACK
                        1 -> Color.WHITE
                        else -> Color.RED
                    }
                    else -> Color.RED
                }

                g?.drawRect(file * 64, rank * 64, 64, 64)
            }
        }
    }
}