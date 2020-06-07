import React from 'react';
import { Field } from 'formik';

const SelectExpirationTime = (props: { name: string }) => {
  const { name } = props;

  return (
    <Field as="select" component="select" name={name}>
      <option value="never">Never expire</option>
      <option value="1year">Expire in 1 year</option>
      <option value="1month">Expire in 1 month</option>
      <option value="1week">Expire in 1 week</option>
      <option value="1day">Expire in 1 day</option>
    </Field>
  );
};

export default SelectExpirationTime;
