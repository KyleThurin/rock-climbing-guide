<template>
  <!--If there IS an area for the area number, display the info-->
  <div id="area" v-if="area">
    <section id="title-section">
      <!--Display the area name and info-->
      <h1>{{ area.areaName }}</h1>
      <div id="area-details">
        <p>{{ area.areaDetails }}</p>
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
      Add New Face
      </button>
    </section>

    <section id="faces">
      <div id="face-title">
        <h2>Climbing Faces</h2>
      </div>
      
      <!--If the number of faces for an area is greater than zero-->
      <ul id="face-list" v-if="faces && faces.length > 0">
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
      //Gets the faces that are linked to this area
      getFacesForArea() {
        areaService.getAllFacesForArea(this.areaId)
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
      goToFaceDetails(faceID) {
        this.$router.push(`/faces/${faceID}/info`); 
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
  
<style scoped>
/* Color scheme
yale-blue: #003B6Dff;
--lapis-lazuli: #00639Aff;
--air-superiority-blue: #6697BCff;
--raw-umber: #856859ff;
--bistre: #433227ff;
*/

/* */
#area {
  display: flex;
  flex-direction: column;
  padding: 30px; /*Adds 30px PADDING to all sides*/
  background: linear-gradient(0deg, #585c5e,#6697BCff); /*Background transition between two colors from bottom up */
  min-height: 100vh; /*Sets the minimum height to 100% of the viewport height*/
  color: #37474f; 
}
#title-section {
  text-align: center;
  padding: 50px 20px;
  margin-bottom: 40px;
  background-color: rgba(255, 255, 255, 0.85); /* Slightly less transparent white */
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  display: inline-block; /* Allows the width to shrink to content */
  width: auto; /* Overrides any default width */
  margin-left: auto;
  margin-right: auto;
}
#title-section h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #1976d2;
}
#area-details {
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

/*ADMIN tools*/
#admin-tools {
  margin-bottom: 20px; /*Adds a BOTTOM margin of 20px between it and the element below */
  /*Centers the button */
  margin-left: auto;
  margin-right: auto;
}

/* Face card section - Similar to Area list */
#face-title{
  text-align: center;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 5px black;
  margin-bottom: 20px;
  margin-top: 20px;
  background-color: #6697BCff;
}
#face-title h2 {
  font-size: 1.6rem;
}
#faces {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-bottom: 20px;
}
#face-list {
  display: grid;
  list-style: none; /*No list style used (removes the bullet points) */
  padding: 20px; /*Adds padding around the list*/
  /*Sets the width of the cards to 300px and wraps them into new rows if there are too many */
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px; /*Sets the gap between cards to be 20px */
  border-radius: 8px; /*Adds rounded corners to the background container*/
}
#card { 
  background-color: #6697BCff;
  color: #424242;
  justify-items: center;
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
.image-container { 
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 15px;
  display: flex;
  justify-content: center;
}
#face-img { 
  display: block;
  max-width: 100%;
  height: auto;
  border-radius: 8px;
}
#name { 
  color: #1976d2;
  margin-top: 10px;
  margin-bottom: 15px;
  font-size: 1.6rem;
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
#info:hover { /* Target the #info:hover button within #face-list */
  background-color: #0d47a1;
  transform: translateY(-3px);
  box-shadow: 0 3px 7px rgba(0, 0, 0, 0.2);
}
#face-buttons{
  display: flex;
  gap: 10px; /* Adds 10px of space between the buttons */
  padding: 10px;
  justify-content: center; /* Centers the buttons horizontally */
}

/* Weather section */
#weather {
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.85);
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
</style>