package impl

import base.IFunCos
import base.IFunSin
import kotlin.math.PI

class DefaultFunSin(
    private val cos: IFunCos
) : IFunSin {
    override fun apply(x: Double): Double = cos.apply(PI / 2 - x)
}