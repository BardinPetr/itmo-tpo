import base.IFunFact
import impl.DefaultFunFact
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.positiveLong
import io.kotest.property.checkAll

class FactTest : FunSpec({
    val fact: IFunFact = DefaultFunFact()

    test("factorial of 0 and 1 are 1") {
        fact.apply(0) shouldBe 1
        fact.apply(1) shouldBe 1
    }

    test("negative number should not be allowed") {
        shouldThrow<IllegalArgumentException> { fact.apply(-1) }
    }

    test("parametric test of factorial") {
        forAll(
            row(2L, 2L),
            row(3L, 6L),
            row(4L, 24L),
            row(5L, 120L),
            row(6L, 720L)
        ) { x, expected -> fact.apply(x) shouldBe expected.toDouble() }
    }

    test("property: n! = n * (n - 1)!") {
        checkAll(Arb.positiveLong(100000)) { n ->
            fact.apply(n) shouldBe (n.toDouble() * fact.apply(n - 1)).plusOrMinus(1e-100)
        }
    }
})
