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


    /** @return Zero: fracta comprise whole lines.
      */
    public final @Override int column() { return 0; }



    public final @Override List<Markup> components() { throw new UnsupportedOperationException(); }



    public final @Override int lineNumber() { return cursor.fractumLineNumber(); }



    public final @Override CharSequence text() { return text; }



   // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String toString() { return Markup.toString( this ); }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** @return False.
      */
    public final @Override boolean isFinal() { return false; }



    /** @return Fractal start.
      */
    public final @Override Symmetry symmetry() { return Symmetry.fractalStart; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final BrecciaCursor cursor;



    final DelimitableCharSequence text; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
