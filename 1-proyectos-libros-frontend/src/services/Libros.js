import axios from "axios";

let URL_API = "http://localhost:8080/api/libros/"

const onRegister = async (author, name, general, date) => {
    try {
        const response = await axios.post(URL_API, {
            autor: author,
            nombreLibro: name,
            genero: general,
            fechaPublicacion: date,
        });
        
    } catch (error) {
        throw(error)
    }
}

const onGetAll = async () => {
    try {
        const response = await axios.get(URL_API);
        return response.data.data;
    } catch (error) {
        throw(error)
    }
}

const onDeleteId = async (libroId) => {
    try {
        const response = await axios.delete(`${URL_API}${libroId}`);
        return response.data;
    } catch (error) {
        throw(error)
    }
}

const onUpdate = async ( idL, author, name, general, date ) => {
    try {
        const response = await axios.put(URL_API, {
            id: idL,
            autor: author,
            nombreLibro: name,
            genero: general,
            fechaPublicacion: date,
        });
    } catch (error) {
        throw(error)
    }
}

const getAllAutor = async ( autor ) => {
    try {
        const response = await axios.post(URL_API+"autor", {
            data: autor,
        });
        return response.data.data;
    } catch (error) {
        throw(error)
    }
}

const getAllGenero = async ( genero ) => {
    try {
        const response = await axios.post(URL_API+"genero", {
            data: genero,
        });
        return response.data.data;
    } catch (error) {
        throw(error)
    }
}

const getAllNombre = async ( nombre ) => {
    try {
        const response = await axios.post(URL_API+"nombre", {
            data: nombre,
        });
        return response.data.data;
    } catch (error) {
        throw(error)
    }
}

const getAllFechas = async ( fechaIni, fechaFinal ) => {
    try {
        const response = await axios.post(URL_API+"fechas", {
            fechaInicio: fechaIni,
            fechaFin: fechaFinal
        });
        return response.data.data;
    } catch (error) {
        throw(error)
    }
}



export default {
    onRegister,
    onGetAll,
    onDeleteId,
    onUpdate,
    getAllAutor,
    getAllGenero,
    getAllNombre,
    getAllFechas,
}