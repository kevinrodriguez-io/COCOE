<template>
    <section>
      <div>
        <v-card>
          <v-card-title>
            Meterings
            <v-spacer></v-spacer>
            <v-text-field append-icon="search" label="Search" single-line hide-details v-model="search" ></v-text-field>
          </v-card-title>
          <v-data-table :headers="headers" :items="items" hide-actions class="elevation-1" >
            <template slot="items" slot-scope="props">
              <td>{{ props.item.name }}</td>
              <td>{{ props.item.lastName }}</td>
              <td>
                <v-text-field v-model.number="props.item.amount" :disabled="props.item.id > -1" @blur="create(props.item)"></v-text-field>
              </td>
            </template>
            <template slot="no-data">
              <v-alert :value="true" color="info" icon="info">
                Empty results
              </v-alert>
            </template>
          </v-data-table>
        </v-card>
      </div>
    </section>
</template>
<script>
  import { FINDMETERSESSION, GETCLIENTSBYAREA, FINDCLIENTMETERINGBYCLIENTANDMETERSESSION, CREATECLIENTMETERING } from '@/store'
  export default {
    props: [ 'meterSessionId' ],
    data: () => ({
      headers: [
        { text: 'Client name', value: 'name' },
        { text: 'Client last name', value: 'lastName' },
        { text: 'Metering', value: 'amount' }
      ],
      allItems: [],
      items: [],
      search: '',

      meterSession: null,
      clients: []

    }),

    watch: {
      search (text) {
        if (text.length == 0) {
          this.items = this.allItems.slice()
          return
        }
        this.items = this.allItems.slice()
        this.items = this.items.filter(I => 
          I.name.toLowerCase().includes(text.toLowerCase()) ||
          I.lastName.toLowerCase().includes(text.toLowerCase())
        )
      }
    },

    created () {
      this.initialize()
    },

    methods: {
      initialize () {
        let that = this
        
        /**********************************
         * Request this meter session
        **********************************/
        that.$store.dispatch(FINDMETERSESSION, { id: that.meterSessionId })
        .then(metersessionResponse => {
          that.meterSession = metersessionResponse.data

          /**********************************
           * START - Request clients for this area
          **********************************/
          that.$store.dispatch(GETCLIENTSBYAREA, { areaid: that.meterSession.areaid })
          .then(clientsByAreaRequest => {
            that.clients = clientsByAreaRequest.data
            that.clients.forEach(client => {
              var itemToAdd = {
                id: -1,
                clientId: client.id,
                name:     client.name,
                lastName: client.lastName,
                amount: 0
              }

              /**********************************
               * Request client metering with 
               * client id and area
              **********************************/
              that.$store.dispatch(FINDCLIENTMETERINGBYCLIENTANDMETERSESSION, { clientid: client.id, metersessionid: that.meterSession.id })
              .then(clientMeteringResponse => {
                itemToAdd.id = clientMeteringResponse.data.id
                itemToAdd.clientId = client.id
                itemToAdd.amount = clientMeteringResponse.data.amount
                that.allItems.push(itemToAdd)
                // No more to fetch?
                that.items = that.allItems.slice()
              })
              .catch(clientMeteringError => { 
                if (clientMeteringError.response) {
                  if (clientMeteringError.response.status) {
                    if (clientMeteringError.response.status == 404) {
                      that.allItems.push(itemToAdd)
                      that.items = that.allItems.slice()
                      return
                    }
                  }
                }
                console.log(clientMeteringError) 
              })
            });
          })
          .catch(clientsByAreaError => { console.log(clientsByAreaError) })
        })
        .catch(metersessionError => { console.log(metersessionError) })
      },
      create (item) {
        let that = this
        let index = that.items.indexOf(item)
        if (confirm("Do you really want to set this metering? (This action can't be undone, removed or edited)")) {
          that.$store.dispatch(CREATECLIENTMETERING, { 
            meterSessionId: that.meterSession.id, 
            clientId: item.clientId, 
            amount: item.amount 
          })
          .then(createClientMeteringResponse => {
            that.items[index].id = createClientMeteringResponse.data.id
            that.items[index].amount = createClientMeteringResponse.data.amount
          })
          .catch(createClientMeteringError => { console.log(error) })
        }
      }
    }

  }
</script>
