#include <iostream>
#include <stdlib.h>
using namespace std ;

void printBoard( int *array ) {

    system("clear");

    for ( int i = 0 ; i < 9 ; i++ ) {

        cout << " " ;

        if ( array[i] == 0 ) {
            cout << i+1 ;
        } else {
            cout << (( array[i] == 1 ) ? 'x' : 'o' ) ;
        }

        if ( ((i+1)%3) == 0 ) {
            cout << "\n" ;
            if ( i != 8 ) {
                for ( int i = 0 ; i < 11 ; i++ ) cout << "-" ;
                cout << "\n" ;
            }
        } else {
            cout << " |" ;
        }

    }
    cout << "\n" ;
}

bool isGameOver( int *array , int *winner ) {

    // horizontal check
    for ( int i = 3 ; i < 10 ; i += 3 ) {
        int s = i - 3 ;
        bool emptySlot = false ;
        for ( int j = s ; j < i && !emptySlot ; j++ ) emptySlot = array[j] == 0 ;
        if ( emptySlot ) continue ;
        bool match = true ;
        s++ ;
        for ( int j = s ; j < i && match ; j++ ) match = ( array[j] == array[j-1] ) ;
        if ( match ) {
            *winner = array[s] ;
            return true ;
        }
    }

    // vertical check
    for ( int i = 0 ; i < 3 ; i++ ) {
        bool emptySlot = false ;
        for ( int j = i ; j < 9 && !emptySlot ; j += 3 ) emptySlot = array[j] == 0 ;
        if ( emptySlot ) continue ;
        bool match = true ;
        for ( int j = i+3 ; j < 9 ; j += 3 ) match = array[j] == array[j-3] ;
        if ( match ) {
            *winner = array[i] ;
            return true ;
        }
    }

    // cross check
    if ( array[4] != 0 && ((array[0] == array[4] && array[4] == array[8]) || ( array[2] == array[4] && array[4] == array[6] ))) {
        *winner = array[4] ;
        return true ;
    }

    // check for tie
    bool slotFilled = true ;
    for ( int i = 0 ; i < 9 && slotFilled ; i++ ) slotFilled = (array[i] != 0) ;

    // check for tie
    if ( slotFilled ) *winner = -1 ;

    // result
    return slotFilled ;

}

int main(int argc, char **argv) {

    int array[9] ;

    char op ;

    cout << "with each turn character will be swaped\n" ;
    cout << "between x and o\n" ;
    cout << "to reset the game type 0\n" ;
    cout << "to exit game type -1\n" ;
    cout << "otherwise index from 1 to 9\n" ;
    cout << "enter to y continue : " ;
    cin >> op ;

    if ( op != 'y' ) return 0 ;

    while ( true ) {

        for ( int i = 0 ; i < 9 ; i++ ) array[i] = 0 ;

        int c = 1 ;
        int t = 0 ;
        char invalid[] = "Invalid Index Try Again\n" ;
        char occupied[] = "Place is already occupied\n" ;
        int winner ;

        while ( !isGameOver( array , &winner ) ) {
            printBoard( array ) ;

            if ( t == 1 ) cout << invalid ;
            else if ( t == 2 ) cout << occupied ;

            t = 0 ;

            cout << "Enter Index for " ;
            cout << (( c == 1 ) ? 'x' : 'o') ;
            cout << " : " ;
            int index ;
            cin >> index ;
            if ( index == -1 ) return 0 ;
            else if ( index < 0 || index > 9 ) t = 1 ;
            else if ( index == 0 ) break ;
            else if ( array[(index-1)] != 0 ) t = 2 ;
            else array[(index-1)] = c ;

            if ( t == 0 ) c = ( c == 1 ) ? 2 : 1 ;

        }

        system("clear");
        cout << ( ( winner == 0 ) ? "It's a tie" : ( ( winner == 1 ) ? "x won" : "o won" )  ) ;

        op = 'n' ;
        cout << "\nEnter 'y' to restart game : " ;
        cin >> op ;
        if ( op != 'y' ) return 0 ;


    }

    return 0;
}
