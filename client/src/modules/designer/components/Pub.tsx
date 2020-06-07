import React from 'react';

import coffee from '../assets/clem-onojeghuo-DoA2duXyzRM-unsplash.jpg';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faTwitter as iconTwitter } from '@fortawesome/free-brands-svg-icons';
import { faBeer as iconCoffee } from '@fortawesome/free-solid-svg-icons';

export default () => (
  <section className="feature-large space--xxs bg--secondary">
    <div className="container">
      <div className="row justify-content-between">
        <div className="col-lg-5 col-md-5">
          <img alt="woman" className="border--round box-shadow-wide" src={coffee} />
        </div>
        <div className="col-lg-6 col-md-5">
          <h2 style={{ marginTop: '0.6em' }}>How to support Mocky?</h2>
          <div className="text-block">
            <h5>Talk about us</h5>
            <p>
              Give a shoot on twitter! Share how Mocky helps you in your daily life, how it improves your productivity.
            </p>

            <h5>Donate to cover operating costs</h5>
            <p>
              Mocky is a free and open-source service, but not a costless service. All contributions to cover operating
              costs are welcome.
            </p>
          </div>
        </div>
        <div className="col-lg-1 col-md-2">
          <div className="text-block">
            <p>&nbsp;</p>
            <br />
            <p>
              <a
                className="btn btn--primary btn-cta-pub"
                href="https://twitter.com/intent/tweet?original_referer=https%3A%2F%2Fpublish.twitter.com%2F&ref_src=twsrc%5Etfw&text=Don%27t%20wait%20for%20the%20backend%20to%20be%20ready%2C%20generate%20custom%20API%20responses%20with%20Mocky%20and%20start%20working%20on%20your%20application%20straightaway&tw_p=tweetbutton&url=https%3A%2F%2Fwww.mocky.io&via=julien_lafont"
                target="_blank"
                rel="noopener noreferrer"
              >
                <span className="btn__text">
                  <FontAwesomeIcon icon={iconTwitter} size="2x" />
                  &nbsp; Give a shout
                </span>
              </a>
            </p>
            <br />
            <br />
            <p>
              <a
                className="btn btn--primary btn-cta-pub"
                href="https://www.buymeacoffee.com/julienlafont"
                target="_blank"
                rel="noopener noreferrer"
              >
                <span className="btn__text">
                  <FontAwesomeIcon icon={iconCoffee} size="2x" />
                  &nbsp; Buy me a beer
                </span>
              </a>
            </p>
          </div>
        </div>
      </div>
    </div>
  </section>
);
