package Breccia.parser;


/** A comment appender in Breccia.
  *
  */   @TagName("CommentAppender") @DataReflector
public interface CommentAppender extends CommentaryHolder {


   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘CommentAppender’.
      */
    public default @Override String tagName() { return "CommentAppender"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
