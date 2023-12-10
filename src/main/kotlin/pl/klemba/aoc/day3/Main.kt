package pl.klemba.aoc.day3

import java.io.File

fun main() {
  // ----- PART 1 -----
  File(inputPath)
    .readLines()
    .let { schematic ->
      schematic.mapIndexed { lineIndex, line ->
        numberRegex.findAll(line)
          .filter { schematic.checkIfAdjacentToASymbol(it.range, lineIndex) }
          .map { it.value.toInt() }
          .sum()
      }.sum()
        .let { println(it) }
    }


}

private fun List<String>.checkIfAdjacentToASymbol(numberIndices: IntRange, lineIndex: Int): Boolean {
  // check top and bottom
  if (lineIndex != 0) {
    if (this[lineIndex - 1].checkIfSymbolsExistForRange(numberIndices, this[0].lastIndex)) return true
  }
  if (lineIndex != this.lastIndex) {
    if (this[lineIndex + 1].checkIfSymbolsExistForRange(numberIndices, this[0].lastIndex)) return true
  }

  // check sides
  if (numberIndices.first != 0) {
    if(this[lineIndex][numberIndices.first - 1].isASymbol()) return true
  }
  if (numberIndices.last != this[lineIndex].lastIndex) {
    if(this[lineIndex][numberIndices.last + 1].isASymbol()) return true
  }

  return false
}

private fun String.checkIfSymbolsExistForRange(indexRange: IntRange, lineLastIndex: Int): Boolean {
  val rangeToCheck: IntRange = when {
    indexRange.first == 0 -> IntRange(0, indexRange.last + 1)
    indexRange.last == lineLastIndex -> IntRange(indexRange.first -1 , indexRange.last)
    else -> IntRange(indexRange.first - 1, indexRange.last + 1)
  }
  return substring(rangeToCheck).any { it.isASymbol() }
}

private fun Char.isASymbol() = this.isDigit().not() && this != '.'

private const val inputPath = "src/main/kotlin/pl/klemba/aoc/day3/input.txt"

private val numberRegex = Regex("\\d+")