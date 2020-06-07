import { NewMockFormValues } from '../../modules/designer/form/types';
import Random from 'randomstring';
import { MockCreated, MockCreateAPI } from './types';
import { MockStored } from '../../redux/mocks/types';

/**
 * Transform Mock form data to the payload expected by the API
 */
const formToApi = (data: NewMockFormValues): MockCreateAPI => {
  const headers = data.headers && data.headers !== '' ? JSON.parse(data.headers) : undefined;
  const secret = data.secret && data.secret !== '' ? data.secret : Random.generate(36);
  const content = data.body && data.body !== '' ? data.body : undefined;
  const name = data.name && data.name !== '' ? data.name : undefined;

  return {
    status: data.status,
    content: content,
    content_type: data.contentType,
    charset: data.charset,
    secret: secret,
    name: name,
    expiration: data.expiration,
    headers: headers,
  };
};

/**
 * Construct from mock API response and mock API request the data to store in the local-storage
 * for future usage
 */
const createdToStore = (created: MockCreated, data: MockCreateAPI): MockStored => {
  const { name, content_type, status, content, charset, headers } = data;

  const deleteLink = `${process.env.REACT_APP_DOMAIN}/manage/delete/${created.id}/${created.secret}`;
  const createdAt = new Date();

  return { ...created, name, status, content, charset, headers, contentType: content_type, deleteLink, createdAt };
};

const MockyAPITransformer = {
  formToApi,
  createdToStore,
};

export default MockyAPITransformer;
