package de.eldecker.dhbw.spring.db.entities;

import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.FARBE_UNBEKANNT;
import static de.eldecker.dhbw.spring.model.KfzMarkeEnum.MARKE_UNBEKANNT;

import static jakarta.persistence.GenerationType.AUTO;

import de.eldecker.dhbw.spring.model.KfzFarbeEnum;
import de.eldecker.dhbw.spring.model.KfzMarkeEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Min;


/**
 * Entity mit Daten zu einem KFZ.
 */
@Entity
@Table( name = "FAHRZEUG_DATEN" )
public class FahrzeugDatenEntity {

    /** Primärschlüssel, wird von JPA gesetzt/verwaltet. */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    /** Automarke, z.B. "VW" oder "SEAT". */
    private KfzMarkeEnum marke = MARKE_UNBEKANNT;

    /** (Haupt-)Farbe des Autos, z.B. "schwarz". */
    private KfzFarbeEnum farbe = FARBE_UNBEKANNT;

    /** 
     * Fahrzeug-Identifizierungsnummer (FIN), 17 Stellen alphanumerisch.
     * <br><br> 
     * 
     * <b>Referenzen:</b>
     * <ul>
     * <li><a href="https://www.adac.de/rund-ums-fahrzeug/auto-kaufen-verkaufen/kfz-zulassung/fahrzeugidentifikationsnummer/">Erklärung FIN</a></li>
     * <li><a href="https://randommer.io/generate-vin">Zufallsgenerator für FIN</a></li>
     * </ul> 
     */
    private String fin;

    /**
      * Erstes Auto von Karl Benz wurde 1885 gebaut und fuhr 1886.
      */
    @Min( value = 1885, message = "Vor 1885 wurden keine Autos gebaut" )
    private int baujahr;


    /**
     * Default-Konstruktor für JPA.
     */
    public FahrzeugDatenEntity() {}
    

    /**
     * Konstruktor, um alle Attribute zu setzen.
     */
    public FahrzeugDatenEntity( KfzMarkeEnum marke,
                                KfzFarbeEnum farbe,
                                String fin,
                                int baujahr ) {
        this.marke = marke;
        this.farbe = farbe;
        this.fin   = fin;

        this.baujahr = baujahr;
    }

    public Long getId() {

        return id;
    }

    public KfzMarkeEnum getMarke() {

        return marke;
    }

    public void setMarke( KfzMarkeEnum marke ) {

        this.marke = marke;
    }

    public KfzFarbeEnum getFarbe() {

        return farbe;
    }

    public void setFarbe( KfzFarbeEnum farbe ) {

        this.farbe = farbe;
    }

    public String getFin() {
        return fin;
    }

    public void setFin( String fin ) {

        this.fin = fin;
    }

    public int getBaujahr() {

        return baujahr;
    }

    public void setBaujahr( int baujahr ) {

        this.baujahr = baujahr;
    }


    /**
     * Gibt String-Repräsentation des Objekts zurück.
     * 
     * @return String mit Marke, Farbe und FIN.
     */
    @Override
    public String toString() {

        return marke + " (" + farbe + ") mit FIN=" + fin;
    }

}
