export const contentTypes = [
  'application/json',
  'application/x-www-form-urlencoded',
  'application/xhtml+xml',
  'application/xml',
  'multipart/form-data',
  'text/css',
  'text/csv',
  'text/html',
  'text/json',
  'text/plain',
  'text/xml',
];

export interface ContentTypeOption {
  value: string;
  label: string;
}

export const contentTypesOptions: Array<ContentTypeOption> = contentTypes.map((c) => {
  let obj: ContentTypeOption = {
    label: c,
    value: c,
  };
  return obj;
});

export const defaultValue = contentTypesOptions[0];
