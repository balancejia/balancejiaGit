/*==============================================================*/
/* DBMS name:      ORACLE Version 10g                           */
/* Created on:     2014/8/6 18:34:25                            */
/*==============================================================*/


alter table CAPP_SOFT_VERSION
   drop constraint FK_CAPP_SOF_RELATIONS_CAPP_SOF;

alter table ORG_DEPARTMENT
   drop constraint FK_ORG_DEPA_FK_ORG_DE_ORG_COMP;

alter table ORG_USER
   drop constraint FK_ORG_USER_FK_NQAVEH_ORG_DEPA;

alter table ORG_USER_ROLES
   drop constraint FK_ORG_USER_ROLES_ORG_USER;

alter table ORG_USER_ROLES
   drop constraint FK_ORG_USER_USERS_ORG_ROLE;

alter table SYS_DICTIONARY_VALUE
   drop constraint FK_SYS_DICT_FK_GXIGGX_SYS_DICT;

drop table ADDRESS_CONTACTS cascade constraints;

drop table CAPP_NEWS cascade constraints;

drop table CAPP_OPINION cascade constraints;

drop table CAPP_SOFT cascade constraints;

drop index "Relationship_5_FK";

drop table CAPP_SOFT_VERSION cascade constraints;

drop table ORG_COMPANY cascade constraints;

drop table ORG_DEPARTMENT cascade constraints;

drop table ORG_ROLE cascade constraints;

drop table ORG_USER cascade constraints;

drop table ORG_USER_ROLES cascade constraints;

drop table PRIVILEGE_ROLE cascade constraints;

drop table PRIVILEGE_TYPE cascade constraints;

drop table PRIVILEGE_USER cascade constraints;

drop table SOFT_TEST_MOBILE cascade constraints;

drop table SYS_DICTIONARY cascade constraints;

drop table SYS_DICTIONARY_VALUE cascade constraints;

drop table SYS_FILE cascade constraints;

drop table SYS_LOG cascade constraints;

drop table SYS_RESOURCE cascade constraints;

/*==============================================================*/
/* Table: ADDRESS_CONTACTS                                      */
/*==============================================================*/
create table ADDRESS_CONTACTS  (
   MEMBER_ID            VARCHAR2(36)                    not null,
   MEMBER_NAME          VARCHAR2(255)                  default NULL,
   PINYIN               VARCHAR2(255)                  default NULL,
   VPMN_PHONE           VARCHAR2(36)                   default NULL,
   MOBILE_PHONE         VARCHAR2(36)                   default NULL,
   EMAIL_               VARCHAR2(255)                  default NULL,
   ADDRESS_             VARCHAR2(512)                  default NULL,
   POSITION_            VARCHAR2(255)                  default NULL,
   CREAT_TIME           DATE                           default NULL,
   UPDATE_TIME          DATE                           default NULL,
   CREAT_USER           VARCHAR2(36)                   default NULL,
   DELETED              CHAR(1)                        default '0' not null,
   ORDER_SEQ            INTEGER                        default NULL,
   DEP_ID               VARCHAR2(36)                   default NULL,
   COM_ID               VARCHAR2(36)                   default NULL,
   constraint PK_ADDRESS_CONTACTS primary key (MEMBER_ID)
);

comment on column ADDRESS_CONTACTS.VPMN_PHONE is
'集团短号';

/*==============================================================*/
/* Table: CAPP_NEWS                                             */
/*==============================================================*/
create table CAPP_NEWS  (
   NEWS_ID              INTEGER                         not null,
   AUTHOR               VARCHAR2(255)                  default NULL,
   CONTENTS             CLOB,
   CREATE_DATE          DATE                           default NULL,
   IMAGE_URL            VARCHAR2(255)                  default NULL,
   TITLE                VARCHAR2(255)                  default NULL,
   UPDATE_DATE          DATE                           default NULL,
   UPLOADER             VARCHAR2(255)                  default NULL,
   constraint PK_CAPP_NEWS primary key (NEWS_ID)
);

/*==============================================================*/
/* Table: CAPP_OPINION                                          */
/*==============================================================*/
create table CAPP_OPINION  (
   OPINION_ID           INTEGER                         not null,
   CONTACT              VARCHAR2(255)                  default NULL,
   DEVICE_ID            VARCHAR2(255)                  default NULL,
   IMSI                 VARCHAR2(255)                  default NULL,
   IS_CHECK             SMALLINT                       default 0 not null,
   MOBILE_MODEL         VARCHAR2(255)                  default NULL,
   OPINION_CONTENT      VARCHAR2(255)                  default NULL,
   OPINION_TYPE         VARCHAR2(255)                  default NULL,
   UPDATE_DATE          DATE                           default NULL not null,
   constraint PK_CAPP_OPINION primary key (OPINION_ID)
);

/*==============================================================*/
/* Table: CAPP_SOFT                                             */
/*==============================================================*/
create table CAPP_SOFT  (
   SOFT_ID              INTEGER                         not null,
   SOFT_DESCRIPTION     VARCHAR2(255)                  default NULL,
   SOFT_NAME            VARCHAR2(255)                  default NULL,
   CODE                 VARCHAR2(255)                  default NULL,
   constraint PK_CAPP_SOFT primary key (SOFT_ID)
);

/*==============================================================*/
/* Table: CAPP_SOFT_VERSION                                     */
/*==============================================================*/
create table CAPP_SOFT_VERSION  (
   VER_ID               INTEGER                         not null,
   SOFT_ID              INTEGER,
   STATUS               INTEGER                        default NULL not null,
   UPLOAD_DATE          DATE                           default NULL,
   VER_CODE             INTEGER                        default NULL,
   VER_NAME             VARCHAR2(255)                  default NULL,
   VER_PATH             VARCHAR2(255)                  default NULL,
   VER_RELEASE          VARCHAR2(255)                  default NULL,
   APP_NAME             VARCHAR2(255)                  default NULL,
   constraint PK_CAPP_SOFT_VERSION primary key (VER_ID)
);

/*==============================================================*/
/* Index: "Relationship_5_FK"                                   */
/*==============================================================*/
create index "Relationship_5_FK" on CAPP_SOFT_VERSION (
   SOFT_ID ASC
);

/*==============================================================*/
/* Table: ORG_COMPANY                                           */
/*==============================================================*/
create table ORG_COMPANY  (
   COM_ID               VARCHAR2(36)                    not null,
   COM_NAME             VARCHAR2(255)                   not null,
   COM_ADDRESS          VARCHAR2(512)                  default NULL,
   LOGO_PATH            VARCHAR2(512)                  default NULL,
   RESERVE_1            VARCHAR2(100)                  default NULL,
   RESERVE_2            VARCHAR2(100)                  default NULL,
   RESERVE_3            VARCHAR2(100)                  default NULL,
   RESERVE_4            VARCHAR2(100)                  default NULL,
   RESERVE_5            VARCHAR2(100)                  default NULL,
   CREAT_TIME           DATE                           default NULL not null,
   CREAT_USER           VARCHAR2(36)                   default NULL not null,
   constraint PK_ORG_COMPANY primary key (COM_ID)
);

comment on column ORG_COMPANY.RESERVE_1 is
'保留字段';

comment on column ORG_COMPANY.RESERVE_2 is
'保留字段';

comment on column ORG_COMPANY.RESERVE_3 is
'保留字段';

comment on column ORG_COMPANY.RESERVE_4 is
'保留字段';

comment on column ORG_COMPANY.RESERVE_5 is
'保留字段';

/*==============================================================*/
/* Table: ORG_DEPARTMENT                                        */
/*==============================================================*/
create table ORG_DEPARTMENT  (
   DEP_ID               VARCHAR2(36)                    not null,
   ORG_COM_ID           VARCHAR2(36),
   DEP_NAME             VARCHAR2(255)                  default NULL not null,
   FULL_NAME            VARCHAR2(255)                  default NULL not null,
   PINYIN               VARCHAR2(255)                  default NULL,
   PARENT_ID            VARCHAR2(36)                   default NULL,
   ORDER_SEQ            INTEGER                        default NULL not null,
   CREAT_TIME           DATE                           default NULL not null,
   CHANGE_TIME          DATE                           default NULL,
   DELETED              CHAR(1)                        default '0' not null,
   COM_ID               VARCHAR2(36)                   default NULL not null,
   constraint PK_ORG_DEPARTMENT primary key (DEP_ID)
);

comment on column ORG_DEPARTMENT.FULL_NAME is
'部门全称';

/*==============================================================*/
/* Table: ORG_ROLE                                              */
/*==============================================================*/
create table ORG_ROLE  (
   ROLE_ID              VARCHAR2(36)                    not null,
   ROLE_NAME            VARCHAR2(255)                  default NULL not null,
   ROLE_CODE            VARCHAR2(255)                  default NULL not null,
   ROLE_TYPE            CHAR(1)                        default NULL not null,
   DESCRIPTION_         VARCHAR2(255)                  default NULL,
   STATUS_              CHAR(1)                        default '1' not null,
   CREATE_DATE          DATE                           default NULL not null,
   constraint PK_ORG_ROLE primary key (ROLE_ID)
);

/*==============================================================*/
/* Table: ORG_USER                                              */
/*==============================================================*/
create table ORG_USER  (
   USER_ID              VARCHAR2(36)                    not null,
   DEPT_ID              VARCHAR2(36),
   USER_NAME            VARCHAR2(45)                   default NULL not null,
   REAL_NAME            VARCHAR2(120)                  default NULL,
   EMAIL_               VARCHAR2(300)                  default NULL,
   MOBILE_PHONE         VARCHAR2(45)                   default NULL,
   PASSWORD_            VARCHAR2(300)                  default NULL not null,
   PWD_FORMAT           VARCHAR2(45)                   default NULL not null,
   APPROVED_            SMALLINT                       default 0 not null,
   LOCKED_OUT           SMALLINT                       default 0 not null,
   LAST_LOCKOUT_DATE    DATE                           default NULL,
   QUESTION_            VARCHAR2(100)                  default NULL,
   ANSWER_              VARCHAR2(100)                  default NULL,
   FAILED_ANSWER_DATE   DATE                           default NULL,
   FAILED_ANSWER_COUNT  INTEGER                        default 0 not null,
   FAILED_DATE          DATE                           default NULL,
   FAILED_COUNT         INTEGER                        default 0,
   LAST_LOGIN_DATE      DATE                           default NULL,
   LAST_PWD_CHANGE      DATE                           default NULL,
   CREATE_DATE          DATE                           default NULL not null,
   COM_ID               VARCHAR2(36)                   default NULL not null,
   STATUS_              CHAR(1)                        default '1' not null,
   DEVICE_ID            VARCHAR2(255)                  default NULL,
   IMSI_                VARCHAR2(255)                  default NULL,
   ACCESS_PERMIT        VARCHAR2(10)                   default 'all' not null,
   constraint PK_ORG_USER primary key (USER_ID)
);

comment on column ORG_USER.USER_ID is
'用户Id';

comment on column ORG_USER.USER_NAME is
'登录用户名';

comment on column ORG_USER.REAL_NAME is
'用户真实姓名';

comment on column ORG_USER.EMAIL_ is
'电子邮件';

comment on column ORG_USER.MOBILE_PHONE is
'移动电话号码';

comment on column ORG_USER.PASSWORD_ is
'登录密码';

comment on column ORG_USER.PWD_FORMAT is
'密码加密方式';

comment on column ORG_USER.APPROVED_ is
'是否审核通过';

comment on column ORG_USER.LOCKED_OUT is
'账号是否锁定';

comment on column ORG_USER.LAST_LOCKOUT_DATE is
'账号锁定时间';

comment on column ORG_USER.QUESTION_ is
'密码问题';

comment on column ORG_USER.ANSWER_ is
'问题答案';

comment on column ORG_USER.FAILED_ANSWER_DATE is
'提醒问题回答错误时间';

comment on column ORG_USER.FAILED_ANSWER_COUNT is
'提醒问题回答错误次数';

comment on column ORG_USER.FAILED_DATE is
'登录失败时间';

comment on column ORG_USER.FAILED_COUNT is
'登录失败次数';

comment on column ORG_USER.LAST_LOGIN_DATE is
'最后一次登录时间';

comment on column ORG_USER.LAST_PWD_CHANGE is
'密码最后一次更改时间';

comment on column ORG_USER.CREATE_DATE is
'用户创建时间';

comment on column ORG_USER.COM_ID is
'企业id';

comment on column ORG_USER.DEVICE_ID is
'设备号';

comment on column ORG_USER.IMSI_ is
'sim卡序列号';

comment on column ORG_USER.ACCESS_PERMIT is
'用户访问许可，all|web|mobile';

/*==============================================================*/
/* Table: ORG_USER_ROLES                                        */
/*==============================================================*/
create table ORG_USER_ROLES  (
   ROLES                VARCHAR2(36)                    not null,
   USERS                VARCHAR2(36)                    not null,
   constraint PK_ORG_USER_ROLES primary key (ROLES, USERS)
);

comment on column ORG_USER_ROLES.USERS is
'用户Id';

/*==============================================================*/
/* Table: PRIVILEGE_ROLE                                        */
/*==============================================================*/
create table PRIVILEGE_ROLE  (
   ROLE_ID              VARCHAR2(36)                    not null,
   OBJ_ID               VARCHAR2(36)                    not null,
   TYPE_                VARCHAR2(60)                    not null,
   COM_ID               VARCHAR2(36)                   default NULL,
   constraint PK_PRIVILEGE_ROLE primary key (ROLE_ID, OBJ_ID, TYPE_)
);

comment on column PRIVILEGE_ROLE.ROLE_ID is
'角色id';

comment on column PRIVILEGE_ROLE.OBJ_ID is
'权限对象id';

comment on column PRIVILEGE_ROLE.COM_ID is
'企业id';

/*==============================================================*/
/* Table: PRIVILEGE_TYPE                                        */
/*==============================================================*/
create table PRIVILEGE_TYPE  (
   TYPE_ID              VARCHAR2(36)                    not null,
   TYPE_NAME            VARCHAR2(60)                   default NULL not null,
   TYPE_KEY             VARCHAR2(60)                   default NULL not null,
   HANDLE_              VARCHAR2(255)                  default NULL not null,
   DESCRIPTION_         VARCHAR2(255)                  default NULL,
   constraint PK_PRIVILEGE_TYPE primary key (TYPE_ID)
);

comment on column PRIVILEGE_TYPE.TYPE_ID is
'权限类型id';

comment on column PRIVILEGE_TYPE.TYPE_NAME is
'权限类型名称';

comment on column PRIVILEGE_TYPE.TYPE_KEY is
'权限类型key';

comment on column PRIVILEGE_TYPE.HANDLE_ is
'权限处理程序';

comment on column PRIVILEGE_TYPE.DESCRIPTION_ is
'描述';

/*==============================================================*/
/* Table: PRIVILEGE_USER                                        */
/*==============================================================*/
create table PRIVILEGE_USER  (
   USER_ID              VARCHAR2(36)                    not null,
   OBJ_ID               VARCHAR2(36)                    not null,
   TYPE_                VARCHAR2(60)                    not null,
   COM_ID               VARCHAR2(36)                   default NULL,
   constraint PK_PRIVILEGE_USER primary key (USER_ID, OBJ_ID, TYPE_)
);

comment on column PRIVILEGE_USER.USER_ID is
'用户id';

comment on column PRIVILEGE_USER.OBJ_ID is
'权限对象id';

comment on column PRIVILEGE_USER.TYPE_ is
'权限类型';

comment on column PRIVILEGE_USER.COM_ID is
'企业id';

/*==============================================================*/
/* Table: SOFT_TEST_MOBILE                                      */
/*==============================================================*/
create table SOFT_TEST_MOBILE  (
   MOBILE_ID            INTEGER                         not null,
   DEVICE_ID            VARCHAR2(255)                  default NULL,
   MOBILE_MODEL         VARCHAR2(255)                  default NULL,
   constraint PK_SOFT_TEST_MOBILE primary key (MOBILE_ID)
);

/*==============================================================*/
/* Table: SYS_DICTIONARY                                        */
/*==============================================================*/
create table SYS_DICTIONARY  (
   DICN_ID              VARCHAR2(36)                    not null,
   DIC_NAME             VARCHAR2(255)                  default NULL not null,
   DICN_CODE            VARCHAR2(255)                  default NULL not null,
   DICN_TYPE            CHAR(1)                        default '1' not null,
   IN_USE               CHAR(1)                        default '1' not null,
   CREATE_DATE          DATE                           default NULL not null,
   constraint PK_SYS_DICTIONARY primary key (DICN_ID)
);

/*==============================================================*/
/* Table: SYS_DICTIONARY_VALUE                                  */
/*==============================================================*/
create table SYS_DICTIONARY_VALUE  (
   DICV_ID              VARCHAR2(36)                    not null,
   SYS_DICN_ID          VARCHAR2(36),
   DICV_TEXT            VARCHAR2(255)                  default NULL not null,
   DIC_VALUE            VARCHAR2(255)                  default NULL not null,
   DICV_ORDER           VARCHAR2(255)                  default NULL not null,
   DICN_ID              VARCHAR2(255)                  default NULL not null,
   constraint PK_SYS_DICTIONARY_VALUE primary key (DICV_ID)
);

/*==============================================================*/
/* Table: SYS_FILE                                              */
/*==============================================================*/
create table SYS_FILE  (
   FILE_ID              VARCHAR2(36)                    not null,
   NEW_NAME             VARCHAR2(255)                  default NULL not null,
   OLD_NAME             VARCHAR2(255)                  default NULL not null,
   PATH_                VARCHAR2(255)                  default NULL not null,
   FILE_SIZE            VARCHAR2(255)                  default NULL not null,
   UPLOAD_TIME          DATE                           default NULL not null,
   USER_ID              VARCHAR2(255)                  default NULL,
   VERSION_             VARCHAR2(255)                  default NULL,
   constraint PK_SYS_FILE primary key (FILE_ID)
);

/*==============================================================*/
/* Table: SYS_LOG                                               */
/*==============================================================*/
create table SYS_LOG  (
   LOG_ID               VARCHAR2(36)                    not null,
   USER_ID              VARCHAR2(255)                  default NULL not null,
   USER_NAME            VARCHAR2(255)                  default NULL not null,
   ACTION_              VARCHAR2(255)                  default NULL not null,
   MODULE_              VARCHAR2(255)                  default NULL not null,
   RESULT_              VARCHAR2(255)                  default NULL not null,
   DESCRIPTION_         VARCHAR2(255)                  default NULL,
   ACCESS_TYPE         	VARCHAR2(255) 				   default NULL,
   DEVICE_         		VARCHAR2(255) 				   default NULL,
   REALITY_IP           VARCHAR2(255)                  default NULL,
   PROXY_IP             VARCHAR2(255)                  default NULL,
   ALLOW_DELETE         CHAR(1)                        default '0' not null,
   CREATE_DATE          DATE                           default NULL not null,
   constraint PK_SYS_LOG primary key (LOG_ID)
);

/*==============================================================*/
/* Table: SYS_RESOURCE                                          */
/*==============================================================*/
create table SYS_RESOURCE  (
   RESOURCE_ID          VARCHAR2(36)                    not null,
   NAME_                VARCHAR2(60)                   default NULL not null,
   CODE_                VARCHAR2(30)                   default NULL not null,
   PARENT_ID            VARCHAR2(36)                   default NULL,
   BIG_ICON             VARCHAR2(100)                  default NULL,
   SMAIL_ICON           VARCHAR2(100)                  default NULL,
   ORDER_SEQ            INTEGER                        default NULL not null,
   URL_                 VARCHAR2(255)                  default NULL,
   TARGET_              VARCHAR2(30)                   default NULL,
   DESCRIPTION_         VARCHAR2(255)                  default NULL,
   MENU_ABLE            CHAR(1)                        default '1' not null,
   ACCESSIBLE_          CHAR(1)                        default '1' not null,
   constraint PK_SYS_RESOURCE primary key (RESOURCE_ID)
);

comment on column SYS_RESOURCE.NAME_ is
'菜单名称';

comment on column SYS_RESOURCE.CODE_ is
'菜单代码，访问路径可由代码拼接而成';

comment on column SYS_RESOURCE.PARENT_ID is
'父菜单id';

comment on column SYS_RESOURCE.BIG_ICON is
'菜单大图标';

comment on column SYS_RESOURCE.SMAIL_ICON is
'菜单小图标';

comment on column SYS_RESOURCE.ORDER_SEQ is
'同级菜单排序';

comment on column SYS_RESOURCE.URL_ is
'菜单url';

comment on column SYS_RESOURCE.TARGET_ is
'结果回显目标';

comment on column SYS_RESOURCE.DESCRIPTION_ is
'菜单描述';

comment on column SYS_RESOURCE.MENU_ABLE is
'是否隐藏';

alter table CAPP_SOFT_VERSION
   add constraint FK_CAPP_SOF_RELATIONS_CAPP_SOF foreign key (SOFT_ID)
      references CAPP_SOFT (SOFT_ID);

alter table ORG_DEPARTMENT
   add constraint FK_ORG_DEPA_FK_ORG_DE_ORG_COMP foreign key (ORG_COM_ID)
      references ORG_COMPANY (COM_ID);

alter table ORG_USER
   add constraint FK_ORG_USER_FK_NQAVEH_ORG_DEPA foreign key (DEPT_ID)
      references ORG_DEPARTMENT (DEP_ID);

alter table ORG_USER_ROLES
   add constraint FK_ORG_USER_ROLES_ORG_USER foreign key (USERS)
      references ORG_USER (USER_ID);

alter table ORG_USER_ROLES
   add constraint FK_ORG_USER_USERS_ORG_ROLE foreign key (ROLES)
      references ORG_ROLE (ROLE_ID);

alter table SYS_DICTIONARY_VALUE
   add constraint FK_SYS_DICT_FK_GXIGGX_SYS_DICT foreign key (SYS_DICN_ID)
      references SYS_DICTIONARY (DICN_ID);

