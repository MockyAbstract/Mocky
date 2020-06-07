import React from 'react';

export default () => (
  <section className="space--xs">
    <div className="container">
      <div className="row">
        <div className="col-md-6 col-lg-3">
          <div className="feature feature-6">
            <h5>JSONP support</h5>
            <p>
              Add <code>?callback=myfunction</code> to your mocky URL to transform the response into a JSONP response.
              <br />
              <a
                href="https://www.mocky.io/v2/5185415ba171ea3a00704eed?callback=execute"
                target="_blank"
                rel="noopener noreferrer"
              >
                See in action
              </a>
            </p>
          </div>
        </div>

        <div className="col-md-6 col-lg-3">
          <div className="feature feature-6">
            <h5>CORS</h5>
            <p>
              CORS Preflight requests are automatically accepted from any origin. You just have call the mock with an{' '}
              <code>Origin</code> header.
              <br />
              <a
                href="https://www.test-cors.org/#?client_method=POST&client_credentials=false&client_headers=X-FOO-BAR%0AX-Access-Token&client_postdata=%7B%22sample%22%3A%20%22data%22%7D&server_url=https%3A%2F%2Fwww.mocky.io%2Fv2%2F5185415ba171ea3a00704eed&server_enable=true&server_status=200&server_credentials=false&server_tabs=remote"
                target="_blank"
                rel="noopener noreferrer"
              >
                See in action
              </a>
            </p>
          </div>
        </div>
        <div className="col-md-6 col-lg-3">
          <div className="feature feature-6">
            <h5>Arbitrary Delay</h5>
            <p>
              Add the <code>?mocky-delay=100ms</code> paramater to your mocky URL to delay the response. Maximum delay:
              60s.
              <br />
              <a
                href="https://www.mocky.io/v2/5185415ba171ea3a00704eed?mocky-delay=10s"
                target="_blank"
                rel="noopener noreferrer"
              >
                See in action
              </a>
              <br />
              <a
                href="https://github.com/scala/scala/blob/v2.9.3/src/library/scala/concurrent/duration/Duration.scala#L76"
                target="_blank"
                rel="noopener noreferrer"
              >
                Allowed values
              </a>
            </p>
          </div>
        </div>
        <div className="col-md-6 col-lg-3">
          <div className="feature feature-6">
            <h5>Limitation</h5>
            <p>
              You're allowed to call your mocky up to 100 times per seconds. Call it at least once in the year to
              postpone automatic deletion.
            </p>
          </div>
        </div>
      </div>
    </div>
  </section>
);
