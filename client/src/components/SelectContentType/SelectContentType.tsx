import React from 'react';
import CreatableSelect from 'react-select/creatable';
import { contentTypesOptions, defaultValue } from './models';
import { FieldProps } from 'formik';

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

const SelectContentType = ({
  field,
  form: { touched, errors, setFieldValue },
  ...props
}: FieldProps & { label: string }) => {
  let hasError = !!errors[field.name] && !!touched[field.name];

  return (
    <CreatableSelect
      isClearable
      placeholder="Select (or create your own)"
      defaultValue={defaultValue}
      options={contentTypesOptions}
      styles={customStyles(hasError)}
      inputId={field.name}
      {...field}
      {...props}
      value={(contentTypesOptions ? contentTypesOptions.find((option) => option.value === field.value) : '') as any}
      onChange={(option) => {
        if (option != null) {
          setFieldValue(field.name, (option as any).value, true);
        } else {
          setFieldValue(field.name, '');
        }
      }}
    />
  );
};

export default SelectContentType;
