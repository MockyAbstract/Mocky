import React from 'react';
import { NavLink } from 'react-router-dom';

export default () => (
  <>
    <p className="lead">
      This mock is gone{' '}
      <span role="img" aria-label="sad">
        🥺
      </span>
    </p>

    <NavLink to="/manage" className="btn btn--primary type--uppercase" href="/manage">
      <span className="btn__text">Go back</span>
    </NavLink>
  </>
);
