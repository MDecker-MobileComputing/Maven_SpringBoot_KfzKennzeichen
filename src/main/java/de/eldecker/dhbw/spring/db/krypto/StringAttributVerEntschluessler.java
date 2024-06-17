package de.eldecker.dhbw.spring.db.krypto;

import java.security.GeneralSecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;


/**
 * {@code AttributeConverter}, um bestimmte Entity-Attribute vor Speichern auf Datenbank
 * zu verschlüsseln und beim Lesen wieder zu entschlüsseln. Den zu verschlüsselnden 
 * Attributen ist mit der Annotation {@code Convert} dieser {@code AttributeConverter}
 * zuzuweisen.
 */
public class StringAttributVerEntschluessler implements AttributeConverter<String, String> {

    private final static Logger LOG = LoggerFactory.getLogger( StringAttributVerEntschluessler.class );
    
    /** Bean für Ver- und Entschlüsselung. */
    @Autowired
    private AesHelfer _aesVerschluessler;
    
    
    /**
     * Attribut vor Speichern auf Datenbank verschlüsseln.
     * 
     * @param stringKlartext Attributwert im Klartext
     * 
     * @return Chiffre von {@code stringKlartext}
     */
    @Override
    public String convertToDatabaseColumn( String stringKlartext ) {

        try {
        
            return _aesVerschluessler.verschluesseln( stringKlartext );
        }
        catch ( GeneralSecurityException ex ) {

            String fehlertext = "Verschlüsselung von String fehlgeschlagen: " + ex.getMessage();            
            LOG.error( fehlertext );
            throw new KryptoRuntimeException( fehlertext, ex ); 
        }        
    }


    /**
     * Attribute von Datenbank entschlüsseln.
     * 
     * @param stringVerschluesselt Chiffre für Attributwert
     * 
     * @return Klartext von {@code stringVerschluesselt}
     */
    @Override
    public String convertToEntityAttribute( String stringVerschluesselt ) {

        try {
            
            return _aesVerschluessler.entschluesseln( stringVerschluesselt );
        }
        catch ( GeneralSecurityException ex ) {

            String fehlertext = "Verschlüsselung von String fehlgeschlagen: " + ex.getMessage();            
            LOG.error( fehlertext );
            throw new KryptoRuntimeException( fehlertext, ex ); 
        }                
    }

}
