package de.eldecker.dhbw.spring.web;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.eldecker.dhbw.spring.db.KfzKennzeichenRepo;
import de.eldecker.dhbw.spring.db.entities.FahrzeugHalterEntity;
import de.eldecker.dhbw.spring.db.entities.KfzKennzeichenEntity;
import jakarta.annotation.PostConstruct;


/**
 * REST-Controller, um externe Anfragen (z.B. von anderen Microservices)
 * zu beantworten.
 */
@RestController
@RequestMapping( "/api/v1" )
public class ExternRestController {

    private static final Logger LOG = LoggerFactory.getLogger( ExternRestController.class );

    /** Bean für Zugriff auf Datenbanktabelle mit KFZ-Kennzeichen. */
    @Autowired
    private KfzKennzeichenRepo _kfzKennzeichenRepo;
    
    private FahrzeugHalterEntity _fahrzeugHalterLeer = new FahrzeugHalterEntity();

    /** Konfiguration, ob REST-Endpunkt sporadische Fehler zurückliefern soll. */
    @Value( "${de.eldecker.kfz-kennzeichen.rest.sporadischefehler:false}" )
    private boolean _sporadischeFehler;

    /**
     * Methode schreibt auf Logger, ob sporadische Fehler bei REST-Abfragen
     * laut Konfiguration erzeugt werden sollen.
     */
    @PostConstruct
    public void nachKonstruktor() {

        LOG.info( "Erzeugung sporadische Fehler bei REST-Abfragen: {}",
                          _sporadischeFehler );
    }


    /**
     * REST-Methode für Endpunkt, um Infos zu KFZ-Kennzeichen abzufragen.
     * <br><br>
     *
     * Beispiel-URL für Abfrage KFZ-Kennzeichen "BAD E 1234:
     * <pre>
     * http://localhost:8080/api/v1/abfrage/BAD%20E%201234
     * </pre><br>
     * Die Leerzeichen müssen mit {@code %20} kodiert werden.
     *
     * @param kennzeichen KFZ-Kennzeichen, für das die Halterinformationen zurückgegeben
     *                    werden sollen.
     *
     * @return Wenn gefunden, dann serialisiertes Objekt und HTTP-Status-Code 200 (OK),
     *         wenn nicht gefunden, dann HTTP-Status-Code 404 (Not Found); mit 50%
     *         Wahrscheinlichkeit treten allerdings interne Fehler auf (um Fehler-Handling
     *         beim Client zu testen).
     */
    @GetMapping( "/abfrage/{kennzeichen}" )
    public ResponseEntity<FahrzeugHalterEntity> kennzeichenAbfragen ( @PathVariable String kennzeichen )
                               throws Exception {

        kennzeichen = kennzeichen.trim();

        LOG.info( "REST-Abfrage für KFZ-Kennzeichen erhalten: \"{}\"", kennzeichen );

        if ( _sporadischeFehler && Math.random() <= 0.5 ) {

            LOG.error( "Interner Fehler bei Abfrage von KFZ-Kennzeichen \"{}\" (Zufallsentscheidung).", 
                       kennzeichen );
            return ResponseEntity.status( INTERNAL_SERVER_ERROR ).body( null );
        }

        Optional<KfzKennzeichenEntity> kennzeichenOptional =
                            _kfzKennzeichenRepo.findByKennzeichen( kennzeichen );

        if ( kennzeichenOptional.isEmpty() ) {

           LOG.warn( "Kein KFZ-Kennzeichen \"{}\" gefunden.", kennzeichen );
           return ResponseEntity.status( NOT_FOUND ).body( _fahrzeugHalterLeer );

        } else {

            KfzKennzeichenEntity kfzKennzeichen = kennzeichenOptional.get();
            FahrzeugHalterEntity fahrzeugHalterDaten = kfzKennzeichen.getFahrzeugHalter();

            LOG.info( "KFZ-Kennzeichen gefunden: {}", kfzKennzeichen );
            return ResponseEntity.status( OK )
                                 .body( fahrzeugHalterDaten );
        }
    }

}
