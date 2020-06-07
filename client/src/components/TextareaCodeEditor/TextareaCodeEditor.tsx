import React from 'react';
import { FieldProps } from 'formik';
import TextareaAutosize from 'react-textarea-autosize';

const placeholder = JSON.stringify(
  {
    identity: {
      id: 'b06cd03f-75d0-413a-b94b-35e155444d70',
      login: 'John Doe',
    },
    permissions: { roles: ['moderator'] },
  },
  null,
  2
);

const TextareaCodeEditor = ({ field, form, ...props }: FieldProps & { label: string }) => {
  return (
    <TextareaAutosize
      minRows={12}
      maxRows={20}
      placeholder={placeholder}
      className="textarea--code"
      {...field}
      {...props}
    />
  );
};

export default TextareaCodeEditor;
