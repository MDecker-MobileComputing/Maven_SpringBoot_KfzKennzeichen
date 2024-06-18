package de.eldecker.dhbw.spring.model;


/**
 * Farbe von Auto, weil die ein Unfallzeuge beobachtet haben könnte.
 * <br><br>
 *
 * Quelle für beliebteste Auto-Farben:
 * https://www.meinauto.de/news/beliebteste-autofarben-2023-trend-zu-dezent-ungebrochen
 */
public enum KfzFarbeEnum {

    FARBE_UNBEKANNT( "<Farbe unbekannt>" ),

    BLAU   ( "Blauer"    ),
    BRAUN  ( "Brauner"   ),
    GELB   ( "Gelber"    ),
    GRAU   ( "Grauer"    ),
    GRUEN  ( "Grüner"    ),
    ORANGE ( "Orange"    ),
    PINK   ( "Pinker"    ),
    ROT    ( "Roter"     ),
    SCHWARZ( "Schwarzer" ),
    SILBER ( "Silberner" ),
    VIOLET ( "Violetter" ),
    WEISS  ( "Weißer"    );


    /**
     * Farbe für Anzeige vor Automarke, also dekliniert für Nominativ (1. Fall) singular,
     * z.B. "roter" für "ROT" (Verwendung: "roter Porsche).
     */
    private String _dekliniert;


    /**
     * Konstruktor um Anzeigeform der Farbe in deklinierter Form zu setzen
     *
     * @param dekliniert z.B. "roter" für "ROT".
     */
    private KfzFarbeEnum( String dekliniert ) {

        _dekliniert = dekliniert;
    }


    /**
     * Farbe für Anzeige, in deklinierter Form.
     *
     *  @return deklinierte Farbe, z.B. "roter" oder "grüner"
     *          für "roter VW" oder "grüner Porsche".
     */
    @Override
    public String toString() {

        return _dekliniert;
    }


}