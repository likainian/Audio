音视频录制
网络摄像头（ipc）通常具备语音提示功能，还可通过app端向IPC传输一段不超过10s的音频文件来定义语音提示内容。
1，录制一段不超过10s的音频文件，以pcm格式封装并保存为wav格式
2，除语音输入外，还要支持通过输入文字生成音频
3，录制好的音频文件可以点击试听，保存本地和发送给IPC
4,录制界面。文字转音频界面，试听分享界面都是独立的activity,界面布局，自由发挥。

1，使用MVVM设计模式，
2.git管理代码，

项目介绍：
app项目主model

core项目的base相关。

主要展示MVVM和databinding，以及kotlin的使用。
MVVM相较mvp多的是数据和视图的对应关系，从而更彻底的解耦view和逻辑处理。

databing的data设置和自定义adapter都有体现,双向绑定因为没有具体应用场景，在项目中没有。

kotlin主要是一些判断空，高阶函数，以及java调用kotlin时的一些注解。


com.fly.audio.widgets.crop.image文件夹是我之前写的剪切比较能体现面对对象思想：
TransformImageView->CropImageView->GestureCropImageView
之间时继承关系，TransformImageView管理图片变换，CropImageView管理剪切，GestureCropImageView管理手势
是比较复杂的业务逻辑关系。(只截取了部分功能)

说明：网络连接部分，做了一个简单地封装，没有请求。语音合成的也放在录音文件夹了
