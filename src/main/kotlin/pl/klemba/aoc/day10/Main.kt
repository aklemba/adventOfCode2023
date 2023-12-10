package pl.klemba.aoc.day10

import java.io.File

fun main() {
  // ----- PART 1 -----
  val pipeField = File(inputPath)
    .readLines()

  // Find starting point
  val startingIndex: Coordinates = pipeField.findStartingIndex()
  println("Starting index: ${startingIndex}")

  // Find one of the pipes that create the loop with starting point
  // Check top first as it is clearly visible that is starts from top
  val topCoordinates = startingIndex.copy(y = startingIndex.y - 1)
  println("Top coordinates: ${topCoordinates}")

  val char: Char = pipeField[topCoordinates.y][topCoordinates.x]
  val actor = char.mapToActor()
  // Go through the pipes
  var nextStep = NextStep(Direction.N, actor as Actor.Pipe, topCoordinates)
  var pipeCount = 1

  while(nextStep.pipe !is Actor.StartingPoint) {
    pipeCount++
    nextStep = pipeField.followThePipeAndCount(nextStep.originDirection, nextStep.pipe as Actor.Pipe, nextStep.pipeCoordinates)
  }
  println(pipeCount/2)
}

private fun List<String>.followThePipeAndCount(
  originDirection: Direction,
  pipe: Actor.Pipe,
  pipeCoordinates: Coordinates,
): NextStep {
  val nextDirection = when (originDirection.oppositeDirection) {
    pipe.direction1 -> pipe.direction2
    pipe.direction2 -> pipe.direction1
    else -> throw IllegalStateException("One direction has to be the same as origin")
  }
  val nextCoordinates = calculateNextCoordinates(nextDirection, pipeCoordinates)
  println("Symbol: ${this[nextCoordinates.y][nextCoordinates.x]}")
  val actor = this[nextCoordinates.y][nextCoordinates.x].mapToActor()

  println("Next direction: ${nextDirection}, next coordinates: ${nextCoordinates}")

  return NextStep(nextDirection, actor, nextCoordinates)
}

class NextStep(val originDirection: Direction,
               val pipe: Actor,
               val pipeCoordinates: Coordinates)

private fun calculateNextCoordinates(nextDirection: Direction, pipeCoordinates: Coordinates): Coordinates {
  val coordinatesChange = when (nextDirection) {
    Direction.N -> Coordinates(0, -1)
    Direction.W -> Coordinates(-1, 0)
    Direction.S -> Coordinates(0, 1)
    Direction.E -> Coordinates(1, 0)
  }
  return pipeCoordinates.copyWithCoordinatesChange(coordinatesChange)
}

private fun Char.mapToActor(): Actor {
  if (this == 'S') return Actor.StartingPoint
  return Actor.Pipe
    .getByChar(this)
    ?: throw IllegalStateException("It has to be a pipe!")
}

private fun List<String>.findStartingIndex(): Coordinates {
  map { startingPointRegex.find(it) }
    .forEachIndexed { lineIndex, matchResult ->
      if (matchResult != null) return Coordinates(
        matchResult.range.first,
        lineIndex
      )
    }
  throw IllegalStateException("Missing starting point!")
}

data class Coordinates(val x: Int, val y: Int) {
  fun copyWithCoordinatesChange(coordinatesChange: Coordinates) =
    copy(x = this.x + coordinatesChange.x, y = this.y + coordinatesChange.y)
}

private val startingPointRegex = Regex("S")
private const val inputPath = "src/main/kotlin/pl/klemba/aoc/day10/input.txt"

enum class Direction {
  N,
  W,
  S,
  E;

  val oppositeDirection: Direction
    get() = when (this) {
      N -> S
      W -> E
      E -> W
      S -> N
    }
}

sealed class Actor {
  object StartingPoint : Actor()
  sealed class Pipe(val direction1: Direction, val direction2: Direction, val charEquivalent: Char) : Actor() {
    object N_S : Pipe(Direction.N, Direction.S, '|')
    object N_E : Pipe(Direction.N, Direction.E, 'L')
    object W_E : Pipe(Direction.W, Direction.E, '-')
    object W_N : Pipe(Direction.W, Direction.N, 'J')
    object S_W : Pipe(Direction.S, Direction.W, '7')
    object E_S : Pipe(Direction.E, Direction.S, 'F')

    companion object {
      fun getByChar(char: Char): Pipe? = when (char) {
        N_S.charEquivalent -> N_S
        N_E.charEquivalent -> N_E
        W_E.charEquivalent -> W_E
        W_N.charEquivalent -> W_N
        S_W.charEquivalent -> S_W
        E_S.charEquivalent -> E_S
        else -> null
      }
    }
  }
}