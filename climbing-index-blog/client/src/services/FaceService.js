import axios from 'axios';

const faceService = {
  //List all faces for all areas
    getAllFaces() {
      return axios.get('/faces');
    },
    //List all the faces for a specific area
    getFaceById(id) {
      return axios.get(`/faces/${id}`);
    },
    //List all the routes for a specific climbing face
    getAllRoutesForFace(id){
        return axios.get(`/faces/${id}/routes`);
    },
    ///////ADMIN user only\\\\\\\
    //Create new climbing face
    createFace(face) {
      return axios.post('/faces', face);
    },
    //Update a climbing face
    updateFace(id, face) {
      return axios.put(`/faces/${id}`, face);
    },
    //TO DO: 
    //Delete a climbing face
    deleteFace(id) {
      return axios.delete(`/faces/${id}`);
    },
  };
  
  export default faceService;