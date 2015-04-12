/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/8/6 17:48:17                            */
/*==============================================================*/


drop table if exists address_contacts;

drop table if exists capp_news;

drop table if exists capp_opinion;


drop table if exists capp_soft_version;

drop table if exists capp_soft;

drop table if exists org_user_roles;

drop table if exists org_role;

drop table if exists org_user;

drop table if exists org_department;

drop table if exists org_company;

drop table if exists privilege_type;

drop table if exists privilege_role;

drop table if exists privilege_user;

drop table if exists soft_test_mobile;

drop table if exists sys_dictionary_value;

drop table if exists sys_dictionary;

drop table if exists sys_file;

drop table if exists sys_log;

drop table if exists sys_resource;

/*==============================================================*/
/* Table: address_contacts                                      */
/*==============================================================*/
create table address_contacts
(
   MEMBER_ID            varchar(36) not null,
   MEMBER_NAME          varchar(255) default NULL,
   PINYIN               varchar(255) default NULL,
   VPMN_PHONE           varchar(36) default NULL comment '集团短号',
   MOBILE_PHONE         varchar(36) default NULL,
   EMAIL_               varchar(255) default NULL,
   ADDRESS_             varchar(512) default NULL,
   POSITION_            varchar(255) default NULL,
   CREAT_TIME           datetime,
   UPDATE_TIME          datetime,
   CREAT_USER           varchar(36) default NULL,
   DELETED              char(1) not null default '0',
   ORDER_SEQ            int default NULL,
   DEP_ID               varchar(36) default NULL,
   COM_ID               varchar(36) default NULL,
   primary key (MEMBER_ID)
);

/*==============================================================*/
/* Table: capp_news                                             */
/*==============================================================*/
create table capp_news
(
   NEWS_ID              bigint not null,
   AUTHOR               varchar(255) default NULL,
   CONTENTS             text,
   CREATE_DATE          datetime,
   IMAGE_URL            varchar(255) default NULL,
   TITLE                varchar(255) default NULL,
   UPDATE_DATE          datetime,
   UPLOADER             varchar(255) default NULL,
   primary key (NEWS_ID)
);

/*==============================================================*/
/* Table: capp_opinion                                          */
/*==============================================================*/
create table capp_opinion
(
   OPINION_ID           bigint not null,
   CONTACT              varchar(255) default NULL,
   DEVICE_ID            varchar(255) default NULL,
   IMSI                 varchar(255) default NULL,
   IS_CHECK             tinyint not null default 0,
   MOBILE_MODEL         varchar(255) default NULL,
   OPINION_CONTENT      varchar(255) default NULL,
   OPINION_TYPE         varchar(255) default NULL,
   UPDATE_DATE          datetime not null,
   primary key (OPINION_ID)
);

/*==============================================================*/
/* Table: capp_soft                                             */
/*==============================================================*/
create table capp_soft
(
   SOFT_ID              bigint not null,
   SOFT_DESCRIPTION     varchar(255) default NULL,
   SOFT_NAME            varchar(255) default NULL,
   CODE                 varchar(255) default NULL,
   primary key (SOFT_ID)
);

/*==============================================================*/
/* Table: capp_soft_version                                     */
/*==============================================================*/
create table capp_soft_version
(
   VER_ID               bigint not null,
   SOFT_ID              bigint,
   STATUS               bigint not null default 0,
   UPLOAD_DATE          datetime,
   VER_CODE             bigint default NULL,
   VER_NAME             varchar(255) default NULL,
   VER_PATH             varchar(255) default NULL,
   VER_RELEASE          varchar(255) default NULL,
   APP_NAME             varchar(255) default NULL,
   primary key (VER_ID)
);

/*==============================================================*/
/* Table: org_company                                           */
/*==============================================================*/
create table org_company
(
   COM_ID               varchar(36) not null,
   COM_NAME             varchar(255) not null,
   COM_ADDRESS          varchar(512) default NULL,
   LOGO_PATH            varchar(512) default NULL,
   RESERVE_1            varchar(100) default NULL comment '保留字段',
   RESERVE_2            varchar(100) default NULL comment '保留字段',
   RESERVE_3            varchar(100) default NULL comment '保留字段',
   RESERVE_4            varchar(100) default NULL comment '保留字段',
   RESERVE_5            varchar(100) default NULL comment '保留字段',
   CREAT_TIME           datetime not null,
   CREAT_USER           varchar(36)  not null,
   primary key (COM_ID)
);

/*==============================================================*/
/* Table: org_department                                        */
/*==============================================================*/
create table org_department
(
   DEP_ID               varchar(36) not null,
   ORG_COM_ID           varchar(36),
   DEP_NAME             varchar(255)  not null,
   FULL_NAME            varchar(255)  not null comment '部门全称',
   PINYIN               varchar(255) default NULL,
   PARENT_ID            varchar(36) default NULL,
   ORDER_SEQ            int  not null,
   CREAT_TIME           datetime not null,
   CHANGE_TIME          datetime,
   DELETED              char(1) not null default '0',
   COM_ID               varchar(36)  not null,
   primary key (DEP_ID)
);

/*==============================================================*/
/* Table: org_role                                              */
/*==============================================================*/
create table org_role
(
   ROLE_ID              varchar(36) not null,
   ROLE_NAME            varchar(255)  not null,
   ROLE_CODE            varchar(255)  not null,
   ROLE_TYPE            char(1)  not null,
   DESCRIPTION_         varchar(255) default NULL,
   STATUS_              char(1) not null default '1',
   CREATE_DATE          datetime not null,
   primary key (ROLE_ID)
);

/*==============================================================*/
/* Table: org_user                                              */
/*==============================================================*/
create table org_user
(
   USER_ID              varchar(36) not null comment '用户Id',
   DEPT_ID               varchar(36),
   USER_NAME            varchar(45)  not null comment '登录用户名',
   REAL_NAME            varchar(120) default NULL comment '用户真实姓名',
   EMAIL_               varchar(300) default NULL comment '电子邮件',
   MOBILE_PHONE         varchar(45) default NULL comment '移动电话号码',
   PASSWORD_            varchar(300)  not null comment '登录密码',
   PWD_FORMAT           varchar(45)  not null comment '密码加密方式',
   APPROVED_            tinyint not null default 0 comment '是否审核通过',
   LOCKED_OUT           tinyint not null default 0 comment '账号是否锁定',
   LAST_LOCKOUT_DATE    datetime comment '账号锁定时间',
   QUESTION_            varchar(100) default NULL comment '密码问题',
   ANSWER_              varchar(100) default NULL comment '问题答案',
   FAILED_ANSWER_DATE   datetime default NULL comment '提醒问题回答错误时间',
   FAILED_ANSWER_COUNT  int not null default 0 comment '提醒问题回答错误次数',
   FAILED_DATE          datetime comment '登录失败时间',
   FAILED_COUNT         int default 0 comment '登录失败次数',
   LAST_LOGIN_DATE      datetime comment '最后一次登录时间',
   LAST_PWD_CHANGE      datetime comment '密码最后一次更改时间',
   CREATE_DATE          datetime not null comment '用户创建时间',
   COM_ID               varchar(36)  not null comment '企业id',
   STATUS_              char(1) not null default '1',
   DEVICE_ID            varchar(255) default NULL comment '设备号',
   IMSI_                varchar(255) default NULL comment 'sim卡序列号',
   ACCESS_PERMIT        varchar(10) not null default 'all' comment '用户访问许可，all|web|mobile',
   primary key (USER_ID)
);

/*==============================================================*/
/* Table: org_user_roles                                        */
/*==============================================================*/
create table org_user_roles
(
   ROLES              varchar(36) not null,
   USERS              varchar(36) not null comment '用户Id',
   primary key (ROLES, USERS)
);

/*==============================================================*/
/* Table: privilege_role                                        */
/*==============================================================*/
create table privilege_role
(
   ROLE_ID              varchar(36) not null comment '角色id',
   OBJ_ID               varchar(36) not null comment '权限对象id',
   TYPE_                varchar(60) not null,
   COM_ID               varchar(36) default NULL comment '企业id',
   primary key (ROLE_ID, OBJ_ID, TYPE_)
);

/*==============================================================*/
/* Table: privilege_type                                        */
/*==============================================================*/
create table privilege_type
(
   TYPE_ID              varchar(36) not null comment '权限类型id',
   TYPE_NAME            varchar(60)  not null comment '权限类型名称',
   TYPE_KEY             varchar(60)  not null comment '权限类型key',
   HANDLE_              varchar(255)  not null comment '权限处理程序',
   DESCRIPTION_         varchar(255) default NULL comment '描述',
   primary key (TYPE_ID)
);

/*==============================================================*/
/* Table: privilege_user                                        */
/*==============================================================*/
create table privilege_user
(
   USER_ID              varchar(36) not null comment '用户id',
   OBJ_ID               varchar(36) not null comment '权限对象id',
   TYPE_                varchar(60) not null comment '权限类型',
   COM_ID               varchar(36) default NULL comment '企业id',
   primary key (USER_ID, OBJ_ID, TYPE_)
);

/*==============================================================*/
/* Table: soft_test_mobile                                      */
/*==============================================================*/
create table soft_test_mobile
(
   MOBILE_ID            bigint not null,
   DEVICE_ID            varchar(255) default NULL,
   MOBILE_MODEL         varchar(255) default NULL,
   primary key (MOBILE_ID)
);

/*==============================================================*/
/* Table: sys_dictionary                                        */
/*==============================================================*/
create table sys_dictionary
(
   DICN_ID              varchar(36) not null,
   DIC_NAME             varchar(255)  not null,
   DICN_CODE            varchar(255)  not null,
   DICN_TYPE            char(1) not null default '1',
   IN_USE               char(1) not null default '1',
   CREATE_DATE          datetime not null,
   primary key (DICN_ID)
);

/*==============================================================*/
/* Table: sys_dictionary_value                                  */
/*==============================================================*/
create table sys_dictionary_value
(
   DICV_ID              varchar(36) not null,
   SYS_DICN_ID          varchar(36),
   DICV_TEXT            varchar(255)  not null,
   DIC_VALUE            varchar(255)  not null,
   DICV_ORDER           varchar(255)  not null,
   DICN_ID              varchar(255)  not null,
   primary key (DICV_ID)
);

/*==============================================================*/
/* Table: sys_file                                              */
/*==============================================================*/
create table sys_file
(
   FILE_ID              varchar(36) not null,
   NEW_NAME             varchar(255)  not null,
   OLD_NAME             varchar(255)  not null,
   PATH_                varchar(255)  not null,
   FILE_SIZE            varchar(255)  not NULL,
   UPLOAD_TIME          datetime not null,
   USER_ID              varchar(255) default NULL,
   VERSION_             varchar(255) default NULL,
   primary key (FILE_ID)
);

/*==============================================================*/
/* Table: sys_log                                               */
/*==============================================================*/
create table sys_log
(
   LOG_ID               varchar(36) not null,
   USER_ID              varchar(255)  not null,
   USER_NAME            varchar(255)  not null,
   ACTION_              varchar(255)  not null,
   MODULE_              varchar(255)  not null,
   RESULT_              varchar(255)  not null,
   DESCRIPTION_         varchar(255) default NULL,
   ACCESS_TYPE         	varchar(255) default NULL,
   DEVICE_         		varchar(255) default NULL,
   REALITY_IP           varchar(255) default NULL,
   PROXY_IP             varchar(255) default NULL,
   ALLOW_DELETE         char(1) not null default '0',
   CREATE_DATE          datetime not null,
   primary key (LOG_ID)
);

/*==============================================================*/
/* Table: sys_resource                                          */
/*==============================================================*/
create table sys_resource
(
   RESOURCE_ID          varchar(36) not null,
   NAME_                varchar(60)  not null comment '菜单名称',
   CODE_                varchar(30)  not null comment '菜单代码，访问路径可由代码拼接而成',
   PARENT_ID            varchar(36) default NULL comment '父菜单id',
   BIG_ICON             varchar(100) default NULL comment '菜单大图标',
   SMAIL_ICON           varchar(100) default NULL comment '菜单小图标',
   ORDER_SEQ            int  not null comment '同级菜单排序',
   URL_                 varchar(255) default NULL comment '菜单url',
   TARGET_              varchar(30) default NULL comment '结果回显目标',
   DESCRIPTION_         varchar(255) default NULL comment '菜单描述',
   MENU_ABLE            char(1) not null default '1' comment '是否隐藏',
   ACCESSIBLE_          char(1) not null default '1',
   primary key (RESOURCE_ID)
);

alter table capp_soft_version add constraint FK_Relationship_5 foreign key (SOFT_ID)
      references capp_soft (SOFT_ID) on delete restrict on update restrict;

alter table org_department add constraint FK_fk_org_dep_com_id foreign key (ORG_COM_ID)
      references org_company (COM_ID) on delete restrict on update restrict;

alter table org_user add constraint FK_FK_nqavehvfear8yc5ml6im6npqr foreign key (DEPT_ID)
      references org_department (DEP_ID) on delete restrict on update restrict;

alter table org_user_roles add constraint FK_roles foreign key (USERS)
      references org_user (USER_ID) on delete restrict on update restrict;

alter table org_user_roles add constraint FK_users foreign key (ROLES)
      references org_role (ROLE_ID) on delete restrict on update restrict;

alter table sys_dictionary_value add constraint FK_FK_gxiggxcp31dsdtbxwkudk5apt foreign key (SYS_DICN_ID)
      references sys_dictionary (DICN_ID) on delete restrict on update restrict;

