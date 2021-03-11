package Breccia.parser;


/** A command point in Breccia.
  */
public abstract @DataReflector class CommandPoint extends Point {


    protected CommandPoint( BrecciaCursor cursor ) { super( cursor ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a command point.
      */
    public static abstract class End extends Point.End {


        protected End() {}}}


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
