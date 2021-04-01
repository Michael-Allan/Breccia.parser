package Breccia.parser;


public @DataReflector interface CommentaryHolder extends Markup {


    /** The commentary held, or null if there is none.
      */
    public Markup commentary();



    /** The backslash ‘\’ sequence that delimits any held commentary.
      */
    public Markup delimiter(); }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
