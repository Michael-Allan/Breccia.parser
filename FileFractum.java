package Breccia.parser;


/** A file fractum in Breccia.  This is an initial state.
  *
  * <p>In the case of a headless file fractum, the text and component list will be empty
  * and the line number will reflect the position of the body alone.</p>
  */
public @DataReflector interface FileFractum extends Fractum, Markup {


    /** @return The file descriptor, or null if there is none.
      */
    public FileDescriptor descriptor();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


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
        public default @Override int typestamp() { return Typestamp.fileFractumEnd; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A file descriptor.
      */
    public static @DataReflector interface FileDescriptor extends Markup {


       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        /** The default implementation returns ‘FileDescriptor’.
          */
        public default @Override String tagName() { return "FileDescriptor"; }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
