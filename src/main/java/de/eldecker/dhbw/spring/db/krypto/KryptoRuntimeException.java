package de.eldecker.dhbw.spring.db.krypto;


@SuppressWarnings("serial")
public class KryptoRuntimeException extends RuntimeException {

    public KryptoRuntimeException( String fehlertext ) {
        
        super( fehlertext );
    }
    
    public KryptoRuntimeException( String fehlertext, Exception ex ) {
        
        super( fehlertext, ex );
    }
    
}
