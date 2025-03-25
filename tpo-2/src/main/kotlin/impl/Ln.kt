package impl

import base.IFunLn
import kotlin.math.pow

class DefaultFunLn(
    private val terms: Int = 200
) : IFunLn {
    /**
     * https://personal.math.ubc.ca/~cbm/aands/abramowitz_and_stegun.pdf
     * Page 68, 4.1.27
     */
    override fun apply(x: Double): Double {
        require(x > 0) { "x must be positive" }
        return (1..terms step 2)
            .sumOf { i ->
                (x - 1)
                    .div(x + 1)
                    .pow(i)
                    .div(i)
            } * 2
    }
}
