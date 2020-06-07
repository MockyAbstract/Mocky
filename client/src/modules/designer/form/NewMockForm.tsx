import { withFormik } from 'formik';
import { connect } from 'react-redux';

import { store } from '../../../redux/mocks/slice';
import MockyAPI from '../../../services/MockyAPI/MockyAPI';
import { initialState, newMockValidationSchema } from './models';
import NewMockFormView from './NewMockFormView';
import { NewMockFormProps, NewMockFormValues } from './types';
import { withRouter, RouteComponentProps } from 'react-router-dom';
import GA from '../../../services/Analytics/GA';

const mapDispatchToProps = { store };

const form = withFormik<NewMockFormProps & typeof mapDispatchToProps & RouteComponentProps, NewMockFormValues>({
  validationSchema: newMockValidationSchema,
  validateOnBlur: true,
  validateOnChange: false,
  mapPropsToValues: (initialProps) => initialState,
  handleSubmit: async (values, { props }) => {
    const result = await MockyAPI.create(values);
    if (result !== undefined) {
      GA.event('mock', 'create');
      props.store(result);
      props.history.push('/design/confirmation');
    }
  },
})(NewMockFormView);

const NewMockForm = withRouter(connect(null, mapDispatchToProps)(form));

export default NewMockForm;
