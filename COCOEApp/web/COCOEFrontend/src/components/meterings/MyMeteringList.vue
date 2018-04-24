<template>
    <section>
      <div>
        <v-dialog v-model="dialog" max-width="500px">
          <!-- <v-btn fab fixed right bottom color="primary" dark slot="activator" class="mb-2"><v-icon>add</v-icon></v-btn> -->
          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>
            <v-card-text>
              <v-container grid-list-md>
                <v-layout wrap v-if="!isEditing">
                  <v-flex xs12>
                    <v-text-field label="Header" v-model="editedItem.header"></v-text-field>
                  </v-flex>
                  <v-flex xs6>
                    <v-select 
                      :items="areas" 
                      v-model="editedItem.areaid" 
                      label="Select area" 
                      single-line
                      item-text="name"
                      item-value="id"
                    ></v-select>
                  </v-flex>
                  <v-flex xs6>
                    <v-select 
                      :items="statuses" 
                      v-model="editedItem.status" 
                      label="Select status" 
                      single-line
                      item-text="text"
                      item-value="value"
                    ></v-select>
                  </v-flex>
                </v-layout>
                <v-layout wrap v-else>
                  <v-flex xs12>
                    <v-text-field label="Header" v-model="editedItem.header"></v-text-field>
                  </v-flex>
                  <v-flex xs12>
                    <v-select 
                      :items="statuses" 
                      v-model="editedItem.status" 
                      label="Select status" 
                      single-line
                      item-text="text"
                      item-value="value"
                    ></v-select>
                  </v-flex>
                </v-layout>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" flat @click.native="close">Cancel</v-btn>
              <v-btn color="blue darken-1" flat @click.native="save">Save</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-card>
          <v-card-title>
            Metering sessions
            <v-spacer></v-spacer>
            <v-text-field append-icon="search" label="Search" single-line hide-details v-model="search" ></v-text-field>
          </v-card-title>
          <v-data-table :headers="headers" :items="items" hide-actions class="elevation-1" >
            <template slot="items" slot-scope="props">
              <td class="justify-center layout px-0">
                <v-btn class="primary mx-0" small :to="getDetailsLink(props.item)">
                  Details
                </v-btn>
              </td>
              <td>{{ props.item.code }}</td>
              <td>{{ getAreaNameFromId(props.item.areaid) }}</td>
              <td>{{ props.item.header }}</td>
              <td>{{ props.item.status }}</td>
              <!-- <td>{{ props.item.createdDate }}</td> -->
              <td class="justify-center layout px-0">
                <v-btn icon class="mx-0" @click="editItem(props.item)">
                  <v-icon color="teal">edit</v-icon>
                </v-btn>
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
  import { GETMETERSESSIONSFORUSER, FINDCURRENTUSER, GETAREAS, GETMETERSESSIONS, CREATEMETERSESSION, EDITMETERSESSION, DELETEMETERSESSION } from '@/store'
  export default {
    data: () => ({
      dialog: false,
      headers: [
        { text: '', value: 'id', sortable: false },
        { text: 'Code', value: 'code' },
        { text: 'Area', value: 'areaid' },
        { text: 'Header', value: 'header' },
        { text: 'Status', value: 'status' },
        // { text: 'Creation date', value: 'createdDate' },
        { text: 'Actions', value: 'id', sortable: false }
      ],
      allItems: [],
      items: [],
      areas: [],
      statuses: [
        {
          text: 'Waiting to start',
          value: 'Waiting to start'
        },
        {
          text: 'Started',
          value: 'Started'
        },
        {
          text: 'On hold',
          value: 'On hold'
        },
        {
          text: 'Finised',
          value: 'Finised'
        }
      ],
      search: '',
      editedIndex: -1,
      editedItem: {
        id: -1,
        areaid: -1,
        header: '',
        code: '',
        status: '',
        createdDate: ''
      },
      defaultItem: {
        id: -1,
        areaid: -1,
        header: '',
        code: '',
        status: '',
        createdDate: ''
      }
    }),
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New metering session' : 'Edit metering session'
      },
      isEditing () {
        return this.editedIndex > -1
      }
    },
    watch: {
      dialog (val) {
        val || this.close()
      },
      search (text) {
        if (text.length == 0) {
          this.items = this.allItems.slice()
          return
        }
        this.items = this.allItems.slice()
        this.items = this.items.filter(I => 
          I.code.toLowerCase().includes(text.toLowerCase()) ||
          I.header.toLowerCase().includes(text.toLowerCase()) ||
          this.getAreaNameFromId(I.areaid).toLowerCase().includes(text.toLowerCase()) ||
          I.status.toLowerCase().includes(text.toLowerCase())
        )
      }
    },
    created () {
      this.initialize()
    },
    methods: {
      initialize () {
        let that = this
        that.$store.dispatch(FINDCURRENTUSER)
        .then(userResponse => {
          that.$store.dispatch(GETMETERSESSIONSFORUSER, { id: userResponse.data.id })
          .then(response => {
            that.allItems = response.data
            that.items = response.data
          })
          .catch(error => { console.log(error) })
        })
        .catch(userError => { console.log(userError) })
        
        that.$store.dispatch(GETAREAS)
        .then(response => {
          that.areas = response.data
        })
        .catch(error => {
          console.log(error)
        })
      },

      editItem (item) {
        this.editedIndex = this.items.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      getDetailsLink (item) {
        return '/mymetering/'+item.id
      },

      close () {
        this.dialog = false
        setTimeout(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        }, 300)
      },

      save () {
        let that = this
        if (this.editedIndex > -1) {
          // Object.assign(this.items[this.editedIndex], this.editedItem)
          this.$store.dispatch(EDITMETERSESSION, { 
            id: this.editedItem.id,
            header: this.editedItem.header, 
            status: this.editedItem.status
           })
          .then(response => { 
            Object.assign(this.items[this.editedIndex], response.data)
          })
          .catch(error => { console.log(error) })
        } else {
          // this.$store.dispatch(CREATEMETERSESSION, { 
          //   header: this.editedItem.header, 
          //   areaid: this.editedItem.areaid,
          //   status: this.editedItem.status
          // })
          // .then(response => { 
          //   that.items.push(response.data) 
          // })
          // .catch(error => { console.log(error) })
        }
        this.close()
      },

      getAreaNameFromId(id) {
        let results = this.areas.filter(I=>I.id == id)
        if (results.length > 0) {
          return results[0].name;
        } else {
          return 'loading...'
        }
      }

    }
  }
</script>
