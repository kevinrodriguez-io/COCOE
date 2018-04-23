import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Login from '@/components/Login'
import Register from '@/components/Register'
import AreaList from '@/components/areas/AreaList'
import ClientList from '@/components/clients/ClientList'
import MeteringList from '@/components/meterings/MeteringList'
import Metering from '@/components/meterings/Metering'
import store from '../store';

Vue.use(Router)

export default new Router({
  // mode: 'history',
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld,
      beforeEnter: (to, from, next) => { if (!store.getters.isUserLoggedIn) { next('/login') } else { next() } }
    },
    {
      path: '/areas',
      name: 'Areas',
      component: AreaList,
      beforeEnter: (to, from, next) => { if (!store.getters.isUserLoggedIn) { next('/login') } else { next() } }
    },
    {
      path: '/clients',
      name: 'Clients',
      component: ClientList,
      beforeEnter: (to, from, next) => { if (!store.getters.isUserLoggedIn) { next('/login') } else { next() } }
    },
    {
      path: '/meterings',
      name: 'Meterings',
      component: MeteringList,
      beforeEnter: (to, from, next) => { if (!store.getters.isUserLoggedIn) { next('/login') } else { next() } } 
    },
    {
      path: '/metering/:id',
      name: 'Metering',
      component: Metering,
      beforeEnter: (to, from, next) => { if (!store.getters.isUserLoggedIn) { next('/login') } else { next() } } 
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    }
  ]
})
