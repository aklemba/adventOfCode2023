package pl.klemba.aoc.day4

import java.io.File
import kotlin.math.pow

fun main() {
  // ----- PART 1 -----
  File(inputPath)
    .readLines()
    .sumOf { card -> getNumberOfPointsForCard(card) }
    .let { println(it) }

  // ----- PART 2 -----
  val cardList = File(inputPath)
    .readLines()

  val amountOfScratchCards = IntArray(cardList.size) { 1 }

  cardList
    .foldIndexed(amountOfScratchCards) { index, acc, card -> calculateNumberOfCards(card, acc, index) }
    .sum()
    .let { println(it) }
}

fun getNumberOfPointsForCard(card: String): Int {
  val amountOfMatchingNumbers = getNumberOfMatchingNumbers(card)
  return if (amountOfMatchingNumbers == 1) 1 else 2.0.pow(amountOfMatchingNumbers - 1).toInt()
}

fun getNumberOfMatchingNumbers(card: String): Int {
  val winningNumbers = getWinningNumbers(card)
  val numbersYouHave = getNumbersYouHave(card)
  return winningNumbers.intersect(numbersYouHave.toSet()).size
}

fun getWinningNumbers(card: String): List<Int> =
  winningNumbersRegex.find(card)
    ?.value
    ?.removeRange(0, 2)
    ?.trim()
    ?.split("  ", " ")
    ?.map { it.toInt() }
    ?: throw IllegalStateException("No winning numbers found!")

fun getNumbersYouHave(card: String): List<Int> =
  numbersYouHaveRegex.find(card)
    ?.value
    ?.removeRange(0, 2)
    ?.trim()
    ?.split("  ", " ")
    ?.map { it.toInt() }
    ?: throw IllegalStateException("No your numbers found!")

fun calculateNumberOfCards(card: String, acc: IntArray, index: Int): IntArray {
  val numberOfPoints = getNumberOfMatchingNumbers(card)
  val numberOfCards = acc[index]
  repeat(numberOfPoints) { i ->
    acc[index + i + 1] += numberOfCards
  }
  return acc
}


private val winningNumbersRegex = Regex(""": +\d+(?: +\d+)+""")
private val numbersYouHaveRegex = Regex("""\| +\d+(?: +\d+)+""")


private const val inputPath = "src/main/kotlin/pl/klemba/aoc/day4/input.txt"