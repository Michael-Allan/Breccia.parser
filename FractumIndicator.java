package Breccia.parser;

import java.util.List;


public @DataReflector interface FractumIndicator extends Markup {


    public List<Pattern> patterns();



    public ResourceIndicator resourceIndicator();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘FractumIndicator’.
      */
    public default @Override String tagName() { return "FractumIndicator"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
