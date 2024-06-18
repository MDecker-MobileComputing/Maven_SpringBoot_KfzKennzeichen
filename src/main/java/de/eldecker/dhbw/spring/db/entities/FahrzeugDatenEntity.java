package de.eldecker.dhbw.spring.db.entities;

import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.FARBE_UNBEKANNT;
import static de.eldecker.dhbw.spring.model.KfzMarkeEnum.MARKE_UNBEKANNT;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

import de.eldecker.dhbw.spring.model.KfzFarbeEnum;
import de.eldecker.dhbw.spring.model.KfzMarkeEnum;
import de.eldecker.dhbw.spring.validatoren.JahrNichtInZukunft;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;


/**
 * Entity mit Daten zu einem Fahrzeug. Einem KFZ-Kennzeichen ist immer genau ein Objekt
 * dieser Klasse zugeordnet. Es kann aber sein, dass ein Objekt dieser Klasse noch kein
 * KFZ-Kennzeichen hat (z.B. Auto, das noch nicht zugelassen ist, weil es noch beim
 * Händler steht).
 */
@Entity
@Table( name = "FAHRZEUG_DATEN" )
public class FahrzeugDatenEntity {

    /** Primärschlüssel, wird von JPA gesetzt/verwaltet. */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    /**
     * (Haupt-)Farbe des Autos, z.B. "schwarz".
     * <br><br>
     *
     * Der Datentyp des Attributs ist ein Enum, es gibt keine {@code Enumerated}-Annotation,
     * deshalb wird der zugewiesene Wert auf den Index (0-basiert) des Elements im Enum
     * abgebildet (Default-Verhalten: ORDINAL). Wenn aber die Elemente in {@link KfzFarbeEnum}
     * geändert werden, dann werden die Index-Werte beim Einlesen auf andere Farben
     * abgebildet. Bei Verwendung von H2 als Datenbank wird für dieses Attribut eine Spalte
     * vom Typ {@code TINYINT} angelegt.
     */
    private KfzFarbeEnum farbe = FARBE_UNBEKANNT;

    /**
     * Automarke, z.B. "VW" oder "SEAT".
     * <br><br>
     *
     * Wegen {@code Enumerated( STRING )} werden die Enum-Elemente nicht auf Index-Werte
     * abgebildet, sondern auf Strings; dadurch ergibt sich kein Problem, wenn in
     * {@link KfzMarkeEnum} weitere Enum-Elemente hinzukommen, z.B. weil neue Auto-Marken
     * erscheinen (z.B. für E-Autos). Bei Verwendung von H2 als Datenbank wird für dieses
     * Attribut eine Spalte vom Typ {@code ENUM} angelegt.
     */
    @Enumerated( STRING )
    private KfzMarkeEnum marke = MARKE_UNBEKANNT;

    /**
     * Fahrzeug-Identifizierungsnummer (FIN), 17 Stellen alphanumerisch.
     * <br><br>
     *
     * <b>Referenzen:</b>
     * <ul>
     * <li><a href="https://bit.ly/3xmm8OM">Erklärung FIN (ADAC)</a></li>
     * <li><a href="https://randommer.io/generate-vin">Zufallsgenerator für FIN</a></li>
     * </ul>
     */
    @Pattern( regexp = "[A-Z0-9]{17}",
              message = "Ungültiges Format für Fahrzeug-Identifizierungsnummer (FIN)" )
    private String fin;

    /**
     * Baujahrs (nicht Zulassungsjahr) des Fahrzeugs.
     * <br><br>
     *
     * Erstes Auto von Karl Benz wurde 1885 gebaut (und fuhr 1886), deshalb
     * wird mit der Annotation {@code Min} festgelegt, dass die Jahreszahl
     * nicht vor 1885 sein darf.
     * <br><br>
     *
     * Mit dem <i>Custom Bean Validator</i> {@code JahrNichtInZukunft} wird
     * sichergestellt, dass kein Jahr in der Zukunft gesetzt wird.
     */
    @Min( value = 1885,
          message = "Vor 1885 wurden keine Autos gebaut" )
    @JahrNichtInZukunft( message = "Das Baujahr des Fahrzeugs liegt in der Zukunft" )
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
     * Gibt String-Repräsentation des Objekts zurück, kann zur Anzeige 
     * auf UI verwendet werden.
     *
     * @return String mit Marke, Farbe, Baujahr und FIN.
     *         Beispiel: "Grüner Porsche, Baujahr 2015, FIN=WP0AA29997BEXXUMN."
     */
    @Override
    public String toString() {

        return String.format( "%s %s, Baujahr %d, FIN: %s.", 
                              farbe, marke, baujahr, fin );
    }

}
