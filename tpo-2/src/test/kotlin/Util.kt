import base.DFun
import io.kotest.core.test.TestScope
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import kotlin.math.sign

fun TestScope.assertAsymptote(
    x: Double,
    f: DFun,
    isRight: Boolean,
    isUp: Boolean,
    shift: Double = 0.1,
    count: Int = 10
) {
    assertThrows<Exception> { f.apply(x) }
    generateSequence(shift) { it / 2 }
        .take(count)
        .map { x + if (isRight) it else -it }
        .map(f::apply)
        .zipWithNext { a, b -> sign(b - a).toInt() }
        .all { it == (if (isUp) 1 else -1) } shouldBe true
}
