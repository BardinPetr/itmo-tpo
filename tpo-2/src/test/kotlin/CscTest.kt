import impl.DefaultFunCsc
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import org.junit.jupiter.api.assertThrows
import util.isApproximatelyDivisible
import kotlin.math.PI
import kotlin.math.sin as mockSin

private const val EPS = 1e-4

class CscTest : FunSpec({
    val csc = DefaultFunCsc(::mockSin)

    test("parametric test") {
        forAll(
            row(PI / 2, 1.0),
            row(-PI / 2, -1.0),
            row(PI / 6, 2.0),
            row(-PI / 6, -2.0),
            row(5 * PI / 6, 2.0),
            row(-5 * PI / 6, -2.0)
        ) { x, y -> csc.apply(x) shouldBe y.plusOrMinus(EPS) }
    }

    context("invalid input tests") {
        withData(0.0, PI) {
            assertThrows<IllegalArgumentException> { csc.apply(it) }
        }
    }

    test("asymptote") {
        assertAsymptote(0.0, csc::apply, isRight = true, isUp = true)
        assertAsymptote(0.0, csc::apply, isRight = false, isUp = false)
        assertAsymptote(PI, csc::apply, isRight = true, isUp = false)
        assertAsymptote(PI, csc::apply, isRight = false, isUp = true)
    }

    // tg(x + 2pi) = cos(x) <=> cover any real
    test("property: periodicity") {
        assertPeriodicity(csc::apply, 2 * PI, EPS) {
            Arb.double(-1e3, 1e3)
                .cut(0.0, modulo = PI, radius = 0.1)
        }
    }
})