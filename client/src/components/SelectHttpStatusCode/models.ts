import { GroupedOptionsType } from 'react-select';

export interface StatusOption {
  value: string;
  label: string;
  highlight?: boolean;
}

type StatusGroupedOptions = GroupedOptionsType<StatusOption>;

export const defaultValue: StatusOption = { value: '200', label: 'OK', highlight: true };

export const status: StatusGroupedOptions = [
  {
    label: '1xx Informational response',
    options: [
      { value: '100', label: 'Continue' },
      { value: '101', label: 'Switching Protocols' },
      { value: '102', label: 'Processing' },
    ],
  },
  {
    label: '2xx Success',
    options: [
      { value: '200', label: 'OK', highlight: true },
      { value: '201', label: 'Created', highlight: true },
      { value: '202', label: 'Accepted' },
      { value: '203', label: 'Non-Authoritative Information' },
      { value: '204', label: 'No Content', highlight: true },
      { value: '205', label: 'Reset Content' },
      { value: '206', label: 'Partial Content' },
      { value: '207', label: 'Multi-Status' },
      { value: '208', label: 'Already Reported' },
      { value: '226', label: 'IM Used' },
    ],
  },
  {
    label: '3xx Redirection',
    options: [
      { value: '300', label: 'Multiple Choices' },
      { value: '301', label: 'Moved Permanently' },
      { value: '302', label: 'Found' },
      { value: '303', label: 'See Other' },
      { value: '304', label: 'Not Modified' },
      { value: '305', label: 'Use Proxy' },
      { value: '306', label: 'Switch Proxy' },
      { value: '307', label: 'Temporary Redirect' },
      { value: '308', label: 'Permanent Redirect' },
    ],
  },
  {
    label: '4xx Client errors',
    options: [
      { value: '400', label: 'Bad Request', highlight: true },
      { value: '401', label: 'Unauthorized', highlight: true },
      { value: '402', label: 'Payment Required', highlight: true },
      { value: '403', label: 'Forbidden', highlight: true },
      { value: '404', label: 'Not Found', highlight: true },
      { value: '405', label: 'Method Not Allowed' },
      { value: '406', label: 'Not Acceptable' },
      { value: '407', label: 'Proxy Authentication Required' },
      { value: '408', label: 'Request Timeout' },
      { value: '409', label: 'Conflict' },
      { value: '410', label: 'Gone' },
      { value: '411', label: 'Length Required' },
      { value: '412', label: 'Precondition Failed' },
      { value: '413', label: 'Request Entity Too Large' },
      { value: '414', label: 'Request-URI Too Long' },
      { value: '415', label: 'Unsupported Media Type' },
      { value: '416', label: 'Requested Range Not Satisfiable' },
      { value: '417', label: 'Expectation Failed' },
      { value: '418', label: 'Im a teapot' },
      { value: '420', label: 'Enhance Your Calm' },
      { value: '422', label: 'Unprocessable Entity', highlight: true },
      { value: '423', label: 'Locked' },
      { value: '424', label: 'Failed Dependency' },
      { value: '425', label: 'Unordered Collection' },
      { value: '426', label: 'Upgrade Required' },
      { value: '428', label: 'Precondition Required' },
      { value: '429', label: 'Too Many Requests' },
      { value: '431', label: 'Request Header Fields Too Large' },
      { value: '444', label: 'No Response' },
      { value: '449', label: 'Retry With' },
      { value: '450', label: 'Blocked by Windows Parental Controls' },
      { value: '499', label: 'Client Closed Request' },
    ],
  },
  {
    label: '5xx Server errors',
    options: [
      { value: '500', label: 'Internal Server Error', highlight: true },
      { value: '501', label: 'Not Implemented' },
      { value: '502', label: 'Bad Gateway' },
      { value: '503', label: 'Service Unavailable', highlight: true },
      { value: '504', label: 'Gateway Timeout' },
      { value: '505', label: 'HTTP Version Not Supported' },
      { value: '506', label: 'Variant Also Negotiates' },
      { value: '507', label: 'Insufficient Storage' },
      { value: '509', label: 'Bandwidth Limit Exceeded' },
      { value: '510', label: 'Not Extended' },
    ],
  },
];
