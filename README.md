# switchView
![](https://github.com/799536960/switchView/blob/master/20170524104352-7c011ad592.%5Bgif-2-mp4.com%5D.gif)  


```java
<com.duma.ld.mylibrary.SwitchView
        android:layout_width="200dp"
        android:layout_height="50dp"
        app:time="500"//动画时间
        app:bgColor="#FFE0B2"//背景色
        app:leftColor="#8cc152"//左边颜色
        app:rightColor="#757575"//右边颜色
        app:setChecked="true"//true 是在右边 反之
        app:textLeft="预约车位"//左边text
        app:textLeftColor="#8cc152"//左边字的颜色
        app:textRight="我有车位"//同上
        app:textRightColor="#8cc152" />//同上
```
用法
```java
compile 'com.ld:switchView:1.1.5'
 ```
 
 点击监听
```java
  switchView.setOnClickCheckedListener(new SwitchView.onClickCheckedListener() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this, "type:" + switchView.isChecked(), Toast.LENGTH_SHORT).show();
            }
        });
```
有问题需要 欢迎留言~  
