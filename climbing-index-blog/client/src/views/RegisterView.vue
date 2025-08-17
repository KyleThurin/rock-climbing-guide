<template>
  <div id="register">
    <form id="register-form" v-on:submit.prevent="register">
      <div id="title">
        <h1>Create Account</h1>
      </div>
      <div id="fields">
        <label for="username">Username</label>
        <input type="text" id="username" placeholder="Username" v-model="user.username" required autofocus/>

        <label for="name">Name</label>
        <input type="text" id="name" placeholder="Name" v-model="user.name" required/>

        <label for="password">Password</label>
        <input type="password" id="password" placeholder="Password" v-model="user.password" required/>

        <label for="confirmPassword">Confirm password</label>
        <input type="password" id="confirmPassword" placeholder="Confirm Password" v-model="user.confirmPassword" required/>

        <label for="address">Address</label>
        <input type="text" id="address" placeholder="Address" v-model="user.address"/>

        <label for="city">City</label>
        <input type="text" id="city" placeholder="City" v-model="user.city" />

        <label for="state">State</label>
        <input type="text" id="state" placeholder="State" v-model="user.stateCode" maxlength="2" required/>

        <label for="zip">ZIP</label>
        <input type="number" id="zip" placeholder="ZIP" v-model="user.zip" required minlength="5" maxlength="5"/>

        <div>
          <button id="submit-btn" type="submit">Create Account</button>
        </div>
      </div>
      <div id="sign-in">
        Have an account?
        <router-link v-bind:to="{ name: 'login' }">Sign in!</router-link>
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
        name: "",
        password: "",
        confirmPassword: "",
        address: "",
        city: "",
        stateCode: "",
        zip: "",
        role: "user",
      },
    };
  },
  methods: {
    error(msg) {
      alert(msg);
    },
    success(msg) {
      alert(msg);
    },
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.error("Password & Confirm Password do not match");
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.success("Thank you for registering, please sign in.");
              this.$router.push({
                path: "/login",
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            if (!response) {
              this.error(error);
            } else if (response.status === 400) {
              if (response.data.errors) {
                // Show the validation errors
                let msg = "Validation error: ";
                for (let err of response.data.errors) {
                  msg += `'${err.field}':${err.defaultMessage}. `;
                }
                this.error(msg);
              } else {
                this.error(response.data.message);
              }
            } else {
              this.error(response.data.message);
            }
          });
      }
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
#register {
  display: flex;
  flex-direction: column;
  padding: 30px; /*Adds 30px PADDING to all sides*/
  background: linear-gradient(0deg, #585c5e,#6697BCff); /*Background transition between two colors from bottom up */
  min-height: 100vh; /*Sets the minimum height to 100% of the viewport height*/
  flex-direction: column;
  justify-content: center; /* Centers content vertically */
  align-items: center; /* Centers content horizontally */
  padding: 30px; /* Adds 30px PADDING to all sides */
}
#register-form {
  text-align: center;
  max-width: 600px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.85); /* Slightly less transparent white */
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  display: inline-block; /* Allows the width to shrink to content */
  width: auto; /* Overrides any default width */
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
  gap: 15px; /* Adjust the gap for spacing between steps */
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
#submit-btn {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: transform 0.2s ease-in-out; 
}
#submit-btn:hover {
  transform: translateX(-5px);
  background-color: #0056b3;
}

/*Bottom*/
#sign-in{
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
