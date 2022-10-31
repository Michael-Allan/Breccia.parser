package Breccia.parser;


/** A point in Breccia.
  */
public @DataReflector interface Point extends BodyFractum {


    public @TagName("Bullet") Granum bullet();



    /** The point descriptor, or null if there is none.
      */
    public @TagName("Descriptor") Granum descriptor();



    public @TagName("PerfectIndent") Granum perfectIndent();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** The end of a point.
      */
    public static interface End extends BodyFractum.End {}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
