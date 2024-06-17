package de.eldecker.dhbw.spring.db;

import de.eldecker.dhbw.spring.db.entities.KfzKennzeichenEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface KfzKennzeichenRepo extends JpaRepository<KfzKennzeichenEntity, Long> {

}
