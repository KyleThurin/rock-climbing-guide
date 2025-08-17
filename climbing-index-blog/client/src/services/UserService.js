import axios from 'axios';

const userService = {
    getAllLogs() {
      return axios.get(`/logs`);
    },
    getUserLogsForArea(id){
      return axios.get(`/logs?area=${id}`);
    },
    getUserLogsForFace(id){
      return axios.get(`/logs?face=${id}`);
    },
    getUserLogsForRoute(id){
      return axios.get(`/logs?route=${id}`);
    },
    createNewLog(reservation) {
      return axios.post(`/reservations`, reservation);
    },
}
export default userService;