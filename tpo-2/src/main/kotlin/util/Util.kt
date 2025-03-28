package util

import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.ggsize
import org.jetbrains.letsPlot.letsPlot
import java.io.FileWriter
import kotlin.math.abs
import kotlin.math.truncate

fun linspace(a: Double, b: Double, numPoints: Long): List<Double> =
    ((b - a) / (numPoints - 1))
        .let { step -> (0 until numPoints).map { a + it * step } }

fun plotFunction(
    xMin: Double,
    xMax: Double,
    points: Long,
    clip: Double = 1e2,
    name: String,
    func: (Double) -> Array<Double>,
) {
    val ptsX = linspace(xMin, xMax, points)
    val ptsY = ptsX.map { func.runCatching { invoke(it) }.getOrNull() }
    val data = mapOf(
        "x" to ptsX,
        "impl" to ptsY.map { it?.get(0)?.coerceIn(-clip, clip) },
        "true" to ptsY.map { it?.get(1)?.coerceIn(-clip, clip) }
    )

    FileWriter("$name.csv")
        .use { wr ->
            ptsX
                .zip(ptsY)
                .forEach { (x, y) -> wr.write("%.3f,%.3f\n".format(x, y?.get(0))) }
        }

    val plot = letsPlot(data) +
            geomLine(color = "red") { x = "x"; y = "true"; } +
            geomLine { x = "x"; y = "impl"; } +
            ggsize(1000, 600)
    plot.save("$name.png")
}

fun Figure.save(filename: String) = ggsave(this, filename)

fun fact(x: Long) = if (x < 2) 1.0 else (2..x).fold(1.0, Double::times)

fun isNearZero(x: Double, delta: Double = 1e-10) = abs(x) < delta

fun isApproximatelyDivisible(x: Double, divisor: Double, delta: Double = 1e-50) =
    abs(x)
        .div(divisor)
        .let { isNearZero(it - truncate(it), delta) }

