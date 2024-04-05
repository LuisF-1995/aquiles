import React, { useEffect, useState } from 'react';
import RootHeader from '../components/header/RootHeader';
import Login from '../components/login/Login';
import { Backdrop, CircularProgress } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { applicationPaths } from '../constants/routes';

const RootPage = () => {
  const navigate = useNavigate();
  const [openLogin, setOpenLogin] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);

  const changeLoginState = () => {
    setOpenLogin(!openLogin);
  };

  const changeLoadingState = (loadingState:boolean) => {
    setLoading(loadingState);
  };

  useEffect(() => {
    if(localStorage.length > 0){
      navigate(`/${applicationPaths.dashboard.root}`);
    }
  }, [])

  return (
    <main className='container-fluid p-0'>
      <RootHeader changeLoginState={changeLoginState}/>
      <Login open={openLogin} closeModal={changeLoginState} changeLoadingState={changeLoadingState}/>
      <Backdrop
        sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={loading}
      >
        <CircularProgress color="inherit" />
      </Backdrop>
    </main>
  )
}

export default RootPage;