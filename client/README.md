# Mocky Frontend

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app), using the [Redux](https://redux.js.org/) and [Redux Toolkit](https://redux-toolkit.js.org/) template.

## Environment variables

You must define these environment variables. For local development, fill the `.env` file.

### Domain & API

- `REACT_APP_DOMAIN="http://localhost:3000"`: What is the URL of this frontend
- `REACT_APP_API_URL="https://api.mocky.site"`: What is the URL of the mocky API

### Maintenance mode

- `REACT_APP_MAINTENANCE=false`: Set to true to activate the maintenance page

### Analytics tracking

- `REACT_APP_GOOGLE_ANALYTICS_TRACKING_ID="UA-XXXXXX-X"`: Fill this variable if you want to activate Google analytics tracking

### Customization

- `REACT_APP_SHOW_PROMOTING_PANEL=true`: Display or not the promoting panel (give a shoot on twitter, buy me a beer)

## Available Scripts

In the project directory, you can run:

### `yarn start:dev`

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### `yarn test`

Launches the test runner in the interactive watch mode.<br />
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `yarn build`

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.
