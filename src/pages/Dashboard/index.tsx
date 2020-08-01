import React from 'react';

import logoImg from '../../assets/utils/logo.svg';
import elesisIcon from '../../assets/heroes/Elesis/Elesis-IconHero.png';
import ioIcon from '../../assets/heroes/Io/Io-IconHero.png';
import amyIcon from '../../assets/heroes/Amy/Amy-IconHero.png';
import unknownIcon from '../../assets/utils/unknown.svg';

import { Container, Header, Team, HeroCard } from './styles';

const Dashboard: React.FC = () => (
  <Container>
    <Header id="main-header">
      <div className="content">
        <img src={logoImg} alt="Buffstack" />
        <h1>
          <strong>B</strong>uff<strong>S</strong>tack
        </h1>
      </div>
    </Header>

    <Team>
      <div className="content">
        <ul>
          <li>
            <HeroCard>
              <img src={elesisIcon} alt="Elesis" />
              <p>Elesis</p>
            </HeroCard>
          </li>
          <li>
            <HeroCard>
              <img src={ioIcon} alt="Elesis" />
              <p>Hero name</p>
            </HeroCard>
          </li>
          <li>
            <HeroCard>
              <img src={amyIcon} alt="Elesis" />
              <p>Hero name</p>
            </HeroCard>
          </li>
          <li>
            <HeroCard>
              <img src={unknownIcon} alt="Elesis" />
              <p />
            </HeroCard>
          </li>
        </ul>
      </div>
    </Team>
  </Container>
);
export default Dashboard;
