package de.eldecker.dhbw.spring.logik;

import static de.eldecker.dhbw.spring.model.KfzFarbeEnum.ROT;
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
        
        long anzahlKfzKennzeichen = _kfzKennzeichenRepo.count();
        if ( anzahlKfzKennzeichen > 0 ) {
         
            LOG.info( "Es sind schon {} KFZ-Kennzeichen in der DB, deshalb werden keine Daten geladen." );
            
        } else {
                                    
            FahrzeugDatenEntity  fahrzeugDaten = null; 
            KfzKennzeichenEntity kennzeichen   = null;
            
            fahrzeugDaten = new FahrzeugDatenEntity( VW, ROT, "WVWHG83A8VNUBRGCG", 2007 );
            kennzeichen = new KfzKennzeichenEntity();
            
            kennzeichen = new KfzKennzeichenEntity( "KA XX 123", fahrzeugDaten );
            _kfzKennzeichenRepo.save( kennzeichen );
            
            LOG.warn( "Demo-Daten in Datenbank geladen." );
        }
    }
       
}
