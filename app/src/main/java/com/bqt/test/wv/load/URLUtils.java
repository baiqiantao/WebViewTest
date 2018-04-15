package com.bqt.test.wv.load;

import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 里面的内容基本都来自 {@link android.webkit.URLUtil}
 */
public class URLUtils {
	// to refer to bar.png under your package's asset/foo/ directory, use "file:///android_asset/foo/bar.png".
	public static final String ASSET_BASE = "file:///android_asset/";
	
	/* to refer to bar.png under your package's 【res/drawable/】 directory, use "file:///android_res/drawable/bar.png".
	Use "drawable" to refer to"drawable-hdpi" directory as well. */
	public static final String RESOURCE_BASE = "file:///android_res/";
	
	public static final String FILE_BASE = "file://";//内部存储路径为【file:///sdcard/】或【file:///storage/emulated/0/】
	public static final String FILE_BASE_SIMPLE = "file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	public static final String FILE_BASE_SIMPLE2 = "file:///storage/emulated/0/";
	
	public static final String PROXY_BASE = "file:///cookieless_proxy/";
	public static final String CONTENT_BASE = "content:";
	
	public static byte[] decode(byte[] url) throws IllegalArgumentException {
		if (url.length == 0) {
			return new byte[0];
		}
		
		// Create a new byte array with the same length to ensure capacity
		byte[] tempData = new byte[url.length];
		
		int tempCount = 0;
		for (int i = 0; i < url.length; i++) {
			byte b = url[i];
			if (b == '%') {
				if (url.length - i > 2) {
					b = (byte) (parseHex(url[i + 1]) * 16 + parseHex(url[i + 2]));
					i += 2;
				} else {
					throw new IllegalArgumentException("Invalid format");
				}
			}
			tempData[tempCount++] = b;
		}
		byte[] retData = new byte[tempCount];
		System.arraycopy(tempData, 0, retData, 0, tempCount);
		return retData;
	}
	
	/**
	 * @return True iff当且仅当 the url is correctly URL encoded
	 */
	static boolean verifyURLEncoding(String url) {
		int count = url.length();
		if (count == 0) {
			return false;
		}
		
		int index = url.indexOf('%');
		while (index >= 0 && index < count) {
			if (index < count - 2) {
				try {
					parseHex((byte) url.charAt(++index));
					parseHex((byte) url.charAt(++index));
				} catch (IllegalArgumentException e) {
					return false;
				}
			} else {
				return false;
			}
			index = url.indexOf('%', index + 1);
		}
		return true;
	}
	
	private static int parseHex(byte b) {
		if (b >= '0' && b <= '9') return (b - '0');
		if (b >= 'A' && b <= 'F') return (b - 'A' + 10);
		if (b >= 'a' && b <= 'f') return (b - 'a' + 10);
		
		throw new IllegalArgumentException("Invalid hex char '" + b + "'");
	}
	
	/**
	 * @return True iff the url is an asset file.
	 */
	public static boolean isAssetUrl(String url) {
		return (null != url) && url.startsWith(ASSET_BASE);
	}
	
	/**
	 * @return True iff the url is a resource file.
	 */
	public static boolean isResourceUrl(String url) {
		return (null != url) && url.startsWith(RESOURCE_BASE);
	}
	
	/**
	 * @return True iff the url is a local file.
	 */
	public static boolean isFileUrl(String url) {
		return (null != url) && (url.startsWith(FILE_BASE) && !url.startsWith(ASSET_BASE) && !url.startsWith(PROXY_BASE));
	}
	
	/**
	 * @return True iff the url is an about: url.
	 */
	public static boolean isAboutUrl(String url) {
		return (null != url) && url.startsWith("about:");
	}
	
	/**
	 * @return True iff the url is a data: url.
	 */
	public static boolean isDataUrl(String url) {
		return (null != url) && url.startsWith("data:");
	}
	
	/**
	 * @return True iff the url is a javascript: url.
	 */
	public static boolean isJavaScriptUrl(String url) {
		return (null != url) && url.startsWith("javascript:");
	}
	
	/**
	 * @return True iff the url is an http: url.
	 */
	public static boolean isHttpUrl(String url) {
		return null != url && url.length() > 6 && url.substring(0, 7).equalsIgnoreCase("http://");
	}
	
	/**
	 * @return True iff the url is an https: url.
	 */
	public static boolean isHttpsUrl(String url) {
		return null != url && url.length() > 7 && url.substring(0, 8).equalsIgnoreCase("https://");
	}
	
	/**
	 * @return True iff the url is a network url.
	 */
	public static boolean isNetworkUrl(String url) {
		return null != url && url.length() != 0 && (isHttpUrl(url) || isHttpsUrl(url));
	}
	
	/**
	 * @return True iff the url is a content: url.
	 */
	public static boolean isContentUrl(String url) {
		return null != url && url.startsWith(CONTENT_BASE);
	}
	
	/**
	 * @return True iff the url is valid.
	 */
	public static boolean isValidUrl(String url) {
		return null != url && url.length() != 0 &&
				(isAssetUrl(url) || isResourceUrl(url) || isFileUrl(url) || isAboutUrl(url) ||
						isHttpUrl(url) || isHttpsUrl(url) || isJavaScriptUrl(url) || isContentUrl(url));
	}
	
	/**
	 * Strips the url of the anchor.  剥离锚点的网址。
	 */
	public static String stripAnchor(String url) {
		int anchorIndex = url.indexOf('#');
		if (anchorIndex != -1) {
			return url.substring(0, anchorIndex);
		}
		return url;
	}
	
	/**
	 * Guesses canonical规范的、典型的 filename that a download would have, using the URL and contentDisposition.
	 * File extension, if not defined, is added based on the mimetype
	 *
	 * @param url                Url to the content
	 * @param contentDisposition Content-Disposition HTTP header or null
	 * @param mimeType           Mime-type of the content or null
	 * @return suggested filename
	 */
	public static String guessFileName(String url, String contentDisposition, String mimeType) {
		String filename = null;
		String extension = null;
		
		// If we couldn't do anything with the hint, move toward the content disposition
		if (contentDisposition != null) {
			filename = parseContentDisposition(contentDisposition);
			if (filename != null) {
				int index = filename.lastIndexOf('/') + 1;
				if (index > 0) {
					filename = filename.substring(index);
				}
			}
		}
		
		// If all the other http-related approaches方法 failed, use the plain uri
		if (filename == null) {
			String decodedUrl = Uri.decode(url);
			if (decodedUrl != null) {
				int queryIndex = decodedUrl.indexOf('?');
				// If there is a query string strip it, same as desktop browsers
				if (queryIndex > 0) {
					decodedUrl = decodedUrl.substring(0, queryIndex);
				}
				if (!decodedUrl.endsWith("/")) {
					int index = decodedUrl.lastIndexOf('/') + 1;
					if (index > 0) {
						filename = decodedUrl.substring(index);
					}
				}
			}
		}
		
		// Finally, if couldn't get filename from URI, get a generic filename
		if (filename == null) {
			filename = "downloadfile";
		}
		
		// Split filename between base and extension. Add an extension if filename does not have one
		int dotIndex = filename.indexOf('.');
		if (dotIndex < 0) {
			if (mimeType != null) {
				extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
				if (extension != null) {
					extension = "." + extension;
				}
			}
			if (extension == null) {
				if (mimeType != null && mimeType.toLowerCase(Locale.ROOT).startsWith("text/")) {
					if (mimeType.equalsIgnoreCase("text/html")) {
						extension = ".html";
					} else {
						extension = ".txt";
					}
				} else {
					extension = ".bin";
				}
			}
		} else {
			if (mimeType != null) {
				// Compare the last segment部分 of the extension against the mime type.
				// If there's a mismatch, discard丢弃 the entire整个 extension.
				int lastDotIndex = filename.lastIndexOf('.');
				String typeFromExt = MimeTypeMap.getSingleton().getMimeTypeFromExtension(filename.substring(lastDotIndex + 1));
				if (typeFromExt != null && !typeFromExt.equalsIgnoreCase(mimeType)) {
					extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
					if (extension != null) {
						extension = "." + extension;
					}
				}
			}
			if (extension == null) {
				extension = filename.substring(dotIndex);
			}
			filename = filename.substring(0, dotIndex);
		}
		
		return filename + extension;
	}
	
	/**
	 * Regex used to parse content-disposition headers
	 */
	private static final Pattern CONTENT_DISPOSITION_PATTERN =
			Pattern.compile("attachment;\\s*filename\\s*=\\s*(\"?)([^\"]*)\\1\\s*$", Pattern.CASE_INSENSITIVE);
	
	/*
	 * Parse the Content-Disposition HTTP Header.
	 * The format of the header is defined here: http://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html
	 * This header provides a filename for content that is going to be downloaded to the file system.
	 * We only support the attachment附件 type.
	 * Note that RFC 2616 specifies the filename value must be double-quoted双引号。.
	 * Unfortunately不幸的是 some servers服务器 do not quote the value为这个值加引号
	 * so to maintain以保持 consistent一致的 behaviour with other browsers浏览器, we allow同样支持 unquoted values too.
	 */
	static String parseContentDisposition(String contentDisposition) {
		try {
			Matcher m = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition);
			if (m.find()) {
				return m.group(2);
			}
		} catch (IllegalStateException ex) {
			// This function is defined as returning null when it can't parse the header 该函数被定义为在无法解析头时返回null
		}
		return null;
	}
}