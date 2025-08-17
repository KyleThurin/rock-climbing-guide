<template>
  <div id="face" v-if="face">
    <section id="title-section">
        <!--Display the area name and info-->
        <h1 v-if="area">{{ area.areaName }}</h1> 
        <div id="area-details" v-if="area"> 
            <p>{{ area.areaDetails }}</p>
        </div>
        <p v-else>Loading area details...</p> 
        
        <h1>{{ face.faceName }}</h1>
        <div id="face-details">
            <p>{{ face.faceNotes }}</p>
        </div>
    </section>

    <section id="weather">
        <div id="weather-title">
          <h2>Current Weather</h2>
        </div>
        <!--If there is a weather icon for the given lat/long, display the img-->
        <div id="icon-pic" v-if="icon">
          <img id="weather-icon" :src="icon" :alt="weather ? weather : 'Weather Icon'" style="min-width: 200px; height: auto;">
        </div>
        <p v-else>Loading weather icon...</p>
        
        <div id="weather-info">
          <!--If there is weather for the given lat/long, display the info-->
          <p v-if="weather">{{ weather }}</p>
          <!--If not, display the loading message-->
          <p v-else>Loading weather info...</p>
        </div>
      </section>

    <section id="admin-tools">
      <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" v-on:click="$router.push({ name: 'new-form' })">
        Add New Route
      </button>
    </section>

    <section id="routes">
      <div id="route-title">
        <h2>Climbing Routes</h2>
      </div>

      <ul id="route-list" v-if="routes && routes.length > 0">
        <li id="card" v-for="route in routes" :key="route.routeID">
          <img id="route-img" :src="getImagePath(route.routeName)" :alt="route.routeName" style="max-width: 200px; height: auto;">
          <h3 id="name">{{ route.routeName }}</h3>
          <p class="grade">{{ route.routeGrade }}</p>
          <button id="info" @click="goToRouteDetails(route.routeID)">Route Details</button>

          <nav id="route-buttons">
            <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="editRoute(route.routeID)">
              Update Route
            </button>
            <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="deleteRoute(route.routeID, route.routeName)">
              Delete Route
            </button>
          </nav>
        </li>
      </ul>
      <p v-else>No routes available for this face.</p>
    </section>
  </div>
  <div v-else-if="error">
    <p>Error loading face details: {{ error }}</p>
  </div>
  <div v-else>
    <p>Loading face details...</p>
  </div>
</template>

<script>
import areaService from '../services/AreaService';
import faceService from '../services/FaceService';
import routeService from '../services/RouteService'; // Assuming you have a RouteService

export default {
  data() {
    return {
      faceId: this.$route.params.id, // Get the face ID from the route parameters
      area: null,
      face: null,
      routes: [],
      weather: null,
      icon: null,
      error: null,
    };
  },
  methods: {
    //Gets the info for this area
    getAreaDetails(areaID) {
        areaService.getAreaById(areaID)
          .then(response => {
            this.area = response.data;
            console.log("Fetched area:", this.area); //For debugging
          })
          .catch(error => {
            console.error("Error finding area details:", error); //For debugging
            this.error = "Failed to load area details.";
          });
      },
    //Gets the info for this face
    getFaceDetails() {
  faceService.getFaceById(this.faceId)
    .then(response => {
      this.face = response.data;
      console.log("Fetched face:", this.face); 
      if (this.face && this.face.areaID) {
        console.log("Face areaId for fetching area:", this.face.areaID); //For debugging
        //Pass 'areaID' to functions
        this.getAreaDetails(this.face.areaID);
        this.getWeatherForArea(this.face.areaID);
        this.getWeatherIcon(this.face.areaID); 
      } else {
        console.warn("Face or face.areaId is undefined/null after fetching face details."); //For debugging
        this.error = "Could not find associated area for this face.";
      }
    })
    .catch(error => {
      console.error("Error finding face details:", error); //For debugging
      this.error = "Failed to load face details.";
    });
},
    //Gets the current weather for this area
    getWeatherForArea(areaID) {
        areaService.getAreaWeatherByID(areaID)
          .then(response => {
            this.weather = response.data;
          })
          .catch(error => {
            console.error("Error in finding current weather:", error); //For debugging
            this.error = "Failed to load weather.";
          });
      },
      //Gets the current weather for this area
      getWeatherIcon(areaID) {
        areaService.getAreaWeatherIcon(areaID)
        .then(response => {
          this.icon = response.data;
          console.log("Weather Icon URL:", this.icon); //For debugging
        })
        .catch(error => {
          console.error("Error fetching weather icon:", error); //For debugging
        });
      },
    //Gets the routes that are linked to this face
    getRoutesForFace() {
      faceService.getAllRoutesForFace(this.faceId)
        .then(response => {
          this.routes = response.data;
        })
        .catch(error => {
          console.error("Error finding routes:", error); //For debugging
          this.error = "Failed to load routes.";
        });
    },
    //Finds and displays the images for the route(s) associated
    getImagePath(routeName) {
      try {
        const imageName = `${routeName}.jpg`;
        return new URL(`../imgs/${imageName}`, import.meta.url).href;
      } catch (error) {
        // If no img is found, use the 'test.jpg'
        return new URL(`../imgs/test.jpg`, import.meta.url).href;
      }
    },
    //Navigates to the info page for the route when the button is pushed
    goToRouteDetails(routeId) {
      this.$router.push(`/routes/${routeId}/info`);
    },
    //TODO: Work on
    editRoute(routeId) {
      alert('Calm down, I am still working on this!');
      console.log('Calm down, I am still working on this!');
    },
    deleteRoute(routeId, routeName) {
      if (confirm(`Are you sure you want to delete "${routeName}" (ID: ${routeId})?`)) {
        routeService.deleteRoute(routeId)
          .then(() => {
            alert(`Route "${routeName}" has been deleted.`);
            this.getRoutesForFace(); //Refresh the list after deleting route
          })
          .catch(error => {
            console.error(`Error deleting route with ID ${routeId}:`, error); //For debugging
            alert(`Failed to delete route "${routeName}". Please try again.`);
          });
      }
    },
  },
  created() {
    this.getFaceDetails();
    this.getRoutesForFace();
  },
};
</script>

<style scoped>
/* Color scheme
yale-blue: #003B6Dff;
--lapis-lazuli: #00639Aff;
--air-superiority-blue: #6697BCff;
--raw-umber: #856859ff;
--bistre: #433227ff;
*/

#face {
  display: flex;
  flex-direction: column;
  padding: 30px;
  background: linear-gradient(0deg, #585c5e, #6697BCff);
  min-height: 100vh;
  color: #37474f;
}

#title-section {
  text-align: center;
  padding: 50px 20px;
  margin-bottom: 40px;
  background-color: rgba(255, 255, 255, 0.85);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  display: inline-block;
  width: auto;
  margin-left: auto;
  margin-right: auto;
}

#title-section h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #1976d2;
}

#face-details {
  font-size: 1.1rem;
  line-height: 1.6;
  padding: 20px;
  text-align: center;
  border-radius: 8px;
  box-shadow: 0 2px 5px black;
  margin-bottom: 20px;
  margin-top: 20px;
  background-color: #6697BCff;
}

/*Weather section*/
#weather {
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  background-color: rgba(255, 255, 255, 0.85);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center; /* Add this line */
}
#weather-title{
  padding: 20px;
  text-align: center;
  border-radius: 8px;
  box-shadow: 0 2px 5px black;
  margin-bottom: 20px;
  margin-top: 20px;
  background-color: #6697BCff;
  display: inline-block;
}
#weather-title h2 {
  font-size: 1.6rem;
}
#icon-pic{
  justify-items: center;
  transition: transform 0.2s ease-in-out;
}

#weather-icon{
  border-radius: 8px;
}
#weather-info{
  padding: 20px;
  text-align: center;
  border-radius: 8px;
  box-shadow: 0 2px 5px black;
  margin-bottom: 20px;
  margin-top: 20px;
  background-color: #6697BCff;
}

#weather h2 {
  color: #00639Aff;
  text-align: center;
}
#weather p {
  font-size: 1rem;
  line-height: 1.5;
  text-align: center;
}

/* ADMIN tools */
#admin-tools {
  margin-bottom: 20px;
  margin-left: auto;
  margin-right: auto;
}

/* Route card section */
#route-title {
  text-align: center;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 5px black;
  margin-bottom: 20px;
  margin-top: 20px;
  background-color: #6697BCff;
}

#route-title h2 {
  font-size: 1.6rem;
}

#routes {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 20px;
}

#route-list {
  display: grid;
  list-style: none;
  padding: 0;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

#card {
  background-color: #6697BCff;
  color: #424242;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  text-align: center;
  border-radius: 8px;
  box-shadow: 0 2px 5px black;
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

#card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 10px black;
}

#route-img {
  display: block;
  border-radius: 8px;
  max-width: 300px;
  min-height: 200px;
  height: auto;
  margin-bottom: 15px;
}

#name {
  color: #1976d2;
  margin-top: 10px;
  margin-bottom: 15px;
  font-size: 1.6rem;
}

.grade {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #003B6Dff;
}

#route-buttons {
  display: flex;
  gap: 10px;
  padding: 10px;
  justify-content: center;
  flex-wrap: wrap;
}

#info {
  background-color: #1976d2;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease, transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
}

#info:hover {
  background-color: #0d47a1;
  transform: translateY(-3px);
  box-shadow: 0 3px 7px rgba(0, 0, 0, 0.2);
}
</style>