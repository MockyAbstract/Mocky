import React from 'react';
import Moment from 'react-moment';
import wrap from 'word-wrap';

import {
  faEdit as iconEdit,
  faExternalLinkAlt as iconOpen,
  faTrash as iconDelete,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { MockStored } from '../../../redux/mocks/types';
import { NavLink } from 'react-router-dom';

const ManagerTable = (props: { mocks: MockStored[] }) => (
  <section className="text-center space--xxs">
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-12">
          <table className="table border--round table--alternate-row table-sm">
            <thead className="thead-dark">
              <tr>
                <th style={{ width: '35%' }}>Name</th>
                <th style={{ width: '45 %' }}>Description</th>
                <th style={{ width: '15%' }}>Date</th>
                <th style={{ width: '10%' }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {props.mocks.map((mock) => {
                const name = (mock.name && <span>{wrap(mock.name, { width: 30, cut: true })}</span>) || (
                  <span>{mock.id}</span>
                );

                const resume = (
                  <>
                    <span className="badge badge-dark">{mock.status}</span>&nbsp;
                    <span className="badge badge-info">{mock.contentType}</span>&nbsp;
                    <span className="badge badge-warning">{mock.charset}</span>
                    {(mock.content && (
                      <pre className="resume">
                        {/**/}
                        {(mock.content ?? '').substr(0, 2000).trim()}
                      </pre>
                    )) || (
                      <>
                        <br />
                        <span className="badge badge-light">NO CONTENT</span>
                      </>
                    )}
                  </>
                );

                const createdAt = (
                  <span>
                    Created on
                    <br />
                    <Moment format="YYYY-MM-DD HH:MM" withTitle date={mock.createdAt} />
                  </span>
                );

                const expireAt = mock.expireAt && (
                  <>
                    <hr />
                    <span>
                      Expire <Moment withTitle to={mock.expireAt} />
                    </span>
                  </>
                );

                const openLink = (
                  <a href={mock.link} target="_blank" rel="noopener noreferrer">
                    <FontAwesomeIcon icon={iconOpen} />
                  </a>
                );

                const deleteLink = (
                  <NavLink to={`/manage/delete/${mock.id}/${mock.secret}`} className="icon-delete">
                    <FontAwesomeIcon icon={iconDelete} />
                  </NavLink>
                );

                const editLink = (
                  <span className="icon-edit" data-tooltip="SOON!">
                    <FontAwesomeIcon icon={iconEdit} />
                  </span>
                );

                return (
                  <tr key={mock.id}>
                    <td>{name}</td>
                    <td>{resume}</td>
                    <td>
                      {createdAt}
                      {expireAt}
                    </td>
                    <td>
                      {openLink}&nbsp;&nbsp;&nbsp;
                      {editLink}&nbsp;&nbsp;&nbsp;
                      {deleteLink}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>

          <div className="alert bg--primary">
            <div className="alert__body">
              <strong>Warning</strong>: These data are stored on your computer. It will be lost if you clean your
              browser cache (local-storage).
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
);

export default ManagerTable;
