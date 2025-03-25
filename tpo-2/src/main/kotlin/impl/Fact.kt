package impl

import base.IFunFact

class DefaultFunFact : IFunFact {
    override fun apply(x: Long): Double {
        require(x >= 0) { "Should be non-negative" }
        return if (x < 2) 1.0 else (2..x).fold(1.0, Double::times)
    }
}