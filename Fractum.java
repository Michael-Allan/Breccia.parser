package Breccia.parser;

import java.util.List;


/** A fractum of Breccia modelled as a parse state.  It covers the markup of the fractal head alone,
  * leaving the body (if any) to be covered by future states.
  */
public abstract class Fractum implements Markup, ParseState {


    protected Fractum( BrecciaCursor cursor ) { this.cursor = cursor; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { throw new UnsupportedOperationException(); }



    public final @Override int lineNumber() { return cursor.fractumLineNumber(); }



    public final @Override CharSequence text() { throw new UnsupportedOperationException(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final BrecciaCursor cursor; }




                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
