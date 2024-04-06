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

export type UserRoles = 
  'TENANT_OWNER' |
  'USERS_ADMIN' |
  'CUSTOMER' |
  'COMMERCIAL_ADVISOR' |
  'REGIONAL_DIRECTOR'
;

export interface Rol{
  id?:number;
  user?:User;
  rol: UserRoles;
  tenantId:string;
};

export type RegistrationPlans = 'FREE' | 'A' | 'B' | 'C';

export interface Tenant{
  id?:number;
  companyName:string;
  companyId:string; // Es el mismo NIT en colombia
  country:string;
  isolated:boolean;
  registrationDate?:Date;
  plan: RegistrationPlans;
  users?:User[];
  tenantPort?:number;
};

export interface TenantRegisterContainer{
  tenant: Tenant;
  owner: User;
};