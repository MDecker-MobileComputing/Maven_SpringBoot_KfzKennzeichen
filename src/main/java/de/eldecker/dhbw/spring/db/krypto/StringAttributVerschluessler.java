package de.eldecker.dhbw.spring.db.krypto;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;

public class StringAttributVerschluessler implements AttributeConverter<String, String> {

    @Autowired
    private AesHelfer _aesVerschluessler;
    
    
    /**
     * Attribut vor Speichern auf Datenbank verschlüsseln.
     */
    @Override
    public String convertToDatabaseColumn( String stringKlartext ) {

        return _aesVerschluessler.verschluesseln( stringKlartext );
    }


    /**
     * Attribute von Datenbank entschlüsseln.
     */
    @Override
    public String convertToEntityAttribute( String stringVerschluesselt ) {

        return _aesVerschluessler.entschluesseln( stringVerschluesselt );
    }

}
