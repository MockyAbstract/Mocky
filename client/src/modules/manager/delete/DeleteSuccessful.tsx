import React from 'react';

import ManagerTitle from '../components/ManagerTitle';
import MockGone from '../components/MockGone';

export default () => {
  return (
    <>
      <ManagerTitle />

      <section className="text-center space--xxs">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-md-8">
              <div className="boxed boxed--border bg--primary boxed--lg box-shadow">
                <h3>Mock Deletion</h3>
                <MockGone />
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};
