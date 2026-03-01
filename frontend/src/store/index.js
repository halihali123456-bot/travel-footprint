import { createStore } from 'vuex'

export default createStore({
  state: {
    user: JSON.parse(localStorage.getItem('user')) || null,
    token: localStorage.getItem('token') || ''
  },
  getters: {
    isLoggedIn: state => !!state.token,
    currentUser: state => state.user,
    token: state => state.token
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      localStorage.setItem('user', JSON.stringify(user))
    },
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    CLEAR_USER(state) {
      state.user = null
      state.token = ''
      localStorage.removeItem('user')
      localStorage.removeItem('token')
    },
    UPDATE_USER(state, user) {
      state.user = { ...state.user, ...user }
      localStorage.setItem('user', JSON.stringify(state.user))
    }
  },
  actions: {
    login({ commit }, { user, token }) {
      commit('SET_USER', user)
      commit('SET_TOKEN', token)
    },
    logout({ commit }) {
      commit('CLEAR_USER')
    },
    updateUser({ commit }, user) {
      commit('UPDATE_USER', user)
    }
  }
})
