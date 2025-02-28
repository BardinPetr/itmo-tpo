data class LeftistNode<T>(
    val element: T,
    var left: LeftistNode<T>? = null,
    var right: LeftistNode<T>? = null,
    var dist: Int = 0
)


private fun <T : Comparable<T>> merge(x: LeftistNode<T>?, y: LeftistNode<T>?): LeftistNode<T>? {
    var x = x ?: return y
    var y = y ?: return x
    if (x.element > y.element)
        x = y.also { y = x }

    x.right = merge(x.right, y)

    if (x.left == null) {
        x.left = x.right
        x.right = null
    } else {
        if (x.left!!.dist < x.right!!.dist)
            x.left = x.right.also { x.right = x.left }
        x.dist = x.right!!.dist + 1
    }
    return x
}


class LeftistHeap<T : Comparable<T>> {
    private var root: LeftistNode<T>? = null

    val isEmpty: Boolean
        get() = root == null

    fun clear() {
        root = null
    }

    fun insert(x: T) {
        root = merge(LeftistNode(x), root)
    }

    fun popMin(): T {
        if (this.isEmpty) throw NoSuchElementException()
        val minItem = root!!.element
        root = merge(root!!.left, root!!.right)
        return minItem
    }

    fun peekMin(): T {
        if (this.isEmpty) throw NoSuchElementException()
        return root!!.element
    }

    fun merge(rhs: LeftistHeap<T>) {
        if (this === rhs) return
        root = merge(root, rhs.root)
        rhs.root = null
    }
}
