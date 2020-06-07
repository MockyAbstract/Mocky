import React from 'react';
import { default as Loading } from 'react-loader-spinner';

import 'react-loader-spinner/dist/loader/css/react-spinner-loader.css';
import './styles.css';

export default () => (
  <div className="loaderWrapper">
    <Loading type="ThreeDots" color="#4a90e2" width={50} height={30} />
  </div>
);
