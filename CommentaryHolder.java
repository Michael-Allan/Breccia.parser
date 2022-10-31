package Breccia.parser;


public @DataReflector interface CommentaryHolder extends Granum {


    /** The commentary held, or null if there is none.
      */
    public @TagName("Commentary") Granum commentary();



    /** The backslash ‘\’ sequence that delimits any held commentary.
      */
    public @TagName("Delimiter") Granum delimiter(); }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
