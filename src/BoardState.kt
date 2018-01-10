import java.awt.Graphics
import java.lang.Integer.signum
import java.lang.Math.abs
import javax.swing.JOptionPane

class BoardState(board: Array<Array<Piece?>>, private var turn: ETeam, capWhite: ArrayList<Piece>, capBlack: ArrayList<Piece>) {
    val board = Array(8, { arrayOfNulls<Piece>(8) })
    private val capBlack = ArrayList<Piece>(capBlack)
    private val capWhite = ArrayList<Piece>(capWhite)
    val black = ArrayList<Piece>()
    val white = ArrayList<Piece>()
    private var blackKing : Piece? = null
    private var whiteKing : Piece? = null

    init {
        for (file in board.indices) {
            for (rank in board[file].indices) {
                if (board[file][rank] != null){
                    val piece = board[file][rank]!!

                    this.board[file][rank] = when (piece.javaClass.name) {
                        "Bishop" -> Bishop(piece as Bishop)
                        "Pawn" -> Pawn(piece as Pawn)
                        "King" -> King(piece as King)
                        "Queen" -> Queen(piece as Queen)
                        "Rook" -> Rook(piece as Rook)
                        "Knight" -> Knight(piece as Knight)
                        else -> null
                    }

                    when (this.board[file][rank]!!.team) {
                        ETeam.Black -> {
                            black.add(this.board[file][rank]!!)

                            if (this.board[file][rank] is King)
                                blackKing = this.board[file][rank]
                        }
                        ETeam.White -> {
                            white.add(this.board[file][rank]!!)

                            if (this.board[file][rank] is King)
                                whiteKing = this.board[file][rank]
                        }
                    }
                }
            }
        }
    }

    fun isOccupied(x: Int, y: Int) : Boolean {
        return board[x][y] != null
    }

    fun isOccupied(x: Int, y: Int, team: ETeam) : Boolean {
        return board[x][y] != null && board[x][y]?.team == team
    }

    fun get(x: Int, y: Int) : Piece? {
        return board[x][y]
    }

    fun playMove(f1: Int, r1: Int, f2: Int, r2: Int) : Piece? {
        val cap = move(f1, r1, f2, r2, false)

        turn = when (turn) {
            ETeam.White -> ETeam.Black
            ETeam.Black -> ETeam.White
        }

        return cap
    }

    /*
    returns the captured piece of a move, else null
     */
    private fun move(f1: Int, r1: Int, f2: Int, r2: Int, simulate: Boolean) : Piece? {
        if (f1 < 0 || f1 >= Board.NUM_FILES ||
                f2 < 0 || f2 >= Board.NUM_FILES ||
                r1 < 0 || r1 >= Board.NUM_RANKS ||
                r2 < 0 || r2 >= Board.NUM_RANKS)
            return null

        if (board[f1][r1] == null)
            return null

        var promoted = false
        var promPiece: Piece? = null
        if (board[f1][r1] is King) {
            //castle
            var dif = f2 - f1
            if (abs(dif) == 2) {
                val rookFile = when (dif > 0) {
                    true -> 7
                    false -> 0
                }
                val rookRank = when (turn) {
                    ETeam.Black -> 7
                    ETeam.White -> 0
                }

                //move rook
                dif = signum(dif)
                val rook = board[rookFile][rookRank]
                board[f1 + dif][rookRank] = rook
                board[f1 + dif][rookRank]?.tile = Tile(f1 + dif, rookRank)
                board[f1 + dif][rookRank]?.onMove()
                board[rookFile][rookRank] = null
            }
        } else if (!simulate && board[f1][r1] is Pawn) {
            //promotion - only when not simulating valid moves
            val destRank = when (board[f1][r1]!!.team) {
                ETeam.White -> 7
                ETeam.Black -> 0
            }

            if (r2 == destRank) {
                promoted = true

                val choices = arrayOf("Knight", "Bishop", "Rook", "Queen")
                val response = JOptionPane.showOptionDialog(App.instance, "Promote pawn to:", "Promote",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choices, choices[3])

                promPiece = when (response) {
                    0 -> Knight(turn)
                    1 -> Bishop(turn)
                    2 -> Rook(turn)
                    3 -> Queen(turn)
                    else -> null
                }
            }
        }

        val cap = board[f2][r2]

        if (promoted)
            board[f2][r2] = promPiece
        else
            board[f2][r2] = board[f1][r1]

        board[f2][r2]?.tile = Tile(f2, r2)
        board[f2][r2]?.onMove()
        board[f1][r1] = null

        if (cap != null)
            when (cap.team) {
                ETeam.Black -> {
                    black.remove(cap)
                    capBlack.add(cap)
                }
                ETeam.White -> {
                    white.remove(cap)
                    capWhite.add(cap)
                }
            }

        return cap
    }

    fun simMove(f1: Int, r1: Int, f2: Int, r2: Int) : BoardState {
        val sim = clone()
        sim.move(f1, r1, f2, r2, true)
        return sim
    }

    fun isInCheck() : Boolean {
        return inCheck(turn)
    }

    private fun inCheck(team: ETeam) : Boolean {
        val king = when (team) {
            ETeam.White -> whiteKing
            ETeam.Black -> blackKing
        }
        val pieces = when (team) {
            ETeam.White -> black
            ETeam.Black -> white
        }

        var valid : List<Tile>
        for (piece in pieces) {
            valid = piece.getValidMoves(this)

            if (valid.contains(king?.tile))
                return true
        }

        return false
    }

    private fun clone() : BoardState {
        return BoardState(board, turn, capWhite, capBlack)
    }

    fun draw(g: Graphics?, viewFromWhite: Boolean) {
        for (file in board.indices) {
            board[file].indices
                    .filter { board[file][it] != null }
                    .forEach {
                        if (viewFromWhite)
                            board[file][it]!!.draw(g, file * 64, (7 - it) * 64)
                        else
                            board[file][it]!!.draw(g, (7 - file) * 64, it * 64)
                    }
        }
    }
}