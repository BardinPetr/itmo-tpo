import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.names.DuplicateTestNameMode
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class LeftistHeapTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerTest
    duplicateTestNameMode = DuplicateTestNameMode.Silent

    fun LeftistHeap<Int>.shouldBeEmpty(): Boolean =
        this.isEmpty shouldBe true

    infix fun LeftistHeap<Int>.shouldContain(expectedContents: List<Int>) {
        val contents = mutableListOf<Int>()
        while (!this.isEmpty)
            contents.add(this.popMin())
        contents shouldBe expectedContents
    }

    Given("an empty LeftistHeap") {
        val heap = LeftistHeap<Int>()

        Then("it should be empty") {
            heap.shouldBeEmpty()
        }

        When("clear is called") {
            heap.clear()

            Then("it should still be empty") {
                heap.shouldBeEmpty()
            }
        }

        Then("calling popMin should throw an exception") {
            shouldThrow<Exception> { heap.popMin() }
        }

        Then("calling peekMin should throw an exception") {
            shouldThrow<Exception> { heap.peekMin() }
        }

        When("insert is called") {
            heap.insert(5)

            Then("it should not be empty") {
                heap.isEmpty shouldBe false
            }

            Then("peekMin should return the inserted value") {
                heap.peekMin() shouldBe 5
            }

            When("popMin is called") {
                val min = heap.popMin()

                Then("it should return the inserted value") {
                    min shouldBe 5
                }

                Then("it should be empty again") {
                    heap.shouldBeEmpty()
                }
            }
        }

        When("merge is called with a non-empty heap") {
            val otherHeap = LeftistHeap<Int>()
            otherHeap.insert(5)
            heap.merge(otherHeap)

            Then("new heap should contain only one element from other heap") {
                heap.popMin() shouldBe 5
                heap.shouldBeEmpty()
            }
        }

        When("inserting duplicate elements") {
            heap.insert(5)
            heap.insert(5)
            heap.insert(3)
            heap.insert(3)

            When("popping minimum") {
                val min = heap.popMin()
                Then("it should return the smallest value") {
                    min shouldBe 3
                }
            }

            Then("heap should contain duplicates in order") {
                heap shouldContain listOf(3, 3, 5, 5)
            }
        }
    }

    Given("a non-empty LeftistHeap") {
        val heap = LeftistHeap<Int>()
        heap.insert(5)

        When("insert is called with a smaller value") {
            heap.insert(3)

            Then("peekMin should return the smaller value") {
                heap.peekMin() shouldBe 3
            }
        }

        When("insert is called with a larger value") {
            heap.insert(7)

            Then("peekMin should still return the original value") {
                heap.peekMin() shouldBe 5
            }
        }

        When("popMin is called") {
            val min = heap.popMin()

            Then("it should return the original value") {
                min shouldBe 5
            }

            Then("it should be empty") {
                heap.shouldBeEmpty()
            }
        }

        When("merge is called with an empty heap") {
            val otherHeap = LeftistHeap<Int>()
            heap.merge(otherHeap)

            Then("it should still not be empty") {
                heap.isEmpty shouldBe false
            }

            Then("only original element should stay") {
                heap.popMin() shouldBe 5
                heap.shouldBeEmpty()
            }
        }

        When("merge is called with a non-empty heap with a smaller value") {
            val otherHeap = LeftistHeap<Int>()
            otherHeap.insert(3)
            heap.merge(otherHeap)

            Then("peekMin should return the smaller value") {
                heap.peekMin() shouldBe 3
            }
        }

        When("merge is called with a non-empty heap with a larger value") {
            val otherHeap = LeftistHeap<Int>()
            otherHeap.insert(7)
            heap.merge(otherHeap)

            Then("peekMin should still return the original value") {
                heap.peekMin() shouldBe 5
            }
        }

        When("merge is called with same heap") {
            heap.merge(heap)

            Then("heap should not change") {
                heap shouldContain listOf(5)
            }
        }
    }

    Given("a LeftistHeap with multiple values") {
        val heap = LeftistHeap<Int>()
        heap.insert(5)
        heap.insert(3)
        heap.insert(7)

        When("popMin is called") {
            val min = heap.popMin()

            Then("it should return the smallest value") {
                min shouldBe 3
            }

            Then("peekMin should return the next smallest value") {
                heap.peekMin() shouldBe 5
            }
        }

        When("merge is called with another heap with multiple values") {
            val otherHeap = LeftistHeap<Int>()
            otherHeap.insert(2)
            otherHeap.insert(4)
            otherHeap.insert(6)
            heap.merge(otherHeap)

            Then("peekMin should return the smallest value from both heaps") {
                heap.peekMin() shouldBe 2
            }

            Then("heap should contain all elements from both heaps") {
                heap shouldContain listOf(2, 3, 4, 5, 6, 7)
            }
        }
    }
})
