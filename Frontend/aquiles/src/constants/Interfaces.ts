export interface ApiRequestResponse{
  model?: any;
  success: boolean;
  httpStatus: number;
  message?: string;
};

export interface UserAuthenticated{
  userId:number;
  companyId:string;
  jwt:string;
};

export interface UserLogin{
  email: string;
  password: string;
};

export interface User{
  id?:number;
  name:string;
  lastname?:string;
  dni:string;
  email:string;
  password:string;
  isActive?:boolean;
  phone?:string;
  regionalId?:number;
  roles?:Rol[];
};

export const UserRoles = {
  TENANT_OWNER: 'TENANT_OWNER' ,
  USERS_ADMIN: 'USERS_ADMIN',
  CUSTOMER: 'CUSTOMER',
  COMMERCIAL_ADVISOR: 'COMMERCIAL_ADVISOR',
  REGIONAL_DIRECTOR: 'REGIONAL_DIRECTOR'
};

export interface Rol{
  id?:number;
  user?:User;
  rol:string;
  tenantId:string;
};

export const RegistrationPlans = {
  FREE: 'FREE',
  A: 'A',
  B: 'B',
  C: 'C'
};

export interface Tenant{
  id?:number;
  companyName:string;
  companyId:string; // Es el mismo NIT en colombia
  country:string;
  isolated:boolean;
  registrationDate?:Date;
  plan:string;
  users?:User[];
  tenantPort?:number;
};

export interface TenantRegisterContainer{
  tenant: Tenant;
  owner: User;
};