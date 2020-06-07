import React from 'react';
import Moment from 'react-moment';

import { MockStored } from '../../../redux/mocks/types';

export default ({ id, secret, mock }: { id: string; secret: string; mock?: MockStored }) => (
  <pre>
    ID: {id}
    <br />
    SECRET: {secret}
    <br />
    {mock && (
      <>
        <br />
        NAME: {mock.name ?? 'Unamed mock'}
        <br />
        CREATION DATE: <Moment format="YYYY-MM-DD HH:MM" withTitle date={mock.createdAt} />
      </>
    )}
  </pre>
);
