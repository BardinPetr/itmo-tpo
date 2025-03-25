import impl.DefaultFunCos
import impl.DefaultFunFact
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlin.math.PI
import kotlin.math.cos as cosTrue

private const val EPS = 1e-10

class CosTest : FunSpec({
    val cos = DefaultFunCos(DefaultFunFact())

    // check basic 0->pi/2 part
    context("parametric cos test") {
        withData(0.0, PI / 6, PI / 4, PI / 3, PI / 2) {
            cos.apply(it) shouldBe cosTrue(it).plusOrMinus(EPS)
        }
    }

    // cos(pi - x) = -cos(x) <=> cover pi/2->pi
    test("property: cos(pi - x) = -cos(x)") {
        checkAll(Arb.double(-10.0, 10.0)) { x ->
            cos.apply(PI - x) shouldBe cos.apply(x).times(-1).plusOrMinus(EPS)
        }
    }

    // cos(pi + x) = -cos(x) <=> cover pi->2pi
    test("property: cos(pi + x) = -cos(x)") {
        checkAll(Arb.double(-10.0, 10.0)) { x ->
            cos.apply(PI + x) shouldBe cos.apply(x).times(-1).plusOrMinus(EPS)
        }
    }

    // cos(x + 2pi) = cos(x) <=> cover any real
    test("property: periodicity") {
        assertPeriodicity(cos::apply, 2 * PI, EPS) { Arb.double(-10.0, 10.0) }
    }
})
