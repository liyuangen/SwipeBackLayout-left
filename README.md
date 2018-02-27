[![](https://jitpack.io/v/liyuangen/SwipeBackLayout-left.svg)](https://jitpack.io/#liyuangen/SwipeBackLayout-left)

```

allprojects {
	repositories {
		...
		maven {url'https://jitpack.io'}
	}
}

```
	

```

dependencies {
	compile 'com.github.liyuangen:SwipeBackLayout-left:1.0'
}

```


# SwipeBackLayout-left
根据SwipeBackLayout自己定制的只有左滑返回的SwipeBackLayout
注意事项：

1.主界面不能设置android:windowIsTranslucent=“true”也不要继承SwipeBackLayout

2.要侧滑返回的页面继承SwipeBackLayout 设置android:windowIsTranslucent=“true”

该功能只是简单的左滑返回，具体的请参考https://github.com/ikew0ng/SwipeBackLayout
