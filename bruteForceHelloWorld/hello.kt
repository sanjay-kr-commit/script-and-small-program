fun main( args : Array<String> ) {

  var pointer = true
  
  var input = ""
  
  if ( args.size != 0 ) for ( arg in args ) {
    if ( arg == "-np" ) pointer = false
    else input += "$arg "

  }
  
  if ( input == "" ) input = "Hello World" ;
  
  val buffer = StringBuilder( "\r" )
  
  for ( i in input ) {
    for ( j in 'a' .. i ) Thread.sleep(10).also { print( "$buffer$j${if ( pointer ) " <-" else "" }" ) }
    buffer.append( i )
  }
  
  print( "$buffer   \n" )

}
