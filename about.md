1. 隐蔽性策略：代码伪装与分散 (Covertness Strategy: Code Obfuscation and Dispersal - PAUA-3)
为确保代码无法被轻易识别为恶意负载，需要进行多层伪装：

方法名和变量名伪装 (Semantic Camouflage): 使用与核心业务逻辑或正常日志/配置/初始化相关的名称。例如，将 I/O 操作伪装成“数据预热 (Data Pre-fetching)”或“缓存初始化 (Cache Initialization)”。

代码分散 (Code Dispersal): 不将所有负载逻辑集中在一个方法中。将 CPU 负载、I/O 负载、线程负载分散到不同的类和方法中，并在系统的初始化阶段、不频繁的后台任务或生命周期钩子中被看似不相关的代码调用。

常量和计算伪装 (Calculation Obfuscation): 避免使用硬编码的随机数。将负载参数（如文件大小、线程数、延迟时间）封装在复杂的、看似用于计算系统常数或校验和的方法中。

2. Java 隐蔽性实现与资源消耗模块 (Covert Java Implementation Modules)
我们使用 Java 实现三个核心模块：参数生成 (Parameters)、I/O 负载、线程负载。

A. 参数伪装与生成模块 (Covert Parameter Generation)
该模块将负载参数伪装成基于系统时间、环境常数或哈希计算得出的“配置校验码 (Configuration Checksum)”。