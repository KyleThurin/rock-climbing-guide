import axios from 'axios';

const areaService = {
  //Get all 'areas'
    getAllAreas() {
      return axios.get(`/areas`);
    },
    //Get 'area' for a specific 'id'
    getAreaById(id) {
      return axios.get(`/areas/${id}`);
    },
    //Get all the 'faces' for a specific area 'id'
    getAllFacesForArea(id){
        return axios.get(`/areas/${id}/faces`);
    },
    //Get weather details for a specific ID
    getAreaWeatherByID(id){
        return axios.get(`/areas/${id}/currentWeather`);
    },
    getAreaWeatherIcon(id){
      return axios.get(`/areas/${id}/weatherIcon`);
  },

    ///////ADMIN user only\\\\\\\
    //Create new climbing area
    createArea(area) {
      alert("Creating area ID: "+area)
      return axios.post(`/areas`, area);
    },
    //Update a climbing area
    updateArea(id, area) {
      alert("Updating area ID: "+id)
      return axios.put(`/areas/${id}`, area);
    },
    //Delete a climbing area
    deleteArea(id) {
      alert("deleting area ID: "+id)
      return axios.delete(`/areas/${id}`);
    },
  };
  
  export default areaService;