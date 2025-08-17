<template>
    <!--If there IS an area for the area number, display the info-->
    <div id="detail" v-if="area">
      <section id="title-section">
        <!--Display the area name and info-->
        <h1>{{ area.areaName }}</h1>
        <div id="area-details">
          <p>{{ area.areaDetails }}</p>
        </div>
        <!--Display the face name and info-->
        <h1>{{ face.faceName }}</h1>
        <div id="face-details">
          <p>{{ face.faceDetails }}</p>
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
  
      <!--ADMIN tools-->
      <section id="admin-tools">
        <button id="info" 
        v-if="$store.state.user.role === 'ROLE_ADMIN'" 
        v-on:click="$router.push({ name: 'new-form' })">
        Add New Route
        </button>
      </section>
  
      <section id="routes">
        <div id="routes-title">
          <h2>Climbing Routes</h2>
        </div>
        
        <!--If the number of faces for an area is greater than zero-->
        <ul id="route-list" v-if="faces && faces.length > 0">
          <!--Loop through the faces and lists the items for each-->
          <li id="card" v-for="face in faces" :key="face.faceID"> 
            <!--Displays the image of the area-->
            <img id="face-img" :src="getImagePath(face.faceName)" :alt="face.faceName" style="max-width: 200px; height: auto;">
            <!--Display the name of the face-->
            <h3 id="name">{{ face.faceName }}</h3>
            <!--Button linked to the Face's detailed page-->
            <button id="info" @click="goToFaceDetails(face.faceID)">Face Details</button>
  
            <!--Button navigation section-->
            <nav id="face-buttons">
              <!--ADMIN Tool: Button to UPDATE area-->
              <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="goToAreaDetails(area.areaID)">
                Update Face
              </button>
              <!--ADMIN Tool: Button to DELETE area-->
              <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="deleteArea(area.areaID, area.areaName)">
                Delete Face
              </button>
            </nav>
          </li>
        </ul>
        <p v-else>No faces available for this area.</p>
      </section>
    </div>
    <!--Error section-->
    <div v-else-if="error">
      <p>Error loading area details: {{ error }}</p>
    </div>
    <div v-else>
      <p>Loading area details...</p>
    </div>
  </template>

<script>
  import areaService from '../services/AreaService';
  import faceService from '../services/FaceService';
  import routeService from '../services/RouteService';
  
  export default {
    data() {
      return {
        areaId: this.$route.params.id, // Get the area ID from the route parameters
        area: null,
        faces: [],
        weather: null,
        icon: null, //null
        error: null,
      };
    },
    methods: {
      //Gets the info for this area
      getAreaDetails() {
        areaService.getAreaById(this.areaId)
          .then(response => {
            this.area = response.data;
          })
          .catch(error => {
            console.error("Error finding area details:", error);
            this.error = "Failed to load area details.";
          });
      },
      //Gets the info for this area
      getFaceDetails() {
        faceService.getFaceById(this.faceId)
          .then(response => {
            this.area = response.data;
          })
          .catch(error => {
            console.error("Error finding area details:", error);
            this.error = "Failed to load area details.";
          });
      },
      //Gets the faces that are linked to this area
      getFacesForArea() {
        faceService.getAllRoutesForFace(this.faceId)
          .then(response => {
            this.faces = response.data;
          })
          .catch(error => {
            console.error("Error finding faces:", error);
            this.error = "Failed to load faces.";
          });
      },
      //Gets the current weather for this area
      getWeatherForArea() {
        areaService.getAreaWeatherByID(this.areaId)
          .then(response => {
            this.weather = response.data;
          })
          .catch(error => {
            console.error("Error in finding current weather:", error);
            this.error = "Failed to load weather.";
          });
      },
      //TODO: Need to get the current weather icon from the API
      //Gets the current weather for this area
      getWeatherIcon() {
        areaService.getAreaWeatherIcon(this.areaId)
        .then(response => {
          this.icon = response.data;
          console.log("Weather Icon URL:", this.icon); // Add this line
        })
        .catch(error => {
          console.error("Error fetching weather icon:", error);
        });
      },

      //Finds and displays the images for the face(s) associated 
      getImagePath(faceName) {
        const imageName = `${faceName}.jpg`;
        return new URL(`../imgs/${imageName}`, import.meta.url).href;
      },
      //Navigates to the info page for the area when the button is pushed
      goToFaceDetails(faceId) {
        this.$router.push(`/faces/${faceId}/info`); 
      },
    },
    created() {
      this.getAreaDetails();
      this.getFacesForArea();
      this.getWeatherForArea();
      this.getWeatherIcon();
    },
  };
</script>  