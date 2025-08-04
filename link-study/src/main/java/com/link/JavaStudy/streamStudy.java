package com.link.JavaStudy;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class streamStudy {
    /*
        filter(Predicate<T> predicate)：过滤流中的元素，只保留符合条件的元素。
        map(Function<T, R> mapper)：将流中的元素按照指定的方式进行映射，返回一个新的流。
        flatMap(Function<T, Stream<R>> mapper)：将流中的元素按照指定的方式进行映射，然后将所有映射结果合并成一个流。
        limit(long maxSize)：截取流中的前maxSize个元素。
        skip(long n)：跳过流中的前n个元素。
        sorted()：对流中的元素进行排序。
        distinct()：去重，去除流中重复的元素。
        forEach(Consumer<T> action)：对流中的每个元素执行指定的操作。
        reduce(T identity, BinaryOperator<T> accumulator)：将流中的元素按照指定的方式进行累加，返回一个Optional对象。
        collect(Collector<T, A, R> collector)：将流中的元素收集到一个集合中。
        concat 合并a和b两个流为一个流
    * */


    public static void main(String[] args) {
        List<String> list = Arrays.asList("a","bb","bb","ccc");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Stream<String> stream = list.stream();
        Stream<String> stringStream = list.parallelStream();



        // 按规则过滤
//        filterStream(list);

        // 分页要多少个
//        limitStream(list);

        // 跳过前x个
//        skipStream(list);

        // 去重
        distinctStream(list);

        // 排序
//        sortedStream(list);

        // 合流
//        concatStream(list,numbers);

        // 遍历
//        forEachStream(list);

        //合到集合中
//        collectStream();

        TestStream();

    }

    private static void distinctStream(List<String> list) {
        list.stream().distinct().forEach(System.out::println);
        System.out.println("去重结束");
    }

    private static void TestStream() {
        ArrayList<Integer> list = new ArrayList<>();
        Collections.addAll(list,1,2,3,4,5,6,7,8,9,10);
        // 0,过滤奇数,只留下偶数
        list.stream().filter(s->s%2==0).forEach(System.out::println);
        List<Actor>  actors = new ArrayList<>();
        boolean a = false;
        List<String> listMan = Arrays.asList("蔡坤坤,24" , "叶齁咸,23", "刘不甜,22", "吴签,24", "谷嘉,30", "肖梁梁,27");
        List<String> listWMan = Arrays.asList("赵小颖,35" , "杨颖,36", "高元元,43", "张天天,31", "刘诗,35", "杨小幂,33");


        for (String M : listMan) {
            String[] split = M.split(",");
            Actor act = new Actor();
            act.setName(split[0]);
            act.setAge(Integer.parseInt(split[1]));
            act.setSex("男");
            actors.add(act);
        }

        for (String WM : listWMan) {
            String[] split = WM.split(",");
            Actor act = new Actor();
            act.setName(split[0]);
            act.setAge(Integer.parseInt(split[1]));
            act.setSex("女");
            actors.add(act);
        }
        actors.forEach(System.out::println);
        System.out.println("完成用例输出");
        System.out.println("1，男演员只要名字为3个字的前两人");
        List<Actor> listM = actors.stream().filter(s-> s.getName().length()==3).filter(s -> Objects.equals(s.sex, "男")).collect(Collectors.toList());
        listM.forEach(System.out::println);
        System.out.println("2，女演员只要姓杨的，并且不要第一个");
        List<Actor> listWM = actors.stream().filter(s -> Objects.equals(s.getSex(), "女")).filter(s -> Objects.equals(s.getName().substring(0,1),"杨")).skip(1).collect(Collectors.toList());
        listWM.forEach(System.out::println);
        System.out.println("3，把过滤后的男演员姓名和女演员姓名合并到一起");
        List<Actor> listAll = Stream.concat(listM.stream(),listWM.stream()).collect(Collectors.toList());
        listAll.forEach(System.out::println);
        System.out.println("4，将上一步的演员信息封装成Actor对象。");
        System.out.println(actors);
        System.out.println("5，将所有的演员对象都保存到List集合中。");
        System.out.println(actors);
    }

    static class Actor{
        String name;
        int age;
        String sex;

        public Actor() {

        }

        @Override
        public String toString() {
            return "Actor{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", sex='" + sex + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

    public static void sortedStream(List<String> list){

        list.stream().sorted(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);

            }
        }).forEach(System.out::println);

        System.out.println("sortedStream out");

    }

    public static void filterStream(List<String> list) {
        list.stream().filter(s -> s.contains("a")).forEach(System.out::println);
        System.out.println("filterStream 1 out");
        list.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.startsWith("a");       // 开头是XXX
            }
        }).forEach(System.out::println);
        System.out.println("filterStream 2 out");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        numbers.stream().filter(s -> s % 2 == 0).forEach(System.out::println);

        System.out.println("filterStream 3 out");

        list.stream().filter(s -> s.length()>1 ).forEach(System.out::println);



        System.out.println("filterStream 4 out");
    }

    public static void limitStream(List<String> list) {
        list.stream().limit(1).forEach(System.out::println);
    }

    public static void skipStream(List<String> list) {
        list.stream().skip(3).forEach(System.out::println);
    }

    public static void concatStream(List<String> list, List<Integer> numbers) {
        Stream.concat(list.stream(), numbers.stream()).forEach(System.out::println);
    };

    private static void forEachStream(List<String> list) {
        list.stream().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s+"Yes");
            }
        });
    }

    private static void collectStream() {
        List<String> list = Arrays.asList("张三-18-男","李四-22-女","王五-23-男");

        List<String> streamList = list.stream().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                String[]  s2 = s.split("-");
                return s2[2].equals("男") && Integer.parseInt(s2[1]) > 20;
            }
        }).collect(Collectors.toList());

        System.out.println(streamList);
    }
}
