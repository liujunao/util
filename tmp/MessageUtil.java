package com.east.common.util;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.Assert;

import com.east.common.helper.ArrayHelper;
import com.east.common.helper.StringHelper;
import com.east.common.message.Message;
import com.east.common.message.MessageBundle;

public class MessageUtil {

	public static final String DEFAULT_BASENAME = "messages";
	public static final String DEFAULT_DELIMITER = "\n";

	public static final Locale DEFAULT_LOCAL = Locale.CHINA;
	
	private MessageUtil() {
	}
	
	public static final String resolveMessages(MessageBundle msgBundle, MessageSource messageSource, Locale locale) {
		Assert.notNull(msgBundle, "Message bundle cannot be null.");

		return resolveMessages(msgBundle.getMessages(), messageSource, locale, DEFAULT_DELIMITER);
	}

	public static final String resolveMessages(MessageBundle msgBundle, MessageSource messageSource, Locale locale, String delimiter) {
		Assert.notNull(msgBundle, "Message bundle cannot be null.");

		return resolveMessages(msgBundle.getMessages(), messageSource, locale, delimiter);
	}

	public static final String resolveMessage(Message message, MessageSource messageSource, Locale locale) {

		return resolveMessage(message, messageSource, locale, false);
	}
	
	/**
	 * Resolve message to String
	 * @param message
	 * @param basenames
	 * @param locale
	 * @param addFieldName whether need to display filed
	 * @return
	 */
	public static final String resolveMessage(Message message,
			MessageSource messageSource, Locale locale, boolean addFieldName) {

		Assert.notNull(message, "Message cannot be null.");

		locale = regulateLocale(locale);

		return doResolveMessage(message, messageSource, locale, addFieldName);
	}

	public static final String resolveMessages(Message[] messages,
			MessageSource messageSource, Locale locale, String delimiter) {

		return resolveMessages(messages, messageSource, locale, delimiter, false);
	}
	
	/**
	 * Resolve message to String
	 * @param messages
	 * @param basenames
	 * @param locale
	 * @param delimiter
	 * @param addFieldName	whether need to display filed
	 * @return
	 */
	public static final String resolveMessages(Message[] messages,
			MessageSource messageSource, Locale locale, String delimiter, boolean addFieldName) {

		Assert.notEmpty(messages, "Messages cannot be empty.");

		locale = regulateLocale(locale);
		delimiter = regulateDelimiter(delimiter);

//		MessageSource messageSource = getMessageSource(basenames);

		String[] resolvedMsgs = new String[messages.length];

		for (int i = 0; i < resolvedMsgs.length; i++) {
            resolvedMsgs[i] = doResolveMessage(messages[i], messageSource, locale, addFieldName);
        }

		return StringHelper.join(resolvedMsgs, delimiter);
	}

//	public static final String[] resolveMessages(Message[] messages,
//			MessageSource messageSource, Locale locale) {
//
//		MessageSource messageSource = StringHelper.isEmpty(basename) ? null
//				: new String[] { basename };
//
//		return resolveMessages(messages, basenames, locale);
//	}

	public static final String[] resolveMessages(Message[] messages,
			MessageSource messageSource, Locale locale) {

		return resolveMessages(messages, messageSource, locale, false);
	}
	
	/**
	 * Resolve message to String array
	 * @param messages
	 * @param basenames
	 * @param locale
	 * @param addFiledName	whether need to display filed
	 * @return
	 */
	public static final String[] resolveMessages(Message[] messages,
			MessageSource messageSource, Locale locale, boolean addFiledName) {

		Assert.notEmpty(messages, "Messages cannot be empty.");

		locale = regulateLocale(locale);

//		AbstractMessageSource messageSource = getMessageSource(basenames);

		String[] resolvedMsgs = new String[messages.length];

		for (int i = 0; i < resolvedMsgs.length; i++) {
            resolvedMsgs[i] = doResolveMessage(messages[i], messageSource, locale, addFiledName);
        }

		return resolvedMsgs;
	}

//	public static final String resolveMessage(String messageCode,
//			MessageSource messageSource, Locale locale) {
//
//		MessageSource messageSource = StringHelper.isEmpty(basename) ? null
//				: new String[] { basename };
//
//		return resolveMessage(messageCode, basenames, locale);
//	}

	public static final String resolveMessage(String messageCode,
			MessageSource messageSource) {

		return resolveMessage(messageCode, messageSource, null);
	}
	
	public static final String resolveMessage(String messageCode,
			MessageSource messageSource, Locale locale) {

		return resolveMessage(messageCode, null, null, messageSource, locale);
	}
	
	public static final String resolveMessage(String messageCode, Object[] args,
			MessageSource messageSource, Locale locale) {

		return resolveMessage(messageCode, args, null, messageSource, locale);
	}
	
	public static final String resolveMessage(String messageCode, String defaultMessage,
			MessageSource messageSource) {

		return resolveMessage(messageCode, null, defaultMessage, messageSource, null);
	}
	
	public static final String resolveMessage(String messageCode, String defaultMessage,
			MessageSource messageSource, Locale locale) {

		return resolveMessage(messageCode, null, defaultMessage, messageSource, locale);
	}
	
	public static final String resolveMessage(String messageCode, Object[] args, String defaultMessage,
			MessageSource messageSource, Locale locale) {

		Assert.notNull(messageCode, "Message Code cannot be null.");

		locale = regulateLocale(locale);
		
		String result = null;
		try {
			result = messageSource.getMessage(messageCode, args, locale);
		} catch (NoSuchMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return defaultMessage;
		}
		
		if (StringHelper.isEmpty(result))
			return defaultMessage;

		return result;
	}
	
	public static final String resolveMessages(String[] messageCodes,
			MessageSource messageSource, Locale locale, String delimiter) {

		Assert.notEmpty(messageCodes, "Messages cannot be empty.");

		locale = regulateLocale(locale);
		delimiter = regulateDelimiter(delimiter);

		String[] resolvedMsgs = new String[messageCodes.length];

		for (int i = 0; i < resolvedMsgs.length; i++) {
			resolvedMsgs[i] = messageSource.getMessage(messageCodes[i], null,
					locale);
		}

		return StringHelper.join(resolvedMsgs, delimiter);
	}

//	public static final String[] resolveMessages(String[] messageCodes,
//			MessageSource messageSource, Locale locale) {
//
//		MessageSource messageSource = StringHelper.isEmpty(basename) ? null
//				: new String[] { basename };
//
//		return resolveMessages(messageCodes, basenames, locale);
//	}

	public static final String[] resolveMessages(String[] messageCodes,
			MessageSource messageSource, Locale locale) {

		Assert.notEmpty(messageCodes, "Messages cannot be empty.");

		locale = regulateLocale(locale);

//		AbstractMessageSource messageSource = getMessageSource(basenames);
//		messageSource.setUseCodeAsDefaultMessage(true);

		String[] resolvedMsgs = new String[messageCodes.length];

		for (int i = 0; i < resolvedMsgs.length; i++) {
			resolvedMsgs[i] = messageSource.getMessage(messageCodes[i], null,
					locale);
		}

		return resolvedMsgs;
	}

	private static final String doResolveMessage(Message message,
			MessageSource messageSource, Locale locale, boolean addFiledName) {
		
		String messageFiled = message.getField();
		String messageCode = message.getCode();
		Object[] arguments = message.getArguments();
		String msg = addFiledName ? "[" + messageFiled + "]" : "";

		try {
			msg += messageSource.getMessage(messageCode, arguments, locale);

		} catch (RuntimeException exception) {
			msg += messageSource.getMessage(messageCode, arguments, message
					.getDefaultMessage(), DEFAULT_LOCAL);
		}
		
		return msg;
	}

//	private static final String[] regulateBasenames(MessageSource messageSource) {
//		return ArrayHelper.isEmpty(basenames) ? DEFAULT_BASENAMES : basenames;
//	}

	private static final Locale regulateLocale(Locale locale) {
		return locale == null ? DEFAULT_LOCAL : locale;
	}

	private static final String regulateDelimiter(String delimiter) {
		return delimiter == null ? DEFAULT_DELIMITER : delimiter;
	}

//	private static final AbstractMessageSource getMessageSource(
//			MessageSource messageSource) {
//
//		basenames = regulateBasenames(basenames);
//		
//		//TODO 获取MessageSource
//		ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(HttpServletRequestReference.getRequest().getServletContext());
//		AbstractMessageSource messageSource = (AbstractMessageSource)appContext.getBean("messageSource");
////		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//		if (messageSource instanceof ResourceBundleMessageSource)
//			((ResourceBundleMessageSource)messageSource).setBasenames(basenames);
//		if (messageSource instanceof ReloadableResourceBundleMessageSource)
//			((ReloadableResourceBundleMessageSource)messageSource).setBasenames(basenames);
//
//		return messageSource;
//	}

	public static final String toString(Message message) {
		StringBuilder buffer = new StringBuilder();

		if (message == null) {
			buffer.append("Message is null.");
		} else {
			buffer.append(message.toString());
			buffer.append("{");

			Character type = message.getType();

			if (type == null) {
                buffer.append("\nType is not specified,");
            } else {
				switch (type.charValue()) {
				case Message.TYPE_ERROR_CHAR:
					buffer.append("\nError Message,");
					break;

				case Message.TYPE_INFO_CHAR:
					buffer.append("\nInfo Message,");
					break;

				case Message.TYPE_WARN_CHAR:
					buffer.append("\nWarn Message,");
					break;

				default:
					buffer.append("\nType = ");
					buffer.append(type);
					buffer.append(",");
				}
			}

			String field = message.getField();

			if (field == null) {
                buffer.append("\nField is not specified,");
            } else {
				buffer.append("\nField = ");
				buffer.append(field);
				buffer.append(",");
			}

			String code = message.getCode();

			if (field == null) {
                buffer.append("\nCode is not specified,");
            } else {
				buffer.append("\nCode = ");
				buffer.append(code);
				buffer.append(",");
			}

			Object[] arguments = message.getArguments();

			if (ArrayHelper.isEmpty(arguments)) {
                buffer.append("\nArguments are not specified,");
            } else {
				buffer.append("\nArguments = ");
				buffer.append(Arrays.toString(arguments));
				buffer.append(",");
			}

			String defaultMsg = message.getDefaultMessage();

			if (defaultMsg == null) {
                buffer.append("\nDefault Message Format is not specified,");
            } else {
				buffer.append("\nDefault Message Format = ");
				buffer.append(defaultMsg);
				buffer.append(",");
			}

//			buffer.append("\nResolved = ");
//			buffer.append(resolveMessage(message, null, null));
			buffer.append("\n}");
		}

		return buffer.toString();
	}

	public static final String toString(Message[] messages) {
		if (ArrayHelper.isEmpty(messages)) {
            return "No messages.";
        } else {
			String[] msgStrings = new String[messages.length];

			for (int i = 0; i < msgStrings.length; i++) {
                msgStrings[i] = toString(messages[i]);
            }

			return StringHelper.join(msgStrings, DEFAULT_DELIMITER);
		}
	}

	public static final String toString(MessageBundle msgBundle) {
		Message[] messages = msgBundle == null ? null : msgBundle.getMessages();

		return toString(messages);
	}

}