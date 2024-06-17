package de.eldecker.dhbw.spring.db.krypto;

import static jakarta.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


/**
 * Bean-Klasse für symmetrische Verschlüsselung mit AES128. 
 */
@Component
public class AesHelfer {

    private final static Logger LOG = LoggerFactory.getLogger( AesHelfer.class );
    
    private static final String KRYPTO_ALGO_NAME = "AES/CBC/PKCS5Padding"; 
    
    /**
     * Symmetrischer Schlüssel (128 Bit) als Hexadezimalziffer mit 32 Buchstaben,
     * aus Datei {@code application.properties}. Da es sich bei AES um ein
     * symmetrisches Verfahren handelt, wird dieses Schlüssel sowohl für die
     * Ver- als auch die Entschlüsselung benötigt. 
     */
    @Value( "${de.eldecker.kfz-kennzeichen.krypto.schluessel}" )
    private String _schluesselHex;
    
    private Cipher _aesCipher = null;
    
    private SecretKeySpec _secretKey = null;
    
    private Encoder _base64Encoder = Base64.getEncoder();
    
    private Decoder _base64Decoder = Base64.getDecoder();
    
    
    /**
     * Diese Methode wird unmittelbar nach Erzeugung der Bean aufgerufen,
     * aber erst, wenn der Konstruktor abgearbeitet wurde, so dass der
     * aus der Datei {@code application.properties} eingelesene symmetrische
     * Schlüssel zur Verfügung steht.
     * <br><br>
     * 
     * Wenn diese Methode eine Exception wirft, dann bricht das Programm ab.
     * Das ist beabsichtigt, weil die Anwendung nicht sinnvoll funktionieren
     * kann wenn der Zugriff auf die verschlüsselten Datenbankspalten nicht
     * möglich ist.
     * 
     * @throws NoSuchAlgorithmException Verschlüsselungsalgo nicht gefunden
     * 
     * @throws NoSuchPaddingException Padding für Verschlüsselung nicht gefunden
     */
    @PostConstruct
    public void initialisierung() throws NoSuchAlgorithmException, NoSuchPaddingException {
        
       
        _aesCipher = Cipher.getInstance( KRYPTO_ALGO_NAME );
        
        final byte[] keyBytes = parseHexBinary( _schluesselHex );
        
        _secretKey = new SecretKeySpec( keyBytes, "AES" );
        
        
        // Testverschlüsselung vornehmen?
        
        LOG.info( "Verschlüsselungs-Algo initialisiert: " + _aesCipher );
    }
    
    public String verschluesseln( String stringKlartext ) 
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        
        _aesCipher.init( ENCRYPT_MODE, _secretKey );
        
        byte[] klartextBytes = stringKlartext.getBytes();
        
        byte[] encryptedBytes = _aesCipher.doFinal( klartextBytes );
        
        _base64Encoder.encodeToString( encryptedBytes ); 
        
        return _base64Encoder.encodeToString( encryptedBytes );
    }
    
    public String entschluesseln( String stringVerschluesselt ) {
        
        if ( stringVerschluesselt.startsWith( "abc" ) ) {

            return stringVerschluesselt.substring( 3 );

        } else {

            return stringVerschluesselt;
        }
    }
    
}
