package de.eldecker.dhbw.spring.logik;

import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.ROT;
import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.ORANGE;
import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.WEISS;
import static de.eldecker.dhbw.spring.model.KfzMarkeEnum.BMW;
import static de.eldecker.dhbw.spring.model.KfzMarkeEnum.VW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.db.KfzKennzeichenRepo;
import de.eldecker.dhbw.spring.db.entities.FahrzeugDatenEntity;
import de.eldecker.dhbw.spring.db.entities.KfzKennzeichenEntity;


/**
 * Diese Bean lädt unmittelbar nach dem Start der App Demo-Daten in die Datenbank
 * wenn die Tabelle für {@link KfzKennzeichenEntity} komplett leer ist.
 */
@Component
public class DemoDatenImporter implements ApplicationRunner {

    private final static Logger LOG = LoggerFactory.getLogger( DemoDatenImporter.class );
    
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
            
            // Kennzeichen 1
            fahrzeugDaten = new FahrzeugDatenEntity( VW, ROT, "WVWHG83A8VNUBRGCG", 2007 );            
            kennzeichen   = new KfzKennzeichenEntity( "KA XX 123", fahrzeugDaten );
            _kfzKennzeichenRepo.save( kennzeichen );
            
            
            // Kennzeichen 2
            fahrzeugDaten = new FahrzeugDatenEntity( BMW, ORANGE, "5UMDU93418MXAHHKP", 2024 );            
            kennzeichen   = new KfzKennzeichenEntity( "HD MM 4096", fahrzeugDaten );
            _kfzKennzeichenRepo.save( kennzeichen );
            
            // Kennzeichen 3
            fahrzeugDaten = new FahrzeugDatenEntity( BMW, WEISS, "5UMBT935X6DFAE9BU", 1975 );            
            kennzeichen   = new KfzKennzeichenEntity( "MA AB 123H", fahrzeugDaten );
            _kfzKennzeichenRepo.save( kennzeichen );
            
            long anzahlKfzKennzeichenNeu = _kfzKennzeichenRepo.count();
            LOG.warn( "Demo-Daten in Datenbank geladen: {} KFZ-Kennzeichen", 
                      anzahlKfzKennzeichenNeu );
        }
    }
       
}
