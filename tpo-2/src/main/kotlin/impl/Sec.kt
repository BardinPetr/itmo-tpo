package impl

import base.IFunCos
import base.IFunSec

class DefaultFunSec(
    private val cos: IFunCos
) : IFunSec {
    override fun apply(x: Double): Double {
        val c = cos.apply(x)
        require(c != 0.0) { "Not defined in pi/2+n*pi" }
        return 1 / c
    }
}