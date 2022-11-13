package ${package}.exception;

import ${package}.util.GsonUtil;
import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Log4j2
@RestControllerAdvice
public class ExceptionHandle {

    /**
     * 未知异常处理
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Response> unknownExceptionHandler(Exception e) {
        log.error("UnknownException message: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(Response.buildFailure(SysErrorCode.S_UNKNOWN_ERROR.getErrCode(),
                        SysErrorCode.S_UNKNOWN_ERROR.getErrDesc()));
    }

    /**
     * Feign 调用异常处理
     */
    @ExceptionHandler({FeignException.class})
    public ResponseEntity<Response> feignExceptionHandler(FeignException e) {
        log.error("FeignException message: {}", e.getMessage(), e);
        Response response = GsonUtil.jsonToBean(e.contentUTF8(), Response.class);
        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * 系统异常处理
     */
    @ExceptionHandler({SysException.class})
    public ResponseEntity<Response> sysExceptionHandler(SysException e) {
        log.error("SysException message: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(Response.buildFailure(e.getErrCode(), e.getErrDesc()));
    }

    /**
     * 业务异常处理
     */
    @ExceptionHandler({BizException.class})
    public ResponseEntity<Response> bizExceptionHandler(BizException e) {
        log.warn("BizException message: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(Response.buildFailure(e.getErrCode(), e.getErrDesc()));
    }

    /**
     * 数据校验的处理
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<Response> validatorExceptionHandler(Exception e) {
        String msg;
        if (e instanceof MethodArgumentNotValidException) {
            // RequestBody类校验异常
            msg = msgConvert(((MethodArgumentNotValidException) e).getBindingResult());
        } else if (e instanceof BindException) {
            // RequestBody类校验异常
            msg = msgConvert(((BindException) e).getBindingResult());
        } else {
            // java包装类校验异常（String、Long、Double等等）
            msg = msgConvert(((ConstraintViolationException) e).getConstraintViolations());
        }
        String exceptionMessage = SysErrorCode.S_PARAM_ERROR.getErrDesc() + "：" + msg;
        log.warn(exceptionMessage, e);
        return ResponseEntity.internalServerError()
                .body(Response.buildFailure(SysErrorCode.S_PARAM_ERROR.getErrCode(), exceptionMessage));
    }

    /**
     * 校验消息转换拼接，对象校验
     */
    private String msgConvert(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        FieldError error = fieldErrors.get(0);
        return error.getField() + error.getDefaultMessage();
    }

    /**
     * 校验消息转换拼接，基本数据类型
     */
    private String msgConvert(Set<ConstraintViolation<?>> constraintViolations) {
        ConstraintViolation<?> violation = constraintViolations.iterator().next();
        String[] name = violation.getPropertyPath().toString().split("\\.");
        return name[name.length - 1] + violation.getMessage();
    }

}
