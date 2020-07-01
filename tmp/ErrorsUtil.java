package com.east.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.east.common.exception.base.BaseRuntimeException;
import com.east.common.helper.StringHelper;
import com.east.common.message.MessageBundle;

public class ErrorsUtil {

	public static final String DEFAULT_CODE = "global";
	public static final String DEFAULT_BASENAME = "errorMessages";

	private static final String[] DEFAULT_BASENAMES = new String[] { DEFAULT_BASENAME };

	public static final void reject(Errors errors, String errorCode,
			String defaultMessage) {

		reject(errors, errorCode, null, defaultMessage);
	}

	public static final void reject(Errors errors, String errorCode,
			Object[] errorArgs, String defaultMessage) {

		String path = getCurrentPath(errors);

		if (path == null) {
            errors.reject(errorCode, errorArgs, defaultMessage);
        } else {
            errors.rejectValue("", errorCode, errorArgs, defaultMessage);
        }
	}

	public static final String getCurrentPath(Errors errors) {
		String path = errors.getNestedPath();

		if (StringHelper.isEmpty(path)) {
            return null;
        } else {
			return path.substring(0, path.length()
					- Errors.NESTED_PATH_SEPARATOR.length());
		}
	}

	public static final boolean hasCurrentPathErrors(Errors errors) {
		String path = getCurrentPath(errors);

		if (path == null) {
            return errors.hasErrors();
        } else {
            return errors.hasFieldErrors(path);
        }
	}
	
	
	//TOTO
	public static final void transferExceptionErrors(MessageBundle msgBundle, BaseRuntimeException e) {
		msgBundle.addErrorMessage(DEFAULT_CODE, null, e.getMessage());
	}

	@SuppressWarnings("unchecked")
	public static final void transferErrors(MessageBundle msgBundle, Errors errors) {

		List<ObjectError> objectErrorList = errors.getGlobalErrors();

		for (ObjectError objectError : objectErrorList) {
			msgBundle.addErrorMessage(objectError.getCode(), objectError
					.getArguments(), objectError.getDefaultMessage());
		}

		List<FieldError> fieldErrorList = errors.getFieldErrors();

		for (FieldError fieldError : fieldErrorList) {
			msgBundle.addErrorMessage(fieldError.getField(), fieldError.getCode(), 
					fieldError.getArguments(), fieldError.getDefaultMessage());
		}
	}

	public static final void transferErrors(TransferInfo[] transferInfos,
			Object target, Errors errors, Errors addErrors, boolean includeGlobal) {

		transferFieldErrors(transferInfos, target, errors, addErrors);

		if (includeGlobal) {
            transferGlobalErrors(target, errors, addErrors);
        }
	}

	public static final void transferErrors(String srcField, String destField,
			Object target, Errors errors, Errors addErrors,
			boolean includeGlobal) {

		transferFieldErrors(srcField, destField, target, errors, addErrors);

		if (includeGlobal) {
            transferGlobalErrors(target, errors, addErrors);
        }
	}

	public static final void transferErrors(String[][] fields, Object target,
			Errors errors, Errors addErrors, boolean includeGlobal) {

		transferFieldErrors(fields, target, errors, addErrors);

		if (includeGlobal) {
            transferGlobalErrors(target, errors, addErrors);
        }
	}

	@SuppressWarnings("unchecked")
	public static final void transferGlobalErrors(Object target, Errors errors,
			Errors addErrors) {

		List<ObjectError> list = addErrors.getGlobalErrors();

		if (!CollectionUtils.isEmpty(list)) {
			BindException bindException = new BindException(target, errors
					.getObjectName());

			for (ObjectError error : list) {
                bindException.addError(error);
            }

			errors.addAllErrors(bindException);
		}
	}

	public static final void transferFieldErrors(TransferInfo transferInfo,
			Object target, Errors errors, Errors addErrors) {

		transferFieldErrors(new TransferInfo[] { transferInfo }, target,
				errors, addErrors);
	}

	public static final void transferFieldErrors(TransferInfo[] transferInfos,
			Object target, Errors errors, Errors addErrors) {

		String[][] fields = new String[transferInfos.length][2];

		for (int i = 0; i < fields.length; i++) {
			fields[i][0] = transferInfos[i].getSource();
			fields[i][1] = transferInfos[i].getDestination();
		}

		transferFieldErrors(fields, target, errors, addErrors);
	}

	public static final void transferFieldErrors(String srcField,
			String destField, Object target, Errors errors, Errors addErrors) {

		TransferInfo transferInfo = new TransferInfo();
		transferInfo.setSource(srcField);
		transferInfo.setDestination(destField);

		transferFieldErrors(transferInfo, target, errors, addErrors);
	}

	@SuppressWarnings("unchecked")
	public static final void transferFieldErrors(String[][] fields,
			Object target, Errors errors, Errors addErrors) {

		BindException bindException = new BindException(target, errors
				.getObjectName());

		String objectName = errors.getObjectName();

		for (String[] field : fields) {
			List<FieldError> list = addErrors.getFieldErrors(field[0]);

			for (FieldError fieldError : list) {
				fieldError = new FieldError(objectName, field[1], fieldError
						.getRejectedValue(), fieldError.isBindingFailure(),
						fieldError.getCodes(), fieldError.getArguments(),
						fieldError.getDefaultMessage());
				bindException.addError(fieldError);
			}
		}

		if (bindException.hasErrors()) {
            errors.addAllErrors(bindException);
        }
	}

	public static final List<String> getAllErrorList(Errors errors) {
		return getAllErrorList(errors, null, (String[]) null);
	}

	public static final List<String> getAllErrorList(Errors errors,
			Locale locale) {

		return getAllErrorList(errors, locale, (String[]) null);
	}

	public static final List<String> getAllErrorList(Errors errors,
			Locale locale, String basename) {

		String[] basenames = basename == null ? null
				: new String[] { basename };

		return getAllErrorList(errors, locale, basenames);
	}

	public static final List<String> getAllErrorList(Errors errors,
			Locale locale, String[] basenames) {

		Map<String, List<String>> fieldMap = getFieldErrorMap(errors, locale,
				basenames);
		List<String> globalList = getGlobalErrorList(errors, locale, basenames);

		ArrayList<String> list = new ArrayList<String>();

		if (!fieldMap.isEmpty()) {
			for (List<String> msgList : fieldMap.values()) {
                list.addAll(msgList);
            }
		}

		list.addAll(globalList);

		return list;
	}

	public static final Map<String, List<String>> getFieldErrorMap(Errors errors) {
		return getFieldErrorMap(errors, null, (String[]) null);
	}

	public static final Map<String, List<String>> getFieldErrorMap(
			Errors errors, Locale locale) {

		return getFieldErrorMap(errors, locale, (String[]) null);
	}

	public static final Map<String, List<String>> getFieldErrorMap(
			Errors errors, Locale locale, String basename) {

		String[] basenames = basename == null ? null
				: new String[] { basename };

		return getFieldErrorMap(errors, locale, basenames);
	}

	@SuppressWarnings("unchecked")
	public static final Map<String, List<String>> getFieldErrorMap(
			Errors errors, Locale locale, String[] basenames) {

		if (locale == null) {
            locale = Locale.CHINA;
        }

		if (basenames == null) {
            basenames = DEFAULT_BASENAMES;
        }

		ResourceBundleMessageSource msgSource = new ResourceBundleMessageSource();
		msgSource.setBasenames(basenames);

		HashMap<String, List<String>> errorMap = new HashMap<String, List<String>>();

		List<FieldError> errorList = errors.getFieldErrors();

		for (FieldError fieldError : errorList) {
			String fieldName = fieldError.getField();
			List<String> list = errorMap.get(fieldName);

			if (list == null) {
				list = new ArrayList<String>();
				errorMap.put(fieldName, list);
			}

			String message = msgSource.getMessage(fieldError.getCode(),
					fieldError.getArguments(), fieldError.getDefaultMessage(),
					locale);

			list.add(message);
		}

		return errorMap;
	}

	@SuppressWarnings("unchecked")
	public static final String[] getFieldErrorMessages(Errors errors,
			String field, Locale locale, String[] basenames) {

		if (locale == null) {
            locale = Locale.CHINA;
        }

		if (basenames == null) {
            basenames = DEFAULT_BASENAMES;
        }

		ResourceBundleMessageSource msgSource = new ResourceBundleMessageSource();
		msgSource.setBasenames(basenames);

		List<FieldError> errorList = errors.getFieldErrors(field);

		if (CollectionUtils.isEmpty(errorList)) {
            return new String[0];
        } else {
			ArrayList<String> list = new ArrayList<String>();

			for (FieldError fieldError : errorList) {
				String message = fieldError.getDefaultMessage();

				list.add(message);
			}

			return list.toArray(new String[list.size()]);
		}
	}

	public static final List<String> getGlobalErrorList(Errors errors) {
		return getGlobalErrorList(errors, null, (String[]) null);
	}

	public static final List<String> getGlobalErrorList(Errors errors,
			Locale locale) {

		return getGlobalErrorList(errors, locale, (String[]) null);
	}

	public static final List<String> getGlobalErrorList(Errors errors,
			Locale locale, String basename) {

		String[] basenames = basename == null ? null
				: new String[] { basename };

		return getGlobalErrorList(errors, locale, basenames);
	}

	@SuppressWarnings("unchecked")
	public static final List<String> getGlobalErrorList(Errors errors,
			Locale locale, String[] basenames) {

		if (locale == null) {
            locale = Locale.CHINA;
        }

		if (basenames == null) {
            basenames = DEFAULT_BASENAMES;
        }

		ResourceBundleMessageSource msgSource = new ResourceBundleMessageSource();
		msgSource.setBasenames(basenames);

		ArrayList<String> list = new ArrayList<String>();

		List<ObjectError> errorList = errors.getGlobalErrors();

		for (ObjectError objError : errorList) {
			String message = objError.getDefaultMessage();

			list.add(message);
		}

		return list;
	}

	public static final String[] getGlobalErrorMessages(Errors errors,
			Locale locale, String[] basenames) {

		List<String> list = getGlobalErrorList(errors, locale, basenames);

		if (CollectionUtils.isEmpty(list)) {
            return new String[0];
        } else {
            return list.toArray(new String[list.size()]);
        }
	}

	public static final BindException getErrorsObject(Object target,
			String objectName) {

		return new BindException(new SupportNullBindingResult(target,
				objectName));
	}

	public static class TransferInfo implements Cloneable, Serializable {

		private static final long serialVersionUID = 5648380576353819783L;

		private String source;
		private String destination;

		public TransferInfo() {
		}

		public TransferInfo(TransferInfo transferInfo) {
			this.source = transferInfo.source;
			this.destination = transferInfo.destination;
		}

		public String getSource() {
			return source;
		}

		public String getDestination() {
			return destination;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public void setDestination(String destination) {
			this.destination = destination;
		}

		public Object clone() {
			return new TransferInfo(this);
		}
	}

	public static class SupportNullBindingResult extends
			BeanPropertyBindingResult {

		private static final long serialVersionUID = 2983454233121033853L;

		public SupportNullBindingResult(Object target, String objectName) {
			super(target, objectName);
		}

		public Object getActualFieldValue(String field) {
			try {
				return super.getActualFieldValue(field);

			} catch (NullValueInNestedPathException nvinpException) {
				return null;
			}
		}
	}
}