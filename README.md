# APP
工具类
  ConstData 用于储存全局变量以及不同界面的信息传递
  Speker 语音
  LogicTool 通过范型接口用于夸类别实现不同的搜索反馈以及筛选功能
  |-需要被继承
  RetrofitTool 通过实现不同的Service 提供不同数据类型的请求 以及对应的失败处理机制
  +FileTool 用于下载和访问数据，通过通过范型接口用于不同类别实现不同的下载与访问方式（建议，或者打算）
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
  |-like（TreeMap）通过重载比较器实现按照value比较，使用时不需要再次按照value排序
