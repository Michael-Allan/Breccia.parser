package Breccia.parser;

import java.util.List;


       @TagName("ResourceIndicant") @DataReflector
public interface ResourceIndicant extends Markup {


    public @TagName("Reference") Markup reference();



    /** A list of the qualifiers of this indicant.
      */
    public @DataReflector List<String> qualifiers();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘ResourceIndicant’.
      */
    public default @Override String tagName() { return "ResourceIndicant"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
