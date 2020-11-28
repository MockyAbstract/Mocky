import React from 'react';

import randomArray from 'unique-random-array';

import bg from './assets/chi-hang-leong-hehYcAGhbmY-unsplash.jpg';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faExternalLinkAlt as iconLink } from '@fortawesome/free-solid-svg-icons';
import './styles.css';

import { products } from './models';

/*  eslint "react/jsx-no-target-blank": ["warn", { "allowReferrer": true }] */

export default () => {
  const randomizedProducts = randomArray(products);
  const featuredProducts = [randomizedProducts(), randomizedProducts()];

  return (
    <section className="feature-large sponsoring space--xxs bg--secondary">
      <div className="container">
        <div className="row justify-content-between">
          <div className="col-lg-5 col-md-5 hidden-xs hidden-sm">
            <img alt="sponsoring" className="border--round box-shadow-wide" src={bg} />
          </div>
          <div className="col-lg-7 col-md-7 col-xs-12 col-sm-12">
            <h3>
              Check out{' '}
              <a href="https://www.abstractapi.com/" target="_blank" rel="noopener">
                Abstract API
              </a>
            </h3>
            <h4>the home for modern, developer-friendly tools</h4>
            <div className="text-block">
              {featuredProducts.map((product) => (
                <>
                  <h5>
                    {product.name}{' '}
                    <a href={product.url} target="_blank" rel="noopener">
                      <FontAwesomeIcon icon={iconLink} />
                    </a>
                  </h5>
                  <p>{product.description}</p>
                </>
              ))}
            </div>
            <a
              className="btn btn--primary btn--unfilled"
              href="https://www.abstractapi.com/"
              target="_blank"
              rel="noopener"
            >
              Get your Free API Key
            </a>
          </div>
        </div>
      </div>
    </section>
  );
};
