package mx.edu.utez.proyectoslibrosbackend.model.libros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrosRepository extends JpaRepository<Libros, Long> {
    @Modifying
    @Query(value = "SELECT l FROM Libros l WHERE l.autor = :autor")
    List<Libros> findAutor(@Param("autor") String autor);

    @Query(value = "SELECT l FROM Libros l WHERE l.nombreLibro = :nombreLibro")
    List<Libros> findNombre(@Param("nombreLibro") String nombreLibro);

    @Query(value = "SELECT l FROM Libros l WHERE l.genero = :genero")
    List<Libros> findGemero(@Param("genero") String genero);
}
