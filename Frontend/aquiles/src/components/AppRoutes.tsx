import React from 'react';
import { createBrowserRouter, Outlet } from 'react-router-dom';
import RootPage from '../pages/Root';
import RegisterTenant from '../pages/RegisterTenant';
import ErrorPage from '../pages/ErrorPage';
import { applicationPaths } from '../constants/routes';
import Dashboard from '../pages/Dashboard';
import MyProfile from './dashboard/MyProfile';
import RegisterUser from './dashboard/RegisterUser';
import Statistics from './dashboard/Statistics';
import Infractions from './dashboard/Infractions';
import Areas from './dashboard/Areas';

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
        element: <Dashboard/>,
        children: [
          {
            path: applicationPaths.dashboard.profile,
            element: <MyProfile/>
          },
          {
            path: applicationPaths.dashboard.registerUser,
            element: <RegisterUser/>
          },
          {
            path: applicationPaths.dashboard.statistics,
            element: <Statistics/>
          },
          {
            path: applicationPaths.dashboard.infractions,
            element: <Infractions/>
          },
          {
            path: applicationPaths.dashboard.areas,
            element: <Areas/>
          }
        ]
      }
    ]
  }
]);

export default routes;