export const apiRoutes = {
  tenantService: {
    root: "http://localhost:1095/api/v1/aquiles/tenant-service",
    services: {
      root: "/services",
      params: "/params"
    },
    tenant: {
      root: "/tenant",
      byCompanyId: "/company"
    },
    tenant_user: {
      root: "/user",
      params: "/params"
    }
  },
  userService:{
    root: "http://localhost:1096/api/v1/aquiles/user-service",
    user: {
      root: "/user",
      params: "/params",
      register: "/register"
    },
    authenticate: {
      root: "/auth"
    },
    rol: {
      root: "/rol"
    }
  }
};