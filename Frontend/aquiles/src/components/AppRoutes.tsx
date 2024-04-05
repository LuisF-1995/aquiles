import React from 'react';
import { createBrowserRouter, Outlet } from 'react-router-dom';
import RootPage from '../pages/Root';
import RegisterTenant from '../pages/RegisterTenant';
import ErrorPage from '../pages/ErrorPage';
import { applicationPaths } from '../constants/routes';
import Dashboard from '../pages/Dashboard';

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
        path: applicationPaths.registerTenant,
        element: <RegisterTenant/>
      },
      {
        path: applicationPaths.dashboard.root,
        element: <Dashboard/>
      }
    ]
  }
]);

export default routes;