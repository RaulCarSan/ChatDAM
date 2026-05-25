package org.iesalandalus.ChatDAM.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "contenido", nullable = false)
    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    private String contenido;

    @Column(name = "nombre_usuario", nullable = false)
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre de usuario solo puede contener letras")
    private String nombreUsuario;

    @Column(name = "fecha", nullable = false)
    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    @NotBlank(message = "La hora no puede estar vacía")
    private String hora;

    public Mensaje() {}

    public Mensaje(String contenido, String nombreUsuario, LocalDate fecha, String hora) {
        this.contenido = contenido;
        this.nombreUsuario = nombreUsuario;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}
