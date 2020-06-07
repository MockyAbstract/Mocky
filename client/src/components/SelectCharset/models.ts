export const charsets = ['UTF-8', 'ISO-8859-1', 'UTF-16'];

export interface CharsetOption {
  value: string;
  label: string;
}

export const charsetOptions: Array<CharsetOption> = charsets.map((c) => {
  let obj: CharsetOption = {
    label: c,
    value: c,
  };
  return obj;
});
