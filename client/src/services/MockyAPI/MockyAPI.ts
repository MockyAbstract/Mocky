import { NewMockFormValues } from '../../modules/designer/form/types';
import MockyAPITransformer from './MockAPITransformer';
import HTTP from '../HTTP';
import { MockCreated, DeleteMock } from './types';
import { MockStored } from '../../redux/mocks/types';

const URL = process.env.REACT_APP_API_URL + '/api/mock';

const create = async (data: NewMockFormValues): Promise<MockStored | undefined> => {
  const payload = MockyAPITransformer.formToApi(data);
  const response = await HTTP.post<MockCreated>(URL, payload);

  if (!response.data) {
    console.error(`API Response error: ${response.body}`);
    return undefined;
  } else {
    return MockyAPITransformer.createdToStore(response.data, payload);
  }
};

const _delete = async (data: DeleteMock): Promise<Boolean> => {
  return await HTTP.delete(`${URL}/${data.id}`, data);
};

const check = async (data: DeleteMock): Promise<Boolean> => {
  const response = await HTTP.post<Boolean>(`${URL}/${data.id}/check`, data);
  return response.data ?? false;
};

const MockyAPI = {
  delete: _delete,
  check,
  create,
};

export default MockyAPI;
