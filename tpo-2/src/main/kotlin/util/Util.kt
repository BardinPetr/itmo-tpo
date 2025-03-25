package util

import org.jetbrains.letsPlot.Figure
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomLine
import org.jetbrains.letsPlot.ggsize
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.letsPlot

fun linspace(a: Double, b: Double, numPoints: Long): List<Double> =
    ((b - a) / (numPoints - 1))
        .let { step -> (0 until numPoints).map { a + it * step } }

fun plotFunction(xMin: Double, xMax: Double, points: Long, func: (Double) -> Array<Double>): Plot {
    val ptsX = linspace(xMin, xMax, points)
    val ptsY = ptsX.map(func)
    val data = mapOf(
        "x" to ptsX,
        "impl" to ptsY.map { it[0] },
        "true" to ptsY.map { it[1] }
    )
    return letsPlot(data) +
            geomLine(color = "red") { x = "x"; y = "true"; } +
            geomLine { x = "x"; y = "impl"; } +
            ggsize(1000, 600)
}

fun Figure.save(filename: String) = ggsave(this, filename)
