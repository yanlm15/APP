# APP MakeBigNews
## 成员
    闫力敏 2015011391 （组长）
    张斐然 2015012332
    陈星谷 2015011389
## 概述
    通过实验室提供的API实现新闻类阅读的APP，APP的实现包括UI设计，后台逻辑，应用第三方工具包
    ### UI设计
    +主界面 通过Fregment实现新闻列表的分区展示，并提供搜索接口，实现各种监听响应，以及新闻跳转。
    +搜索结果页面 复用主界面的Fregment实现对搜索结果的分条展示，以及新闻跳转
    +详细新闻页面 显示新闻详细内容，并支持图文混排，相关推荐，以及跳转
    ### 后台逻辑
    + 为各个UI提供相应的功能支持，引用Retrofit 处理网络请求，并提供筛选新闻，屏蔽关键词，文件操作的接口
    ### 第三方包
    +引入讯飞实现语音合成，调用第三方SDK提供微信，QQ，微博的分享功能


## 功能
    ### 离线查看
    +在离线的情况下可以访问之前访问的新闻，并且被标灰
    ### 夜间模式
    +提供正常的夜间模式
    ### 无图模式
    +跳过图片加载实现节省流量
    ### 新闻收藏
    +收藏新文链接
    ### 新闻下载
    +下载后可以离线储存
    ### 语音朗读
    +支持语音朗读新闻内容
## 使用
    +将APP下载到手机，打开后，可通过新闻列表查看具体新闻，也可以搜索感兴趣的新闻
    +浏览详细新闻可以看到相关的推荐新闻
    +通过设置用户可以个性化设计显示模式




<!--   ConstData 用于储存全局变量以及不同界面的信息传递
  Speker 语音
  LogicTool 通过范型接口用于夸类别实现不同的搜索反馈以及筛选功能
  |-需要被继承
  RetrofitTool 通过实现不同的Service 提供不同数据类型的请求 以及对应的失败处理机制
  FileTool 用于下载和访问数据，通过通过范型接口用于不同类别实现不同的下载与访问方式
Service
  NewService 通过id请求单个新闻
  SearchService 通过关键词请求最大量的ID或者依据参数请求（Map）
  UrlService 通过图片链接请求单张图片

UI类
  详细新闻界面显示（show_news_activity）
  |-图文显示
    |-图片
      |-加载 应用了retrofit 框架 每次请求到一张图片就更新界面，未请求到的图片用占位图显示
    |-文字
      |-加载 应用了retrofit 框架 请求成功后调用反馈函数实现界面更新
    |-显示 通过正则表达式处理文本，配合html解析实现图文混排，并实现人物链接
    |-推荐 通过继承LogicTool 重载 onSuccess接口 实现相关内容的搜索，然后通过重载textview的点击函数实现跳转
unit 
  Constdata
  |-like（TreeMap）通过重载比较器实现按照value比较，使用时不需要再次按照value排序 -->
