import React, { useEffect, useState } from 'react';
import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import { applicationPaths } from '../constants/routes';
import { AppBar, Avatar, Backdrop, Badge, Box, CircularProgress, createTheme, Divider, IconButton, ListItemIcon, Menu, MenuItem, ThemeProvider, Toolbar, Tooltip, Typography } from '@mui/material';
import { ApiRequestResponse, User } from '../constants/Interfaces';
import { storageCompanyIdKeyName, storageTokenKeyName, storageUserIdKeyName } from '../constants/globalConstants';
import { sendGet } from '../services/apiRequests';
import { apiRoutes } from '../constants/Api';
import { Logout, HomeRounded, Menu as MenuIcon, Notifications } from '@mui/icons-material';
import { MaterialUISwitch } from '../components/custom-components/CustomSwitch';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#1976d2',
    },
  },
});
const lightTheme = createTheme({
  palette: {
    mode:'light',
    primary: {
      main: '#1976d2',
    },
  },
});

const Dashboard = () => {
  const navigate = useNavigate();
  const [waiting, setWaiting] = useState(true);
  const [userInfo, setUserInfo] = useState<User>(null);
  const commercialNav = "Cotizaciones";
  const clientsNav = "Clientes";
  const projectsNav = "Proyectos";
  const pages = [
    {
      name: clientsNav,
      path: `${applicationPaths.dashboard.areas}`,
      items: [
        {
          name: "Ver",
          path: ``
        },
      ]
    }
  ];
  const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null);
  const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);
  const [activeSubMenu, setActiveSubMenu] = useState(null);
  const [anchorElNotifications, setAnchorElNotifications] = useState(null);
  const openNotifications = Boolean(anchorElNotifications);
  const [notifications, setNotifications] = useState<string[]>([]);
  const [pageDarkTheme, setPageDarkTheme] = useState<boolean>(true);


  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const handleOpenNotifications = (event:React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    setAnchorElNotifications(event.currentTarget);
  };
  const handleCloseNotifications = () => {
    setAnchorElNotifications(null);
  };

  const goToProfile = () => {
    setAnchorElUser(null);
    navigate(`/${applicationPaths.dashboard.root}/${applicationPaths.dashboard.profile}`);
  }

  const handleCloseSession = () => {
    setAnchorElUser(null);
    localStorage.clear();
    navigate(`../`);
  }

  const changeTheme = () => {
    setPageDarkTheme(!pageDarkTheme);
  }

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

  useEffect(() => {
    if(window.location.pathname === `/${applicationPaths.dashboard.root}`){
      if(localStorage.length > 0){
        getUserInfo();
      }
      else{
        setWaiting(false);
        Swal.fire({
          title: 'Expiró la sesión',
          text: `La sesión expiró, debe volver a iniciar sesión`,
          icon: 'info',
          confirmButtonText: "Iniciar sesión"
        })
        .then(option => {
          if(option.isConfirmed){
            Swal.close();
            navigate(`../`);
          }
          else
            setTimeout(() => {
              Swal.close();
              navigate(`../`);
            }, 5000);
        })
      }
    }
  }, [window.location.pathname])

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
    <main>
      Dashboard de {userInfo.name}
      <ThemeProvider theme={pageDarkTheme ? darkTheme : lightTheme}>
        <AppBar position="static" color='primary'>
          <Toolbar style={{minHeight:"50px !important"}}>
            <NavLink to={""}>
              <IconButton sx={{ display: { xs: 'none', md: 'flex' }, m:0, p:0, width:50, height:50 }} >
                <HomeRounded sx={{width:"100%", height:"100%"}} />
              </IconButton>
            </NavLink>
            <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, justifyContent:'center', alignItems: "center", height:50, gap: "5px" }}>
              {pages.map((page:{name:string, path:string, items:any[]}, index:number) => (
                <div key={index}
                  onMouseEnter={() => setActiveSubMenu(index)} 
                  onMouseLeave={() => setActiveSubMenu(null)}
                  style={{height:"100%", zIndex:1}}
                >
                  <NavLink  to={page.path} className="nav-bar-option"
                    style={({ isActive, isPending, isTransitioning }) => {
                      return {
                        textDecoration: "none",
                        display: "flex",
                        borderBottom: isActive ? "2px solid white" : "",
                        color: isPending ? "red" : "white",
                        viewTransitionName: isTransitioning ? "slide" : "",
                      };
                    }} >
                    {page.name}
                  </NavLink>
                  {activeSubMenu === index && page.items && page.items.length > 0 && (
                    <ul style={{position:"absolute", margin:0, padding:6, backgroundColor:"#272727", listStyle:"none", borderRadius:5}}>
                      {page.items.map((item, itemIndex) => (
                        <li key={itemIndex} style={{height:30}}>
                          <NavLink to={item.path} className="menuoption-subitem"
                            style={({ isActive, isPending, isTransitioning }) => {
                              return {
                                textDecoration: "none",
                                display: "flex",
                                color: isPending ? "red" : "white",
                                viewTransitionName: isTransitioning ? "slide" : "",
                              };
                            }}
                          >
                            {item.name}
                          </NavLink>
                        </li>
                      ))}
                    </ul>
                  )}
                </div>
              ))}
            </Box>
  
            <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
              <IconButton
                size="large"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleOpenNavMenu}
                color="inherit"
              >
                <MenuIcon />
              </IconButton>
              <Menu
                id="menu-appbar"
                anchorEl={anchorElNav}
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'left',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'left',
                }}
                open={Boolean(anchorElNav)}
                onClose={handleCloseNavMenu}
                sx={{
                  display: { xs: 'block', md: 'none' },
                }}
              >
                {pages.map((page:{name:string, path:string, items:any[]}, index:number) => (
                  <Link key={index} to={page.path} style={{textDecoration: "none", color:"white"}}>
                    <MenuItem onClick={handleCloseNavMenu}>
                      <Typography textAlign="center">{page.name}</Typography>
                    </MenuItem>
                  </Link>
                ))}
              </Menu>
            </Box>
            
            <HomeRounded sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
            <Typography
              variant="h5"
              noWrap
              component="h5"
              sx={{
                mr: 2,
                display: { xs: 'flex', md: 'none' },
                flexGrow: 1,
                fontFamily: 'monospace',
                fontWeight: 700,
                letterSpacing: '.3rem',
                color: 'inherit',
                textDecoration: 'none',
              }}
            >
              <Link to={""} style={{textDecoration: "none", color:"white"}}>HOME</Link>
            </Typography>
            
            {/* ================> Notifications section <================= */}
            <Box sx={{ display: { xs: 'flex', md: 'flex' }, paddingRight:2 }}>
              <Tooltip
                title={"Ver notificaciones"} 
                placement='bottom'
                arrow>
                <IconButton
                  size="large"
                  aria-label="show notifications"
                  color="inherit"
                  onClick={handleOpenNotifications}
                >
                  <Badge badgeContent={notifications.length} color="error" max={99}>
                    <Notifications />
                  </Badge>
                </IconButton>
              </Tooltip>
            </Box>
            <Menu
              id="notificationsMenu"
              MenuListProps={{
                'aria-labelledby': 'long-button',
              }}
              anchorEl={anchorElNotifications}
              open={openNotifications}
              onClose={handleCloseNotifications}
              PaperProps={{
                style: {
                  maxHeight: "60vh",
                  minWidth: 200,
                  maxWidth: "40vw",
                },
              }}
            >
              {notifications && notifications.length > 0 ? 
                notifications.map((notification, index:number) => (
                  <Tooltip
                    key={index}
                    title={<p style={{fontSize:"0.9rem"}}>{notification}</p>} 
                    placement='top-start'
                    arrow>
                    <MenuItem /* selected={notification === 'None'} */ onClick={handleCloseNotifications}>
                      {notification}
                    </MenuItem>
                  </Tooltip>
                ))
                :
                <p style={{margin:0, textAlign:"center"}}>Todo está al dia</p>
              }
            </Menu>
            {/* ========================================================= */}

            {/* ===============> Swith theme section <============================================== */}
            <Box sx={{ display: { xs: 'flex', md: 'flex' }, paddingRight:2}} >
              <MaterialUISwitch onChange={changeTheme} theme={pageDarkTheme ? darkTheme : lightTheme}/>
            </Box>
            {/* ==================================================================================== */}

            {/* =============================================> My Account section <============================================================= */}
            <Box sx={{ flexGrow: 0 }}>
              <Tooltip title="Mi cuenta">
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                  <Avatar children={userInfo.name && userInfo.name.length > 0 && userInfo.name.includes(" ") ? `${userInfo.name.split(' ')[0][0]}${userInfo.name.split(' ')[1][0]}`: userInfo.name && userInfo.name.length > 0 ? userInfo.name[0] : "NA"} alt={userInfo.name ? userInfo.name:""} src="/static/images/avatar.jpg" />
                </IconButton>
              </Tooltip>
              <Menu
                anchorEl={anchorElUser}
                id="menu-user"
                open={Boolean(anchorElUser)}
                onClose={handleCloseUserMenu}
                PaperProps={{
                  elevation: 0,
                  sx: {
                    overflow: 'visible',
                    filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
                    mt: 1.5,
                    '& .MuiAvatar-root': {
                      width: 32,
                      height: 32,
                      ml: -0.5,
                      mr: 1,
                    },
                    '&::before': {
                      content: '""',
                      display: 'block',
                      position: 'absolute',
                      top: 0,
                      right: 14,
                      width: 10,
                      height: 10,
                      bgcolor: 'background.paper',
                      transform: 'translateY(-50%) rotate(45deg)',
                      zIndex: 0,
                    },
                  },
                }}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
                keepMounted
              >
                <MenuItem key="user-profile" onClick={goToProfile}>
                  <Avatar /><Typography textAlign="center">Mi perfil</Typography>
                </MenuItem>
                <Divider/>
                <MenuItem key="close-session" onClick={handleCloseSession}>
                  <ListItemIcon>
                    <Logout fontSize="small" />
                  </ListItemIcon>
                  Cerrar sesión
                </MenuItem>
              </Menu>
            </Box>
            {/* =============================================================================================================================== */}
          </Toolbar>
        </AppBar>
      </ThemeProvider>
      <Outlet/>
    </main>
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