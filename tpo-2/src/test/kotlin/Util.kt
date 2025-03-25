import base.DFun
import io.kotest.core.test.TestScope
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll
import kotlin.math.sign

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
    provideRange: () -> Arb<Double>,
) {
    checkAll(provideRange()) { x ->
        f.apply(period + x) shouldBe f.apply(x).plusOrMinus(epsilon)
    }
}
