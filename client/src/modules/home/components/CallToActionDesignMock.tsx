import React from 'react';
import { NavLink } from 'react-router-dom';

export default () => (
  <section className="imagebg space--xxs" data-gradient-bg="#4876BD,#5448BD,#8F48BD,#BD48B1">
    <div className="container">
      <div className="row">
        <div className="col-md-12">
          <div className="cta cta-1 cta--horizontal boxed boxed--border text-center-xs">
            <div className="row justify-content-center p-5">
              <div className="col-lg-3">
                <h4>No signup</h4>
              </div>
              <div className="col-lg-4">
                <p className="lead">
                  Start designing your mock
                  <br />
                </p>
              </div>
              <div className="col-lg-4 text-center">
                <NavLink to="/design" className="btn btn--primary type--uppercase">
                  <span className="btn__text">NEW MOCK</span>
                </NavLink>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
);
