import './styles.css';

import { FieldProps } from 'formik';
import React from 'react';
import Select, { GroupType } from 'react-select';

import { defaultValue, status, StatusOption } from './models';

const formatGroupLabel = (data: GroupType<StatusOption>) => (
  <div className="select--dark">
    <span className="text-dark">{data.label}</span>
  </div>
);

const formatOptionLabel = (data: StatusOption) => {
  const highlightClass = data.highlight!! ? 'type--bold' : '';
  return (
    <span className={highlightClass}>
      <span className="color--primary">{data.value}</span> - {data.label}
    </span>
  );
};

const customStyles = (hasError: boolean) => ({
  input: () => {
    return { height: 'fit-content' };
  },
  control: (styles: any) => {
    return {
      ...styles,
      borderColor: hasError ? '#dc3545' : styles.borderColor,
      '&:hover': {
        borderColor: hasError ? '#dc3545' : styles.borderColor,
      },
    };
  },
});

const SelectHttpStatusCode = ({
  field,
  form: { touched, errors, setFieldValue },
  ...props
}: FieldProps & { isError: Boolean }) => {
  let hasError = !!errors[field.name] && !!touched[field.name];

  return (
    <Select
      options={status}
      defaultValue={defaultValue}
      formatGroupLabel={formatGroupLabel}
      formatOptionLabel={formatOptionLabel}
      styles={customStyles(hasError)}
      inputId={field.name}
      {...field}
      {...props}
      value={(status ? status.find((option) => option.value === field.value) : '') as any}
      onChange={(option) => setFieldValue(field.name, (option as any).value)}
    />
  );
};

export default SelectHttpStatusCode;
