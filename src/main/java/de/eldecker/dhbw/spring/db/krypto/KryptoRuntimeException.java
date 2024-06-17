package de.eldecker.dhbw.spring.db.krypto;


/**
 * Eigene ungeprüfte Exception für Fehler während Ver- oder Entschlüsselung
 * von Entity-Attributen. 
 */
@SuppressWarnings("serial")
public class KryptoRuntimeException extends RuntimeException {

    public KryptoRuntimeException( String fehlertext ) {
        
        super( fehlertext );
    }
    
    public KryptoRuntimeException( String fehlertext, Exception ex ) {
        
        super( fehlertext, ex );
    }
    
}
