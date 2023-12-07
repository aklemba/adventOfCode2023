package pl.klemba.aoc.day1

import java.io.File

fun main() {
  // ----- PART 1 -----
  File(inputPath)
    .readLines()
    .map { extractCalibrationValue(it) }
    .reduce { accumulator, calibrationValue -> accumulator + calibrationValue }
    .let { println(it) }

  // ----- PART 2 -----
  File(inputPath)
    .readLines()
    .map { rawLine -> formatToDigits(rawLine) }
    .map { extractCalibrationValue(it) }
    .reduce { accumulator, calibrationValue -> accumulator + calibrationValue }
    .let { println(it) }
}

fun formatToDigits(rawLine: String): String {
  var calibrationValue = ""

  var rawLineLeftToCheck = rawLine
  while (rawLineLeftToCheck.isNotEmpty()) {
    rawLineLeftToCheck.getDigitAtStart()?.let { calibrationValue += it }
    rawLineLeftToCheck = rawLineLeftToCheck.substring(1)
  }
  return calibrationValue
}

private fun String.getDigitAtStart(): Int? {
  println("length: " + this.length)
  if (first().isDigit()) return first().digitToInt()
  digitNames
    .forEach { mapEntry -> if (startsWith(mapEntry.key)) return mapEntry.value }
  return null
}

fun extractCalibrationValue(line: String): Int {
  val firstDigit = line.first { it.isDigit() }
  val lastDigit = line.last { it.isDigit() }
  return "$firstDigit$lastDigit".toInt()
}

fun transformDigitNames(line: String) =
  digitNames.keys
    .fold(line) { accumulator, digitName -> accumulator.transformDigitName(digitName) }

private fun String.transformDigitName(digitName: String): String =
  this.replace(digitName, digitNames[digitName].toString())

private const val inputPath = "src/main/kotlin/pl/klemba/aoc/day1/input.txt"

private val digitNames: Map<String, Int> = listOf(
  "one",
  "two",
  "three",
  "four",
  "five",
  "six",
  "seven",
  "eight",
  "nine"
).mapIndexed { index, digitName -> digitName to index + 1 }
  .toMap()