//返回统一处理类
public class CommonReturn {

    //表明对应请求的返回处理结果“success”或“fail”
    private String status;
    //若status = success，则data内返回前端需要的json数据；若status = fail，则data内使用通用的错误码格式
    private Object data;

    //定义一个通用的创建方法
    public static CommonReturn success(Object result) {
        return CommonReturn.create(result, "success");
    }

    public static CommonReturn fail(Object result) {
        return CommonReturn.create(result, "fail");
    }

    public static CommonReturn create(Object result, String status) {
        CommonReturn commonReturn = new CommonReturn(status, result);
        return commonReturn;
    }

    public CommonReturn() {
    }

    public CommonReturn(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
