package impl

import base.IFunCsc
import base.IFunSin

class DefaultFunCsc(
    private val sin: IFunSin,
) : IFunCsc {
    override fun apply(x: Double): Double {
        val c = sin.apply(x)
        require(c != 0.0) { "Not defined in n*pi" }
        return 1 / c
    }
}