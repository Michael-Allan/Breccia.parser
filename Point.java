package Breccia.parser;


/** A point in Breccia.
  */
public @DataReflector interface Point extends BodyFractum {


    public @TagName("Bullet") Markup bullet();



    /** The point descriptor, or null if there is none.
      */
    public @TagName("Descriptor") Markup descriptor();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a point.
      */
    public static interface End extends BodyFractum.End {}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
