import { createGlobalStyle } from 'styled-components';

export default createGlobalStyle`
  * {
    margin: 0;
    padding: 0;
    box-sizing:border-box;
    outline: 0;
  }

  body {
    background: #B73317;
    color: #E5E5E5;
    -webkit-font-smoothing: antialiased;
  }

  body, input, button {
    font-family: 'Open Sans', sans-serif;
    font-weight: 600;
  }

  strong {
    font-weight: 700;
  }

  button {
    cursor: pointer;
  }
`;
