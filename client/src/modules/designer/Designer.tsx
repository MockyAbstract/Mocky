import React from 'react';

import DesignerTitle from './components/DesignerTitle';
import NewMockFeatures from './components/NewMockFeatures';
import NewMockForm from './form/NewMockForm';

export default () => {
  return (
    <>
      <DesignerTitle />

      <NewMockForm />

      <NewMockFeatures />
    </>
  );
};
