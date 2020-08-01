import styled from 'styled-components';

export const Container = styled.div`
  height: 100vh;
`;

export const Header = styled.div`
  height: 100px;
  background: #800000;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.25);
  position: relative;
  z-index: 1;

  flex: 1;

  .content {
    max-width: 1100px;
    height: 100px;
    margin: 0 auto;

    display: flex;
  }

  img {
    height: 96px;
    width: 96px;
    margin: 2px 0;
  }

  h1 {
    font-weight: 600;
    font-size: 72px;
    height: 96px;
    margin-top: 16px;
  }
`;

export const Team = styled.div`
  height: 180px;
  width: 1100px;
  margin: 0 auto;

  .content {
    max-width: 1100px;
  }

  ul {
    display: flex;
    padding-top: 24px;
  }

  li {
    list-style-type: none;
    margin-left: 42px;
  }

  li:first-child {
    margin: 0;
  }
`;

export const HeroCard = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;

  height: 116px;
  width: 96px;

  border: 2px solid #f06542;

  img {
    height: 92px;
    width: 92px;
    border: 2px solid #9d20c3;
  }

  p {
    height: 20px;
    text-align: center;
    width: 100%;
    background: #f06542;

    font-size: 14px;
    font-weight: bold;
  }
`;
