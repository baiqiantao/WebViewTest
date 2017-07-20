package test.bqt.com.webviewtest;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebSettings;

public class WebViewModel implements Parcelable {
	//这些搜索引擎都有大量大量的参数可以配置
	public static final String SEARCH_TYPE_BAIDU = "https://www.baidu.com/s?ie=UTF-8&wd=";
	public static final String SEARCH_TYPE_GOOGLE = "https://www.google.com.hk/#safe=strict&q=";
	public static final String SEARCH_TYPE_GITHUB = "https://github.com/search?utf8=%E2%9C%93&q=";

	public String title;
	public String url;
	public String searchType;//Baidu，GitHub，Google等
	public boolean showProgressBar;

	//***************************************************************************************************************
	//                                                                   以下为WebSettings中的同名方法
	//***************************************************************************************************************
	public boolean setBuiltInZoomControls;
	public boolean setDisplayZoomControls;
	public boolean setJavaScriptEnabled;
	public boolean setJavaScriptCanOpenWindowsAutomatically;
	public boolean setUseWideViewPort;
	public boolean setLoadWithOverviewMode;
	public boolean setAppCacheEnabled;
	public boolean setDatabaseEnabled;
	public boolean setDomStorageEnabled;
	public boolean setSupportMultipleWindows;
	public boolean setSupportZoom;
	public boolean setGeolocationEnabled;
	public int setCacheMode;
	public WebSettings.LayoutAlgorithm setLayoutAlgorithm;

	private WebViewModel(Builder builder) {
		searchType = builder.searchType;
		title = builder.title;
		url = builder.url;
		showProgressBar = builder.showProgressBar;
		setBuiltInZoomControls = builder.setBuiltInZoomControls;
		setDisplayZoomControls = builder.setDisplayZoomControls;
		setJavaScriptEnabled = builder.setJavaScriptEnabled;
		setJavaScriptCanOpenWindowsAutomatically = builder.setJavaScriptCanOpenWindowsAutomatically;
		setUseWideViewPort = builder.setUseWideViewPort;
		setLoadWithOverviewMode = builder.setLoadWithOverviewMode;
		setAppCacheEnabled = builder.setAppCacheEnabled;
		setDatabaseEnabled = builder.setDatabaseEnabled;
		setDomStorageEnabled = builder.setDomStorageEnabled;
		setSupportMultipleWindows = builder.setSupportMultipleWindows;
		setSupportZoom = builder.setSupportZoom;
		setGeolocationEnabled = builder.setGeolocationEnabled;
		setCacheMode = builder.setCacheMode;
		setLayoutAlgorithm = builder.setLayoutAlgorithm;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@Override
	public String toString() {
		return "WebViewModel{" +
				"searchType='" + searchType + '\'' +
				", title='" + title + '\'' +
				", url='" + url + '\'' +
				", showProgressBar=" + showProgressBar +
				", setBuiltInZoomControls=" + setBuiltInZoomControls +
				", setDisplayZoomControls=" + setDisplayZoomControls +
				", setJavaScriptEnabled=" + setJavaScriptEnabled +
				", setJavaScriptCanOpenWindowsAutomatically=" + setJavaScriptCanOpenWindowsAutomatically +
				", setUseWideViewPort=" + setUseWideViewPort +
				", setLoadWithOverviewMode=" + setLoadWithOverviewMode +
				", setAppCacheEnabled=" + setAppCacheEnabled +
				", setDatabaseEnabled=" + setDatabaseEnabled +
				", setDomStorageEnabled=" + setDomStorageEnabled +
				", setSupportMultipleWindows=" + setSupportMultipleWindows +
				", setSupportZoom=" + setSupportZoom +
				", setGeolocationEnabled=" + setGeolocationEnabled +
				", setCacheMode=" + setCacheMode +
				", setLayoutAlgorithm=" + setLayoutAlgorithm +
				'}';
	}

	public static final class Builder {
		//***************************************************************************************************************
		//                                                                             在这里设置默认值
		//***************************************************************************************************************
		private String title = "默认的TITLE";
		private String url = "https://github.com/baiqiantao";
		private boolean showProgressBar = true;
		private String searchType = "baidu";
		//以下默认值均是WebSettings中同名get方法获取到的默认值
		private boolean setAppCacheEnabled = false;//应用缓存API可用
		private boolean setBuiltInZoomControls = false;//使用内置的缩放机制，设为false时setDisplayZoomControls无效
		private int setCacheMode = WebSettings.LOAD_DEFAULT;//使用缓存的方式，默认值LOAD_DEFAULT，LOAD_NO_CACHE
		private boolean setDatabaseEnabled = false;//数据库存储API可用
		private boolean setDisplayZoomControls = true;//使用内置的缩放机制时是否展示缩放控件
		private boolean setDomStorageEnabled = false;//DOM存储API可用
		private boolean setGeolocationEnabled = true;
		private boolean setJavaScriptCanOpenWindowsAutomatically = false;//让JavaScript自动打开窗口
		private boolean setJavaScriptEnabled = false;//允许js交互
		private boolean setLoadWithOverviewMode = false;//允许缩小内容以适应屏幕宽度
		private boolean setSupportMultipleWindows = false;//支持多窗口。如果设置为true，主程序要实现onCreateWindow
		private boolean setSupportZoom = true;
		private boolean setUseWideViewPort = false;//重要！布局的宽度总是与WebView控件上的设备无关像素宽度一致
		private WebSettings.LayoutAlgorithm setLayoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS;//设置布局类型
		//会引起WebView重新布局。默认值NARROW_COLUMNS（适应内容大小），SINGLE_COLUMN（适应屏幕，内容将自动缩放）

		private Builder() {
		}

		public Builder title(String val) {
			title = val;
			return this;
		}

		public Builder url(String val) {
			url = val;
			return this;
		}

		public Builder showProgressBar(boolean val) {
			showProgressBar = val;
			return this;
		}

		public Builder searchType(String val) {
			searchType = val;
			return this;
		}

		public Builder setBuiltInZoomControls(boolean val) {
			setBuiltInZoomControls = val;
			return this;
		}

		public Builder setDisplayZoomControls(boolean val) {
			setDisplayZoomControls = val;
			return this;
		}

		public Builder setJavaScriptEnabled(boolean val) {
			setJavaScriptEnabled = val;
			return this;
		}

		public Builder setJavaScriptCanOpenWindowsAutomatically(boolean val) {
			setJavaScriptCanOpenWindowsAutomatically = val;
			return this;
		}

		public Builder setUseWideViewPort(boolean val) {
			setUseWideViewPort = val;
			return this;
		}

		public Builder setLoadWithOverviewMode(boolean val) {
			setLoadWithOverviewMode = val;
			return this;
		}

		public Builder setAppCacheEnabled(boolean val) {
			setAppCacheEnabled = val;
			return this;
		}

		public Builder setDatabaseEnabled(boolean val) {
			setDatabaseEnabled = val;
			return this;
		}

		public Builder setDomStorageEnabled(boolean val) {
			setDomStorageEnabled = val;
			return this;
		}

		public Builder setSupportMultipleWindows(boolean val) {
			setSupportMultipleWindows = val;
			return this;
		}

		public Builder setSupportZoom(boolean val) {
			setSupportZoom = val;
			return this;
		}

		public Builder setGeolocationEnabled(boolean val) {
			setGeolocationEnabled = val;
			return this;
		}

		public Builder setCacheMode(int val) {
			setCacheMode = val;
			return this;
		}

		public Builder setLayoutAlgorithm(WebSettings.LayoutAlgorithm val) {
			setLayoutAlgorithm = val;
			return this;
		}

		public WebViewModel build() {
			return new WebViewModel(this);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeString(this.url);
		dest.writeString(this.searchType);
		dest.writeByte(this.showProgressBar ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setBuiltInZoomControls ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setDisplayZoomControls ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setJavaScriptEnabled ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setJavaScriptCanOpenWindowsAutomatically ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setUseWideViewPort ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setLoadWithOverviewMode ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setAppCacheEnabled ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setDatabaseEnabled ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setDomStorageEnabled ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setSupportMultipleWindows ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setSupportZoom ? (byte) 1 : (byte) 0);
		dest.writeByte(this.setGeolocationEnabled ? (byte) 1 : (byte) 0);
		dest.writeInt(this.setCacheMode);
		dest.writeInt(this.setLayoutAlgorithm == null ? -1 : this.setLayoutAlgorithm.ordinal());
	}

	protected WebViewModel(Parcel in) {
		this.title = in.readString();
		this.url = in.readString();
		this.searchType = in.readString();
		this.showProgressBar = in.readByte() != 0;
		this.setBuiltInZoomControls = in.readByte() != 0;
		this.setDisplayZoomControls = in.readByte() != 0;
		this.setJavaScriptEnabled = in.readByte() != 0;
		this.setJavaScriptCanOpenWindowsAutomatically = in.readByte() != 0;
		this.setUseWideViewPort = in.readByte() != 0;
		this.setLoadWithOverviewMode = in.readByte() != 0;
		this.setAppCacheEnabled = in.readByte() != 0;
		this.setDatabaseEnabled = in.readByte() != 0;
		this.setDomStorageEnabled = in.readByte() != 0;
		this.setSupportMultipleWindows = in.readByte() != 0;
		this.setSupportZoom = in.readByte() != 0;
		this.setGeolocationEnabled = in.readByte() != 0;
		this.setCacheMode = in.readInt();
		int tmpSetLayoutAlgorithm = in.readInt();
		this.setLayoutAlgorithm = tmpSetLayoutAlgorithm == -1 ? null : WebSettings.LayoutAlgorithm.values()[tmpSetLayoutAlgorithm];
	}

	public static final Creator<WebViewModel> CREATOR = new Creator<WebViewModel>() {
		@Override
		public WebViewModel createFromParcel(Parcel source) {
			return new WebViewModel(source);
		}

		@Override
		public WebViewModel[] newArray(int size) {
			return new WebViewModel[size];
		}
	};
}