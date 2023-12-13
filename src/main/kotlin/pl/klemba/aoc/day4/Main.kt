package pl.klemba.aoc.day4

import java.io.File
import kotlin.math.pow

fun main() {
  // ----- PART 1 -----
  File(inputPath)
    .readLines()
    .sumOf { card -> getNumberOfPointsForCard(card) }
    .let { println(it) }
}

fun getNumberOfPointsForCard(card: String): Int {
  val winningNumbers = getWinningNumbers(card)
  val numbersYouHave = getNumbersYouHave(card)
  val amountOfMatchingNumbers = winningNumbers.intersect(numbersYouHave.toSet()).size
  return if(amountOfMatchingNumbers == 1) 1 else 2.0.pow(amountOfMatchingNumbers - 1).toInt()
}

fun getWinningNumbers(card: String): List<Int> =
  winningNumbersRegex.find(card)
    ?.value
    ?.removeRange(0,2)
    ?.trim()
    ?.split("  ", " ")
    ?.map { it.toInt() }
    ?: throw IllegalStateException("No winning numbers found!")

fun getNumbersYouHave(card: String): List<Int> =
  numbersYouHaveRegex.find(card)
    ?.value
    ?.removeRange(0,2)
    ?.trim()
    ?.split("  ", " ")
    ?.map { it.toInt() }
    ?: throw IllegalStateException("No your numbers found!")


private val winningNumbersRegex = Regex(""": +\d+(?: +\d+)+""")
private val numbersYouHaveRegex = Regex("""\| +\d+(?: +\d+)+""")


private const val inputPath = "src/main/kotlin/pl/klemba/aoc/day4/input.txt"