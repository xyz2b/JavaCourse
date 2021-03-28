## GC总结

### 各GC配置
```
-XX:+UseSerialGC 串行化GC
-XX:+UseParNewGC 改进版的Serial GC(并行)，可以配合CMS使用

-XX:+UseParallelGC	并行GC，下面三个参数同等效果
-XX:+UseParallelOldGC
-XX:+UseParallelGC -XX:+UseParallelOldGC
-XX:ParallelGCThreads=N 指定GC线程数

-XX:+UseConcMarkSweepGC 	CMS GC


-XX:+UseG1GC		G1 GC
-XX:MaxGCPauseMillis=50


-XX:+UnlockExperimentalVMOptions -XX:+UseZGC		ZGC(Linux Java11)

-XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC    Shenandoah(Linux Java11)
```

### 如何查看、分析不同GC配置下的日志信息
1.串行GC(年轻代为serialGC，老年代为serialOldGC)
```
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx500m -Xms500m -XX:+UseSerialGC -Xloggc:serialGC.log GCLogAnalysis

2次FullGC

# JVM版本		JRE版本			JVM编译环境
Java HotSpot(TM) 64-Bit Server VM (25.281-b09) for bsd-amd64 JRE (1.8.0_281-b09), built on Dec  9 2020 12:44:49 by "java_re" with gcc 4.2.1 Compatible Apple LLVM 10.0.0 (clang-1000.11.45.5)
# 物理内存: 页大小, 物理内存总大小(剩余多少可用)
Memory: 4k page, physical 16777216k(137352k free)

/proc/meminfo:

# java启动命令行参数
CommandLine flags: -XX:InitialHeapSize=524288000 -XX:MaxHeapSize=524288000 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseSerialGC

# GC日志(三种不同的日志)
# 只有年轻代GC
# GC发生的时间	[GC堆内存变化情况 GC(GC原因:分配失败)  [DefNew(年轻代,标识使用的算法为SerialGC): 年轻代堆内存的变化(年轻代堆内存总大小) 年轻代GC暂时时间] 整堆内存的变化(整堆内存总大小) 整堆GC暂停时间] [GC CPU使用情况]
2021-03-26T09:32:08.444-0800: 0.228: [GC (Allocation Failure) 2021-03-26T09:32:08.444-0800: 0.228: [DefNew: 136576K->17024K(153600K), 0.0223483 secs] 136576K->43508K(494976K), 0.0224473 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]
2021-03-26T09:32:08.485-0800: 0.269: [GC (Allocation Failure) 2021-03-26T09:32:08.485-0800: 0.269: [DefNew: 153600K->17023K(153600K), 0.0307965 secs] 180084K->90903K(494976K), 0.0308554 secs] [Times: user=0.02 sys=0.01, real=0.03 secs]
2021-03-26T09:32:08.530-0800: 0.315: [GC (Allocation Failure) 2021-03-26T09:32:08.530-0800: 0.315: [DefNew: 153599K->17023K(153600K), 0.0249825 secs] 227479K->133950K(494976K), 0.0250447 secs] [Times: user=0.02 sys=0.01, real=0.02 secs]
2021-03-26T09:32:08.578-0800: 0.362: [GC (Allocation Failure) 2021-03-26T09:32:08.578-0800: 0.362: [DefNew: 153599K->17023K(153600K), 0.0332754 secs] 270526K->179000K(494976K), 0.0333591 secs] [Times: user=0.01 sys=0.02, real=0.04 secs]
2021-03-26T09:32:08.626-0800: 0.410: [GC (Allocation Failure) 2021-03-26T09:32:08.626-0800: 0.410: [DefNew: 153599K->17023K(153600K), 0.0227812 secs] 315576K->224673K(494976K), 0.0228731 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]
2021-03-26T09:32:08.663-0800: 0.447: [GC (Allocation Failure) 2021-03-26T09:32:08.663-0800: 0.447: [DefNew: 153599K->17022K(153600K), 0.0249392 secs] 361249K->269819K(494976K), 0.0250197 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]
2021-03-26T09:32:08.703-0800: 0.487: [GC (Allocation Failure) 2021-03-26T09:32:08.703-0800: 0.487: [DefNew: 153414K->17023K(153600K), 0.0232416 secs] 406210K->312444K(494976K), 0.0233244 secs] [Times: user=0.01 sys=0.01, real=0.02 secs]

# 年轻代和老年代GC
# GC发生的时间	[GC堆内存变化情况 GC(GC原因:分配失败)  [DefNew(年轻代,标识使用的算法为SerialGC): 年轻代堆内存的变化(年轻代堆内存总大小) 年轻代GC暂时时间] [Tenured(老年代,标识使用的算法是serialOldGC) 老年代堆内存的变化(老年代堆内存总大小) 老年代GC暂时停时间] 整堆内存变化(整堆大小), [Metaspace(元数据区) 元数据区堆内存变化(元数据区堆内存总大小)] 整堆GC暂时时间]  [GC CPU使用情况]
2021-03-26T09:32:08.740-0800: 0.524: [GC (Allocation Failure) 2021-03-26T09:32:08.740-0800: 0.524: [DefNew: 153599K->153599K(153600K), 0.0000158 secs]2021-03-26T09:32:08.740-0800: 0.524: [Tenured: 295420K->259402K(341376K), 0.0469450 secs] 449020K->259402K(494976K), [Metaspace: 2570K->2570K(1056768K)], 0.0470561 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
2021-03-26T09:32:08.805-0800: 0.589: [GC (Allocation Failure) 2021-03-26T09:32:08.805-0800: 0.589: [DefNew: 136576K->17022K(153600K), 0.0068741 secs] 395978K->302059K(494976K), 0.0069433 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
2021-03-26T09:32:08.831-0800: 0.615: [GC (Allocation Failure) 2021-03-26T09:32:08.847-0800: 0.632: [DefNew: 153598K->17019K(153600K), 0.0260644 secs] 438635K->341846K(494976K), 0.0430238 secs] [Times: user=0.01 sys=0.01, real=0.04 secs]
2021-03-26T09:32:08.891-0800: 0.675: [GC (Allocation Failure) 2021-03-26T09:32:08.891-0800: 0.675: [DefNew: 153595K->153595K(153600K), 0.0000199 secs]2021-03-26T09:32:08.891-0800: 0.675: [Tenured: 324827K->294760K(341376K), 0.0426505 secs] 478422K->294760K(494976K), [Metaspace: 2570K->2570K(1056768K)], 0.0427866 secs] [Times: user=0.04 sys=0.00, real=0.04 secs]
2021-03-26T09:32:08.955-0800: 0.739: [GC (Allocation Failure) 2021-03-26T09:32:08.955-0800: 0.739: [DefNew: 136576K->17023K(153600K), 0.0056016 secs] 431336K->335388K(494976K), 0.0056912 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
2021-03-26T09:32:08.978-0800: 0.763: [GC (Allocation Failure) 2021-03-26T09:32:08.978-0800: 0.763: [DefNew: 153306K->153306K(153600K), 0.0000200 secs]2021-03-26T09:32:08.979-0800: 0.763: [Tenured: 318364K->315567K(341376K), 0.0434427 secs] 471671K->315567K(494976K), [Metaspace: 2570K->2570K(1056768K)], 0.0435827 secs] [Times: user=0.04 sys=0.00, real=0.05 secs]
2021-03-26T09:32:09.044-0800: 0.828: [GC (Allocation Failure) 2021-03-26T09:32:09.044-0800: 0.828: [DefNew: 136576K->136576K(153600K), 0.0000225 secs]2021-03-26T09:32:09.044-0800: 0.828: [Tenured: 315567K->311217K(341376K), 0.0476746 secs] 452143K->311217K(494976K), [Metaspace: 2570K->2570K(1056768K)], 0.0478409 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
2021-03-26T09:32:09.113-0800: 0.897: [GC (Allocation Failure) 2021-03-26T09:32:09.113-0800: 0.897: [DefNew: 136576K->136576K(153600K), 0.0000175 secs]2021-03-26T09:32:09.113-0800: 0.897: [Tenured: 311217K->334701K(341376K), 0.0351527 secs] 447793K->334701K(494976K), [Metaspace: 2570K->2570K(1056768K)], 0.0352660 secs] [Times: user=0.03 sys=0.01, real=0.03 secs]
2021-03-26T09:32:09.168-0800: 0.952: [GC (Allocation Failure) 2021-03-26T09:32:09.168-0800: 0.952: [DefNew: 136547K->136547K(153600K), 0.0000167 secs]2021-03-26T09:32:09.168-0800: 0.952: [Tenured: 334701K->341304K(341376K), 0.0458721 secs] 471249K->342878K(494976K), [Metaspace: 2570K->2570K(1056768K)], 0.0459729 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]

# Full GC
# GC发生的时间	[GC堆内存变化情况 Full GC(GC原因:分配失败) [Tenured(老年代,标识使用的算法是serialOldGC) 老年代堆内存的变化(老年代堆内存总大小) 老年代GC暂时停时间] 整堆内存变化(整堆大小), [Metaspace(元数据区) 元数据区堆内存变化(元数据区堆内存总大小)] 整堆GC暂时时间]  [GC CPU使用情况]
2021-03-26T09:32:09.235-0800: 1.020: [Full GC (Allocation Failure) 2021-03-26T09:32:09.235-0800: 1.020: [Tenured: 341304K->341284K(341376K), 0.0461890 secs] 494542K->352391K(494976K), [Metaspace: 2570K->2570K(1056768K)], 0.0463016 secs] [Times: user=0.04 sys=0.00, real=0.05 secs]
2021-03-26T09:32:09.303-0800: 1.088: [Full GC (Allocation Failure) 2021-03-26T09:32:09.303-0800: 1.088: [Tenured: 341284K->334825K(341376K), 0.0523485 secs] 494853K->334825K(494976K), [Metaspace: 2570K->2570K(1056768K)], 0.0524760 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]

# 堆内存使用情况
Heap
 def new generation   total 153600K, used 8342K [0x00000007a0c00000, 0x00000007ab2a0000, 0x00000007ab2a0000)
  eden space 136576K,   6% used [0x00000007a0c00000, 0x00000007a1425b48, 0x00000007a9160000)
  from space 17024K,   0% used [0x00000007a9160000, 0x00000007a9160000, 0x00000007aa200000)
  to   space 17024K,   0% used [0x00000007aa200000, 0x00000007aa200000, 0x00000007ab2a0000)
 tenured generation   total 341376K, used 334825K [0x00000007ab2a0000, 0x00000007c0000000, 0x00000007c0000000)
   the space 341376K,  98% used [0x00000007ab2a0000, 0x00000007bf99a688, 0x00000007bf99a800, 0x00000007c0000000)
 Metaspace       used 2576K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 276K, capacity 386K, committed 512K, reserved 1048576K
```

2.ParNewGC(年轻代为ParNewGC，老年代为serialOldGC)
```
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx512m -Xms512m -XX:+UseParNewGC -Xloggc:parNewGC.log GCLogAnalysis

1次FullGC

Java HotSpot(TM) 64-Bit Server VM (25.281-b09) for bsd-amd64 JRE (1.8.0_281-b09), built on Dec  9 2020 12:44:49 by "java_re" with gcc 4.2.1 Compatible Apple LLVM 10.0.0 (clang-1000.11.45.5)
Memory: 4k page, physical 16777216k(537644k free)

/proc/meminfo:

CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParNewGC

# 年轻代GC
# GC发生的时间	[GC堆内存变化情况 GC(GC原因:分配失败)  [ParNew(年轻代,同时能标识所使用的GC算法为ParNew): 年轻代堆内存的变化(年轻代堆内存总大小) 年轻代GC暂时时间] 整堆内存的变化(整堆内存总大小) 整堆GC暂停时间] [GC CPU使用情况]
2021-03-26T09:47:59.346-0800: 0.214: [GC (Allocation Failure) 2021-03-26T09:47:59.363-0800: 0.231: [ParNew: 139776K->17464K(157248K), 0.0165029 secs] 139776K->40035K(506816K), 0.0333572 secs] [Times: user=0.03 sys=0.09, real=0.04 secs]
2021-03-26T09:47:59.403-0800: 0.271: [GC (Allocation Failure) 2021-03-26T09:47:59.403-0800: 0.271: [ParNew: 157129K->17472K(157248K), 0.0175377 secs] 179701K->86541K(506816K), 0.0176222 secs] [Times: user=0.05 sys=0.11, real=0.02 secs]
2021-03-26T09:47:59.438-0800: 0.306: [GC (Allocation Failure) 2021-03-26T09:47:59.438-0800: 0.306: [ParNew: 157248K->17471K(157248K), 0.0278853 secs] 226317K->133374K(506816K), 0.0279575 secs] [Times: user=0.26 sys=0.01, real=0.03 secs]
2021-03-26T09:47:59.481-0800: 0.349: [GC (Allocation Failure) 2021-03-26T09:47:59.481-0800: 0.349: [ParNew: 157247K->17471K(157248K), 0.0272083 secs] 273150K->179456K(506816K), 0.0272823 secs] [Times: user=0.24 sys=0.02, real=0.02 secs]
2021-03-26T09:47:59.526-0800: 0.394: [GC (Allocation Failure) 2021-03-26T09:47:59.526-0800: 0.394: [ParNew: 157247K->17470K(157248K), 0.0284636 secs] 319232K->224295K(506816K), 0.0285414 secs] [Times: user=0.26 sys=0.02, real=0.03 secs]
2021-03-26T09:47:59.575-0800: 0.443: [GC (Allocation Failure) 2021-03-26T09:47:59.575-0800: 0.444: [ParNew: 157146K->17470K(157248K), 0.0284277 secs] 363971K->267108K(506816K), 0.0285223 secs] [Times: user=0.26 sys=0.02, real=0.03 secs]
2021-03-26T09:47:59.623-0800: 0.491: [GC (Allocation Failure) 2021-03-26T09:47:59.623-0800: 0.491: [ParNew: 157246K->17470K(157248K), 0.0257650 secs] 406884K->307524K(506816K), 0.0258495 secs] [Times: user=0.24 sys=0.01, real=0.02 secs]
2021-03-26T09:47:59.665-0800: 0.533: [GC (Allocation Failure) 2021-03-26T09:47:59.665-0800: 0.533: [ParNew: 157246K->17469K(157248K), 0.0287572 secs] 447300K->346459K(506816K), 0.0288443 secs] [Times: user=0.17 sys=0.02, real=0.03 secs]

# 年轻代和老年代GC
# GC发生的时间	[GC堆内存变化情况 GC(GC原因:分配失败)  [ParNew(年轻代,标识使用的算法是ParNew): 年轻代堆内存的变化(年轻代堆内存总大小) 年轻代GC暂时时间] [Tenured(老年代,标识使用的算法是serialOldGC) 老年代堆内存的变化(老年代堆内存总大小) 老年代GC暂时停时间] 整堆内存变化(整堆大小), [Metaspace(元数据区) 元数据区堆内存变化(元数据区堆内存总大小)] 整堆GC暂时时间]  [GC CPU使用情况]
2021-03-26T09:47:59.713-0800: 0.581: [GC (Allocation Failure) 2021-03-26T09:47:59.713-0800: 0.581: [ParNew: 157245K->157245K(157248K), 0.0000359 secs]2021-03-26T09:47:59.713-0800: 0.582: [Tenured: 328990K->268534K(349568K), 0.0566612 secs] 486235K->268534K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0568327 secs] [Times: user=0.06 sys=0.00, real=0.06 secs]
2021-03-26T09:47:59.791-0800: 0.659: [GC (Allocation Failure) 2021-03-26T09:47:59.791-0800: 0.659: [ParNew: 139703K->17469K(157248K), 0.0062522 secs] 408237K->313948K(506816K), 0.0063483 secs] [Times: user=0.06 sys=0.00, real=0.00 secs]
2021-03-26T09:47:59.815-0800: 0.683: [GC (Allocation Failure) 2021-03-26T09:47:59.815-0800: 0.683: [ParNew: 157219K->17470K(157248K), 0.0137018 secs] 453698K->358403K(506816K), 0.0137969 secs] [Times: user=0.13 sys=0.00, real=0.01 secs]
2021-03-26T09:47:59.849-0800: 0.717: [GC (Allocation Failure) 2021-03-26T09:47:59.865-0800: 0.733: [ParNew: 157195K->157195K(157248K), 0.0009708 secs]2021-03-26T09:47:59.866-0800: 0.734: [Tenured: 340932K->286681K(349568K), 0.0507010 secs] 498128K->286681K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0680835 secs] [Times: user=0.05 sys=0.00, real=0.07 secs]
2021-03-26T09:47:59.938-0800: 0.806: [GC (Allocation Failure) 2021-03-26T09:47:59.938-0800: 0.806: [ParNew: 139670K->17470K(157248K), 0.0065753 secs] 426351K->335769K(506816K), 0.0066717 secs] [Times: user=0.06 sys=0.00, real=0.01 secs]
2021-03-26T09:47:59.963-0800: 0.831: [GC (Allocation Failure) 2021-03-26T09:47:59.963-0800: 0.831: [ParNew: 156981K->156981K(157248K), 0.0000219 secs]2021-03-26T09:47:59.963-0800: 0.831: [Tenured: 318299K->313045K(349568K), 0.0406871 secs] 475281K->313045K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0408304 secs] [Times: user=0.04 sys=0.00, real=0.04 secs]
2021-03-26T09:48:00.024-0800: 0.892: [GC (Allocation Failure) 2021-03-26T09:48:00.024-0800: 0.892: [ParNew: 139497K->139497K(157248K), 0.0000193 secs]2021-03-26T09:48:00.024-0800: 0.892: [Tenured: 313045K->308783K(349568K), 0.0463681 secs] 452543K->308783K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0464999 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
2021-03-26T09:48:00.094-0800: 0.962: [GC (Allocation Failure) 2021-03-26T09:48:00.094-0800: 0.962: [ParNew: 139776K->17470K(157248K), 0.0076852 secs] 448559K->360607K(506816K), 0.0077694 secs] [Times: user=0.08 sys=0.00, real=0.01 secs]
2021-03-26T09:48:00.124-0800: 0.992: [GC (Allocation Failure) 2021-03-26T09:48:00.124-0800: 0.992: [ParNew: 157246K->157246K(157248K), 0.0000185 secs]2021-03-26T09:48:00.124-0800: 0.992: [Tenured: 343136K->346730K(349568K), 0.0422667 secs] 500383K->346730K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0423775 secs] [Times: user=0.04 sys=0.01, real=0.04 secs]
2021-03-26T09:48:00.188-0800: 1.056: [GC (Allocation Failure) 2021-03-26T09:48:00.188-0800: 1.056: [ParNew: 139668K->139668K(157248K), 0.0000189 secs]2021-03-26T09:48:00.188-0800: 1.056: [Tenured: 346730K->349327K(349568K), 0.0444748 secs] 486398K->350313K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0445890 secs] [Times: user=0.04 sys=0.00, real=0.05 secs]

# Full GC
# GC发生的时间	[GC堆内存变化情况 Full GC(GC原因:分配失败) [Tenured(老年代,标识使用的算法是serialOldGC) 老年代堆内存的变化(老年代堆内存总大小) 老年代GC暂时停时间] 整堆内存变化(整堆大小), [Metaspace(元数据区) 元数据区堆内存变化(元数据区堆内存总大小)] 整堆GC暂时时间]  [GC CPU使用情况]
2021-03-26T09:48:00.256-0800: 1.124: [Full GC (Allocation Failure) 2021-03-26T09:48:00.256-0800: 1.124: [Tenured: 349327K->349497K(349568K), 0.0450291 secs] 506450K->351625K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0451132 secs] [Times: user=0.04 sys=0.00, real=0.05 secs]
Heap
 par new generation   total 157248K, used 8046K [0x00000007a0000000, 0x00000007aaaa0000, 0x00000007aaaa0000)
  eden space 139776K,   5% used [0x00000007a0000000, 0x00000007a07db848, 0x00000007a8880000)
  from space 17472K,   0% used [0x00000007a8880000, 0x00000007a8880000, 0x00000007a9990000)
  to   space 17472K,   0% used [0x00000007a9990000, 0x00000007a9990000, 0x00000007aaaa0000)
 tenured generation   total 349568K, used 349497K [0x00000007aaaa0000, 0x00000007c0000000, 0x00000007c0000000)
   the space 349568K,  99% used [0x00000007aaaa0000, 0x00000007bffee7b0, 0x00000007bffee800, 0x00000007c0000000)
 Metaspace       used 2576K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 276K, capacity 386K, committed 512K, reserved 1048576K
```

3.ParallelGC(年轻代ParallelScavenge，老年代ParallelOld)
```
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx512m -Xms512m -XX:+UseParallelGC -Xloggc:parallelGC.log GCLogAnalysis

7次FullGC

Java HotSpot(TM) 64-Bit Server VM (25.281-b09) for bsd-amd64 JRE (1.8.0_281-b09), built on Dec  9 2020 12:44:49 by "java_re" with gcc 4.2.1 Compatible Apple LLVM 10.0.0 (clang-1000.11.45.5)
Memory: 4k page, physical 16777216k(293680k free)

/proc/meminfo:

CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC

# 年轻代GC
# GC发生的时间	[GC堆内存变化情况 GC(GC原因:分配失败)  [PSYoungGen(年轻代,同时能标识所使用的GC算法为ParallelScavenge): 年轻代堆内存的变化(年轻代堆内存总大小)] 整堆内存的变化(整堆内存总大小) 整堆GC暂停时间] [GC CPU使用情况]
2021-03-26T09:55:27.411-0800: 0.225: [GC (Allocation Failure) [PSYoungGen: 131584K->21484K(153088K)] 131584K->39365K(502784K), 0.0127850 secs] [Times: user=0.02 sys=0.07, real=0.01 secs]
2021-03-26T09:55:27.442-0800: 0.256: [GC (Allocation Failure) [PSYoungGen: 153068K->21502K(153088K)] 170949K->77515K(502784K), 0.0166223 secs] [Times: user=0.03 sys=0.11, real=0.01 secs]
2021-03-26T09:55:27.474-0800: 0.288: [GC (Allocation Failure) [PSYoungGen: 152573K->21503K(153088K)] 208586K->118925K(502784K), 0.0132074 secs] [Times: user=0.04 sys=0.06, real=0.01 secs]
2021-03-26T09:55:27.502-0800: 0.316: [GC (Allocation Failure) [PSYoungGen: 153087K->21503K(153088K)] 250509K->155550K(502784K), 0.0116616 secs] [Times: user=0.03 sys=0.07, real=0.01 secs]
2021-03-26T09:55:27.532-0800: 0.347: [GC (Allocation Failure) [PSYoungGen: 152570K->21490K(153088K)] 286617K->195044K(502784K), 0.0179349 secs] [Times: user=0.04 sys=0.08, real=0.04 secs]
2021-03-26T09:55:27.590-0800: 0.405: [GC (Allocation Failure) [PSYoungGen: 153074K->21502K(80384K)] 326628K->239706K(430080K), 0.0202812 secs] [Times: user=0.04 sys=0.09, real=0.02 secs]
2021-03-26T09:55:27.618-0800: 0.432: [GC (Allocation Failure) [PSYoungGen: 80080K->34938K(116736K)] 298284K->256409K(466432K), 0.0046105 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]
2021-03-26T09:55:27.629-0800: 0.443: [GC (Allocation Failure) [PSYoungGen: 93713K->50365K(116736K)] 315183K->275596K(466432K), 0.0062118 secs] [Times: user=0.05 sys=0.01, real=0.01 secs]
2021-03-26T09:55:27.642-0800: 0.456: [GC (Allocation Failure) [PSYoungGen: 109133K->53492K(116736K)] 334364K->289172K(466432K), 0.0082179 secs] [Times: user=0.05 sys=0.02, real=0.01 secs]
2021-03-26T09:55:27.659-0800: 0.473: [GC (Allocation Failure) [PSYoungGen: 112372K->37277K(116736K)] 348052K->306654K(466432K), 0.0120831 secs] [Times: user=0.03 sys=0.05, real=0.02 secs]
2021-03-26T09:55:27.678-0800: 0.492: [GC (Allocation Failure) [PSYoungGen: 96157K->20484K(116736K)] 365534K->322070K(466432K), 0.0121119 secs] [Times: user=0.03 sys=0.06, real=0.02 secs]

# Full GC
# GC发生的时间	[GC堆内存变化情况 Full GC(GC原因:分配失败) [PSYoungGen(年轻代,同时能标识所使用的GC算法为ParallelScavenge): 年轻代堆内存的变化(年轻代堆内存总大小)] [ParOldGen(老年代，标识使用算法为ParallelOld) 老年代堆内存的变化(老年代堆内存总大小)] 整堆内存变化(整堆大小), [Metaspace(元数据区) 元数据区堆内存变化(元数据区堆内存总大小)] 整堆GC暂时时间]  [GC CPU使用情况]
2021-03-26T09:55:27.690-0800: 0.505: [Full GC (Ergonomics) [PSYoungGen: 20484K->0K(116736K)] [ParOldGen: 301585K->221898K(349696K)] 322070K->221898K(466432K), [Metaspace: 2570K->2570K(1056768K)], 0.0291493 secs] [Times: user=0.25 sys=0.01, real=0.03 secs]
2021-03-26T09:55:27.729-0800: 0.544: [GC (Allocation Failure) [PSYoungGen: 58781K->19271K(116736K)] 280679K->241169K(466432K), 0.0025714 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]
2021-03-26T09:55:27.741-0800: 0.555: [GC (Allocation Failure) [PSYoungGen: 78151K->24317K(116736K)] 300049K->264804K(466432K), 0.0051095 secs] [Times: user=0.05 sys=0.00, real=0.00 secs]
2021-03-26T09:55:27.755-0800: 0.569: [GC (Allocation Failure) [PSYoungGen: 83197K->22020K(116736K)] 323684K->285479K(466432K), 0.0053542 secs] [Times: user=0.05 sys=0.00, real=0.01 secs]
2021-03-26T09:55:27.771-0800: 0.585: [GC (Allocation Failure) [PSYoungGen: 80900K->21887K(116736K)] 344359K->303988K(466432K), 0.0046687 secs] [Times: user=0.04 sys=0.00, real=0.00 secs]
2021-03-26T09:55:27.787-0800: 0.601: [GC (Allocation Failure) [PSYoungGen: 80311K->18996K(116736K)] 362412K->322609K(466432K), 0.0054892 secs] [Times: user=0.04 sys=0.01, real=0.01 secs]
2021-03-26T09:55:27.792-0800: 0.607: [Full GC (Ergonomics) [PSYoungGen: 18996K->0K(116736K)] [ParOldGen: 303613K->255320K(349696K)] 322609K->255320K(466432K), [Metaspace: 2570K->2570K(1056768K)], 0.0359764 secs] [Times: user=0.32 sys=0.00, real=0.03 secs]
2021-03-26T09:55:27.841-0800: 0.655: [GC (Allocation Failure) [PSYoungGen: 58880K->18105K(116736K)] 314200K->273425K(466432K), 0.0025658 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
2021-03-26T09:55:27.854-0800: 0.668: [GC (Allocation Failure) [PSYoungGen: 76985K->21046K(116736K)] 332305K->293878K(466432K), 0.0048109 secs] [Times: user=0.04 sys=0.00, real=0.00 secs]
2021-03-26T09:55:27.869-0800: 0.683: [GC (Allocation Failure) [PSYoungGen: 79851K->20676K(116736K)] 352684K->313763K(466432K), 0.0048443 secs] [Times: user=0.04 sys=0.01, real=0.01 secs]
2021-03-26T09:55:27.883-0800: 0.697: [GC (Allocation Failure) [PSYoungGen: 79066K->22387K(116736K)] 372153K->335137K(466432K), 0.0071355 secs] [Times: user=0.04 sys=0.02, real=0.01 secs]
2021-03-26T09:55:27.890-0800: 0.705: [Full GC (Ergonomics) [PSYoungGen: 22387K->0K(116736K)] [ParOldGen: 312750K->274476K(349696K)] 335137K->274476K(466432K), [Metaspace: 2570K->2570K(1056768K)], 0.0436881 secs] [Times: user=0.32 sys=0.00, real=0.04 secs]
2021-03-26T09:55:27.946-0800: 0.761: [GC (Allocation Failure) [PSYoungGen: 58880K->20517K(116736K)] 333356K->294993K(466432K), 0.0027861 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
2021-03-26T09:55:27.958-0800: 0.773: [GC (Allocation Failure) [PSYoungGen: 78830K->21202K(116736K)] 353306K->315731K(466432K), 0.0049777 secs] [Times: user=0.04 sys=0.00, real=0.01 secs]
2021-03-26T09:55:27.974-0800: 0.788: [GC (Allocation Failure) [PSYoungGen: 79904K->18692K(116736K)] 374433K->332982K(466432K), 0.0053164 secs] [Times: user=0.05 sys=0.01, real=0.00 secs]
2021-03-26T09:55:27.989-0800: 0.803: [GC (Allocation Failure) [PSYoungGen: 77572K->24005K(116736K)] 391862K->356604K(466432K), 0.0101644 secs] [Times: user=0.02 sys=0.05, real=0.01 secs]
2021-03-26T09:55:27.999-0800: 0.814: [Full GC (Ergonomics) [PSYoungGen: 24005K->0K(116736K)] [ParOldGen: 332599K->293627K(349696K)] 356604K->293627K(466432K), [Metaspace: 2570K->2570K(1056768K)], 0.0333398 secs] [Times: user=0.29 sys=0.00, real=0.04 secs]
2021-03-26T09:55:28.047-0800: 0.861: [GC (Allocation Failure) [PSYoungGen: 58803K->20915K(116736K)] 352431K->314543K(466432K), 0.0188382 secs] [Times: user=0.04 sys=0.00, real=0.02 secs]
2021-03-26T09:55:28.079-0800: 0.893: [GC (Allocation Failure) [PSYoungGen: 79795K->18621K(116736K)] 373423K->332140K(466432K), 0.0048637 secs] [Times: user=0.04 sys=0.00, real=0.01 secs]
2021-03-26T09:55:28.096-0800: 0.910: [GC (Allocation Failure) [PSYoungGen: 77501K->16793K(116736K)] 391020K->347960K(466432K), 0.0053960 secs] [Times: user=0.03 sys=0.01, real=0.01 secs]
2021-03-26T09:55:28.101-0800: 0.916: [Full GC (Ergonomics) [PSYoungGen: 16793K->0K(116736K)] [ParOldGen: 331167K->300694K(349696K)] 347960K->300694K(466432K), [Metaspace: 2570K->2570K(1056768K)], 0.0369350 secs] [Times: user=0.31 sys=0.00, real=0.03 secs]
2021-03-26T09:55:28.151-0800: 0.965: [GC (Allocation Failure) [PSYoungGen: 58485K->20190K(119296K)] 359180K->320884K(468992K), 0.0029880 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
2021-03-26T09:55:28.165-0800: 0.979: [GC (Allocation Failure) [PSYoungGen: 81630K->40203K(116736K)] 382324K->340898K(466432K), 0.0047360 secs] [Times: user=0.04 sys=0.00, real=0.00 secs]
2021-03-26T09:55:28.179-0800: 0.994: [GC (Allocation Failure) [PSYoungGen: 101476K->57849K(116736K)] 402171K->360222K(466432K), 0.0066253 secs] [Times: user=0.06 sys=0.00, real=0.01 secs]
2021-03-26T09:55:28.195-0800: 1.009: [GC (Allocation Failure) [PSYoungGen: 116729K->41748K(116736K)] 419102K->378219K(466432K), 0.0093329 secs] [Times: user=0.08 sys=0.01, real=0.01 secs]
2021-03-26T09:55:28.204-0800: 1.019: [Full GC (Ergonomics) [PSYoungGen: 41748K->0K(116736K)] [ParOldGen: 336471K->312015K(349696K)] 378219K->312015K(466432K), [Metaspace: 2570K->2570K(1056768K)], 0.0365427 secs] [Times: user=0.33 sys=0.00, real=0.04 secs]
2021-03-26T09:55:28.255-0800: 1.069: [GC (Allocation Failure) [PSYoungGen: 58623K->18640K(116736K)] 370639K->330656K(466432K), 0.0026599 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
2021-03-26T09:55:28.273-0800: 1.088: [GC (Allocation Failure) [PSYoungGen: 77520K->18232K(116736K)] 389536K->346263K(466432K), 0.0040129 secs] [Times: user=0.03 sys=0.00, real=0.00 secs]
2021-03-26T09:55:28.277-0800: 1.092: [Full GC (Ergonomics) [PSYoungGen: 18232K->0K(116736K)] [ParOldGen: 328030K->314417K(349696K)] 346263K->314417K(466432K), [Metaspace: 2570K->2570K(1056768K)], 0.0336969 secs] [Times: user=0.30 sys=0.01, real=0.04 secs]
2021-03-26T09:55:28.325-0800: 1.139: [GC (Allocation Failure) [PSYoungGen: 58880K->21947K(116736K)] 373297K->336364K(466432K), 0.0028715 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
Heap
 PSYoungGen      total 116736K, used 54330K [0x00000007b5580000, 0x00000007c0000000, 0x00000007c0000000)
  eden space 58880K, 54% used [0x00000007b5580000,0x00000007b751fbf0,0x00000007b8f00000)
  from space 57856K, 37% used [0x00000007bc780000,0x00000007bdceecc8,0x00000007c0000000)
  to   space 57856K, 0% used [0x00000007b8f00000,0x00000007b8f00000,0x00000007bc780000)
 ParOldGen       total 349696K, used 314417K [0x00000007a0000000, 0x00000007b5580000, 0x00000007b5580000)
  object space 349696K, 89% used [0x00000007a0000000,0x00000007b330c418,0x00000007b5580000)
 Metaspace       used 2576K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 276K, capacity 386K, committed 512K, reserved 1048576K
```

4.CMS(年轻代ParNew，老年代CMS)
```
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx512m -Xms512m -XX:+UseConcMarkSweepGC -Xloggc:cmsGC.log GCLogAnalysis

Java HotSpot(TM) 64-Bit Server VM (25.281-b09) for bsd-amd64 JRE (1.8.0_281-b09), built on Dec  9 2020 12:44:49 by "java_re" with gcc 4.2.1 Compatible Apple LLVM 10.0.0 (clang-1000.11.45.5)
Memory: 4k page, physical 16777216k(354404k free)

/proc/meminfo:

CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:MaxNewSize=178958336 -XX:MaxTenuringThreshold=6 -XX:NewSize=178958336 -XX:OldPLABSize=16 -XX:OldSize=357912576 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:+UseParNewGC

# 年轻代GC
# GC发生的时间	[GC堆内存变化情况 GC(GC原因:分配失败)  [ParNew(年轻代,同时能标识所使用的GC算法为ParNew): 年轻代堆内存的变化(年轻代堆内存总大小)] 整堆内存的变化(整堆内存总大小) 整堆GC暂停时间] [GC CPU使用情况]
2021-03-26T09:55:46.091-0800: 0.175: [GC (Allocation Failure) 2021-03-26T09:55:46.091-0800: 0.176: [ParNew: 139776K->17472K(157248K), 0.0178686 secs] 139776K->48241K(506816K), 0.0180800 secs] [Times: user=0.04 sys=0.11, real=0.01 secs]
2021-03-26T09:55:46.131-0800: 0.216: [GC (Allocation Failure) 2021-03-26T09:55:46.131-0800: 0.216: [ParNew: 157248K->17470K(157248K), 0.0200325 secs] 188017K->96044K(506816K), 0.0201214 secs] [Times: user=0.06 sys=0.11, real=0.02 secs]
2021-03-26T09:55:46.172-0800: 0.257: [GC (Allocation Failure) 2021-03-26T09:55:46.172-0800: 0.257: [ParNew: 157246K->17471K(157248K), 0.0258175 secs] 235820K->137615K(506816K), 0.0259024 secs] [Times: user=0.24 sys=0.01, real=0.02 secs]
2021-03-26T09:55:46.224-0800: 0.309: [GC (Allocation Failure) 2021-03-26T09:55:46.224-0800: 0.309: [ParNew: 157247K->17471K(157248K), 0.0212134 secs] 277391K->179540K(506816K), 0.0212893 secs] [Times: user=0.19 sys=0.02, real=0.02 secs]
2021-03-26T09:55:46.263-0800: 0.348: [GC (Allocation Failure) 2021-03-26T09:55:46.263-0800: 0.348: [ParNew: 157247K->17469K(157248K), 0.0231042 secs] 319316K->221247K(506816K), 0.0231826 secs] [Times: user=0.19 sys=0.02, real=0.02 secs]

# CMS初始标记阶段
# [GC (CMS Initial Mark) [1 CMS-initial-mark: 老年代内存使用量(老年代内存总量)] 堆内存使用量(堆内存总量), GC暂停时间] [GC CPU使用率]
2021-03-26T09:55:46.287-0800: 0.372: [GC (CMS Initial Mark) [1 CMS-initial-mark: 203777K(349568K)] 233302K(506816K), 0.0019621 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
# CMS并发标记阶段开始
2021-03-26T09:55:46.289-0800: 0.374: [CMS-concurrent-mark-start]
# CMS并标记阶段结束
# [使用时间] [CPU使用率]
2021-03-26T09:55:46.293-0800: 0.378: [CMS-concurrent-mark: 0.004/0.004 secs] [Times: user=0.01 sys=0.00, real=0.01 secs]
# CMS 并发预清理阶段开始
2021-03-26T09:55:46.294-0800: 0.379: [CMS-concurrent-preclean-start]
# CMS 并发预清理阶段结束
# [使用时间] [CPU使用率]
2021-03-26T09:55:46.295-0800: 0.380: [CMS-concurrent-preclean: 0.002/0.002 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
# CMS 并发可终止预清理阶段开始
2021-03-26T09:55:46.295-0800: 0.380: [CMS-concurrent-abortable-preclean-start]

2021-03-26T09:55:46.306-0800: 0.391: [GC (Allocation Failure) 2021-03-26T09:55:46.306-0800: 0.391: [ParNew: 157074K->17470K(157248K), 0.0222272 secs] 360852K->260766K(506816K), 0.0223203 secs] [Times: user=0.18 sys=0.02, real=0.02 secs]
2021-03-26T09:55:46.347-0800: 0.432: [GC (Allocation Failure) 2021-03-26T09:55:46.347-0800: 0.432: [ParNew: 157246K->17471K(157248K), 0.0290154 secs] 400542K->304804K(506816K), 0.0291137 secs] [Times: user=0.23 sys=0.02, real=0.03 secs]
2021-03-26T09:55:46.394-0800: 0.479: [GC (Allocation Failure) 2021-03-26T09:55:46.394-0800: 0.479: [ParNew: 157247K->17470K(157248K), 0.0289016 secs] 444580K->347990K(506816K), 0.0289942 secs] [Times: user=0.25 sys=0.02, real=0.03 secs]

# CMS 并发可终止预清理阶段结束
# [持续时间] [CPU使用率]
2021-03-26T09:55:46.424-0800: 0.509: [CMS-concurrent-abortable-preclean: 0.004/0.128 secs] [Times: user=0.71 sys=0.06, real=0.13 secs]
# CMS最终标记阶段
2021-03-26T09:55:46.424-0800: 0.509: [GC (CMS Final Remark) [YG occupancy: 31019 K (157248 K)]2021-03-26T09:55:46.424-0800: 0.509: [Rescan (parallel) , 0.0002694 secs]2021-03-26T09:55:46.424-0800: 0.509: [weak refs processing, 0.0000172 secs]2021-03-26T09:55:46.424-0800: 0.509: [class unloading, 0.0002497 secs]2021-03-26T09:55:46.424-0800: 0.509: [scrub symbol table, 0.0002755 secs]2021-03-26T09:55:46.425-0800: 0.510: [scrub string table, 0.0001253 secs][1 CMS-remark: 330519K(349568K)] 361538K(506816K), 0.0010301 secs] [Times: user=0.00 sys=0.01, real=0.00 secs]
# CMS并发清除阶段开始
2021-03-26T09:55:46.425-0800: 0.510: [CMS-concurrent-sweep-start]
# CMS并发清除阶段结束
2021-03-26T09:55:46.425-0800: 0.510: [CMS-concurrent-sweep: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
# CMS并发重置阶段开始
2021-03-26T09:55:46.426-0800: 0.510: [CMS-concurrent-reset-start]
# CMS并发重置阶段结束
2021-03-26T09:55:46.426-0800: 0.511: [CMS-concurrent-reset: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]


2021-03-26T09:55:46.440-0800: 0.525: [GC (Allocation Failure) 2021-03-26T09:55:46.440-0800: 0.525: [ParNew: 157246K->17470K(157248K), 0.0091672 secs] 436830K->336551K(506816K), 0.0092300 secs] [Times: user=0.09 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.449-0800: 0.534: [GC (CMS Initial Mark) [1 CMS-initial-mark: 319081K(349568K)] 339386K(506816K), 0.0002306 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-03-26T09:55:46.450-0800: 0.535: [CMS-concurrent-mark-start]
2021-03-26T09:55:46.450-0800: 0.535: [CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.450-0800: 0.535: [CMS-concurrent-preclean-start]
2021-03-26T09:55:46.451-0800: 0.536: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.451-0800: 0.536: [CMS-concurrent-abortable-preclean-start]
2021-03-26T09:55:46.474-0800: 0.559: [GC (Allocation Failure) 2021-03-26T09:55:46.474-0800: 0.559: [ParNew: 157246K->157246K(157248K), 0.0000220 secs]2021-03-26T09:55:46.474-0800: 0.559: [CMS2021-03-26T09:55:46.474-0800: 0.559: [CMS-concurrent-abortable-preclean: 0.001/0.023 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
 (concurrent mode failure): 319081K->258131K(349568K), 0.0599291 secs] 476327K->258131K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0600365 secs] [Times: user=0.06 sys=0.00, real=0.06 secs]
2021-03-26T09:55:46.553-0800: 0.638: [GC (Allocation Failure) 2021-03-26T09:55:46.553-0800: 0.638: [ParNew: 139776K->17470K(157248K), 0.0093822 secs] 397907K->305769K(506816K), 0.0094581 secs] [Times: user=0.07 sys=0.00, real=0.01 secs]
2021-03-26T09:55:46.563-0800: 0.648: [GC (CMS Initial Mark) [1 CMS-initial-mark: 288298K(349568K)] 306390K(506816K), 0.0003966 secs] [Times: user=0.00 sys=0.00, real=0.02 secs]
2021-03-26T09:55:46.585-0800: 0.670: [CMS-concurrent-mark-start]
2021-03-26T09:55:46.587-0800: 0.672: [CMS-concurrent-mark: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.587-0800: 0.672: [CMS-concurrent-preclean-start]
2021-03-26T09:55:46.588-0800: 0.673: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.588-0800: 0.673: [CMS-concurrent-abortable-preclean-start]
2021-03-26T09:55:46.609-0800: 0.694: [GC (Allocation Failure) 2021-03-26T09:55:46.615-0800: 0.700: [ParNew: 157246K->17468K(157248K), 0.0100561 secs] 445545K->350164K(506816K), 0.0162125 secs] [Times: user=0.09 sys=0.00, real=0.02 secs]
2021-03-26T09:55:46.625-0800: 0.710: [CMS-concurrent-abortable-preclean: 0.001/0.038 secs] [Times: user=0.11 sys=0.00, real=0.04 secs]
2021-03-26T09:55:46.625-0800: 0.710: [GC (CMS Final Remark) [YG occupancy: 20324 K (157248 K)]2021-03-26T09:55:46.625-0800: 0.710: [Rescan (parallel) , 0.0002989 secs]2021-03-26T09:55:46.626-0800: 0.711: [weak refs processing, 0.0000197 secs]2021-03-26T09:55:46.626-0800: 0.711: [class unloading, 0.0001773 secs]2021-03-26T09:55:46.626-0800: 0.711: [scrub symbol table, 0.0002792 secs]2021-03-26T09:55:46.626-0800: 0.711: [scrub string table, 0.0001353 secs][1 CMS-remark: 332696K(349568K)] 353020K(506816K), 0.0009995 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.627-0800: 0.712: [CMS-concurrent-sweep-start]
2021-03-26T09:55:46.627-0800: 0.712: [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.627-0800: 0.712: [CMS-concurrent-reset-start]
2021-03-26T09:55:46.627-0800: 0.712: [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.647-0800: 0.732: [GC (Allocation Failure) 2021-03-26T09:55:46.647-0800: 0.732: [ParNew: 157244K->157244K(157248K), 0.0000224 secs]2021-03-26T09:55:46.647-0800: 0.732: [CMS: 297701K->298572K(349568K), 0.0449183 secs] 454945K->298572K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0450496 secs] [Times: user=0.04 sys=0.00, real=0.05 secs]
2021-03-26T09:55:46.693-0800: 0.778: [GC (CMS Initial Mark) [1 CMS-initial-mark: 298572K(349568K)] 298717K(506816K), 0.0002512 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.693-0800: 0.778: [CMS-concurrent-mark-start]
2021-03-26T09:55:46.694-0800: 0.779: [CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.694-0800: 0.779: [CMS-concurrent-preclean-start]
2021-03-26T09:55:46.694-0800: 0.779: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.694-0800: 0.779: [CMS-concurrent-abortable-preclean-start]
2021-03-26T09:55:46.710-0800: 0.795: [GC (Allocation Failure) 2021-03-26T09:55:46.710-0800: 0.795: [ParNew: 139776K->17471K(157248K), 0.0079349 secs] 438348K->349673K(506816K), 0.0080208 secs] [Times: user=0.08 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.718-0800: 0.803: [CMS-concurrent-abortable-preclean: 0.001/0.024 secs] [Times: user=0.10 sys=0.00, real=0.02 secs]
2021-03-26T09:55:46.719-0800: 0.804: [GC (CMS Final Remark) [YG occupancy: 18214 K (157248 K)]2021-03-26T09:55:46.719-0800: 0.804: [Rescan (parallel) , 0.0003578 secs]2021-03-26T09:55:46.719-0800: 0.804: [weak refs processing, 0.0000160 secs]2021-03-26T09:55:46.719-0800: 0.804: [class unloading, 0.0001756 secs]2021-03-26T09:55:46.719-0800: 0.804: [scrub symbol table, 0.0002895 secs]2021-03-26T09:55:46.719-0800: 0.804: [scrub string table, 0.0001370 secs][1 CMS-remark: 332201K(349568K)] 350416K(506816K), 0.0010521 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-03-26T09:55:46.720-0800: 0.805: [CMS-concurrent-sweep-start]
2021-03-26T09:55:46.720-0800: 0.805: [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.720-0800: 0.805: [CMS-concurrent-reset-start]
2021-03-26T09:55:46.720-0800: 0.805: [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.744-0800: 0.829: [GC (Allocation Failure) 2021-03-26T09:55:46.744-0800: 0.829: [ParNew: 156979K->156979K(157248K), 0.0000210 secs]2021-03-26T09:55:46.744-0800: 0.829: [CMS: 332129K->314341K(349568K), 0.0446097 secs] 489108K->314341K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0447240 secs] [Times: user=0.05 sys=0.00, real=0.04 secs]
2021-03-26T09:55:46.789-0800: 0.874: [GC (CMS Initial Mark) [1 CMS-initial-mark: 314341K(349568K)] 314880K(506816K), 0.0002986 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.789-0800: 0.874: [CMS-concurrent-mark-start]
2021-03-26T09:55:46.790-0800: 0.875: [CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
2021-03-26T09:55:46.790-0800: 0.875: [CMS-concurrent-preclean-start]
2021-03-26T09:55:46.791-0800: 0.876: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.791-0800: 0.876: [CMS-concurrent-abortable-preclean-start]
2021-03-26T09:55:46.815-0800: 0.900: [GC (Allocation Failure) 2021-03-26T09:55:46.815-0800: 0.900: [ParNew: 139776K->17470K(157248K), 0.0112295 secs] 454117K->359205K(506816K), 0.0113065 secs] [Times: user=0.10 sys=0.00, real=0.01 secs]
2021-03-26T09:55:46.826-0800: 0.911: [CMS-concurrent-abortable-preclean: 0.001/0.035 secs] [Times: user=0.13 sys=0.00, real=0.03 secs]
2021-03-26T09:55:46.826-0800: 0.911: [GC (CMS Final Remark) [YG occupancy: 17506 K (157248 K)]2021-03-26T09:55:46.826-0800: 0.911: [Rescan (parallel) , 0.0003459 secs]2021-03-26T09:55:46.827-0800: 0.912: [weak refs processing, 0.0000146 secs]2021-03-26T09:55:46.827-0800: 0.912: [class unloading, 0.0001730 secs]2021-03-26T09:55:46.827-0800: 0.912: [scrub symbol table, 0.0003155 secs]2021-03-26T09:55:46.827-0800: 0.912: [scrub string table, 0.0001393 secs][1 CMS-remark: 341734K(349568K)] 359241K(506816K), 0.0010510 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.827-0800: 0.912: [CMS-concurrent-sweep-start]
2021-03-26T09:55:46.828-0800: 0.913: [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.828-0800: 0.913: [CMS-concurrent-reset-start]
2021-03-26T09:55:46.828-0800: 0.913: [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.846-0800: 0.931: [GC (Allocation Failure) 2021-03-26T09:55:46.846-0800: 0.931: [ParNew: 157246K->157246K(157248K), 0.0000190 secs]2021-03-26T09:55:46.846-0800: 0.931: [CMS: 340924K->329074K(349568K), 0.0493668 secs] 498171K->329074K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0495760 secs] [Times: user=0.05 sys=0.00, real=0.05 secs]
2021-03-26T09:55:46.896-0800: 0.981: [GC (CMS Initial Mark) [1 CMS-initial-mark: 329074K(349568K)] 331889K(506816K), 0.0002455 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.896-0800: 0.981: [CMS-concurrent-mark-start]
2021-03-26T09:55:46.897-0800: 0.982: [CMS-concurrent-mark: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.897-0800: 0.982: [CMS-concurrent-preclean-start]
2021-03-26T09:55:46.898-0800: 0.983: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
2021-03-26T09:55:46.898-0800: 0.983: [CMS-concurrent-abortable-preclean-start]
2021-03-26T09:55:46.919-0800: 1.004: [GC (Allocation Failure) 2021-03-26T09:55:46.919-0800: 1.004: [ParNew: 139776K->139776K(157248K), 0.0000217 secs]2021-03-26T09:55:46.919-0800: 1.004: [CMS2021-03-26T09:55:46.919-0800: 1.004: [CMS-concurrent-abortable-preclean: 0.001/0.021 secs] [Times: user=0.03 sys=0.00, real=0.02 secs]
 (concurrent mode failure): 329074K->333088K(349568K), 0.0474783 secs] 468850K->333088K(506816K), [Metaspace: 2570K->2570K(1056768K)], 0.0475901 secs] [Times: user=0.04 sys=0.00, real=0.05 secs]
Heap
 par new generation   total 157248K, used 124877K [0x00000007a0000000, 0x00000007aaaa0000, 0x00000007aaaa0000)
  eden space 139776K,  89% used [0x00000007a0000000, 0x00000007a79f3558, 0x00000007a8880000)
  from space 17472K,   0% used [0x00000007a9990000, 0x00000007a9990000, 0x00000007aaaa0000)
  to   space 17472K,   0% used [0x00000007a8880000, 0x00000007a8880000, 0x00000007a9990000)
 concurrent mark-sweep generation total 349568K, used 333088K [0x00000007aaaa0000, 0x00000007c0000000, 0x00000007c0000000)
 Metaspace       used 2576K, capacity 4486K, committed 4864K, reserved 1056768K
  class space    used 276K, capacity 386K, committed 512K, reserved 1048576K
```

4.G1(全区域)
```
java -XX:+PrintGC -XX:+PrintGCDateStamps -Xmx512m -Xms512m -XX:+UseG1GC -Xloggc:g1GC.log GCLogAnalysis

Java HotSpot(TM) 64-Bit Server VM (25.281-b09) for bsd-amd64 JRE (1.8.0_281-b09), built on Dec  9 2020 12:44:49 by "java_re" with gcc 4.2.1 Compatible Apple LLVM 10.0.0 (clang-1000.11.45.5)
Memory: 4k page, physical 16777216k(355068k free)

/proc/meminfo:

CommandLine flags: -XX:InitialHeapSize=536870912 -XX:MaxHeapSize=536870912 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC

# 年轻代GC
# 年轻代压缩前后大小(年轻代总大小)
2021-03-26T12:57:13.265-0800: 0.104: [GC pause (G1 Evacuation Pause) (young) 30M->9973K(512M), 0.0045966 secs]
2021-03-26T12:57:13.279-0800: 0.118: [GC pause (G1 Evacuation Pause) (young) 37M->19M(512M), 0.0047267 secs]
2021-03-26T12:57:13.307-0800: 0.145: [GC pause (G1 Evacuation Pause) (young) 74M->39M(512M), 0.0057814 secs]
2021-03-26T12:57:13.334-0800: 0.173: [GC pause (G1 Evacuation Pause) (young) 111M->66M(512M), 0.0080524 secs]
2021-03-26T12:57:13.367-0800: 0.206: [GC pause (G1 Evacuation Pause) (young) 151M->95M(512M), 0.0086690 secs]
2021-03-26T12:57:13.525-0800: 0.364: [GC pause (G1 Evacuation Pause) (young)-- 450M->368M(512M), 0.0034272 secs]

# 年轻代GC
# 初始标记
2021-03-26T12:57:13.529-0800: 0.367: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 368M->368M(512M), 0.0011676 secs]
# 并发标记根region扫描
2021-03-26T12:57:13.530-0800: 0.368: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.530-0800: 0.368: [GC concurrent-root-region-scan-end, 0.0000660 secs]
# 并发标记
2021-03-26T12:57:13.530-0800: 0.368: [GC concurrent-mark-start]
2021-03-26T12:57:13.532-0800: 0.371: [GC concurrent-mark-end, 0.0022471 secs]
# 最终标记
2021-03-26T12:57:13.532-0800: 0.371: [GC remark, 0.0011980 secs]
2021-03-26T12:57:13.534-0800: 0.372: [GC cleanup 384M->383M(512M), 0.0007315 secs]
# 并发清理
2021-03-26T12:57:13.534-0800: 0.373: [GC concurrent-cleanup-start]
2021-03-26T12:57:13.534-0800: 0.373: [GC concurrent-cleanup-end, 0.0000084 secs]

2021-03-26T12:57:13.546-0800: 0.385: [GC pause (G1 Evacuation Pause) (young) 427M->386M(512M), 0.0030798 secs]

# 混合区域(年轻代(Eden+S0+S1)+老年代+Humongous)
2021-03-26T12:57:13.552-0800: 0.391: [GC pause (G1 Evacuation Pause) (mixed) 409M->345M(512M), 0.0026994 secs]
2021-03-26T12:57:13.559-0800: 0.398: [GC pause (G1 Evacuation Pause) (mixed) 368M->305M(512M), 0.0034342 secs]
2021-03-26T12:57:13.569-0800: 0.407: [GC pause (G1 Evacuation Pause) (mixed) 334M->273M(512M), 0.0031564 secs]
2021-03-26T12:57:13.576-0800: 0.414: [GC pause (G1 Evacuation Pause) (mixed) 298M->247M(512M), 0.0047528 secs]
2021-03-26T12:57:13.584-0800: 0.423: [GC pause (G1 Evacuation Pause) (mixed) 274M->240M(512M), 0.0034120 secs]
2021-03-26T12:57:13.588-0800: 0.427: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 241M->240M(512M), 0.0012589 secs]
2021-03-26T12:57:13.589-0800: 0.428: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.589-0800: 0.428: [GC concurrent-root-region-scan-end, 0.0001023 secs]
2021-03-26T12:57:13.589-0800: 0.428: [GC concurrent-mark-start]
2021-03-26T12:57:13.590-0800: 0.429: [GC concurrent-mark-end, 0.0008935 secs]
2021-03-26T12:57:13.590-0800: 0.429: [GC remark, 0.0013858 secs]
2021-03-26T12:57:13.592-0800: 0.431: [GC cleanup 246M->246M(512M), 0.0008882 secs]
2021-03-26T12:57:13.623-0800: 0.461: [GC pause (G1 Evacuation Pause) (young)-- 426M->378M(512M), 0.0041286 secs]
2021-03-26T12:57:13.628-0800: 0.466: [GC pause (G1 Evacuation Pause) (mixed) 385M->357M(512M), 0.0048664 secs]
2021-03-26T12:57:13.633-0800: 0.472: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 360M->357M(512M), 0.0012312 secs]
2021-03-26T12:57:13.634-0800: 0.473: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.634-0800: 0.473: [GC concurrent-root-region-scan-end, 0.0000585 secs]
2021-03-26T12:57:13.634-0800: 0.473: [GC concurrent-mark-start]
2021-03-26T12:57:13.635-0800: 0.474: [GC concurrent-mark-end, 0.0006411 secs]
2021-03-26T12:57:13.635-0800: 0.474: [GC remark, 0.0012302 secs]
2021-03-26T12:57:13.637-0800: 0.475: [GC cleanup 362M->362M(512M), 0.0006626 secs]
2021-03-26T12:57:13.647-0800: 0.485: [GC pause (G1 Evacuation Pause) (young) 424M->374M(512M), 0.0026839 secs]
2021-03-26T12:57:13.652-0800: 0.491: [GC pause (G1 Evacuation Pause) (mixed) 395M->333M(512M), 0.0031964 secs]
2021-03-26T12:57:13.659-0800: 0.498: [GC pause (G1 Evacuation Pause) (mixed) 356M->299M(512M), 0.0036552 secs]
2021-03-26T12:57:13.667-0800: 0.505: [GC pause (G1 Evacuation Pause) (mixed) 324M->290M(512M), 0.0036005 secs]
2021-03-26T12:57:13.671-0800: 0.509: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 293M->291M(512M), 0.0021696 secs]
2021-03-26T12:57:13.673-0800: 0.512: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.673-0800: 0.512: [GC concurrent-root-region-scan-end, 0.0001008 secs]
2021-03-26T12:57:13.673-0800: 0.512: [GC concurrent-mark-start]
2021-03-26T12:57:13.674-0800: 0.512: [GC concurrent-mark-end, 0.0006667 secs]
2021-03-26T12:57:13.674-0800: 0.512: [GC remark, 0.0014925 secs]
2021-03-26T12:57:13.675-0800: 0.514: [GC cleanup 300M->300M(512M), 0.0008980 secs]
2021-03-26T12:57:13.699-0800: 0.538: [GC pause (G1 Evacuation Pause) (young) 413M->320M(512M), 0.0045123 secs]
2021-03-26T12:57:13.706-0800: 0.545: [GC pause (G1 Evacuation Pause) (mixed) 337M->310M(512M), 0.0052175 secs]
2021-03-26T12:57:13.713-0800: 0.552: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 316M->312M(512M), 0.0015737 secs]
2021-03-26T12:57:13.715-0800: 0.553: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.715-0800: 0.553: [GC concurrent-root-region-scan-end, 0.0000908 secs]
2021-03-26T12:57:13.715-0800: 0.553: [GC concurrent-mark-start]
2021-03-26T12:57:13.716-0800: 0.554: [GC concurrent-mark-end, 0.0006859 secs]
2021-03-26T12:57:13.716-0800: 0.554: [GC remark, 0.0016375 secs]
2021-03-26T12:57:13.717-0800: 0.556: [GC cleanup 318M->318M(512M), 0.0009412 secs]
2021-03-26T12:57:13.731-0800: 0.570: [GC pause (G1 Evacuation Pause) (young) 411M->336M(512M), 0.0040541 secs]
2021-03-26T12:57:13.737-0800: 0.576: [GC pause (G1 Evacuation Pause) (mixed) 355M->317M(512M), 0.0055903 secs]
2021-03-26T12:57:13.747-0800: 0.586: [GC pause (G1 Evacuation Pause) (mixed) 343M->325M(512M), 0.0032005 secs]
2021-03-26T12:57:13.751-0800: 0.589: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 327M->326M(512M), 0.0021910 secs]
2021-03-26T12:57:13.753-0800: 0.592: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.753-0800: 0.592: [GC concurrent-root-region-scan-end, 0.0001232 secs]
2021-03-26T12:57:13.753-0800: 0.592: [GC concurrent-mark-start]
2021-03-26T12:57:13.754-0800: 0.593: [GC concurrent-mark-end, 0.0009784 secs]
2021-03-26T12:57:13.754-0800: 0.593: [GC remark, 0.0015811 secs]
2021-03-26T12:57:13.756-0800: 0.595: [GC cleanup 335M->334M(512M), 0.0009225 secs]
2021-03-26T12:57:13.757-0800: 0.596: [GC concurrent-cleanup-start]
2021-03-26T12:57:13.757-0800: 0.596: [GC concurrent-cleanup-end, 0.0000102 secs]
2021-03-26T12:57:13.767-0800: 0.606: [GC pause (G1 Evacuation Pause) (young) 405M->347M(512M), 0.0038038 secs]
2021-03-26T12:57:13.775-0800: 0.613: [GC pause (G1 Evacuation Pause) (mixed) 365M->324M(512M), 0.0048114 secs]
2021-03-26T12:57:13.784-0800: 0.622: [GC pause (G1 Evacuation Pause) (mixed) 352M->332M(512M), 0.0028675 secs]
2021-03-26T12:57:13.787-0800: 0.626: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 334M->332M(512M), 0.0016154 secs]
2021-03-26T12:57:13.789-0800: 0.627: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.789-0800: 0.627: [GC concurrent-root-region-scan-end, 0.0000852 secs]
2021-03-26T12:57:13.789-0800: 0.627: [GC concurrent-mark-start]
2021-03-26T12:57:13.789-0800: 0.628: [GC concurrent-mark-end, 0.0006766 secs]
2021-03-26T12:57:13.790-0800: 0.628: [GC remark, 0.0014677 secs]
2021-03-26T12:57:13.791-0800: 0.630: [GC cleanup 340M->340M(512M), 0.0008088 secs]
2021-03-26T12:57:13.804-0800: 0.643: [GC pause (G1 Evacuation Pause) (young) 412M->350M(512M), 0.0040686 secs]
2021-03-26T12:57:13.811-0800: 0.650: [GC pause (G1 Evacuation Pause) (mixed) 369M->335M(512M), 0.0051359 secs]
2021-03-26T12:57:13.817-0800: 0.656: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 337M->335M(512M), 0.0016636 secs]
2021-03-26T12:57:13.819-0800: 0.658: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.819-0800: 0.658: [GC concurrent-root-region-scan-end, 0.0000860 secs]
2021-03-26T12:57:13.819-0800: 0.658: [GC concurrent-mark-start]
2021-03-26T12:57:13.820-0800: 0.658: [GC concurrent-mark-end, 0.0008658 secs]
2021-03-26T12:57:13.820-0800: 0.659: [GC remark, 0.0016513 secs]
2021-03-26T12:57:13.822-0800: 0.660: [GC cleanup 341M->341M(512M), 0.0008046 secs]
2021-03-26T12:57:13.834-0800: 0.673: [GC pause (G1 Evacuation Pause) (young) 410M->358M(512M), 0.0046313 secs]
2021-03-26T12:57:13.842-0800: 0.681: [GC pause (G1 Evacuation Pause) (mixed) 380M->348M(512M), 0.0044818 secs]
2021-03-26T12:57:13.847-0800: 0.685: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 349M->348M(512M), 0.0015753 secs]
2021-03-26T12:57:13.848-0800: 0.687: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.848-0800: 0.687: [GC concurrent-root-region-scan-end, 0.0001026 secs]
2021-03-26T12:57:13.848-0800: 0.687: [GC concurrent-mark-start]
2021-03-26T12:57:13.849-0800: 0.688: [GC concurrent-mark-end, 0.0006668 secs]
2021-03-26T12:57:13.849-0800: 0.688: [GC remark, 0.0017160 secs]
2021-03-26T12:57:13.851-0800: 0.690: [GC cleanup 351M->351M(512M), 0.0007282 secs]
2021-03-26T12:57:13.860-0800: 0.699: [GC pause (G1 Evacuation Pause) (young) 415M->370M(512M), 0.0038506 secs]
2021-03-26T12:57:13.867-0800: 0.705: [GC pause (G1 Evacuation Pause) (mixed) 393M->354M(512M), 0.0054553 secs]
2021-03-26T12:57:13.876-0800: 0.715: [GC pause (G1 Evacuation Pause) (mixed) 380M->360M(512M), 0.0030639 secs]
2021-03-26T12:57:13.880-0800: 0.718: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 361M->360M(512M), 0.0017208 secs]
2021-03-26T12:57:13.881-0800: 0.720: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.881-0800: 0.720: [GC concurrent-root-region-scan-end, 0.0001143 secs]
2021-03-26T12:57:13.882-0800: 0.720: [GC concurrent-mark-start]
2021-03-26T12:57:13.882-0800: 0.721: [GC concurrent-mark-end, 0.0009394 secs]
2021-03-26T12:57:13.883-0800: 0.721: [GC remark, 0.0015445 secs]
2021-03-26T12:57:13.884-0800: 0.723: [GC cleanup 365M->365M(512M), 0.0008975 secs]
2021-03-26T12:57:13.893-0800: 0.732: [GC pause (G1 Evacuation Pause) (young) 407M->372M(512M), 0.0029127 secs]
2021-03-26T12:57:13.901-0800: 0.740: [GC pause (G1 Evacuation Pause) (mixed) 397M->355M(512M), 0.0053817 secs]
2021-03-26T12:57:13.912-0800: 0.750: [GC pause (G1 Evacuation Pause) (mixed) 377M->356M(512M), 0.0035557 secs]
2021-03-26T12:57:13.916-0800: 0.755: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 358M->357M(512M), 0.0021935 secs]
2021-03-26T12:57:13.919-0800: 0.757: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.919-0800: 0.757: [GC concurrent-root-region-scan-end, 0.0001031 secs]
2021-03-26T12:57:13.919-0800: 0.757: [GC concurrent-mark-start]
2021-03-26T12:57:13.919-0800: 0.758: [GC concurrent-mark-end, 0.0006946 secs]
2021-03-26T12:57:13.919-0800: 0.758: [GC remark, 0.0015710 secs]
2021-03-26T12:57:13.921-0800: 0.760: [GC cleanup 363M->363M(512M), 0.0006858 secs]
2021-03-26T12:57:13.927-0800: 0.766: [GC pause (G1 Evacuation Pause) (young) 406M->370M(512M), 0.0035516 secs]
2021-03-26T12:57:13.935-0800: 0.773: [GC pause (G1 Evacuation Pause) (mixed) 398M->359M(512M), 0.0082492 secs]
2021-03-26T12:57:13.947-0800: 0.786: [GC pause (G1 Evacuation Pause) (mixed) 388M->369M(512M), 0.0037215 secs]
2021-03-26T12:57:13.952-0800: 0.790: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 371M->369M(512M), 0.0016110 secs]
2021-03-26T12:57:13.953-0800: 0.792: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.953-0800: 0.792: [GC concurrent-root-region-scan-end, 0.0001325 secs]
2021-03-26T12:57:13.953-0800: 0.792: [GC concurrent-mark-start]
2021-03-26T12:57:13.954-0800: 0.793: [GC concurrent-mark-end, 0.0008823 secs]
2021-03-26T12:57:13.954-0800: 0.793: [GC remark, 0.0012798 secs]
2021-03-26T12:57:13.956-0800: 0.794: [GC cleanup 378M->378M(512M), 0.0006666 secs]
2021-03-26T12:57:13.963-0800: 0.801: [GC pause (G1 Evacuation Pause) (young) 407M->379M(512M), 0.0019142 secs]
2021-03-26T12:57:13.968-0800: 0.806: [GC pause (G1 Evacuation Pause) (mixed) 406M->365M(512M), 0.0049579 secs]
2021-03-26T12:57:13.974-0800: 0.813: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 375M->366M(512M), 0.0014421 secs]
2021-03-26T12:57:13.975-0800: 0.814: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.976-0800: 0.814: [GC concurrent-root-region-scan-end, 0.0000887 secs]
2021-03-26T12:57:13.976-0800: 0.814: [GC concurrent-mark-start]
2021-03-26T12:57:13.976-0800: 0.815: [GC concurrent-mark-end, 0.0006091 secs]
2021-03-26T12:57:13.976-0800: 0.815: [GC remark, 0.0011107 secs]
2021-03-26T12:57:13.977-0800: 0.816: [GC cleanup 371M->371M(512M), 0.0005491 secs]
2021-03-26T12:57:13.981-0800: 0.820: [GC pause (G1 Evacuation Pause) (young) 403M->378M(512M), 0.0027068 secs]
2021-03-26T12:57:13.987-0800: 0.826: [GC pause (G1 Evacuation Pause) (mixed) 406M->372M(512M), 0.0055084 secs]
2021-03-26T12:57:13.993-0800: 0.832: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 373M->372M(512M), 0.0017301 secs]
2021-03-26T12:57:13.995-0800: 0.833: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:13.995-0800: 0.833: [GC concurrent-root-region-scan-end, 0.0000108 secs]
2021-03-26T12:57:13.995-0800: 0.833: [GC concurrent-mark-start]
2021-03-26T12:57:13.996-0800: 0.834: [GC concurrent-mark-end, 0.0007865 secs]
2021-03-26T12:57:13.996-0800: 0.834: [GC remark, 0.0020487 secs]
2021-03-26T12:57:13.998-0800: 0.837: [GC cleanup 379M->379M(512M), 0.0009028 secs]
2021-03-26T12:57:14.006-0800: 0.844: [GC pause (G1 Evacuation Pause) (young) 407M->381M(512M), 0.0026676 secs]
2021-03-26T12:57:14.011-0800: 0.850: [GC pause (G1 Evacuation Pause) (mixed) 405M->375M(512M), 0.0058001 secs]
2021-03-26T12:57:14.017-0800: 0.856: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 376M->375M(512M), 0.0023317 secs]
2021-03-26T12:57:14.020-0800: 0.858: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.020-0800: 0.858: [GC concurrent-root-region-scan-end, 0.0000132 secs]
2021-03-26T12:57:14.020-0800: 0.858: [GC concurrent-mark-start]
2021-03-26T12:57:14.021-0800: 0.860: [GC concurrent-mark-end, 0.0010486 secs]
2021-03-26T12:57:14.021-0800: 0.860: [GC remark, 0.0023737 secs]
2021-03-26T12:57:14.024-0800: 0.862: [GC cleanup 383M->383M(512M), 0.0009878 secs]
2021-03-26T12:57:14.028-0800: 0.867: [GC pause (G1 Evacuation Pause) (young) 409M->385M(512M), 0.0025931 secs]
2021-03-26T12:57:14.036-0800: 0.875: [GC pause (G1 Evacuation Pause) (mixed) 411M->377M(512M), 0.0042705 secs]
2021-03-26T12:57:14.041-0800: 0.879: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 378M->377M(512M), 0.0016860 secs]
2021-03-26T12:57:14.043-0800: 0.881: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.043-0800: 0.881: [GC concurrent-root-region-scan-end, 0.0000780 secs]
2021-03-26T12:57:14.043-0800: 0.881: [GC concurrent-mark-start]
2021-03-26T12:57:14.043-0800: 0.882: [GC concurrent-mark-end, 0.0007314 secs]
2021-03-26T12:57:14.044-0800: 0.882: [GC remark, 0.0017255 secs]
2021-03-26T12:57:14.045-0800: 0.884: [GC cleanup 380M->380M(512M), 0.0008637 secs]
2021-03-26T12:57:14.050-0800: 0.888: [GC pause (G1 Evacuation Pause) (young) 407M->388M(512M), 0.0026658 secs]
2021-03-26T12:57:14.058-0800: 0.897: [GC pause (G1 Evacuation Pause) (mixed) 414M->380M(512M), 0.0052168 secs]
2021-03-26T12:57:14.064-0800: 0.903: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 384M->380M(512M), 0.0015466 secs]
2021-03-26T12:57:14.066-0800: 0.904: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.066-0800: 0.904: [GC concurrent-root-region-scan-end, 0.0001010 secs]
2021-03-26T12:57:14.066-0800: 0.904: [GC concurrent-mark-start]
2021-03-26T12:57:14.067-0800: 0.905: [GC concurrent-mark-end, 0.0007120 secs]
2021-03-26T12:57:14.067-0800: 0.905: [GC remark, 0.0015837 secs]
2021-03-26T12:57:14.068-0800: 0.907: [GC cleanup 383M->383M(512M), 0.0009124 secs]
2021-03-26T12:57:14.075-0800: 0.913: [GC pause (G1 Evacuation Pause) (young) 409M->387M(512M), 0.0022770 secs]
2021-03-26T12:57:14.081-0800: 0.919: [GC pause (G1 Evacuation Pause) (mixed)-- 413M->382M(512M), 0.0053249 secs]
2021-03-26T12:57:14.087-0800: 0.926: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 389M->383M(512M), 0.0018474 secs]
2021-03-26T12:57:14.089-0800: 0.928: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.089-0800: 0.928: [GC concurrent-root-region-scan-end, 0.0000780 secs]
2021-03-26T12:57:14.089-0800: 0.928: [GC concurrent-mark-start]
2021-03-26T12:57:14.090-0800: 0.929: [GC concurrent-mark-end, 0.0007517 secs]
2021-03-26T12:57:14.090-0800: 0.929: [GC remark, 0.0015635 secs]
2021-03-26T12:57:14.092-0800: 0.931: [GC cleanup 389M->389M(512M), 0.0007633 secs]
2021-03-26T12:57:14.095-0800: 0.934: [GC pause (G1 Evacuation Pause) (young) 412M->388M(512M), 0.0022535 secs]
2021-03-26T12:57:14.104-0800: 0.943: [GC pause (G1 Evacuation Pause) (mixed)-- 417M->387M(512M), 0.0049373 secs]
2021-03-26T12:57:14.109-0800: 0.948: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 387M->386M(512M), 0.0015349 secs]
2021-03-26T12:57:14.111-0800: 0.949: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.111-0800: 0.950: [GC concurrent-root-region-scan-end, 0.0000794 secs]
2021-03-26T12:57:14.111-0800: 0.950: [GC concurrent-mark-start]
2021-03-26T12:57:14.112-0800: 0.950: [GC concurrent-mark-end, 0.0006515 secs]
2021-03-26T12:57:14.112-0800: 0.950: [GC remark, 0.0012868 secs]
2021-03-26T12:57:14.113-0800: 0.952: [GC cleanup 394M->394M(512M), 0.0005770 secs]
2021-03-26T12:57:14.118-0800: 0.956: [GC pause (G1 Evacuation Pause) (young) 419M->397M(512M), 0.0021357 secs]
2021-03-26T12:57:14.122-0800: 0.961: [GC pause (G1 Evacuation Pause) (mixed)-- 424M->397M(512M), 0.0036929 secs]
2021-03-26T12:57:14.126-0800: 0.965: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 398M->398M(512M), 0.0010796 secs]
2021-03-26T12:57:14.127-0800: 0.966: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.128-0800: 0.966: [GC concurrent-root-region-scan-end, 0.0000372 secs]
2021-03-26T12:57:14.128-0800: 0.966: [GC concurrent-mark-start]
2021-03-26T12:57:14.128-0800: 0.967: [GC concurrent-mark-end, 0.0009024 secs]
2021-03-26T12:57:14.129-0800: 0.967: [GC remark, 0.0010301 secs]
2021-03-26T12:57:14.130-0800: 0.968: [GC cleanup 410M->410M(512M), 0.0003959 secs]
2021-03-26T12:57:14.132-0800: 0.971: [GC pause (G1 Evacuation Pause) (young) 430M->406M(512M), 0.0017881 secs]
2021-03-26T12:57:14.139-0800: 0.977: [GC pause (G1 Evacuation Pause) (mixed)-- 437M->430M(512M), 0.0024865 secs]
2021-03-26T12:57:14.143-0800: 0.982: [GC pause (G1 Evacuation Pause) (mixed)-- 451M->442M(512M), 0.0013259 secs]
2021-03-26T12:57:14.145-0800: 0.984: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 448M->442M(512M), 0.0007670 secs]
2021-03-26T12:57:14.146-0800: 0.985: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.146-0800: 0.985: [GC concurrent-root-region-scan-end, 0.0000611 secs]
2021-03-26T12:57:14.146-0800: 0.985: [GC concurrent-mark-start]
2021-03-26T12:57:14.147-0800: 0.986: [GC concurrent-mark-end, 0.0007760 secs]
2021-03-26T12:57:14.147-0800: 0.986: [GC remark, 0.0010272 secs]
2021-03-26T12:57:14.148-0800: 0.987: [GC cleanup 451M->451M(512M), 0.0003890 secs]
2021-03-26T12:57:14.149-0800: 0.988: [GC pause (G1 Evacuation Pause) (young)-- 453M->450M(512M), 0.0007475 secs]
2021-03-26T12:57:14.151-0800: 0.989: [GC pause (G1 Humongous Allocation) (mixed)-- 453M->452M(512M), 0.0008068 secs]
2021-03-26T12:57:14.152-0800: 0.990: [GC pause (G1 Humongous Allocation) (mixed)-- 453M->453M(512M), 0.0007182 secs]
2021-03-26T12:57:14.153-0800: 0.991: [GC pause (G1 Evacuation Pause) (mixed)-- 453M->453M(512M), 0.0008316 secs]
2021-03-26T12:57:14.153-0800: 0.992: [GC pause (G1 Evacuation Pause) (young) 453M->453M(512M), 0.0004973 secs]
2021-03-26T12:57:14.154-0800: 0.993: [Full GC (Allocation Failure)  453M->331M(512M), 0.0315475 secs]
2021-03-26T12:57:14.186-0800: 1.025: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 333M->331M(512M), 0.0007385 secs]
2021-03-26T12:57:14.187-0800: 1.025: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.187-0800: 1.025: [GC concurrent-root-region-scan-end, 0.0000511 secs]
2021-03-26T12:57:14.187-0800: 1.025: [GC concurrent-mark-start]
2021-03-26T12:57:14.187-0800: 1.026: [GC concurrent-mark-end, 0.0006077 secs]
2021-03-26T12:57:14.187-0800: 1.026: [GC remark, 0.0009704 secs]
2021-03-26T12:57:14.188-0800: 1.027: [GC cleanup 336M->336M(512M), 0.0003300 secs]
2021-03-26T12:57:14.192-0800: 1.031: [GC pause (G1 Evacuation Pause) (young) 373M->344M(512M), 0.0019711 secs]
2021-03-26T12:57:14.195-0800: 1.033: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 345M->343M(512M), 0.0010553 secs]
2021-03-26T12:57:14.196-0800: 1.034: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.196-0800: 1.034: [GC concurrent-root-region-scan-end, 0.0000592 secs]
2021-03-26T12:57:14.196-0800: 1.034: [GC concurrent-mark-start]
2021-03-26T12:57:14.197-0800: 1.035: [GC concurrent-mark-end, 0.0008557 secs]
2021-03-26T12:57:14.197-0800: 1.035: [GC remark, 0.0009550 secs]
2021-03-26T12:57:14.198-0800: 1.036: [GC cleanup 354M->354M(512M), 0.0004571 secs]
2021-03-26T12:57:14.201-0800: 1.039: [GC pause (G1 Evacuation Pause) (young) 377M->353M(512M), 0.0019710 secs]
2021-03-26T12:57:14.207-0800: 1.045: [GC pause (G1 Evacuation Pause) (mixed) 379M->360M(512M), 0.0020073 secs]
2021-03-26T12:57:14.209-0800: 1.047: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 360M->360M(512M), 0.0012971 secs]
2021-03-26T12:57:14.210-0800: 1.049: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.210-0800: 1.049: [GC concurrent-root-region-scan-end, 0.0000423 secs]
2021-03-26T12:57:14.210-0800: 1.049: [GC concurrent-mark-start]
2021-03-26T12:57:14.211-0800: 1.049: [GC concurrent-mark-end, 0.0008196 secs]
2021-03-26T12:57:14.211-0800: 1.050: [GC remark, 0.0012193 secs]
2021-03-26T12:57:14.212-0800: 1.051: [GC cleanup 368M->368M(512M), 0.0005148 secs]
2021-03-26T12:57:14.216-0800: 1.055: [GC pause (G1 Evacuation Pause) (young) 388M->366M(512M), 0.0017536 secs]
2021-03-26T12:57:14.221-0800: 1.060: [GC pause (G1 Evacuation Pause) (mixed) 392M->363M(512M), 0.0037479 secs]
2021-03-26T12:57:14.225-0800: 1.064: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 364M->363M(512M), 0.0023532 secs]
2021-03-26T12:57:14.228-0800: 1.066: [GC concurrent-root-region-scan-start]
2021-03-26T12:57:14.228-0800: 1.066: [GC concurrent-root-region-scan-end, 0.0000713 secs]
2021-03-26T12:57:14.228-0800: 1.066: [GC concurrent-mark-start]
2021-03-26T12:57:14.229-0800: 1.067: [GC concurrent-mark-end, 0.0008981 secs]
2021-03-26T12:57:14.229-0800: 1.067: [GC remark, 0.0015789 secs]
2021-03-26T12:57:14.230-0800: 1.069: [GC cleanup 372M->372M(512M), 0.0007949 secs]
2021-03-26T12:57:14.233-0800: 1.072: [GC pause (G1 Evacuation Pause) (young) 396M->370M(512M), 0.0024937 secs]
2021-03-26T12:57:14.240-0800: 1.079: [GC pause (G1 Evacuation Pause) (mixed) 396M->368M(512M), 0.0039471 secs]
```

### 各种GC有什么特点和使用场景
1.Serial（年轻代）

特点: 
- 单线程串行
- 执行期间完全停顿用户线程
- 标记-复制

适用: 
- 单CPU环境下的客户端模式的虚拟机

2.SerialOld （老年代）

特点:
- 单线程串行
- 执行期间完全停顿用户线程
- 标记-整理

适用: 
- 单CPU环境下的客户端模式的虚拟机
- CMS的后备预案

2.ParNew（年轻代）

特点: 
- 多线程并行
- 执行期间完全停顿用户线程
- 标记-复制

适用: 
- 多CPU环境时在Server模式下与CMS配合

3.Parallel Scavenge（年轻代）

特点: 
- 多线程并行
- 执行期间完全停顿用户线程
- 标记-复制
- 关注点为达到一个可控的吞吐量

适用:
- 关注吞吐量的系统
- 在后台运算不需要太多交互的任务

4.Parallel Old（老年代）

特点:
- 多线程并行
- 执行期间完全停顿用户线程
- 标记-整理
- 关注点为达到一个可控的吞吐量

适用:
- 关注吞吐量的系统
- 在后台运算不需要太多交互的任务

5.CMS（老年代）

特点:
- 低停顿
- 部分阶段能够与用户线程并发执行，部分阶段必须停顿用户线程
- 标记-清除
- 容易造成内存碎片，导致FullGC
- 对处理器资源敏感
- 易产生浮动垃圾，可能出现并发失败，导致Full GC

适用:
- 对延迟比较敏感的系统(如与用户交互的B/S系统，高频交易系统等)

6.G1（全功能）

特点:
- Region内存布局
- 面向局部收集的设计思路
- 建立停顿时间模型，可以指定最大停顿时间
- 标记-整理
- 不追求一次清理干净，追求能够应付应用的存储分配速率
- 每个Region必须有一份卡表，导致维护跨区引用的记忆集使用额外内存较大
- 同时使用写前和写后屏障，执行负载高
- 部分阶段可以与用户线程并发执行，部分阶段必须停顿用户线程

适用:
- 需要可预期停顿时间的系统
- 对延迟比较敏感的系统，替换CMS


### Java各版本默认GC
Java7 Parallel Scavenge（新生代）+Parallel Old（老年代）

Java8 Parallel Scavenge（新生代）+Parallel Old（老年代）

Java9-Java12 Garbage-First (G1)
