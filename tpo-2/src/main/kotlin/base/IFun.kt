package base

fun interface IFun<T, R> {
    fun apply(x: T): R
}

fun interface IFun2<T1, T2, R> {
    fun apply(a: T1, b: T2): R
}

fun interface DFun : IFun<Double, Double>
fun interface DFun2 : IFun2<Double, Double, Double>
fun interface LFun : IFun<Long, Long>
