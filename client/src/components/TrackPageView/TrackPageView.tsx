import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import GA from '../../services/Analytics/GA';

export default () => {
  const { pathname } = useLocation();

  useEffect(() => {
    GA.pageView(pathname);
  }, [pathname]);

  return null;
};
