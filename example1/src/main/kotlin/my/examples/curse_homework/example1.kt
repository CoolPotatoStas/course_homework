package my.examples.curse_homework

interface Tree<out T1, out T2 : Tree<T1, T2>> {
    val value: T1
    val listOfChildren: List<T2>
}

open class TreeImpl<T1, T2>(
    override val value: T1,
    override val listOfChildren: List<T2>
) : Tree<T1, T2> where T2 : TreeImpl<T1, T2> {
}

class TreeAnyImpl(
    override val value: Any,
    override val listOfChildren: ArrayList<TreeAnyImpl>
) : TreeImpl<Any, TreeAnyImpl>(value, listOfChildren)

fun <T1, T2 : Tree<T1, T2>> Tree<T1, T2>.visit() {

    println("Node [${value.toString()}]")
    if (listOfChildren.isNotEmpty()) {
        println("Children of node [${value.toString()}] {")
        listOfChildren.forEach {
            it.visit()
        }
        println("}")
    }
}

fun <T1, T2 : Tree<T1, T2>> Tree<T1, T2>.searchByValue(valueInp: T1): Tree<T1, T2>? {
    if (this.value == valueInp) return this
    var tree: Tree<T1, T2>? = null
    this.listOfChildren.forEach {
        if (tree != null) return@forEach
        tree = it.searchByValue<T1, T2>(valueInp)
    }
    return tree
}

fun TreeAnyImpl.addSubTree(subTree: TreeAnyImpl): TreeAnyImpl {

    val listForAddSubTree = this.listOfChildren.toMutableList()
    listForAddSubTree.add(subTree)

    val newList = listForAddSubTree.toList()

    return TreeAnyImpl(value = this.value, listOfChildren = newList as ArrayList<TreeAnyImpl>)
}

fun main() {

    val treeAnyImpl: TreeAnyImpl = TreeAnyImpl(
        11, arrayListOf(
            TreeAnyImpl(12, arrayListOf()),
            TreeAnyImpl(24, arrayListOf())
        )
    )
    treeAnyImpl.visit()
    treeAnyImpl.searchByValue(24)?.visit()
    treeAnyImpl.searchByValue(0)?.visit()

    println()
    val rootTree = treeAnyImpl.addSubTree(
        TreeAnyImpl(
            15, arrayListOf(
                TreeAnyImpl(8, arrayListOf())
            )
        )
    )
    rootTree.visit()
}