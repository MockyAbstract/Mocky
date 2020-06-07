import '../styles.css';

import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory, useParams } from 'react-router-dom';
import { useAsync } from 'react-use';

import { remove, selectMockById } from '../../../redux/mocks/slice';
import MockyAPI from '../../../services/MockyAPI/MockyAPI';
import DeleteMockInformation from '../components/DeleteMockInformation';
import ManagerTitle from '../components/ManagerTitle';
import MockGone from '../components/MockGone';
import SearchingMockLoader from '../components/SearchingMockLoader';
import { DeleteMockParams } from '../types';
import GA from '../../../services/Analytics/GA';

const DeletionApproval = () => {
  const { id, secret }: DeleteMockParams = useParams();
  const dispatch = useDispatch();
  const history = useHistory();

  const [deleting, setDeleting] = useState(false);
  const [deleted, setDeleted] = useState(false);

  // Check if the mock exist in the store
  const mock = useSelector(selectMockById(id, secret));

  const state = useAsync(async () => {
    return MockyAPI.check({ id, secret });
  }, [id, secret]);

  const triggerDelete = async () => {
    setDeleting(true);

    // Delete from the store
    dispatch(remove(id));

    GA.event('mock', 'delete');

    // Try delete from the API
    const result = await MockyAPI.delete({ id, secret });

    // Mock deleted by the API -> confirmation
    if (result) {
      dispatch(remove(id));
      setDeleted(true);
    } else {
      history.push('/manage/delete/done');
    }
  };

  return (
    <>
      <ManagerTitle />

      <section className="text-center space--xxs">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-md-8">
              <div className="boxed boxed--border bg--primary boxed--lg box-shadow">
                <h3>Mock Deletion</h3>

                {(deleted && <MockGone />) ||
                  (state.loading ? (
                    <SearchingMockLoader />
                  ) : state.error ? (
                    <p>Something got wrong...</p>
                  ) : state.value ? (
                    <>
                      <p className="lead">You're about to definitively delete the following mock, are you sure?</p>
                      <DeleteMockInformation id={id} secret={secret} mock={mock} />
                      <button
                        type="submit"
                        className="btn btn--primary btn--confirmation"
                        onClick={triggerDelete}
                        disabled={deleting}
                      >
                        DELETE NOW
                      </button>
                    </>
                  ) : (
                    <p className="lead">This mock don't exist (anymore).</p>
                  ))}
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
};

export default DeletionApproval;
