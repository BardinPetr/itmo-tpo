fun arctg(x: Double, order: Int = 11): Double {
    if (order <= 0) throw IllegalArgumentException("Order must be greater than 0")
    if (order % 2 == 0) throw IllegalArgumentException("Order should be odd")
    if (x < -1 || x > 1) throw IllegalArgumentException("Series doesn't converge at x=$x. Convergence area is [-1; 1]")

    var result = 0.0
    var term = x
    var sign = 1
    for (n in 1..order step 2) {
        result += sign * term / n
        term *= x * x
        sign *= -1
    }
    return result
}
