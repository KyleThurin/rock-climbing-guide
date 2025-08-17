import axios from 'axios';

const routeService = {
    getAllRoutes() {
      return axios.get('/routes');
    },
    getRouteById(id) {
      return axios.get(`/routes/${id}`);
    },
    createRoute(route) {
      return axios.post('/routes', route);
    },
    updateRoute(id, route) {
      return axios.put(`/routes/${id}`, route);
    },
    deleteRoute(id) {
      return axios.delete(`/routes/${id}`);
    },
  };
  
  export default routeService;