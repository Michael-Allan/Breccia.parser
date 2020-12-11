package Breccia.parser;

import java.nio.file.Path;


/** A reader of plain Breccia.
  */
public class BrecciaReader implements BreccianReader {


    /** @param _f The path of the Breccian file to read.
      */
    public BrecciaReader( Path _f ) {}



   // ━━━  A u t o   C l o s e a b l e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override void close() {}



   // ━━━  B r e c c i a n   R e a d e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override boolean hasNext() { return false; }



    public @Override void next() { throw new UnsupportedOperationException(); }} // Yet uncoded.



                                                        // Copyright © 2020  Michael Allan.  Licence MIT.
