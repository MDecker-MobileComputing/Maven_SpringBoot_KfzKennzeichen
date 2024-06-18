package de.eldecker.dhbw.spring.db.krypto;

import static jakarta.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


/**
 * Bean-Klasse für symmetrische Verschlüsselung mit dem Algorithmus "AES128".
 */
@Component
public class AesHelfer {

    private final static Logger LOG = LoggerFactory.getLogger( AesHelfer.class );

    /**
     * Genauer Bezeichner Verschlüsselungsalgorithmus:
     * <ul>
     * <li>AES: Symmetrischer Verschlüsselungsalgorithmus, Block-Chiffre</li>
     * <li>ECB: Blockmodus</li>
     * <li>PKCS5Padding: Algo für Füll-Bytes, um Block bei Bedarf aufzufüllen.</li>
     * </ul>
     */
    private static final String KRYPTO_ALGO_NAME = "AES/ECB/PKCS5Padding";

    /**
     * Anzahl der Zufallszeichen, die vor den zu verschlüsselnden String
     * gestellt werden, damit identische Klartext nicht zu identischen
     * Chiffraten führen und ein Angreifer daraus Rückschlüsse ziehen kann.
     */
    private static final int ANZAHL_ZUFALLSZEICHEN = 3;

    /**
     * Symmetrischer Schlüssel (128 Bit) als Hexadezimalziffer mit 32 Buchstaben,
     * aus Datei {@code application.properties}. Da es sich bei AES um ein
     * symmetrisches Verfahren handelt, wird dieses Schlüssel sowohl für die
     * Ver- als auch die Entschlüsselung benötigt.
     * Default-Wert ist ein leerer String.
     */
    @Value( "${de.eldecker.kfz-kennzeichen.krypto.schluessel:}" )
    private String _schluesselHex;
    
    /** 
     * Wenn {@code true}, dann wird den String vor Verschlüsselung ein
     * Zufalls-String vorangestellt, damit gleiche Klartexte nicht zur
     * gleichen Chiffre führen. Per Default ausgeschaltet.
     */
    @Value( "${de.eldecker.kfz-kennzeichen.krypto.versalzung:false}" )
    private boolean _versalzung;
    
    /** Objekt für Ver-/Entschlüsselung */
    private Cipher _aesCipher = null;

    /** Objekt mit symmetrischem Schlüssel */
    private SecretKeySpec _secretKey = null;

    /** Objekt für die Umwandlung von {@code byte[]} nach String mit Base64-Kodierung */
    private Encoder _base64Encoder = Base64.getEncoder();

    /** Objekt für die (Rück-)Umwandlung von einem String nach {@code byte[]} mit Base64-Kodierung */
    private Decoder _base64Decoder = Base64.getDecoder();

    /** Sicherer Zufallsgenerator für Erzeugung zufälliger Zeichenketten. */
    private SecureRandom _secureRandom = new SecureRandom();


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
     * <br><br>
     *
     * Es wird auch ein Test-String ver- und wieder entschlüsselt.
     *
     * @throws NoSuchAlgorithmException Verschlüsselungsalgo nicht gefunden
     *
     * @throws GeneralSecurityException Fehler bei Ver- oder Entschlüsselung
     */
    @PostConstruct
    public void initialisierung() throws NoSuchAlgorithmException, GeneralSecurityException {

        if ( _schluesselHex.length() != 32 ) {

            throw new GeneralSecurityException( "Hex-Zahl mit Schlüssel hat falsche Länge." );
        }

        _aesCipher = Cipher.getInstance( KRYPTO_ALGO_NAME );

        final byte[] keyBytes = parseHexBinary( _schluesselHex );
        _secretKey = new SecretKeySpec( keyBytes, "AES" );

        testVerEntschluesselung(); // throws GeneralSecurityException

        LOG.info( "Verschlüsselungs-Algo initialisiert: {}", _aesCipher  );
        LOG.info( "Versalzung eingeschaltet: {}"           , _versalzung );
    }


    /**
     * Test: Verschlüsselt einen String und entschlüsselt ihn wieder.
     *
     *  @throws GeneralSecurityException Fehler bei Ver- oder Entschlüsselung
     */
    private void testVerEntschluesselung() throws GeneralSecurityException {

        final String testString = "Lorem Ipsum ?!*";

        final String testStringVerschluesselt = verschluesseln( testString );
        LOG.info( "Test-String verschlüsselt: \"{}\"", testStringVerschluesselt );

        final String testStringEntschluesselt = entschluesseln( testStringVerschluesselt );
        LOG.info( "Test-String entschlüsselt: \"{}\" ", testStringEntschluesselt );

        if ( testString.equals( testStringEntschluesselt ) == false ) {

            throw new GeneralSecurityException( "Test für Ver- und Entschlüsselung fehlgeschlagen" );
        }
    }


    /**
     * Verschlüsselt {@code stringKlartext}.
     *
     * @param stringKlartext Zu verschlüsselnder String
     *
     * @return Chiffre in Base64-Kodierung
     *
     * @throws GeneralSecurityException Fehler beim Verschlüsseln
     */
    public String verschluesseln( String stringKlartext ) throws GeneralSecurityException {

        _aesCipher.init( ENCRYPT_MODE, _secretKey ); // throws InvalidKeyException

        stringKlartext = salzDazu( stringKlartext );

        byte[] klartextBytes = stringKlartext.getBytes();

        byte[] encryptedBytes = _aesCipher.doFinal( klartextBytes ); // throws IllegalBlockSizeException, BadPaddingException

        return _base64Encoder.encodeToString( encryptedBytes );
    }


    /**
     * Entschlüsselt {@code stringVerschluesselt}.
     *
     * @param stringVerschluesselt Base64-Kodierung von Chiffre
     *
     * @return Entschlüsselter String
     *
     * @throws GeneralSecurityException Fehler beim Entschlüsseln
     */
    public String entschluesseln( String stringVerschluesselt ) throws GeneralSecurityException {

        _aesCipher.init( DECRYPT_MODE, _secretKey );

        byte[] encryptedBytes = _base64Decoder.decode( stringVerschluesselt );

        byte[] decryptedBytes = _aesCipher.doFinal( encryptedBytes );

        String decryptedString = new String( decryptedBytes );

        return salzEntfernen( decryptedString );
    }

    
    /**
     * Vor {@code inputString} einige Zufallszeichen dazu, damit gleiche
     * Klartexte nicht gleiche Chiffren ergeben; wenn Versalzung ausgeschaltet,
     * dann wird {@code inputString} unverändert zurückgegeben.
     * 
     * @param inputString String, vor den einige Zufallszeichen gestellt werden
     * 
     * @return {@code inputString} mit einigen Zufallszeichen vorne dran
     */
    private String salzDazu( String inputString ) {

        if ( _versalzung == false ) {
            
            return inputString;
        }
        
        final String zufallsStr = erzeugeZufallsString( ANZAHL_ZUFALLSZEICHEN );
        return zufallsStr + inputString;
    }

    
    /**
     * Von {@code inputString} werden die vorangestellten Zufallszeichen entfernt.
     * Wenn Versalzung ausgeschaltet ist, dann wird {@code inputString} unverändert
     * zurückgegeben.
     * 
     * @param inputString String, von dem die vorangestellten Zufallszeichen entfernt
     *                    werden sollen
     *                    
     * @return {@code inputString} ohne Zufallszeichen am Anfang 
     */
    private String salzEntfernen( String inputString ) {
        
        if ( _versalzung == false || inputString.length() <= ANZAHL_ZUFALLSZEICHEN ) {

            return inputString;
        }

        return inputString.substring( ANZAHL_ZUFALLSZEICHEN );
    }

    
    /**
     * Erzeugt String mit {@code anzahl} zufälliger Buchstaben.
     *
     * @param anzahl Anzahl zufälliger Buchstaben
     *
     * @return String mit {@code anzahl} zufällig gewählter Groß-
     *         und Kleinbuchstaben, für {@code anzahl=3} z.B. "XaD"
     */
    public String erzeugeZufallsString( int anzahl ) {

        final StringBuilder stringBuilder = new StringBuilder( anzahl );
        for (int i = 0; i < anzahl; i++) {

            int zufallsZahl = _secureRandom.nextInt( 26 );
            char basisChar  = _secureRandom.nextBoolean() ? 'a' : 'A';

            char zufallsBuchstabe = (char) ( basisChar + zufallsZahl );
            stringBuilder.append( zufallsBuchstabe );
        }

        return stringBuilder.toString();
    }

}
