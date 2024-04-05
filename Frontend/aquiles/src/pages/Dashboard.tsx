import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import { applicationPaths } from '../constants/routes';
import { Backdrop, CircularProgress } from '@mui/material';
import { ApiRequestResponse, User } from '../constants/Interfaces';
import { storageCompanyIdKeyName, storageTokenKeyName, storageUserIdKeyName } from '../constants/globalConstants';
import { sendGet } from '../services/apiRequests';
import { apiRoutes } from '../constants/Api';

const Dashboard = () => {
  const navigate = useNavigate();
  const [waiting, setWaiting] = useState(true);
  const [userInfo, setUserInfo] = useState<User>(null);

  useEffect(() => {
    if(localStorage.length > 0){
      getUserInfo();
    }
    else{
      Swal.fire({
        title: 'Expiró la sesión',
        text: `La sesión expiró, debe volver a iniciar sesión`,
        icon: 'info',
        confirmButtonText: "Iniciar sesión"
      })
      .then(option => {
        if(option.isConfirmed){
          Swal.close();
          navigate(applicationPaths.root);
        }
        else
          setTimeout(() => {
            Swal.close();
            navigate(`/`);
          }, 5000);
      })
    }
  }, []);

  const getUserInfo = async() => {
    const userId = localStorage.getItem(storageUserIdKeyName);
    const jwtToken = localStorage.getItem(storageTokenKeyName);
    const tenantId = localStorage.getItem(storageCompanyIdKeyName);

    if(userId && jwtToken){
      try {
        const userInfoApi:ApiRequestResponse = await sendGet(`${apiRoutes.userService.root}${apiRoutes.userService.user.root}/${userId}`, jwtToken, tenantId);
        setWaiting(false);

        if(userInfoApi && userInfoApi.httpStatus === 200 && userInfoApi.success && userInfoApi.model)
          setUserInfo(userInfoApi.model);
        else{
          setUserInfo(null);
          Swal.fire({
            title: 'Expiró la sesión',
            text: `La sesión expiró, debe volver a iniciar sesión`,
            icon: 'info',
            confirmButtonText: "Iniciar sesión"
          })
          .then(option => {
            localStorage.clear();
            if(option.isConfirmed){
              Swal.close();
              navigate(applicationPaths.root);
            }
            else
              setTimeout(() => {
                Swal.close();
                navigate(applicationPaths.root);
              }, 5000);
          })
        }
      } 
      catch (error) {
        setWaiting(false);
        localStorage.clear();
      }
    }
  }

  return (
    userInfo ? 
    <div>Dashboard de {userInfo.name}</div>
    :
    <Backdrop
      sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
      open={waiting}
    >
      <CircularProgress color="inherit" />
    </Backdrop>
  )
}

export default Dashboard