export interface MockStored {
  name?: string;
  id: string;
  secret: string;
  link: string;
  contentType: string;
  status: number;
  content?: string;
  charset: string;
  headers?: string;
  deleteLink: string;
  createdAt: Date;
  expireAt?: Date;
}

export interface MockState {
  last?: MockStored;
  all: Array<MockStored>;
}
