package Breccia.parser;

import java.io.IOException;


/** A pull parser of Breccian markup that operates as a unidirectional cursor over a series
  * of discrete parse states.
  */
public interface BreccianCursor { // Cf. `javax.xml.stream.XMLStreamReader`.


    /** Whether this cursor can advance to the next parse state.
      *
      *     @see #next()
      */
    public boolean hasNext();



    /** Advances this cursor to the next parse state.
      *
      *     @see hasNext()
      *     @see state()
      *     @return The new parse state.
      *     @throws java.util.NoSuchElementException If `hasNext` is false.
      */
    public ParseState next() throws ParseError;



    /** The present parse state.
      *
      *     @see next()
      */
    public ParseState state(); }


                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
