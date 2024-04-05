import React, { useEffect, useState } from 'react';
import { ApiRequestResponse, User } from '../constants/Interfaces';
import { storageCompanyIdKeyName, storageTokenKeyName, storageUserIdKeyName } from '../constants/globalConstants';
import { sendGet } from '../services/apiRequests';
import Swal from 'sweetalert2';
import { apiRoutes } from '../constants/Api';
import { useNavigate } from 'react-router-dom';
import { applicationPaths } from '../constants/routes';

const UserUtils = () => {

  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState<User>(null);
  const [waiting, setWaiting] = useState<boolean>(false);
  
  
  
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
  };

  return (
    <div>uU erUtils</div>
  )
}

export default UserUtils