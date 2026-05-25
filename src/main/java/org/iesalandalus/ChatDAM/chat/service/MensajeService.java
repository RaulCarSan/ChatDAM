package org.iesalandalus.ChatDAM.chat.service;

import org.iesalandalus.ChatDAM.chat.repository.MensajeRepository;
import org.iesalandalus.ChatDAM.model.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensajeService {

    @Autowired
    private MensajeRepository mensajeRepository;

    public List<Mensaje> obtenerUltimos10Mensajes() {
        List<Mensaje> todosLosMensajes = mensajeRepository.findAll();

        if (todosLosMensajes.size() > 10) {
            return todosLosMensajes.subList(todosLosMensajes.size() - 10, todosLosMensajes.size());
        }
        return todosLosMensajes;
    }

    public void guardarMensaje(Mensaje mensaje) {
        if (mensaje == null) {
            throw new IllegalArgumentException("No se puede guardar un mensaje nulo");
        }
        mensajeRepository.save(mensaje);
    }
}