package Breccia.parser;

import java.util.List;


/** A fractum modelled as a parse state.  It covers the markup of the fractal head alone,
  * leaving the body (if any) to be covered by future states.
  */
public abstract class Fractum implements Markup, ParseState {


    protected Fractum() {}



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<Markup> components() { throw new UnsupportedOperationException(); }



    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }}




                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
