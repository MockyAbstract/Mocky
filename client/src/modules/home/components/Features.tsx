import React from 'react';
import { NavLink } from 'react-router-dom';

export default () => (
  <section className="space--xs">
    <div className="container">
      <div className="row">
        <div className="col-md-6 col-lg-3">
          <div className="feature feature-6">
            <i className="icon color--primary icon-Money-2 icon--sm"></i>
            <h5>Free &amp; Unlimited</h5>
            <p>
              Mocky is free to use, no ads, no hidden subscriptions or service limits. Your mocks will be available{' '}
              <strong>forever</strong> if you call it at least on time per year, but without any{' '}
              <NavLink to="/faq">guarantee</NavLink>.
            </p>
          </div>
        </div>
        <div className="col-md-6 col-lg-3">
          <div className="feature feature-6">
            <i className="icon color--primary icon-Box-Full icon--sm"></i>
            <h5>Total control</h5>
            <p>
              New in Mocky, you can now update or delete your mocks at any time.
              <br />
              The next release will go further and offer you request inspector and cloud-based mock management.
            </p>
          </div>
        </div>
        <div className="col-md-6 col-lg-3">
          <div className="feature feature-6">
            <i className="icon color--primary icon-Chemical icon--sm"></i>
            <h5>Developper Friendly</h5>
            <p>
              Mocky is compatible with JS, Mobile and Server applications, featuring CORS, JSONP and GZIP responses.
              <br />
              No authentication, just call it!
            </p>
          </div>
        </div>
        <div className="col-md-6 col-lg-3">
          <div className="feature feature-6">
            <i className="icon color--primary icon-Code-Window icon--sm"></i>
            <h5>Open Source&nbsp;</h5>
            <p>
              Mocky is distributed with Apache 2 licence on Github. Ready-to-use distributions are available to host
              your own Mocky instance.
            </p>
          </div>
        </div>
      </div>
    </div>
  </section>
);
