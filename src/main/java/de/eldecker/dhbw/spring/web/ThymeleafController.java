package de.eldecker.dhbw.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.eldecker.dhbw.spring.db.KfzKennzeichenRepo;
import de.eldecker.dhbw.spring.db.entities.KfzKennzeichenEntity;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Controller für Thymeleaf (Template-Engine).
 */
@Controller
@RequestMapping( "/app" )
public class ThymeleafController {
    
    private static final Logger LOG = LoggerFactory.getLogger( ThymeleafController.class );

    /** Bean für Zugriff auf Datenbanktabelle mit KFZ-Kennzeichen. */
    @Autowired
    private KfzKennzeichenRepo _kfzKennzeichenRepo;
    
    
    /**
     * Controller-Methode für Abfrage KFZ-Kennzeichen.
     * 
     * @param model Objekt für Platzhalterwerte in Template
     * 
     * @param feld_1 Unterscheidungszeichen von KFZ-Kennzeichen, z.B. "KA" für Karlsruhe.               
     * 
     * @param feld_2 Ein oder zwei Buchstaben "in der Mitte", also zwischen Unterscheidungszeichen
     *               und Zahlen (vorderer Teil Erkennungsnummer).
     * 
     * @param feld_3 Zahl am Schluss, ein bis vier Stellen (hinterer Teil Erkennungsnummer).
     * 
     * @return Name von Template-Datei {@code abfrage-kfzkennzeichen-ergebnis.html} ohne Datei-Endung
     */
    @GetMapping( "/abfrage-kfz" )
    public String abfrageKfzKennzeichen( Model model,
                                         @RequestParam( value = "feld_1", required = true ) String feld1,
                                         @RequestParam( value = "feld_2", required = true ) String feld2,
                                         @RequestParam( value = "feld_3", required = true ) int    feld3 ) {        
        final String kfzKennzeichen = 
                        String.format( "%s %s %d", 
                                       feld1.trim().toUpperCase(), // Frontend sollte keine Leerzeichen durchlassen
                                       feld2.trim().toUpperCase(),
                                       feld3 );
        
        LOG.info( "Abfrage für KFZ-Kennzeichen \"{}\" erhalten.", kfzKennzeichen );
        
        final Optional<KfzKennzeichenEntity> kennzeichenOptional = _kfzKennzeichenRepo.findByKennzeichen( kfzKennzeichen );
        if ( kennzeichenOptional.isEmpty() ) {
        
            final String nachricht = String.format( "KFZ-Kennzeichen \"%s\" nicht gefunden.", kfzKennzeichen );
            LOG.info( nachricht );
            model.addAttribute( "nachricht", nachricht );
            
        } else { // gefunden
            
            final KfzKennzeichenEntity kennzeichenEntity = kennzeichenOptional.get();
            
            final String nachricht = String.format( "KFZ-Kennzeichen \"%s\" gefunden.", kennzeichenEntity );
            LOG.info( nachricht );
            
            model.addAttribute( "nachricht"        , nachricht );            
            model.addAttribute( "kennzeichenEntity", kennzeichenEntity );
        }
                        
        return "abfrage-kfzkennzeichen-ergebnis";
    }

}
