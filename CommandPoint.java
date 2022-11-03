package Breccia.parser;

import java.util.List;


/** A command point in Breccia.
  */
public @DataReflector interface CommandPoint extends Point {


    /** The appendage clause, or null if there is none.
      */
    public AppendageClause appendageClause() throws ParseError;



    /** A list of the command modifiers.
      */
    public @DataReflector List<String> modifiers(); // `String` vs. `Enum` for sake of extensibility.



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


      @DataReflector @TagName("AppendageClause")
    public static interface AppendageClause extends Granum {


        /** The appendage itself.
          */
        public @TagName("Appendage") Granum appendage();



        /** The colon ‘:’ that delimits the appendage.
          */
        public @TagName("Delimiter") Granum delimiter();



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘AppendageClause’.
          */
        public default @Override String tagName() { return "AppendageClause"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a command point.
      */
    public static interface End extends Point.End {}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
