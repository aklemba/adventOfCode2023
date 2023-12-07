package pl.klemba.aoc.day2

import java.io.File

fun main() {
  // ----- PART 1 -----
  File(inputPath)
    .readLines()
    .foldIndexed(0) { index, acc, line -> if (line.meetsRequirements) acc + index + 1 else acc }
    .let { println(it) }

  // ----- PART 2 -----
  File(inputPath)
    .readLines()
    .map { it.maxRgb }
    .sumOf { it.red * it.green * it.blue }
    .let { println(it) }
}

private val String.maxRgb: Rgb
  get() = Rgb(
    red = highestCubeNumber(this, redRegex),
    green = highestCubeNumber(this, greenRegex),
    blue = highestCubeNumber(this, blueRegex)
  )

data class Rgb(var red: Int, var green: Int, var blue: Int)

private val String.meetsRequirements: Boolean
  get() {
    if (highestCubeNumber(this, redRegex) > MAX_RED) return false
    if (highestCubeNumber(this, greenRegex) > MAX_GREEN) return false
    if (highestCubeNumber(this, blueRegex) > MAX_BLUE) return false
    return true
  }

private fun highestCubeNumber(rawLine: String, regex: Regex): Int =
  regex
    .findAll(rawLine)
    .map { it.destructured.component1().toInt() }
    .sortedDescending()
    .firstOrNull()
    ?: 0

private val redRegex = Regex(" (\\d*) red")
private val greenRegex = Regex(" (\\d*) green")
private val blueRegex = Regex(" (\\d*) blue")

private const val inputPath = "src/main/kotlin/pl/klemba/aoc/day2/input.txt"

private const val MAX_RED = 12
private const val MAX_GREEN = 13
private const val MAX_BLUE = 14
