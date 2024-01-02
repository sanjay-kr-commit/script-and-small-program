data class Node <T> (
    var value : T ,
    var next : Node<T>? = null
) {
    override fun toString(): String {
        return "$value ${
            next?.toString() ?: ""
        }"
    }
}

// with back tracking
fun spiralOrderWithRecursion(node : Node<Int>?, index : Int = 0 ) : Array<Int> = node?.run {
    val array = spiralOrderWithRecursion( next , index + 1 )
    if ( value == 0 ) return@run array
    val center = (array.size - 1) / 2
    var distance = if ( center < index ) index - center else center - index
    if ( center != index ) distance *= 2
    if ( center < index ) distance--
    array[distance] = value
    array
} ?: Array( index ) { 0 }

// without back tracking
fun spiralOrderWithoutRecursion( node : Node<Int> ) : Array<Int> {
    var size = 0
    var iterator : Node<Int>? = node
    while ( iterator != null ) {
        size++
        iterator = iterator.next
    }
    iterator = node
    val array = Array( size ) { 0 }
    var index = 0
    val centre = (size-1) / 2
    while ( iterator != null ) {
        var distance = if ( index > centre ) index - centre else centre - index
        if ( centre != index ) distance *= 2
        if ( centre < index ) distance--
        array[distance] = iterator.value
        iterator = iterator.next
        index++
    }
    return array
}


fun main() {
    val head = Node( 1 ).apply {
        next = Node( 2 ).apply {
            next = Node( 3 ).apply {
                next = Node( 4 ).apply {
                    next = Node( 5 ).apply {
                        next = Node( 6 ).apply {
                            next = Node( 7 )
                        }
                    }
                }
            }
        }
    }
    println( head )
    for ( i in spiralOrderWithRecursion( head ) ) print( "$i " )
    println()
    for ( i in spiralOrderWithoutRecursion( head ) ) print( "$i " )
}