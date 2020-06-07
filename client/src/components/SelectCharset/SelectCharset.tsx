import React from 'react';
import Select from 'react-select';
import { charsetOptions } from './models';
import { FieldProps } from 'formik';

const customStyles = {
  input: (provided: any, state: any) => {
    return { height: 'fit-content' };
  },
};

const SelectCharset = ({ field, form: { setFieldValue }, ...props }: FieldProps & { label: string }) => {
  return (
    <Select
      options={charsetOptions}
      styles={customStyles}
      {...field}
      {...props}
      value={(charsetOptions ? charsetOptions.find((option) => option.value === field.value) : '') as any}
      onChange={(option) => setFieldValue(field.name, (option as any).value)}
    />
  );
};

export default SelectCharset;
