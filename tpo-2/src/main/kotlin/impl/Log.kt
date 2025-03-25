package impl

import base.IFunLn
import base.IFunLog

class DefaultFunLog(
    private val ln: IFunLn,
) : IFunLog {
    override fun apply(a: Double, b: Double): Double {
        require(a > 0) { "log base should be positive" }
        require(a != 1.0) { "log base should not be 1" }
        require(b > 0) { "value should be positive" }
        return ln.apply(b) / ln.apply(a)
    }
}
