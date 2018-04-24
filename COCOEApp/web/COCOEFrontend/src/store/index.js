import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
import router from '@/router'
import { debug } from 'util';
import { parse } from 'url';

Vue.use(Vuex)

/**********************************
 * AUTH ACTION NAMES
**********************************/
export const LOGIN = 'login'
export const REGISTER = 'register'
export const LOGOUT = 'logout'

/**********************************
 * AREA ACTION NAMES
**********************************/
export const GETAREAS = 'getAreas'
export const FINDAREA = 'findArea'
export const CREATEAREA = 'createArea'
export const EDITAREA = 'editArea'
export const DELETEAREA = 'deleteArea'

/**********************************
 * CLIENT ACTION NAMES
**********************************/
export const FINDCLIENT = 'findClient'
export const GETCLIENTS = 'getClients'
export const CREATECLIENT = 'createClient'
export const EDITCLIENT = 'editClient'
export const DELETECLIENT = 'deleteClient'

/**********************************
 * METERSESSION ACTION NAMES
**********************************/
export const GETMETERSESSIONS = 'getMeterSessions'
export const GETMETERSESSIONSFORUSER = 'getMeterSessionsForUser'
export const FINDMETERSESSION = 'findMeterSession'
export const CREATEMETERSESSION = 'createMeterSession'
export const EDITMETERSESSION = 'editMeterSession'
export const DELETEMETERSESSION = 'deleteMeterSession'

/**********************************
 * METERSESSIONUSER ACTION NAMES
**********************************/
export const GETMETERSESSIONUSERSBYMETERSESSION = 'getMeterSessionUsersByMeterSession'
export const CREATEMETERSESSIONUSER = 'createMeterSessionUser'
export const DELETEMETERSESSIONUSER = 'deleteMeterSessionUser'

/**********************************
 * USER ACTION NAMES
**********************************/
export const GETUSERS = 'getUsers'
export const FINDCURRENTUSER = 'findCurrentUser'
export const SETUPCURRENTUSERROLE = 'setupCurrentUserRole'
export const FINDUSER = 'findUser'

/**********************************
 * AUTH MUTATION NAMES
**********************************/
export const SETJWTTOKEN = 'setJwtToken'
export const REMOVEJWTTOKEN = 'removeJwtToken'
export const SETUSERROLE = 'setUserRole'
export const REMOVEUSERROLE = 'removeUserRole'

/**********************************
 * INTERNAL KEYS
**********************************/
const LOCALSTORAGE_TOKEN = 'token'
const APIENDPOINT = "http://localhost:8080/COCOEApp/api/"

const state = {
  jwtToken: (localStorage.getItem(LOCALSTORAGE_TOKEN) != 'undefined') ? localStorage.getItem(LOCALSTORAGE_TOKEN) : undefined,
  userRole: null
}

const getters = {
  isUserLoggedIn (state) {
    return state.jwtToken != null && state.jwtToken != undefined
  },
  loggedUserName (state) {
    if (state.jwtToken != undefined) {
      return JSON.parse(window.atob(state.jwtToken.split('.')[1])).sub
    } else {
      return null
    }
  },
  loggedUserRole (state) {
    return state.userRole
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
    state.userRole = null
  },
  [SETUSERROLE] (state, payload) {
    state.userRole = payload.userRole
  },
  [REMOVEUSERROLE] (state) {
    state.userRole = null
  }
}

const actions = {
  /**********************************
   * AUTH REQUESTS HERE
  **********************************/
  [LOGIN] (context, payload) {
    let requestContent = { 
      userName: payload.userName, 
      password: payload.password
    }
    return new Promise((resolve, reject) => {
      axios.post(APIENDPOINT + 'user/login', requestContent)
      .then(response => {
        context.commit(SETJWTTOKEN, { bearer: response.data.Bearer })
        context.dispatch(SETUPCURRENTUSERROLE).then(i=>{
          resolve(response)
        }).catch(e=>{ reject(e) })
      })
      .catch(error => {
        reject(error)
      })
    })
  },
  [REGISTER] (context, payload) {
    let requestContent = { 
      userName: payload.userName, 
      password: payload.password,
      name: payload.name,
      lastName: payload.lastName
    }
    return new Promise((resolve, reject) => {
      axios.post(APIENDPOINT + 'user/register', requestContent)
      .then(response => {
        context.commit(SETJWTTOKEN, { bearer: response.data.Bearer })
        context.dispatch(SETUPCURRENTUSERROLE).then(i=>{
          resolve(response)
        }).catch(e=>{ reject(e) })
      })
      .catch(error => {
        reject(error)
      })
    })
  },
  [LOGOUT] ({commit}) {
    commit(REMOVEJWTTOKEN)
  },

  /**********************************
   * AREA REQUESTS HERE
  **********************************/
  [FINDAREA] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'area/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [GETAREAS] (context) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'area/', { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [CREATEAREA] (context, payload) {
    let requestContent = { name: payload.name }
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.post(APIENDPOINT + 'area/create', requestContent, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [EDITAREA] (context, payload) {
    let requestContent = { id: payload.id, name: payload.name }
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.put(APIENDPOINT + 'area/edit', requestContent, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [DELETEAREA] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.delete(APIENDPOINT + 'area/delete/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },

  /**********************************
   * CLIENT REQUESTS HERE
  **********************************/
  [FINDCLIENT] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'client/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [GETCLIENTS] (context) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'client/', { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [CREATECLIENT] (context, payload) {
    let requestContent = { 
      name: payload.name,
      lastName: payload.lastName,
      active: payload.active,
      areaid: payload.areaid,
      direction: payload.direction
    }
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.post(APIENDPOINT + 'client/create', requestContent, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [EDITCLIENT] (context, payload) {
    let requestContent = { 
      id: payload.id, 
      name: payload.name,
      lastName: payload.lastName,
      active: payload.active,
      areaid: payload.areaid,
      direction: payload.direction
    }
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.put(APIENDPOINT + 'client/edit', requestContent, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [DELETECLIENT] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.delete(APIENDPOINT + 'client/delete/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },

  /**********************************
   * METERSESSION REQUESTS HERE
  **********************************/
  [FINDMETERSESSION] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'metersession/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [GETMETERSESSIONS] (context) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'metersession/', { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [GETMETERSESSIONSFORUSER] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'metersession/byUser/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [CREATEMETERSESSION] (context, payload) {
    let requestContent = { 
      header: payload.header, 
      areaid: payload.areaid,
      status: payload.status
    }
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.post(APIENDPOINT + 'metersession/create', requestContent, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [EDITMETERSESSION] (context, payload) {
    let requestContent = { 
      id: payload.id,
      header: payload.header, 
      status: payload.status
    }
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.put(APIENDPOINT + 'metersession/edit', requestContent, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [DELETEMETERSESSION] (context, payload) {
    return new Promise((resolve, reject) => {
      debugger
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.delete(APIENDPOINT + 'metersession/delete/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },

  /**********************************
   * METERSESSIONUSER REQUESTS HERE
  **********************************/
  [GETMETERSESSIONUSERSBYMETERSESSION] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'metersessionuser/byMeterSession/' + payload.meterSessionId, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [CREATEMETERSESSIONUSER] (context, payload) {
    let requestContent = { 
      metersessionid: payload.metersessionid, 
      userid: payload.userid
    }
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.post(APIENDPOINT + 'metersessionuser/create', requestContent, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [DELETEMETERSESSIONUSER] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.delete(APIENDPOINT + 'metersessionuser/delete/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => {
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  
  /**********************************
   * USER REQUESTS HERE
  **********************************/
  [FINDUSER] (context, payload) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'user/' + payload.id, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [FINDCURRENTUSER] (context) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      let userName = context.getters.loggedUserName
      if (token != undefined) {
        axios.get(APIENDPOINT + 'user/findByUsername/' + userName, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [SETUPCURRENTUSERROLE] (context) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      let userName = context.getters.loggedUserName
      if (token != undefined) {
        axios.get(APIENDPOINT + 'user/findByUsername/' + userName, { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { 
          context.commit(SETUSERROLE, { userRole: response.data.role })
          resolve(response) 
        })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  [GETUSERS] (context) {
    return new Promise((resolve, reject) => {
      let token = context.state.jwtToken
      if (token != undefined) {
        axios.get(APIENDPOINT + 'user/', { headers: { 'Authorization': 'Bearer ' + token } })
        .then(response => { resolve(response) })
        .catch(error => { 
          if (error.response) {
            if (error.response.status == 401) {
              context.commit(REMOVEJWTTOKEN)
              router.push('/login')
            }
          }
          reject(error) 
        })
      } else {
        router.push('/login')
        reject('User is not logged in')
      }
    })
  },
  
}

export default new Vuex.Store({ state, getters, mutations, actions })