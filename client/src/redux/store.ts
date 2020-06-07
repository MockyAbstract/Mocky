import { configureStore, ThunkAction, Action } from '@reduxjs/toolkit';
import mocksReducer from './mocks/slice';

import { save, load } from 'redux-localstorage-simple';

export const store = configureStore({
  reducer: {
    mocks: mocksReducer,
  },
  // Store / Load store from local-storage
  middleware: [save()],
  preloadedState: load(),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export type AppThunk<ReturnType = void> = ThunkAction<ReturnType, RootState, unknown, Action<string>>;
