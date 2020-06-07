const express = require('express');
const history = require('connect-history-api-fallback');
const enforce = require('express-sslify');

const app = express();

// allow to call history-mode route
app.use(history());

// Redirect http to https (with "trustProtoHeader" because the app will be behind a LB)
app.use(enforce.HTTPS({ trustProtoHeader: true }));

// Serve all the files in '/build' directory
app.use(express.static('build'));

app.listen(process.env.PORT, '0.0.0.0', () => {
  console.log(`Mocky is running on port ${process.env.PORT}`); // eslint-disable-line no-console
});
