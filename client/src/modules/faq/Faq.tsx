import React from 'react';

export default () => (
  <>
    <section className="text-center  space--xs">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-10 col-lg-8">
            <h2>FAQ</h2>
            <p className="lead">
              What you always wanted to know about <code>Mocky</code>
            </p>
          </div>
        </div>
      </div>
    </section>

    <section>
      <div className="container">
        <div className="row">
          <div className="col-lg-2">
            <div className="text-block">
              <h4>General</h4>
            </div>
          </div>
          <div className="col-lg-5">
            <div className="text-block">
              <h5>What is Mocky?</h5>
              <p>
                Mocky is a simple app which allows to generate custom HTTP responses. It's helpful when you have to
                request a build-in-progress WS, when you want to mock the backend response in a singleapp, or when you
                want to test your WS client.
              </p>
            </div>
            <div className="text-block">
              <h5>Is it really free?</h5>
              <p>
                Yes, mocky is really and totally free for you (but not for me{' '}
                <span role="img" aria-label="up side down">
                  🙃
                </span>{' '}
                ). If you feel like it, I accept{' '}
                <a href="https://www.buymeacoffee.com/julienlafont" target="_blank" rel="noreferrer noopener">
                  donations
                </a>{' '}
                to help me finance the service!
              </p>
            </div>
          </div>
          <div className="col-lg-5">
            <div className="text-block">
              <h5>How many mocks can I store? How long do they last?</h5>
              <p>
                You can create as many mocks as you want, and they will last forever. There is just one rule: call it at
                least once time per year to keep it alive!
              </p>
            </div>
            <div className="text-block">
              <h5>What is mocky SLA?</h5>
              <p>
                <code>Mocky do NOT commit for any Service-level agreement</code>. Everything is done to provide the best
                service quality, but I don't guarantee the sustainability or the stability of the application. If you
                need guarantees, you have to host your own Mocky instance.
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section>
      <div className="container">
        <div className="row">
          <div className="col-lg-2">
            <div className="text-block">
              <h4>Privacy</h4>
            </div>
          </div>
          <div className="col-lg-5">
            <div className="text-block">
              <h5>Will my mock be private?</h5>
              <p>Your mock is only accessible from its unique and private URL. Keep it secret!</p>
            </div>
            <div className="text-block">
              <h5>Is it allowed to store Personal Information?</h5>
              <p>
                <code>Absolutely NO!</code> This website is hosted in Europe, where the GDPR regulate the processing of
                Personal Identifiable Information. It's strictly forbidden to store PII without the right declarations.
              </p>
            </div>
          </div>
          <div className="col-lg-5">
            <div className="text-block">
              <h5>Are my data encrypted at rest?</h5>
              <p>
                <code>No.</code> Mocky administrators have access to all mocks and have the right to delete any mock
                without warning or restriction.
              </p>
            </div>
            <div className="text-block">
              <h5>Could you delete a mock for me please?</h5>
              <p>
                <i>I don't guarantee to process your request, but send me a direct message on twitter.</i>
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </>
);
