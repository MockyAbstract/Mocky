interface HttpResponse<T> extends Response {
  data?: T;
}

async function get<T>(url: string): Promise<HttpResponse<T>> {
  const request = {
    method: 'GET',
    mode: 'cors' as RequestMode,
  };

  const response: HttpResponse<T> = await fetch(url, request);
  response.data = await response.json();
  return response;
}

async function post<T>(url: string, payload: any): Promise<HttpResponse<T>> {
  const request = {
    headers: { 'Content-Type': 'application/json' },
    method: 'POST',
    body: JSON.stringify(payload),
    mode: 'cors' as RequestMode,
  };

  const response: HttpResponse<T> = await fetch(url, request);
  response.data = await response.json();
  return response;
}

async function _delete(url: string, payload: any): Promise<Boolean> {
  const request = {
    headers: { 'Content-Type': 'application/json' },
    method: 'DELETE',
    body: JSON.stringify(payload),
    mode: 'cors' as RequestMode,
  };

  const response = await fetch(url, request);
  return response.status === 204;
}

const HTTP = {
  get: get,
  post: post,
  delete: _delete,
};

export default HTTP;
