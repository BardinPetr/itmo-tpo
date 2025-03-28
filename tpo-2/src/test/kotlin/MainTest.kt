import base.DFun
import base.IFunCsc
import base.IFunLog
import base.IFunSec
import impl.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.funSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import mock.DoubleFunTable
import kotlin.math.PI
import kotlin.math.ln as lnMock
import kotlin.math.tan as tanMock
import kotlin.math.cos as cosMock
import kotlin.math.sin as sinMock

const val asm1 = -4.04615
const val asm2 = -2.23781655
const val asm3 = -2.23702831


fun systemTest(fFactory: () -> DFun) = funSpec {
    val f = fFactory()
    test("asymptote") {
        // ln part
        assertAsymptote(0.0, f::apply, isRight = true, isUp = true)
        assertAsymptote(1.0, f::apply, isRight = false, isUp = false)
        assertAsymptote(1.0, f::apply, isRight = true, isUp = true)

        // trig part
        assertAsymptote(asm1, f::apply, isRight = false, isUp = true)
        assertAsymptote(asm1, f::apply, isRight = true, isUp = true)
        assertAsymptote(asm2, f::apply, isRight = false, isUp = true)
        assertAsymptote(asm3, f::apply, isRight = true, isUp = true)
    }

    context("parametric test") {
        withData(
            // ln
            row(2.64073, 1.0),
            row(1.75607, 0.68456), // min
            row(1.28947, 1.0),
            // asymptote x=1
            row(0.77633, -1.0),
            row(0.31433, 0.0),
            row(0.23113, 1.0),


            // trig
            row(-0.19014, 0.1),
            row(-0.372335, 0.181867), // max
            row(-0.56731, 0.1),
            row(-0.798823, 0.0), // min
            row(-1.0022, 0.1),
            row(-1.962474, 1.881829), // max
            row(-2.088058, 0.5),
            row(-2.112865, 0.0), // min
            row(-2.126812, 0.5),
            // asymptote
            row(-2.712572, 1.0),
            row(-3.48133, 1.0),
            // asymptote
            row(-4.84266, 0.85),
            row(-4.96331, 0.82167), // min
            row(-5.13331, 0.85),
            row(-5.42525, 0.90885), // max
            row(-5.62233, 0.85),
            row(-6.06165, 0.2)
        )
        { (x, expected) -> f.apply(x) shouldBe expected.plusOrMinus(5e-2) }
    }

    test("property: periodicity") {
        val skipRadius = 0.2
        assertPeriodicity(f::apply, 2 * PI, 1.0, clipX = Double.NEGATIVE_INFINITY..(-1e-1)) {
            Arb.double(-10.0, 0.0)
                .cut(0.0, 1e-1)
                .cut(asm1, skipRadius, modulo = 2 * PI)
                .cut(asm2, skipRadius, modulo = 2 * PI)
                .cut(asm3, skipRadius, modulo = 2 * PI)
        }
    }
}

class MainTest : FunSpec({
    val cscTable = DoubleFunTable("csc.csv")
    val secTable = DoubleFunTable("sec.csv")
    val cscMock: IFunCsc = tableMock<IFunCsc>(cscTable)
    val secMock: IFunSec = tableMock<IFunSec>(secTable)

    include(
        "all mock: ",
        systemTest {
            DefaultMainFun(
                ::lnMock,
                ::logMock,
                ::tanMock,
                cscMock::apply,
                secMock::apply
            )
        }
    )

    include(
        "mock L2: ",
        systemTest {
            val ln = ::lnMock
            val log = ::logMock
            val cos = ::cosMock
            val sin = ::sinMock
            val tan = DefaultFunTan(sin, cos)
            val csc = cscMock
            val sec = secMock
            DefaultMainFun(ln, log, tan, csc, sec)
        }
    )

    include(
        "mock L1: ",
        systemTest {
            val ln = ::lnMock
            val log = DefaultFunLog(ln)
            val cos = ::cosMock
            val sin = DefaultFunSin(cos)
            val tan = DefaultFunTan(sin, cos)
            val csc = DefaultFunCsc(sin)
            val sec = DefaultFunSec(cos)
            DefaultMainFun(ln, log, tan, csc, sec)
        }
    )

    include(
        "no mock: ",
        systemTest {
            val ln = DefaultFunLn()
            val log = DefaultFunLog(ln)
            val cos = DefaultFunCos()
            val sin = DefaultFunSin(cos)
            val tan = DefaultFunTan(sin, cos)
            val csc = DefaultFunCsc(sin)
            val sec = DefaultFunSec(cos)
            DefaultMainFun(ln, log, tan, csc, sec)
        }
    )
})