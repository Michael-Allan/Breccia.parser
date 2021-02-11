package Breccia.parser;

import java.util.Iterator;


public class ResourceIndicator implements Markup {


    public ResourceIndicator() {}



    public boolean isFractal() { throw new UnsupportedOperationException(); }



    public final FlatMarkup reference = new FlatMarkup( "Reference" );



   // ━━━  I t e r a b l e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public Iterator<Markup> iterator() { throw new UnsupportedOperationException(); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public @Override String tagName() { return "ResourceIndicator"; }



    public CharSequence text() { throw new UnsupportedOperationException(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
