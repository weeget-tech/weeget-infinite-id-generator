package cn.weeget.service.id.exception;

import cn.weeget.util.common.web.exception.BusinessException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author tanyong
 * @Create: 2020/8/19 16:56
 * @Description: 服务异常
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends BusinessException {

    private int errorCode;

    private String errorMsg;

    public ServiceException(int errorCode, String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public ServiceException(String richErrorMsg){
        this(500, richErrorMsg);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
