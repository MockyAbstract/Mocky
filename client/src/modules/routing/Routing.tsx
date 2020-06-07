import React, { Suspense } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import CookieNotification from '../../components/CookieNotification/CookieNotification';
import ErrorBoundary from '../../components/ErrorBoundary/ErrorBoundary';
import Loader from '../../components/Loader/Loader';
import ScrollToTopOnNavigationChange from '../../components/ScrollToTopOnNavigationChange/ScrollToTopOnNavigationChange';
import TrackPageView from '../../components/TrackPageView/TrackPageView';
import About from '../../modules/about/About';
import Designer from '../../modules/designer/Designer';
import Faq from '../../modules/faq/Faq';
import Home from '../../modules/home/Home';
import Page404 from '../../modules/routing/Page404';
import NewMockConfirmation from '../designer/NewMockConfirmation';
import DeleteSuccessful from '../manager/delete/DeleteSuccessful';
import DeletionApproval from '../manager/delete/DeletionApproval';
import Manager from '../manager/Manager';
import CookiePolicy from '../policies/CookiePolicy';
import Footer from '../skeleton/Footer';
import NavBar from '../skeleton/NavBar';
import Maintenance from '../maintenance/Maintenance';

const isMaintenance = process.env.REACT_APP_MAINTENANCE === 'true';

const Routing = () => (
  <BrowserRouter>
    <ScrollToTopOnNavigationChange />
    <TrackPageView />

    {isMaintenance && <Maintenance />}

    {!isMaintenance && (
      <>
        <NavBar />
        <div className="main-container">
          <ErrorBoundary>
            <Suspense fallback={<Loader />}>
              <Switch>
                <Route exact path="/" component={Home} />
                <Route path="/design/confirmation" component={NewMockConfirmation} />
                <Route path="/design" component={Designer} />
                <Route path="/manage/delete/done" component={DeleteSuccessful} />
                <Route path="/manage/delete/:id/:secret" component={DeletionApproval} />
                <Route path="/manage" component={Manager} />
                <Route path="/about" component={About} />
                <Route path="/faq" component={Faq} />
                <Route path="/policies/cookies" component={CookiePolicy} />
                <Route path="*" component={Page404} />
              </Switch>
            </Suspense>
            <CookieNotification />
          </ErrorBoundary>
          <Footer />
        </div>
      </>
    )}
  </BrowserRouter>
);

export default Routing;
