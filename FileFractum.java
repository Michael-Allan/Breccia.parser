package Breccia.parser;


/** A file fractum in Breccia.  This is an initial state.
  *
  * <p>In the case of a headless file fractum, both the component list and text will be empty
  * and the line number will reflect the position of the body alone.</p>
  *
  */   @TagName("FileFractum") @DataReflector
public interface FileFractum extends Fractum, Markup {


   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns 1: a file fractum begins with the first line of its file.
      */
    public @Override int lineNumber();



    /** The default implementation returns ‘FileFractum’.
      */
    public default @Override String tagName() { return "FileFractum"; }



   // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns true.
      */
    public default @Override boolean isInitial() { return true; }



    /** The default implementation returns {@linkplain Typestamp#fileFractum fileFractum}.
      */
    public default @Override int typestamp() { return Typestamp.fileFractum; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a file fractum.  This is a final state.
      */
    public static interface End extends Fractum.End {


       // ━━━  P a r s e   S t a t e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


       /** Returns true.
         */
        public default @Override boolean isFinal() { return true; }



        /** The default implementation returns {@linkplain Typestamp#fileFractumEnd fileFractumEnd}.
          */
        public default @Override int typestamp() { return Typestamp.fileFractumEnd; }}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
