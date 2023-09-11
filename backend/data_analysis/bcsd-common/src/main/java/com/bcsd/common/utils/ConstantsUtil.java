package com.bcsd.common.utils;

public class ConstantsUtil {

    public static final int MAX_INT = Integer.MAX_VALUE;

    public static final String AES = "shdes82@dodswch2";

    /**
     * 服务名
     */
    public static final String Server_Name_AUTH = "com-jgdt-auth";
    public static final String Server_Name_BPM = "com-jgdt-bpm";
    public static final String SERVER_NAME_CONFIG = "com-jgdt";

    /*客户权限系统*/
    public static final String SERVER_NAME_CUSTOMER = "com-jgdt-sungrow-customer";
    /*市场、售后系统*/
    public static final String SERVER_NAME_MARKET_AFTERSALE = "com-jgdt-sungrow-market-aftersale";
    /*合同、财务系统*/
    public static final String SERVER_NAME_FINANCE_CONTRACTS = "com-jgdt-sungrow-finance-contracts";
    /*订单、产品系统*/
    public static final String SERVER_NAME_ORDER_PRODUCT = "com-jgdt-sungrow-order-product";
    /*工程、供应链系统*/
    public static final String SERVER_NAME_SUPPLY_PROJECT = "com-jgdt-sungrow-supply-project";
    /*条码系统*/
    public static final String SERVER_NAME_BARCODE = "com-jgdt-sungrow-barcode";

    /**
     * 服务之间默认的权限令牌
     */
    public static final String BACK_TOKEN = "3C38D033466621CAC31B7113B38FCFE9";
    /**
     * spring boot扫描包名
     */
    public static final String JPA_REPOSITORIES_PACKAGE = "com.jgdt.**.repositories";
    public static final String COMPONENT_PACKAGE = "com.jgdt.**";
    public static final String ENTITY_PACKAGE = "com.jgdt.**.po";
    public static final String MAPPER_PACKAGE = "com.jgdt.financecontracts.mapper";
    /**
     * redis key前缀
     */
    public static final String Base_Service_Cache_Value = "com:jgdt:1.0";
    public static final String TOKEN_NAME = "authentication";
    public static final String BACK_TOKEN_NAME = "clientToken";
    public static final String LOGIN_CAPTCHA = "login.captcha";
    public static final String LOGIN_STATE = "login.state";
    public static final String AUTHORIZE_CODE = "authorize.code";
    public static final String CLIENT_ID = "client.id";
    public static final String ACCESS_TOKEN = "access.token";
    public static final String REFRESH_TOKEN = "refresh.token";
    public static final String GRANT_TYPE = "grant.type";
    public static final String CLIENT_SECRET = "client.secret";
    /**
     * 当前minio文件总大小
     */
    public static final String FILE_SIZE = "com.jgdt.file.size";
    /**
     * 第三方服务调用内部接口
     */
    public static final String CLIENT_KEY = "clientKey";
    public static final String API_KEY = "apiKey";
    public static final String API_SECRET = "apiSecret";
    public static final String THIRD_PART_CLIENT_KEY = "3C38D033466621CAC31B7113B38FCFE9";
    public static final String THIRD_PART_CLIENT_SECRET = "58b65297-3467-0859-8337-8cbaf81ef68a";
    public static final String TARGETNAMESPACE = "http://www.jgdt.com/";
    public static final String DOC_NUMBER = "docNumber";
    public static final String HEADERIP = "x-real-ip";
    public static final String ID = "ID_";
    public static final String DRUID_PASS = "P@ssw0rd";
    public static final String AUTHENTICATION = "authentication";
    public static final String AUTHORIZATION_CODE = "authorization_code";

    /*创建用户默认密码*/
    public static final String DEFAULT_PASSWORD = "sun123456";

    public static final String CUSTOMERFILENAME = "客户信息.xls";

    public static final String ORDER_BT_MATERIAL = "订单物料信息.xls";

    public static final String ORDER_BT_LIST = "BT订单信息.xls";

    public static final String SUPPLIERFILENAME = "供应商信息.xls";

    public static final String ENDUSERFILENAME = "终端用户信息.xls";

    public static final String CREDIT_CONTRACT_FILE_NAME = "赊销合同.xls";

    public static final String PROJECT_DATA_FILENAME = "项目资料.xls";

    public static final String USER_TYPE = "userType";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String NAME1 = "name";
    public static final String ROLE_NAME = "roleName";
    public static final String ACCOUNT = "account";
    public static final String IS_SUPER = "isSuper";
    public static final String GROUPID = "groupId";
    public static final String ROLEID = "roleId";
    public static final String STATUS = "status"; //员工状态

    public static final String ROLEALIAS = "roleAlias";
    public static final String TENANTID = "tenantId";
    public static final String ORGALIAS = "orgAlias"; //省区编码
    public static final String TENANTNAME = "tenantName";
    public static final String DEPARTMENT_NAME = "departmentName";

    public static final String DATA = "data";

    /**
     * 服务管理
     */
    public static final String FIELD_HEADERS = "headers";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TESTVALUE = "testValue";
    public static final String FIELD_BODYDATA = "bodyData";
    public static final String FIELD_QUERYS = "querys";
    public static final String FIELD_CHILDREN = "children";
    public static final String FIELD_BODYTYPE = "bodyType";
    public static final String FORM = "form";
    public static final String DATATYPE = "dataType";
    public static final String LINE = "line";
    public static final String TYPE = "type";
    public static final String DATA1 = "data";
    public static final String PK = "pk";
    public static final String ARRAY = "array";

    /**
     * 根据表名和查询条件查询数据
     */
    /*查询条件中的表名*/
    public static final String TNAME = "tableName";
    public static final String USERID = "userId";
    public static final String PUSH_MESSAGE_TO_CLIENT = "channel:userRemindFromServer";


    public static final String ID_ = "id";

    public static final String CUR_VALUE = "curValue";

    /**
     * 邮件模板相关
     */
    public static final String LOGOUT_CHANNEL = "channel:userLogout";
    public static final String ADMIN = "admin";
    /**
     * 业务对象、表单
     */
    public static final String REDIS_LOCK = "redisLock:";

    /**
     * e签宝回调地址
     */
    public static final String E_CALL_BACK = "/contract/esign/notify/receive";
    /**
     * 外部系统调用家庭光伏平台
     */
    public static final String EXTERNAL = "/external";
    /*e签宝合同回调*/
    public static final String E_EXTERNAL = "/external/finance_contracts/esign/notify/receive";

    /*e签宝接货单回调*/
    public static final String E_RECEIPEXTERNAL = "/external/supply_project/esign/notify/receive";

    /**
     * j2cache 缓存 region
     */
    public static final String J2CACHE_FINDBYUSERID = "j2cache:findByUserId";
    public static final String J2CACHE_FINDORGALLSUPER = "j2cache:findOrgAllSuper";
    public static final String J2CACHE_LOGINUSERINFO = "j2cache:loginUserInfo";


    public static final String CONTRACT_LOCK = "contract_lock";

    /*合同模块start*/
    public static final String CUSTOMER_TRANSFER_LOCK = "customer_transfer_lock";
    public static final String CUSTOMER_REFUND_LOCK = "customer_refund_lock";
    public static final String CUSTOMER_RECEIPT_LOCK = "customer_receipt_lock";
    public static final String CUSTOMER_ACCOUNT_LOCK = "customer_account_lock";
    public static final String CUSTOMER_ENDUSER_ADD_LOCK = "customer_enduser_add_lock_";
    public static final String BANK_TRANSACTION_DETAIL_LOCK = "bank_transaction_detail_lock";
    public static final String CONTRACT_JOB_LOCK = "contract_job_lock";
    public static final String LABOR_SETTLEMENT_JOB_LOCK = "laborSettlementJobLock";
    public static final String DOWNLOAD_CONTRACT_PDF_JOB_LOCK = "download_contract_pdf_job_lock";
    public static final String UPDATE_SALES_ORDER_STATUS_LOCK = "update_sales_order_status_lock";
    public static final String VALUE_Y = "Y";
    public static final String VALUE_N = "N";
    public static final String VALUE_C = "C";

    public static final Integer SUC_CODE = 1;
    public static final String CUSTOMER_ACCOUNT_LOCK_ID = "customer_account_lock_id_";
    public static final String CUSTOMER_RECEIPT_LOCK_ID = "customer_receipt_lock_id_";
    public static final String CUSTOMER_TRANSFER_LOCK_ID = "customer_transfer_lock_id_";
    public static final String SALES_ORDER_LOCK_ID = "sales_order_lock_id_";
    public static final String OUTSTANDING_LOCK = "outstanding_lock";//库存锁
    public static final String CHECK_ORDERANDCONTRACT_CONSISTENCY_LOCK = "check_orderandcontract_consistency_lock";//校验合同一致性锁
    public static final String GET_POWER_STATION_STATE_INFO_LOCK = "get_power_station_state_info_lock";//获取国网状态加锁
    public static final String LOCK_SYS_POWER_STATION_SUNCLOUD_STATUS = "lock_sys_power_station_suncloud_status";//获取阳光云状态加锁
    public static final String LOCK_GEN_PROJECT_ORDER_BT_ACCEPTANCE_PDF = "lock_gen_project_order_bt_acceptance_pdf";//获取阳光云状态加锁
    public static final String CONTRACT_FLOW_FINISH_LOCK_ID = "contract_flow_finish_lock_id_";
    public static final String LOCK_GEN_MAJOR_ACCOUNT_INDEX_REPORT = "lock_gen_major_account_index_report";//
    public static final String LOCK_YEU_XIU_EXAMINE_JOB = "lock_yeu_xiu_examine_job";//
    public static final String LOCK_YEU_XIU_NONGHU_EXAMINE_JOB = "lock_yeu_xiu_nonghu_examine_job";//
    public static final String LOCK_HX_NOTIFY = "lock_hx_notify_";//
    public static final String LOCK_INVOICE = "lock_invoice_";//
    public static final String LOCK_HANDLE_USERINCOMEPOWERSTATION_ERRORDATA_JOB = "lock_handle_userincomepowerstation_errordata_job";//
    public static final String LOCK_SYS_SUNCLOUDTODAYENERGY_JOB = "lock_sys_suncloudtodayenergy_job";//
    public static final String SAVE_USER_INCOME_PAYMENT_BILL_LOCK = "save_user_income_payment_bill_lock";
    public static final String LOCK_BATCH_REPLACE_MATERIAL = "lock_batch_replace_material";//
    public static final String LOCK_BATCH_SAVE_PROJECTORDERBT_INVOICE = "lock_batch_save_projectorderbt_invoice";//

    public static final String LOCK_SEND_PROJECTORDERBTINVOICE_TO_ZHEYINJOB = "lock_send_projectorderbtinvoice_to_zheyinjob";//



    /*合同模块end*/

    public static final String CUSTOMER_ADMIN = "customerAdmin";

    public static final String PERSONAL = "personal";

    public static final String COMPANY = "company";

    public static final String SAP_RESULT_SUCCESS = "S";

    public static final String SAP_RESULT_ERROR = "E";

    /**
     * 对接阳光云平台接口(userName/passWord/appkey/url)
     */
    public static final String USER_ACCOUNT = "jtgfshadmin";

    public static final String USER_PASSWORD = "jtgfsh123456";

    public static final String APPKEY = "C779311B93430DDD2A8124FE4A9586A2";
    /*
    1：表示系统用户名密码登录(默认方式)；
     */
    public static final String LOGIN_TYPE = "1";

    public static final String X_ACCESS_KEY = "fnsxg06uimwqye3h8dypp4363fin9fhx";
    /*
    系统编码：       第三方调用：901
     */
    public static final String SYS_CODE = "901";

    /*
    导出excel名和sheet名
     */
    public static final String STANDARD_SCHEME_EXPORT_EXCEL_NAME = "标准方案-物料信息导出报表.xlsx";
    public static final String STANDARD_SCHEME_EXPORT_SHEET_NAME = "标准方案物料信息报表";
    public static final String COMBINATION_SCHEME_EXPORT_EXCEL_NAME = "组合方案-物料信息导出报表.xlsx";
    public static final String COMBINATION_SCHEME_EXPORT_SHEET_NAME = "组合方案物料信息报表";
    public static final String ELECTRIC_CONFIGURATION_EXPORT_EXCEL_NAME = "电气配置-物料信息导出报表.xlsx";
    public static final String ELECTRIC_CONFIGURATION_EXPORT_SHEET_NAME = "电气配置物料信息报表";
    public static final String REBATE_APPLICATION_EXPORT_EXCEL_NAME = "返利申请导出报表.xlsx";
    public static final String REBATE_APPLICATION_EXPORT_SHEET_NAME = "返利申请";

    /*
    下载导入模板excel名和sheet名
     */
    public static final String STANDARD_SCHEME_EXCEL_NAME = "标准方案导入模板.xlsx";
    public static final String STANDARD_SCHEME_SHEET_NAME = "标准方案物料信息";
    public static final String COMBINATION_SCHEME_EXCEL_NAME = "组合方案导入模板.xlsx";
    public static final String COMBINATION_SCHEME_SHEET_NAME = "组合方案物料信息";
    public static final String ELECTRIC_CONFIGURATION_EXCEL_NAME = "电气配置导入模板.xlsx";
    public static final String ELECTRIC_CONFIGURATION_SHEET_NAME = "电气配置物料信息";
    public static final String BOM_MATERIAL_EXCEL_NAME = "BOM物料导入模板.xlsx";
    public static final String BOM_MATERIAL_SHEET_NAME = "BOM物料导入模板";
    public static final String MATERIAL_PRICE_EXCEL_NAME = "物料价格导入模板.xlsx";
    public static final String MATERIAL_PRICE_SHEET_NAME = "物料价格导入模板";
    public static final String MATERIAL_INFO_EXCEL_NAME = "物料信息导入模板.xlsx";
    public static final String MATERIAL_INFO_SHEET_NAME = "物料信息导入模板";

    /**
     * 消息队列名称
     */
    public static final String EXCHANGE_NAME = "sss";
    public static final String DELAYED_QUEUE = "delayed.queue";
    public static final String SMS_QUEUE = "sms.queue";
    public static final String PUSH_ACCOUNT_QUEUE = "push.account.queue";
    public static final String REGULARLY_REMINDED = "regularly.reminded.queue";
    public static final String UPLOAD_FILE_URL = "upload.file.url";


    /**
     * 默认文件桶名称
     */
    public static final String FILE_BUCKET = "sungrow";

    /**
     * 部门中“家庭光伏BT供应商”别名
     */
    public static final String BT = "BTsurpply";

    /**
     * 部门中“家庭光伏BT供应商”下的供应商别名前缀
     */
    public static final String ALIAS = "Alias";

    /**
     * 渠道专员
     */
    public static final String QUDAO = "qudaozhuanyuan";

    /**
     * 省区负责人
     */
    public static final String SQFZR = "sqfuzeren";

    /**
     * 1级审批节点名称
     */
    public static final String LEVEL1 = "1级审批";
    public static final String LEVEL2 = "2级审批";

    /**
     * 分包商开发人员
     */
    public static final String KAIFA = "Subcontractor_developer";

    public static final String PHONENUMBERS = "phoneNumbers";
    public static final String CODE = "code";
    /*系统名称*/
    public static final String SYSTEMATIC_NAME = "systematicName";

    /*24小时内发送短信限制次数*/
    public static final String SENDLIMITCOUNT = "sendLimitCount";
    /*24小时内发送短信限制次数对应的key*/
    public static final String SENDLIMITREDISKEY = "sendLimitRedisKey";
    /*验证码存储的key*/
    public static final String CODEREDISKEY = "codeRedisKey";
    /*验证码发送间隔等待时间key*/
    public static final String WAITTIMEREDISKEY = "waitTimeRedisKey";
    public static final String ISFORGET = "isForget";

    public static final String TURN_TO = "turnTo";
    public static final String PROCESS_NAME = "processName";
    public static final String APPLICATION_ID = "applicationId";
    public static final String FORM_ID = "formId";
    public static final String DEPARTMENT_ID = "departmentId";
    public static final String BUSINESS_OWNER = "businessOwner";





    public static final String QUERY_STATUS = "querystatus";
    public static final String EPAY_OPEN_ACCOUNT = "openaccount";
    public static final String SCODE_RESEND = "scode";
    public static final String SCODE_VERIFY = "scodeverify";
    public static final String UploadTimeImg = "uploadimg";
    public static final String BINDING = "binding";
    public static final String UNBINDING = "unbinding";
    public static final String WITHDRAW = "withdraw";
    public static final String EPAY_CLOSE_ACCOUNT = "closeaccount";
    public static final String S_CODE_VERIFY = "scodeverify";

    //交付系统
    public static final String DELIVERY = "deliver";
    public static final String YUEXIU_INF_SUC_CODE = "S";
    public static final String YUEXIU_INF_FAIL_CODE = "E";

    public static final String LOCK_ADD_CONTRACT = "lock_add_contract";


    public static final String PLATFORM_TYPE_ANDROID = "android";


    //推送账号到研发设计软件admin默认的密码
    public static final String PUSH_ACCOUNT_PWD = "57d54cc48b705a4dfac9b9d9318e64dd";

    //首页统计相关
    public static final String BT_ORDER_STATISTICS = "bt_order_statistics";
    public static final String BT_ORDER_STATISTICS_TODAY = "bt_order_statistics_today";
    public static final String BT_ORDER_CHART_STATISTICS_MONTH = "bt_order_chart_statistics_month";
    public static final String BT_ORDER_CHART_STATISTICS_DAY = "bt_order_chart_statistics_day";
    public static final String BT_ORDER_PROJECT_STATISTICS = "bt_order_project_statistics";

    public static final String DICT_PROVINCE = "shengqu";

    //系统模块工具配置
    public static final String HUANENG_SAP_NO = "huaneng_sap_no";
    public static final String OPERATE_NAME_ADD = "新增";
    public static final String OPERATE_NAME_UPDATE = "修改";
    public static final String OPERATE_NAME_MODULE = "组件规格";
    public static final String OPERATE_NAME_SCHEME_FILING = "归档";
    public static final String OPERATE_NAME_DEL = "删除";

    public static final String GENERATE_NUM = "generateNum:";

    public static final String IS_SUPER_ = "true";
    public static final String SUBMIT_NODE = "提交节点";
    public static final String RE_SUBMIT = "重新提交表单";
    public static final String START_EPC_WORKFLOW = "待处理EPC订单";
    public static final String WORKFLOW_APPROVED = "流程已审批！";

    // 经销商首页统计概览 reids key
    public static final String HOMEPAGE_DISTRIBUTOR_STATISTICS = "homepage_distributor_statistics";
    // 经销商首页 完成情况比例 redis key
    public static final String HOMEPAGE_COMPLETION_RATIO_STATISTICS = "homepage_completion_ratio_statistics";
    // 经销商首页 工程状态及严重堵点 redis key
    public static final String HOMEPAGE_SERIOUS_BLOCK_STATISTICS = "homepage_serious_block_statistics";

    public static final long  ASYNC_TASK_POLLING_TIMEOUT = 2 * 60 * 60 * 1000;

    public static final String STATISTICS_PROJECT_NODE_INV_AGE = "projectNodeInvAge:";

    public static final String TPL_ORG_FILE_NAME = "合同模板授权信息.xls";

    public static final String SYN_BASIC_MATERIAL_WMS = "syn.basic.material.wms.queue";
    /** wms反馈接口 */
    public static final String WMS = "/external/supply_project/wms";

    public static final String TEST_MQ_RETRY = "test.mq.Retry.queue";
    /**
     * 物料二次报装规格默认值
     */
    public static final String NO_MIN_PACKAGE = "-999";
}
