package Breccia.parser;

import Java.DelimitableCharSequence;
import java.util.List;

import static Java.CharBuffers.newDelimitableCharSequence;


/** A fractum of Breccia modelled as a parse state.  It covers the markup of the fractal head alone,
  * leaving the body (if any) to be covered by future states.
  */
public abstract class Fractum implements Markup, ParseState {


    protected Fractum( BrecciaCursor cursor ) {
        this.cursor = cursor;
        text = newDelimitableCharSequence( cursor.buffer ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { throw new UnsupportedOperationException(); }



    public final @Override int lineNumber() { return lineNumber; }



    public final @Override CharSequence text() { return text; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final BrecciaCursor cursor;



    int lineNumber;



    final DelimitableCharSequence text; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
