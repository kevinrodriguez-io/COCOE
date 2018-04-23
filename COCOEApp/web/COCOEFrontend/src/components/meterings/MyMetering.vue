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
          <h1>{{metersession.header}} ({{metersession.code}})</h1>
           <dl class="mt-3">
            <dt><b>Area</b></dt>
            <dd>{{currentArea.name}}</dd>
            <dt><b>Creation date</b></dt>
            <dd>{{metersession.createdDate}}</dd>
            <dt><b>Status</b></dt>
            <dd>{{metersession.status}}</dd>
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
import { FINDMETERSESSION, FINDAREA } from '@/store'
export default {
  data() {
    return {
      metersession: {},
      currentArea: {},
      breadcrumbs: [
        {
          text: 'Meterings',
          disabled: false,
          route: '/mymeterings'
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
      that.$store.dispatch(FINDMETERSESSION, { id: that.$route.params.id })
      .then(response => {
        that.metersession = response.data
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
