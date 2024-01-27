import java.io.File
import java.util.Stack
import kotlin.system.exitProcess

fun main(args: Array<String> ) {

    if ( args.isEmpty() ) {
        println( "No Argument Found" )
        exitProcess( -1 )
    }

    val sourceFileList : List<String> = args.filter { it.endsWith( ".bf" ) }

    if ( sourceFileList.isEmpty() ) {
        println( "No Source File Is Give" )
        exitProcess( -1 ) ;
    }

    sourceFileList.forEach { sourceFile ->
        interpret( sourceFile )
    }

}

fun interpret( sourceFile : String ) {
    val file = File( sourceFile )
    if ( !file.isFile ) {
        println( "$file Not Found" )
        return
    }
    val instructions = file.readText()
    val allocatedMemory = ArrayList<Int>()
    allocatedMemory.add( 0 )
    var pointer : Int = 0
    var index : Int = 0
    val stack = Stack<Int>()
    while ( index < instructions.length ) {
        when ( instructions[index] ) {
            '+' -> allocatedMemory[pointer]++
            '-' -> allocatedMemory[pointer]--
            '>' -> {
                if ( allocatedMemory.size == pointer+1 ) allocatedMemory.add(0)
                pointer++
            }
            '<' -> pointer--
            '.' -> print( allocatedMemory[pointer].toChar() )
            ',' -> allocatedMemory[pointer] = readln()[0].code
            '[' -> {
                if ( allocatedMemory[pointer] != 0 ) stack.push( index )
                else {
                    index += instructions.substring( index ).indexOf( ']' )
                }
            }
            ']' -> {
                val loopStart = stack.pop()
                if ( allocatedMemory[pointer] != 0 ) index = loopStart -1
            }
        }
        index++
    }
}