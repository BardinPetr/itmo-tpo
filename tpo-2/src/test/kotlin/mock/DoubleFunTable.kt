package mock

import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.streams.asSequence

class DoubleFunTable(
    filename: String,
    private val xPrecision: Int = 4
) {
    private val table: Map<Long, Double> =
        readCSV(filename)
            .associate { it[0].toLong() to it[1].toDouble() }

    fun find(x: Double) =
        x
            .times(10.0.pow(xPrecision))
            .roundToLong()
            .let(table::get)
            ?: throw IllegalArgumentException("Value not found in table: $x")
}

fun readCSV(filename: String) =
    object {}
        .javaClass
        .classLoader
        .getResourceAsStream(filename)
        ?.bufferedReader()
        ?.use { rd ->
            rd
                .lines()
                .asSequence()
                .map { it.split(",") }
                .toList()
        }
        ?: throw IllegalArgumentException("File invalid: $filename")
