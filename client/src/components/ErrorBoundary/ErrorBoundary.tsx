import React, { Component, ErrorInfo } from 'react';
import Page500 from '../../modules/routing/Page500';

type ErrorBoundaryState = {
  hasError: Boolean;
};

class ErrorBoundary extends Component<{}, ErrorBoundaryState> {
  private initialState: ErrorBoundaryState = {
    hasError: false,
  };

  public state: ErrorBoundaryState = this.initialState;

  public static getDerivedStateFromError(): ErrorBoundaryState {
    return { hasError: true };
  }

  public componentDidCatch(error: Error, info: ErrorInfo) {
    console.error('ErrorBoundary caught an error', error, info);
  }

  public render() {
    if (this.state.hasError) {
      return <Page500 />;
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
