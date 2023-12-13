package pl.klemba.aoc.day5

import java.io.File

fun main() {
  // ----- PART 1 -----
  val almanac = File(inputPath)
    .readLines()

  val seeds: List<Long> = getSeeds(almanac)
  println("Seeds: $seeds")

  val mappings = getMappings(almanac)

  mappings
    .forEach { println(it) }
  seeds
    .map { seed -> seed.mapToLocation(mappings) }
    .let {
      println("Resultlist: $it")
      println(it.min())
    }

  // ----- PART 2 -----
  val almanac2 = File(inputPath)
    .readLines()

  val seedRanges: List<LongRange> = getSeedRangesPart2(almanac2)

  val mappings2 = getMappings(almanac2)

  seedRanges
    .fold(Long.MAX_VALUE) { acc, longRange -> findMinLocationOf(longRange, mappings2).let { location -> if (location < acc) location else acc } }
    .let { println(it) }
}

fun getMappings(almanac: List<String>): ArrayList<ArrayList<MappingPattern>> {
  val mappings = ArrayList<ArrayList<MappingPattern>>()
  return almanac
    .fold(mappings) { acc, line -> addToMappingsList(line, acc) }
}

private fun String.hasThreeNumbers() = getMappingLine() != null

private fun String.getMappingLine() = mappingLineRegex.matchEntire(this)

fun addToMappingsList(line: String, acc: ArrayList<ArrayList<MappingPattern>>): java.util.ArrayList<java.util.ArrayList<MappingPattern>> {
  when {
    line.endsWith("map:") -> {
      acc.add(ArrayList())
    }
    line.hasThreeNumbers() -> {
      acc.last().add(getMappingPattern(line))
    }
  }
  return acc
}

fun getMappingPattern(line: String) =
  line.getMappingLine()
    ?.destructured
    ?.let { (destinationValue, sourceValue, rangeValue ) -> mapToMappingPattern(destinationValue.toLong(), sourceValue.toLong(), rangeValue.toLong()) }
    ?: throw IllegalStateException("Mapping pattern not found! Wierd since it has been checked before!")

fun mapToMappingPattern(destinationValue: Long, sourceValue: Long, rangeValue: Long) =
  MappingPattern(LongRange(sourceValue, sourceValue + rangeValue), destinationValue - sourceValue)

fun getSeeds(almanac: List<String>): List<Long> =
  numberRegex.findAll(almanac[0])
    .map { it.destructured.component1().toLong() }
    .toList()

fun getSeedRangesPart2(almanac: List<String>): List<LongRange> =
  getSeeds(almanac)
    .chunked(2)
    .map { LongRange(it[0], it[0] + it[1]) }

private fun Long.mapToLocation(mappings: java.util.ArrayList<java.util.ArrayList<MappingPattern>>) =
  mappings
    .fold(this) { acc, mappingPatterns ->
      mappingPatterns.find { it.sourceRange.contains(acc) }
        ?.let { pattern -> pattern.valueShift + acc }
        ?: acc
    }

fun findMinLocationOf(longRange: LongRange, almanac2: ArrayList<ArrayList<MappingPattern>>) =
  longRange
    .fold(Long.MAX_VALUE) { acc, seed -> seed.mapToLocation(almanac2).let { location -> if (location < acc) location else acc } }

/**
 * @param sourceRange range of IDs which can be mapped by this pattern
 * @param valueShift parameter indicating needed value shift (addition of the value to source ID) to achieve the mapping result
 */
data class MappingPattern(val sourceRange: LongRange, val valueShift: Long)

private val numberRegex = Regex(""" (\d+)""")

private val mappingLineRegex = Regex("""(\d+) (\d+) (\d+)""")
private const val inputPath = "src/main/kotlin/pl/klemba/aoc/day5/input.txt"