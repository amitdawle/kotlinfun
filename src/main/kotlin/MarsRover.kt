/*
A squad of robotic rovers are to be landed by NASA on a plateau on Mars.

This plateau, which is curiously rectangular, must be navigated by the rovers so that their on board
cameras can get a complete view of the surrounding terrain to send back to Earth. A rover's position is represented
by a combination of an x and y co-ordinates and a letter representing one of the four cardinal compass points.
The plateau is divided up into a grid to simplify navigation.
An example position might be 0, 0, N, which means the rover is in the bottom left corner and facing North.
In order to control a rover, NASA sends a simple string of letters.
The possible letters are 'L', 'R' and 'M'. 'L' and 'R' makes the rover spin 90 degrees left or right respectively,
without moving from its current spot.
'M' means move forward one grid point, and maintain the same heading.
Assume that the square directly North from (x, y) is (x, y+1).
 */

enum class Spin {
    L, R
}

enum class Mode {
    ACTIVE,
    LOST
}

data class Coordinate(val x: Int, val y: Int)

enum class Orientation {
    N {
        override fun move(p: Coordinate): Coordinate = Coordinate(p.x, p.y + 1)

        override fun spin(s: Spin): Orientation {
            return when (s) {
                Spin.R -> E
                Spin.L -> W
            }
        }
    },
    S {
        override fun move(p: Coordinate): Coordinate = Coordinate(p.x, p.y - 1)

        override fun spin(s: Spin): Orientation {
            return when (s) {
                Spin.R -> W
                Spin.L -> E
            }
        }
    },
    E {
        override fun move(p: Coordinate): Coordinate = Coordinate(p.x + 1, p.y)

        override fun spin(s: Spin): Orientation {
            return when (s) {
                Spin.R -> S
                Spin.L -> N
            }
        }
    },
    W {
        override fun move(p: Coordinate): Coordinate = Coordinate(p.x - 1, p.y)

        override fun spin(s: Spin): Orientation {
            return when (s) {
                Spin.R -> N
                Spin.L -> S
            }
        }
    };
    abstract fun spin(s: Spin): Orientation
    abstract fun move(p: Coordinate): Coordinate
}

data class MarsRover(val c: Coordinate, val o: Orientation, val mode: Mode = Mode.ACTIVE)

data class Grid(val length: Int, val height: Int) {

    fun isInside(c: Coordinate): Boolean {
        return when {
            (c.x < 0 || c.x > length) -> false
            (c.y < 0 || c.y > height) -> false
            else -> true
        }
    }
}


fun parse(command: Char): ((MarsRover, Grid) -> MarsRover) {
    return when (command) {
        'M' -> { r, g ->
            val newPosition = r.o.move(r.c)
            if(g.isInside(newPosition)) {
                MarsRover(newPosition, r.o)
            } else {
                r
            }
        }
        'L' -> { r, _ -> MarsRover(r.c, r.o.spin(Spin.L)) }
        'R' -> { r, _ -> MarsRover(r.c, r.o.spin(Spin.R)) }
        else -> throw IllegalArgumentException("Cannot parse $command")
    }
}


fun main(args: Array<String>) {
    val g = Grid(5, 5)
    val rover = moveRover( MarsRover( Coordinate(1, 2), Orientation.N), "LMLMLMLMM", g)
    val rover2 = moveRover(MarsRover(Coordinate(3, 3), Orientation.E),"MMRMMRMRRM", g)
    println(rover)
    println(rover2)
}

fun moveRover(rover : MarsRover,  commands: String, g: Grid): MarsRover {
    return commands.map { cmd -> parse(cmd) }
        .fold(rover) { r, f ->  f(r, g)}

}



