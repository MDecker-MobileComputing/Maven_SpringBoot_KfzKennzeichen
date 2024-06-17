package de.eldecker.dhbw.spring.db.entities;

import static jakarta.persistence.GenerationType.AUTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Ein Objekt dieser Klasse repräsentiert den Halter eines KFZs.
 * Ein Halter kann mehrere KFZs halten, also von mehreren Objekten
 * der Klasse {@link KfzKennzeichenEntity} referenziert werden.
 * <br><br>
 *
 *  Wir gehen davon aus, dass eine eeutsche Anschrift hinterlegt
 *  sein muss, deshalb ist der Wert {@code int} für das Attribut
 *  {@code postleitzahl} ausreichend und es wird kein Attribut
 *  für ein Land benötigt.
 */
@Entity
@Table( name = "FAHRZEUG_HALTER" )
public class FahrzeugHalterEntity {

    /** Primärschlüssel, wird von JPA gesetzt/verwaltet. */
    @Id
    @GeneratedValue( strategy = AUTO )
    private Long id;

    /** Anrede, z.B. "Herr" oder "Frau Dr." */
    private String anrede;

    /** Vorname, z.B. "Herbert" */
    private String vorname;

    /** Vorname, z.B. "Müller-Lüdenscheidt" */
    private String nachname;

    /** Anschrift mit Hausnummer (in Deutschland), z.B. "Hauptstraße 23B" */
    private String anschrift;

    /**
     * Fünfstellige Postleitzahl in Deutschland; die evtl. führende
     * 0 (z.B. "04103" für einen Teil von Leipzig) muss ggf. bei der
     * Anzeige ergänzt werden.
     */
    private int plz;

    /** Wohnort in Deutschland, z.B. "Hamburg". */
    private String wohnort;


    /**
     * Default-Konstruktor für JPA.
     */
    public FahrzeugHalterEntity() {}

    /**
     * Konstruktor, um alle Attribute zu setzen.
     */
    public FahrzeugHalterEntity( String anrede, String vorname, String nachname,
                                 String anschrift, int plz, String wohnort ) {

       this.anrede    = anrede;
       this.vorname   = vorname;
       this.nachname  = nachname;
       this.anschrift = anschrift;
       this.plz       = plz;
       this.wohnort   = wohnort;
    }

    public Long getId() {

        return id;
    }
    
        
    public String getAnrede() {
        
        return anrede;
    }

    public void setAnrede( String anrede ) {
        
        this.anrede = anrede;
    }

    public String getVorname() {
        
        return vorname;
    }

    public void setVorname( String vorname ) {
        
        this.vorname = vorname;
    }

    public String getNachname() {
        
        return nachname;
    }

    public void setNachname( String nachname ) {
        
        this.nachname = nachname;
    }

    public String getAnschrift() {
        
        return anschrift;
    }

    public void setAnschrift( String anschrift ) {
        
        this.anschrift = anschrift;
    }

    public int getPlz() {
        
        return plz;
    }

    public void setPlz( int plz ) {
        
        this.plz = plz;
    }

    public String getWohnort() {
        
        return wohnort;
    }

    public void setWohnort( String wohnort ) {
        
        this.wohnort = wohnort;
    }

    @Override
    public String toString() {
        
        return anrede + " " + vorname + " " + nachname + " aus " + wohnort;
    }
}
