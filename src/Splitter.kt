import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/*
Used to split the initial chess pieces image
 */
fun main(args: Array<String>) {
    val b = File("black")
    val w = File("white")
    b.mkdir()
    w.mkdir()

    val pieces: BufferedImage = ImageIO.read(File("pieces.png"))

    println("${pieces.width},${pieces.height}")

    val half = pieces.height / 2
    var top = 0
    var bot = half

    for (y in 0 until pieces.height) {
        for (x in 0 until pieces.width - 1) {
            if (pieces.getRGB(x, y) != 0) {
                top = y
                break
            }
        }

        if (top != 0)
            break
    }

    for (y in half..0) {
        for (x in 0..(pieces.width - 1)) {
            if (pieces.getRGB(x, y) != 0) {
                bot = y
                break
            }
        }

        if (bot != half)
            break
    }

    println("black: $top,$bot")
    val h = bot - top
    val blackPieces = pieces.getSubimage(0, top, pieces.width, h)
    ImageIO.write(blackPieces, "png", File(b, "blackPieces.png"))

    var piece : BufferedImage
    for (i in 0..5) {
        piece = blackPieces.getSubimage(i * 160, 0, 160, blackPieces.height)
        ImageIO.write(piece, "png", File(b, "${getName(i)}.png"))
    }

    top = half

    for (y in half..pieces.height) {
        for (x in 0..(pieces.width - 1)) {
            if (pieces.getRGB(x, y) != 0) {
                top = y
                break
            }
        }

        if (top != half)
            break
    }

    bot = top + h

    println("white: $top,$bot")
    val whitePieces = pieces.getSubimage(0, top, pieces.width, bot - top)
    ImageIO.write(whitePieces, "png", File(w, "whitePieces.png"))

    for (i in 0..5) {
        piece = whitePieces.getSubimage(i * 160, 0, 160, whitePieces.height)
        ImageIO.write(piece, "png", File(w, "${getName(i)}.png"))
    }
}

fun getName(i: Int): String {
    return when (i) {
        0 -> "king"
        1 -> "queen"
        2 -> "rook"
        3 -> "bishop"
        4 -> "knight"
        5 -> "pawn"
        else -> "unk"
    }
}