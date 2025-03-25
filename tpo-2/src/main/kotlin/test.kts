import kotlin.math.sign

val x = 0.0
val count = 10
val shift = 0.1
val isRight = true


val y =
    generateSequence(shift) { it / 2 }
        .take(count)
        .map { x + if (isRight) it else -it }
        .map { -it }
        .zipWithNext { a, b -> sign(b - a).toInt() }
        .all { it == -1 }

println(y)
