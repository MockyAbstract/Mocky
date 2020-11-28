import './index.css';

import * as React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';

import Routing from './modules/routing/Routing';
import { store } from './redux/store';
import * as serviceWorker from './serviceWorker';
import GA from './services/Analytics/GA';

GA.initialize();

ReactDOM.render(
  <Provider store={store}>
    <Routing />
  </Provider>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
