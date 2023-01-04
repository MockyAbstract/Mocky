import React from 'react';
import { Link } from 'react-router-dom';
import background from './assets/sand-bg.jpg';

export default () => (
  <section className="cover height-60 imagebg text-center" data-overlay="3">
    <img
      alt="background"
      src={background}
      className="background-image-holder"
      style={{ opacity: 1 }}
    />
    <div className="container pos-vertical-center">
      <div className="row">
        <div className="col-md-12">
          <h1>Your mocky repository is empty...</h1>
          <Link className="btn btn--primary type--uppercase" to="/design">
            <span className="btn__text">Create your first mock!</span>
          </Link>
        </div>
      </div>
    </div>
  </section>
);