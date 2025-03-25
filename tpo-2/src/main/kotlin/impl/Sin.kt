package impl

import base.IFunCos
import base.IFunSin
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultFunSin(
    private val cos: IFunCos,
    private val terms: Int = 20
) : IFunSin {
    override fun apply(x: Double): Double =
        sqrt(1 - cos.apply(x).pow(2))
}