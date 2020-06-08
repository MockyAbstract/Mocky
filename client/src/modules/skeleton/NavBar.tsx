import React from 'react';
import { NavLink } from 'react-router-dom';

import logo from './assets/logo-dark.png';

export default () => (
  <div className="nav-container">
    <div>
      <nav className="bar bar-1">
        <div className="container">
          <div className="row">
            <div className="col-lg-1 hidden-xs col-md-3">
              <div className="bar__module">
                <a href="/" className="logo-link">
                  <img className="logo" alt="logo" src={logo} style={{ maxHeight: '30px' }} />
                </a>
              </div>
            </div>
            <div className="col-lg-11 col-md-12 text-right text-left-xs text-left-sm">
              <div className="bar__module">
                <NavLink to="/manage" className="btn btn--sm type--uppercase hidden-xs">
                  <span className="btn__text">Manage my mocks</span>
                </NavLink>

                <NavLink to="/design" className="btn btn--sm type--uppercase btn--primary">
                  <span className="btn__text">New mock</span>
                </NavLink>
              </div>
            </div>
          </div>
        </div>
      </nav>
    </div>
  </div>
);
