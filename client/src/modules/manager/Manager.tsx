import React from 'react';
import { useSelector } from 'react-redux';

import { selectAllMocks } from '../../redux/mocks/slice';
import EmptyPlaceholder from './table/EmptyPlaceholder';
import ManagerTable from './table/ManagerTable';
import ManagerTitle from './components/ManagerTitle';
import './styles.css';

const Manager = () => {
  const mocks = useSelector(selectAllMocks);
  const isEmpty = mocks.length === 0;

  return (
    <>
      <ManagerTitle />

      {isEmpty && <EmptyPlaceholder />}

      {!isEmpty && <ManagerTable mocks={mocks} />}
    </>
  );
};

export default Manager;
