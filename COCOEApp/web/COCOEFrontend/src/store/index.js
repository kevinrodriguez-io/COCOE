import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

export const INCREMENT = 'increment'
export const DECREMENT = 'decrement'

export const LOGIN = 'login'
export const REGISTER = 'register'
export const LOGOUT = 'logout'

export const SETJWTTOKEN = 'setJwtToken'
export const REMOVEJWTTOKEN = 'removeJwtToken'

const LOCALSTORAGE_TOKEN = 'token'

const state = {
  jwtToken: (localStorage.getItem(LOCALSTORAGE_TOKEN) != 'undefined') ? localStorage.getItem(LOCALSTORAGE_TOKEN) : undefined
}

const getters = {
  isUserLoggedIn (state) {
    return state.jwtToken != null && state.jwtToken != undefined
  }
}

const mutations = {
  [SETJWTTOKEN] (state, payload) {
    localStorage.setItem(LOCALSTORAGE_TOKEN, payload.bearer)
    state.jwtToken = payload.bearer
  },
  [REMOVEJWTTOKEN] (state) {
    localStorage.removeItem(LOCALSTORAGE_TOKEN)
    state.jwtToken = undefined
  }
}

const actions = {
  [LOGIN] ({commit}, payload) {
    let requestContent = { 
      userName: payload.userName, 
      password: payload.password
    }
    return new Promise((resolve, reject) => {
      axios.post('http://localhost:8080/COCOEApp/api/user/login', requestContent)
      .then(response => {
        commit(SETJWTTOKEN, { bearer: response.data.Bearer })
        resolve(response)
      })
      .catch(error => {
        reject(error)
      })
    })
  },
  [REGISTER] ({commit}, payload) {
    let requestContent = { 
      userName: payload.userName, 
      password: payload.password,
      name: payload.name,
      lastName: payload.lastName
    }
    return new Promise((resolve, reject) => {
      axios.post('http://localhost:8080/COCOEApp/api/user/register', requestContent)
      .then(response => {
        commit(SETJWTTOKEN, { bearer: response.data.Bearer })
        resolve(response)
      })
      .catch(error => {
        reject(error)
      })
    })
  },
  [LOGOUT] ({commit}) {
    commit(REMOVEJWTTOKEN)
  }
}

export default new Vuex.Store({ state, getters, mutations, actions })