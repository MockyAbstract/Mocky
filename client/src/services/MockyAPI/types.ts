export interface MockCreated {
  id: string;
  secret: string;
  link: string;
  expireAt: Date;
}

export interface MockCreateAPI {
  status: number;
  content?: string;
  headers?: string;
  charset: string;
  content_type: string;
  secret: string;
  name?: string;
  expiration: string;
}

export interface DeleteMock {
  id: string;
  secret: string;
}
