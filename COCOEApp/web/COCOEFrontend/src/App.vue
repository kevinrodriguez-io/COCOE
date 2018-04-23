<template>
  
  <v-app v-if="isLoggedIn">
    <v-navigation-drawer blue persistent :mini-variant="miniVariant" :clipped="clipped" v-model="drawer" enable-resize-watcher app>
      <v-list>
        <v-list-tile value="true" v-for="(item, i) in items" :key="i" @click="1 == 1" :to="item.route">
          <v-list-tile-action>
            <v-icon v-html="item.icon"></v-icon>
          </v-list-tile-action>
          <v-list-tile-content>
            <v-list-tile-title v-text="item.title"></v-list-tile-title>
          </v-list-tile-content>
        </v-list-tile>
      </v-list>
    </v-navigation-drawer>
    <v-toolbar app :clipped-left="clipped" class="indigo darken-3" dark >
      <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
      <v-btn icon @click.stop="miniVariant = !miniVariant">
        <v-icon v-html="miniVariant ? 'chevron_right' : 'chevron_left'"></v-icon>
      </v-btn>
      <!-- <v-btn icon @click.stop="clipped = !clipped">
        <v-icon>web</v-icon>
      </v-btn> -->
      <!-- <v-btn icon @click.stop="fixed = !fixed">
        <v-icon>remove</v-icon>
      </v-btn> -->
      <v-toolbar-title><v-icon>lightbulb_outline</v-icon> COCOE</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn @click="exit" class="indigo darken-5"> Exit
        <v-icon>exit_to_app</v-icon>
      </v-btn>
    </v-toolbar>
    <v-content>
      <router-view/>
    </v-content>
    <v-footer :fixed="fixed" app>
      <span>&copy; 2018</span>
    </v-footer>
  </v-app>

  <v-app v-else>
    <router-view/>
  </v-app>

</template>

<script>
import { LOGOUT } from './store'
export default {
  data() {
    return {
      clipped: true,
      drawer: true,
      fixed: false,
      items: [
        {
          icon: "place",
          title: "Areas",
          route: "/areas"
        },
        {
          icon: "person",
          title: "Clients",
          route: "/clients"
        },
        {
          icon: "pie_chart",
          title: "Meterings",
          route: "/meterings"
        },
        {
          icon: "insert_drive_file",
          title: "Billing",
          route: "/client"
        }
      ],
      miniVariant: false
    };
  },
  computed: {
    isLoggedIn() {
      return this.$store.getters.isUserLoggedIn
    }
  },
  methods: {
    exit (event) {
      this.$store.dispatch(LOGOUT)
      this.$router.push({name:'Login'})
    }
  },
  name: "App"
};
</script>
