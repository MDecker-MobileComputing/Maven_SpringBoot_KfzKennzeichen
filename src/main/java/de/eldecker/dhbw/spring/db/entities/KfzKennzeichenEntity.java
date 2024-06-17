package de.eldecker.dhbw.spring.db.entities;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Pattern;


/**
 * Ein Objekt dieser Klasse repräsentiert genau ein KFZ-Kennzeichen und
 * referenziert genau ein zugehöriges {@link FahrzeugDatenEntity}-Objekt
 */
@Entity
@Table( name = "KFZ_KENNZEICHEN" )
public class KfzKennzeichenEntity {

    /** Primärschlüssel, wird von JPA gesetzt/verwaltet. */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    /**
     * KFZ-Kennzeichen, z.B. "KA X 1234".
     * <br><br>
     * <b>Aufbau:</b>
     * <ul>
     * <li>Unterscheidungszeichen: 1-3 Großbuchstaben, z.B.
     *     für Landkreis/Stadt, Behörde (z.B. "THW" für
     *     technisches Hilfswerk) oder Militär 
     *     ("Y" für Bundeswehr, "X" für  Nato)</li>
     * <li>Erkennungsnummer: ein oder zwei Großbuchstaben,
     *     dann Leerzeichen, dann 1-4 Ziffern.</li>
     * <li>Optional: "H" für historische Fahrzeuge.</li>
     * <ul><br><br>
     *
     * Das KFZ-Kennzeichen wird mit einem regulären Ausdruck geprüft
     * (Bean Validation).
     */
    @Pattern( regexp = "[A-Z]{1,3} [A-Z]{1,2} [0-9]{1,4}(H)?",
              message = "Ungültiges KFZ-Kennzeichen" )
    private String kennzeichen;

    /**
     * Anmerkungen zu den Annotationen:
     * <ul>
     * <li>Fetch-Type {@code EAGER} in Annotation {@code OnetoOne} ist eigentlich Default.</li>
     * <li>Für Annotation {@code JoinColumn} ist kein Attribut {@code referencedColumnName}
     *     erforderlich, da die andere Referenz nur ein Primärschlüsselattribut hat.</li>
     * <li>Mit {@code cascade = PERSIST} wird gewährleistet, dass das referenzierte
     *     {@link FahrzeugDatenEntity}-Objekt ggf. vorher auch gespeichert wird.</li>
     * </ul>
     */
    @OneToOne( fetch = EAGER, cascade = PERSIST )
    @JoinColumn( name = "fahrzeug_daten_fk" )
    private FahrzeugDatenEntity fahrzeugDaten;


    /**
     * Default-Konstruktor für JPA.
     */
    public KfzKennzeichenEntity() {}


    /**
     * Konstruktor, um alle Attribute zu setzen.
     */
    public KfzKennzeichenEntity( String kennzeichen, FahrzeugDatenEntity fahrzeugdaten ) {

        this.kennzeichen   = kennzeichen;
        this.fahrzeugDaten = fahrzeugdaten;
    }

    public Long getId() {

        return id;
    }

    public String getKennzeichen() {

        return kennzeichen;
    }

    public void setKennzeichen( String kennzeichen ) {

        this.kennzeichen = kennzeichen;
    }

    public FahrzeugDatenEntity getFahrzeugDaten() {

        return fahrzeugDaten;
    }

    public void setFahrzeugDaten( FahrzeugDatenEntity fahrzeugDaten ) {

        this.fahrzeugDaten = fahrzeugDaten;
    }

    public String toString() {

        return "KFZ-Kennzeichen: " + kennzeichen + " - " + fahrzeugDaten;
    }

}
