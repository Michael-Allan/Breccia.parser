package Breccia.parser;


/** An comment appender in Breccia.
  *
  */   @TagName("CommentAppender") @DataReflector
public interface CommentAppender extends CommentaryHolder {


   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘CommentAppender’.
      */
    public default @Override String tagName() { return "CommentAppender"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
