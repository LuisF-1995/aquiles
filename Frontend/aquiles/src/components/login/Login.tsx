import React, { useState } from 'react';
import { Fade, Modal, TextField } from '@mui/material';
import './styles.css';
import { UserLogin } from '../../constants/Interfaces';
import { NavLink } from 'react-router-dom';
import { applicationPaths } from '../../constants/routes';

const Login = (props:{open:boolean; closeModal:any}) => {
  const [user, setUser] = useState<UserLogin>({
    email: "",
    password: ""
  });

  const handleClose = () => {
    props.closeModal(false);
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUser({
      ...user,
      [event.target.name]: event.target.value
    });
  };

  const handleLogin = async (form:React.FormEvent<HTMLFormElement>) => {
    form.preventDefault();
  };

  return (
    props.open &&
    <Modal
        aria-labelledby="modal-title"
        aria-describedby="modal-form"
        className="container-lg d-flex justify-content-center align-items-center"
        open={props.open}
        onClose={handleClose}
        closeAfterTransition
        BackdropProps={{
          timeout: 500,
        }}
      >
        <Fade in={props.open}>
          <section className='paper'>
            <form onSubmit={handleLogin} className='container-fluid login-form'>
              <TextField
                name='email'
                id="login-username"
                label="Email"
                type='email'
                required
                className='row mt-4 mb-1'
                value={user.email}
                onChange={handleChange}
                variant="outlined"
              />
              <TextField
                name='password'
                id="login-password"
                label="Password"
                type='password'
                required
                className='row mt-2 mb-1'
                value={user.password}
                onChange={handleChange}
                variant="outlined"
              />
              <div className='row mt-2 w-100'>
                <button type="submit" className='btn btn-outline-danger'>Log in</button>
              </div>
              <div className='row my-4 w-100'>
                <p className='col-8 m-0 p-0'>¿Not registered yet?</p>
                <NavLink 
                  to={`/${applicationPaths.registerTenant}`}
                  className='col-4'
                  style={({ isActive, isPending, isTransitioning }) => {
                    return {
                      display: isActive && 'none'
                    };
                  }} 
                >
                  Register
                </NavLink>
              </div>
            </form>
          </section>
        </Fade>
      </Modal>
  )
}

export default Login