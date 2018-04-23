<template>
    <section>
      <v-flex xs10 offset-xs1 md4 offset-md4 class="my-3">
        <v-card class="py-5">
          <v-layout column wrap align-center justify-center class="mx-4">
              <h2 class="headline">Please enter your registration data</h2>
              <v-text-field name="input-1-3" label="Username" v-model="username" single-line></v-text-field>
              <v-text-field name="input-1-3" label="Name" v-model="name" single-line></v-text-field>
              <v-text-field name="input-1-3" label="Last name" v-model="lastName" single-line></v-text-field>
              <v-text-field name="input-10-1" label="Password" hint="At least 8 characters" v-model="password" min="8" :append-icon="e1 ? 'visibility' : 'visibility_off'" :append-icon-cb="() => (e1 = !e1)" :type="e1 ? 'password' : 'text'"></v-text-field>
              <v-btn color="primary" :loading="loading" @click.native="loader = 'loading'" :disabled="loading">Register</v-btn>
              <v-btn color="primary" flat :to="{name: 'Login'}">Log in</v-btn>
          </v-layout>
        </v-card>
      </v-flex>
    </section>
</template>
<script>
import { REGISTER } from '../store'
export default {
  data () {
    return {

      username: '',
      name: '',
      lastName: '',
      password: '',

      e1: true,

      loader: null,
      loading: false

    }
  },
  watch: {
    loader () {
      let that = this
      const l = that.loader
      that[l] = !that[l]
      let payload = { userName: that.username, password: that.password, name: that.name, lastName: that.lastName  };
      if (that.loader != null) {
        that.$store.dispatch(REGISTER, payload)
        .then(response => {
          that.$router.push('/')
          that[l] = false
        }).catch(error => {
          alert(error)
          that[l] = false
        })
      }
      that.loader = null
    }
  }
}
</script>
