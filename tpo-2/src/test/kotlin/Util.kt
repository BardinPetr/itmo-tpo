import base.DFun
import io.kotest.core.test.TestScope
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.checkAll
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import mock.DoubleFunTable
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.sign

fun logMock(a: Double, b: Double) = log(b, a)

fun TestScope.assertAsymptote(
    x: Double,
    f: DFun,
    isRight: Boolean,
    isUp: Boolean,
    shift: Double = 0.1,
    count: Int = 10
) {
    generateSequence(shift) { it / 2 }
        .take(count)
        .map { x + if (isRight) it else -it }
        .map(f::apply)
        .zipWithNext { a, b -> sign(b - a).toInt() }
        .all { it == (if (isUp) 1 else -1) } shouldBe true
}

suspend fun TestScope.assertPeriodicity(
    f: DFun,
    period: Double,
    epsilon: Double,
    clipX: ClosedFloatingPointRange<Double> = (Double.NEGATIVE_INFINITY..Double.POSITIVE_INFINITY),
    provideRange: () -> Arb<Double>,
) {
    checkAll(provideRange()) { x ->
        val nxt = x + period
        if (clipX.contains(nxt) && clipX.contains(x))
            f.apply(nxt) shouldBe f.apply(x).plusOrMinus(epsilon)
    }
}

inline fun <reified T : DFun> tableMock(table: DoubleFunTable): T =
    mockk<T>()
        .apply {
            val xSlot = slot<Double>()
            every { apply(capture(xSlot)) } answers { table.find(xSlot.captured) }
        }

fun Arb<Double>.cut(center: Double, radius: Double, modulo: Double? = null) =
    filterNot {
        abs((modulo?.let { m -> it % m } ?: it) - center) <= radius
    }
