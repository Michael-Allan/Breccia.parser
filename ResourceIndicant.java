package Breccia.parser;

import java.util.List;


       @TagName("ResourceIndicant") @DataReflector
public interface ResourceIndicant extends Granum {


    public @TagName("Reference") Granum reference();



    /** A list of the qualifiers of this indicant.
      */
    public @DataReflector List<String> qualifiers();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘ResourceIndicant’.
      */
    public default @Override String tagName() { return "ResourceIndicant"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
