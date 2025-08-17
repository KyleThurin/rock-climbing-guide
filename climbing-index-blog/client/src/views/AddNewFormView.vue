<template>
  <div id="new-form">
    <section id="title-section">
      <h1 id="title">Add New Climbing Area</h1>
      <p>Please fill out the form below to add a new climbing area.</p>
    </section>

    <form id="area-form">
      <div class="form-group">
        <label for="areaName">Area Name:</label>
        <input type="text" id="areaName" v-model="newArea.areaName" required>
      </div>

      <div class="form-group">
        <label for="latitude">Latitude:</label>
        <input type="number" step="0.000001" id="latitude" v-model="newArea.latitude" required>
      </div>

      <div class="form-group">
        <label for="longitude">Longitude:</label>
        <input type="number" step="0.000001" id="longitude" v-model="newArea.longitude" required>
      </div>

      <div class="form-group">
        <label for="areaDetails">Area Details:</label>
        <textarea id="areaDetails" v-model="newArea.areaDetails"></textarea>
      </div>

      <button type="submit" id="submit-button" v-on:click="submitForm">Add Area</button>
      <button type="button" id="cancel-button" @click="$router.push({ name: 'areas' })">Cancel</button>
    </form>
  </div>
 
</template>

<script>
import areaService from '../services/AreaService';

export default {
  data() {
    return {
      newArea: {
        areaName: '',
        latitude: null,
        longitude: null,
        areaDetails: ''
      },
      error: null
    };
  },
  methods: {
    submitForm() {
      areaService.createArea(this.newArea)
        .then(response => {
          if (response && response.data) {
            this.newArea = { areaName: '', latitude: null, longitude: null, areaDetails: '' };
            this.$router.push({ name: 'areas' });
          } else {
            this.error = "Failed to add the new area.";
            console.error("Error creating area: ", response);
          }
        })
        .catch(error => {
          this.error = "There was an error adding the new area.";
          console.error("Error creating area:", error);
        });
    }
  }
};

</script>

<style scoped>
/*TODO Update */
#new-form {
  padding: 30px;
  background: linear-gradient(135deg, #73bcec, #585c5e);
  min-height: 100vh;
  color: #37474f;
  display: flex;
  flex-direction: column;
  align-items: center;
}

#title-section {
  text-align: center;
  padding: 30px 20px;
  margin-bottom: 30px;
  background-color: rgba(255, 255, 255, 0.85);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  width: 80%;
  max-width: 600px;
}

#title-section h1 {
  font-size: 2.2rem;
  margin-bottom: 10px;
  color: #1976d2;
}

#title-section p {
  font-size: 1.1rem;
  line-height: 1.6;
}

#area-form {
  background-color: #f9f9f9;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  width: 80%;
  max-width: 600px;
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #37474f;
}

.form-group input[type="text"],
.form-group input[type="number"],
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1rem;
  box-sizing: border-box;
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

#submit-button,
#cancel-button {
  background-color: #1976d2;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

#cancel-button {
  background-color: #6c757d;
  margin-top: 10px;
}

#submit-button:hover {
  background-color: #0d47a1;
}

#cancel-button:hover {
  background-color: #545b62;
}
</style>