<template>

    <section>
      <v-flex xs10 offset-xs1 class="my-3">
        <v-card class="py-5">
          <v-layout column wrap align-center justify-center class="mx-4">
              <h2 class="headline">Please enter your credentials</h2>
              <v-text-field 
                name="input-1-3" 
                label="Username" 
                v-model="username"
                single-line
              ></v-text-field>
              <v-text-field
                name="input-10-1"
                label="Password"
                hint="At least 8 characters"
                v-model="password"
                min="8"
                :append-icon="e1 ? 'visibility' : 'visibility_off'"
                :append-icon-cb="() => (e1 = !e1)"
                :type="e1 ? 'password' : 'text'"
              ></v-text-field>
                  <v-btn
                    color="primary"
                    :loading="loading"
                    @click.native="loader = 'loading'"
                    :disabled="loading"
                  >
                    Sign in
                  </v-btn>
          </v-layout>
        </v-card>
      </v-flex>
    </section>

</template>
<script>
export default {
  data () {
    return {

      username: '',
      password: '',

      e1: true,

      loader: null,
      loading: false

    }
  },
  watch: {
    loader () {
      const l = this.loader
      this[l] = !this[l]
      console.log(l)
      setTimeout(()=>(this[l] = false),3000)
      this.loader = null
    }
  }
}
</script>
