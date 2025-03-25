//import io.kotest.assertions.throwables.shouldNotThrow
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.FunSpec
//import io.kotest.core.tuple
//import io.kotest.datatest.withData
//import io.kotest.matchers.doubles.plusOrMinus
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.string.shouldContainIgnoringCase
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.bind
//import io.kotest.property.arbitrary.constant
//import io.kotest.property.arbitrary.double
//import io.kotest.property.arbitrary.merge
//import io.kotest.property.checkAll
//import kotlin.math.abs
//import kotlin.math.sqrt
//import kotlin.math.tan
//import arctg as atanTest
//import kotlin.math.atan as atanTrue
//
//class ArctgTest : FunSpec({
//    fun checker(x: Double, tolerance: Double = 1e-3) =
//        atanTest(x) shouldBe atanTrue(x).plusOrMinus(tolerance)
//
//    // unit tests
//    context("arctg should be zero at x=0") {
//        atanTest(0.0) shouldBe 0
//    }
//
//    context("arctg should give valid results on bounding points [-1, 1] within large inaccuracy") {
//        withData(1.0, -1.0) { checker(it, tolerance = 1e-1) }
//    }
//
//    context("arctg should give valid results in convergence area with tolerance up to 10^-4") {
//        withData(1 / sqrt(3.0), -1 / sqrt(3.0)) { checker(it, 1e-4) }
//    }
//
//    context("arctg should raise exception outside of series convergence radius") {
//        withData(-10.0, -1.1, 1.1, 10.0) {
//            shouldThrow<IllegalArgumentException> { checker(it) }
//                .message shouldContainIgnoringCase "doesn't converge"
//        }
//    }
//
//    context("arctg should have increasing accuracy with increasing series order") {
//        (1..30 step 2)
//            .map { order ->
//                (-50..50)
//                    .map { it.toDouble() / 100 } // -0.5 -- 0.5
//                    .map { abs(atanTest(it, order) - atanTrue(it)) }
//                    .average()
//            }
//            .zipWithNext { a, b -> a - b }
//            .count { it < 0 }
//            .shouldBe(0)
//    }
//
//    context("arctg should not work with invalid series order") {
//        shouldThrow<IllegalArgumentException> { atanTest(0.0, 0) }
//        shouldThrow<IllegalArgumentException> { atanTest(0.0, -1) }
//        shouldThrow<IllegalArgumentException> { atanTest(0.0, 4) }
//        shouldNotThrow<IllegalArgumentException> { atanTest(0.0, 1) }
//    }
//
//    // property tests
//    context("arctg should be an odd function") {
//        Arb
//            .double(-1.0, 1.0)
//            .checkAll(10000)
//            { -atanTest(it) shouldBe atanTest(-it).plusOrMinus(1e-11) }
//    }
//
//    context("arctg should give valid results with specified tolerance inside convergence area as property test") {
//        Arb
//            // high-precision area
//            .bind(
//                Arb.double(-0.9, 0.9),
//                Arb.constant(1e-11), ::tuple
//            )
//            // low-precision area
//            .merge(
//                Arb.bind(
//                    Arb
//                        .double(-1.0, -0.9)
//                        .merge(Arb.double(0.9, 1.0)),
//                    Arb.constant(1e-3), ::tuple
//                )
//            )
//            .checkAll(10000)
//            { (x, tolerance) -> tan(atanTest(x, 1001)) shouldBe x.plusOrMinus(tolerance) }
//    }
//})
