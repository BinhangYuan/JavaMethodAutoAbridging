require('expose?$!expose?jQuery!jquery')
require("bootstrap-webpack")
require('./styles.css')

import React from 'react'
import { render } from 'react-dom'
import { Provider } from 'react-redux'
import thunkMiddleware from 'redux-thunk'
import createLogger from 'redux-logger'
import { createStore, applyMiddleware } from 'redux'

import Reducer from './reducers'
import App from './components/app'

const logger = createLogger()
const store = createStore(Reducer, applyMiddleware(thunkMiddleware, logger))



render(
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('app')
)