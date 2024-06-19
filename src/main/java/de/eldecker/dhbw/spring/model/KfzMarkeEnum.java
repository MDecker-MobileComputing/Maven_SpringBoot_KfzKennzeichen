package de.eldecker.dhbw.spring.model;

import java.util.regex.Pattern;


/**
 * Für Listen mit Automarken siehe z.B.:
 * <ul>
 * <li>https://www.autoscout24.de/</li>
 * <li>https://www.autohaus24.de/auto-lexikon/automarken</li>
 * </ul>
 * <br>
 *
 * Es wird die Automarke statt dem Hersteller als Attribut auf oberste Ebene
 * eines Fahrzeugs gespeichert, weil diese von einem Zeugen bei einem Unfalls
 * am ehesten erkannt wird.
 * <br><br>
 *
 * Es gibt Hersteller, die mehrere marken haben, z.B. "VW" bringt unter
 * "VW" selbst Autos heraus (Modelle z.B. Passat, Golf, Polo), aber
 * Seat gehört auch zum VW-Konzert.
 */
public enum KfzMarkeEnum {

    MARKE_UNBEKANNT,

    AUDI,
    BMW,
    FERRARI,
    FIAT,
    FORD,
    LAMBORGHINI,
    MERCEDES,
    OPEL,
    PORSCHE,
    SEAT,
    VW;

    
    /** 
     * Regulärer Ausdruck für Vokalerkennung.
     * Da die Bezeichner für die Elemente im Enum nur aus Großbuchstaben bestehen
     * dürfen, ist es ausreichend nur auf Großbuchstaben zu prüfen.
     * Wenn ein Bezeichner für eine Automarke keinen einzigen Vokal enthält, dann
     * wird er als Abkürzung behandelt, die unverändert zurückgegeben wird. 
     */
    private static final Pattern VOKAL_MATCHER = Pattern.compile(".*[AEIOU].*");

    
    /**
     * Liefert String für Anzeige der Automarke zurück, z.B. "Porsche" (statt "PORSCHE"),
     * aber "BWM" (Abkürzungen, d.h. Markennamen ohne Vokal, werden unverändert zurückgegeben).
     * 
     * @return String zur Anzeige 
     */
    @Override
    public String toString() {
        
        final String nameKfzMarke = name();
        
        if ( !VOKAL_MATCHER.matcher( nameKfzMarke ).matches() ) {
            
            return nameKfzMarke;
            
        } else {
            
            return nameKfzMarke.substring( 0, 1 ) +  
                   nameKfzMarke.substring( 1    ).toLowerCase();
        }
    }
    
}
