import React from 'react';
import { FieldProps } from 'formik';
import TextareaAutosize from 'react-textarea-autosize';

const placeholder = JSON.stringify({ 'X-Foo-Bar': 'Hello World' }, null, 2);

const TextareaHeaders = ({ field, form: { touched, errors }, ...props }: FieldProps & { label: string }) => {
  let classNameError = !!errors[field.name] && !!touched[field.name] ? 'input--error' : '';

  return (
    <TextareaAutosize
      minRows={4}
      maxRows={6}
      placeholder={placeholder}
      className={`textarea--code ${classNameError}`}
      {...field}
      {...props}
    />
  );
};

export default TextareaHeaders;
