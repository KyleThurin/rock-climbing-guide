<template>
  <div id="areas">
    <!--Title of page-->
    <section id="title-section">
      <h1 id="title">Climbing Areas</h1>
      <p>Select the climbing area you are interested in</p>
    </section>

    <!--ADMIN tools-->
    <section id="admin-tools">
      <!--A button linking to the new area form that only appears for those logged in as ADMIN-->
      <button id="info"  v-on:click="$router.push({ name: 'new-form' })">
      Add New Area
      </button>
    </section>

    <!--Creates 'area-list'-->
    <ul id="area-list">
      <!--Loop through the areas and lists the items for each-->
      <li id="card" v-for="area in areas" :key="area.areaID">
        <!--Displays the image of the area
        <img id="area-img" :src="getImagePath(area.areaName)" :alt="area.areaName" style="max-width: 300px; height: auto;">-->
        <img id="area-img" :src="getImagePath(area.areaName)" :alt="area.areaName">
        <!--Displays the Area's name-->
        <h2 id="name">{{ area.areaName }}</h2>
        <!--Button linked to the area's info page-->
        <button id="info" @click="goToAreaDetails(area.areaID)">Area Details</button>

        <!--Button navigation section-->
        <nav id="area-buttons">
          <!--ADMIN Tool: Button to UPDATE area-->
          <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="goToAreaDetails(area.areaID)">
            Update Area
          </button>
          <!--ADMIN Tool: Button to DELETE area-->
          <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="deleteArea(area.areaID, area.areaName)">
            Delete Area
          </button>
        </nav>
      </li>
    </ul>
  </div>
</template>
  
<script>
  import areaService from '../services/AreaService';
  
  export default {
  data() {
    return {
      areas: [],
      showImage: true,
      error: null,
    };
  },
  methods: {
    //Gets all 'areas'
    getAreas() {
      areaService.getAllAreas()
        .then(response => {
          this.areas = response.data;
        })
        .catch(error => {
          console.error("Error finding areas:", error);
          this.error = "Failed to load climbing areas.";
        });
    },
    //Navigates to the info page for the area when the button is pushed
    goToAreaDetails(areaId) {
      this.$router.push(`/areas/${areaId}/info`); 
    },
    //Finds and displays the area image
    getImagePath(areaName) {
      //Try to find an image using the area's name
      try {
        const imageName = `${areaName}.jpg`;
        return new URL(`../imgs/${imageName}`, import.meta.url).href;
      } catch (error) {  //If no img is found
        //Use the 'test.jpg'
        return new URL(`../imgs/test.jpg`, import.meta.url).href;
      }
    },
    //Deletes the selected area
    deleteArea(areaId, areaName){
      //Displays confirmation alert to delete area
      if (confirm(`Are you sure you want to delete "${areaName}"?`)) {
        if (confirm(`The programmer spent a lot of time on this, and you're just going to delete "${areaName}"???`)) {
          areaService.deleteArea(areaId)
          .then(response => {
            //Alerts the user of what they have done
            alert(`You win... Area with ID ${areaId} has been deleted.`);
            this.getAreas(); //Refresh the list after deleting area
          })
          .catch(error => {
            console.error(`Error deleting area with ID ${areaId}:`, error);
            alert(`Failed to delete area with ID ${areaId}. This is probably a sign that you chose WRONG!!!!.`);
          })
        }
      }
    },
  },
  created() {
    //Call 'getAreas' when the component is created
    this.getAreas(); 
  },
};
</script>
  
<style scoped>
/* Color scheme
--yale-blue: #003B6Dff;
--lapis-lazuli: #00639Aff;
--air-superiority-blue: #6697BCff;
--raw-umber: #856859ff;
--bistre: #433227ff;
*/

/* */
#areas {
  display: flex;
  flex-direction: column;
  padding: 30px; /*Adds 30px PADDING to all sides*/
  background: linear-gradient(0deg, #585c5e,#6697BCff); /*Background transition between two colors from bottom up */
  min-height: 100vh; /*Sets the minimum height to 100% of the viewport height*/
  color: #37474f;
}
/*Title section */
#title-section {
  text-align: center;
  padding: 50px 20px; /*Top & Bottom: 50px padding; Left & Right: 20px padding;*/
  margin-bottom: 40px;
  background-color: rgba(255, 255, 255, 0.85);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  display: inline-block; /* Allows the width to shrink to content */
  width: auto; /* Overrides any default width */

}
#title-section h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #1976d2;
}
#title-section p {
  font-size: 1.1rem;
  line-height: 1.6;
}
/*ADMIN tools*/
#admin-tools {
  margin-bottom: 20px; /*Adds a BOTTOM margin of 20px between it and the element below */
  /*Centers the button */
  margin-left: auto;
  margin-right: auto;
}
/*Area card section */
#area-list {
  display: grid;
  list-style: none; /*No list style used (removes the bullet points) */
  background-color: #f9f9f9; /*Sets the background color of the element to be off-white*/
  padding: 20px; /*Adds 30px PADDING to all sides*/
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
#area-img {
  display: block;
  border-radius: 8px;
  max-width: 300px;
  min-height: 200px;
  height: auto;
}
#name {
  color: #1976d2;
  margin-top: 10px;
  margin-bottom: 15px;
  font-size: 1.6rem;
}
#area-buttons{
  display: flex;
  gap: 10px; /* Adds 10px of space between the buttons */
  padding: 10px;
  justify-content: center; /* Centers the buttons horizontally */
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