import React from 'react';

import background from './assets/inner-6.jpg';

export default () => (
  <div className="main-container">
    <section className="cover height-100 imagebg text-center" data-overlay="7">
      <div
        className="background-image-holder"
        style={{
          background: `url(${background})`,
          opacity: 1,
        }}
      >
        <img alt="background" src={background} />
      </div>
      <div className="container pos-vertical-center">
        <div className="row">
          <div className="col-md-12">
            <h2>We're currently making some improvements &mdash; Check back soon.</h2>
          </div>
        </div>
      </div>
    </section>
  </div>
);
