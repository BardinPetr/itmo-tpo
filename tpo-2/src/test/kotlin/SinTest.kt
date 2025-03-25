import impl.DefaultFunSin
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlin.math.PI
import kotlin.math.cos as mockCos
import kotlin.math.sin as trueSin

private const val EPS = 1e-4

class SinTest : FunSpec({
    val sin = DefaultFunSin(::mockCos)

    context("parametric sin test") {
        withData(0.0, PI / 6, PI / 4, PI / 3, PI / 2) {
            sin.apply(it) shouldBe trueSin(it).plusOrMinus(EPS)
        }
    }

    test("property: sin(pi - x) = sin(x)") {
        checkAll(Arb.double(-10.0, 10.0)) { x ->
            sin.apply(PI - x) shouldBe sin.apply(x).plusOrMinus(EPS)
        }
    }

    test("property: sin(pi + x) = -sin(x)") {
        checkAll(Arb.double(-10.0, 10.0)) { x ->
            sin.apply(PI + x) shouldBe sin.apply(x).times(-1).plusOrMinus(EPS)
        }
    }

    test("property: periodicity") {
        assertPeriodicity(sin::apply, 2 * PI, EPS) { Arb.double(-10.0, 10.0) }
    }
})