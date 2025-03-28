import impl.DefaultFunSec
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
import kotlin.math.cos as mockCos

private const val EPS = 1e-3

class SecTest : FunSpec({
    val sec = DefaultFunSec(::mockCos)

    test("parametric test") {
        forAll(
            row(0.0, 1.0),
            row(PI, -1.0),
            row(PI / 3, 2.0),
            row(-PI / 3, 2.0),
            row(2 * PI / 3, -2.0),
            row(4 * PI / 3, -2.0)
        ) { x, y -> sec.apply(x) shouldBe y.plusOrMinus(EPS) }
    }

    context("invalid input tests") {
        withData(PI / 2, 3 * PI / 2) {
            assertThrows<IllegalArgumentException> { sec.apply(it) }
        }
    }

    test("asymptote") {
        assertAsymptote(PI / 2, sec::apply, isRight = false, isUp = true)
        assertAsymptote(PI / 2, sec::apply, isRight = true, isUp = false)
        assertAsymptote(3 * PI / 2, sec::apply, isRight = false, isUp = false)
        assertAsymptote(3 * PI / 2, sec::apply, isRight = true, isUp = true)
    }

    // tg(x + 2pi) = cos(x) <=> cover any real
    test("property: periodicity") {
        assertPeriodicity(sec::apply, 2 * PI, EPS) {
            Arb.double(-1e3, 1e3)
                .cut(PI / 2, modulo = PI, radius = 0.1)
        }
    }
})