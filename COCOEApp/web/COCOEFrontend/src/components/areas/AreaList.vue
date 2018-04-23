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
                  <v-flex xs12>
                    <v-text-field label="Name" v-model="editedItem.name"></v-text-field>
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
            Areas
            <v-spacer></v-spacer>
            <v-text-field append-icon="search" label="Search" single-line hide-details v-model="search" ></v-text-field>
          </v-card-title>
          <v-data-table :headers="headers" :items="items" hide-actions class="elevation-1" >
            <template slot="items" slot-scope="props">
              <td>{{ props.item.code }}</td>
              <td>{{ props.item.name }}</td>
              <td>{{ props.item.createdDate }}</td>
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
  import { GETAREAS, CREATEAREA, EDITAREA, DELETEAREA } from '@/store'
  export default {
    data: () => ({
      dialog: false,
      headers: [
        { text: 'Code', value: 'code' },
        { text: 'Name', value: 'name' },
        { text: 'Creation date', value: 'createdDate' },
        { text: 'Actions', value: 'id', sortable: false }
      ],
      allItems: [],
      items: [],
      search: '',
      editedIndex: -1,
      editedItem: {
        id: -1,
        code: '',
        name: '',
        createdDate: ''
      },
      defaultItem: {
        id: -1,
        code: '',
        name: '',
        createdDate: ''
      }
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New area' : 'Edit area'
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
          I.name.toLowerCase().includes(text.toLowerCase())
        )
      }
    },

    created () {
      this.initialize()
    },

    methods: {
      initialize () {
        let that = this
        that.$store.dispatch(GETAREAS)
        .then(response => {
          that.allItems = response.data
          that.items = response.data
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
          that.$store.dispatch(DELETEAREA, { id : item.id })
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
          this.$store.dispatch(EDITAREA, { id: this.editedItem.id, name: this.editedItem.name })
          .then(response => { 
            Object.assign(this.items[this.editedIndex], response.data)
          })
          .catch(error => { console.log(error) })
        } else {
          this.$store.dispatch(CREATEAREA, { name: this.editedItem.name })
          .then(response => { 
            that.items.push(response.data) 
          })
          .catch(error => { console.log(error) })
        }
        this.close()
      }
    }
  }
</script>
