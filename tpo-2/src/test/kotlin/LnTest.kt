import impl.DefaultFunLn
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.tuple
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlin.math.E
import kotlin.math.pow

private const val EPS = 1e-4

class LnTest : FunSpec({
    val ln = DefaultFunLn()

    test("parametric ln test") {
        forAll(
            row(1.0, 0.0),
            row(E, 1.0),
            row(E.pow(2), 2.0),
        )
        { x, expected -> ln.apply(x) shouldBe expected.plusOrMinus(EPS) }
    }

    context("negative and zero should not be allowed") {
        withData(0.0, -1.0, -10.0, -1e-300) {
            shouldThrow<IllegalArgumentException> { ln.apply(it) }
        }
    }

    test("property: a = ln(b) <=> e ^ a = b") {
        Arb
            // high-precision area
            .bind(
                Arb.positiveDouble(10.0),
                Arb.constant(1e-2), ::tuple
            )
            // low-precision area
            .merge(
                Arb.bind(
                    Arb.double(10.0, 100.0),
                    Arb.constant(5.0), ::tuple
                )
            )
            .checkAll(10000) { (x, tolerance) ->
                E.pow(ln.apply(x)) shouldBe x.plusOrMinus(tolerance)
            }
    }
})
