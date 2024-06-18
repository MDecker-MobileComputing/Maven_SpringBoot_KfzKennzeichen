package de.eldecker.dhbw.spring.db;

import de.eldecker.dhbw.spring.db.entities.KfzKennzeichenEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository für {@link KfzKennzeichenEntity}, wird von <i>Spring Data JPA</i>
 * automatisch implementiert und instanziiert.
 * 
 * @param <KfzKennzeichenEntity> Entity-Klasse, für die dieses Repo zuständig ist
 * 
 * @param <Long> Typ von Primärschlüssel
 */
public interface KfzKennzeichenRepo extends JpaRepository<KfzKennzeichenEntity, Long> {

    /**
     * KFZ-Kennzeichen auflösen.
     *  
     * @param KfzKennzeichen Kennzeichen, das gesucht werden, soll, mit Leerzeichen
     *                       und normiert auf Großbuchstaben, kein "H" am Ende.
     *                       Beispiel: "KA X 123"
     *                       
     * @return Optional enthält Datensatz wenn gefunden
     */
    public Optional<KfzKennzeichenEntity> findByKennzeichen( String KfzKennzeichen ); 
    
}
