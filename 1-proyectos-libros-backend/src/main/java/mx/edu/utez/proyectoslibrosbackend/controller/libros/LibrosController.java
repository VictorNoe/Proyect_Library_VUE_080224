package mx.edu.utez.proyectoslibrosbackend.controller.libros;

import mx.edu.utez.proyectoslibrosbackend.model.libros.Libros;
import mx.edu.utez.proyectoslibrosbackend.services.libros.LibrosServices;
import mx.edu.utez.proyectoslibrosbackend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = {"*"})
public class LibrosController {
    @Autowired
    LibrosServices services;

    @GetMapping("/")
    public ResponseEntity<CustomResponse<List<Libros>>> getAll() {
        return new ResponseEntity<>(this.services.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Libros>> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.services.getOne(id), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<CustomResponse<Libros>> insert(@Validated @RequestBody Libros libros) {
        return  new ResponseEntity<>(this.services.insert(libros), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<CustomResponse<Libros>> update(@Validated  @RequestBody Libros libros)  {
        return new ResponseEntity<>(this.services.update(libros),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Libros>> delete(@PathVariable("id") Long id){
        return new ResponseEntity<>(this.services.delete(id),HttpStatus.CREATED);
    }
}
