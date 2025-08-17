<template>
  <div id="faces-view">
    <section id="title-section">
      <h1 id="title">Climbing Faces</h1>
      <p>Explore the climbing faces organized by area.</p>
    </section>

    <section id="admin-tools">
      <!--A button linking to the new face form that only appears for those logged in as ADMIN-->
      <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="$router.push({ name: 'new-form' })">
        Add New Face
      </button>
    </section>

    <div id="areas-container">
      <div v-for="area in areasWithFaces" :key="area.areaID" class="area-group">
        <!--Area name -->
        <h2 class="area-name">{{ area.areaName }}</h2>
        <!--If an 'area' has 'faces' and the length is greater than zero, create 'face-list'-->
        <ul id="face-list" v-if="area.faces && area.faces.length > 0">
          <!--Loop through the faces in the current area and lists the items for each-->
          <li id="card" v-for="face in area.faces" :key="face.faceID">
            <!--Displays the image of the current face-->
            <img id="face-img" :src="getImagePath(face.faceName)" :alt="face.faceName" />
            <!--Displays the name of the face-->
            <h3 id="name">{{ face.faceName }}</h3>
            <!--Button linked to the faces' info page-->
            <button id="info" @click="goToFaceDetails(face.faceID)">Face Details</button>

            <!--Button navigation section-->
            <nav id="face-buttons">
              <!--ADMIN Tool: Button to UPDATE area-->
              <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="editFace(face.faceID)">
                Update Face
              </button>
              <!--ADMIN Tool: Button to DELETE area-->
              <button id="info" v-if="$store.state.user.role === 'ROLE_ADMIN'" @click="deleteFace(face.faceID, face.faceName, area.areaName)">
                Delete Face
              </button>
            </nav>
          </li>
        </ul>
        <!--If the current 'area' doesn't have any faces linked to it-->
        <p v-else class="no-faces-message">
          <!--Display this message-->
          No faces listed for this area at this time.
        </p>
      </div>
        <p v-if="areasWithFaces.length === 0 && !error" class="no-areas-message">
          No climbing areas found with faces.
        </p>
        <p v-if="error" class="error-message">{{ error }}</p>
    </div>
  </div>
</template>

<script>
import faceService from '../services/FaceService';
import areaService from '../services/AreaService';

export default {
  data() {
    return {
      areas: [],
      faces: [],
      error: null,
    };
  },
  computed: {
    areasWithFaces() {
      //Creates 'areasMap', mapping an empty 'faces' array to each of the 'areas'
      const areasMap = new Map(this.areas.map(area => [area.areaID, { ...area, faces: [] }]));
      //Loops through all the faces
      this.faces.forEach(face => {
        //If the current area's 'areaID' matches the current faces '.areaID'
        console.log("Processing face in areasWithFaces:", face);
        if (areasMap.has(face.areaID)) {
          //It maps it together
          areasMap.get(face.areaID).faces.push(face);
        }
      });
      return Array.from(areasMap.values());
    }
  },
  methods: {
    fetchAllData() {
      areaService.getAllAreas()
        .then(areasResponse => {
          this.areas = areasResponse.data;
          console.log("Fetched areas:", this.areas); // Log fetched areas
          return faceService.getAllFaces();
        })
        .then(facesResponse => {
          this.faces = facesResponse.data;
          console.log("Fetched faces:", this.faces); // Log fetched faces
        })
        .catch(error => {
          console.error('Error fetching data:', error);
          this.error = 'Failed to load climbing areas & faces.';
        });
    },
    //Navigates to the info page for the area when the button is pushed
    goToFaceDetails(faceID) {
      console.log("Navigating with faceId:", faceID); // Log the ID being used for navigation
      if (faceID) { // Only navigate if faceId is not undefined
        this.$router.push(`/faces/${faceID}/info`);
      } else {
        console.error("Attempted to navigate to FaceInfoView with an undefined faceId:",faceID);
        // Optionally, you could show an error message to the user
      }
      //this.$router.push(`/faces/${faceId}/info`); 
    },
    //Navigates to the info page for the area to be edited when the button is pushed
    editFace(faceId) {
      //TODO: I need help with this
      alert('Calm down, I am still working on this!');
      console.log('Calm down, I am still working on this!');
    },
    //TODO: Need to fix this
    //Deletes the selected area
    deleteFace(faceId, faceName, areaName) {
      //Displays confirmation alert to delete face
      if (confirm(`Are you sure you want to delete "${faceName}" (ID: ${faceId}) from area "${areaName}"? `)) {
        faceService.deleteFace(faceId)
          .then(() => {
            alert(`Face "${faceName}" has been deleted.`);
            this.fetchAllData();
          })
          .catch(error => {
            console.error(`Error deleting face with ID ${faceId}:`, error);
            alert(`Failed to delete face "${faceName}". Please try again.`);
          });
      }
    },
    getImagePath(faceName) {
      try {
        const imageName = `${faceName}.jpg`;
        return new URL(`../imgs/${imageName}`, import.meta.url).href;
      } catch (error) {
        return new URL(`../imgs/test.jpg`, import.meta.url).href;
      }
    },
  },
  created() {
    this.fetchAllData();
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

#faces-view {
  display: flex;
  flex-direction: column;
  padding: 30px;
  background: linear-gradient(0deg, #585c5e, #6697BCff);
  min-height: 100vh;
  color: #37474f;
}
/*Title*/
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
#title-section p {
  font-size: 1.1rem;
  line-height: 1.6;
}

#admin-tools {
  margin-bottom: 20px;
  margin-left: auto;
  margin-right: auto;
}
/*Area cards*/
#areas-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
}
.area-group {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.area-name {
  color: #003B6Dff;
  margin-top: 0;
  margin-bottom: 20px;
  text-align: center;
  font-size: 2rem;
  padding-bottom: 10px;
}
#face-list {
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
#face-img {
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
#face-buttons {
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
.no-areas-message,
.no-faces-message{
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
</style>