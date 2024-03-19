package Breccia.parser;

import java.util.List;


       @TagName("FileLocant") @DataReflector
public interface FileLocant extends Granum {


    public @TagName("Reference") Granum reference();



    /** A list of the qualifiers of this locant.
      */
    public @DataReflector List<String> qualifiers(); // `String` vs. `Enum` for sake of extensibility.



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘FileLocant’.
      */
    public default @Override String tagName() { return "FileLocant"; }}



                                             // Copyright © 2021-2022, 2024  Michael Allan.  Licence MIT.
