<template>

    <section>
      <div>
        <v-dialog v-model="dialog" max-width="500px">
          <v-btn fab fixed right bottom color="primary" dark slot="activator" class="mb-2"><v-icon>add</v-icon></v-btn>
          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>
            <v-card-text>
              <v-container grid-list-md>
                <v-layout wrap>
                  <v-flex xs6>
                    <v-text-field label="Name" v-model="editedItem.name"></v-text-field>
                  </v-flex>
                  <v-flex xs6>
                    <v-text-field label="Last name" v-model="editedItem.lastName"></v-text-field>
                  </v-flex>
                  <v-flex xs6>
                    <v-checkbox label="Active" v-model="editedItem.active"></v-checkbox>
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
                  <v-flex xs12>
                    <v-text-field label="Direction" v-model="editedItem.direction" multi-line></v-text-field>
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
            Clients
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
              <td>{{ props.item.name }}</td>
              <td>{{ props.item.lastName }}</td>
              <td><v-checkbox disabled v-model="props.item.active"></v-checkbox></td>
              <!-- <td>{{ props.item.active }}</td> -->
              <td>{{ getAreaNameFromId(props.item.areaid) }}</td>
              <td>{{ props.item.direction }}</td>
              <!-- <td>{{ props.item.createdDate }}</td> -->
              <!-- <td>{{ props.item.lastBillingDate }}</td> -->
              <td class="justify-center layout px-0">
                <v-btn icon class="mx-0" @click="editItem(props.item)">
                  <v-icon color="teal">edit</v-icon>
                </v-btn>
                <v-btn icon class="mx-0" @click="deleteItem(props.item)">
                  <v-icon color="pink">delete</v-icon>
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
  import { GETAREAS, GETCLIENTS, CREATECLIENT, EDITCLIENT, DELETECLIENT } from '@/store'
  export default {
    data: () => ({
      dialog: false,
      headers: [
        { text: '', value: 'id', sortable: false },
        { text: 'Code', value: 'code' },
        { text: 'Name', value: 'name' },
        { text: 'Last name', value: 'lastName' },
        { text: 'Active', value: 'active' },
        { text: 'Area', value: 'areaid' },
        { text: 'Direction', value: 'direction' },
        // { text: 'Creation date', value: 'createdDate' },
        // { text: 'Last billing date', value: 'lastBillingDate' },
        { text: 'Actions', value: 'id', sortable: false }
      ],
      search: '',
      allItems: [],
      items: [],
      areas: [],
      editedIndex: -1,
      editedItem: {
        id: -1,
        code: '',
        name: '',
        lastName: '',
        active: true,
        areaid: -1,
        direction: '',
        createdDate: '',
        lastBillingDate: ''
      },
      defaultItem: {
        id: -1,
        code: '',
        name: '',
        lastName: '',
        active: true,
        areaid: -1,
        direction: '',
        createdDate: '',
        lastBillingDate: ''
      }
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New client' : 'Edit client'
      }
    },

    watch: {
      dialog (val) {
        val || this.close()
      },
      search (text) {
        let that = this
        that.getAreaNameFromId()
        if (text.length == 0) {
          this.items = this.allItems.slice()
          return
        }
        this.items = this.allItems.slice()
        this.items = this.items.filter(I => 
          I.code.toLowerCase().includes(text.toLowerCase()) ||
          I.name.toLowerCase().includes(text.toLowerCase()) ||
          I.lastName.toLowerCase().includes(text.toLowerCase()) ||
          that.getAreaNameFromId(I.areaid).toLowerCase().includes(text.toLowerCase()) ||
          I.direction.toLowerCase().includes(text.toLowerCase())
        )
      }
    },

    created () {
      this.initialize()
    },

    methods: {
      initialize () {
        let that = this
        that.$store.dispatch(GETCLIENTS)
        .then(response => {
          that.allItems = response.data
          that.items = response.data
        })
        .catch(error => {
          console.log(error)
        })
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

      deleteItem (item) {
        let that = this
        let index = that.items.indexOf(item)
        if (confirm('Are you sure you want to delete this item?')) {
          that.$store.dispatch(DELETECLIENT, { id : item.id })
          .then(response => { that.items.splice(index, 1) })
          .catch(error => { console.log(error) })
        }
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
          this.$store.dispatch(EDITCLIENT, { 
            id: this.editedItem.id, 
            name: this.editedItem.name,
            lastName: this.editedItem.lastName,
            active: this.editedItem.active,
            areaid: this.editedItem.areaid,
            direction: this.editedItem.direction
          })
          .then(response => { 
            Object.assign(this.items[this.editedIndex], response.data)
          })
          .catch(error => { console.log(error) })
        } else {
          this.$store.dispatch(CREATECLIENT, { 
            name: this.editedItem.name,
            lastName: this.editedItem.lastName,
            active: this.editedItem.active,
            areaid: this.editedItem.areaid,
            direction: this.editedItem.direction
          })
          .then(response => { 
            that.items.push(response.data) 
          })
          .catch(error => { console.log(error) })
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
      },

      getDetailsLink(item) {
        return '/client/' + item.id
      }

    }
  }
</script>
