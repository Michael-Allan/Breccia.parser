package Breccia.parser;


/** A cursor based, unidirectional reader of Breccian markup.
  */
public interface BreccianReader extends AutoCloseable { // Cf. `javax.xml.stream.XMLStreamReader`.


    /** Answers whether the read cursor can advance to the next parse state.
      *
      *     @see #next()
      */
    public boolean hasNext();



    /** Advances the read cursor to the next parse state.
      */
    public void next();



   // ━━━  A u t o   C l o s e a b l e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public void close(); }


                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
