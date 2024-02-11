<template lang="">
  <b-container fluid>
      <b-row class="p-5 text-end">
        <b-col>
          <b-button variant="success" v-b-modal.modal-1>Agregar</b-button>
        </b-col>
      </b-row>
      <b-row>
        <b-col v-if="cargando" class="d-flex justify-content-center mb-3">
          <b-spinner style="width: 3rem; height: 3rem;"></b-spinner>
        </b-col>
        <b-col cols="3" v-else v-for="libro in libros" :key="libro.id">
          <b-card
          :title=libro.autor
          img-src=""
          img-alt="Image"
          img-top
          tag="article"
          style="max-width: 20rem;"
          class="mb-2"
          @click="editarLibro(libro)"
          >
            <b-card-text>
               {{ libro.nombreLibro }}
            </b-card-text>
            <b-card-text>
               {{ libro.genero }}
            </b-card-text>
            <b-card-text>
               {{ libro.anioPublicacion }}
            </b-card-text>
            <div class="text-end">
              <b-button variant="danger" @click="eliminarLibro(libro.id)">Eliminar</b-button>
            </div>
          </b-card>
        </b-col>
      </b-row>
      <b-modal 
        id="modal-1" 
        title="Agregar nuevo libro"
        scrollable
        centered
        @ok="insertarLibro"
      >
        <form ref="form" >
          <div class="mb-3">
            <label for="autor" class="form-label">Autor:</label>
            <input type="text" id="autor" v-model="autor" class="form-control">
          </div>
          <div class="mb-3">
            <label for="nombreLibro" class="form-label">Nombre del Libro:</label>
            <input type="text" id="nombreLibro" v-model="nombreLibro" class="form-control">
          </div>
          <div class="mb-3">
            <label for="genero" class="form-label">Género:</label>
            <input type="text" id="genero" v-model="genero" class="form-control">
          </div>
          <div class="mb-3">
            <label for="anioPublicacion" class="form-label">Año de Publicación:</label>
            <input type="number" id="anioPublicacion" v-model.number="anioPublicacion" class="form-control">
          </div>
        </form>
      </b-modal>
  </b-container>
</template>
<script>
import { Alert } from "bootstrap";
import Libros from "./services/Libros";

export default {
  data() {
    return {
      autor: '',
      nombreLibro: '',
      genero: '',
      anioPublicacion: '',
      libros: [],
      status: false,
      cargando: false,
      libroEditado: {
        id: null,
        autor: '',
        nombreLibro: '',
        genero: '',
        anioPublicacion: '',
      }
    }
  },
  mounted() {
    this.obtenerLibros();
  },
  methods: {
    async insertarLibro(event) {
      const anioActual = new Date().getFullYear();
      try {
        if (this.anioPublicacion > anioActual) {
            return alert("El año de edicion no es el correcto");
        }
        if (this.status) {
          await Libros.onUpdate(
            this.libroEditado.id,
            this.autor,
            this.nombreLibro,
            this.genero,
            this.anioPublicacion,
          );
        } else {
          if (this.anioPublicacion > anioActual) {
            return alert("El año de edicion no es el correcto");
        }
          await Libros.onRegister(
            this.autor,
            this.nombreLibro,
            this.genero,
            this.anioPublicacion,
          );
        }
        this.obtenerLibros();
      } catch (error) {
        throw(error)
      } finally {
        this.$bvModal.hide('modal-1');
        this.autor = '';
        this.nombreLibro = '';
        this.genero = '';
        this.anioPublicacion = '';
      }
    },

    async obtenerLibros() {
      try {
        this.cargando = true;
        const resutl = await Libros.onGetAll();
        this.libros = resutl;
      } catch (error) {
        throw (error)
      } finally {
        this.cargando = false;
      }
    },

    async eliminarLibro(libroId) {
      const result = await Libros.onDeleteId(libroId);
      console.log(result);
      this.obtenerLibros();
    },


    editarLibro(libro) {
      this.libroEditado = { ...libro };
      this.autor = this.libroEditado.autor;
      this.nombreLibro = this.libroEditado.nombreLibro;
      this.genero = this.libroEditado.genero;
      this.anioPublicacion = this.libroEditado.anioPublicacion;
      this.$bvModal.show('modal-1');
      this.status = true
    },
  },
}
</script>
<style lang="">
  
</style>