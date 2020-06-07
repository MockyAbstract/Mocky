import * as Yup from 'yup';

import { NewMockFormValues } from './types';

export const initialState: NewMockFormValues = {
  status: 200,
  charset: 'UTF-8',
  contentType: 'application/json',
  headers: '',
  body: '',
  secret: '',
  name: '',
  expiration: 'never',
};

// Check if the header field is a "basic" object with string key and string value
// The key must be an alphanumric key
const validateJsonHeaders = {
  name: 'isJsonHeaders',
  exclusive: false,
  /* eslint no-template-curly-in-string: 0 */
  message: '${path} must be a valid JSON object',
  test: function (this: Yup.TestContext, value: string) {
    if (value === undefined) return true;
    try {
      const json = JSON.parse(value);
      if (typeof json !== 'object') return this.createError({ message: 'Headers must be a JSON object' });
      if (!Object.keys(json).every((x) => typeof x === 'string' && x.match(/^[a-zA-Z0-9-_]*$/)))
        return this.createError({ message: 'Headers key must be simple string (allowed characters: a-z A-Z 0-9 _ -)' });
      if (!Object.values(json).every((x) => typeof x === 'string'))
        return this.createError({ message: 'Headers values must be a string' });
      return true;
    } catch (e) {
      return this.createError({ message: 'Headers must be a JSON object' });
    }
  },
};

export const newMockValidationSchema = Yup.object({
  status: Yup.number().required('Please select the status code of your mock.'),
  contentType: Yup.string().max(200).required('Please define the content-type of your mock.'),
  charset: Yup.string().max(50).required('Please select the charset of your mock.'),
  headers: Yup.string().trim().max(1000).test(validateJsonHeaders).optional(),
  name: Yup.string().max(100).optional(),
  body: Yup.string().trim().ensure().max(1000000).optional(),
  secret: Yup.string().max(64).optional(),
});
