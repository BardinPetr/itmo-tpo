import impl.*
import util.plotFunction
import kotlin.math.PI
import kotlin.math.cos as cosTrue
import kotlin.math.ln as lnTrue
import kotlin.math.sin as sinTrue
import kotlin.math.tan as tanTrue

fun main() {
    val cos = DefaultFunCos()
    val sin = DefaultFunSin(cos)
    val tan = DefaultFunTan(sin, cos)
    val csc = DefaultFunCsc(sin)
    val sec = DefaultFunSec(cos)
    val ln = DefaultFunLn()
    val log = DefaultFunLog(ln)
    val f = DefaultMainFun(ln, log, tan, csc, sec)

    plotFunction(-20.0, 10.0, 1000, clip = 10.0, name = "full") { arrayOf(f.apply(it)) }

    plotFunction(-4 * PI, 4 * PI, 1000, name = "sin") { arrayOf(sin.apply(it), sinTrue(it)) }

    plotFunction(-4 * PI, 4 * PI, 1000, name = "cos") { arrayOf(cos.apply(it), cosTrue(it)) }

    plotFunction(-4 * PI, 4 * PI, 1000, name = "tan") { arrayOf(tan.apply(it), tanTrue(it)) }

    plotFunction(-4 * PI, 4 * PI, 1000, name = "csc") { arrayOf(csc.apply(it)) }

    plotFunction(-4 * PI, 4 * PI, 1000, name = "sec") { arrayOf(sec.apply(it)) }

    plotFunction(1e-1, 100.0, 1000, name = "ln") { arrayOf(ln.apply(it), lnTrue(it)) }
}
