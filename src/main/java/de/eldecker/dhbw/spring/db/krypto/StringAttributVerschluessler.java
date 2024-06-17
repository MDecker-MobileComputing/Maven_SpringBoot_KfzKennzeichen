package de.eldecker.dhbw.spring.db.krypto;

import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;



public class StringAttributVerschluessler implements AttributeConverter<String, String> {

    private final static Logger LOG = LoggerFactory.getLogger( StringAttributVerschluessler.class );
    
    /** Bean für Ver- und Entschlüsselung. */
    @Autowired
    private AesHelfer _aesVerschluessler;
    
    
    /**
     * Attribut vor Speichern auf Datenbank verschlüsseln.
     */
    @Override
    public String convertToDatabaseColumn( String stringKlartext ) {

        try {
        
            return _aesVerschluessler.verschluesseln( stringKlartext );
        }
        catch ( InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex ) {

            String fehlertext = "Verschlüsselung von String fehlgeschlagen: " + ex.getMessage();            
            LOG.error( fehlertext );
            throw new KryptoRuntimeException( fehlertext, ex ); 
        }        
    }


    /**
     * Attribute von Datenbank entschlüsseln.
     */
    @Override
    public String convertToEntityAttribute( String stringVerschluesselt ) {

        try {
            
            return _aesVerschluessler.entschluesseln( stringVerschluesselt );
        }
        catch ( InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex ) {

            String fehlertext = "Verschlüsselung von String fehlgeschlagen: " + ex.getMessage();            
            LOG.error( fehlertext );
            throw new KryptoRuntimeException( fehlertext, ex ); 
        }                
    }

}
