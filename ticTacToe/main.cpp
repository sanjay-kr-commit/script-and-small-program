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
        while ( true ) {
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

    }

    return 0;
}
