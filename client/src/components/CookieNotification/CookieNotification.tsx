import './styles.css';

import React from 'react';
import { useCookie } from 'react-use';

import { faTimes as iconClose } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavLink } from 'react-router-dom';

export default () => {
  const [cookieDefined, setCookie] = useCookie('cookie-banner');

  return (
    <>
      {!cookieDefined && (
        <div role="dialog" className="cookie--block">
          <div className="cookie--body">
            <p className="cookie--description">
              By using our website you agree to our <NavLink to="/policies/cookies">Cookie Policy</NavLink>
            </p>
          </div>

          <div className="cookie--close">
            <button type="button" onClick={() => setCookie('dismiss')}>
              <FontAwesomeIcon icon={iconClose} size="lg" />
            </button>
          </div>
        </div>
      )}
    </>
  );
};
