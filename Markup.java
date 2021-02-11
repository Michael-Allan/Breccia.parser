package Breccia.parser;

import java.util.Iterator;


/** A parsed component of markup.
  */
public interface Markup extends Iterable<Markup> {


    /** Whether this markup has parsed components.  For markup with parsed components, the whole of its
      * text is parsed and modelled by components given through the {@linkplain #iterator() iterator}.
      * For markup without parsed components, the text is modelled only as a flat character sequence.
      *
      * <p>Warning: the default implementation of this method returns
      * `{@linkplain #iterator() iterator}().hasNext()`.  If the default implementation is called
      * while an iteration is in progress, then that iteration may yield unexpected results.</p>
      *
      *     @see text()
      */
    public default boolean isComposite() { return iterator().hasNext(); }



    public int lineNumber();



    /** The tag name used in X-Breccia for this markup.
      *
      *     @see <a href='https://www.w3.org/TR/xml/#sec-starttags'>
      *       Start-tags, end-tags, and empty-element tags</a>
      *     @see <a href='http://reluk.ca/project/Breccia/XML/language_definition.brec'>
      *       X-Breccia language definition</a>
      *     @see ParseState#typestamp()
      */
    public String tagName();



    public CharSequence text();



   // ━━━  I t e r a b l e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns an ordered iterator over the parsed components of this markup.
      * The iterator presents the components in linear order.
      *
      * <p>Warning: successive calls may return the same iterator instance,
      * resetting it on each call.  Callers must therefore ensure that each
      * iteration ends before the next begins, or the results may be unexpected.</p>
      *
      *     @see isComposite()
      */
    public Iterator<Markup> iterator(); }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
