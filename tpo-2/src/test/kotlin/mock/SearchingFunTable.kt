package mock

import base.DFun
import base.DFun2
import java.io.FileOutputStream
import java.nio.file.Path
import kotlin.io.path.pathString
import kotlin.math.abs

const val FAIL_EPS = 1e-3

open class SearchingFunTable(
    protected val filename: String,
    protected open val logModeDirectory: String? = null
) {
    protected val table: MutableMap<List<Double>, Double> =
        readCSV(filename)
            .map { it.map(String::toDouble) }
            .associate { it.take(it.size - 1) to it.last() }
            .toMutableMap()

    protected fun search(vararg x: Double): Double? =
        table
            .minByOrNull { (tx, _) ->
                tx
                    .zip(x.toTypedArray())
                    .map { (i, j) -> abs(i - j) }
                    .reduce { acc, i -> acc + i }
                    .let { if (it > FAIL_EPS) Double.MAX_VALUE else it }
            }
            ?.value

    fun find(vararg x: Double): Double? {
        val row = search(*x)
        println("$filename ${x.get(0)} -> $row")
        return logModeDirectory?.let { dir ->
            val file = Path.of(dir, filename.split('/').last()).pathString
            FileOutputStream(file, true)
                .bufferedWriter()
                .use { wr -> wr.appendLine(x.joinToString(",")) }
            0.0
        } ?: row
    }
}

class DFun1SearchingFunTable(filename: String, logModeDirectory: String? = null) :
    SearchingFunTable(filename, logModeDirectory), DFun {
    override fun apply(x: Double): Double = find(x)
        ?: throw IllegalArgumentException("Value not found in table: $x")
}

class DFun2SearchingFunTable(filename: String, logModeDirectory: String? = null) :
    SearchingFunTable(filename, logModeDirectory), DFun2 {
    override fun apply(a: Double, b: Double): Double = find(a, b)
        ?: throw IllegalArgumentException("Value not found in table: $a $b")
}