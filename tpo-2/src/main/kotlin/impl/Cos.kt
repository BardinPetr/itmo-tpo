package impl

import base.IFunCos
import base.IFunFact
import kotlin.math.PI
import kotlin.math.pow

class DefaultFunCos(
    private val fact: IFunFact,
    private val terms: Int = 20
) : IFunCos {
    override fun apply(x: Double): Double =
        baseCos(x.mod(2 * PI))

    private fun baseCos(x: Double) =
        (0..terms)
            .sumOf {
                x
                    .pow(2 * it)
                    .div(fact.apply(2L * it))
                    .times((-1.0).pow(it))
            }
}