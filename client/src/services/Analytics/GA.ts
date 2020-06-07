import ReactGA from 'react-ga';

var initilized = false;

const initialize = (debug: boolean = false) => {
  const trackingId = process.env.REACT_APP_GOOGLE_ANALYTICS_TRACKING_ID;

  if (trackingId) {
    initilized = true;
    ReactGA.initialize(trackingId, { debug: debug });
  }
};

const pageView = (page: string) => {
  if (!initilized) return;
  ReactGA.pageview(page);
};

const event = (category: string, action: string) => {
  if (!initilized) return;
  ReactGA.event({ category, action });
};

const GA = {
  initialize,
  pageView,
  event,
};

export default GA;
