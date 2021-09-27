package Breccia.parser;

import java.util.Set;


/** A command point in Breccia.
  */
public @DataReflector interface CommandPoint extends Point {


    /** The appendage clause, or null if there is none.
      */
    public AppendageClause appendageClause() throws ParseError;



    /** The set of command modifiers.
      */
    public @DataReflector Set<Modifier> modifierSet();
      // Cf. `ResourceIndicant.qualifiers`, whose form enables easier extension.



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


      @DataReflector @TagName("AppendageClause")
    public static interface AppendageClause extends Markup {


        /** The appendage itself.
          */
        public @TagName("Appendage") Markup appendage();



        /** The colon ‘:’ that delimits the appendage.
          */
        public @TagName("Delimiter") Markup delimiter();



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘AppendageClause’.
          */
        public default @Override String tagName() { return "AppendageClause"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a command point.
      */
    public static interface End extends Point.End {}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A command modifier.
      */
    public static enum Modifier { privately; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
