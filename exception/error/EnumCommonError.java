public enum EnumCommonError implements CommonError {
    //10000 为通用类型错误
    UNKNOW_ERROR(10001, "未知错误"),
    EMPTY_ERROR(10002, "参数为空，请重试"),

    //20000 为 company 通用类型错误
    COMPANY_ID_EXIST(20001, "填写的公司名称或简称已存在，请重新填写"),
    COMPANYCODE__ERROR(20002, "密钥出错，请重试"),
    COMPANY_ID_NULL(20003, "公司简称不能为空，请添加"),
    COMPANY_ADD_ERROR(20004, "公司添加出错，请重试或联系管理员"),
    COMPANY_UPDATE_ERROR(20005, "公司信息更新失败，请重试"),

    //30000 为 dept 通用类型错误
    DEPT_INSERT_ERROR(30001, "部门添加出错，请重试"),
    DEPT_ID_EXIST(30002, "该公司的部门缩写已存在，请重试"),
    DEPT_UPDATE_ERROR(30003, "部门信息修改失败，请重试"),

    //40000 为 user 通用类型错误
    USER_MAIL_EXIST(40001, "该邮箱已被注册，请重试"),
    USER_INSERT_ERROR(40002, "用户注册失败，请重试"),
    USER_MAIL_NULL(40006, "邮箱或用户名为空，请重试"),
    USER_NAME_EXIST(40008, "该用户名已被注册，请重试"),
    PASS_MODIFY_ERROR(40009, "密码修改失败，请重试");


    private int errCode;
    private String errMsg;

    EnumCommonError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
