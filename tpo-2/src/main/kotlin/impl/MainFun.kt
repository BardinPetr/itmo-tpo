package impl

import base.*
import kotlin.math.pow

class DefaultMainFun(
    val ln: IFunLn,
    val log: IFunLog,
    val tan: IFunTan,
    val csc: IFunCsc,
    val sec: IFunSec
) : DFun {
    override fun apply(x: Double): Double =
        if (x <= 0)
            ((tan.apply(x) / csc.apply(x) + csc.apply(x) + sec.apply(x)
                .pow(3) / sec.apply(x) + csc.apply(x) * tan.apply(x) / tan.apply(x)) /
                    ((csc.apply(x) + tan.apply(x)).pow(2))).pow(2)
        else
            ((log.apply(10.0, x)
                    / log.apply(3.0, x)
                    / (log.apply(3.0, x) * log.apply(2.0, x)))
                    * log.apply(5.0, x)
                    + log.apply(10.0, x)
                    + (log.apply(5.0, x) + ln.apply(x)) * log.apply(5.0, x).pow(3))
}