<template>
  <div>
    <v-breadcrumbs divider="/">
      <v-breadcrumbs-item v-for="item in breadcrumbs" :key="item.text" :disabled="item.disabled" :to="item.route">
        {{ item.text }}
      </v-breadcrumbs-item>
    </v-breadcrumbs>
    <section>
      <v-flex>
        <v-card class="py-5 px-3">
          <v-layout column>
            <h1>{{client.name}} {{client.lastName}} ({{client.code}})</h1>
            <dl class="mt-3">
              <dt><b>Area</b></dt>
              <dd>{{currentArea.name}}</dd>
              <dt><b>Creation date</b></dt>
              <dd>{{client.createdDate}}</dd>
              <dt><b>Active</b></dt>
              <dd><v-checkbox v-model="client.active" disabled></v-checkbox></dd>
            </dl> 
          </v-layout>
          <!-- <v-layout column wrap align-center justify-center class="mx-4">
          </v-layout> -->
        </v-card>
      </v-flex>
    </section>
  </div>
  
</template>
<script>
import { FINDCLIENT, FINDAREA } from '@/store'
export default {
  data () {
    return {
      client: {},
      currentArea: {},
      breadcrumbs: [
        {
          text: 'Clients',
          disabled: false,
          route: '/clients'
        },
        {
          text: '',
          disabled: false,
          route: '#'
        }
      ]
    }
  },
  created () {
    this.initialize()
  },
  methods: {
    initialize () {
      let that = this
      that.$store.dispatch(FINDCLIENT, { id: that.$route.params.id })
      .then(response => {
        that.client = response.data
        that.breadcrumbs[1].text = response.data.code
        that.$store.dispatch(FINDAREA, { id: response.data.areaid })
        .then(areaResponse => {
          that.currentArea = areaResponse.data
        })
        .catch(areaError => { console.log(areaError) })
      })
      .catch(error => { console.log(error) })
    }
  }
}
</script>

