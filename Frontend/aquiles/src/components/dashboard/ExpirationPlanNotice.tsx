import React, { useEffect, useState } from 'react'
import { ApiRequestResponse, Tenant } from '../../constants/Interfaces';
import { storageCompanyIdKeyName, storageTokenKeyName } from '../../constants/globalConstants';
import { sendGet } from '../../services/apiRequests';
import { apiRoutes } from '../../constants/Api';
import { HttpStatusCode } from 'axios';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import { applicationPaths } from '../../constants/routes';

const ExpirationPlanNotice = () => {
  const navigate = useNavigate();
  const [tenantInfo, setTenantInfo] = useState<Tenant>(null);
  const [daysRemaining, setDaysRemaining] = useState(null);
  const [color, setColor] = useState('');

  useEffect(() => {
    getTenantInfo();
  }, []);

  const getTenantInfo = async() => {
    const jwtToken = localStorage.getItem(storageTokenKeyName);
    const tenantId = localStorage.getItem(storageCompanyIdKeyName);

    if(jwtToken && tenantId && jwtToken.length > 0 && tenantId.length > 0){

      try{
        const tenantInfoApi:ApiRequestResponse = await sendGet(`${apiRoutes.tenantService.root}${apiRoutes.tenantService.tenant.root}${apiRoutes.tenantService.tenant.byCompanyId}/${tenantId}`, jwtToken, tenantId);
  
        if(tenantInfoApi && tenantInfoApi.success && tenantInfoApi.httpStatus == HttpStatusCode.Ok && tenantInfoApi.model)
          setTenantInfo(tenantInfoApi.model);
        else{
          setTenantInfo(null);
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
          });
        }
      }
      catch (error) {
        localStorage.clear();
      }
    }
  };

  const calculateDaysRemaining = (registrationDate:Date) => {
    const endDate:Date = new Date(registrationDate);
    endDate.setDate(endDate.getDate() + 30);
    const now:Date = new Date();
    const difference = endDate.getTime() - now.getTime();
    return Math.ceil(difference / (1000 * 3600 * 24));
  };

  const setAlertColor = (registrationDate:Date):string => {
    const days = calculateDaysRemaining(registrationDate);

    if (days <= 10) {
      return 'red';
    } else if (days <= 20) {
      return 'orange';
    } else {
      return 'green';
    }
  }; 
  

  return (
    tenantInfo && tenantInfo.plan === 'FREE' &&
    <section style={{backgroundColor:setAlertColor(tenantInfo.registrationDate)}}>
      This is a free license and expires in {calculateDaysRemaining(tenantInfo.registrationDate)} days, enjoy!!
    </section>
  )
}

export default ExpirationPlanNotice