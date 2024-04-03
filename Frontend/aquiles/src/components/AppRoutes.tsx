import React from 'react';
import { createBrowserRouter, Outlet } from 'react-router-dom';
import RootPage from '../pages/Root';
import Login from '../pages/Login';
import RegisterTenant from '../pages/RegisterTenant';
import ErrorPage from '../pages/ErrorPage';
import { applicationPaths } from '../constants/routes';

const routes = createBrowserRouter([
  {
    element: <Outlet/>,
    errorElement: <ErrorPage/>,
    caseSensitive: true,
    children: [
      {
        path: "",
        element: <RootPage/>
      },
      {
        path: applicationPaths.login,
        element: <Login/>
      },
      {
        path: applicationPaths.registerTenant,
        element: <RegisterTenant/>
      }
    ]
  }
]);

export default routes;