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
 * <br><br>
 * 
 * Es wird der symmetrische Verschlüsselungsalgorithmus "AES" mit einer Schlüssel-Länge
 * von 128 Bit eingesetzt, siehe {@link AesHelfer}. Der Schlüssel wird in der Datei
 * {@code application.properties} definiert; für eine produktive Anwendung sollte er
 * von der Ausführungsumgebung der Spring-Boot-Anwendung über eine Umgebungsvariable
 * bereitgestellt werden. 
 */
public class KryptoStringAttributeConverter implements AttributeConverter<String, String> {

    private final static Logger LOG = LoggerFactory.getLogger( KryptoStringAttributeConverter.class );
    
    /** Bean für Ver- und Entschlüsselung. */
    @Autowired
    private AesHelfer _aesVerschluessler;
    
    
    /**
     * String-Attribut vor Speichern auf Datenbank verschlüsseln.
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
     * String-Attribut von Datenbank entschlüsseln.
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
