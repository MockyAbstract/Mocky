import React from 'react';
import { NavLink } from 'react-router-dom';

export default () => (
  <section className="height-100 bg--dark text-center">
    <div className="container pos-vertical-center">
      <div className="row">
        <div className="col-md-12">
          <h1 className="h1--large">500</h1>
          <p className="lead">An unexpected error has occured preventing the page from loading.</p>
          <NavLink to="/">Go back to home page</NavLink>
        </div>
      </div>
    </div>
  </section>
);
