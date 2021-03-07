package Breccia.parser;

import java.util.List;


public class ResourceIndicator implements Markup {


    public ResourceIndicator() {}



    public boolean isFractal() { throw new UnsupportedOperationException(); }



    public final FlatMarkup reference = new FlatMarkup( "Reference" );



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int column() { throw new UnsupportedOperationException(); }



    public @Override List<Markup> components() { throw new UnsupportedOperationException(); }



    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public @Override String tagName() { return "ResourceIndicator"; }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
