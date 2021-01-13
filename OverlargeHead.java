package Breccia.parser;


/** Thrown when a fractal head, together with its end boundary, overflows a cursor buffer,
  * the size of which is {@value BrecciaCursor#bufferCapacity } characters.
  */
public class OverlargeHead extends ParseError {


    /** @see #lineNumber
      */
    public OverlargeHead( int lineNumber ) { super( lineNumber ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
