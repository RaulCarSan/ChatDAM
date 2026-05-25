package org.iesalandalus.ChatDAM.chat.repository;

import org.iesalandalus.ChatDAM.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
}
