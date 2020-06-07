import React from 'react';
import { NavLink } from 'react-router-dom';

import code from './assets/carbon.svg';

export default () => (
  <section className="switchable feature-large space--xxs">
    <div className="container">
      <div className="row justify-content-around">
        <div className="col-md-6">
          <img alt="curl -X PUT https://www.mocky.io/v2/5185415ba171ea3a00704eed" src={code} />
        </div>
        <div className="col-md-6 col-lg-5">
          <div className="switchable__text">
            <h2>API Mocks for Free</h2>
            <p className="lead">
              Don't wait for the backend to be ready, generate custom API responses with Mocky and start working on your
              application straightaway
            </p>
            <NavLink to="/design">Start designing your mock »</NavLink>
          </div>
        </div>
      </div>
    </div>
  </section>
);
