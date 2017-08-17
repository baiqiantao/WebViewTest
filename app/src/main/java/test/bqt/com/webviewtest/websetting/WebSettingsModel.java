package test.bqt.com.webviewtest.websetting;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebSettings;

public class WebSettingsModel implements Parcelable {
	//这些搜索引擎都有大量大量的参数可以配置
	public static final String SEARCH_TYPE_BAIDU = "https://www.baidu.com/s?ie=UTF-8&wd=";
	public static final String SEARCH_TYPE_GOOGLE = "https://www.google.com.hk/#safe=strict&q=";
	public static final String SEARCH_TYPE_GITHUB = "https://github.com/search?utf8=%E2%9C%93&q=";
	
	//这些事WebSettings中为setMixedContentMode方法提供的常量，因为只能在5.0以上使用，所以我把他们拷到了这里
	public static final int MIXED_CONTENT_ALWAYS_ALLOW = 0;
	public static final int MIXED_CONTENT_NEVER_ALLOW = 1;
	public static final int MIXED_CONTENT_COMPATIBILITY_MODE = 2;
	
	public String title;
	public String url;
	public String searchType;//Baidu，GitHub，Google等
	public boolean showHorizontalPB;
	public boolean showCenterPB;
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
	public int setMinimumFontSize;
	public int setMixedContentMode;
	public int setTextZoom;
	public WebSettings.LayoutAlgorithm setLayoutAlgorithm;
	
	private WebSettingsModel(Builder builder) {
		searchType = builder.searchType;
		title = builder.title;
		url = builder.url;
		showHorizontalPB = builder.showHorizontalPB;
		showCenterPB = builder.showCenterPB;
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
		setMinimumFontSize = builder.setMinimumFontSize;
		setMixedContentMode = builder.setMixedContentMode;
		setTextZoom = builder.setTextZoom;
		setLayoutAlgorithm = builder.setLayoutAlgorithm;
	}
	
	public static Builder newBuilder() {
		return new Builder();
	}
	
	@Override
	public String toString() {
		return "WebSettingsModel{" +
				"searchType='" + searchType + '\'' +
				", title='" + title + '\'' +
				", url='" + url + '\'' +
				", showHorizontalPB=" + showHorizontalPB +
				", showCenterPB=" + showCenterPB +
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
				", setMinimumFontSize=" + setMinimumFontSize +
				", setMixedContentMode=" + setMixedContentMode +
				", setTextZoom=" + setTextZoom +
				", setLayoutAlgorithm=" + setLayoutAlgorithm +
				'}';
	}
	
	public static final class Builder {
		//***************************************************************************************************************
		//                                                                             在这里设置默认值
		//***************************************************************************************************************
		private String title = "默认的TITLE";
		private String url = "https://github.com/baiqiantao";
		private boolean showHorizontalPB = false;
		private boolean showCenterPB = false;
		private String searchType = "baidu";
		//以下默认值均是WebSettings中同名get方法获取到的默认值
		private boolean setAppCacheEnabled = false;//应用缓存API可用(启动应用缓存)
		private boolean setBuiltInZoomControls = false;//使用内置的缩放机制，包括屏幕上的缩放控件和双指缩放手势
		private int setCacheMode = WebSettings.LOAD_DEFAULT;//使用缓存的方式，默认值LOAD_DEFAULT，LOAD_NO_CACHE
		private boolean setDatabaseEnabled = false;//数据库存储API可用
		private boolean setDisplayZoomControls = true;//是否显示缩放控件。setDisplayZoomControls设为true时才有效
		private boolean setDomStorageEnabled = false;//DOM存储API可用
		private boolean setGeolocationEnabled = true;//定位可用。需要有定位权限和实现onGeolocationPermissionsShowPrompt回调方法
		private boolean setJavaScriptCanOpenWindowsAutomatically = false;//让js自动打开窗口，适用于js方法window.open()
		private boolean setJavaScriptEnabled = false;//允许js交互
		private boolean setLoadWithOverviewMode = false;//是否在概览模式下加载页面，也就是缩放内容以适应屏幕宽度
		private int setMinimumFontSize = 8;//设置最小的字号，默认为8
		private int setMixedContentMode = WebSettingsModel.MIXED_CONTENT_NEVER_ALLOW;//设置混合模式
		//5.0开始默认的混合模式为MIXED_CONTENT_NEVER_ALLOW，即不允许使用混合模式，即https中不能加载http资源
		private boolean setSupportMultipleWindows = false;//支持多窗口。如果设置为true，需要实现onCreateWindow回调方法
		private boolean setSupportZoom = true;//是否支持使用屏幕上的缩放控件和手势进行缩放
		private int setTextZoom = 100;//设置页面上的文本缩放百分比，默认100
		private boolean setUseWideViewPort = false;//重要！布局的宽度总是与WebView控件上的设备无关像素宽度一致
		private WebSettings.LayoutAlgorithm setLayoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS;//设置布局
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
		
		public Builder showHorizontalPB(boolean val) {
			showHorizontalPB = val;
			return this;
		}
		
		public Builder showCenterPB(boolean val) {
			showCenterPB = val;
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
		
		public Builder setMinimumFontSize(int val) {
			setMinimumFontSize = val;
			return this;
		}
		
		public Builder setMixedContentMode(int val) {
			setMixedContentMode = val;
			return this;
		}
		
		public Builder setTextZoom(int val) {
			setTextZoom = val;
			return this;
		}
		
		public Builder setLayoutAlgorithm(WebSettings.LayoutAlgorithm val) {
			setLayoutAlgorithm = val;
			return this;
		}
		
		public WebSettingsModel build() {
			return new WebSettingsModel(this);
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
		dest.writeByte(this.showHorizontalPB ? (byte) 1 : (byte) 0);
		dest.writeByte(this.showCenterPB ? (byte) 1 : (byte) 0);
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
		dest.writeInt(this.setMinimumFontSize);
		dest.writeInt(this.setMixedContentMode);
		dest.writeInt(this.setTextZoom);
		dest.writeInt(this.setLayoutAlgorithm == null ? -1 : this.setLayoutAlgorithm.ordinal());
	}
	
	protected WebSettingsModel(Parcel in) {
		this.title = in.readString();
		this.url = in.readString();
		this.searchType = in.readString();
		this.showHorizontalPB = in.readByte() != 0;
		this.showCenterPB = in.readByte() != 0;
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
		this.setMinimumFontSize = in.readInt();
		this.setMixedContentMode = in.readInt();
		this.setTextZoom = in.readInt();
		int tmpSetLayoutAlgorithm = in.readInt();
		this.setLayoutAlgorithm = tmpSetLayoutAlgorithm == -1 ? null : WebSettings.LayoutAlgorithm.values()[tmpSetLayoutAlgorithm];
	}
	
	public static final Creator<WebSettingsModel> CREATOR = new Creator<WebSettingsModel>() {
		@Override
		public WebSettingsModel createFromParcel(Parcel source) {
			return new WebSettingsModel(source);
		}
		
		@Override
		public WebSettingsModel[] newArray(int size) {
			return new WebSettingsModel[size];
		}
	};
}