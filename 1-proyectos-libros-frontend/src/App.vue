<template lang="">
  <b-container style="height: 2000px" class="p-5" fluid>
    <b-row class="p-3">
      <b-col cols="5">
        <b-datepicker v-model="fechaInicio"></b-datepicker>
      </b-col>
      <b-col cols="5">
        <b-datepicker v-model="fechaFin"></b-datepicker>
      </b-col>
      <b-col cols="2">
        <b-button variant="primary" @click="getDate">Buscar libros</b-button>
      </b-col>
    </b-row>
    <b-row class="p-3" align-v="end">
      <b-col cols="12">
        <b-input-group label="Fecha de inicio">
          <template>
            <b-dropdown v-model="tipoBusqueda" text="categorys" variant="primary">
              <b-dropdown-item @click="tipoBusqueda = 'autor'">Autor</b-dropdown-item>
              <b-dropdown-item @click="tipoBusqueda = 'genero'">Género</b-dropdown-item>
              <b-dropdown-item @click="tipoBusqueda = 'nombre'">Nombre</b-dropdown-item>
            </b-dropdown>
          </template>
          <b-form-input v-model="consulta" placeholder="Ingrese su consulta..."/>
          <b-input-group-append>
            <b-button variant="primary" @click="getCategory">Buscar</b-button>
          </b-input-group-append>
        </b-input-group>
      </b-col>
      <b-col>
      </b-col>
    </b-row>
      <b-row>
        <b-col @dragstart="handleDragStart('add')" cols="6" class="p-2" v-show="showElement">
          <b-card class="p-3">
            <b-row class="mb-3">
              <b-col cols="6">
                <label for="autor" class="form-label">Autor:</label>
                <input type="text" id="autor" v-model="autor" class="form-control">
              </b-col>
              <b-col cols="6">
                <label for="nombreLibro" class="form-label">Nombre del Libro:</label>
                <input type="text" id="nombreLibro" v-model="nombreLibro" class="form-control">
              </b-col>
            </b-row>
            <b-row class="mb-3">
              <b-col cols="6">
                <label for="genero" class="form-label">Género:</label>
                <input type="text" id="genero" v-model="genero" class="form-control">
              </b-col>
              <b-col cols="6">
                <label for="anioPublicacion" class="form-label">Año de Publicación:</label>
                <input type="date" id="anioPublicacion" v-model="anioPublicacion" class="form-control">
              </b-col>      
            </b-row>
          </b-card>
        </b-col>
      </b-row>
      <b-row @dragover.prevent @drop="handleDrop" class="border border-1 p-3">
        <b-col v-if="cargando" class="d-flex justify-content-center mb-3">
          <b-spinner style="width: 3rem; height: 3rem;"></b-spinner>
        </b-col>
        <b-col cols="3" v-for="(libro, index) in libros" :key="libros.id" class="animate__animated animate__fadeIn">
          <b-card
            :title=libro.autor
            tag="article"
            style="max-width: 20rem;"
            class="mb-2"
            draggable="true"
            @dragstart="handleDragStart(index)"
          >
            <b-card-text>
               {{ libro.nombreLibro }}
            </b-card-text>
            <b-card-text>
               {{ libro.genero }}
            </b-card-text>
            <b-card-text>
               {{ libro.fechaPublicacion }}
            </b-card-text>
            <div class="text-end" z-index:1>
              <b-button variant="primary" @click="editarLibro(libro)">Actualizar</b-button>
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
            <input type="date" id="anioPublicacion" v-model="anioPublicacion" class="form-control">
          </div>
        </form>
      </b-modal>
  </b-container>
</template>
<script>
import Libros from "./services/Libros";

export default {
  data() {
    return {
      showElement: true,
      lastScrollPosition: 0,
      fechaInicio: null,
      fechaFin: null,
      tipoBusqueda: '',
      consulta: '',
      autor: '',
      nombreLibro: '',
      genero: '',
      anioPublicacion: "",
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
    window.addEventListener("scroll", this.onScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.onScroll);
  },

  methods: {
    onScroll() {
      const currentScrollPosition = window.pageYOffset || document.documentElement.scrollTop;
      if (Math.abs(currentScrollPosition - this.lastScrollPosition) < 60) {
        return;
      }
      this.showElement = currentScrollPosition < this.lastScrollPosition; 
      this.lastScrollPosition = currentScrollPosition;
    },

    async insertarLibro() {
      const anioActual = new Date().getFullYear();
      try {
        console.log(this.anioPublicacion);
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
          if (!this.autor,!this.nombreLibro,!this.genero,!this.anioPublicacion){
            return alert("Llena todos los campos");
          }
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
        throw (error)
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

    async getCategory() {
      console.log(this.consulta);
      if (this.consulta.trim() === '') {
        await this.obtenerLibros();
        return;
      }
      switch (this.tipoBusqueda) {
        case 'autor':
          const autor = await Libros.getAllAutor(this.consulta)
          this.libros = autor;
          break;
        case 'genero':
          const genero = await Libros.getAllGenero(this.consulta)
          this.libros = genero;
          break;
        case 'nombre':
          const name = await Libros.getAllNombre(this.consulta)
          this.libros = name;
          break;
        default:
          alert('Tipo de búsqueda no válido');
          return;
      }
    },

    async getDate() {
      if (!this.fechaInicio || !this.fechaFin) {
        alert("Ingrese las dos fechas por favor")
        return;
      }
      const result = await Libros.getAllFechas(this.fechaInicio,this.fechaFin);
      this.libros = result;
    },

    formatDate(date) {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      const seconds = date.getSeconds().toString().padStart(2, '0');
      const milliseconds = date.getMilliseconds().toString().padStart(3, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}.${milliseconds}`;
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

    handleDragStart(index) {
      event.dataTransfer.setData("text/plain", index);
    },

    handleDrop(event) {
      event.preventDefault();
      const index = event.dataTransfer.getData("text/plain");
      if(index === "add"){
        return this.insertarLibro();
      }
      const itemText = this.libros[index];
      const dropIndex = this.libros.indexOf(event.target.innerText);
      this.libros.splice(index, 1);
      this.libros.splice(dropIndex, 0, itemText);
    },
    
  },
}
</script>
<style lang="">
  
</style>