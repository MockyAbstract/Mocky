import React from 'react';

import './styles.css';

export default () => (
  <>
    <section className="text-center space--xs">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-10 col-lg-8">
            <h2>Cookie Policy</h2>
          </div>
        </div>
      </div>
    </section>

    <section>
      <div className="container policy">
        <div className="row">
          <div className="col-lg-12">
            <div className="text-block">
              <p>This is the Cookie Policy for Mocky, accessible from https://www.mocky.io</p>

              <p>
                <strong>What Are Cookies</strong>
              </p>

              <p>
                As is common practice with almost all professional websites this site uses cookies, which are tiny files
                that are downloaded to your computer, to improve your experience. This page describes what information
                they gather, how we use it and why we sometimes need to store these cookies. We will also share how you
                can prevent these cookies from being stored however this may downgrade or 'break' certain elements of
                the sites functionality.
              </p>

              <p>
                For more general information on cookies, please read{' '}
                <a href="https://www.cookieconsent.com/what-are-cookies/">"What Are Cookies"</a>.
              </p>

              <p>
                <strong>How We Use Cookies</strong>
              </p>

              <p>
                We use cookies for a variety of reasons detailed below. Unfortunately in most cases there are no
                industry standard options for disabling cookies without completely disabling the functionality and
                features they add to this site. It is recommended that you leave on all cookies if you are not sure
                whether you need them or not in case they are used to provide a service that you use.
              </p>

              <p>
                <strong>Disabling Cookies</strong>
              </p>

              <p>
                You can prevent the setting of cookies by adjusting the settings on your browser (see your browser Help
                for how to do this). Be aware that disabling cookies will affect the functionality of this and many
                other websites that you visit. Disabling cookies will usually result in also disabling certain
                functionality and features of the this site. Therefore it is recommended that you do not disable
                cookies.
              </p>

              <p>
                <strong>The Cookies We Set</strong>
              </p>

              <ul className="bullets">
                <li>
                  <strong>Site preferences cookies</strong>
                  <p>
                    In order to provide you with a great experience on this site we provide the functionality to set
                    your preferences for how this site runs when you use it. In order to remember your preferences we
                    need to set cookies so that this information can be called whenever you interact with a page is
                    affected by your preferences.
                  </p>
                </li>
              </ul>

              <p>
                <strong>Third Party Cookies</strong>
              </p>

              <p>
                In some special cases we also use cookies provided by trusted third parties. The following section
                details which third party cookies you might encounter through this site.
              </p>

              <ul className="bullets">
                <li>
                  <strong>Google Analytics</strong>
                  <p>
                    This site uses Google Analytics which is one of the most widespread and trusted analytics solution
                    on the web for helping us to understand how you use the site and ways that we can improve your
                    experience. These cookies may track things such as how long you spend on the site and the pages that
                    you visit so we can continue to produce engaging content.
                  </p>
                  <p>For more information on Google Analytics cookies, see the official Google Analytics page.</p>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </section>
  </>
);
