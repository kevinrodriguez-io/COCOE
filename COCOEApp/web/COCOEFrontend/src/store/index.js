import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

export const INCREMENT = 'increment'
export const DECREMENT = 'decrement'
export const INCREMENTASYNC = 'incrementAsync'
export const ISUSERLOGGEDIN = 'isUserLoggedIn'
export const LOGIN = 'login'
export const REGISTER = 'register'

const LOCALSTORAGE_TOKEN = 'token'

const state = {
  count: 0,
  userIsLoggedIn: false
}

const getters = {
  [ISUSERLOGGEDIN] (state) {
    let token = localStorage.getItem(LOCALSTORAGE_TOKEN)
    state.userIsLoggedIn = (token != null && token != undefined);
    return state.userIsLoggedIn;
  }
}

const mutations = {
  [INCREMENT] (state) {
    state.count++
  },
  [DECREMENT] (state) {
    state.count--
  }
}

const actions = {
  [INCREMENTASYNC] ({commit}) {
    setTimeout(() =>{ commit(INCREMENT) },200)
  },
  [LOGIN] ({commit}, payload) {
    let requestContent = { username: payload.username, password: payload.password }
    axios.post('http://localhost:8080/COCOEAPP/api/user/login', requestContent)
    .then(response => {
      //response.data.Bearer
    })
    .catch(error => {

    })
  },
  [REGISTER] ({commit}, payload) {

  }
}

export default new Vuex.Store({
  state
})