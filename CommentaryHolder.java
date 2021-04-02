package Breccia.parser;


public @DataReflector interface CommentaryHolder extends Markup {


    /** The commentary held, or null if there is none.
      */
    public @TagName("Commentary") Markup commentary();



    /** The backslash ‘\’ sequence that delimits any held commentary.
      */
    public @TagName("Delimiter") Markup delimiter(); }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
