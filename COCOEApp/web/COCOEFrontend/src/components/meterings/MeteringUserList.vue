<template>

    <section>
      <div>
        <v-dialog v-model="dialog" max-width="500px">
          <v-btn color="primary" dark slot="activator" class="mb-2">Add user to meter session</v-btn>
          <v-card>
            <v-card-title>
              <span class="headline">{{ formTitle }}</span>
            </v-card-title>
            <v-card-text>
              <v-container grid-list-md>
                <v-layout wrap>
                  <v-flex xs12>
                    <v-select 
                      :items="users" 
                      v-model="editedItem.userid" 
                      label="Select user" 
                      single-line
                      item-text="userName"
                      item-value="id"
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
            Users for this meter session
            <v-spacer></v-spacer>
            <v-text-field append-icon="search" label="Search" single-line hide-details v-model="search" ></v-text-field>
          </v-card-title>
          <v-data-table :headers="headers" :items="items" hide-actions class="elevation-1" >
            <template slot="items" slot-scope="props">
              <td>{{ getUserNameFromId(props.item.userid) }}</td>
              <!-- <td>{{ props.item.createdDate }}</td> -->
              <td class="justify-center layout px-0">
                <!-- <v-btn icon class="mx-0" @click="editItem(props.item)">
                  <v-icon color="teal">edit</v-icon>
                </v-btn> -->
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
  import { GETMETERSESSIONUSERSBYMETERSESSION, CREATEMETERSESSIONUSER, DELETEMETERSESSIONUSER, GETUSERS } from '@/store'
  export default {
    props: [ 'meterSessionId' ],
    data: () => ({
      dialog: false,
      headers: [
        { text: 'User', value: 'userid' },
        // { text: 'Creation date', value: 'createdDate' },
        { text: 'Actions', value: 'id', sortable: false }
      ],
      search: '',
      allItems: [],
      items: [],
      users: [],
      editedIndex: -1,
      editedItem: {
        id: -1,
        metersessionid: -1,
        userid: -1,
        createdDate: ''
      },
      defaultItem: {
        id: -1,
        metersessionid: -1,
        userid: -1,
        createdDate: ''
      }
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New user for meter session' : 'Edit client'
      }
    },

    watch: {
      dialog (val) {
        val || this.close()
      },
      search (text) {
        let that = this
        if (text.length == 0) {
          this.items = this.allItems.slice()
          return
        }
        this.items = this.allItems.slice()
        this.items = this.items.filter(I => 
          that.getUserNameFromId(I.userid).toLowerCase().includes(text.toLowerCase())
        )
      }
    },

    created () {
      this.initialize()
    },

    methods: {
      initialize () {
        let that = this
        console.log(GETMETERSESSIONUSERSBYMETERSESSION + this.meterSessionId)
        that.$store.dispatch(GETMETERSESSIONUSERSBYMETERSESSION, { meterSessionId: this.meterSessionId })
        .then(response => {
          that.allItems = response.data
          that.items = response.data
        })
        .catch(error => {
          console.log(error)
        })
        that.$store.dispatch(GETUSERS)
        .then(response => {
          that.users = response.data
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
          that.$store.dispatch(DELETEMETERSESSIONUSER, { id : item.id })
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
        } else {
          this.$store.dispatch(CREATEMETERSESSIONUSER, { 
            metersessionid: parseInt(this.meterSessionId),
            userid: this.editedItem.userid
          })
          .then(response => { 
            that.items.push(response.data) 
          })
          .catch(error => { console.log(error) })
        }
        this.close()
      },

      getUserNameFromId(id) {
        console.log('calling: getUserNameFromId('+id+')')
        let results = this.users.filter(I=>I.id == id)
        if (results.length > 0) {
          return results[0].userName;
        } else {
          return 'loading...'
        }
      }

    }
  }
</script>
