package com.tone.mvc;

import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class ReactorTest {
    @Test
    public void test1() {
        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);
        final Random random = new Random();

        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);

        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);

    }

    @Test
    public void testFluxWindow() {
        // window 操作符的作用类似于 buffer，所不同的是 window 操作符是把当前流中的元素收集到另外的 Flux 序列中，因此返回值类型是 Flux<Flux<T>>。
        // 下面两行语句的输出结果分别是 5 个和 2 个 UnicastProcessor 字符。
        // 这是因为 window 操作符所产生的流中包含的是 UnicastProcessor 类的对象，
        // 而 UnicastProcessor 类的 toString 方法输出的就是 UnicastProcessor 字符。

        Flux.range(1, 100).window(20).subscribe(System.out::println);
        Flux.interval(Duration.ofMillis(100)).window(Duration.ofMillis(1001)).take(2).toStream().forEach(System.out::println);
    }

    @Test
    public void zipWith() {
        //zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并。
        // 在合并时可以不做任何处理，由此得到的是一个元素类型为 Tuple2 的流；
        // 也可以通过一个 BiFunction 函数对合并的元素进行处理，所得到的流的元素类型为该函数的返回值。。

        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2))
                .subscribe(System.out::println);
    }

    @Test
    public void take() {
        //take 系列操作符用来从当前流中提取元素。提取的方式可以有很多种。
        //
        //take(long n)，take(Duration timespan)和 takeMillis(long timespan)：按照指定的数量或时间间隔来提取。
        //takeLast(long n)：提取流中的最后 N 个元素。
        //takeUntil(Predicate<? super T> predicate)：提取元素直到 Predicate 返回 true。
        //takeWhile(Predicate<? super T> continuePredicate)： 当 Predicate 返回 true 时才进行提取。
        //takeUntilOther(Publisher<?> other)：提取元素直到另外一个流开始产生元素。
        //第一行语句输出的是数字 1 到 10；
        // 第二行语句输出的是数字 991 到 1000；
        // 第三行语句输出的是数字 1 到 9；
        // 第四行语句输出的是数字 1 到 10，使得 Predicate 返回 true 的元素也是包含在内的。。

        Flux.range(1, 1000).take(10).subscribe(System.out::println);
        Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
        Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
        Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);
    }

    @Test
    public void reduce() {
        //reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。
        // 累积操作是通过一个 BiFunction 来表示的。在操作时可以指定一个初始值。
        // 如果没有初始值，则序列的第一个元素作为初始值。
        //在代码中，第一行语句对流中的元素进行相加操作，结果为 5050；
        // 第二行语句同样也是进行相加操作，不过通过一个 Supplier 给出了初始值为 100，所以结果为 5150。

        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
    }

    @Test
    public void merge() {
        //merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。
        // 不同之处在于 merge 按照所有流中元素的实际产生顺序来合并，
        // 而 mergeSequential 则按照所有流被订阅的顺序，以流为单位进行合并。
        //
        //代码清单 11 中分别使用了 merge 和 mergeSequential 操作符。
        // 进行合并的流都是每隔 100 毫秒产生一个元素，不过第二个流中的每个元素的产生都比第一个流要延迟 50 毫秒。
        // 在使用 merge 的结果流中，来自两个流的元素是按照时间顺序交织在一起；
        // 而使用 mergeSequential 的结果流则是首先产生第一个流中的全部元素，再产生第二个流中的全部元素。

        Flux.merge(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);

        Flux.mergeSequential(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void flatMap() {
        //flatMap 和 flatMapSequential 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并。
        // flatMapSequential 和 flatMap 之间的区别与 mergeSequential 和 merge 之间的区别是一样的。
        //
        //在代码中，流中的元素被转换成每隔 100 毫秒产生的数量不同的流，再进行合并。
        // 由于第一个流中包含的元素数量较少，所以在结果流中一开始是两个流的元素交织在一起，然后就只有第二个流中的元素。
        Flux.just(5, 10)
                .flatMap(x -> Flux.interval(Duration.ofMillis(x * 10), Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void concatMap() {
        //concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并。
        // 与 flatMap 不同的是，concatMap 会根据原始流中的元素顺序依次把转换之后的流进行合并；
        // 与 flatMapSequential 不同的是，concatMap 对转换之后的流的订阅是动态进行的，
        // 而 flatMapSequential 在合并之前就已经订阅了所有的流。
        //
        //代码，只不过把 flatMap 换成了 concatMap，结果流中依次包含了第一个流和第二个流中的全部元素。
        Flux.just(5, 10)
                .concatMap(x -> Flux.interval(Duration.ofMillis(x * 10), Duration.ofMillis(100)).take(x))
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void combineLatest() {
        //combineLatest 操作符把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。
        // 只要其中任何一个流中产生了新的元素，合并操作就会被执行一次，结果流中就会产生新的元素。
        // 在 代码 中，流中最新产生的元素会被收集到一个数组中，通过 Arrays.toString 方法来把数组转换成 String。
        Flux.combineLatest(Arrays::toString,
                Flux.interval(Duration.ofMillis(100)).take(5),
                Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void subscribe() {
        //当需要处理 Flux 或 Mono 中的消息时，如之前的代码清单所示，可以通过 subscribe 方法来添加相应的订阅逻辑。
        // 在调用 subscribe 方法时可以指定需要处理的消息类型。
        // 可以只处理其中包含的正常消息，也可以同时处理错误消息和完成消息。
        // 代码 中通过 subscribe()方法同时处理了正常消息和错误消息。
//        Flux.just(1, 2)
//                .concatWith(Mono.error(new IllegalStateException()))
//                .subscribe(System.out::println, System.err::println);

        //正常的消息处理相对简单。当出现错误时，有多种不同的处理策略。
        // 第一种策略是通过 onErrorReturn()方法返回一个默认值。
        // 在代码 中，当出现错误时，流会产生默认值 0.
//        Flux.just(1, 2)
//                .concatWith(Mono.error(new IllegalStateException()))
//                .onErrorReturn(0)
//                .subscribe(System.out::println);

        //通过 onErrorResumeWith()方法来根据不同的异常类型来选择要使用的产生元素的流。
        // 在代码 中，根据异常类型来返回不同的流作为出现错误时的数据来源。
        // 因为异常的类型为 IllegalArgumentException。
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println);
    }


    @Test
    public void autoConnect() throws InterruptedException {
        //之前的代码清单中所创建的都是冷序列。冷序列的含义是不论订阅者在何时订阅该序列，总是能收到序列中产生的全部消息。而与之对应的热序列，则是在持续不断地产生消息，订阅者只能获取到在其订阅之后产生的消息。
        //
        //在代码 中，原始的序列中包含 10 个间隔为 1 秒的元素。
        // 通过 publish()方法把一个 Flux 对象转换成 ConnectableFlux 对象。
        // 方法 autoConnect()的作用是当 ConnectableFlux 对象有一个订阅者时就开始产生消息。
        // 代码 source.subscribe()的作用是订阅该 ConnectableFlux 对象，让其开始产生数据。
        // 接着当前线程睡眠 5 秒钟，第二个订阅者此时只能获得到该序列中的后 5 个元素，因此所输出的是数字 5 到 9。
        final Flux<Long> source = Flux.interval(Duration.ofMillis(1000))
                .take(10)
                .publish()
                .autoConnect();
        //第一个订阅者产生数据
        source.subscribe();
        //休眠5s
        Thread.sleep(5000);
        //第二订阅者
        source
                .toStream()
                .forEach(System.out::println);
    }

    @Test
    public void testMono() {
        Mono.fromSupplier(() -> "Hello").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("Hello")).subscribe(System.out::println);
        Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);
    }
}
