package de.eldecker.dhbw.spring.model;


/**
 * Für Listen mit Automarken siehe:
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
    VW    
}
