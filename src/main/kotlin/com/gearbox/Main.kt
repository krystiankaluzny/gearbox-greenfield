package com.gearbox

fun main(args: Array<String>) {
    println("Hello world")

    var src1 = listOf(1, 2, 3, 4)
    var src2 = listOf("A", "B")
    var src3 = listOf(1.0, 2.0)

    val cartesianProduct = cartesianProduct(listOf(src1, src2, src3))

    println(cartesianProduct)
}

private fun cartesianProduct(lists: List<List<Any>>): List<List<Any>> {
    val resultLists: MutableList<List<Any>> = mutableListOf()
    if (lists.isEmpty()) {
        resultLists.add(emptyList())
        return resultLists
    }
    val firstList = lists[0]
    val remainingLists = cartesianProduct(lists.subList(1, lists.size))
    for (item in firstList) {
        for (remainingList in remainingLists) {
            val resultList: MutableList<Any> = mutableListOf()
            resultList.add(item)
            resultList.addAll(remainingList)
            resultLists.add(resultList)
        }
    }
    return resultLists
}