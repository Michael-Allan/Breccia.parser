package Breccia.parser;

import java.util.List;


/** Markup that is non-composite, having no parsed components.
  */
public class FlatMarkup implements Markup {


    /** @see #tagName()
      */
    public FlatMarkup( String tagName ) { this.tagName = tagName; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns an empty list: this markup has no parsed components, only {@linkplain #text() flat text}.
      */
    public @Override List<Markup> components() { return List.of(); }



    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public final @Override String tagName() { return tagName; }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final String tagName; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
