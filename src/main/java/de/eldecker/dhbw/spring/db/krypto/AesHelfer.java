package de.eldecker.dhbw.spring.db.krypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Bean-Klasse für symmetrische Verschlüsselung mit AES128. 
 */
@Component
public class AesHelfer {

    /**
     * Symmetrischer Schlüssel (128 Bit) als Hexadezimalziffer mit 32 Buchstaben,
     * aus Datei {@code application.properties}. Da es sich bei AES um ein
     * symmetrisches Verfahren handelt, wird dieses Schlüssel sowohl für die
     * Ver- als auch die Entschlüsselung benötigt. 
     */
    @Value( "${de.eldecker.kfz-kennzeichen.krypto.schluessel}" )
    private String _schluesselHex;
    
    
    public String verschluesseln( String stringKlartext ) {
        
        return "abc" + stringKlartext;
    }
    
    public String entschluesseln( String stringVerschluesselt ) {
        
        if ( stringVerschluesselt.startsWith( "abc" ) ) {

            return stringVerschluesselt.substring( 3 );

        } else {

            return stringVerschluesselt;
        }
    }
    
}
