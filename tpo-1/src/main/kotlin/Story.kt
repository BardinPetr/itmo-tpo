import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

abstract class SceneObject() {
    var scene: Scene? = null

    init {
        log("Created $this")
    }

    fun log(text: String) = println("[${this::class.simpleName}] $text")

    open fun onBind() {}
}

class Scene {
    private val objects = mutableListOf<SceneObject>()

    internal inline fun <reified T : SceneObject> get(): List<T> =
        objects.filterIsInstance<T>()

    internal inline fun <reified T : SceneObject> each(f: Consumer<T>) =
        objects.filterIsInstance<T>().forEach(f::accept)

    fun addObject(obj: SceneObject) {
        obj.scene = this
        objects.add(obj)
        obj.onBind()
    }

    fun removeObject(obj: SceneObject) {
        obj.scene = null
        objects.remove(obj)
    }
}

class PointOfLight(var lightLevel: Int = 0) : SceneObject() {
    fun shine() {
        log("Shining")
        scheduler.schedule({
            scene!!.each<Atmosphere> { it.lightLevel += lightLevel }
            scene!!.each<PointOfLight> { it.transition() }
        }, 2000, TimeUnit.MILLISECONDS)
    }

    fun transition() {
        log("Transforming...")
        lightLevel = 0
        val crescent = Crescent(Crescent.CrescentType.NARROW)
        scene!!.addObject(crescent)
        crescent.shine(this)
    }

    fun endTransition() {
        scene!!.removeObject(this)
        log("Transformed into crescent")
    }

    override fun onBind() {
        super.onBind()
    }
}

class Crescent(private var type: CrescentType = CrescentType.NONE) : SceneObject() {
    val state get() = type

    fun shine(sourcePoL: PointOfLight?) {
        log("Shining")
        type = CrescentType.NARROW
        sourcePoL?.endTransition()
        scene!!.each<Atmosphere> { it.sunTime() }
    }

    enum class CrescentType {
        NARROW, NONE
    }
}

class Sun : SceneObject() {
    private var flame: SunFlame? = null

    fun shine() {
        flame = SunFlame(Color.WHITE)
        scene!!.addObject(flame!!)
    }
}

enum class Color {
    WHITE, MIXED
}

class SunFlame(var color: Color) : SceneObject() {
    override fun onBind() {
        scene!!.each<Atmosphere> { it.deliverFlames(this) }
    }
}

class Atmosphere(var lightLevel: Int = 0, private val horizon: Horizon = Horizon()) : SceneObject() {
    fun sunTime() {
        log("Atmosphere creating suns")
        scene!!.let {
            val sun1 = Sun()
            val sun2 = Sun()
            it.addObject(sun1)
            it.addObject(sun2)
            sun1.shine()
            sun2.shine()
        }
    }

    fun deliverFlames(flame: SunFlame) {
        log("Flame flying through atmosphere")
        scene!!.each<SunFlame> { it.color = Color.MIXED }
        scene!!.each<Horizon> { it.burn(flame) }
    }

    override fun onBind() {
        scene!!.addObject(horizon)
    }
}

class Horizon(val hp: Int = 2, private var hits: Int = 0) : SceneObject() {

    val hpLeft get() = hp - hits

    fun burn(sender: SunFlame?) {
        log("Horizon burning (by $sender)...")
        hits += 1
        if (hpLeft == 0) {
            log("Horizon burned!")
            scene!!.removeObject(this)
        }
    }
}

/*
fun main() {
    val scene = Scene()
    val atmosphere = Atmosphere()
    scene.addObject(atmosphere)
    val pointOfLight = PointOfLight(1000)
    scene.addObject(pointOfLight)
    pointOfLight.shine()
}
*/
