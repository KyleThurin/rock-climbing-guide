<template>
    <h1 class="climbing-area">{{ area.areaName }}</h1>
    <h2>Details</h2>

  </template>
    
    <script>
    import areaService from '../services/AreaService';
    
    export default {
    data() {
      return {
        area: [],
        error: null,
      };
    },
    methods: {
      getArea() {
        areaService.getAreaById()
          .then(response => {
            this.areas = response.data;
          })
          .catch(error => {
            console.error("Error finding area:", error);
            this.error = "Failed to load climbing area.";
          });
      },
      getFaces(areaId) {
        // Logic for fetching faces (you might navigate to a different view)
        console.log(`List of faces for area ID: ${areaId}`);
        this.$router.push(`/areas/${areaId}/faces`); // Example navigation
      },
      getWeather(areaId) {
        areaService.getAreaWeatherByID(areaId)
          .then(response => {
            alert(`Current Weather: ${response.data}`);
          })
          .catch(error => {
            console.error("Error finding current weather:", error);
            alert("Failed to load weather information.");
          });
      },
    },
    created() {
      //Call
      this.getAreas(); // Call the function to fetch areas when the component is created
    },
  };
  
    </script>
    
    <style scoped>
    </style>