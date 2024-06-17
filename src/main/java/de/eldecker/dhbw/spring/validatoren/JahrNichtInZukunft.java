package de.eldecker.dhbw.spring.validatoren;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


/**
 * <i>Custom Validation Annotation</i> für Bean-Validation definieren, 
 * mit der geprüft wird, dass das Baujahr des Fahrzeugs nicht in der
 * Zukunft liegt. Die Implementierung der eigentlichen Logik ist in
 * der Klasse {@link JahrNichtInZukunftValidator}.
 * <br><br>
 * 
 * Das "@" vor {@code interface} dient der Definition einer Annotation. 
 */
@Documented
@Constraint( validatedBy = JahrNichtInZukunftValidator.class )
@Target( { METHOD, FIELD })
@Retention( RUNTIME )
public @interface JahrNichtInZukunft {
    
    String message() default "Jahreszahl liegt in der Zukunft";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
