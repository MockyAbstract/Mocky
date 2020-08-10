import React from 'react';
import './styles.css';

/*  eslint "react/jsx-no-target-blank": ["warn", { "allowReferrer": true }] */

export default () => {
  const isSponsoringEnabled = process.env.REACT_APP_SHOW_SPONSORING === 'true';
  if (!isSponsoringEnabled) return null;

  return (
    <div className="row sponsoring">
      <div className="col-sm-12">
        Sponsored by{' '}
        <a href="https://www.abstractapi.com" target="_blank" rel="noopener">
          <strong>Abstract API</strong>
        </a>
        , the home for modern, developer-friendly tools like the{' '}
        <a href="https://www.abstractapi.com/ip-geolocation-api" target="_blank" rel="noopener">
          IP Geolocation API
        </a>
        ,{' '}
        <a href="https://www.abstractapi.com/vat-validation-rates-api" target="_blank" rel="noopener">
          VAT Validation &amp; Rates API
        </a>
        ,{' '}
        <a href="https://www.abstractapi.com/holidays-api" target="_blank" rel="noopener">
          Public Holiday API
        </a>
        , and more.
      </div>
    </div>
  );
};
