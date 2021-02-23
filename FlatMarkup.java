package Breccia.parser;

import java.util.Iterator;

import static java.util.Collections.emptyIterator;


/** Markup that is non-composite, having no parsed components.
  */
public class FlatMarkup implements Markup {


    /** @see #tagName()
      */
    public FlatMarkup( String tagName ) { this.tagName = tagName; }



   // ━━━  I t e r a b l e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns an empty iterator.
      *
      *     @see isComposite()
      */
    public final Iterator<Markup> iterator() { return emptyIterator(); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns false: this markup has no parsed components, only {@linkplain #text() flat text}.
      */
    public final @Override boolean isComposite() { return false; }


    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public final @Override String tagName() { return tagName; }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final String tagName; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
