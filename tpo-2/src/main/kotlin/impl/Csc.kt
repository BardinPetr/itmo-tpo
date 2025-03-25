package impl

import base.IFunCsc
import base.IFunSin
import util.isNearZero

class DefaultFunCsc(
    private val sin: IFunSin,
) : IFunCsc {
    override fun apply(x: Double): Double {
        val c = sin.apply(x)
        println("$x $c")
        require(!isNearZero(c)) { "Not defined in n*pi" }
        return 1 / c
    }
}