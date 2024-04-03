import React, { useState } from 'react';
import RootHeader from '../components/root/RootHeader';
import Login from '../components/login/Login';

const RootPage = () => {
  const [openLogin, setOpenLogin] = useState<boolean>(false);

  const changeLoginState = () => {
    setOpenLogin(!openLogin);
  };

  return (
    <main className='container-fluid p-0'>
      <RootHeader changeLoginState={changeLoginState}/>
      <Login open={openLogin} closeModal={changeLoginState}/>
    </main>
  )
}

export default RootPage;