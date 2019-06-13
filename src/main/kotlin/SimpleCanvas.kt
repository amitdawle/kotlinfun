interface Canvas {
    fun draw(p: Point)
    fun render()
}

data class Point(val x: Int, val y: Int)

sealed class Shape
class Line(val from: Point, val to: Point) : Shape()
class Rectangle(val top: Point, val width: Int, val height: Int) : Shape()

// Mutable
class ConsoleCanvas(private val width: Int, height: Int) : Canvas {

    val buffer: Array<Array<Char>> = Array(height) { Array(width) { ' ' } }

    override fun render() {
        (0..width + 1).forEach{print('*')}
        println()
        buffer.forEach { line ->
            print('|')
            line.forEach { c -> print(c) }
            print('|')
            println()
        }
        (0..width + 1).forEach{print('*')}
    }

    override fun draw(p: Point) {
        buffer[p.y][p.x] = '*'
    }
}

fun draw(s: Shape, c: Canvas) {
    when (s) {
        is Line -> {
            if (s.from.x == s.to.x) {
                (s.from.y..s.to.y).forEach { y -> c.draw(Point(s.from.x, y)) }
            } else {
                (s.from.x..s.to.x).forEach { x -> c.draw(Point(x, s.from.y)) }
            }
        }
        is Rectangle -> {
            draw(Line(Point(s.top.x, s.top.y), Point(s.top.x + s.width, s.top.y)), c)
            draw(Line(Point(s.top.x, s.top.y), Point(s.top.x, s.top.y + s.height)), c)
            draw(Line(Point(s.top.x + s.width, s.top.y), Point(s.top.x + s.width, s.top.y + s.height)), c)
            draw(Line(Point(s.top.x, s.top.y + s.height), Point(s.top.x + s.width, s.top.y + s.height)), c)

        }
    }
}

fun main(args: Array<String>) {
    val r = Rectangle(Point(2, 2), 5, 2)
    val l1 = Line(Point(2, 5), Point(2, 7))
    val l2 = Line(Point(7, 5), Point(7, 7))

    val l3 = Line(Point(12, 2), Point(12, 7))
    val l4 = Line(Point(12, 2), Point(18, 2))
    val l5 = Line(Point(15, 2), Point(15, 4))
    val l6 = Line(Point(18, 2), Point(18, 7))

    val l7 = Line(Point(23, 2), Point(23, 7))

    val l8 = Line(Point(28, 2), Point(34, 2))
    val l9 = Line(Point(31, 2), Point(31, 7))

    val c = ConsoleCanvas(40, 10)
    draw(r, c)
    draw(l1, c)
    draw(l2, c)
    draw(l3, c)
    draw(l4, c)
    draw(l5, c)
    draw(l6, c)
    draw(l7, c)
    draw(l8, c)
    draw(l9, c)

    c.render()
}
