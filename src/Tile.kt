data class Tile(val file: Int, val rank: Int) {
    val x = file
    val y = rank

    operator fun minus(other: Tile) : Tile {
        return Tile(file - other.file, rank - other.rank)
    }
}