package pl.klemba.aoc.day1

import java.io.File

fun main() {
  File("src/main/kotlin/pl/klemba/aoc/day1/input.txt")
    .readLines()
    .map { extractCalibrationValue(it) }
    .reduce { accumulator, calibrationValue -> accumulator + calibrationValue }
    .let { println(it) }
}

fun extractCalibrationValue(line: String): Int {
  val firstDigit = line.first { it.isDigit() }
  val lastDigit = line.last { it.isDigit() }
  return "$firstDigit$lastDigit".toInt()
}
