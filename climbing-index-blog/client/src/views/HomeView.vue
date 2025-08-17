<template>
  <div class="home">
    <!--Welcome section-->
    <section id="welcome-section">
      <h1>Welcome to Cliff's Climbing Hub</h1>
      <p>Explore climbing areas, faces, and routes from around MY world!</p>
    </section>
    <!--Facts section-->
    <section id="facts-section">
      <loading-spinner id="spinner" v-bind:spin="isLoading" />
      <!--Area fact card-->
      <div class="fact-card">
        <h3>Climbing Area (Crag)</h3>
        <p class="fact-description">A larger geographical location where climbers gather...</p>
        <ul class="fact-details">
          <li>Diverse routes and opportunities</li>
          <li>Includes cliffs, formations, indoor walls</li>
        </ul>
      </div>
      <!--Face fact card-->
      <div class="fact-card">
        <h3>Face</h3>
        <p class="fact-description">The actual rock wall that climbers ascend...</p>
        <ul class="fact-details">
          <li>Vertical, overhanging, or slab</li>
          <li>Features like cracks and holds</li>
        </ul>
      </div>
      <!--Route fact card-->
      <div class="fact-card">
        <h3>Route</h3>
        <p class="fact-description">A specific path a climber follows on a face...</p>
        <ul class="fact-details">
          <li>Short or multi-pitch</li>
          <li>Rated by difficulty</li>
          <li>Sport, trad, or bouldering</li>
        </ul>
      </div>
    </section>

    <!--Navigation section
    TODO: Remove and replace filler content-->
    <!--Areas nav section-->
    <section id="navigation-section">
      <div id="area" class="nav-card">
        <i class="fas fa-map-marked-alt nav-icon"></i>
        <h2>Climbing Areas</h2>
        <p class="nav-description">Discover amazing climbing destinations.</p>
        <!--Button linking to areas page-->
        <button class="btn-primary" v-on:click="$router.push({ name: 'areas' })">Explore Areas</button>
      </div>

      <!--Faces nav section-->
      <div id="face" class="nav-card">
        <i class="fas fa-mountain nav-icon"></i>
        <h2>Climbing Faces</h2>
        <p class="nav-description">Learn about different rock formations.</p>
        <div class="loading-indicator">
          <input type="checkbox" name="loading" id="loading" v-model="isLoading" />
          <label for="loading">Simulate Loading</label>
        </div>
        <p id="login-message" v-if="!isLoggedIn">
          <router-link v-bind:to="{ name: 'login' }">Login</router-link> to unlock all features!
        </p>
        <!--Button linking to faces page-->
        <button class="btn-primary" v-on:click="$router.push({ name: 'faces' })">View Faces</button>
      </div>

      <!--Routes nav section-->
      <div id="route" class="nav-card">
        <i class="fas fa-hiking nav-icon"></i>
        <h2>Climbing Routes</h2>
        <p class="nav-description">Find your next challenging ascent.</p>
        <div class="view-options">
          <font-awesome-icon
            v-bind:class="{ 'view-icon': true, active: cardView }"
            v-on:click="cardView = true"
            icon="fa-solid fa-grip"
            title="View tiles"
          />
          <font-awesome-icon
            v-bind:class="{ 'view-icon': true, active: !cardView }"
            v-on:click="cardView = false"
            icon="fa-solid fa-table"
            title="View table"
          />
          <span>View: {{ cardView ? 'Tiles' : 'Table' }}</span>
        </div>
        <!--Button linking to routes page-->
        <button class="btn-primary" v-on:click="$router.push({ name: 'routes' })">See Routes</button>
      </div>
    </section>
  </div>
</template>

<script>
import LoadingSpinner from "../components/LoadingSpinner.vue";

export default {
  components: {
    LoadingSpinner,
  },
  data() {
    return {
      isLoading: false,
      cardView: true,
    };
  },
  computed: {
    isLoggedIn() {
      return this.$store.state.token.length > 0;
    },
  },
};
</script>

<style scoped>
/* Color scheme
--yale-blue: #003B6Dff;
--lapis-lazuli: #00639Aff;
--air-superiority-blue: #6697BCff;
--davy'sgrey: #585c5e;
--raw-umber: #856859ff;
--bistre: #433227ff;
*/

/* */
.home {
  padding: 30px; /*Adds 30px PADDING to all sides*/
  background: linear-gradient(0deg, #585c5e,#6697BCff); /*Background transition between two colors from bottom up */
  min-height: 100vh; /*Sets the minimum height to 100% of the viewport height*/
  border-radius: 10px; /*Adds rounded corners to the background container*/
}
#welcome-section {
  text-align: center; /*Aligns the text with the center of the element */
  padding: 50px 20px; /*Top & Bottom: 50px padding; Left & Right: 20px padding;*/
  margin-bottom: 40px; /*Adds a BOTTOM margin of 40px between it and the element below */
  background-color: white; /*Sets the background color of the element */
  border-radius: 10px; /*Adds rounded corners to the background container*/
  box-shadow: 0 4px 8px black; /*Adds shadow effects of 4px along the y-axis, with a blur-radius of 8px */
}
#welcome-section h1 {
  font-size: 2.5rem; /*Sets the SIZE of the text to 2.5rem*/
  margin-bottom: 15px; /*Adds a BOTTOM margin of 15px between it and the element below */
  color: #00639Aff; /*Sets the COLOR of the letters to be blue */
}
#welcome-section p {
  font-size: 1.1rem; /*Sets the SIZE of the text to 1.1rem*/
  line-height: 1.6; /*Sets the height of the line box (spaces between lines of text)*/
}

/*Fact-section */
#facts-section {
  background-color: #f9f9f9; /*Sets the background color of the element to be off-white*/
  padding: 30px; /*Adds 30px PADDING to all sides*/
  margin-bottom: 40px; /*Adds a BOTTOM margin of 40px between it and the element below */
  border-radius: 10px; /*Adds rounded corners to the background container*/
  box-shadow: 0 4px 8px black; /*Adds shadow effects of 4px along the y-axis, with a blur-radius of 8px */
  display: grid;
  /*Sets the width of the cards to 300px and wraps them into new rows if there are too many */
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); 
  gap: 25px; /*Sets the gap between cards to be 25px */
  
}
.fact-card {
  background-color: #979494; /*Sets the background color of the element to be medium light grey */
  padding: 20px; /*Adds 20px PADDING to all sides*/
  border-radius: 8px; /*Adds rounded corners to the background container*/
  border: 1px solid #a09696; /*Adds a 1px solid medium grey border around each card*/
  box-shadow: 0 2px 4px black; /*Adds shadow effects of 2px along the y-axis, with a blur-radius of 4px */
  /*When transforming the card, smoothly animate the change over a duration of 0.2 seconds*/
  transition: transform 0.2s ease-in-out; 
}
.fact-card:hover {
  /*When the mouse hovers over the fact-cards: */
  transform: translateY(-5px); /*Moves the card up by 5px along the y-axis*/
  box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2); /*Adds shadow effects of 5px along the y-axis, with a blur-radius of 10px */
}
.fact-card h3 {
  color: #1976d2; /*Sets the COLOR of the letters to be primary blue */
  margin-top: 0; /*Adds a TOP margin of 0px between it and the element above */
  margin-bottom: 15px; /*Adds a BOTTOM margin of 15px between it and the element below */
  font-size: 1.6rem; /*Sets the SIZE of the text to 1.6rem*/
}
.fact-description {
  color: #424242; /*Sets the COLOR of the letters to be dark grey */
  line-height: 1.5; /*Sets the height of the line box (spaces between lines of text)*/
  margin-bottom: 10px; /*Adds a BOTTOM margin of 10px between it and the element below */
}
.fact-details {
  list-style: none; /*No list style used (removes the bullet points) */
  padding: 0;
  margin-left: 15px;
}
.fact-details li::before {
  content: "•";
  color: #1976d2; /* Primary blue bullet */
  display: inline-block;
  width: 1em;
  margin-left: -1em;
}

/*Navigation section*/
#navigation-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 30px;
}
.nav-card {
  background: linear-gradient(135deg, #bbdefb, #64b5f6); 
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  text-align: center;
  color: rgb(224, 15, 15);
  transition: transform 0.2s ease-in-out;
}
.nav-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.25);
}
.nav-icon {
  font-size: 2.5rem;
  margin-bottom: 20px;
  color: rgba(255, 255, 255, 0.8);
}
.nav-card h2 {
  font-size: 2rem;
  margin-top: 0;
  margin-bottom: 15px;
  color: #37474f;
}
.nav-description {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 25px;
  color: #37474f;
}
/*Button section*/
.btn-primary {
  background-color: #1976d2; 
  color: white;
  border: none;
  padding: 12px 25px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1.1rem;
  transition: background-color 0.3s ease, transform 0.2s ease-in-out;
}
.btn-primary:hover {
  background-color: #0d47a1; 
  transform: scale(1.05);
}

#spinner {
  color: #1976d2; 
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 100;
}
/*Playing with what's already there */
.loading-indicator {
  margin-bottom: 20px;
  color: white;
  opacity: 0.8;
}
#login-message {
  color: #37474f;
  margin-top: 15px;
}
#login-message a {
  color: #f44336;
  text-decoration: none;
  font-weight: bold;
}
#login-message a:hover {
  text-decoration: underline;
}
.view-options {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}
.view-icon {
  font-size: 1.4rem;
  margin-right: 12px;
  padding: 10px;
  color: #e3f2fd;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease, transform 0.2s ease-in-out;
}
.view-icon.active {
  background-color: #1976d2; 
  color: white;
}
.view-icon:not(.active):hover {
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  transform: scale(1.1);
}
.view-options span {
  color: #f44336;
  font-size: 1.1rem;
}
</style>