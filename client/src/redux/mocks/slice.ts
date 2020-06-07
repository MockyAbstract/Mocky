import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '../store';
import { MockState, MockStored } from './types';

const initialState: MockState = {
  last: undefined,
  all: [],
};

export const mocksSlice = createSlice({
  name: 'mocks',
  initialState,
  reducers: {
    store: (state, action: PayloadAction<MockStored>) => {
      state.last = action.payload;
      state.all.push(action.payload);
    },
    remove: (state, action: PayloadAction<string>) => {
      state.all = state.all.filter((mock) => mock.id !== action.payload);
    },
    clearNew: (state) => {
      state.last = undefined;
    },
  },
});

export const { store, remove, clearNew } = mocksSlice.actions;

export const selectLatestMock = (state: RootState) => state.mocks.last;
export const selectAllMocks = (state: RootState) => state.mocks.all;
export const selectMockById = (id: string, secret: string) => (state: RootState) =>
  state.mocks.all.find((mock) => mock.id === id && mock.secret === secret);
export const selectCountMocks = (state: RootState) => state.mocks.all.length;

export default mocksSlice.reducer;
