// 统一响应结果接口
export interface ResultModel {
  code: number // 状态码
  msg: string // 提示信息
  data: any // 响应数据
}

// 分页结果接口
export interface PageModel {
  total: number // 总记录数
  rows: any[] // 数据列表
}

// 统一响应结果接口
export interface PageResultModel {
  code: number // 状态码
  msg: string // 提示信息
  data: any // 响应数据
}

// 分页参数接口
export interface PaginationParams {
  currentPage: number // 响应数据
  pageSize: number // 数据列表
  total: number // 总记录数
}

// -----------------------------部门数据相关接口及类型-----------------------------
// 部门数据接口
export interface DeptModel {
  id?: number // 部门ID
  name: string // 部门名称
  updateTime?: string // 最后操作时间
}

// 部门数据数组
export type DeptModelArray = DeptModel[]

// -----------------------------员工数据相关接口及类型-----------------------------
// 搜索员工数据接口
export interface SearchEmpModel {
  name: string // 员工名称
  gender: string // 性别
  begin: string // 入职开始时间
  end: string // 入职结束时间
  date: string[] // 时间范围
}

// 员工工作经历接口
export interface EmpExprModel {
  id?: number //ID
  empId?: number //员工id
  exprDate: string[] //时间范围
  beginDate: string //开始时间
  endDate: string //结束时间
  company: string //公司
  job: string //职位
}

//员工数据接口
export interface EmpModel {
  id?: number
  username: string
  password: string
  name: string
  gender: string
  phone: string
  job: string
  salary: string
  image: string
  entryDate: string
  deptId: string
  deptName?: string
  exprList: EmpExprModel[]
}

// 员工数据数组
export type EmpModelArray = EmpModel[]

//--------------------------登录------------------------------
// 登录表单信息
export interface LoginEmp {
  username?: string
  password?: string
}

// 登录成功返回信息
export interface LoginInfo {
  id?: number
  name?: string
  username?: string
  token?: string
  role?: 'ADMIN' | 'TEACHER'
}
// ------------------------班级相关接口--------------------------
// 查询班级接口
export interface SearchClazzModel {
  name: string
  begin: string
  end: string
  date: string[]
}

// 班级数据接口
export interface ClazzModel {
  id?: number //ID
  name: string //班级名称
  room: string //班级教室
  subject: number //学科
  beginDate: string //开课时间
  endDate: string //结课时间
  masterId?: number //班主任(员工ID)
  masterName?: string //班主任姓名(员工姓名)
  createTime: string //创建时间
  updateTime: string //更新时间
  status: string //状态 （未开班、已开班、已结课）
}

// 班级数据数组
export type ClazzListModel = ClazzModel[]

// ---------------------------学生相关接口-------------------
// 学生查询接口
export interface SearchStuModel {
  name: string //学生姓名
  degree: number | null //学历
  clazzId: number | null //班级id
}

// 学生数据接口
export interface StuModel {
  id?: number //id
  name: string //姓名
  no: string //学号
  gender: number //性别
  phone: string //手机号
  degree: number //学历
  idCard: string //身份证号
  isCollege: number //是否院校毕业
  address: number //联系地址
  graduationDate: string //毕业时间
  violationCount: number //违纪次数
  violationScore: number //违纪扣分
  clazzId: number //班级ID
  clazzName: string //班级名称
  createTime: string //创建时间
  updateTime: string //更新时间
}

// 学生违纪数据
export interface StuViolationApi {
  id: number
  score: number
}

// 学生数据数组
export type StuModelList = StuModel[]

// -----------------------日志----------------------
export interface LogModel {
  id: number //ID
  operateEmpId: number //操作人id
  operateTime: string //操作时间
  className: string //类名
  methodName: string //方法名
  methodParams: string //操作方法参数
  returnValue: string //返回值
  costTime: string //执行耗时,单位ms
  operateEmpName: string //操作人名称
}

// 日志数组
export type LogListModel = LogModel[]
