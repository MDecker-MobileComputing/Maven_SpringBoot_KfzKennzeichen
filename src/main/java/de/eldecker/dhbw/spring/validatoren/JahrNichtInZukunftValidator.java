package de.eldecker.dhbw.spring.validatoren;

import static java.time.LocalDate.now;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


/**
 * Implementierung der Logik für die Custom Validation Annotation {@code JahrNichtInZukunft}.
 */
public class JahrNichtInZukunftValidator implements ConstraintValidator<JahrNichtInZukunft, Integer> {

    /**
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
