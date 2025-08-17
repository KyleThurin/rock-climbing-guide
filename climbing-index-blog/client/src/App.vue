<template>
  <div id="cart-app">
    <header class="app-header">
      <nav id="nav-bar">
        <!--Button navigation section-->
        <div id="btns">
          <!--If a token is present in '$store.state', display the 'Logout' button-->
          <button id="btn" v-if="$store.state.token" @click="$router.push({ name: 'logout' })">Logout</button>
          <!--If it is NOT present, display the 'Login' button-->
          <button id="btn" v-else @click="$router.push({ name: 'login' })">Login</button>
          <!--Displays the navigation buttons on top of the image only when NOT on home page-->
          <button id="btn" class="btn-home" v-if="$route.name !== 'home'" v-on:click="$router.push({ name: 'home' })"> Home</button>
          <button id="btn" class="btn-areas" v-if="$route.name !== 'home'" v-on:click="$router.push({ name: 'areas' })">Areas</button>
          <button id="btn" class="btn-faces" v-if="$route.name !== 'home'" v-on:click="$router.push({ name: 'faces' })">Faces</button>
          <button id="btn" class="btn-routes" v-if="$route.name !== 'home'" v-on:click="$router.push({ name: 'routes' })">Routes</button>
        </div>
        <!--TODO: Get search function working-->
        <div id="searchBar">
          <!--Search input-->
          <input type="text" id="searchInput" placeholder="Search...">
          <!--Search submit button-->
          <button id="btn" type="submit">Search</button>
        </div>
      </nav>
      <div id="cover-container">
        <!--Cover image-->
        <img src="https://cdn.mos.cms.futurecdn.net/XuvEg8dVRccxXJmgErqsSi-650-80.jpg.webp" alt="Cover Image">
        <!--Displays the app's name on top of the image only when NOT on home page-->
        <h1 id="app-name" v-if="$route.name !== 'home'">Cliff's Climbing Hub</h1>
      </div>
    </header>

    <!--Main/Body-->
    <main>
      <router-view />
    </main>

    <footer class="app-footer">
       &copy; 2025. All rights reserved.    
   </footer>
  </div>
</template>

<style scoped>
/* Color scheme
yale-blue: #003B6Dff;
--lapis-lazuli: #00639Aff;
--air-superiority-blue: #6697BCff;
--raw-umber: #856859ff;
--bistre: #433227ff;
*/

/* */
#cart-app{
  background-color: #6697BCff;
}
.app-header{
  padding: 25px; /*Adds 10px PADDING to all sides*/
  
}
#nav-bar{
  background-color: rgba(255, 255, 255, 0.85);
  display: flex;
  flex-wrap: nowrap; /*Flex items are laid out in a single line and don't wrap */
  justify-content: space-between; /*Aligns flex items to the start of the main axis */
  width: 100%; /*Sets the width to take up 100% of its space */
  z-index: 10; /*Sets the nav-bar above other elements in header area*/
  padding: 10px; /*Adds 10px PADDING to all sides*/
  border-radius: 10px; /*Adds rounded corners to the background container*/
  /*list-style-type: none; */
}
#cover-container {
  width: 100%; /*Sets the width to take up 100% of its space */
  height: 350px; /*Sets the hight to be 350px*/
  position: relative; /*Needed for absolute positioning of the app-name */
  overflow: hidden; /*Keeps the image within the container bounds */
  padding-top: 35px;
  padding-bottom: 35px;
}
#cover-container img {
  width: 100%; /*Sets the image width to equal the full width of the container */
  height: 100%; /*Sets the image width to equal the full height of the container */
  object-fit: cover; /* Maintain aspect ratio and cover the entire container */
  border-radius: 10px; /*Add rounded corners to the background container*/
}
#app-name {
  display: flex;
  position: absolute; /* Position the app-name relative to the nearest positioned ancestor (cover-container) */
  bottom: 40px; /* Position the BOTTOM edge 10px from the bottom of the container */
  left: 40px; /*Position the LEFT edge 10px from the left of the container */
  color: white; /*Sets the COLOR of the letters to be white */
  font-size: 2em; /*Sets the SIZE of the text to 2em*/
  text-align: left; /*Centers the text within its container */
  padding: 10px;    /*Adds 10px PADDING to all sides*/
  background-color: rgba(0, 0, 0, 0.445); /*Adds a semi-transparent background*/
  border-radius: 5px; /*Add rounded corners to the background container*/
}
#btns {
  display: flex; 
  justify-content: space-between; 
  gap: 10px; 
}
#btn {
  background-color: #6697BCff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: transform 0.2s ease-in-out; 
}
#searchBar {
  display: flex; 
  justify-content: space-between; 
  gap: 10px; 
}
input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  background-color: #6697BCff;
  transition: transform 0.2s ease-in-out;
}
.app-footer{
  background-color: #585c5e; /*Sets the footer background color */
}
</style>