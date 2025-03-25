package impl

import base.IFunCos
import base.IFunSin
import base.IFunTan
import kotlin.math.abs

class DefaultFunTan(
    private val sin: IFunSin,
    private val cos: IFunCos
) : IFunTan {
    override fun apply(x: Double): Double {
        val c = cos.apply(x)
        require(abs(c) > 1e-10) { "Not defined in pi/2+n*pi" }
        return sin.apply(x) / c
    }
}