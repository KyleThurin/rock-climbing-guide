<template>
  <div id="login">
    <form id="login-form" v-on:submit.prevent="login" :class="{ 'shake': loginFailed }">
      <div id="title">
        <h1>Sign In</h1>
      </div>
      <div id="fields">
        <label for="username">Username</label>
        <input type="text" id="username" placeholder="Username" v-model="user.username" required autofocus/>
        
        <label for="password">Password</label>
        <input type="password" id="password" placeholder="Password" v-model="user.password" required/>

        <div>
          <button id="sign-in-btn" type="submit">Sign in</button>
        </div>
      </div>

      <div id="register">
        Need an account?
        <router-link v-bind:to="{ name: 'register' }">Register!</router-link>
      </div>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      //Indicates if the user failed to log in
      loginFailed: false,
    };
  },
  methods: {
    login() {
      //Resets 'loginFailed' to 'false' at the start of each attempt to log in
      this.loginFailed = false;
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch((error) => {
          //If the user fails to log in, set 'loginFailed' to be 'true'
          this.loginFailed = true;
          const response = error.response;
          if (!response) {
            alert(error);
          } else if (response.status === 401) {
            confirm("Invalid username and password!");
          } else {
            alert(response.message);
          }
        });
    },
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

/*Page main/body settings */
#login {
  display: flex;
  flex-direction: column;
  padding: 10px; /*Adds 30px PADDING to all sides*/
  background: linear-gradient(0deg, #585c5e,#6697BCff); /*Background transition between two colors from bottom up */
  min-height: 100vh; /*Sets the minimum height to 100% of the viewport height*/
  flex-direction: column;
  justify-content: flex-start; 
  align-items: center; 
  padding: 30px; 
}
#login-form {
  text-align: center;
  max-width: 600px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.85);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  display: inline-block; /* Allows the width to shrink to content */
  width: auto; /* Overrides any default width */
}


/*Sake animation*/
.shake {
  animation: shake 0.5s ease-in-out;
}
@keyframes shake {
  0% { transform: translateX(0); }
  25% { transform: translateX(-10px); }
  50% { transform: translateX(10px); }
  75% { transform: translateX(-10px); }
  100% { transform: translateX(0); }
}


/*Title section */
#title {
  text-align: center;
  padding: 20px;
  text-align: center;
  border-radius: 8px;
  box-shadow: 0 2px 5px black;
  margin-bottom: 20px;
  background-color: #6697BCff;
}
#title h1{
  font-size: 2.5rem; /*Sets the SIZE of the text to 2.5rem*/
  color: #37474f;
}

/*Info-fields */
#fields {
  display: grid;
  gap: 15px;
}
label {
  /*Input field label settings */
  margin-bottom: 5px;
  font-weight: bold;
  color: #37474f;
}
input[type="text"],
input[type="password"],
input[type="number"] {
  /*Input-bar settings*/
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  background-color: #6697BCff;
  transition: transform 0.2s ease-in-out; 
}
input:hover {
  transform: translateX(-5px);
}

/*Button settings */
#sign-in-btn {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: transform 0.2s ease-in-out; 
}
#sign-in-btn:hover {
  transform: translateX(-5px);
  background-color: #0056b3;
}
/*Bottom*/
#register{
  text-align: center;
  padding: 20px;
  text-align: center;
  border-radius: 8px;
  box-shadow: 0 2px 5px black;
  margin-bottom: 20px;
  margin-top: 20px;
  background-color: #6697BCff;
}
a {
  color: #007bff;
  text-decoration: none;
}
a:hover {
  text-decoration: underline;
}
</style>
