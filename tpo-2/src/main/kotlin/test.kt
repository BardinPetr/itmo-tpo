import impl.DefaultFunCos
import impl.DefaultFunFact
import impl.DefaultFunLn


fun main() {
    val mcos = DefaultFunCos(DefaultFunFact())
    val mln = DefaultFunLn()

//    plotFunction(-4 * PI, 4 * PI, 1000) { arrayOf(mcos.apply(it), cos(it)) }
//        .save("/home/petr/study/tpo/tpo-2/cos.png")

//    plotFunction(1e-1, 100.0, 1000) { arrayOf(mln.apply(it), ln(it)) }
//        .save("/home/petr/study/tpo/tpo-2/ln.png")
}
