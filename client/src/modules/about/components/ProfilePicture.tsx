import './ProfilePicture.css';

import React from 'react';

import profile from './assets/profile.jpg';

export default () => (
  <img alt="profile" className="profilePicture-round-double profilePicture-shadow-leftBottom" src={profile} />
);
