import java.util.*
import kotlin.collections.HashSet

val decimalNumericToken : HashSet<Char> = hashSetOf(
    '0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8' , '9' , '.'
)

val operatorToken : HashSet<Char> = hashSetOf(
    '+' , '*' , '/' , '-' , '%' , '^'
)

val openBracketToken : HashSet<Char> = hashSetOf(
    '(' , '[' , '{'
)

val closeBracketToken : HashSet<Char> = hashSetOf(
    ')' , ']' , '}'
)


val bracketToken : HashSet<Char> = HashSet<Char>().apply {
    addAll( openBracketToken )
    addAll( closeBracketToken )
}

val validToken : HashSet<Char> = HashSet<Char>().apply {
    addAll( decimalNumericToken )
    addAll( operatorToken )
    addAll( bracketToken )
}

class IllegalTokenException( token : Char ) : Exception( "Token Not Allowed \"$token\"" )
class IllegalBracketPlacementException : Exception( "Please Check Bracket Placement" )

fun String.checkForIllegalTokens() = forEach { character ->
    if ( !validToken.contains( character ) ) throw IllegalTokenException( character )
}

fun String.checkForBracketPlacement() {
    val stack = Stack<Char>()
    forEach { character ->
        when (character) {
            '(', '[', '{' -> stack.add( character )
            ')' -> if ( stack.isEmpty() || stack.pop() != '(' ) throw IllegalBracketPlacementException()
            ']' -> if ( stack.isEmpty() || stack.pop() != '[' ) throw IllegalBracketPlacementException()
            '}' -> if ( stack.isEmpty() || stack.pop() != '{' ) throw IllegalBracketPlacementException()
        }
    }
    if ( stack.isNotEmpty() ) throw IllegalBracketPlacementException()
}

fun String.removeEmptyBracket() : String {
    val newString = StringBuilder()
    var i = 0
    while ( i < length ) {
        when {
            this[i] == '(' && this[i+1] == ')' -> i++
            this[i] == '[' && this[i+1] == ']' -> i++
            this[i] == '{' && this[i+1] == '}' -> i++
            else -> newString.append( this[i] )
        }
        i++
    }
    return newString.toString()
}

fun String.addEmptyOperator() : String = Array(length){
    this[it].toString()
}.let { array ->

    array.indices.forEach {

        if (
            it in 1 ..< array.size &&
            (array[it] == "(" || array[it] == "[" || array[it] == "{" ) &&
            !operatorToken.contains( array[it-1][0] ) && !openBracketToken.contains( array[it-1][0] )
        ) {
            array[it] = "*${array[it]}"
        }

    }

    // array to string conversion
    return@let StringBuilder().let { newString ->
        array.forEach {
            newString.append( it )
        }
        newString.toString()
    }
}

fun String.validateExpression() : String {
    checkForIllegalTokens()
    checkForBracketPlacement()
    return removeEmptyBracket().addEmptyOperator()
}

fun String.operateOn( operator : Char, applyOperation: ( Double , Double ) -> Any ) : String {
    val operatorIndex = indexOf( operator )

    val a = StringBuilder()
    var prefixIndex = operatorIndex-1
    while ( prefixIndex > -1 && (this[prefixIndex] == '-' || decimalNumericToken.contains( this[prefixIndex] )) ){
        a.append( this[prefixIndex] )
        prefixIndex--
    }
    a.reverse()

    val b = StringBuilder()
    var suffixIndex = operatorIndex + 1
    while ( suffixIndex < length && (this[suffixIndex] == '-' || decimalNumericToken.contains( this[suffixIndex] )) ) {
        b.append( this[suffixIndex] )
        suffixIndex++
    }

    val prefix = substring( 0 , prefixIndex+1 )
    val value = applyOperation( a.toString().toDouble() , b.toString().toDouble() ).toString()
    val suffix = substring( suffixIndex , length )

    return "$prefix$value$suffix"

}

tailrec fun String.evaluateExpression() : String = when {
    // expansion functions
    contains( "^" ) -> operateOn( '^' ) { a , b ->
        if ( b.toInt() == 0 ) 1
            else {
                val expandedForm = StringBuilder()
                expandedForm.append(a)
                repeat(b.toInt() - 1) {
                    expandedForm.append( "*$a" )
                }
                expandedForm
            }
    }.evaluateExpression()
    // operator ordered as in BOD-MAS
    contains( "/" ) -> operateOn( '/' ) { a , b ->
        a / b
    }.evaluateExpression()
    contains( "*" ) -> operateOn( '*' ) { a , b ->
        a * b
    }.evaluateExpression()
    contains( "+" ) -> operateOn( '+' ) { a , b ->
        a + b
    }.evaluateExpression()
    contains( "-" ) && indexOf( "-" ) > 0 -> operateOn( '-' ) { a , b ->
        a - b
    }.evaluateExpression()
    contains( "%" ) -> operateOn( '%' ) { a , b ->
        a % b
    }.evaluateExpression()
    else -> this
}

fun String.parseExpression() : String {

    val validatedExpression = StringBuilder( validateExpression() )

    val bracketFirstOperation : ( Char , Char ) -> Unit = { openBracket : Char, closeBracket : Char ->
        while ( validatedExpression.contains( openBracket ) ) {
            val openBracketIndex = validatedExpression.lastIndexOf( openBracket )
            val closeBracketIndex = openBracketIndex + validatedExpression.substring( openBracketIndex + 1 ).indexOf( closeBracket )
            validatedExpression.replace( openBracketIndex , closeBracketIndex+2 , validatedExpression.substring( openBracketIndex+1 , closeBracketIndex+1 ).evaluateExpression() )
        }
    }

    val maxOfThree : ( Int , Int , Int ) -> Int = { num1 , num2 , num3 ->
        if ( num1 > num2 ) {
            if ( num1 > num3 ) 1 else 3
        } else{
            if ( num2 > num3 ) 2 else 3
        }

    }

    val bracketIsAvailable = IntArray( 3 )

    bracketIsAvailable[0] = validatedExpression.lastIndexOf( "(" )
    bracketIsAvailable[1] = validatedExpression.lastIndexOf( "[" )
    bracketIsAvailable[2] = validatedExpression.lastIndexOf( "{" )
    var max = maxOfThree( bracketIsAvailable[0] , bracketIsAvailable[1] , bracketIsAvailable[2] )

    while ( bracketIsAvailable[max-1] > -1 ) {
        when ( max ) {
            1 -> bracketFirstOperation( '(' , ')' )
            2 -> bracketFirstOperation( '[' , ']' )
            else -> bracketFirstOperation( '{' , '}' )
        }
        bracketIsAvailable[0] = validatedExpression.lastIndexOf( "(" )
        bracketIsAvailable[1] = validatedExpression.lastIndexOf( "[" )
        bracketIsAvailable[2] = validatedExpression.lastIndexOf( "{" )
        max = maxOfThree( bracketIsAvailable[0] , bracketIsAvailable[1] , bracketIsAvailable[2] )
    }

    return validatedExpression.toString().evaluateExpression()
}

fun main( args : Array<String> ) {

    if ( args.isNotEmpty() ) {
        var count = 0
        args.forEach { exp ->
            try {
                println( "$exp : ${ exp.parseExpression() }" )
                count++ ;
            } catch ( _ : Exception ) {}
        }
        if ( count > 0 ) return
    }

    println("Allowed Operation + , - , / , * , % , ^")
    while (true) {
        print("Enter a expression : ")
        try {
            "ans is " + readln().parseExpression().also { println(it) }
        } catch ( e : Exception ) { println( e ) }
    }

}
