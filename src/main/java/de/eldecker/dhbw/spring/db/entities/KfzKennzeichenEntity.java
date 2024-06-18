package de.eldecker.dhbw.spring.db.entities;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * Ein Objekt dieser Klasse repräsentiert genau ein KFZ-Kennzeichen und
 * referenziert genau ein zugehöriges {@link FahrzeugDatenEntity}-Objekt
 */
@Entity
@Table( name = "KFZ_KENNZEICHEN",
        indexes = {@Index(name = "index_kennzeichen", columnList = "kennzeichen")} )
public class KfzKennzeichenEntity {

    /** Primärschlüssel, wird von JPA gesetzt/verwaltet. */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    /**
     * Deutsche KFZ-Kennzeichen, z.B. "KA X 1234".
     * Das "KA" in diesem Beispiel ist das Unterscheidungszeichen,
     * das "X 1234" ist die Erkennundgsnummer. 
     * <br><br>
     *
     * <b>Aufbau:</b>
     * <ul>
     * <li>Unterscheidungszeichen: 1-3 Großbuchstaben, z.B.
     *     für Landkreis/Stadt, Behörde (z.B. "THW" für
     *     technisches Hilfswerk) oder Militär
     *     ("Y" für Bundeswehr, "X" für  Nato)</li>
     * <li>Erkennungsnummer: ein oder zwei Großbuchstaben,
     *     dann Leerzeichen, dann 1-4 Ziffern.</li>
     * <ul><br>
     * Das "H" am Ende für historische Fahrzeuge (30 Jahre oder
     * älter) ist nicht für die eindeutige idenfizierung erforderlich
     * und darf deshalb nicht enthalten sein, siehe Attribut 
     * {@code historisch}.
     * <br><br> 
     *
     * <b>Max/Min Länge:</b>
     * <ul>
     * <li>Kürzestes KFZ-Kennzeichen: "B A 1"? (Länge: 5)</li>
     * <li>Max-Länge ist auf 8 festgesetzt (mit Leerzeichen dazwischen also 10).</li>
     * </ul><br><br>
     *
     * Das KFZ-Kennzeichen wird mit einem regulären Ausdruck geprüft
     * (Bean Validation).
     */
    @Pattern( regexp = "[A-Z]{1,3} [A-Z]{1,2} [0-9]{1,4}",
              message = "Ungültiges KFZ-Kennzeichen" )
    @Size( min = 5, max = 10, message = "KFZ-Kennzeichen hat ungültige Länge" )
    private String kennzeichen;

    /**
     * Historische Fahrzeuge (Erstzulassung vor über 30 Jahren), haben ein "H"
     * ganz am Ende des KFZ-Kennzeichens. Das KFZ-Kennzeichen ist auch ohne
     * "H" am Ende eindeutig, deswegen wird das "H" nicht im Attribut 
     * {@code kennzeichen} gespeichert.
     */
    private boolean historisch = false;
    
    /**
     * Fahrzeugdaten. Anstelle einer 1:1-Beziehung könnten wir die Attribute
     * in der Klasse {@link FahrzeugDatenEntity} auch hier in die Klasse
     * {@link KfzKennzeichenEntity} übernehmen, aber hier wird wegen
     * "Separation of Concerns" eine separate Klasse verwendet. Außerdem
     * ermöglicht uns dies, Fahrzeuge ohne Kennzeichen zu speichern (z.B.
     * noch nicht zugelassen weil noch nicht verkauft).
     * <br><br>
     *
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
     * Ein KFZ-Kennzeichen ist genau einem Fahrzeughalter zugeordnet (dieser kann aber
     * weitere Fahrzeuge mit anderen KFZ-Kennzeichen besitzen).
     */
    @ManyToOne( fetch = EAGER, cascade = PERSIST )
    @JoinColumn( name = "fahrzeug_halter_fk" )
    private FahrzeugHalterEntity fahrzeugHalter;        


    /**
     * Default-Konstruktor für JPA.
     */
    public KfzKennzeichenEntity() {}


    /**
     * Convience-Konstruktor für nicht-historische Fahrzeuge,
     * also mit {@code historisch=false}.
     */
    public KfzKennzeichenEntity( String               kennzeichen,
                                 FahrzeugDatenEntity  fahrzeugdaten,
                                 FahrzeugHalterEntity fahrzeugHalter 
                               ) {

        this.kennzeichen    = kennzeichen;
        this.fahrzeugDaten  = fahrzeugdaten;
        this.fahrzeugHalter = fahrzeugHalter;
        this.historisch     = false;
    }

    /**
     * Konstruktor, um alle Attribute zu setzen.
     */
    public KfzKennzeichenEntity( String               kennzeichen,
                                 FahrzeugDatenEntity  fahrzeugdaten,
                                 FahrzeugHalterEntity fahrzeugHalter,
                                 boolean              historisch 
                               ) {

        this.kennzeichen    = kennzeichen;
        this.fahrzeugDaten  = fahrzeugdaten;
        this.fahrzeugHalter = fahrzeugHalter;
        this.historisch     = historisch;
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
        
    public boolean isHistorisch() {
        
        return historisch;
    }

    public void setHistorisch( boolean historisch ) {
        
        this.historisch = historisch;
    }


    public FahrzeugDatenEntity getFahrzeugDaten() {

        return fahrzeugDaten;
    }

    public void setFahrzeugDaten( FahrzeugDatenEntity fahrzeugDaten ) {

        this.fahrzeugDaten = fahrzeugDaten;
    }

    public FahrzeugHalterEntity getFahrzeugHalter() {

        return fahrzeugHalter;
    }

    public void setFahrzeugHalter( FahrzeugHalterEntity fahrzeugHalter ) {

        this.fahrzeugHalter = fahrzeugHalter;
    }

    /**
     * Gibt String mit KFZ-Kennzeichen einschl. evtl. "H" am Ende zurück.
     * 
     * @return KFZ-Kennzeichen, z.B. "KA T 123H"
     */
    @Override
    public String toString() {

        if ( !historisch ) {
            
            return kennzeichen;
            
        } else {
            
            return kennzeichen + "H";
        }
    }

}
