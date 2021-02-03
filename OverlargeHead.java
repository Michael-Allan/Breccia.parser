package Breccia.parser;


/** Thrown when the size of a fractal head, together with its end boundary,
  * exceeds the parser’s {@linkplain BrecciaCursor#bufferCapacity buffer capacity}.
  */
public class OverlargeHead extends ParseError {


    /** @see #lineNumber
      */
    public OverlargeHead( int lineNumber ) {
        super( lineNumber, """
          The size of the fractal head, together with its end boundary,
          exceeds the parser’s buffer capacity""" ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
