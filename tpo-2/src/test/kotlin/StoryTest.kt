//import Crescent.CrescentType
//import io.kotest.assertions.nondeterministic.eventually
//import io.kotest.assertions.nondeterministic.until
//import io.kotest.core.spec.style.BehaviorSpec
//import io.kotest.matchers.collections.*
//import io.kotest.matchers.shouldBe
//import kotlin.time.Duration.Companion.seconds
//
//class SceneTests : BehaviorSpec({
//    Given("a new Scene") {
//        val scene = Scene()
//
//        When("adding objects") {
//            val light1 = PointOfLight()
//            val light2 = PointOfLight()
//            val atmosphere = Atmosphere()
//
//            scene.addObject(light1)
//            scene.addObject(light2)
//            scene.addObject(atmosphere)
//
//            Then("it should allow to retrieve all objects of a specific type") {
//                scene.get<Atmosphere>()
//                    .shouldContainOnly(atmosphere)
//
//                scene.get<PointOfLight>()
//                    .run {
//                        size shouldBe 2
//                        this shouldContainAll listOf(light1, light2)
//                    }
//            }
//
//            When("objects are removed from scene") {
//                scene.removeObject(light1)
//                scene.removeObject(light2)
//                scene.removeObject(atmosphere)
//
//                Then("it should not return them anymore") {
//                    scene.get<Atmosphere>() shouldHaveSize 0
//                    scene.get<PointOfLight>() shouldHaveSize 0
//                }
//            }
//
//            Then("it should not crash when removing not existing object") {
//                scene.removeObject(atmosphere)
//                scene.removeObject(atmosphere)
//            }
//        }
//    }
//})
//
//class PointOfLightTests : BehaviorSpec({
//    Given("a PointOfLight in atmosphere") {
//        val scene = Scene()
//        val atmosphere = Atmosphere()
//        scene.addObject(atmosphere)
//        val light = PointOfLight()
//        light.lightLevel = 1000
//        scene.addObject(light)
//
//        When("it is created") {
//            Then("it should be at scene and have initial light level") {
//                scene.get<PointOfLight>() shouldHaveSize 1
//                scene.get<PointOfLight>() shouldContain light
//            }
//
//            When("shine is called") {
//                light.shine()
//
//                Then("it should have light level till it is dead") {
//                    until(2.seconds) {
//                        light.lightLevel == 1000
//                    }
//                }
//                Then("when flying it should increase the atmosphere lightLevel") {
//                    eventually(3.seconds) {
//                        scene.get<Atmosphere>()[0].lightLevel shouldBe 1000
//                    }
//                }
//
//                Then("it should add a Crescent object to the scene") {
//                    eventually(3.seconds) {
//                        scene.get<Crescent>() shouldHaveSize 1
//                    }
//                }
//
//                Then("it should be removed from the scene") {
//                    eventually(3.seconds) {
//                        scene.get<PointOfLight>() shouldNotContain light
//                    }
//                }
//            }
//        }
//    }
//})
//
//class CrescentTests : BehaviorSpec({
//    Given("a Crescent") {
//        val scene = Scene()
//        val crescent = Crescent()
//            .also(scene::addObject)
//
//        Then("it should not shine at start") {
//            crescent.state shouldBe CrescentType.NONE
//        }
//
//        When("shine is called") {
//            crescent.shine(null)
//
//            Then("it should shine") {
//                crescent.state shouldBe CrescentType.NARROW
//            }
//        }
//
//        When("shine is called in transition process of PointOfLight") {
//            val point = PointOfLight()
//                .also(scene::addObject)
//                .also(crescent::shine)
//
//            Then("it should notify PointOfLight for it to stop transition") {
//                scene.get<PointOfLight>() shouldNotContain point
//            }
//        }
//    }
//})
//
//class AtmosphereTests : BehaviorSpec({
//    Given("an Atmosphere") {
//        val scene = Scene()
//        val atmosphere = Atmosphere()
//
//        When("it's bound to a scene") {
//            scene.addObject(atmosphere)
//
//            Then("it should add a Horizon object to the scene") {
//                val horizons = scene.get<Horizon>()
//                horizons.size shouldBe 1
//                horizons[0].hpLeft shouldBe 2
//            }
//        }
//
//        When("it is passed a flame to deliver") {
//            val flame = SunFlame(Color.WHITE)
//            scene.addObject(flame)
//
//            Then("atmosphere should deliver the COLOURFUL flame through the scene") {
//                flame.color shouldBe Color.MIXED
//            }
//
//            Then("flames should arrive to Horizon and burn it down by hp") {
//                val horizon = scene.get<Horizon>()[0]
//                horizon.hpLeft shouldBe 1
//            }
//        }
//    }
//})
//
//class SunTests : BehaviorSpec({
//    Given("a Sun") {
//        val scene = Scene()
//        val sun = Sun()
//        scene.addObject(sun)
//
//        When("it shines") {
//            sun.shine()
//
//            Then("it should create flames") {
//                val flames = scene.get<SunFlame>()
//                flames.size shouldBe 1
//                flames[0].color shouldBe Color.WHITE
//            }
//        }
//    }
//})
//
//class HorizonTests : BehaviorSpec({
//    Given("a Horizon with initial health points") {
//        val scene = Scene()
//        val horizon = Horizon()
//        scene.addObject(horizon)
//
//        Then("it initially should have HP") {
//            horizon.hp shouldBe 2
//            horizon.hpLeft shouldBe 2
//        }
//
//        When("it burns") {
//            val flame = SunFlame(Color.WHITE)
//            scene.addObject(flame)
//            horizon.burn(flame)
//
//            Then("its HP should decrement by 1") {
//                horizon.hpLeft shouldBe 1
//            }
//        }
//
//        When("it is burned more than it has hp") {
//            horizon.burn(null)
//            horizon.burn(null)
//
//            Then("it should be removed from the scene if HP reaches 0") {
//                scene.get<Horizon>() shouldNotContain horizon
//            }
//        }
//    }
//})
