package mx.edu.utez.proyectoslibrosbackend.model.libros;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="libros")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private String nombreLibro;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private int anioPublicacion;
}
