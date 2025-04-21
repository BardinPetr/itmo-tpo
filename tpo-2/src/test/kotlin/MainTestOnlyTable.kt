import base.*
import impl.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.funSpec
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import mock.DFun1SearchingFunTable
import mock.DFun2SearchingFunTable

private fun systemTest(fFactory: () -> DFun) = funSpec {
    val f = fFactory()

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
}

//private val logDir: String = "/home/petr/study/tpo/tpo-2/build/tmp"
private val logDir: String? = null

private inline fun <reified T : DFun> tMock(filename: String) =
    tableMock<T>(DFun1SearchingFunTable(filename, logModeDirectory = logDir))

class MainTestOnlyTable : FunSpec({
    val lnMock: IFunLn = tMock("mock/ln.csv")
    val cosMock: IFunCos = tMock("mock/cos.csv")
    val sinMock: IFunSin = tMock("mock/sin.csv")
    val tanMock: IFunTan = tMock("mock/tan.csv")
    val cscMock: IFunCsc = tMock("mock/csc.csv")
    val secMock: IFunSec = tMock("mock/sec.csv")
    val logMock: IFunLog = tableMock2p(DFun2SearchingFunTable("mock/log.csv", logModeDirectory = logDir))
    include(
        "all mock: ",
        systemTest {
            DefaultMainFun(
                lnMock::apply,
                logMock::apply,
                tanMock::apply,
                cscMock::apply,
                secMock::apply
            )
        }
    )

    include(
        "mock L2: ",
        systemTest {
            val ln = lnMock::apply
            val log = logMock::apply
            val cos = cosMock::apply
            val sin = sinMock::apply
            val tan = DefaultFunTan(sin, cos)
            val csc = cscMock
            val sec = secMock
            DefaultMainFun(ln, log, tan, csc, sec)
        }
    )

    include(
        "mock L1: ",
        systemTest {
            val ln = lnMock::apply
            val log = DefaultFunLog(ln)
            val cos = cosMock::apply
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
