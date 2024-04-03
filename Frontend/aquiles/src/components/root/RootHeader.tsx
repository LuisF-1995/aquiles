import React from 'react';
import './styles.css';
import { ExitToAppRounded, PersonAddRounded } from '@mui/icons-material';
import { Link } from 'react-router-dom';
import { applicationPaths } from '../../constants/routes';

const RootHeader = ({changeLoginState}) => {

  const openLogin = () => {
    changeLoginState();
  };

  return (
    <header>
      <nav className="navbar py-1" data-bs-theme="dark">
        <section className="container-fluid">
          <a className="navbar-brand p-0" href="">
            <img src="/images/aquiles-logo.png" alt="Aquiles-logo" width="50" height="50" />
            <span style={{marginLeft:10}}>
              Aquiles
            </span>
          </a>
          <div className='access-buttons'>
            <Link type="button" className="btn btn-info" to={applicationPaths.registerTenant}>
              <PersonAddRounded/>
              Register
            </Link>
            <button type="button" className="btn btn-outline-light" onClick={openLogin} >
              <ExitToAppRounded/>
              Login
            </button>
          </div>
        </section>
      </nav>
    </header>
  )
}

export default RootHeader