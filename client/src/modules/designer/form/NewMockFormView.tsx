import './styles.css';

import { ErrorMessage, FastField, Form, FormikProps } from 'formik';
import React from 'react';

import SelectCharset from '../../../components/SelectCharset/SelectCharset';
import SelectContentType from '../../../components/SelectContentType/SelectContentType';
import SelectExpirationTime from '../../../components/SelectExpirationTime/SelectExpirationTime';
import SelectHttpStatusCode from '../../../components/SelectHttpStatusCode/SelectHttpStatusCode';
import TextareaCodeEditor from '../../../components/TextareaCodeEditor/TextareaCodeEditor';
import TextareaHeaders from '../../../components/TextareaHeaders/TextareaHeaders';
import CleanConfirmationOnSubmit from './CleanConfirmationOnSubmit';
import { NewMockFormValues } from './types';

const NewMockFormView = (props: FormikProps<NewMockFormValues>) => {
  const { touched, errors, isSubmitting, submitCount, isValid } = props;

  return (
    <section className="space--xxs bg--secondary">
      <div className="container">
        <div className="row justify-content-center no-gutters">
          <div className="col-md-10 col-lg-8">
            <div className="boxed boxed--border">
              <Form className="text-left mx-0">
                <CleanConfirmationOnSubmit />

                <div className="row">
                  <div className="col-md-6">
                    <Label>HTTP Status</Label>
                    <RequiredTag />
                    <FastField type="text" name="status" component={SelectHttpStatusCode} />
                    <ErrorFeedback name="status" />
                    <Help>The HTTP Code of the HTTP response you'll receive.</Help>
                  </div>
                </div>

                <div className="row mt-3">
                  <div className="col-md-6">
                    <Label>Response Content Type</Label>
                    <RequiredTag />

                    <FastField type="text" name="contentType" component={SelectContentType} />
                    <ErrorFeedback name="contentType" />
                    <Help>The Content-Type header that will be sent with the response.</Help>
                  </div>
                  <div className="col-md-6">
                    <Label>Charset</Label>
                    <RequiredTag />

                    <FastField type="text" name="charset" component={SelectCharset} />
                    <ErrorFeedback name="charset" />
                    <Help>The Charset used to encode/decode your payload.</Help>
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-12">
                    <Label>HTTP Headers</Label>
                    <OptionalTag />

                    <FastField type="text" name="headers" component={TextareaHeaders} />
                    <ErrorFeedback name="headers" />
                    <Help>Customize the HTTP headers sent in the response. Define the headers as a JSON object.</Help>
                  </div>
                </div>
                <div className="row mt-3">
                  <div className="col-md-12">
                    <Label>HTTP Response Body</Label>
                    <OptionalTag />

                    <FastField type="text" name="body" component={TextareaCodeEditor} />
                    <ErrorFeedback name="body" />
                  </div>
                </div>
                <hr className="mt-4"></hr>
                <h5 className="mb-2">
                  Options to manage your mock after its creation
                  <OptionalTag />
                </h5>
                <div className="row">
                  <div className="col-md-6">
                    <Label>Secret token</Label>

                    <FastField
                      type="text"
                      name="secret"
                      className={`form-control ${!!errors.secret && !!touched.secret ? 'input--error' : ''}`}
                    />
                    <ErrorFeedback name="secret" />
                    <Help>
                      Required to update/delete your mock.
                      <br />
                      If blank, a random secret will be generated.
                    </Help>
                  </div>

                  <div className="col-md-6 mb-5">
                    <Label>Mock identifier</Label>
                    <FastField
                      type="string"
                      name="name"
                      className={`form-control ${!!errors.name && !!touched.name ? 'input--error' : ''}`}
                    />
                    <ErrorFeedback name="name" />
                    <Help>
                      Just a name to identify this mock in your management console later.
                      <br />
                      &nbsp;
                    </Help>
                  </div>
                </div>
                {submitCount > 0 && !isValid && (
                  <div className="alert bg--error">
                    <div className="alert__body">
                      <span>Please fix the errors before saving your mock!</span>
                    </div>
                  </div>
                )}
                <div className="row">
                  <div className="col-md-8 ">
                    <button type="submit" className="btn btn--primary type--uppercase" disabled={isSubmitting}>
                      Generate my HTTP Response
                    </button>
                  </div>
                  <div className="col-md-4 ">
                    <div className="expiration-select input-select">
                      <SelectExpirationTime name="expiration" />
                    </div>
                  </div>
                </div>
              </Form>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

const RequiredTag = () => (
  <>
    &nbsp;<span className="badge badge-info float-right">REQUIRED</span>
  </>
);

const OptionalTag = () => (
  <>
    &nbsp;<span className="badge badge-dark type--fade float-right">OPTIONAL</span>
  </>
);

const Label = (props: React.PropsWithChildren<any>) => <span className="color--dark">{props.children}</span>;

const ErrorFeedback = (props: { name: string }) => (
  <ErrorMessage component="span" className="form-text color--error" name={props.name} />
);

const Help = (props: React.PropsWithChildren<any>) => (
  <small className="form-text color--primary">{props.children}</small>
);

export default NewMockFormView;
