import React, { useState } from 'react';
import { ApiRequestResponse, Tenant, TenantRegisterContainer, User } from '../constants/Interfaces';
import RootHeader from '../components/header/RootHeader';
import Login from '../components/login/Login';
import { Backdrop, CircularProgress, FormControl, IconButton, InputAdornment, InputLabel, OutlinedInput, TextField } from '@mui/material';
import { SendRounded, VisibilityOff, Visibility } from '@mui/icons-material';
import CountrySelect from '../components/custom-components/CountrySelect';
import { sendPost } from '../services/apiRequests';
import { apiRoutes } from '../constants/Api';
import Swal from 'sweetalert2';


const RegisterTenant = () => {
  const [loading, setLoading] = useState(false);
  const [tenant, setTenant] = useState<Tenant>({
    companyName: "",
    companyId: "",
    country: "",
    isolated: false,
    plan: "FREE"
  });
  const [owner, setOwner] = useState<User>({
    name: "",
    lastname: "",
    dni: "",
    email: "",
    password: ""
  });
  const [tenantRegister, setTenantRegister] = useState<TenantRegisterContainer>({
    tenant: tenant,
    owner: owner
  });
  const [countryInfo, setCountryInfo] = useState({
    code: "",
    label: "",
    phone: ""
  });
  const [showPassword, setShowPassword] = useState(false);
  const [validatePass, setValidatePass] = useState("");
  const [showValidatePassword, setShowValidatePassword] = useState(false);
  const [samePass, setSamePass] = useState(false);
  const [openLogin, setOpenLogin] = useState<boolean>(false);

  const changeLoginState = () => {
    setOpenLogin(!openLogin);
  };

  const changeLoadingState = (loadingState:boolean) => {
    setLoading(loadingState);
  };

  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };

  const handleClickShowValidatePassword = () => setShowValidatePassword((show) => !show);
  const handleMouseDownValidatePassword = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };

  const validateSamePass = (name:string, value:string) => {
    if(name === "validatePass"){
      setValidatePass(value);
      if(value === owner.password){
        setSamePass(true);
      }
      else{
        setSamePass(false);
      }
    }
    else if (name === "password"){
      if(value === validatePass){
        setSamePass(true);
      }
      else{
        setSamePass(false);
      }
    }
  }

  const getCountry = (countrySelected:{
    code: string,
    label: string,
    phone: string
  }) => {
    setCountryInfo(countrySelected);
    setTenant({
      ...tenant,
      country: countrySelected && countrySelected.label
    })
  }

  const handlePassValidationChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const name = event.target.name;
    const value = event.target.value;

    if(name === "validatePass" || name === "password"){
      validateSamePass(name, value);
    }
  };

  const handleTenantChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTenant({
      ...tenant,
      [event.target.name]: event.target.value
    });

    setTenantRegister({
      tenant: {
        ...tenant,
        [event.target.name]: event.target.value
      },
      owner: {
        ...owner
      }
    });
  };
  const handleOwnerChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setOwner({
      ...owner,
      [event.target.name]: event.target.value
    });

    setTenantRegister({
      tenant: {
        ...tenant
      },
      owner: {
        ...owner,
        [event.target.name]: event.target.value
      }
    });
  };

  const handleSubmit = async (form:React.FormEvent<HTMLFormElement>) => {
    form.preventDefault();
    setLoading(true);

    if(samePass){
      try {
        const registerResponse:ApiRequestResponse = await sendPost(`${apiRoutes.tenantService.root}${apiRoutes.tenantService.tenant.root}`, tenantRegister);
    
        if (registerResponse && registerResponse.success){
          setLoading(false);
          Swal.fire({
            title: 'Registro exitoso',
            text: `La solicitud de registro se radicó exitosamente.`,
            icon: 'success',
          })
          .then(() => {
            setValidatePass("");
            setCountryInfo({
              code: "",
              label: "",
              phone: ""
            });
            setTenant({
              companyName: "",
              companyId: "",
              country: "",
              isolated: false,
              plan: 'FREE'
            });
            setOwner({
              name: "",
              lastname: "",
              dni: "",
              email: "",
              password: ""
            });
            setTenantRegister({
              tenant: null,
              owner: null
            });
          })
        }
        else{
          setLoading(false);
          Swal.fire({
            title: 'No se pudo solicitar el registro',
            text: ``,
            icon: 'error',
          })
        }
      } 
      catch (error) {
        setLoading(false);
          Swal.fire({
            title: 'Error de comunicación con el servidor',
            text: ``,
            icon: 'error',
          })
      }
    }
    else{
      setLoading(false);
    }
  };

  return (
    <main className='container-fluid p-0'>
      <RootHeader changeLoginState={changeLoginState}/>

      <h2 style={{textAlign:"center", margin:15}}>Tenant registration</h2>
      <form className='row m-4' onSubmit={handleSubmit}>
        <div className='my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12'>
          <TextField
            name='companyName'
            id="tenant-companyName"
            label="Company name"
            type='text'
            fullWidth
            required
            value={tenant.companyName}
            onChange={handleTenantChange}
            variant="outlined"
          />
        </div>
        <div className='my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12'>
          <TextField
            name='companyId'
            id="tenant-companyId"
            label="NIT"
            type='number'
            fullWidth
            required
            value={tenant.companyId}
            onChange={handleTenantChange}
            variant="outlined"
          />
        </div>
        <div className="my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12">
          <CountrySelect onChange={getCountry} required={true} margin={"0px 0px 0px 0px"}/>
        </div>

        <div className='my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12'>
          <TextField
            name='name'
            id="owner-name"
            label="Names"
            type='text'
            fullWidth
            required
            value={owner.name}
            onChange={handleOwnerChange}
            variant="outlined"
          />
        </div>
        <div className='my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12'>
          <TextField
            name='lastname'
            id="owner-lastname"
            label="Last names"
            type='text'
            fullWidth
            value={owner.lastname}
            onChange={handleOwnerChange}
            variant="outlined"
          />
        </div>
        <div className='my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12'>
          <TextField
            name='dni'
            id="owner-dni"
            label="Document number Identification"
            type='number'
            fullWidth
            required
            value={owner.dni}
            onChange={handleOwnerChange}
            variant="outlined"
          />
        </div>

        <div className='my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12'>
          <TextField
            name='email'
            id="owner-email"
            label="Email"
            type='email'
            fullWidth
            required
            value={owner.email}
            onChange={handleOwnerChange}
            variant="outlined"
          />
        </div>
        <div className='my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12'>
          <FormControl variant="outlined" fullWidth required>
            <InputLabel htmlFor="password">Password</InputLabel>
            <OutlinedInput
              required
              name='password'
              id="password"
              label="Password"
              type={showPassword ? 'text' : 'password'}
              value={owner.password}
              onChange={handleOwnerChange}
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    aria-label="toggle password visibility"
                    onClick={handleClickShowPassword}
                    onMouseDown={handleMouseDownPassword}
                    edge="end"
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              }
            />
          </FormControl>
        </div>
        <div className='my-3 col-xxl-4 col-xl-4 col-lg-4 col-md-6 col-sm-6 col-xs-12'>
          <FormControl variant="outlined" fullWidth required error={!samePass && validatePass.length > 0}>
            <InputLabel htmlFor="validate-password">Validate password</InputLabel>
            <OutlinedInput
              required
              name='validatePass'
              id="validate-password"
              label="Validate password"
              type={showValidatePassword ? 'text' : 'password'}
              value={validatePass}
              onChange={handlePassValidationChange}
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    aria-label="toggle password visibility"
                    onClick={handleClickShowValidatePassword}
                    onMouseDown={handleMouseDownValidatePassword}
                    edge="end"
                  >
                    {showValidatePassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              }
            />
          </FormControl>
        </div>

        <div className="row my-3">
          <div className="col d-flex justify-content-center align-items-center">
            <button type='submit' className='btn btn-outline-info'>
              Register
              <SendRounded/>
            </button>
          </div>
        </div>
      </form>

      <Login open={openLogin} closeModal={changeLoginState} changeLoadingState={changeLoadingState}/>
      <Backdrop
        sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}
        open={loading}
      >
        <CircularProgress color="inherit" />
      </Backdrop>
    </main>
  )
};

export default RegisterTenant