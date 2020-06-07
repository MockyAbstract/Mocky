import { useFormikContext } from 'formik';
import { useEffect } from 'react';
import { useDispatch } from 'react-redux';

import { clearNew } from '../../../redux/mocks/slice';

const CleanConfirmationOnSubmit = () => {
  const dispatch = useDispatch();

  const { isValidating } = useFormikContext();

  // Clean the confirmation message when the form is re-submited
  useEffect(() => {
    if (isValidating) {
      dispatch(clearNew());
    }
  }, [isValidating, dispatch]);

  return null;
};

export default CleanConfirmationOnSubmit;
