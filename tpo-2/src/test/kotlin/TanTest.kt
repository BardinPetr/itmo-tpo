import impl.DefaultFunTan
import io.kotest.core.spec.style.FunSpec
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
import kotlin.math.sin as mockSin
import kotlin.math.tan as tanTrue

private const val EPS = 1e-4

class TanTest : FunSpec({
    val tan = DefaultFunTan(::mockSin, ::mockCos)

    // (-pi/2; pi/2)
    context("parametric tan test") {
        withData(
            0.0,
            PI / 3, PI / 4, PI / 6
                    - PI / 3, -PI / 4, -PI / 6
        ) {
            tan.apply(it) shouldBe tanTrue(it).plusOrMinus(EPS)
        }
    }

    context("invalid input tests") {
        withData(PI / 2, -PI / 2) {
            assertThrows<IllegalArgumentException> { tan.apply(it) }
        }
    }

    test("asymptote") {
        assertAsymptote(PI / 2, tan::apply, isRight = false, isUp = true)
        assertAsymptote(-PI / 2, tan::apply, isRight = true, isUp = false)
    }

    // tg(x + 2pi) = cos(x) <=> cover any real
    test("property: periodicity") {
        assertPeriodicity(tan::apply, 2 * PI, 1e-2) {
            Arb.double(-1e5, 1e5)
                .filter { !isApproximatelyDivisible(it, PI / 2, 1e-1) }
        }
    }
})