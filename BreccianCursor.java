package Breccia.parser;

import java.io.IOException;


/** A pull parser of Breccian markup that operates as a unidirectional cursor over a series
  * of discrete parse states.
  */
public interface BreccianCursor { // Cf. `javax.xml.stream.XMLStreamReader`.


    /** Tells whether this cursor can advance to the next parse state.
      *
      *     @see #next()
      */
    public boolean hasNext();



    /** Advances this cursor to the next parse state.
      *
      *     @see hasNext()
      *     @see state()
      *     @return The new parse state.
      */
    public ParseState next();



    /** The present parse state.
      *
      *     @see next()
      */
    public ParseState state(); }


                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
