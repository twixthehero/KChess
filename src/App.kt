import java.awt.BorderLayout
import javax.swing.*

class App: JFrame("Chess"), Runnable {
    companion object {
        lateinit var instance: App
    }

    private val board = Board()
    private val panel = BoardPanel(board)
    var running = false

    init {
        instance = this

        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        jMenuBar = object: JMenuBar() {
            init {
                val file = JMenu("File")
                val ng = JMenuItem("New Game")
                ng.addActionListener({ board.init() })
                val x = JMenuItem("Exit")
                x.addActionListener({ running = false })
                file.add(ng)
                file.add(x)

                val about = JMenu("About")
                val a = JMenuItem("About")
                a.addActionListener({ JOptionPane.showMessageDialog(this@App, "") })
                about.add(a)

                val view = JMenu("View")
                val s = JMenuItem("Switch Sides")
                s.addActionListener({ board.switchSides() })
                view.add(s)

                add(file)
                add(view)
                add(about)
            }
        }

        layout = BorderLayout()
        add(panel, BorderLayout.CENTER)
        pack()

        isVisible = true
    }

    override fun run() {
        val period = 1000000000 / 60
        running = true
        var start: Long
        while (running) {
            start = System.nanoTime()

            panel.invalidate()
            panel.repaint()

            val diff = System.nanoTime() - start
            var leftover = (period - diff) / 1000000

            if (leftover < 10)
                leftover = 10

            Thread.sleep(leftover)
        }

        dispose()
    }
}

fun main(args: Array<String>) {
    App().run()
}