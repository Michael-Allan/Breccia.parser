package Breccia.parser;

import java.util.List;


/** Markup that is anonymous in the Breccia language definition.
  */
public abstract class GenericMarkup implements Markup {


    protected GenericMarkup() {}



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int column() { throw new UnsupportedOperationException(); }



    public @Override List<Markup> components() { throw new UnsupportedOperationException(); }



    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public @Override String tagName() { return "Markup"; }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
