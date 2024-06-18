package de.eldecker.dhbw.spring.logik;

import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.ROT;
import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.ORANGE;
import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.WEISS;
import static de.eldecker.dhbw.spring.model.KfzMarkeEnum.BMW;
import static de.eldecker.dhbw.spring.model.KfzMarkeEnum.MERCEDES;
import static de.eldecker.dhbw.spring.model.KfzMarkeEnum.VW;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.db.KfzKennzeichenRepo;
import de.eldecker.dhbw.spring.db.entities.FahrzeugDatenEntity;
import de.eldecker.dhbw.spring.db.entities.FahrzeugHalterEntity;
import de.eldecker.dhbw.spring.db.entities.KfzKennzeichenEntity;


/**
 * Diese Bean lädt unmittelbar nach dem Start der App Demo-Daten in die Datenbank
 * wenn die Tabelle für {@link KfzKennzeichenEntity} komplett leer ist.
 */
@Component
public class DemoDatenImporter implements ApplicationRunner {

    private final static Logger LOG = LoggerFactory.getLogger( DemoDatenImporter.class );
    
    /** Repo-Bean für Zugriff auf Datenbanktabelle mit KFZ-Kennzeichen. */
    @Autowired
    private KfzKennzeichenRepo _kfzKennzeichenRepo;
    
    
    @Override
    public void run( ApplicationArguments args ) throws Exception {
        
        long anzahlKfzKennzeichenAlt = _kfzKennzeichenRepo.count();
        if ( anzahlKfzKennzeichenAlt > 0 ) {
         
            LOG.info( "Es sind schon {} KFZ-Kennzeichen in der DB, deshalb werden keine Daten geladen.", 
                      anzahlKfzKennzeichenAlt );            
        } else {
                                    
            FahrzeugDatenEntity  fahrzeugDaten = null; 
            KfzKennzeichenEntity kennzeichen   = null;
            
            FahrzeugHalterEntity halter1 = new FahrzeugHalterEntity( "Herr", "Max", "Mustermann", "Kronenplatz 1", 76676, "Karlsruhe" );
            FahrzeugHalterEntity halter2 = new FahrzeugHalterEntity( "Frau", "Pia", "Musterfrau", "Am Ring 42b"  , 68159, "Mannheim"  );
            FahrzeugHalterEntity halter3 = new FahrzeugHalterEntity( "Frau", "Eva", "Musterfrau", "Am Ring 42b"  , 68159, "Mannheim"  ); // Schwester von halter2, gleiche Anschrift darf in DB nicht zu erkennen sein
            
            List<KfzKennzeichenEntity> kfzKennzeichenList = new ArrayList<>( 10 );
            
            // Kennzeichen 1
            fahrzeugDaten = new FahrzeugDatenEntity( VW, ROT, "WVWHG83A8VNUBRGCG", 2007 );            
            kennzeichen   = new KfzKennzeichenEntity( "KA XX 123", fahrzeugDaten, halter1 );
            kfzKennzeichenList.add( kennzeichen );
                        
            // Kennzeichen 2
            fahrzeugDaten = new FahrzeugDatenEntity( BMW, ORANGE, "5UMDU93418MXAHHKP", 2024 );            
            kennzeichen   = new KfzKennzeichenEntity( "HD MM 4096", fahrzeugDaten, halter1 );
            kfzKennzeichenList.add( kennzeichen );
            
            // Kennzeichen 3
            fahrzeugDaten = new FahrzeugDatenEntity( BMW, WEISS, "5UMBT935X6DFAE9BU", 1975 );            
            kennzeichen   = new KfzKennzeichenEntity( "MA AB 123", fahrzeugDaten, halter2, true ); // true=historisch
            kfzKennzeichenList.add( kennzeichen );

            // Kennzeichen 4
            fahrzeugDaten = new FahrzeugDatenEntity( MERCEDES, WEISS, "WDCYR49E636ATTZ9R", 2011 );            
            kennzeichen   = new KfzKennzeichenEntity( "MA X 777", fahrzeugDaten, halter3 );
            kfzKennzeichenList.add( kennzeichen );

            _kfzKennzeichenRepo.saveAll( kfzKennzeichenList );
            
            
            long anzahlKfzKennzeichenNeu = _kfzKennzeichenRepo.count();
            LOG.warn( "Demo-Daten in Datenbank geladen: {} KFZ-Kennzeichen", 
                      anzahlKfzKennzeichenNeu );
        }
    }
       
}
