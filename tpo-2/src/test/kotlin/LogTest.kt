import base.DFun
import impl.DefaultFunLog
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.tuple
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.positiveDouble
import io.kotest.property.checkAll
import org.junit.jupiter.api.assertThrows
import kotlin.math.pow
import kotlin.math.ln as lnTrue
import kotlin.math.log as logTrue

private const val EPS = 1e-4

class LogTest : FunSpec({
    val mockLn = ::lnTrue
    val log = DefaultFunLog(mockLn)

    test("invalid input tests") {
        forAll(
            row(0.0, 2.0),
            row(-1.0, 2.0),
            row(1.0, 2.0),
            row(1.0, 0.0),
            row(1.0, -1.0),
        )
        { a, b -> shouldThrow<IllegalArgumentException> { log.apply(a, b) } }
    }

    test("valid input tests") {
        forAll(
            row(2.0, 1.0 / 2),
            row(2.0, 1.0),
            row(2.0, 2.0),
            row(2.0, 4.0),
            row(10.0, 1.0 / 10),
            row(10.0, 1.0),
            row(10.0, 10.0),
            row(10.0, 100.0),
        )
        { a, b -> log.apply(a, b) shouldBe logTrue(b, a).plusOrMinus(1e-2) }
    }

    test("asymptote x = 0") {
        val f = DFun { log.apply(10.0, it) }
        assertAsymptote(0.0, f, isRight = true, isUp = false)
    }

    test("property: b = log_a(x) <=> a ^ b = x") {
        Arb
            .bind(
                Arb.double(1e-10, 1e10).filter { it != 1.0 }, // a
                Arb.positiveDouble(1e10), // x
                ::tuple
            )
            .checkAll(10000) { (a, x) ->
                a.pow(log.apply(a, x)) shouldBe x.plusOrMinus(EPS)
            }
    }
})
