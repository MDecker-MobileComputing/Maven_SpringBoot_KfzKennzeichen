package de.eldecker.dhbw.spring.validatoren;

import static java.time.LocalDate.now;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


/**
 * Implementierung der Logik für die Custom Validation Annotation {@code JahrNichtInZukunft}.
 * 
 * @param <JahrNichtInZukunft> Zugehörige Annotation
 * 
 * @param <Integer> Typ von Wert, der mit Annotation überprüft wird
 */
public class JahrNichtInZukunftValidator implements ConstraintValidator<JahrNichtInZukunft, Integer> {

    /**
     * Methode mit Logik für die Validierung, die durch die Annotation durchgeführt werden soll:
     * {@code jahreszahl} darf nicht größer als aktuelle Jahreszahl sein.
     * 
     * @param jahreszahl Jahres (vierstellig), die überprüft werden soll
     *  
     * @parm Context Kontext für Validierung 
     * 
     * @return {@code true} wenn {@code jahreszahl} den Wert {@code null} oder einen {@code int}-Wert
     *         hat, der nicht größer als die aktuelle vierstellige Jahreszahl (z.B. {@code 2024}) ist.
     */
    @Override
    public boolean isValid( Integer jahreszahl, ConstraintValidatorContext context ) {
        
        if ( jahreszahl == null ) {
            
            return true;
        }
        
        final int jahrAktuell = now().getYear();
        return jahreszahl <= jahrAktuell;
    }

}
