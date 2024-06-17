package de.eldecker.dhbw.spring.db.krypto;

import static jakarta.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.util.Base64;
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
    
    /** Genauer Bezeichner Verschlüsselungsalgorithmus inkl. Block-Modus und Padding. */
    private static final String KRYPTO_ALGO_NAME = "AES/CBC/PKCS5Padding"; 
    
    /**
     * Symmetrischer Schlüssel (128 Bit) als Hexadezimalziffer mit 32 Buchstaben,
     * aus Datei {@code application.properties}. Da es sich bei AES um ein
     * symmetrisches Verfahren handelt, wird dieses Schlüssel sowohl für die
     * Ver- als auch die Entschlüsselung benötigt. 
     * Default-Wert ist ein leerer String.
     */
    @Value( "${de.eldecker.kfz-kennzeichen.krypto.schluessel:}" )
    private String _schluesselHex;
    
    /** Objekt für Ver-/Entschlüsselung. */
    private Cipher _aesCipher = null;
    
    /** Symmetrischer Schlüssel */
    private SecretKeySpec _secretKey = null;

    /** Objekt für die Umwandlung eines byte-Arrays nach String mit Base64. */
    private Encoder _base64Encoder = Base64.getEncoder();
    
    
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
     * 
     * @throws InvalidKeyException Fehler bei Testverschlüsselung: Kryptographischer 
     *                             Schlüssel nicht in Ordnung
     * 
     * @throws BadPaddingException Fehler bei Testverschlüsselung: Invalide Füllbytes
     *  
     * @throws IllegalBlockSizeException Fehler bei Testverschlüsselung wegen falscher
     */
    @PostConstruct
    public void initialisierung() 
            throws NoSuchAlgorithmException, 
                   NoSuchPaddingException, 
                   InvalidKeyException, 
                   IllegalBlockSizeException, 
                   BadPaddingException {                     
       
        if ( _schluesselHex.length() != 32 ) {
            
            throw new KryptoRuntimeException( "Hex-Zahl mit Schlüssel hat falsche Länge." ); 
        }
        
        _aesCipher = Cipher.getInstance( KRYPTO_ALGO_NAME );
        
        final byte[] keyBytes = parseHexBinary( _schluesselHex );
        
        _secretKey = new SecretKeySpec( keyBytes, "AES" );
                
        // Testverschlüsselung
        final String testString = "Lorem Ipsum";

        final String testStringVerschluesselt = verschluesseln( testString );        
        
        LOG.info( "testString verschlüsselt: " + testStringVerschluesselt );
        
        final String testStringEntschluesselt = entschluesseln( testStringVerschluesselt );                        
        if ( testString.equals( testStringEntschluesselt ) == false ) {
         
            throw new KryptoRuntimeException( 
                    "Unerwarteter Ergebnis-String für Testverschlüsseln: " + testStringEntschluesselt 
                   );
        }
        LOG.info( "Test-String erfolgreich entschlüsselt: " + testStringEntschluesselt );
        
        LOG.info( "Verschlüsselungs-Algo initialisiert: " + _aesCipher );
    }
    
        
    public String verschluesseln( String stringKlartext ) 
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        
        LOG.info( "SecretKey für Verschlüsselung: " + _secretKey );
        _aesCipher.init( ENCRYPT_MODE, _secretKey );
        
        byte[] klartextBytes = stringKlartext.getBytes();
        
        byte[] encryptedBytes = _aesCipher.doFinal( klartextBytes );
                 
        return _base64Encoder.encodeToString( encryptedBytes );
    }
    
    
    public String entschluesseln( String stringVerschluesselt ) 
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        
        LOG.info( "SecretKey für Entschlüsselung: " + _secretKey );
        _aesCipher.init( DECRYPT_MODE, _secretKey );
        
        byte[] encryptedBytes = stringVerschluesselt.getBytes();
        
        byte[] decryptedBytes = _aesCipher.doFinal( encryptedBytes );
                        
        return new String( decryptedBytes );
    }
    
}
