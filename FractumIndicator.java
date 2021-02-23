package Breccia.parser;

import java.util.Iterator;
import java.util.List;


public class FractumIndicator implements Markup {


    public FractumIndicator() {}



    public final List<FlatMarkup> patterns = List.of(); // Yet uncoded.



    public ResourceIndicator resourceIndicator() { throw new UnsupportedOperationException(); }



   // ━━━  I t e r a b l e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public Iterator<Markup> iterator() { throw new UnsupportedOperationException(); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public @Override String tagName() { return "FractumIndicator"; }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
