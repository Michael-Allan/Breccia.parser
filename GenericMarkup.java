package Breccia.parser;

import java.util.Iterator;


public abstract class GenericMarkup implements Markup { /* Modeling what is optional and compound,
  and also anonymous in the Breccia language definition. */


    protected GenericMarkup() {}



   // ━━━  I t e r a b l e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public Iterator<Markup> iterator() { throw new UnsupportedOperationException(); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public @Override String tagName() { return "Markup"; }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
