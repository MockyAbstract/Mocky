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
            <p className="lead">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vel turpis dignissim, vestibulum neque
              pellentesque, consequat est. Nunc turpis turpis, porta id nibh non, ullamcorper fringilla nisi.
            </p>
            <ul className="social-list list-inline list--hover">
              <li className="list-inline-item">
                <a href="https://twitter.com/julien_lafont" rel="noreferrer noopener" target="_blank">
                  <i className="socicon socicon-twitter icon icon--xs"></i>
                </a>
              </li>
              <li className="list-inline-item">
                <a href="https://www.linkedin.com/in/julienlafont/" rel="noreferrer noopener" target="_blank">
                  <i className="socicon socicon-linkedin icon icon--xs"></i>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </section>
);
