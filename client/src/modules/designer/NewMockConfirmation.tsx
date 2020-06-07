import './styles.css';

import React, { useState } from 'react';
import CopyToClipboard from 'react-copy-to-clipboard';
import { useSelector } from 'react-redux';
import { Redirect } from 'react-router-dom';

import { faCopy as iconCopy } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { selectLatestMock, selectCountMocks } from '../../redux/mocks/slice';
import DesignerTitle from './components/DesignerTitle';
import NewMockFeatures from './components/NewMockFeatures';
import Pub from './components/Pub';

const NewMockConfirmation = () => {
  const [copied, setCopied] = useState(0);
  const mock = useSelector(selectLatestMock);
  const nbMocks = useSelector(selectCountMocks);

  const isPromotingActivated = process.env.REACT_APP_SHOW_PROMOTING_PANEL === 'true';

  if (!mock) {
    return <Redirect to="/design" />;
  }

  return (
    <>
      <DesignerTitle />

      {isPromotingActivated && (nbMocks === 2 || nbMocks % 4 === 0) && <Pub />}

      <section className="space--xxs bg--primary">
        <div className="container">
          <div className="row justify-content-center no-gutters">
            <div className="col-md-10 col-lg-8 text-center">
              <h3>{copied > 0 ? "Link copied, You're now ready!" : 'Your mock is ready!'}</h3>

              <div>
                <h4 className="mb-2">
                  Mock URL
                  <CopyToClipboard text={mock.link} onCopy={() => setCopied(1)}>
                    <FontAwesomeIcon icon={iconCopy} className="iconMocky--main" />
                  </CopyToClipboard>
                </h4>

                <pre className="user-select-all">
                  <a href={mock.link} target="_blank" rel="noopener noreferrer">
                    {mock.link}
                  </a>
                </pre>
              </div>

              <span className="type--fade" data-tooltip="This link allow you to delete your mock whenever you want">
                Secret delete link
                <CopyToClipboard text={mock.deleteLink}>
                  <FontAwesomeIcon icon={iconCopy} className="iconMocky--sec" />
                </CopyToClipboard>
              </span>

              <pre className="unpad unmarg--bottom type--fade user-select-all">
                <small>{mock.deleteLink}</small>
              </pre>
            </div>
          </div>
        </div>
      </section>
      <NewMockFeatures />
    </>
  );
};

export default NewMockConfirmation;
