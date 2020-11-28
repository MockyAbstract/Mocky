import React from 'react';

import ProfilePicture from './components/ProfilePicture';

export default () => (
  <section className="switchable feature-large">
    <div className="container">
      <div className="row justify-content-between">
        <div className="col-md-6">
          <ProfilePicture />
        </div>
        <div className="col-md-6 col-lg-5">
          <div className="switchable__text">
            <div className="text-block">
              <h2>Julien Lafont</h2>
              <span>
                CTO&nbsp;
                <a href="https://www.tabmo.io" rel="noreferrer noopener" target="_blank">
                  TabMo
                </a>
              </span>
            </div>
            <p className="lead">Web alchemist and lazy Open Source contributor.</p>
            <p>
              <a href="https://www.buymeacoffee.com/julienlafont">
                <img alt="buy me a coffee" src="https://img.buymeacoffee.com/button-api/?text=Buy me a coffee&emoji=&slug=julienlafont&button_colour=FF5F5F&font_colour=ffffff&font_family=Cookie&outline_colour=000000&coffee_colour=FFDD00" />
                </a>
            </p>
            <ul className="social-list list-inline list--hover">
              <li className="list-inline-item">
                <a href="https://twitter.com/julien_lafont" rel="noreferrer noopener" target="_blank">
                  <i className="socicon socicon-twitter icon icon--sm"></i>
                </a>
              </li>
              <li className="list-inline-item">
                <a href="https://www.linkedin.com/in/julienlafont/" rel="noreferrer noopener" target="_blank">
                  <i className="socicon socicon-linkedin icon icon--sm"></i>
                </a>
              </li>
              <li className="list-inline-item">
                <a href="https://www.github.com/julien-lafont/" rel="noreferrer noopener" target="_blank">
                  <i className="socicon socicon-github icon icon--sm"></i>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </section>
);
