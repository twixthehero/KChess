import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JPanel

class BoardPanel(private val board: Board): JPanel(), MouseListener {
    init {
        board.init()
        preferredSize = Dimension(512, 512)
        addMouseListener(this)
    }

    override fun paintComponent(g: Graphics?) {
        g?.color = Color.GRAY
        g?.clearRect(0, 0, width, height)

        board.draw(g)
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {

    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e == null)
            return

        val tx = e.x / 64
        val ty = Board.NUM_RANKS - e.y / 64 - 1
        board.select(tx, ty)
    }

    override fun mouseExited(e: MouseEvent?) {

    }

    override fun mousePressed(e: MouseEvent?) {

    }
}