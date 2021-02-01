import React from 'react';
import Typed from 'react-typed';

export default () => (
  <section className="text-center space--xxs">
    <div className="container">
      <div className="row">
        <div className="col-md-10 col-lg-8">
          <h1>Mocky</h1>
          <p className="lead">
            <Typed strings={['The world&#39;s easiest &amp; fastest tool belts to mock your APIs']} typeSpeed={40} />
          </p>
        </div>
      </div>
    </div>
  </section>
);
