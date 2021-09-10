package com.jasmine.common.core.common;

import com.jasmine.common.core.dto.R;
import com.jasmine.common.core.dto.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.net.ConnectException;

/**
 * 全局异常处理器
 * <p>
 * 统一以 R 对象格式返回给客服端
 *
 * @author zhangxw
 * @since 0.0.1
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

	/**
	 * 业务参数异常
	 */
	@ExceptionHandler(Exception.class)
	public R handleException(Exception exception) {
		exception.printStackTrace();
		return R.fault(RCode.INTERNAL_SERVER_ERROR.code(), RCode.INTERNAL_SERVER_ERROR.msg() + ":" + exception.getMessage());
	}

	/**
	 * 参数处理错误
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R illegalArgumentExceptionHandler(Exception exception) {
		exception.printStackTrace();
		return R.fault(RCode.BAD_REQUEST.code(), RCode.BAD_REQUEST.msg() + ":" + exception.getMessage());
	}

	/**
	 * 数据资源访问失败异常
	 */
	@ExceptionHandler(ConnectException.class)
	public R handleDataAccessResourceFaultException(ConnectException exception) {
		exception.printStackTrace();
		return R.fault(99999, "连接失败" + exception.getMessage());
	}

//	/**
//	 * 违反数据完整性(有外键关联 或者 必填项不能为空)
//	 *
//	 * @param exception 违反数据完整性(有外键关联 或者 必填项不能为空)
//	 * @return R 返回结果
//	 */
//	@ExceptionHandler(DataIntegrityViolationException.class)
//	public R handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
//		return new R<>().fault(InvokeExceptionEnum.DATA_INTEGRITY_VIOLATION_ERROR.getCode(),
//				InvokeExceptionEnum.DATA_INTEGRITY_VIOLATION_ERROR.getMsg(),
//				StackTraceUtil.filterStackTrace(exception));
//	}

//	/**
//	 * 数据库主键重复
//	 *
//	 * @param exception 数据库主键重复异常
//	 * @return R 返回结果
//	 */
//	@ExceptionHandler(DuplicateKeyException.class)
//	public R handleDuplicateKeyException(DuplicateKeyException exception) {
//		log.error(StackTraceUtil.stackTrace(exception));
//		return new R<>().fault(InvokeExceptionEnum.DUPLICATE_KEY_ERROR.getCode(),
//				InvokeExceptionEnum.DUPLICATE_KEY_ERROR.getMsg(),
//				StackTraceUtil.filterStackTrace(exception));
//	}

	/**
	 * ServletRequest 绑定异常。 属于 HTTP 中的 400 错误
	 * <p>
	 * 示例：返回的 pojo 在序列化成 json 过程失败了，那么抛该异常；
	 */
	@ExceptionHandler(ServletRequestBindingException.class)
	public R handleServletRequestBindingException(ServletRequestBindingException exception) {
		exception.printStackTrace();
		return R.fault(RCode.BAD_REQUEST.code(), "缺少请求参数:" + exception.getMessage());
	}

	/**
	 * 缺少 ServletRequest 请求参数。 属于 HTTP 中的 400 错误
	 * <p>
	 * 1、@RequestParam("licenceId") String licenceId，但发起请求时，未携带该参数，则会抛该异常；
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public R handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
		exception.printStackTrace();
		return R.fault(RCode.BAD_REQUEST.code(), String.format("缺少请求参数[%s]",exception.getParameterName()));
	}

	/**
	 * 未检测到路径参数异常。 属于 HTTP 中的 500 错误
	 * <p>
	 * 1、url为：/licence/{licenceId}，参数签名包含 @PathVariable("licenceId")，当请求的 url 为 /licence 时。
	 */
	@ExceptionHandler(MissingPathVariableException.class)
	public R handleMissingPathVariableException(MissingPathVariableException exception) {
		exception.printStackTrace();
		return R.fault(RCode.BAD_REQUEST.code(), "缺少请求参数:" + exception.getMessage());
	}

	/**
	 * 参数解析异常。 属于 HTTP 中的 400 错误
	 * <p>
	 * 1、JSON parse error： 使用 @RequestBody 接收参数时，参数是 json 字符串，数据是 Long 类型的，json 字符串中却传入了 String 类型。
	 * 2、Required request body is missing： 使用 @RequestBody 接收参数时，不传递参数。
	 * 3、Required request body is missing： 使用 get 请求去请求使用 @RequestBody 接收参数的接口时。
	 * 4、JSON parse error: Numeric value (9999999999999999999) out of range of int (-2147483648 - 2147483647);
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public R handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		exception.printStackTrace();
		return R.fault(RCode.BAD_REQUEST.code(), "参数解析异常:" + exception.getMessage());
	}

	/**
	 * Spring 参数绑定异常
	 * <p>
	 * 1、org.springframework.validation.BindException： 参数上添加了 @Validated 注解，不传递参数。
	 * 2、org.springframework.validation.BindException： 参数上添加了 @Validated 注解，不使用 @RequestBody 接收参数且参数值超出了校验范围。
	 */
	@ExceptionHandler(BindException.class)
	public R handleBindException(BindException exception) {
		exception.printStackTrace();
		return R.fault(RCode.BAD_REQUEST.code(), "参数绑定异常:" + exception.getMessage());
	}

	/**
	 * 方法参数校验异常。 属于 HTTP 中的 400 错误
	 * <p>
	 * 使用 @Validated 注解的参数校验
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		exception.printStackTrace();
		return R.fault(RCode.BAD_REQUEST.code(), "方法参数校验异常:" + exception.getMessage());
	}


	/**
	 * 405 错误
	 * <p>
	 * 1、请求对应的 Controller 方法支持 Post 请求，此时，用 GET 请求来访问。
	 */
	@ExceptionHandler({HttpRequestMethodNotSupportedException.class})
	public R request405(HttpRequestMethodNotSupportedException exception) {
		exception.printStackTrace();
		return R.fault(RCode.BAD_REQUEST.code(), "当前访问路径不支持 %s 请求" + exception.getMethod());
	}

	/**
	 * 空指针异常
	 */
	@ExceptionHandler(NullPointerException.class)
	public R nullPointerExceptionHandler(NullPointerException exception) {
		exception.printStackTrace();
		return R.fault(RCode.INTERNAL_SERVER_ERROR.code(), "空引用:" + exception.getMessage());
	}


	/**
	 * 类型转换异常
	 */
	@ExceptionHandler(ClassCastException.class)
	public R classCastExceptionHandler(ClassCastException exception) {
		exception.printStackTrace();
		return R.fault(RCode.INTERNAL_SERVER_ERROR.code(), "类型转换错误:" + exception.getMessage());
	}

	/**
	 * IO 异常
	 */
	@ExceptionHandler(IOException.class)
	public R ioExceptionHandler(IOException exception) {
		exception.printStackTrace();
		return R.fault(RCode.INTERNAL_SERVER_ERROR.code(), "IO 异常:" + exception.getMessage());
	}

	/**
	 * 方法不存在异常
	 */
	@ExceptionHandler(NoSuchMethodException.class)
	public R noSuchMethodExceptionHandler(NoSuchMethodException exception) {
		exception.printStackTrace();
		return R.fault(RCode.INTERNAL_SERVER_ERROR.code(), "方法不存在:" + exception.getMessage());
	}

	/**
	 * 下标越界异常
	 */
	@ExceptionHandler(IndexOutOfBoundsException.class)
	public R indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException exception) {
		exception.printStackTrace();
		return R.fault(RCode.INTERNAL_SERVER_ERROR.code(), "下标越界:" + exception.getMessage());
	}

	//------------------------------------------------------------------------------------------------------------------

	/**
	 * 503 错误
	 */
	@ExceptionHandler({AsyncRequestTimeoutException.class})
	public R request503(AsyncRequestTimeoutException exception) {
		exception.printStackTrace();
		return R.fault(RCode.INTERNAL_SERVER_ERROR.code(), "异步请求超时:" + exception.getMessage());
	}

	/**
	 * 500 错误
	 * <p>
	 * ConversionNotSupportedException：转换不支持
	 * HttpMessageNotWritableException：返回的 pojo 在序列化成 json 过程失败了，那么抛该异常；
	 * MissingPathVariableException：未检测到路径参数：url为：/licence/{licenceId}，参数签名包含 @PathVariable("licenceId")
	 * ，当请求的url为/licence，在没有明确定义url为/licence的情况下，会被判定为：缺少路径参数；
	 */
	@ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
	public R server500(RuntimeException exception) {
		exception.printStackTrace();
		return R.fault(RCode.INTERNAL_SERVER_ERROR.code(), RCode.INTERNAL_SERVER_ERROR.msg() + ":" + exception.getMessage());
	}


	/**
	 * 404 错误
	 * <p>
	 * NoHandlerFoundException：请求没找到 首先根据请求Url查找有没有对应的控制器，若没有则会抛该异常
	 */
	@ExceptionHandler({NoHandlerFoundException.class})
	public R request404(NoHandlerFoundException exception) {
		exception.printStackTrace();
		return R.fault(RCode.NOT_FOUND.code(), RCode.NOT_FOUND.msg() + ":" + exception.getMessage());
	}

}
